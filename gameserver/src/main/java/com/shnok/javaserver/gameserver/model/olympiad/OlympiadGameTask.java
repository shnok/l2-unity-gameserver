package com.shnok.javaserver.gameserver.model.olympiad;

import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.SpawnType;
import net.sf.l2j.gameserver.model.zone.type.OlympiadStadiumZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class OlympiadGameTask implements Runnable
{
	private static final CLogger LOGGER = new CLogger(OlympiadGameTask.class.getName());
	
	public static final int[] TELEPORT_TO_ARENA =
	{
		120,
		60,
		30,
		15,
		10,
		5,
		4,
		3,
		2,
		1,
		0
	};
	public static final int[] BATTLE_START_TIME =
	{
		60,
		50,
		40,
		30,
		20,
		10,
		5,
		4,
		3,
		2,
		1,
		0
	};
	public static final int[] TELEPORT_TO_TOWN =
	{
		40,
		30,
		20,
		10,
		5,
		4,
		3,
		2,
		1,
		0
	};
	
	private final OlympiadStadiumZone _zone;
	private AbstractOlympiadGame _game;
	private GameState _state = GameState.IDLE;
	private boolean _needAnnounce = false;
	private int _countDown = 0;
	
	private enum GameState
	{
		BEGIN,
		TELE_TO_ARENA,
		GAME_STARTED,
		BATTLE_COUNTDOWN,
		BATTLE_STARTED,
		BATTLE_IN_PROGRESS,
		GAME_STOPPED,
		TELE_TO_TOWN,
		CLEANUP,
		IDLE
	}
	
	public OlympiadGameTask(OlympiadStadiumZone zone)
	{
		_zone = zone;
		zone.registerTask(this);
	}
	
	public final boolean isRunning()
	{
		return _state != GameState.IDLE;
	}
	
	public final boolean isGameStarted()
	{
		return _state.ordinal() >= GameState.GAME_STARTED.ordinal() && _state.ordinal() <= GameState.CLEANUP.ordinal();
	}
	
	public final boolean isInTimerTime()
	{
		return _state == GameState.BATTLE_COUNTDOWN;
	}
	
	public final boolean isBattleStarted()
	{
		return _state == GameState.BATTLE_IN_PROGRESS;
	}
	
	public final boolean isBattleFinished()
	{
		return _state == GameState.TELE_TO_TOWN;
	}
	
	public final boolean needAnnounce()
	{
		if (_needAnnounce)
		{
			_needAnnounce = false;
			return true;
		}
		return false;
	}
	
	public final OlympiadStadiumZone getZone()
	{
		return _zone;
	}
	
	public final AbstractOlympiadGame getGame()
	{
		return _game;
	}
	
	public final void attachGame(AbstractOlympiadGame game)
	{
		if (game != null && _state != GameState.IDLE)
			return;
		
		_game = game;
		_state = GameState.BEGIN;
		_needAnnounce = false;
		
		ThreadPool.execute(this);
	}
	
	@Override
	public final void run()
	{
		try
		{
			int delay = 1; // schedule next call after 1s
			switch (_state)
			{
				// Game created
				case BEGIN:
					_state = GameState.TELE_TO_ARENA;
					_countDown = Config.OLY_WAIT_TIME;
					break;
				
				// Teleport to arena countdown
				case TELE_TO_ARENA:
					_game.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_WILL_ENTER_THE_OLYMPIAD_STADIUM_IN_S1_SECOND_S).addNumber(_countDown));
					
					delay = getDelay(TELEPORT_TO_ARENA);
					if (_countDown <= 0)
						_state = GameState.GAME_STARTED;
					break;
				
				// Game start, port players to arena
				case GAME_STARTED:
					if (!startGame())
					{
						_state = GameState.GAME_STOPPED;
						break;
					}
					
					_state = GameState.BATTLE_COUNTDOWN;
					_countDown = Config.OLY_WAIT_BATTLE;
					delay = getDelay(BATTLE_START_TIME);
					break;
				
				// Battle start countdown, first part (60-10)
				case BATTLE_COUNTDOWN:
					_zone.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_GAME_WILL_START_IN_S1_SECOND_S).addNumber(_countDown));
					
					if (_countDown == 20)
					{
						_game.buffPlayers();
						_game.healPlayers();
					}
					
					delay = getDelay(BATTLE_START_TIME);
					if (_countDown <= 0)
						_state = GameState.BATTLE_STARTED;
					
					break;
				
				// Beginning of the battle
				case BATTLE_STARTED:
					_countDown = 0;
					
					_game.healPlayers();
					_game.resetDamage();
					
					_state = GameState.BATTLE_IN_PROGRESS; // set state first, used in zone update
					if (!startBattle())
						_state = GameState.GAME_STOPPED;
					
					break;
				
				// Checks during battle
				case BATTLE_IN_PROGRESS:
					_countDown += 1000;
					if (checkBattle() || _countDown > Config.OLY_BATTLE)
						_state = GameState.GAME_STOPPED;
					
					break;
				
				// End of the battle
				case GAME_STOPPED:
					_state = GameState.TELE_TO_TOWN;
					_countDown = Config.OLY_WAIT_END;
					stopGame();
					delay = getDelay(TELEPORT_TO_TOWN);
					break;
				
				// Teleport to town countdown
				case TELE_TO_TOWN:
					_game.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_WILL_BE_MOVED_TO_TOWN_IN_S1_SECONDS).addNumber(_countDown));
					
					delay = getDelay(TELEPORT_TO_TOWN);
					if (_countDown <= 0)
						_state = GameState.CLEANUP;
					
					break;
				
				// Removals
				case CLEANUP:
					cleanupGame();
					_state = GameState.IDLE;
					_game = null;
					return;
			}
			ThreadPool.schedule(this, delay * 1000L);
		}
		catch (Exception e)
		{
			switch (_state)
			{
				case GAME_STOPPED, TELE_TO_TOWN, CLEANUP, IDLE:
					LOGGER.error("Couldn't return players back in town.", e);
					_state = GameState.IDLE;
					_game = null;
					return;
			}
			
			LOGGER.error("Couldn't return players back in town.", e);
			_state = GameState.GAME_STOPPED;
			
			ThreadPool.schedule(this, 1000);
		}
	}
	
	private final int getDelay(int[] times)
	{
		int time;
		for (int i = 0; i < times.length - 1; i++)
		{
			time = times[i];
			if (time >= _countDown)
				continue;
			
			final int delay = _countDown - time;
			_countDown = time;
			return delay;
		}
		// should not happens
		_countDown = -1;
		return 1;
	}
	
	/**
	 * Second stage: check for defections, port players to arena, announce game.
	 * @return true if no participants defected.
	 */
	private final boolean startGame()
	{
		// Checking for opponents and teleporting to arena
		if (_game.checkDefection())
			return false;
		
		if (!_game.portPlayersToArena(_zone.getSpawns(SpawnType.NORMAL)))
			return false;
		
		_game.removals();
		_needAnnounce = true;
		
		// inform manager
		OlympiadGameManager.getInstance().startBattle();
		
		return true;
	}
	
	/**
	 * Fourth stage: last checks, start competition itself.
	 * @return true if all participants online and ready on the stadium.
	 */
	private final boolean startBattle()
	{
		// The game successfully started.
		if (_game.checkBattleStatus() && _game.makeCompetitionStart())
		{
			_game.broadcastOlympiadInfo(_zone);
			_zone.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.STARTS_THE_GAME));
			_zone.updateZoneStatus();
			return true;
		}
		return false;
	}
	
	/**
	 * Fifth stage: battle is running, returns true if winner found.
	 * @return
	 */
	private final boolean checkBattle()
	{
		return _game.haveWinner();
	}
	
	/**
	 * Sixth stage: winner's validations
	 */
	private final void stopGame()
	{
		_game.validateWinner(_zone);
		_zone.updateZoneStatus();
		_game.cleanEffects();
	}
	
	/**
	 * Seventh stage: game cleanup (port players back, closing doors, etc)
	 */
	private final void cleanupGame()
	{
		_game.playersStatusBack();
		_game.portPlayersBack();
		_game.clearPlayers();
	}
}