package com.shnok.javaserver.gameserver.scripting.script.siegablehall;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.RestartType;
import com.shnok.javaserver.gameserver.enums.SiegeStatus;
import com.shnok.javaserver.gameserver.enums.SpawnType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHallSiege;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.spawn.Spawn;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public abstract class FlagWar extends ClanHallSiege
{
	private static final String SELECT_ATTACKERS = "SELECT * FROM clanhall_flagwar_attackers WHERE clanhall_id = ?";
	private static final String INSERT_ATTACKERS = "INSERT INTO clanhall_flagwar_attackers VALUES(?,?,?,?)";
	private static final String DELETE_ATTACKERS = "DELETE FROM clanhall_flagwar_attackers WHERE clanhall_id = ?";
	private static final String UPDATE_ATTACKERS_NPC = "UPDATE clanhall_flagwar_attackers SET npc = ? WHERE clan_id = ?";
	
	private static final String SELECT_MEMBERS = "SELECT object_id FROM clanhall_flagwar_members WHERE clan_id = ?";
	private static final String INSERT_MEMBERS = "INSERT INTO clanhall_flagwar_members VALUES (?,?,?)";
	private static final String DELETE_MEMBERS = "DELETE FROM clanhall_flagwar_members WHERE clanhall_id = ?";
	
	private static final String SELECT_OWNER_NPC = "SELECT * FROM clanhall_flagwar_owner_npcs WHERE clanhall_id = ?";
	private static final String INSERT_OWNER_NPC = "INSERT INTO clanhall_flagwar_owner_npcs VALUES (?,?,?)";
	private static final String DELETE_OWNER_NPC = "DELETE FROM clanhall_flagwar_owner_npcs WHERE clan_id = ?";
	
	protected String MAKER_NAME;
	
	protected int ROYAL_FLAG;
	protected int FLAG_RED;
	protected int FLAG_YELLOW;
	protected int FLAG_GREEN;
	protected int FLAG_BLUE;
	protected int FLAG_PURPLE;
	
	protected int ALLY_1;
	protected int ALLY_2;
	protected int ALLY_3;
	protected int ALLY_4;
	protected int ALLY_5;
	
	protected int TELEPORT_1;
	
	protected int MESSENGER;
	
	protected int[] OUTTER_DOORS;
	protected int[] INNER_DOORS;
	protected SpawnLocation[] FLAG_COORDS;
	
	protected SpawnLocation CENTER;
	
	protected int _ownersNpcId;
	
	private Npc _messenger;
	protected Map<Integer, ClanData> _data;
	protected Clan _winner;
	private boolean _firstPhase = true;
	private List<Npc> _npcSpawns = new ArrayList<>();
	private List<Npc> _tentSpawns = new ArrayList<>();
	private List<Npc> _clanlessNpcSpawns = new ArrayList<>();
	private List<Npc> _clanlessTentSpawns = new ArrayList<>();
	
	protected FlagWar(String name, final int hallId)
	{
		super("siegablehall", hallId);
		
		_hall.setSiege(this);
		
		// If siege ends w/ more than 1 flag alive, winner is old owner
		_winner = ClanTable.getInstance().getClan(_hall.getOwnerId());
	}
	
	public abstract String getFlagHtml(int flag);
	
	public abstract String getAllyHtml(int ally);
	
	public Location getClanRestartPoint(Clan clan)
	{
		if (clan == null)
			return null;
		
		final ClanData cd = _data.get(clan.getClanId());
		
		if (cd == null)
			return null;
		
		final int restartPoint = cd.flag - ROYAL_FLAG;
		final int restartPointNpcId = TELEPORT_1 + restartPoint;
		
		final Npc restartPointNpc = World.getInstance().getNpc(restartPointNpcId);
		if (restartPointNpc != null)
			return restartPointNpc.getSpawnLocation();
		
		return null;
	}
	
	protected void attachListeners()
	{
		addFirstTalkId(MESSENGER, ROYAL_FLAG, FLAG_RED, FLAG_YELLOW, FLAG_GREEN, FLAG_BLUE, FLAG_PURPLE);
		addTalkId(MESSENGER);
		
		for (int i = 0; i < 6; i++)
			addFirstTalkId(TELEPORT_1 + i);
		
		addCreated(MESSENGER, ALLY_1, ALLY_2, ALLY_3, ALLY_4, ALLY_5);
		addMyDying(ALLY_1, ALLY_2, ALLY_3, ALLY_4, ALLY_5);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		String html = null;
		
		if (npc.getNpcId() >= ROYAL_FLAG && npc.getNpcId() <= FLAG_PURPLE)
			return "";
		
		if (npc.getNpcId() != MESSENGER)
		{
			int index = npc.getNpcId() - TELEPORT_1;
			if ((index == 0 && _firstPhase) || npc._i_ai1 != 0)
			{
				html = "teleporter_notyet.htm";
			}
			else
			{
				npc._i_ai1 = 1;
				teleportPlayers(npc);
				startQuestTimer("clear-tp-delay", npc, player, 60000);
			}
		}
		
		return html;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (_hall.isInSiege())
		{
			if (_data.size() > (_hall.getOwnerId() != 0 ? 1 : 0) && npc.getClanId() > 0)
				removeParticipant(npc.getClanId(), true);
			
			if (_clanlessNpcSpawns.contains(npc))
				_clanlessNpcSpawns.remove(npc);
			
			final List<Integer> clanIds = new ArrayList<>(_data.keySet());
			if (_firstPhase)
			{
				// "Clanless"/"Unassosiated to any clan" npcs are not killed yet
				if (!_clanlessNpcSpawns.isEmpty() && clanIds.size() > (_hall.getOwnerId() > 0 ? 1 : 0))
					return;
					
				// Siege ends if just 1 flag is alive
				// Hall was free before battle
				if (((clanIds.size() == 1) && (_hall.getOwnerId() <= 0)))
				{
					doUnSpawns();
					
					_missionAccomplished = true;
					_winner = ClanTable.getInstance().getClan(clanIds.get(0));
					
					cancelSiegeTask();
					endSiege();
				}
				else if ((_data.size() == 2) && (_hall.getOwnerId() > 0)) // Hall has defender (owner)
				{
					final int preliminaryWinnerClanId = _data.keySet().stream().filter(c -> c != _hall.getOwnerId()).findFirst().orElse(0);
					final Clan preliminaryWinnerClan = ClanTable.getInstance().getClan(preliminaryWinnerClanId);
					
					_messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.S1_CLAN_WON_MATCH_S2).addString(preliminaryWinnerClan.getName()).addFortId(_hall.getId()), 8000);
					_messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.FINAL_MATCH_BEGIN), 8000);
					
					// Second stage lasts 40 minutes
					cancelSiegeTask();
					_siegeTask = ThreadPool.schedule(this::endSiege, 40 * 60 * 1000L);
					
					_firstPhase = false;
					_hall.getSiegeZone().setActive(false);
					
					for (int doorId : INNER_DOORS)
						_hall.openDoor(null, doorId);
					
					doUnSpawns();
					
					for (Entry<Integer, ClanData> entry : _data.entrySet())
						doSpawns(entry.getKey(), entry.getValue(), true, false);
					
					ThreadPool.schedule(() ->
					{
						_messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_SIEGE_FINALS_BEGUN), 8000);
						
						for (int doorId : INNER_DOORS)
							_hall.closeDoor(null, doorId);
						
						for (Entry<Integer, ClanData> entry : _data.entrySet())
							doSpawns(entry.getKey(), entry.getValue(), false, true);
						
						// Send maker second stage start event
						sendMakerEvent("onFlagWarFinalEvent");
						
						_hall.getSiegeZone().setActive(true);
					}, 300000);
				}
				else
				{
					doUnSpawns();
					cancelSiegeTask();
					endSiege();
				}
			}
			else
			{
				doUnSpawns();
				
				_missionAccomplished = true;
				_winner = ClanTable.getInstance().getClan(clanIds.get(0));
				
				endSiege();
			}
		}
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (npc.getNpcId() == MESSENGER)
			_messenger = npc;
		else
			ThreadPool.schedule(() -> npc.getAI().addMoveToDesire(CENTER, 5), (Rnd.get(6) + 5) * 1000L);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("clear-tp-delay"))
			npc._i_ai1 = 0;
		
		return null;
	}
	
	@Override
	public Clan getWinner()
	{
		return _winner;
	}
	
	@Override
	public void prepareSiege()
	{
		registerOwnerIfAbsent();
		
		_hall.banishForeigners();
		_hall.updateSiegeStatus(SiegeStatus.REGISTRATION_OVER);
		
		_siegeTask = ThreadPool.schedule(this::startSiege, _hall.getNextSiegeTime() - System.currentTimeMillis());
	}
	
	@Override
	public void prepareSiege(long delay)
	{
		registerOwnerIfAbsent();
		
		_hall.banishForeigners();
		_hall.updateSiegeStatus(SiegeStatus.REGISTRATION_OVER);
		
		_siegeTask = ThreadPool.schedule(this::startSiege, delay);
	}
	
	@Override
	public void startSiege()
	{
		removeInvalidRegistrations();
		
		_winner = null;
		_firstPhase = true;
		_missionAccomplished = false;
		
		if (_data.keySet().size() < (_hall.getOwnerId() > 0 ? 2 : 1))
		{
			for (int clanId : _data.keySet())
				removeParticipant(clanId, _hall.getOwnerId() != clanId);
			
			clearTables();
			_attackers.clear();
			_hall.updateNextSiege();
			_hall.updateSiegeStatus(SiegeStatus.REGISTRATION_OPENED);
			
			_messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST).addFortId(_hall.getId()), 8000);
			
			return;
		}
		
		_messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_OF_S1_BEGUN).addFortId(_hall.getId()), 8000);
		_messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.TRYOUTS_ABOUT_TO_BEGIN), 8000);
		
		// Announce timer shedules
		ThreadPool.schedule(() -> _messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_WAR_BEGINS_IN_S1_MINUTES).addNumber(1), 8000), 240000);
		
		final int[] secAnnoucements =
		{
			20,
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
		
		for (int secs : secAnnoucements)
			ThreadPool.schedule(() -> _messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_WAR_BEGINS_IN_S1_SECONDS).addNumber(secs), 8000), (300 - secs) * 1000L);
		
		// Open doors for challengers
		for (int door : OUTTER_DOORS)
			_hall.openDoor(null, door);
		
		// Schedule open doors closement, banish non siege participants and siege start in 5 minutes
		ThreadPool.schedule(() ->
		{
			_messenger.broadcastPacketInRadius(SystemMessage.getSystemMessage(SystemMessageId.CLANHALL_SIEGE_TRYOUTS_BEGUN), 8000);
			
			for (int door : OUTTER_DOORS)
				_hall.closeDoor(null, door);
			
			final List<Integer> allowedPlayers = _data.values().stream().flatMap(clanData -> clanData.players.stream()).toList();
			
			for (Player player : _hall.getZone().getKnownTypeInside(Player.class, p -> p.getClan() == null || !allowedPlayers.contains(p.getObjectId())))
				player.teleportTo(_hall.getRndSpawn(SpawnType.BANISH), 20);
			
			// Teleport owner inside
			if (_hall.getOwnerId() > 0)
			{
				final Clan owner = ClanTable.getInstance().getClan(_hall.getOwnerId());
				if (owner != null)
				{
					for (Player player : _hall.getZone().getKnownTypeInside(Player.class, p -> p.getClan() == owner || !allowedPlayers.contains(p.getObjectId())))
						player.teleportTo(_hall.getRndSpawn(SpawnType.OWNER), 0);
				}
			}
			
			// Set as an active siege zone
			_hall.getSiegeZone().setActive(true);
			
			// Spawns challengers npcs
			for (Entry<Integer, ClanData> entry : _data.entrySet())
			{
				ClanData cd = entry.getValue();
				
				doSpawns(entry.getKey(), cd, false, true);
			}
			
			// Send maker siege start event
			sendMakerEvent("onSiegeStart");
			
			// Spawn "Defender NPC"/"Filler" Npcs
			doClanlessNpcSpawns(false, true);
			
			_hall.updateSiegeStatus(SiegeStatus.IN_PROGRESS);
			
			// First stage lasts 20 minutes
			cancelSiegeTask();
			_siegeTask = ThreadPool.schedule(this::endSiege, 20 * 60 * 1000L);
		}, 300000);
		
		// Spawns challengers flags
		for (Entry<Integer, ClanData> entry : _data.entrySet())
		{
			ClanData cd = entry.getValue();
			
			doSpawns(entry.getKey(), cd, true, false);
			
			for (int objId : cd.players)
			{
				final Player player = World.getInstance().getPlayer(objId);
				if (player != null)
					cd.playersInstance.add(player);
			}
		}
		
		// Spawn "Defender NPC"/"Filler" flags
		doClanlessNpcSpawns(true, false);
	}
	
	@Override
	public void endSiege()
	{
		cancelSiegeTask();
		
		// Send maker siege end event
		sendMakerEvent("onSiegeEnd");
		
		doUnSpawns();
		
		final int prevOwner = _hall.getOwnerId();
		
		if (_hall.getOwnerId() > 0)
		{
			final Clan clan = ClanTable.getInstance().getClan(_hall.getOwnerId());
			clan.setClanHallId(0);
			
			_hall.free();
		}
		
		final Clan winner = getWinner();
		
		if (_missionAccomplished && (winner != null))
		{
			_hall.setOwner(winner);
			
			winner.setClanHallId(_hall.getId());
			
			World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.S1_CLAN_DEFEATED_S2).addString(winner.getName()).addFortId(_hall.getId()));
		}
		else if (_firstPhase)
		{
			if (prevOwner > 0)
			{
				final Clan prevOwnerClan = ClanTable.getInstance().getClan(prevOwner);
				_hall.setOwner(prevOwnerClan);
				prevOwnerClan.setClanHallId(_hall.getId());
			}
			
			World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.MATCH_OF_S1_DRAW).addFortId(_hall.getId()));
		}
		else
		{
			World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_S1_DRAW).addFortId(_hall.getId()));
		}
		
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_OF_S1_FINISHED).addFortId(_hall.getId()));
		
		_missionAccomplished = false;
		
		_hall.getSiegeZone().setActive(false);
		
		_hall.updateNextSiege();
		_hall.spawnDoor(false);
		_hall.banishForeigners();
		
		final byte state = 0;
		for (Clan clan : _attackers)
		{
			clan.setFlag(null);
			
			for (Player player : clan.getOnlineMembers())
			{
				player.setSiegeState(state);
				player.broadcastUserInfo();
			}
		}
		
		// Update pvp flag for winners when siege zone becomes inactive
		for (Player player : _hall.getSiegeZone().getKnownTypeInside(Player.class))
			player.updatePvPStatus();
		
		_attackers.clear();
		
		_siegeTask = ThreadPool.schedule(this::prepareSiege, _hall.getNextSiegeTime() - System.currentTimeMillis() - 3600000);
		LOGGER.info("Siege of {} scheduled for {}.", _hall.getName(), _hall.getSiegeDate().getTime());
		
		_hall.updateSiegeStatus(SiegeStatus.REGISTRATION_OPENED);
		
		unspawnNpcs();
		
		if (_hall.getOwnerId() > 0 && prevOwner != _hall.getOwnerId())
		{
			if (prevOwner > 0)
				deleteOwnersNpc(prevOwner);
			
			final ClanData clanData = _data.get(_hall.getOwnerId());
			saveOwnersNpc(_hall.getOwnerId(), clanData.npc);
		}
		
		if (prevOwner > 0 && _hall.getOwnerId() <= 0)
			deleteOwnersNpc(_hall.getOwnerId());
		
		if (_data.size() > 0)
		{
			for (int clanId : _data.keySet().toArray(Integer[]::new))
				removeParticipant(clanId, _hall.getOwnerId() != clanId);
		}
		
		clearTables();
	}
	
	@Override
	public final boolean canPlantFlag()
	{
		return false;
	}
	
	@Override
	public final boolean doorIsAutoAttackable()
	{
		return false;
	}
	
	void doSpawns(int clanId, ClanData cd, boolean tents, boolean npcs)
	{
		if (_firstPhase && clanId == _hall.getOwnerId())
			return;
		
		try
		{
			int index = 0;
			if (_firstPhase)
				index = cd.flag - FLAG_RED;
			else
				index = clanId == _hall.getOwnerId() ? 5 : 6;
			
			final SpawnLocation loc = FLAG_COORDS[index];
			
			if (cd.flag != 0 && tents)
			{
				cd.flagInstance = new Spawn(cd.flag);
				cd.flagInstance.setLoc(loc);
				_tentSpawns.add(cd.flagInstance.doSpawn(false));
			}
			
			if (npcs)
			{
				// If no NPC is selected spawn ALLY_2 (index - 1)
				final int npcId = cd.npc != 0 ? cd.npc : ALLY_2;
				
				cd.warrior = new Spawn(npcId, true);
				cd.warrior.setLoc(loc);
				
				_npcSpawns.add(cd.warrior.doSpawn(false, null, clanId));
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't generate spawns for {}.", e, getName());
		}
	}
	
	protected ClanData registerClan(Clan clan)
	{
		if (clan == null)
			return null;
		
		if (clan.getClanHallId() > 0 && clan.getClanHallId() != _hall.getId())
			return null;
		
		final boolean isOwner = clan.getClanId() == _hall.getOwnerId();
		
		_attackers.add(clan);
		
		final ClanData cd = new ClanData();
		cd.flag = (isOwner) ? ROYAL_FLAG : (ROYAL_FLAG + _data.size() + 1);
		cd.players.add(clan.getLeaderId());
		
		if (isOwner)
			cd.npc = _ownersNpcId;
		
		_data.put(clan.getClanId(), cd);
		
		saveClan(clan.getClanId(), cd.flag);
		saveMember(clan.getClanId(), clan.getLeaderId());
		
		return cd;
	}
	
	private final void removeParticipant(int clanId, boolean teleport)
	{
		final ClanData cd = _data.remove(clanId);
		if (cd == null)
			return;
		
		cd.players.clear();
		
		// Teleport players outside
		if (teleport)
		{
			for (Player player : cd.playersInstance)
				player.teleportTo(RestartType.TOWN);
		}
		
		cd.playersInstance.clear();
	}
	
	public boolean canPayRegistration()
	{
		return true;
	}
	
	@Override
	public final void loadAttackers()
	{
		if (_data == null)
			_data = HashMap.newHashMap(6);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_ATTACKERS))
		{
			ps.setInt(1, _hall.getId());
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final int clanId = rs.getInt("clan_id");
					
					if (ClanTable.getInstance().getClan(clanId) == null)
						continue;
					
					final ClanData cd = new ClanData();
					cd.flag = rs.getInt("flag");
					cd.npc = rs.getInt("npc");
					
					_data.put(clanId, cd);
					
					try (PreparedStatement ps2 = con.prepareStatement(SELECT_MEMBERS))
					{
						ps2.setInt(1, clanId);
						
						try (ResultSet rs2 = ps2.executeQuery())
						{
							while (rs2.next())
								cd.players.add(rs2.getInt("object_id"));
						}
					}
				}
			}
			
			if (_hall.getOwnerId() != 0)
			{
				try (PreparedStatement ps2 = con.prepareStatement(SELECT_OWNER_NPC))
				{
					ps2.setInt(1, _hall.getId());
					
					try (ResultSet rs2 = ps2.executeQuery())
					{
						if (rs2.next() && _hall.getOwnerId() == rs2.getInt("clan_id"))
							_ownersNpcId = rs2.getInt("npc_id");
					}
				}
				
				if (_ownersNpcId == 0)
					_ownersNpcId = ALLY_2;
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load attackers for {}.", e, getName());
		}
	}
	
	private final void saveClan(int clanId, int flag)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_ATTACKERS))
		{
			ps.setInt(1, _hall.getId());
			ps.setInt(2, flag);
			ps.setInt(3, 0);
			ps.setInt(4, clanId);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save clans for {}.", e, getName());
		}
	}
	
	protected final void saveNpc(int npc, int clanId)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_ATTACKERS_NPC))
		{
			ps.setInt(1, npc);
			ps.setInt(2, clanId);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save NPCs for {}.", e, getName());
		}
	}
	
	protected final void saveMember(int clanId, int objectId)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_MEMBERS))
		{
			ps.setInt(1, _hall.getId());
			ps.setInt(2, clanId);
			ps.setInt(3, objectId);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save members for {}.", e, getName());
		}
	}
	
	protected final void saveOwnersNpc(int clanId, int npcId)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_OWNER_NPC))
		{
			ps.setInt(1, _hall.getId());
			ps.setInt(2, npcId);
			ps.setInt(3, clanId);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save owner's npc for {}.", e, getName());
		}
	}
	
	protected final void deleteOwnersNpc(int clanId)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps1 = con.prepareStatement(DELETE_OWNER_NPC))
		{
			ps1.setInt(1, _hall.getId());
			ps1.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't clear data tables for {}.", e, getName());
		}
	}
	
	private void clearTables()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps1 = con.prepareStatement(DELETE_ATTACKERS);
			PreparedStatement ps2 = con.prepareStatement(DELETE_MEMBERS))
		{
			ps1.setInt(1, _hall.getId());
			ps1.execute();
			
			ps2.setInt(1, _hall.getId());
			ps2.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't clear data tables for {}.", e, getName());
		}
	}
	
	private void doClanlessNpcSpawns(boolean tents, boolean npcs)
	{
		try
		{
			final int[] tmpFlags =
			{
				0,
				1,
				2,
				3,
				4
			};
			
			final List<Integer> takenFlags = _data.values().stream().map(dt -> dt.flag - FLAG_RED).toList();
			
			for (int flg : tmpFlags)
			{
				if (!takenFlags.contains(flg))
				{
					int flagNpcId = FLAG_RED + flg;
					
					final SpawnLocation loc = FLAG_COORDS[flg];
					
					if (tents)
					{
						final Spawn flagInstance = new Spawn(flagNpcId);
						flagInstance.setLoc(loc);
						final Npc flagNpc = flagInstance.doSpawn(false);
						
						_clanlessTentSpawns.add(flagNpc);
					}
					
					if (npcs)
					{
						final Spawn warrior = new Spawn(ALLY_2, true);
						warrior.setLoc(loc);
						final Npc warriorNpc = warrior.doSpawn(false);
						_clanlessNpcSpawns.add(warriorNpc);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't generate spawns for {}.", e, getName());
		}
	}
	
	private void doUnSpawns()
	{
		_tentSpawns.forEach(Npc::deleteMe);
		_tentSpawns.clear();
		
		_npcSpawns.forEach(Npc::deleteMe);
		_npcSpawns.clear();
		
		_clanlessTentSpawns.forEach(Npc::deleteMe);
		_clanlessTentSpawns.clear();
		
		_clanlessNpcSpawns.forEach(Npc::deleteMe);
		_clanlessNpcSpawns.clear();
	}
	
	private void sendMakerEvent(String eventName)
	{
		NpcMaker maker = SpawnManager.getInstance().getNpcMaker(MAKER_NAME);
		maker.getMaker().onMakerScriptEvent(eventName, maker, 0, 0);
	}
	
	private void removeInvalidRegistrations()
	{
		Iterator<Entry<Integer, ClanData>> iterator = _data.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<Integer, ClanData> entry = iterator.next();
			ClanData clanData = entry.getValue();
			
			if (entry.getKey() != _hall.getOwnerId() && clanData.flag == 0)
			{
				// Remove invalid registration from the map
				iterator.remove();
				_attackers.remove(ClanTable.getInstance().getClan(entry.getKey()));
			}
		}
	}
	
	private static final void teleportPlayers(Npc npc)
	{
		final int posIndex = Rnd.get(1) + 1;
		final int posX = getNpcIntAIParamOrDefault(npc, "Pos_x" + posIndex, 0);
		final int posY = getNpcIntAIParamOrDefault(npc, "Pos_y" + posIndex, 0);
		final int posZ = getNpcIntAIParamOrDefault(npc, "Pos_z" + posIndex, 0);
		
		npc.forEachKnownTypeInRadius(Player.class, 250, player ->
		{
			if (Math.abs(player.getZ() - npc.getZ()) <= 100)
				player.teleportTo(posX, posY, posZ, 0);
		});
	}
	
	private static final int getNpcIntAIParamOrDefault(Npc npc, String name, int defaultValue)
	{
		return npc.getSpawn().getMemo().getInteger(name, npc.getTemplate().getAiParams().getInteger(name, defaultValue));
	}
	
	private final void registerOwnerIfAbsent()
	{
		if (_hall.getOwnerId() > 0 && !_data.containsKey(_hall.getOwnerId()))
			registerClan(ClanTable.getInstance().getClan(_hall.getOwnerId()));
	}
	
	protected class ClanData
	{
		int flag = 0;
		int npc = 0;
		
		List<Integer> players = new ArrayList<>(18);
		List<Player> playersInstance = new ArrayList<>(18);
		
		Spawn warrior = null;
		Spawn flagInstance = null;
	}
}