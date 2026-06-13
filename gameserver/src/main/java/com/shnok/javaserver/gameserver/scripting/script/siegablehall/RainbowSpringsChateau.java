package com.shnok.javaserver.gameserver.scripting.script.siegablehall;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.cache.HtmCache;
import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.RestartType;
import com.shnok.javaserver.gameserver.enums.SiegeStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHallSiege;
import com.shnok.javaserver.gameserver.model.residence.clanhall.SiegableHall;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

/**
 * Rainbow Springs Chateau is located north of the Hot Springs area. A new style of siege war has been applied to it, which is different from previous contested clan hall war styles.<br>
 * <br>
 * In order to own the clan hall, winning is decided through a mini-game. Only a clan level 3 or higher is allowed to participate and the maximum number of clans is four. Participants may have 5 or more member in their party.<br>
 * <br>
 * <b>How to Participate</b><br>
 * <br>
 * A clan leader in possession of a 'Rainbow Springs Clan Hall War Decree' may register to participate through a messenger NPC up to one hour before the contest begins. The top four clans, owning the highest number of Rainbow Springs Clan Hall War Decrees amongst them, will be chosen to compete and
 * their clan leaders notified through a system message.<br>
 * <br>
 * Rainbow Springs Clan Hall War Decrees may be obtained by fishing in the Hot Springs area of Goddard. However, a player must use Hot Springs Bait for fishing bait.<br>
 * <br>
 * If a participating clan requests disorganization in the middle of a siege war registration period, the siege war participation registration is automatically cancelled. However, the certificates are not returned at this time.<br>
 * <br>
 * A participating clan may cancel its participation up until the end of the registration period. If this happens, the clan will only get back half of their submitted certificates.<br>
 * <br>
 * The clan that owns the Rainbow Springs Chateau is automatically registered for the next contest. In this case, the top three clans, owning the highest number of Rainbow Springs Clan Hall War Decrees amongst them, will be chosen to compete.<br>
 * <br>
 * <b>Battle Method</b><br>
 * <br>
 * A Rainbow Springs Chateau siege war is held at the east arena of Rainbow Springs.<br>
 * <br>
 * After clans are selected to compete, the contest is comprised of a one-hour waiting period, a contest time of 58 minutes, and an end time of 2 minutes.<br>
 * <br>
 * Registered clans must organize one party with five or more members, with their clan leader as the party leader. The party must move to the arena before the siege war starts and enter the arena through the Caretaker. The surrounding area of the arena is a Peaceful Zone.<br>
 * <br>
 * Participating clans enter into different game fields respectively and through the Caretaker.<br>
 * <br>
 * Upon entering the arena, all buffs that are active on all participating characters will be removed, and summoned pet/servitors will disappear and cannot be summoned back into the arena.<br>
 * <br>
 * Once the contest begins, a Hot Springs Yeti and a Hot Springs Gourd appear in the center of the arena and treasure chests appear randomly throughout the arena.<br>
 * <br>
 * The Hot Springs Yeti announces the game rules and helps guide players.<br>
 * <br>
 * All of the Hot Springs Yeti's shouts may be collected and then exchanged with/for various Hot Springs items. Text exchange or Hot Springs item use is only available to the clan leader and clan members.<br>
 * <br>
 * Participants can break the spawned treasure chests in the arena and can acquire text passages. Text items cannot be exchanged; therefore to give them to other characters, they should be dropped first and then picked up.<br>
 * <br>
 * Treasure Chests can only be damaged with your bare hands, however Mimics can be attacked with P. Atk and M. Atk.<br>
 * <br>
 * When using Hot Springs Nectar, an Enraged Yeti is spawned at certain intervals and interrupts the game. The Enraged Yeti can be attacked with P. Atk. and M. Atk.<br>
 * <br>
 * The Hot Springs items may be used when the Hot Springs Yeti is a target, and they can reduce the HP of the gourd, cast debuff on other teams, change the gourd, or recover.<br>
 * <br>
 * The team that first destroys the Hot Springs Gourd wins the game. All the participants are then teleported outside of the arena after a 2-minute delay.<br>
 * <br>
 * At the end of the game, a manager NPC appears and cleans the arena by picking up all the item drops.<br>
 * <br>
 * The game continues even if the server goes down in the middle of the mini-game.
 */
public final class RainbowSpringsChateau extends ClanHallSiege
{
	private static final String SELECT_ATTACKERS = "SELECT * FROM rainbowsprings_attacker_list";
	private static final String INSERT_ATTACKER = "INSERT INTO rainbowsprings_attacker_list VALUES (?,?)";
	private static final String DELETE_ATTACKER = "DELETE FROM rainbowsprings_attacker_list WHERE clanId = ?";
	private static final String DELETE_ALL_ATTACKERS = "DELETE FROM rainbowsprings_attacker_list";
	
	private static final int MIN_PARTY_COUNT = 5;
	private static final int MIN_CLAN_MEMBER_COUNT = 5;
	private static final int WAIT_TIME = 3600000;
	
	private static final int RAINBOW_SPRINGS = 62;
	
	private static final int WAR_DECREES = 8034;
	
	private static final int MESSENGER = 35604;
	private static final int CARETAKER = 35603;
	
	private static final int[] GOURDS =
	{
		35588,
		35589,
		35590,
		35591
	};
	
	private static final Location[] ARENAS = new Location[]
	{
		new Location(151562, -127080, -2214), // Arena 1
		new Location(153141, -125335, -2214), // Arena 2
		new Location(153892, -127530, -2214), // Arena 3
		new Location(155657, -125752, -2214), // Arena 4
	};
	
	private final Map<Integer, Integer> _warDecrees = new ConcurrentHashMap<>();
	private final List<Clan> _acceptedClans = new CopyOnWriteArrayList<>();
	
	private SiegableHall _rainbow;
	
	private ScheduledFuture<?> _nextSiege;
	private ScheduledFuture<?> _siegeEnd;
	
	private String _registrationEnds;
	
	private Clan _winner;
	
	private Npc _caretaker;
	
	public RainbowSpringsChateau()
	{
		super("siegablehall", RAINBOW_SPRINGS);
		
		_rainbow = ClanHallManager.getInstance().getSiegableHall(RAINBOW_SPRINGS);
		if (_rainbow == null)
			return;
		
		loadAttackers();
		
		final long delay = _rainbow.getNextSiegeTime() - System.currentTimeMillis();
		if (delay > -1)
		{
			setRegistrationEndString(_rainbow.getNextSiegeTime());
			_nextSiege = ThreadPool.schedule(this::setFinalAttackers, delay);
		}
		else
			LOGGER.warn("No date was set for Rainbow Springs Chateau siege. Siege is canceled.");
		
		addCreated(CARETAKER);
		addFirstTalkId(MESSENGER, CARETAKER);
		addTalkId(MESSENGER, CARETAKER);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		_caretaker = npc;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		String html = "";
		
		final int npcId = npc.getNpcId();
		if (npcId == MESSENGER)
		{
			final String main = (_rainbow.getOwnerId() > 0) ? "messenger_yetti001.htm" : "messenger_yetti001a.htm";
			html = HtmCache.getInstance().getHtm("data/html/script/siegablehall/RainbowSpringsChateau/" + main);
			html = html.replace("%time%", _registrationEnds);
			
			if (_rainbow.getOwnerId() > 0)
				html = html.replace("%owner%", ClanTable.getInstance().getClan(_rainbow.getOwnerId()).getName());
		}
		else if (npcId == CARETAKER)
			html = (_rainbow.isWaitingBattle()) ? "game_manager001.htm" : "game_manager003.htm";
		
		return html;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String html = event;
		
		final Clan clan = player.getClan();
		
		switch (npc.getNpcId())
		{
			case MESSENGER:
				switch (event)
				{
					case "register":
						if (!player.isClanLeader())
							html = "messenger_yetti010.htm";
						else if (clan.getLevel() < 3 || clan.getMembersCount() < MIN_CLAN_MEMBER_COUNT)
							html = "messenger_yetti011.htm";
						else if (clan.getClanHallId() > 0)
							html = "messenger_yetti012.htm";
						else if (!_rainbow.isRegistering())
							html = "messenger_yetti014.htm";
						else if (_warDecrees.containsKey(clan.getClanId()))
							html = "messenger_yetti013.htm";
						else
						{
							final ItemInstance warDecrees = player.getInventory().getItemByItemId(WAR_DECREES);
							if (warDecrees == null)
								html = "messenger_yetti008.htm";
							else
							{
								html = "messenger_yetti009.htm";
								
								// Register the Clan in attacker side.
								addAttacker(clan.getClanId(), warDecrees.getCount());
								
								// Destroy all war decrees.
								player.destroyItem(warDecrees, true);
							}
						}
						break;
					
					case "cancel":
						if (!player.isClanLeader())
							html = "messenger_yetti010.htm";
						else if (!_warDecrees.containsKey(clan.getClanId()))
							html = "messenger_yetti016.htm";
						else if (!_rainbow.isRegistering() || _rainbow.isWaitingBattle())
							html = "messenger_yetti017.htm";
						else
						{
							final Integer warDecrees = _warDecrees.remove(clan.getClanId());
							if (warDecrees != null)
							{
								html = "messenger_yetti019.htm";
								
								// Return back half war decrees to the Player.
								giveItems(player, WAR_DECREES, warDecrees / 2);
							}
							
							// Remove the Clan from accepted Clans.
							_acceptedClans.remove(clan);
							
							// Unregister the Clan from attacker side.
							removeAttacker(clan.getClanId());
						}
						break;
				}
				break;
			
			case CARETAKER:
				if (event.equals("portToArena"))
				{
					final Party party = player.getParty();
					if (clan == null)
						html = "game_manager009.htm";
					else if (!player.isClanLeader())
						html = "game_manager004.htm";
					else if (!player.isInParty())
						html = "game_manager005.htm";
					else if (party.getLeaderObjectId() != player.getObjectId())
						html = "game_manager006.htm";
					else if (party.getMembers().stream().anyMatch(p -> p.getClanId() != player.getClanId()))
						html = "game_manager007.htm";
					else if (party.getMembersCount() < MIN_PARTY_COUNT)
						html = "game_manager008.htm";
					else if (clan.getClanHallId() > 0)
						html = "game_manager010.htm";
					else if (clan.getLevel() < Config.CH_MINIMUM_CLAN_LEVEL)
						html = "game_manager011.htm";
					else if (clan.getMembersCount() < MIN_CLAN_MEMBER_COUNT)
						html = "game_manager012.htm";
					else if (!_acceptedClans.contains(clan))
					{
						if (!_warDecrees.containsKey(clan.getClanId()))
							html = "game_manager014.htm";
						else
							html = "game_manager015.htm";
					}
					else
					{
						final int index = _acceptedClans.indexOf(clan);
						if (index < 0 || index > 3)
							return html;
						
						for (Player member : player.getParty().getMembers())
						{
							member.stopAllEffects();
							
							final Summon summon = member.getSummon();
							if (summon != null)
								summon.unSummon(member);
							
							member.teleToLocation(ARENAS[index]);
						}
					}
				}
				break;
		}
		
		return html;
	}
	
	@Override
	public Clan getWinner()
	{
		return _winner;
	}
	
	@Override
	public void startSiege()
	{
		// Do nothing.
	}
	
	@Override
	public void endSiege()
	{
		if (_siegeEnd != null)
		{
			_siegeEnd.cancel(false);
			_siegeEnd = null;
		}
		ThreadPool.execute(() -> siegeEnd(null));
	}
	
	@Override
	public void spawnNpcs()
	{
		SpawnManager.getInstance().startSpawnTime("agit_defend_warfare_start", "62", null, null, true);
		
		_caretaker.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_OF_S1_BEGUN).addFortId(RAINBOW_SPRINGS), 8000);
	}
	
	@Override
	public void unspawnNpcs()
	{
		// Do nothing
	}
	
	@Override
	public void loadAttackers()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_ATTACKERS);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
				_warDecrees.put(rs.getInt("clanId"), rs.getInt("war_decrees_count"));
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load attackers.", e);
		}
	}
	
	private void addAttacker(int clanId, int count)
	{
		_warDecrees.put(clanId, count);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_ATTACKER))
		{
			ps.setInt(1, clanId);
			ps.setInt(2, count);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't add attacker.", e);
		}
	}
	
	private void removeAttacker(int clanId)
	{
		_warDecrees.remove(clanId);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_ATTACKER))
		{
			ps.setInt(1, clanId);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't remove attacker.", e);
		}
	}
	
	private void clearAttackers()
	{
		_warDecrees.clear();
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_ALL_ATTACKERS))
		{
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't clear attackers.", e);
		}
	}
	
	private void setRegistrationEndString(long time)
	{
		final Calendar c = Calendar.getInstance();
		c.setTime(new Date(time));
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR);
		int mins = c.get(Calendar.MINUTE);
		
		_registrationEnds = year + "-" + month + "-" + day + " " + hour + (mins < 10 ? ":0" : ":") + mins;
	}
	
	private void setFinalAttackers()
	{
		// Test ownership.
		if (_rainbow.getOwnerId() > 0)
		{
			final Clan owner = ClanTable.getInstance().getClan(_rainbow.getOwnerId());
			if (owner != null)
			{
				// Add automatically the old owner into accepted Clans.
				_acceptedClans.add(owner);
			}
		}
		
		// Sort war decrees, then reverse order. For each entry, we test if the Clan can be accepted, up to 4.
		_warDecrees.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey).forEachOrdered(clanId ->
		{
			final Clan clan = ClanTable.getInstance().getClan(clanId);
			
			if (_acceptedClans.size() > 3)
			{
				final Player leader = clan.getLeader().getPlayerInstance();
				leader.sendPacket(SystemMessageId.CLANHALL_WAR_REGISTRATION_FAILED);
				
				return;
			}
			
			if (clan == null || clan.getDissolvingExpiryTime() > 0)
				_warDecrees.remove(clanId);
			else
			{
				_acceptedClans.add(clan);
			}
		});
		
		// If final list of Clans is at least 2, we schedule the siege start.
		if (_acceptedClans.size() >= 2)
		{
			scheduleAnnoucements(WAIT_TIME);
			
			if (_nextSiege != null)
				_nextSiege.cancel(false);
			
			_nextSiege = ThreadPool.schedule(this::siegeStart, WAIT_TIME);
			
			_rainbow.updateSiegeStatus(SiegeStatus.REGISTRATION_OVER);
			_rainbow.getSiege().getAttackerClans().clear();
			
			for (Clan ac : _acceptedClans)
				_rainbow.addAttacker(ac);
		}
		// If not, the siege is aborted and reported. Registrations are anew opened.
		else
		{
			_rainbow.updateNextSiege();
			
			if (_nextSiege != null)
				_nextSiege.cancel(false);
			
			_nextSiege = ThreadPool.schedule(this::setFinalAttackers, _rainbow.getNextSiegeTime() - System.currentTimeMillis());
			
			_rainbow.updateSiegeStatus(SiegeStatus.REGISTRATION_OPENED);
			_rainbow.getSiege().getAttackerClans().clear();
			
			sendSMToAssociatedPlayersExceptOwner(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_WAR_REGISTRATION_PERIOD_ENDED));
			sendSMToAssociatedPlayers(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_WAR_CANCELLED));
			sendSMToAssociatedPlayers(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_S1_DRAW).addFortId(RAINBOW_SPRINGS));
			
			// Clear decrees
			clearAttackers();
			_acceptedClans.clear();
			
			if (_rainbow.getOwnerId() > 0)
			{
				final Clan owner = ClanTable.getInstance().getClan(_rainbow.getOwnerId());
				if (owner != null)
				{
					// Free the Siegable Hall and re-set owner.
					_rainbow.free();
					_rainbow.setOwner(owner);
				}
			}
		}
	}
	
	private void siegeStart()
	{
		if (_rainbow.getOwnerId() > 0)
		{
			final Clan owner = ClanTable.getInstance().getClan(_rainbow.getOwnerId());
			if (owner != null)
			{
				// Free the Siegable Hall.
				_rainbow.free();
			}
		}
		
		sendSMToAssociatedPlayers(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_OF_S1_BEGUN).addFortId(RAINBOW_SPRINGS));
		
		// Set the status as IN PROGRESS.
		_rainbow.updateSiegeStatus(SiegeStatus.IN_PROGRESS);
		
		// Set the siege end task.
		_siegeEnd = ThreadPool.schedule(() -> siegeEnd(null), _rainbow.getSiegeLength() - 120000);
		
		// Spawn NPCs.
		spawnNpcs();
	}
	
	private void siegeEnd(Clan winner)
	{
		if (_winner != null)
			_rainbow.setOwner(_winner);
		else
			sendSMToAssociatedPlayers(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_S1_DRAW).addFortId(RAINBOW_SPRINGS));
		
		final NpcMaker rainbowMaker = SpawnManager.getInstance().getNpcMaker("godard06_npc2414_02m1");
		rainbowMaker.getMaker().onMakerScriptEvent("onSiegeEnd", rainbowMaker, 0, 0);
		
		SpawnManager.getInstance().stopSpawnTime("agit_defend_warfare_start", "62", null, null, true);
		
		_rainbow.updateNextSiege();
		
		_nextSiege.cancel(false);
		_nextSiege = ThreadPool.schedule(this::setFinalAttackers, _rainbow.getNextSiegeTime() - System.currentTimeMillis());
		
		_rainbow.updateSiegeStatus(SiegeStatus.REGISTRATION_OPENED);
		_rainbow.getSiege().getAttackerClans().clear();
		
		setRegistrationEndString(_rainbow.getNextSiegeTime());
		
		// Clear decrees
		clearAttackers();
		_acceptedClans.clear();
		
		// Teleport out of the arenas is made 2 mins after game ends
		ThreadPool.schedule(() ->
		{
			for (Creature chr : ClanHallManager.getInstance().getSiegableHall(RAINBOW_SPRINGS).getSiegeZone().getCreatures())
				chr.teleportTo(RestartType.TOWN);
		}, 120000);
	}
	
	private void scheduleAnnoucements(long delay)
	{
		final int[] minAnnoucements =
		{
			50,
			40,
			30,
			20,
			10,
			5,
			3,
			2,
			1
		};
		final int[] secAnnoucements =
		{
			10,
			9,
			8,
			7,
			6,
			5,
			4,
			3,
			2,
			1
		};
		
		ThreadPool.schedule(() ->
		{
			sendSMToAssociatedPlayers(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_WAR_REGISTRATION_PERIOD_ENDED));
			sendSMToAssociatedPlayers(SystemMessage.getSystemMessage(SystemMessageId.REGISTERED_FOR_CLANHALL_WAR));
		}, delay - 3600 * 1000);
		
		for (int mins : minAnnoucements)
			ThreadPool.schedule(() -> sendSMToAssociatedPlayers(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_WAR_BEGINS_IN_S1_MINUTES).addNumber(mins)), delay - mins * 60 * 1000);
		
		for (int secs : secAnnoucements)
			ThreadPool.schedule(() -> sendSMToAssociatedPlayers(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_WAR_BEGINS_IN_S1_SECONDS).addNumber(secs)), delay - secs * 1000);
	}
	
	public void setMiniGameWinner(Npc killedGourd, Player winner)
	{
		if (!_rainbow.isInSiege())
			return;
		
		final Clan clan = winner.getClan();
		if (clan == null)
			return;
		
		final int index = _acceptedClans.indexOf(clan);
		if (index == -1)
			return;
		
		if (killedGourd.getNpcId() == GOURDS[index])
		{
			if (_siegeEnd != null)
			{
				_siegeEnd.cancel(false);
				_siegeEnd = null;
			}
			ThreadPool.execute(() -> siegeEnd(clan));
		}
	}
	
	public List<Player> getAllAssociatedPlayers(boolean excludeOwner)
	{
		List<Player> playerList = new ArrayList<>();
		
		for (Clan cl : _acceptedClans)
		{
			if (excludeOwner && _hall.getOwnerId() > 0 && _hall.getOwnerId() == cl.getClanId())
				continue;
			
			playerList.addAll(Arrays.asList(cl.getOnlineMembers()));
		}
		
		return playerList;
	}
	
	public void sendSMToAssociatedPlayers(SystemMessage sm)
	{
		for (Player pl : getAllAssociatedPlayers(false))
			pl.sendPacket(sm);
	}
	
	public void sendSMToAssociatedPlayersExceptOwner(SystemMessage sm)
	{
		for (Player pl : getAllAssociatedPlayers(true))
			pl.sendPacket(sm);
	}
}