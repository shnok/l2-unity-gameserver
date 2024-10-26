package com.shnok.javaserver.gameserver.model.entity;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.gameserver.data.manager.DuelManager;
import net.sf.l2j.gameserver.enums.TeamType;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.duels.DuelResult;
import net.sf.l2j.gameserver.enums.duels.DuelState;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExDuelEnd;
import net.sf.l2j.gameserver.network.serverpackets.ExDuelReady;
import net.sf.l2j.gameserver.network.serverpackets.ExDuelStart;
import net.sf.l2j.gameserver.network.serverpackets.ExDuelUpdateUserInfo;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.PlaySound;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Duel
{
	private static final PlaySound B04_S01 = new PlaySound(1, "B04_S01");
	private static final int TWO_MINUTES = 120000;
	
	private final Set<PlayerCondition> _playerConditions = ConcurrentHashMap.newKeySet();
	private final int _duelId;
	private final boolean _isPartyDuel;
	private final long _duelEndTime;
	private final Player _playerA;
	private final Player _playerB;
	
	private Future<?> _task;
	
	private int _surrenderRequest;
	private int _countdown = 5;
	
	public Duel(Player playerA, Player playerB, boolean isPartyDuel, int duelId)
	{
		_duelId = duelId;
		_playerA = playerA;
		_playerB = playerB;
		_isPartyDuel = isPartyDuel;
		
		_duelEndTime = System.currentTimeMillis() + TWO_MINUTES;
		
		// Save Players conditions before adding them in duel state.
		savePlayerConditions();
		
		if (_isPartyDuel)
		{
			_countdown = 35;
			
			// Inform players that they will be ported shortly.
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.IN_A_MOMENT_YOU_WILL_BE_TRANSPORTED_TO_THE_SITE_WHERE_THE_DUEL_WILL_TAKE_PLACE);
			broadcastTo(_playerA, sm);
			broadcastTo(_playerB, sm);
			
			// Set states.
			_playerA.getParty().getMembers().forEach(p -> p.setInDuel(_duelId));
			_playerB.getParty().getMembers().forEach(p -> p.setInDuel(_duelId));
		}
		else
		{
			// Set states.
			_playerA.setInDuel(_duelId);
			_playerB.setInDuel(_duelId);
		}
		
		// Start task. Can be shutdowned if the check commands it.
		_task = ThreadPool.scheduleAtFixedRate(this::start, 1000, 1000);
	}
	
	/**
	 * This method makes the countdown, both for party (35sec timer) and 1vs1 cases (5sec timer).<br>
	 * <br>
	 * It also listen the different ways to disturb the duel. Two cases are possible :
	 * <ul>
	 * <li>DuelResult is under CONTINUE state, nothing happens. The task will continue to run every second.</li>
	 * <li>DuelResult is anything except CONTINUE, then the duel ends. Animations are played on any duel end cases, except CANCELED.</li>
	 * </ul>
	 */
	public void start()
	{
		// Test duel condition, no matter if duel already begun or is under preparation.
		final DuelResult status = checkEndDuelCondition();
		if (status != DuelResult.CONTINUE)
		{
			// Abort the task if it was currently running.
			if (_task != null)
			{
				_task.cancel(false);
				_task = null;
			}
			
			// Stop the fight.
			if (_isPartyDuel)
			{
				_playerA.getParty().stopToFight();
				_playerB.getParty().stopToFight();
			}
			else
			{
				_playerA.stopToFight();
				_playerB.stopToFight();
			}
			
			// Play the animations.
			switch (status)
			{
				case TEAM_1_WIN, TEAM_2_SURRENDER:
					playAnimation(_playerB);
					break;
				
				case TEAM_2_WIN, TEAM_1_SURRENDER:
					playAnimation(_playerA);
					break;
			}
			
			// End the duel. Restore conditions, teleport back if it was a Party duel, broadcast messages, etc.
			endDuel(status);
			return;
		}
		
		// Schedule anew, until time reaches 0.
		if (_countdown >= 0)
		{
			switch (_countdown)
			{
				case 33:
					teleportPlayers(-83760, -238825, -3331);
					break;
				
				case 30, 20, 15, 10, 3, 2, 1:
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.THE_DUEL_WILL_BEGIN_IN_S1_SECONDS).addNumber(_countdown);
					broadcastTo(_playerA, sm);
					broadcastTo(_playerB, sm);
					break;
				
				case 0:
					sm = SystemMessage.getSystemMessage(SystemMessageId.LET_THE_DUEL_BEGIN);
					broadcastTo(_playerA, sm);
					broadcastTo(_playerB, sm);
					
					startDuel();
					break;
			}
			
			// Decrease timer.
			_countdown--;
		}
	}
	
	/**
	 * Start the duel.<br>
	 * <br>
	 * Save players conditions, cancel active states, set the team color and send all duel packets.
	 */
	private void startDuel()
	{
		final ExDuelReady ready = new ExDuelReady(_isPartyDuel);
		final ExDuelStart start = new ExDuelStart(_isPartyDuel);
		
		// Send duel packets.
		broadcastTo(_playerA, ready, start, B04_S01);
		broadcastTo(_playerB, ready, start, B04_S01);
		
		// Set states.
		if (_isPartyDuel)
		{
			for (Player partyPlayer : _playerA.getParty().getMembers())
			{
				partyPlayer.prepareToDuel(TeamType.BLUE);
				
				broadcastTo(_playerB, new ExDuelUpdateUserInfo(partyPlayer));
			}
			
			for (Player partyPlayer : _playerB.getParty().getMembers())
			{
				partyPlayer.prepareToDuel(TeamType.RED);
				
				broadcastTo(_playerA, new ExDuelUpdateUserInfo(partyPlayer));
			}
		}
		else
		{
			_playerA.prepareToDuel(TeamType.BLUE);
			_playerB.prepareToDuel(TeamType.RED);
			
			broadcastTo(_playerA, new ExDuelUpdateUserInfo(_playerB));
			broadcastTo(_playerB, new ExDuelUpdateUserInfo(_playerA));
		}
	}
	
	/**
	 * Save the current player condition: hp, mp, cp, location
	 */
	private void savePlayerConditions()
	{
		if (_isPartyDuel)
		{
			for (Player partyPlayer : _playerA.getParty().getMembers())
				_playerConditions.add(new PlayerCondition(partyPlayer, _isPartyDuel));
			
			for (Player partyPlayer : _playerB.getParty().getMembers())
				_playerConditions.add(new PlayerCondition(partyPlayer, _isPartyDuel));
		}
		else
		{
			_playerConditions.add(new PlayerCondition(_playerA, _isPartyDuel));
			_playerConditions.add(new PlayerCondition(_playerB, _isPartyDuel));
		}
	}
	
	/**
	 * Restore player conditions.
	 * @param abnormalEnd : true if the duel was canceled.
	 */
	private void restorePlayerConditions(boolean abnormalEnd)
	{
		// Restore player conditions, clear the Map.
		_playerConditions.forEach(cond -> cond.restoreCondition(!_isPartyDuel && abnormalEnd));
		
		if (_isPartyDuel)
		{
			_playerA.getParty().resetDuelState();
			_playerB.getParty().resetDuelState();
		}
		else
		{
			_playerA.resetDuelState();
			_playerB.resetDuelState();
		}
	}
	
	/**
	 * @return the duel id.
	 */
	public int getId()
	{
		return _duelId;
	}
	
	/**
	 * @return the remaining time.
	 */
	public int getRemainingTime()
	{
		return (int) (_duelEndTime - System.currentTimeMillis());
	}
	
	/**
	 * @return the player that requested the duel.
	 */
	public Player getPlayerA()
	{
		return _playerA;
	}
	
	/**
	 * @return the player that was challenged.
	 */
	public Player getPlayerB()
	{
		return _playerB;
	}
	
	/**
	 * @return true if the duel was a party duel, false otherwise.
	 */
	public boolean isPartyDuel()
	{
		return _isPartyDuel;
	}
	
	/**
	 * Teleport all players to the given coordinates. Used by party duel only.
	 * @param x
	 * @param y
	 * @param z
	 */
	protected void teleportPlayers(int x, int y, int z)
	{
		// TODO: adjust the values if needed... or implement something better (especially using more then 1 arena)
		if (!_isPartyDuel)
			return;
		
		int offset = 0;
		
		for (Player partyPlayer : _playerA.getParty().getMembers())
		{
			partyPlayer.teleportTo(x + offset - 180, y - 150, z, 0);
			offset += 40;
		}
		
		offset = 0;
		for (Player partyPlayer : _playerB.getParty().getMembers())
		{
			partyPlayer.teleportTo(x + offset - 180, y + 150, z, 0);
			offset += 40;
		}
	}
	
	/**
	 * Broadcast a packet to the {@link Player} set as parameter or his team.
	 * @param player : The {@link Player} to send packet to.
	 * @param packets : The {@link L2GameServerPacket}s to send.
	 */
	public void broadcastTo(Player player, L2GameServerPacket... packets)
	{
		if (_isPartyDuel && player.getParty() != null)
		{
			for (Player partyPlayer : player.getParty().getMembers())
			{
				for (L2GameServerPacket packet : packets)
					partyPlayer.sendPacket(packet);
			}
		}
		else
		{
			for (L2GameServerPacket packet : packets)
				player.sendPacket(packet);
		}
	}
	
	/**
	 * Playback the bow animation for loser.
	 * @param player : The {@link Player} to handle.
	 */
	private void playAnimation(Player player)
	{
		if (!player.isOnline() || player.getDuelState() != DuelState.DEAD)
			return;
		
		if (_isPartyDuel && player.getParty() != null)
		{
			for (Player partyPlayer : player.getParty().getMembers())
				partyPlayer.broadcastPacket(new SocialAction(partyPlayer, 7));
		}
		else
			player.broadcastPacket(new SocialAction(player, 7));
	}
	
	/**
	 * This method ends a duel, sending messages to each team, end duel packet, cleaning player conditions and then removing duel from manager.
	 * @param result : The duel result.
	 */
	protected void endDuel(DuelResult result)
	{
		SystemMessage sm = null;
		switch (result)
		{
			case TEAM_2_SURRENDER:
				sm = SystemMessage.getSystemMessage((_isPartyDuel) ? SystemMessageId.SINCE_S1_PARTY_WITHDREW_FROM_THE_DUEL_S2_PARTY_HAS_WON : SystemMessageId.SINCE_S1_WITHDREW_FROM_THE_DUEL_S2_HAS_WON).addString(_playerB.getName()).addString(_playerA.getName());
				broadcastTo(_playerA, sm);
				broadcastTo(_playerB, sm);
			case TEAM_1_WIN:
				sm = SystemMessage.getSystemMessage((_isPartyDuel) ? SystemMessageId.S1_PARTY_HAS_WON_THE_DUEL : SystemMessageId.S1_HAS_WON_THE_DUEL).addString(_playerA.getName());
				break;
			
			case TEAM_1_SURRENDER:
				sm = SystemMessage.getSystemMessage((_isPartyDuel) ? SystemMessageId.SINCE_S1_PARTY_WITHDREW_FROM_THE_DUEL_S2_PARTY_HAS_WON : SystemMessageId.SINCE_S1_WITHDREW_FROM_THE_DUEL_S2_HAS_WON).addString(_playerA.getName()).addString(_playerB.getName());
				broadcastTo(_playerA, sm);
				broadcastTo(_playerB, sm);
			case TEAM_2_WIN:
				sm = SystemMessage.getSystemMessage((_isPartyDuel) ? SystemMessageId.S1_PARTY_HAS_WON_THE_DUEL : SystemMessageId.S1_HAS_WON_THE_DUEL).addString(_playerB.getName());
				break;
			
			case CANCELED, TIMEOUT:
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_DUEL_HAS_ENDED_IN_A_TIE);
				break;
		}
		
		// Send end duel packet.
		ExDuelEnd duelEnd = new ExDuelEnd(_isPartyDuel);
		
		broadcastTo(_playerA, sm, duelEnd);
		broadcastTo(_playerB, sm, duelEnd);
		
		// Restore Players conditions.
		restorePlayerConditions(result == DuelResult.CANCELED);
		
		// Cleanup.
		DuelManager.getInstance().removeDuel(_duelId);
	}
	
	/**
	 * This method checks all possible scenarios which can disturb a duel, and return the appropriate status.
	 * @return DuelResult : The duel status.
	 */
	protected DuelResult checkEndDuelCondition()
	{
		// Both players are offline.
		if (!_playerA.isOnline() && !_playerB.isOnline())
			return DuelResult.CANCELED;
		
		// Player A is offline.
		if (!_playerA.isOnline())
		{
			onPlayerDefeat(_playerA);
			return DuelResult.TEAM_1_SURRENDER;
		}
		
		// Player B is offline.
		if (!_playerB.isOnline())
		{
			onPlayerDefeat(_playerB);
			return DuelResult.TEAM_2_SURRENDER;
		}
		
		// Duel surrender request.
		if (_surrenderRequest != 0)
			return (_surrenderRequest == 1) ? DuelResult.TEAM_1_SURRENDER : DuelResult.TEAM_2_SURRENDER;
		
		// Duel timed out.
		if (getRemainingTime() <= 0)
			return DuelResult.TIMEOUT;
		
		// One of the players is declared winner.
		if (_playerA.getDuelState() == DuelState.WINNER)
			return DuelResult.TEAM_1_WIN;
		
		if (_playerB.getDuelState() == DuelState.WINNER)
			return DuelResult.TEAM_2_WIN;
		
		if (!_isPartyDuel)
		{
			// Duel was interrupted e.g.: player was attacked by mobs / other players
			if (_playerA.getDuelState() == DuelState.INTERRUPTED || _playerB.getDuelState() == DuelState.INTERRUPTED)
				return DuelResult.CANCELED;
			
			// Players are too far apart.
			if (!_playerA.isIn3DRadius(_playerB, 2000))
				return DuelResult.CANCELED;
			
			// One of the players is engaged in PvP.
			if (_playerA.getPvpFlag() != 0 || _playerB.getPvpFlag() != 0)
				return DuelResult.CANCELED;
			
			// One of the players is in a Siege, Peace or PvP zone.
			if (_playerA.isInsideZone(ZoneId.PEACE) || _playerB.isInsideZone(ZoneId.PEACE) || _playerA.isInsideZone(ZoneId.SIEGE) || _playerB.isInsideZone(ZoneId.SIEGE) || _playerA.isInsideZone(ZoneId.PVP) || _playerB.isInsideZone(ZoneId.PVP))
				return DuelResult.CANCELED;
		}
		else
		{
			if (_playerA.getParty() != null)
			{
				for (Player partyMember : _playerA.getParty().getMembers())
				{
					// Duel was interrupted e.g.: player was attacked by mobs / other players
					if (partyMember.getDuelState() == DuelState.INTERRUPTED)
						return DuelResult.CANCELED;
					
					// Players are too far apart.
					if (!partyMember.isIn3DRadius(_playerB, 2000))
						return DuelResult.CANCELED;
					
					// One of the players is engaged in PvP.
					if (partyMember.getPvpFlag() != 0)
						return DuelResult.CANCELED;
					
					// One of the players is in a Siege, Peace or PvP zone.
					if (partyMember.isInsideZone(ZoneId.PEACE) || partyMember.isInsideZone(ZoneId.PVP) || partyMember.isInsideZone(ZoneId.SIEGE))
						return DuelResult.CANCELED;
				}
			}
			
			if (_playerB.getParty() != null)
			{
				for (Player partyMember : _playerB.getParty().getMembers())
				{
					// Duel was interrupted e.g.: player was attacked by mobs / other players
					if (partyMember.getDuelState() == DuelState.INTERRUPTED)
						return DuelResult.CANCELED;
					
					// Players are too far apart.
					if (!partyMember.isIn3DRadius(_playerA, 2000))
						return DuelResult.CANCELED;
					
					// One of the players is engaged in PvP.
					if (partyMember.getPvpFlag() != 0)
						return DuelResult.CANCELED;
					
					// One of the players is in a Siege, Peace or PvP zone.
					if (partyMember.isInsideZone(ZoneId.PEACE) || partyMember.isInsideZone(ZoneId.PVP) || partyMember.isInsideZone(ZoneId.SIEGE))
						return DuelResult.CANCELED;
				}
			}
		}
		
		return DuelResult.CONTINUE;
	}
	
	/**
	 * Register a surrender request. It updates DuelState of players.
	 * @param player : The player who surrenders.
	 */
	public void doSurrender(Player player)
	{
		// A surrender request is already under process, return.
		if (_surrenderRequest != 0)
			return;
		
		// TODO: Can every party member cancel a party duel? or only the party leaders?
		if (_isPartyDuel)
		{
			if (_playerA.getParty().containsPlayer(player))
			{
				_surrenderRequest = 1;
				
				for (Player partyPlayer : _playerA.getParty().getMembers())
					partyPlayer.setDuelState(DuelState.DEAD);
				
				for (Player partyPlayer : _playerB.getParty().getMembers())
					partyPlayer.setDuelState(DuelState.WINNER);
			}
			else if (_playerB.getParty().containsPlayer(player))
			{
				_surrenderRequest = 2;
				
				for (Player partyPlayer : _playerB.getParty().getMembers())
					partyPlayer.setDuelState(DuelState.DEAD);
				
				for (Player partyPlayer : _playerA.getParty().getMembers())
					partyPlayer.setDuelState(DuelState.WINNER);
			}
		}
		else
		{
			if (player == _playerA)
			{
				_surrenderRequest = 1;
				
				_playerA.setDuelState(DuelState.DEAD);
				_playerB.setDuelState(DuelState.WINNER);
			}
			else if (player == _playerB)
			{
				_surrenderRequest = 2;
				
				_playerB.setDuelState(DuelState.DEAD);
				_playerA.setDuelState(DuelState.WINNER);
			}
		}
	}
	
	/**
	 * This method is called whenever a player was defeated in a duel. It updates DuelState of players.
	 * @param player : The defeated player.
	 */
	public void onPlayerDefeat(Player player)
	{
		// Set player as defeated.
		player.setDuelState(DuelState.DEAD);
		
		if (_isPartyDuel)
		{
			boolean teamDefeated = true;
			for (Player partyPlayer : player.getParty().getMembers())
			{
				if (partyPlayer.getDuelState() == DuelState.DUELLING)
				{
					teamDefeated = false;
					break;
				}
			}
			
			if (teamDefeated)
			{
				Player winner = _playerA;
				if (_playerA.getParty().containsPlayer(player))
					winner = _playerB;
				
				for (Player partyPlayer : winner.getParty().getMembers())
					partyPlayer.setDuelState(DuelState.WINNER);
			}
		}
		else
		{
			if (_playerA == player)
				_playerB.setDuelState(DuelState.WINNER);
			else
				_playerA.setDuelState(DuelState.WINNER);
		}
	}
	
	/**
	 * This method is called when a player join/leave a party during a Duel, and enforce Duel cancellation.
	 */
	public void onPartyEdit()
	{
		if (!_isPartyDuel)
			return;
		
		// Teleport back players, setting their duelId to 0.
		for (PlayerCondition cond : _playerConditions)
		{
			cond.restoreCondition(true);
			cond.getPlayer().setInDuel(0);
		}
		
		// Cancel the duel properly.
		endDuel(DuelResult.CANCELED);
	}
}