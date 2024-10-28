package com.shnok.javaserver.gameserver.model.residence.castle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.CastleManager;
import com.shnok.javaserver.gameserver.data.manager.HeroManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.enums.SiegeStatus;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.TowerSpawnLocation;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.ClanMember;
import com.shnok.javaserver.gameserver.model.residence.Siegable;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PlaySound;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.UserInfo;

public class Siege implements Siegable
{
	private static final CLogger LOGGER = new CLogger(Siege.class.getName());
	
	private static final String LOAD_SIEGE_CLAN = "SELECT clan_id,type FROM siege_clans WHERE castle_id=?";
	private static final String CLEAR_SIEGE_CLANS = "DELETE FROM siege_clans WHERE castle_id=?";
	private static final String CLEAR_PENDING_CLANS = "DELETE FROM siege_clans WHERE castle_id=? AND type='PENDING'";
	private static final String CLEAR_SIEGE_CLAN = "DELETE FROM siege_clans WHERE castle_id=? AND clan_id=?";
	private static final String UPDATE_SIEGE_INFOS = "UPDATE castle SET siegeDate=?, regTimeOver=? WHERE id=?";
	private static final String ADD_OR_UPDATE_SIEGE_CLAN = "INSERT INTO siege_clans (clan_id,castle_id,type) VALUES (?,?,?) ON DUPLICATE KEY UPDATE type=VALUES(type)";
	
	private final Castle _castle;
	
	private final Map<Clan, SiegeSide> _registeredClans = new ConcurrentHashMap<>();
	
	protected Calendar _siegeEndDate;
	protected ScheduledFuture<?> _siegeTask;
	
	private Clan _formerOwner;
	private SiegeStatus _siegeStatus = SiegeStatus.REGISTRATION_OPENED;
	
	private List<NpcMaker> _makerEvents = Collections.emptyList();
	
	public Siege(Castle castle)
	{
		_castle = castle;
		
		// Add castle owner as defender (add owner first so that they are on the top of the defender list)
		if (_castle.getOwnerId() > 0)
		{
			final Clan clan = ClanTable.getInstance().getClan(_castle.getOwnerId());
			if (clan != null)
				_registeredClans.put(clan, SiegeSide.OWNER);
		}
		
		// Feed _registeredClans.
		try (Connection con = ConnectionPool.getConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(LOAD_SIEGE_CLAN))
			{
				ps.setInt(1, _castle.getId());
				
				try (ResultSet rs = ps.executeQuery())
				{
					while (rs.next())
					{
						final Clan clan = ClanTable.getInstance().getClan(rs.getInt("clan_id"));
						if (clan != null)
							_registeredClans.put(clan, Enum.valueOf(SiegeSide.class, rs.getString("type")));
					}
				}
				
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load siege registered clans.", e);
		}
		
		startAutoTask();
	}
	
	@Override
	public void startSiege()
	{
		if (isInProgress())
			return;
		
		if (getAttackerClans().isEmpty())
		{
			final SystemMessage sm = SystemMessage.getSystemMessage((_castle.getOwnerId() <= 0) ? SystemMessageId.SIEGE_OF_S1_HAS_BEEN_CANCELED_DUE_TO_LACK_OF_INTEREST : SystemMessageId.S1_SIEGE_WAS_CANCELED_BECAUSE_NO_CLANS_PARTICIPATED);
			sm.addFortId(_castle.getId());
			
			World.toAllOnlinePlayers(sm);
			saveCastleSiege(true);
			return;
		}
		
		// Set the initial Clan owner of the Castle, which will be reused in the siege end.
		_formerOwner = ClanTable.getInstance().getClan(_castle.getOwnerId());
		
		// Edit status so that same siege instance cannot be started again.
		changeStatus(SiegeStatus.IN_PROGRESS);
		
		_castle.getSiegeZone().banishForeigners(_castle.getOwnerId());
		_castle.getSiegeZone().setActive(true);
		
		updatePlayerSiegeStateFlags(false);
		
		_castle.getControlTowers().forEach(TowerSpawnLocation::polymorph);
		
		_castle.closeDoors();
		_castle.spawnSiegeGuardsOrMercenaries();
		
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_OF_S1_HAS_STARTED).addFortId(_castle.getId()));
		World.toAllOnlinePlayers(new PlaySound("systemmsg_e.17"));
		
		// Temporary alliance is in effect message.
		announce(SystemMessageId.TEMPORARY_ALLIANCE, SiegeSide.ATTACKER);
		
		// Set the siege timer.
		_siegeEndDate = Calendar.getInstance();
		_siegeEndDate.add(Calendar.MINUTE, Config.SIEGE_LENGTH);
		
		// Process the siege timer.
		processSiegeTimer();
	}
	
	@Override
	public void endSiege()
	{
		if (!isInProgress())
			return;
		
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_OF_S1_HAS_ENDED).addFortId(_castle.getId()));
		World.toAllOnlinePlayers(new PlaySound("systemmsg_e.18"));
		
		if (_castle.getOwnerId() > 0)
		{
			Clan clan = ClanTable.getInstance().getClan(_castle.getOwnerId());
			World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.CLAN_S1_VICTORIOUS_OVER_S2_S_SIEGE).addString(clan.getName()).addFortId(_castle.getId()));
			
			// An initial clan was holding the castle and is different of current owner.
			if (_formerOwner != null && clan != _formerOwner)
			{
				// Delete circlets and crown's leader for initial castle's owner (if one was existing)
				_castle.checkItemsForClan(_formerOwner);
				
				// Refresh hero diaries.
				for (ClanMember member : clan.getMembers())
				{
					final Player player = member.getPlayerInstance();
					if (player != null && player.isNoble())
						HeroManager.getInstance().setCastleTaken(player.getObjectId(), _castle.getId());
				}
			}
		}
		else
			World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.SIEGE_S1_DRAW).addFortId(_castle.getId()));
		
		// Cleanup clans kills/deaths counters, cleanup flag.
		for (Clan clan : _registeredClans.keySet())
		{
			clan.setSiegeKills(0);
			clan.setSiegeDeaths(0);
			clan.setFlag(null);
		}
		
		// Refresh reputation points towards siege end.
		updateClansReputation();
		
		// Teleport all non-owning castle players.
		_castle.getSiegeZone().banishForeigners(_castle.getOwnerId());
		
		// Clear all flags.
		updatePlayerSiegeStateFlags(true);
		
		// Save castle specific data.
		saveCastleSiege(true);
		
		// Clear registered clans.
		clearAllClans();
		
		// Remove all towers from this castle.
		_castle.getControlTowers().forEach(TowerSpawnLocation::unpolymorph);
		
		// Despawn guards or mercenaries.
		_castle.despawnSiegeGuardsOrMercenaries();
		
		// Respawn/repair castle doors.
		_castle.spawnDoors(false);
		
		// Set the siege zone as inactive.
		_castle.getSiegeZone().setActive(false);
		
		// Reset all artifacts ownership to 0.
		_castle.getArtifacts().forEach(asl -> asl.getNpc().setScriptValue(0));
	}
	
	@Override
	public final List<Clan> getAttackerClans()
	{
		return _registeredClans.entrySet().stream().filter(e -> e.getValue() == SiegeSide.ATTACKER).map(Map.Entry::getKey).toList();
	}
	
	@Override
	public final List<Clan> getDefenderClans()
	{
		return _registeredClans.entrySet().stream().filter(e -> e.getValue() == SiegeSide.DEFENDER || e.getValue() == SiegeSide.OWNER).map(Map.Entry::getKey).toList();
	}
	
	@Override
	public boolean checkSide(Clan clan, SiegeSide type)
	{
		return clan != null && _registeredClans.get(clan) == type;
	}
	
	@Override
	public boolean checkSides(Clan clan, SiegeSide... types)
	{
		return clan != null && ArraysUtil.contains(types, _registeredClans.get(clan));
	}
	
	@Override
	public boolean checkSides(Clan clan)
	{
		return clan != null && _registeredClans.containsKey(clan);
	}
	
	@Override
	public Npc getFlag(Clan clan)
	{
		return (checkSide(clan, SiegeSide.ATTACKER)) ? clan.getFlag() : null;
	}
	
	@Override
	public final Calendar getSiegeDate()
	{
		return _castle.getSiegeDate();
	}
	
	public Map<Clan, SiegeSide> getRegisteredClans()
	{
		return _registeredClans;
	}
	
	public final List<Clan> getPendingClans()
	{
		return _registeredClans.entrySet().stream().filter(e -> e.getValue() == SiegeSide.PENDING).map(Map.Entry::getKey).toList();
	}
	
	/**
	 * Update clan reputation points over siege end, as following :
	 * <ul>
	 * <li>The former clan failed to defend the castle : 1000 points for new owner, -1000 for former clan.</li>
	 * <li>The former clan successfully defended the castle, ending in a draw : 500 points for former clan.</li>
	 * <li>No former clan, which means players successfully attacked over NPCs : 1000 points for new owner.</li>
	 * </ul>
	 */
	public void updateClansReputation()
	{
		final Clan owner = ClanTable.getInstance().getClan(_castle.getOwnerId());
		if (_formerOwner != null)
		{
			// Defenders fail
			if (_formerOwner != owner)
			{
				_formerOwner.takeReputationScore(1000);
				_formerOwner.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.CLAN_WAS_DEFEATED_IN_SIEGE_AND_LOST_S1_REPUTATION_POINTS).addNumber(1000));
				
				// Attackers succeed over defenders
				if (owner != null)
				{
					owner.addReputationScore(1000);
					owner.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.CLAN_VICTORIOUS_IN_SIEGE_AND_GAINED_S1_REPUTATION_POINTS).addNumber(1000));
				}
			}
			// Draw
			else
			{
				_formerOwner.addReputationScore(500);
				_formerOwner.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.CLAN_VICTORIOUS_IN_SIEGE_AND_GAINED_S1_REPUTATION_POINTS).addNumber(500));
			}
		}
		// Attackers win over NPCs
		else if (owner != null)
		{
			owner.addReputationScore(1000);
			owner.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.CLAN_VICTORIOUS_IN_SIEGE_AND_GAINED_S1_REPUTATION_POINTS).addNumber(1000));
		}
	}
	
	/**
	 * This method is used to switch a specific {@link Clan} {@link SiegeSide} to another {@link SiegeSide}.
	 * @param clan : The {@link Clan} to edit state.
	 * @param newState : The new {@link SiegeSide} to set.
	 */
	private void switchSide(Clan clan, SiegeSide newState)
	{
		_registeredClans.put(clan, newState);
	}
	
	/**
	 * This method is used to switch all {@link SiegeSide}s to another {@link SiegeSide}.
	 * @param newState : The new {@link SiegeSide} to set.
	 * @param previousStates : The {@link SiegeSide}s to replace.
	 */
	private void switchSides(SiegeSide newState, SiegeSide... previousStates)
	{
		for (Map.Entry<Clan, SiegeSide> entry : _registeredClans.entrySet())
		{
			if (ArraysUtil.contains(previousStates, entry.getValue()))
				entry.setValue(newState);
		}
	}
	
	public SiegeSide getSide(Clan clan)
	{
		return _registeredClans.get(clan);
	}
	
	/**
	 * Check if both {@link Clan}s set as parameters are registered as opponents.
	 * @param formerClan : The first {@link Clan} to check.
	 * @param targetClan : The second {@link Clan} to check.
	 * @return True if one side is attacker/defender and other side is defender/attacker, and false if one of clan isn't registered or if previous statement didn't match.
	 */
	public boolean isOnOppositeSide(Clan formerClan, Clan targetClan)
	{
		// Clans don't exist ; return false.
		if (formerClan == null || targetClan == null)
			return false;
		
		final SiegeSide formerSide = _registeredClans.get(formerClan);
		final SiegeSide targetSide = _registeredClans.get(targetClan);
		
		// Clans aren't registered ; return false.
		if (formerSide == null || targetSide == null)
			return false;
		
		// One side is owner, pending or defender and the other is attacker ; or vice-versa.
		switch (formerSide)
		{
			case OWNER, DEFENDER, PENDING:
				return targetSide == SiegeSide.ATTACKER;
			
			case ATTACKER:
				return targetSide == SiegeSide.OWNER || targetSide == SiegeSide.DEFENDER || targetSide == SiegeSide.PENDING;
		}
		return false;
	}
	
	/**
	 * When control of castle changed during siege.
	 */
	public void midVictory()
	{
		if (!isInProgress())
			return;
		
		// Despawn all Guards.
		_castle.despawnSiegeGuardsOrMercenaries();
		
		// Respawn Guards.
		_castle.spawnSiegeGuardsOrMercenaries();
		
		if (_castle.getOwnerId() <= 0)
			return;
		
		final List<Clan> attackers = getAttackerClans();
		final List<Clan> defenders = getDefenderClans();
		
		final Clan castleOwner = ClanTable.getInstance().getClan(_castle.getOwnerId());
		final int allyId = castleOwner.getAllyId();
		
		// Temporary alliance disolve message.
		announce(SystemMessageId.TEMPORARY_ALLIANCE_DISSOLVED, SiegeSide.ATTACKER);
		
		// All defenders and owner become attackers.
		switchSides(SiegeSide.ATTACKER, SiegeSide.DEFENDER, SiegeSide.OWNER);
		
		// Newly named castle owner is set.
		switchSide(castleOwner, SiegeSide.OWNER);
		
		// Define newly named castle owner registered allies as defenders.
		if (allyId != 0)
		{
			for (Clan clan : attackers)
			{
				if (clan.getAllyId() == allyId)
					switchSide(clan, SiegeSide.DEFENDER);
			}
		}
		_castle.getSiegeZone().banishForeigners(_castle.getOwnerId());
		
		// Removes defenders' flags.
		for (Clan clan : defenders)
			clan.setFlag(null);
		
		// Remove all castle doors upgrades.
		_castle.removeDoorUpgrade();
		
		// Remove all castle traps upgrades.
		_castle.removeTrapUpgrade();
		
		// Respawn door to castle but make them weaker (50% hp).
		_castle.spawnDoors(true);
		
		// Remove all Flame Towers, respawn all Life Towers.
		_castle.getControlTowers().forEach(TowerSpawnLocation::midVictory);
		
		updatePlayerSiegeStateFlags(false);
	}
	
	/**
	 * Broadcast a {@link SystemMessage} to given {@link SiegeSide}s.
	 * @param sm : The {@link SystemMessage} to send to {@link Clan}s members.
	 * @param sides : The {@link SiegeSide}s to inform. Only ATTACKER and DEFENDER actually react to this method.
	 */
	public void announce(SystemMessage sm, SiegeSide... sides)
	{
		for (SiegeSide side : sides)
		{
			if (side == SiegeSide.ATTACKER)
				getAttackerClans().forEach(c -> c.broadcastToMembers(sm));
			else if (side == SiegeSide.DEFENDER)
				getDefenderClans().forEach(c -> c.broadcastToMembers(sm));
		}
	}
	
	/**
	 * Broadcast a static {@link SystemMessageId} to given {@link SiegeSide}s.
	 * @param smId : The {@link SystemMessageId} to send to {@link Clan}s members.
	 * @param sides : The {@link SiegeSide}s to inform. Only ATTACKER and DEFENDER actually react to this method.
	 * @see #announce(SystemMessage, SiegeSide...)
	 */
	public void announce(SystemMessageId smId, SiegeSide... sides)
	{
		announce(SystemMessage.getSystemMessage(smId), sides);
	}
	
	private void updatePlayerSiegeStateFlags(boolean clear)
	{
		for (Clan clan : getAttackerClans())
		{
			for (Player member : clan.getOnlineMembers())
			{
				member.setSiegeState((clear) ? 0 : 1);
				member.sendPacket(new UserInfo(member));
				member.broadcastRelationsChanges();
			}
		}
		
		for (Clan clan : getDefenderClans())
		{
			for (Player member : clan.getOnlineMembers())
			{
				member.setSiegeState((clear) ? 0 : 2);
				member.sendPacket(new UserInfo(member));
				member.broadcastRelationsChanges();
			}
		}
	}
	
	/** Clear all registered siege clans from database for castle */
	public void clearAllClans()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(CLEAR_SIEGE_CLANS))
		{
			ps.setInt(1, _castle.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't clear siege registered clans.", e);
		}
		
		_registeredClans.clear();
		
		// Add back the owner after cleaning the map.
		if (_castle.getOwnerId() > 0)
		{
			final Clan clan = ClanTable.getInstance().getClan(_castle.getOwnerId());
			if (clan != null)
				_registeredClans.put(clan, SiegeSide.OWNER);
		}
	}
	
	/** Clear all siege clans waiting for approval from database for castle */
	protected void clearPendingClans()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(CLEAR_PENDING_CLANS))
		{
			ps.setInt(1, _castle.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't clear siege pending clans.", e);
		}
		
		_registeredClans.entrySet().removeIf(e -> e.getValue() == SiegeSide.PENDING);
	}
	
	/**
	 * Register clan as attacker
	 * @param player : The player trying to register
	 */
	public void registerAttacker(Player player)
	{
		if (player.getClan() == null)
			return;
		
		int allyId = 0;
		if (_castle.getOwnerId() != 0)
			allyId = ClanTable.getInstance().getClan(_castle.getOwnerId()).getAllyId();
		
		// If the castle owning clan got an alliance, same alliance can't be attacked.
		if (allyId != 0 && player.getClan().getAllyId() == allyId)
		{
			player.sendPacket(SystemMessageId.CANNOT_ATTACK_ALLIANCE_CASTLE);
			return;
		}
		
		// Can't register as attacker if at least one allied clan is registered as defender
		if (allyIsRegisteredOnOppositeSide(player.getClan(), true))
			player.sendPacket(SystemMessageId.CANT_ACCEPT_ALLY_ENEMY_FOR_SIEGE);
		// Save to database
		else if (checkIfCanRegister(player, SiegeSide.ATTACKER))
			registerClan(player.getClan(), SiegeSide.ATTACKER);
	}
	
	/**
	 * Register clan as defender.
	 * @param player : The player trying to register
	 */
	public void registerDefender(Player player)
	{
		if (player.getClan() == null)
			return;
		
		// Castle owned by NPC is considered as full side
		if (_castle.getOwnerId() <= 0)
			player.sendPacket(SystemMessageId.DEFENDER_SIDE_FULL);
		// Can't register as defender if at least one allied clan is registered as attacker
		else if (allyIsRegisteredOnOppositeSide(player.getClan(), false))
			player.sendPacket(SystemMessageId.CANT_ACCEPT_ALLY_ENEMY_FOR_SIEGE);
		// Save to database
		else if (checkIfCanRegister(player, SiegeSide.PENDING))
			registerClan(player.getClan(), SiegeSide.PENDING);
	}
	
	/**
	 * Verify if allies are registered on different list than the actual player's choice. Let's say clan A and clan B are in same alliance. If clan A wants to attack a castle, clan B mustn't be on defenders' list. The contrary is right too : you can't defend if one ally is on attackers' list.
	 * @param clan : The clan used for alliance existence checks.
	 * @param attacker : A boolean used to know if this check is used for attackers or defenders.
	 * @return true if one clan of the alliance is registered in other side.
	 */
	private boolean allyIsRegisteredOnOppositeSide(Clan clan, boolean attacker)
	{
		// Check if player's clan got an alliance ; if not, skip the check
		final int allyId = clan.getAllyId();
		if (allyId != 0)
		{
			// Verify through the clans list for existing clans
			for (Clan alliedClan : ClanTable.getInstance().getClans())
			{
				// If a clan with same allyId is found (so, same alliance)
				if (alliedClan.getAllyId() == allyId)
				{
					// Skip player's clan from the check
					if (alliedClan.getClanId() == clan.getClanId())
						continue;
					
					// If the check is made for attackers' list
					if (attacker)
					{
						// Check if the allied clan is on defender / defender waiting lists
						if (checkSides(alliedClan, SiegeSide.DEFENDER, SiegeSide.OWNER, SiegeSide.PENDING))
							return true;
					}
					else
					{
						// Check if the allied clan is on attacker list
						if (checkSides(alliedClan, SiegeSide.ATTACKER))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Remove clan from siege. Drop it from _registeredClans and database. Castle owner can't be dropped.
	 * @param clan : The clan to check.
	 */
	public void unregisterClan(Clan clan)
	{
		// Check if clan parameter is ok, avoid to drop castle owner, then remove if possible. If it couldn't be removed, return.
		if (clan == null || clan.getCastleId() == _castle.getId() || _registeredClans.remove(clan) == null)
			return;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(CLEAR_SIEGE_CLAN))
		{
			ps.setInt(1, _castle.getId());
			ps.setInt(2, clan.getClanId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't unregister clan on siege.", e);
		}
	}
	
	/**
	 * This method allows to :
	 * <ul>
	 * <li>Check if the siege time is deprecated, and recalculate otherwise.</li>
	 * <li>Schedule start siege (it's in an else because saveCastleSiege() already affect it).</li>
	 * </ul>
	 */
	private void startAutoTask()
	{
		if (_castle.getSiegeDate().getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
			saveCastleSiege(false);
		else
		{
			if (_siegeTask != null)
				_siegeTask.cancel(false);
			
			_siegeTask = ThreadPool.schedule(this::siegeStart, 1000);
		}
	}
	
	/**
	 * @param player : The player trying to register.
	 * @param type : The SiegeSide to test.
	 * @return true if the player can register.
	 */
	private boolean checkIfCanRegister(Player player, SiegeSide type)
	{
		SystemMessage sm;
		
		if (isRegistrationOver())
			sm = SystemMessage.getSystemMessage(SystemMessageId.DEADLINE_FOR_SIEGE_S1_PASSED).addFortId(_castle.getId());
		else if (isInProgress())
			sm = SystemMessage.getSystemMessage(SystemMessageId.NOT_SIEGE_REGISTRATION_TIME2);
		else if (player.getClan() == null || player.getClan().getLevel() < Config.MINIMUM_CLAN_LEVEL)
			sm = SystemMessage.getSystemMessage(SystemMessageId.ONLY_CLAN_LEVEL_4_ABOVE_MAY_SIEGE);
		else if (player.getClan().hasCastle())
			sm = (player.getClan().getClanId() == _castle.getOwnerId()) ? SystemMessage.getSystemMessage(SystemMessageId.CLAN_THAT_OWNS_CASTLE_IS_AUTOMATICALLY_REGISTERED_DEFENDING) : SystemMessage.getSystemMessage(SystemMessageId.CLAN_THAT_OWNS_CASTLE_CANNOT_PARTICIPATE_OTHER_SIEGE);
		else if (player.getClan().isRegisteredOnSiege())
			sm = SystemMessage.getSystemMessage(SystemMessageId.ALREADY_REQUESTED_SIEGE_BATTLE);
		else if (checkIfAlreadyRegisteredForSameDay(player.getClan()))
			sm = SystemMessage.getSystemMessage(SystemMessageId.APPLICATION_DENIED_BECAUSE_ALREADY_SUBMITTED_A_REQUEST_FOR_ANOTHER_SIEGE_BATTLE);
		else if (type == SiegeSide.ATTACKER && getAttackerClans().size() >= Config.MAX_ATTACKERS_NUMBER)
			sm = SystemMessage.getSystemMessage(SystemMessageId.ATTACKER_SIDE_FULL);
		else if ((type == SiegeSide.DEFENDER || type == SiegeSide.PENDING || type == SiegeSide.OWNER) && (getDefenderClans().size() + getPendingClans().size() >= Config.MAX_DEFENDERS_NUMBER))
			sm = SystemMessage.getSystemMessage(SystemMessageId.DEFENDER_SIDE_FULL);
		else
			return true;
		
		player.sendPacket(sm);
		return false;
	}
	
	/**
	 * @param clan The L2Clan of the player trying to register
	 * @return true if the clan has already registered to a siege for the same day.
	 */
	public boolean checkIfAlreadyRegisteredForSameDay(Clan clan)
	{
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			final Siege siege = castle.getSiege();
			if (siege == this)
				continue;
			
			if (siege.getSiegeDate().get(Calendar.DAY_OF_WEEK) == getSiegeDate().get(Calendar.DAY_OF_WEEK) && siege.checkSides(clan))
				return true;
		}
		return false;
	}
	
	/**
	 * Save castle siege related to database.
	 * @param launchTask : if true, launch the start siege task.
	 */
	private void saveCastleSiege(boolean launchTask)
	{
		// Set the next siege date in 2 weeks from now.
		setNextSiegeDate();
		
		// You can edit time anew.
		_castle.setTimeRegistrationOver(false);
		
		// Save the new date.
		saveSiegeDate();
		
		// Prepare start siege task.
		if (launchTask)
			startAutoTask();
		
		LOGGER.info("New date for {} siege: {}.", _castle.getName(), _castle.getSiegeDate().getTime());
	}
	
	/**
	 * Save siege date to database.
	 */
	private void saveSiegeDate()
	{
		if (_siegeTask != null)
		{
			_siegeTask.cancel(false);
			_siegeTask = ThreadPool.schedule(this::siegeStart, 1000);
		}
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_SIEGE_INFOS))
		{
			ps.setLong(1, getSiegeDate().getTimeInMillis());
			ps.setString(2, String.valueOf(isTimeRegistrationOver()));
			ps.setInt(3, _castle.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save siege date.", e);
		}
	}
	
	/**
	 * Save registration to database.
	 * @param clan : The L2Clan of player.
	 * @param type
	 */
	public void registerClan(Clan clan, SiegeSide type)
	{
		if (clan.hasCastle())
			return;
		
		switch (type)
		{
			case DEFENDER, PENDING, OWNER:
				if (getDefenderClans().size() + getPendingClans().size() >= Config.MAX_DEFENDERS_NUMBER)
					return;
				break;
			
			default:
				if (getAttackerClans().size() >= Config.MAX_ATTACKERS_NUMBER)
					return;
				break;
		}
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(ADD_OR_UPDATE_SIEGE_CLAN))
		{
			ps.setInt(1, clan.getClanId());
			ps.setInt(2, _castle.getId());
			ps.setString(3, type.toString());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't register clan on siege.", e);
		}
		
		_registeredClans.put(clan, type);
	}
	
	/**
	 * Set the date for the next siege.
	 */
	private void setNextSiegeDate()
	{
		final Calendar siegeDate = _castle.getSiegeDate();
		if (siegeDate.getTimeInMillis() < System.currentTimeMillis())
			siegeDate.setTimeInMillis(System.currentTimeMillis());
		
		switch (_castle.getId())
		{
			case 3, 4, 6, 7:
				siegeDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				break;
			
			default:
				siegeDate.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				break;
		}
		
		// Set next siege date if siege has passed ; add 14 days (2 weeks).
		siegeDate.add(Calendar.WEEK_OF_YEAR, 2);
		
		// Set default hour to 18:00. This can be changed - only once - by the castle leader via the chamberlain.
		siegeDate.set(Calendar.HOUR_OF_DAY, 18);
		siegeDate.set(Calendar.MINUTE, 0);
		siegeDate.set(Calendar.SECOND, 0);
		siegeDate.set(Calendar.MILLISECOND, 0);
		
		// Send message and allow registration for next siege.
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.S1_ANNOUNCED_SIEGE_TIME).addFortId(_castle.getId()));
		changeStatus(SiegeStatus.REGISTRATION_OPENED);
	}
	
	public final Castle getCastle()
	{
		return _castle;
	}
	
	public final boolean isInProgress()
	{
		return _siegeStatus == SiegeStatus.IN_PROGRESS;
	}
	
	public final boolean isRegistrationOver()
	{
		return _siegeStatus != SiegeStatus.REGISTRATION_OPENED;
	}
	
	public final boolean isTimeRegistrationOver()
	{
		return _castle.isTimeRegistrationOver();
	}
	
	/**
	 * @return siege registration end date, which always equals siege date minus one day.
	 */
	public final long getSiegeRegistrationEndDate()
	{
		return _castle.getSiegeDate().getTimeInMillis() - 86400000;
	}
	
	public void endTimeRegistration(boolean automatic)
	{
		_castle.setTimeRegistrationOver(true);
		if (!automatic)
			saveSiegeDate();
	}
	
	public void addMakerEvent(NpcMaker quest)
	{
		if (_makerEvents.isEmpty())
			_makerEvents = new ArrayList<>(3);
		
		_makerEvents.add(quest);
	}
	
	public SiegeStatus getStatus()
	{
		return _siegeStatus;
	}
	
	protected void changeStatus(SiegeStatus status)
	{
		_siegeStatus = status;
		
		for (NpcMaker maker : _makerEvents)
			maker.getMaker().onSiegeEvent(this, maker);
	}
	
	private void siegeStart()
	{
		_siegeTask.cancel(false);
		
		if (isInProgress())
			return;
		
		if (!isTimeRegistrationOver())
		{
			final long regTimeRemaining = getSiegeRegistrationEndDate() - Calendar.getInstance().getTimeInMillis();
			if (regTimeRemaining > 0)
			{
				_siegeTask = ThreadPool.schedule(this::siegeStart, regTimeRemaining);
				return;
			}
			
			endTimeRegistration(true);
		}
		
		final long timeRemaining = getSiegeDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
		
		if (timeRemaining > 86400000)
			_siegeTask = ThreadPool.schedule(this::siegeStart, timeRemaining - 86400000);
		else if (timeRemaining <= 86400000 && timeRemaining > 13600000)
		{
			World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.REGISTRATION_TERM_FOR_S1_ENDED).addFortId(_castle.getId()));
			changeStatus(SiegeStatus.REGISTRATION_OVER);
			clearPendingClans();
			_siegeTask = ThreadPool.schedule(this::siegeStart, timeRemaining - 13600000);
		}
		else if (timeRemaining <= 13600000 && timeRemaining > 600000)
			_siegeTask = ThreadPool.schedule(this::siegeStart, timeRemaining - 600000);
		else if (timeRemaining <= 600000 && timeRemaining > 300000)
			_siegeTask = ThreadPool.schedule(this::siegeStart, timeRemaining - 300000);
		else if (timeRemaining <= 300000 && timeRemaining > 10000)
			_siegeTask = ThreadPool.schedule(this::siegeStart, timeRemaining - 10000);
		else if (timeRemaining <= 10000 && timeRemaining > 0)
			_siegeTask = ThreadPool.schedule(this::siegeStart, timeRemaining);
		else
			startSiege();
	}
	
	private void processSiegeTimer()
	{
		if (!isInProgress())
			return;
		
		final long timeRemaining = _siegeEndDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
		
		if (timeRemaining > 3600000)
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 3600000);
		else if (timeRemaining > 1800000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.S1_HOURS_UNTIL_SIEGE_CONCLUSION).addNumber(1), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 1800000);
		}
		else if (timeRemaining > 600000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.S1_MINUTES_UNTIL_SIEGE_CONCLUSION).addNumber(30), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 600000);
		}
		else if (timeRemaining > 300000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.S1_MINUTES_UNTIL_SIEGE_CONCLUSION).addNumber(10), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 300000);
		}
		else if (timeRemaining > 60000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.S1_MINUTES_UNTIL_SIEGE_CONCLUSION).addNumber(5), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 60000);
		}
		else if (timeRemaining > 10000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.S1_MINUTES_UNTIL_SIEGE_CONCLUSION).addNumber(1), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 10000);
		}
		else if (timeRemaining > 9000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(10), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 9000);
		}
		else if (timeRemaining > 8000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(9), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 8000);
		}
		else if (timeRemaining > 7000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(8), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 7000);
		}
		else if (timeRemaining > 6000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(7), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 6000);
		}
		else if (timeRemaining > 5000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(6), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 5000);
		}
		else if (timeRemaining > 4000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(5), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 4000);
		}
		else if (timeRemaining > 3000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(4), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 3000);
		}
		else if (timeRemaining > 2000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(3), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 2000);
		}
		else if (timeRemaining > 1000)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(2), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining - 1000);
		}
		else if (timeRemaining > 0)
		{
			announce(SystemMessage.getSystemMessage(SystemMessageId.CASTLE_SIEGE_S1_SECONDS_LEFT).addNumber(1), SiegeSide.ATTACKER, SiegeSide.DEFENDER);
			ThreadPool.schedule(this::processSiegeTimer, timeRemaining);
		}
		else
			endSiege();
	}
}