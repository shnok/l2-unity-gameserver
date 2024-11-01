package com.shnok.javaserver.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.data.sql.PlayerInfoTable;
import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.data.xml.PlayerData;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeShowInfoUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class HeroManager
{
	private static final CLogger LOGGER = new CLogger(HeroManager.class.getName());
	
	private static final String LOAD_HEROES = "SELECT heroes.char_id, characters.char_name, heroes.class_id, heroes.count, heroes.played, heroes.active FROM heroes, characters WHERE characters.obj_Id = heroes.char_id AND heroes.played = 1";
	private static final String LOAD_ALL_HEROES = "SELECT heroes.char_id, characters.char_name, heroes.class_id, heroes.count, heroes.played, heroes.active FROM heroes, characters WHERE characters.obj_Id = heroes.char_id";
	private static final String SELECT_HEROES_TO_BE = "SELECT olympiad_nobles.char_id, characters.char_name FROM olympiad_nobles, characters WHERE characters.obj_Id = olympiad_nobles.char_id AND olympiad_nobles.class_id = ? AND olympiad_nobles.competitions_done >= ? AND olympiad_nobles.competitions_won > 0 ORDER BY olympiad_nobles.olympiad_points DESC, olympiad_nobles.competitions_done DESC, olympiad_nobles.competitions_won DESC";
	private static final String RESET_PLAYED = "UPDATE heroes SET played = 0";
	private static final String INSERT_HERO = "INSERT INTO heroes (char_id, class_id, count, played, active) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE count=VALUES(count),played=VALUES(played),active=VALUES(active)";
	private static final String LOAD_CLAN_DATA = "SELECT characters.clanid AS clanid, coalesce(clan_data.ally_Id, 0) AS allyId FROM characters LEFT JOIN clan_data ON clan_data.clan_id = characters.clanid WHERE characters.obj_Id = ?";
	
	private static final String LOAD_MESSAGE = "SELECT message FROM heroes WHERE char_id=?";
	private static final String LOAD_DIARY = "SELECT * FROM  heroes_diary WHERE char_id=? ORDER BY time ASC";
	private static final String LOAD_FIGHTS = "SELECT * FROM olympiad_fights WHERE (charOneId=? OR charTwoId=?) AND start<? ORDER BY start ASC";
	
	private static final String UPDATE_DIARY = "INSERT INTO heroes_diary (char_id, time, action, param) values(?,?,?,?)";
	private static final String UPDATE_MESSAGE = "UPDATE heroes SET message=? WHERE char_id=?";
	private static final String DELETE_ITEMS = "DELETE FROM items WHERE item_id IN (6842, 6611, 6612, 6613, 6614, 6615, 6616, 6617, 6618, 6619, 6620, 6621) AND owner_id NOT IN (SELECT obj_Id FROM characters WHERE accesslevel > 0)";
	
	public static final String CHAR_ID = "char_id";
	public static final String CLASS_ID = "class_id";
	public static final String CHAR_NAME = "char_name";
	public static final String COUNT = "count";
	public static final String PLAYED = "played";
	public static final String CLAN_NAME = "clan_name";
	public static final String CLAN_CREST = "clan_crest";
	public static final String ALLY_NAME = "ally_name";
	public static final String ALLY_CREST = "ally_crest";
	public static final String ACTIVE = "active";
	
	public static final int ACTION_RAID_KILLED = 1;
	public static final int ACTION_HERO_GAINED = 2;
	public static final int ACTION_CASTLE_TAKEN = 3;
	
	private final Map<Integer, StatSet> _heroes = new HashMap<>();
	private final Map<Integer, StatSet> _completeHeroes = new HashMap<>();
	
	private final Map<Integer, StatSet> _heroCounts = new HashMap<>();
	private final Map<Integer, List<StatSet>> _heroFights = new HashMap<>();
	private final List<StatSet> _fights = new ArrayList<>();
	
	private final Map<Integer, List<StatSet>> _heroDiaries = new HashMap<>();
	private final Map<Integer, String> _heroMessages = new HashMap<>();
	private final List<StatSet> _diary = new ArrayList<>();
	
	protected HeroManager()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps2 = con.prepareStatement(LOAD_CLAN_DATA))
		{
			try (PreparedStatement ps = con.prepareStatement(LOAD_HEROES);
				ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final int objectId = rs.getInt(CHAR_ID);
					
					final StatSet hero = new StatSet();
					hero.set(CHAR_NAME, rs.getString(CHAR_NAME));
					hero.set(CLASS_ID, rs.getInt(CLASS_ID));
					hero.set(COUNT, rs.getInt(COUNT));
					hero.set(PLAYED, rs.getInt(PLAYED));
					hero.set(ACTIVE, rs.getInt(ACTIVE));
					
					loadFights(objectId);
					loadDiary(objectId);
					loadMessage(objectId);
					
					ps2.setInt(1, objectId);
					
					try (ResultSet rs2 = ps2.executeQuery())
					{
						if (rs2.next())
						{
							String clanName = "";
							String allyName = "";
							int clanCrest = 0;
							int allyCrest = 0;
							
							final int clanId = rs2.getInt("clanid");
							if (clanId > 0)
							{
								final Clan clan = ClanTable.getInstance().getClan(clanId);
								if (clan != null)
								{
									clanName = clan.getName();
									clanCrest = clan.getCrestId();
									
									final int allyId = rs2.getInt("allyId");
									if (allyId > 0)
									{
										allyName = clan.getAllyName();
										allyCrest = clan.getAllyCrestId();
									}
								}
							}
							
							hero.set(CLAN_CREST, clanCrest);
							hero.set(CLAN_NAME, clanName);
							hero.set(ALLY_CREST, allyCrest);
							hero.set(ALLY_NAME, allyName);
						}
					}
					ps2.clearParameters();
					
					_heroes.put(objectId, hero);
				}
			}
			
			try (PreparedStatement ps = con.prepareStatement(LOAD_ALL_HEROES);
				ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final int objectId = rs.getInt(CHAR_ID);
					
					final StatSet hero = new StatSet();
					hero.set(CHAR_NAME, rs.getString(CHAR_NAME));
					hero.set(CLASS_ID, rs.getInt(CLASS_ID));
					hero.set(COUNT, rs.getInt(COUNT));
					hero.set(PLAYED, rs.getInt(PLAYED));
					hero.set(ACTIVE, rs.getInt(ACTIVE));
					
					ps2.setInt(1, objectId);
					
					try (ResultSet rs2 = ps2.executeQuery())
					{
						if (rs2.next())
						{
							String clanName = "";
							String allyName = "";
							int clanCrest = 0;
							int allyCrest = 0;
							
							final int clanId = rs2.getInt("clanid");
							if (clanId > 0)
							{
								final Clan clan = ClanTable.getInstance().getClan(clanId);
								if (clan != null)
								{
									clanName = clan.getName();
									clanCrest = clan.getCrestId();
									
									final int allyId = rs2.getInt("allyId");
									if (allyId > 0)
									{
										allyName = clan.getAllyName();
										allyCrest = clan.getAllyCrestId();
									}
								}
							}
							
							hero.set(CLAN_CREST, clanCrest);
							hero.set(CLAN_NAME, clanName);
							hero.set(ALLY_CREST, allyCrest);
							hero.set(ALLY_NAME, allyName);
						}
					}
					ps2.clearParameters();
					
					_completeHeroes.put(objectId, hero);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load heroes.", e);
		}
		LOGGER.info("Loaded {} heroes and {} all time heroes.", _heroes.size(), _completeHeroes.size());
	}
	
	private static String calcFightTime(long fightTime)
	{
		String format = String.format("%%0%dd", 2);
		fightTime = fightTime / 1000;
		String seconds = String.format(format, fightTime % 60);
		String minutes = String.format(format, (fightTime % 3600) / 60);
		return minutes + ":" + seconds;
	}
	
	/**
	 * Restore hero message.
	 * @param objectId : The objectId of the hero.
	 */
	private void loadMessage(int objectId)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_MESSAGE))
		{
			ps.setInt(1, objectId);
			
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
					_heroMessages.put(objectId, rs.getString("message"));
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load hero message for: {}.", e, objectId);
		}
	}
	
	private void loadDiary(int objectId)
	{
		int entries = 0;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_DIARY))
		{
			ps.setInt(1, objectId);
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final long time = rs.getLong("time");
					final int action = rs.getInt("action");
					final int param = rs.getInt("param");
					
					final StatSet entry = new StatSet();
					entry.set("date", new SimpleDateFormat("yyyy-MM-dd HH").format(time));
					
					if (action == ACTION_RAID_KILLED)
					{
						final NpcTemplate template = NpcData.getInstance().getTemplate(param);
						if (template != null)
							entry.set("action", template.getName() + " was defeated");
					}
					else if (action == ACTION_HERO_GAINED)
						entry.set("action", "Gained Hero status");
					else if (action == ACTION_CASTLE_TAKEN)
					{
						final Castle castle = CastleManager.getInstance().getCastleById(param);
						if (castle != null)
							entry.set("action", castle.getName() + " Castle was successfuly taken");
					}
					_diary.add(entry);
					
					entries++;
				}
			}
			
			_heroDiaries.put(objectId, _diary);
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load hero diary for: {}.", e, objectId);
		}
		LOGGER.info("Loaded {} diary entries for hero: {}.", entries, PlayerInfoTable.getInstance().getPlayerName(objectId));
	}
	
	private void loadFights(int charId)
	{
		StatSet heroCountData = new StatSet();
		
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH, 1);
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE, 0);
		data.set(Calendar.MILLISECOND, 0);
		
		long from = data.getTimeInMillis();
		int numberOfFights = 0;
		int victories = 0;
		int losses = 0;
		int draws = 0;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_FIGHTS))
		{
			ps.setInt(1, charId);
			ps.setInt(2, charId);
			ps.setLong(3, from);
			
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					int charOneId = rset.getInt("charOneId");
					int charOneClass = rset.getInt("charOneClass");
					int charTwoId = rset.getInt("charTwoId");
					int charTwoClass = rset.getInt("charTwoClass");
					int winner = rset.getInt("winner");
					long start = rset.getLong("start");
					long time = rset.getLong("time");
					int classed = rset.getInt("classed");
					
					if (charId == charOneId)
					{
						String name = PlayerInfoTable.getInstance().getPlayerName(charTwoId);
						String cls = PlayerData.getInstance().getClassNameById(charTwoClass);
						if (name != null && cls != null)
						{
							StatSet fight = new StatSet();
							fight.set("oponent", name);
							fight.set("oponentclass", cls);
							
							fight.set("time", calcFightTime(time));
							fight.set("start", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(start));
							
							fight.set("classed", classed);
							if (winner == 1)
							{
								fight.set("result", "<font color=\"00ff00\">victory</font>");
								victories++;
							}
							else if (winner == 2)
							{
								fight.set("result", "<font color=\"ff0000\">loss</font>");
								losses++;
							}
							else if (winner == 0)
							{
								fight.set("result", "<font color=\"ffff00\">draw</font>");
								draws++;
							}
							
							_fights.add(fight);
							
							numberOfFights++;
						}
					}
					else if (charId == charTwoId)
					{
						String name = PlayerInfoTable.getInstance().getPlayerName(charOneId);
						String cls = PlayerData.getInstance().getClassNameById(charOneClass);
						if (name != null && cls != null)
						{
							StatSet fight = new StatSet();
							fight.set("oponent", name);
							fight.set("oponentclass", cls);
							
							fight.set("time", calcFightTime(time));
							fight.set("start", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(start));
							
							fight.set("classed", classed);
							if (winner == 1)
							{
								fight.set("result", "<font color=\"ff0000\">loss</font>");
								losses++;
							}
							else if (winner == 2)
							{
								fight.set("result", "<font color=\"00ff00\">victory</font>");
								victories++;
							}
							else if (winner == 0)
							{
								fight.set("result", "<font color=\"ffff00\">draw</font>");
								draws++;
							}
							
							_fights.add(fight);
							
							numberOfFights++;
						}
					}
				}
			}
			
			heroCountData.set("victory", victories);
			heroCountData.set("draw", draws);
			heroCountData.set("loss", losses);
			
			_heroCounts.put(charId, heroCountData);
			_heroFights.put(charId, _fights);
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load hero fights history for: {}.", e, charId);
		}
		LOGGER.info("Loaded {} fights for: {}.", numberOfFights, PlayerInfoTable.getInstance().getPlayerName(charId));
	}
	
	public Map<Integer, StatSet> getHeroes()
	{
		return _heroes;
	}
	
	public Map<Integer, StatSet> getAllHeroes()
	{
		return _completeHeroes;
	}
	
	public int getHeroByClass(int classId)
	{
		if (_heroes.isEmpty())
			return 0;
		
		for (Map.Entry<Integer, StatSet> hero : _heroes.entrySet())
		{
			if (hero.getValue().getInteger(CLASS_ID) == classId)
				return hero.getKey();
		}
		return 0;
	}
	
	public void resetData()
	{
		_heroDiaries.clear();
		_heroFights.clear();
		_heroCounts.clear();
		_heroMessages.clear();
	}
	
	public void showHeroDiary(Player player, int heroclass, int objectId, int page)
	{
		final List<StatSet> mainList = _heroDiaries.get(objectId);
		if (mainList == null)
			return;
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/olympiad/herodiary.htm");
		html.replace("%heroname%", PlayerInfoTable.getInstance().getPlayerName(objectId));
		html.replace("%message%", _heroMessages.get(objectId));
		html.disableValidation();
		
		if (!mainList.isEmpty())
		{
			List<StatSet> list = new ArrayList<>(mainList);
			Collections.reverse(list);
			
			boolean color = true;
			int counter = 0;
			int breakAt = 0;
			final int perPage = 10;
			
			final StringBuilder sb = new StringBuilder(500);
			for (int i = ((page - 1) * perPage); i < list.size(); i++)
			{
				breakAt = i;
				StatSet diaryEntry = list.get(i);
				StringUtil.append(sb, "<tr><td>", ((color) ? "<table width=270 bgcolor=\"131210\">" : "<table width=270>"), "<tr><td width=270><font color=\"LEVEL\">", diaryEntry.getString("date"), ":xx</font></td></tr><tr><td width=270>", diaryEntry.getString("action"), "</td></tr><tr><td>&nbsp;</td></tr></table></td></tr>");
				color = !color;
				
				counter++;
				if (counter >= perPage)
					break;
			}
			
			if (breakAt < (list.size() - 1))
				html.replace("%buttprev%", "<button value=\"Prev\" action=\"bypass _diary?class=" + heroclass + "&page=" + (page + 1) + "\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			else
				html.replace("%buttprev%", "");
			
			if (page > 1)
				html.replace("%buttnext%", "<button value=\"Next\" action=\"bypass _diary?class=" + heroclass + "&page=" + (page - 1) + "\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			else
				html.replace("%buttnext%", "");
			
			html.replace("%list%", sb.toString());
		}
		else
		{
			html.replace("%list%", "");
			html.replace("%buttprev%", "");
			html.replace("%buttnext%", "");
		}
		player.sendPacket(html);
	}
	
	public void showHeroFights(Player player, int heroclass, int objectId, int page)
	{
		final List<StatSet> list = _heroFights.get(objectId);
		if (list == null)
			return;
		
		int win = 0;
		int loss = 0;
		int draw = 0;
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/olympiad/herohistory.htm");
		html.replace("%heroname%", PlayerInfoTable.getInstance().getPlayerName(objectId));
		html.disableValidation();
		
		if (!list.isEmpty())
		{
			final StatSet heroCount = _heroCounts.get(objectId);
			if (heroCount != null)
			{
				win = heroCount.getInteger("victory");
				loss = heroCount.getInteger("loss");
				draw = heroCount.getInteger("draw");
			}
			
			boolean color = true;
			int counter = 0;
			int breakat = 0;
			final int perpage = 20;
			
			final StringBuilder sb = new StringBuilder(500);
			for (int i = ((page - 1) * perpage); i < list.size(); i++)
			{
				breakat = i;
				StatSet fight = list.get(i);
				StringUtil.append(sb, "<tr><td>", ((color) ? "<table width=270 bgcolor=\"131210\">" : "<table width=270><tr><td width=220><font color=\"LEVEL\">"), fight.getString("start"), "</font>&nbsp;&nbsp;", fight.getString("result"), "</td><td width=50 align=right>", ((fight.getInteger("classed") > 0) ? "<font color=\"FFFF99\">cls</font>" : "<font color=\"999999\">non-cls<font>"), "</td></tr><tr><td width=220>vs ", fight.getString("oponent"), " (", fight.getString("oponentclass"), ")</td><td width=50 align=right>(", fight.getString("time"), ")</td></tr><tr><td colspan=2>&nbsp;</td></tr></table></td></tr>");
				color = !color;
				
				counter++;
				if (counter >= perpage)
					break;
			}
			
			if (breakat < (list.size() - 1))
				html.replace("%buttprev%", "<button value=\"Prev\" action=\"bypass _match?class=" + heroclass + "&page=" + (page + 1) + "\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			else
				html.replace("%buttprev%", "");
			
			if (page > 1)
				html.replace("%buttnext%", "<button value=\"Next\" action=\"bypass _match?class=" + heroclass + "&page=" + (page - 1) + "\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
			else
				html.replace("%buttnext%", "");
			
			html.replace("%list%", sb.toString());
		}
		else
		{
			html.replace("%list%", "");
			html.replace("%buttprev%", "");
			html.replace("%buttnext%", "");
		}
		
		html.replace("%win%", win);
		html.replace("%draw%", draw);
		html.replace("%loos%", loss);
		
		player.sendPacket(html);
	}
	
	public synchronized void computeNewHeroes()
	{
		// Reset heroes played variable.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(RESET_PLAYED))
		{
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't reset heroes.", e);
		}
		
		// If heroes exist, do special operations on them before computing new heroes.
		for (StatSet set : _heroes.values())
		{
			final Player worldPlayer = World.getInstance().getPlayer(set.getString(CHAR_NAME));
			if (worldPlayer == null)
				continue;
			
			// Unset the Player as Hero.
			worldPlayer.setHero(false);
			
			// Unequip Hero items, if found.
			for (ItemInstance item : worldPlayer.getInventory().getPaperdollItems())
			{
				if (item.isHeroItem())
					worldPlayer.useEquippableItem(item, true);
			}
			
			// Check inventory and delete Hero items.
			for (ItemInstance item : worldPlayer.getInventory().getAvailableItems(false, true, false))
			{
				if (!item.isHeroItem())
					continue;
				
				worldPlayer.destroyItem(item, true);
			}
			
			worldPlayer.broadcastUserInfo();
		}
		
		// Compute new heroes.
		final List<StatSet> newHeroes = new ArrayList<>();
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_HEROES_TO_BE))
		{
			for (ClassId id : ClassId.VALUES)
			{
				if (id.getLevel() != 3)
					continue;
				
				ps.setInt(1, id.getId());
				ps.setInt(2, Config.OLY_MIN_MATCHES);
				
				try (ResultSet rs = ps.executeQuery())
				{
					if (rs.next())
					{
						final StatSet hero = new StatSet();
						hero.set(CLASS_ID, id.getId());
						hero.set(CHAR_ID, rs.getInt(CHAR_ID));
						hero.set(CHAR_NAME, rs.getString(CHAR_NAME));
						
						newHeroes.add(hero);
					}
					ps.clearParameters();
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load heroes to be.", e);
		}
		
		if (newHeroes.isEmpty())
		{
			_heroes.clear();
			return;
		}
		
		final Map<Integer, StatSet> heroes = new HashMap<>();
		
		for (StatSet hero : newHeroes)
		{
			final int objectId = hero.getInteger(CHAR_ID);
			
			StatSet set = _completeHeroes.get(objectId);
			if (set != null)
			{
				set.set(COUNT, set.getInteger(COUNT) + 1);
				set.set(PLAYED, 1);
				set.set(ACTIVE, 0);
			}
			else
			{
				set = new StatSet();
				set.set(CHAR_NAME, hero.getString(CHAR_NAME));
				set.set(CLASS_ID, hero.getInteger(CLASS_ID));
				set.set(COUNT, 1);
				set.set(PLAYED, 1);
				set.set(ACTIVE, 0);
			}
			heroes.put(objectId, set);
		}
		
		// Delete hero items.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_ITEMS))
		{
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't delete hero items.", e);
		}
		
		_heroes.clear();
		_heroes.putAll(heroes);
		
		updateHeroes();
	}
	
	private void updateHeroes()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_HERO))
		{
			for (Map.Entry<Integer, StatSet> heroEntry : _heroes.entrySet())
			{
				final int heroId = heroEntry.getKey();
				final StatSet hero = heroEntry.getValue();
				
				ps.setInt(1, heroId);
				ps.setInt(2, hero.getInteger(CLASS_ID));
				ps.setInt(3, hero.getInteger(COUNT));
				ps.setInt(4, hero.getInteger(PLAYED));
				ps.setInt(5, hero.getInteger(ACTIVE));
				ps.addBatch();
				
				if (!_completeHeroes.containsKey(heroId))
				{
					try (PreparedStatement ps2 = con.prepareStatement(LOAD_CLAN_DATA))
					{
						ps2.setInt(1, heroId);
						
						try (ResultSet rs2 = ps2.executeQuery())
						{
							if (rs2.next())
							{
								String clanName = "";
								String allyName = "";
								int clanCrest = 0;
								int allyCrest = 0;
								
								final int clanId = rs2.getInt("clanid");
								if (clanId > 0)
								{
									final Clan clan = ClanTable.getInstance().getClan(clanId);
									if (clan != null)
									{
										clanName = clan.getName();
										clanCrest = clan.getCrestId();
										
										final int allyId = rs2.getInt("allyId");
										if (allyId > 0)
										{
											allyName = clan.getAllyName();
											allyCrest = clan.getAllyCrestId();
										}
									}
								}
								
								hero.set(CLAN_CREST, clanCrest);
								hero.set(CLAN_NAME, clanName);
								hero.set(ALLY_CREST, allyCrest);
								hero.set(ALLY_NAME, allyName);
							}
						}
					}
					
					_heroes.put(heroId, hero);
					_completeHeroes.put(heroId, hero);
				}
			}
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't update heroes.", e);
		}
	}
	
	public void setHeroGained(int objectId)
	{
		setDiaryData(objectId, ACTION_HERO_GAINED, 0);
	}
	
	public void setRBkilled(int objectId, int npcId)
	{
		setDiaryData(objectId, ACTION_RAID_KILLED, npcId);
		
		final NpcTemplate template = NpcData.getInstance().getTemplate(npcId);
		if (template == null)
			return;
		
		// Get Data
		final List<StatSet> list = _heroDiaries.get(objectId);
		if (list == null)
			return;
		
		// Clear old data
		_heroDiaries.remove(objectId);
		
		// Prepare new data
		StatSet entry = new StatSet();
		entry.set("date", new SimpleDateFormat("yyyy-MM-dd HH").format(System.currentTimeMillis()));
		entry.set("action", template.getName() + " was defeated");
		
		// Add to old list
		list.add(entry);
		
		// Put new list into diary
		_heroDiaries.put(objectId, list);
	}
	
	public void setCastleTaken(int objectId, int castleId)
	{
		setDiaryData(objectId, ACTION_CASTLE_TAKEN, castleId);
		
		final Castle castle = CastleManager.getInstance().getCastleById(castleId);
		if (castle == null)
			return;
		
		// Get Data
		final List<StatSet> list = _heroDiaries.get(objectId);
		if (list == null)
			return;
		
		// Clear old data
		_heroDiaries.remove(objectId);
		
		// Prepare new data
		final StatSet entry = new StatSet();
		entry.set("date", new SimpleDateFormat("yyyy-MM-dd HH").format(System.currentTimeMillis()));
		entry.set("action", castle.getName() + " Castle was successfuly taken");
		
		// Add to old list
		list.add(entry);
		
		// Put new list into diary
		_heroDiaries.put(objectId, list);
	}
	
	public void setDiaryData(int objectId, int action, int param)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_DIARY))
		{
			ps.setInt(1, objectId);
			ps.setLong(2, System.currentTimeMillis());
			ps.setInt(3, action);
			ps.setInt(4, param);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save diary data for {}.", e, objectId);
		}
	}
	
	/**
	 * Set new hero message for hero
	 * @param player the player instance
	 * @param message String to set
	 */
	public void setHeroMessage(Player player, String message)
	{
		_heroMessages.put(player.getObjectId(), message);
	}
	
	/**
	 * Update hero message in database
	 * @param objectId : The Player objectId.
	 */
	public void saveHeroMessage(int objectId)
	{
		final String message = _heroMessages.get(objectId);
		if (message == null)
			return;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_MESSAGE))
		{
			ps.setString(1, message);
			ps.setInt(2, objectId);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save hero message for {}.", e, objectId);
		}
	}
	
	/**
	 * Save all hero messages to DB.
	 */
	public void shutdown()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_MESSAGE))
		{
			for (Map.Entry<Integer, String> entry : _heroMessages.entrySet())
			{
				ps.setString(1, entry.getValue());
				ps.setInt(2, entry.getKey());
				ps.addBatch();
			}
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save hero messages upon shutdown.", e);
		}
	}
	
	public boolean isActiveHero(int id)
	{
		final StatSet entry = _heroes.get(id);
		
		return entry != null && entry.getInteger(ACTIVE) == 1;
	}
	
	public boolean isInactiveHero(int id)
	{
		final StatSet entry = _heroes.get(id);
		
		return entry != null && entry.getInteger(ACTIVE) == 0;
	}
	
	public void activateHero(Player player)
	{
		final StatSet hero = _heroes.get(player.getObjectId());
		if (hero == null)
			return;
		
		hero.set(ACTIVE, 1);
		
		player.setHero(true);
		player.broadcastPacket(new SocialAction(player, 16));
		player.broadcastUserInfo();
		
		final Clan clan = player.getClan();
		if (clan != null && clan.getLevel() >= 5)
		{
			clan.addReputationScore(1000);
			clan.broadcastToMembers(new PledgeShowInfoUpdate(clan), SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_BECAME_HERO_AND_GAINED_S2_REPUTATION_POINTS).addString(hero.getString("char_name")).addNumber(1000));
		}
		
		// Set Gained hero and reload data
		setHeroGained(player.getObjectId());
		loadFights(player.getObjectId());
		loadDiary(player.getObjectId());
		
		_heroMessages.put(player.getObjectId(), "");
		
		updateHeroes();
	}
	
	public static HeroManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HeroManager INSTANCE = new HeroManager();
	}
}