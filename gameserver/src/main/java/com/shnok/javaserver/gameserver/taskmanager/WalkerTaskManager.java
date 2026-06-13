package com.shnok.javaserver.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.actor.Npc;

/**
 * Handles {@link Npc} waiting state case, when their current WalkerLocation got a delay.
 */
public final class WalkerTaskManager implements Runnable
{
	private final Map<Npc, Long> _walkers = new ConcurrentHashMap<>();
	
	protected WalkerTaskManager()
	{
		// Run task each second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public final void run()
	{
		// List is empty, skip.
		if (_walkers.isEmpty())
			return;
		
		// Get current time.
		final long time = System.currentTimeMillis();
		
		// Loop all Walkers.
		for (Map.Entry<Npc, Long> entry : _walkers.entrySet())
		{
			// Time hasn't passed yet, skip.
			if (time < entry.getValue())
				continue;
			
			// Retrieve the Walker.
			final Npc npc = entry.getKey();
			
			// Npc is still moving ; delay the acquisition of next point.
			if (npc.isMoving())
				continue;
			
			// Order the Npc to move to next point.
			npc.getAI().moveToNextPoint();
			
			// Release it from the map.
			_walkers.remove(npc);
		}
	}
	
	/**
	 * Adds {@link Npc} to the WalkerTaskManager.
	 * @param npc : The {@link Npc} to be added.
	 * @param delay : The delay to add.
	 */
	public final void add(Npc npc, int delay)
	{
		_walkers.put(npc, System.currentTimeMillis() + delay);
	}
	
	public static final WalkerTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final WalkerTaskManager INSTANCE = new WalkerTaskManager();
	}
}