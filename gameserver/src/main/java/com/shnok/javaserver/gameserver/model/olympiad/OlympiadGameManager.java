package com.shnok.javaserver.gameserver.model.olympiad;

import java.util.Collection;
import java.util.List;

import net.sf.l2j.commons.logging.CLogger;

import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.type.OlympiadStadiumZone;
import net.sf.l2j.gameserver.network.SystemMessageId;

public class OlympiadGameManager implements Runnable
{
	private static final CLogger LOGGER = new CLogger(OlympiadGameManager.class.getName());
	
	private volatile boolean _battleStarted = false;
	private final OlympiadGameTask[] _tasks;
	
	protected OlympiadGameManager()
	{
		final Collection<OlympiadStadiumZone> zones = ZoneManager.getInstance().getAllZones(OlympiadStadiumZone.class);
		if (zones == null || zones.isEmpty())
			throw new Error("No olympiad stadium zones defined !");
		
		_tasks = new OlympiadGameTask[zones.size()];
		int i = 0;
		for (OlympiadStadiumZone zone : zones)
			_tasks[i++] = new OlympiadGameTask(zone);
		
		LOGGER.info("Loaded {} stadiums.", _tasks.length);
	}
	
	@Override
	public final void run()
	{
		if (Olympiad.getInstance().isInCompetitionPeriod())
		{
			List<List<Integer>> readyClassed = OlympiadManager.getInstance().hasEnoughClassBasedParticipants();
			boolean readyNonClassed = OlympiadManager.getInstance().hasEnoughNonClassBasedParticipants();
			
			// Broadcast to registered Players there isn't enough participants.
			if (readyClassed == null && !readyNonClassed)
			{
				// Broadcast to registered class based Players.
				OlympiadManager.getInstance().getClassBasedParticipants().values().stream().flatMap(Collection::stream).forEach(this::notifyPlayerOfGameDelay);
				
				// Broadcast to registered non class based Players.
				OlympiadManager.getInstance().getNonClassBasedParticipants().stream().forEach(this::notifyPlayerOfGameDelay);
				
				return;
			}
			
			// set up the games queue
			for (int i = 0; i < _tasks.length; i++)
			{
				OlympiadGameTask task = _tasks[i];
				synchronized (task)
				{
					// Fair arena distribution
					if (!task.isRunning())
					{
						// 0,2,4,6,8.. arenas checked for classed or teams first
						if (readyClassed != null && (i % 2) == 0)
						{
							// if no ready teams found check for classed
							final AbstractOlympiadGame newGame = OlympiadGameClassed.createGame(i, readyClassed);
							if (newGame != null)
							{
								task.attachGame(newGame);
								continue;
							}
							
							readyClassed = null;
						}
						
						// 1,3,5,7,9.. arenas used for non-classed
						// also other arenas will be used for non-classed if no classed or teams available
						if (readyNonClassed)
						{
							final AbstractOlympiadGame newGame = OlympiadGameNonClassed.createGame(i, OlympiadManager.getInstance().getNonClassBasedParticipants());
							if (newGame != null)
							{
								task.attachGame(newGame);
								continue;
							}
							
							readyNonClassed = false;
						}
					}
				}
				
				// stop generating games if no more participants
				if (readyClassed == null && !readyNonClassed)
					break;
			}
		}
		else if (isAllTasksFinished())
		{
			OlympiadManager.getInstance().clearParticipants();
			
			_battleStarted = false;
			
			LOGGER.info("All current Olympiad games finished.");
		}
	}
	
	private void notifyPlayerOfGameDelay(int objectId)
	{
		final Player player = World.getInstance().getPlayer(objectId);
		if (player != null)
			player.sendPacket(SystemMessageId.GAMES_DELAYED);
	}
	
	protected final boolean isBattleStarted()
	{
		return _battleStarted;
	}
	
	protected final void startBattle()
	{
		_battleStarted = true;
	}
	
	public final boolean isAllTasksFinished()
	{
		for (OlympiadGameTask task : _tasks)
		{
			if (task.isRunning())
				return false;
		}
		return true;
	}
	
	public final OlympiadGameTask getOlympiadTask(int id)
	{
		if (id < 0 || id >= _tasks.length)
			return null;
		
		return _tasks[id];
	}
	
	public OlympiadGameTask[] getOlympiadTasks()
	{
		return _tasks;
	}
	
	public final int getNumberOfStadiums()
	{
		return _tasks.length;
	}
	
	public final void notifyCompetitorDamage(Player player, int damage)
	{
		if (player == null)
			return;
		
		final int id = player.getOlympiadGameId();
		if (id < 0 || id >= _tasks.length)
			return;
		
		final AbstractOlympiadGame game = _tasks[id].getGame();
		if (game != null)
			game.addDamage(player, damage);
	}
	
	public static final OlympiadGameManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final OlympiadGameManager INSTANCE = new OlympiadGameManager();
	}
}