package com.shnok.javaserver.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.actor.Player;

/**
 * Update and clear PvP flag of {@link Player}s after specified time.
 */
public final class PvpFlagTaskManager implements Runnable
{
	private final Map<Player, Long> _players = new ConcurrentHashMap<>();
	
	protected PvpFlagTaskManager()
	{
		// Run task each second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public final void run()
	{
		// List is empty, skip.
		if (_players.isEmpty())
			return;
		
		// Get current time.
		final long currentTime = System.currentTimeMillis();
		
		// Loop all players.
		for (Map.Entry<Player, Long> entry : _players.entrySet())
		{
			// Get time left and check.
			final Player player = entry.getKey();
			final long timeLeft = entry.getValue();
			
			// Time is running out, clear PvP flag and remove from list.
			if (currentTime > timeLeft)
				remove(player, true);
			// Time almost runned out, update to blinking PvP flag.
			else if (currentTime > (timeLeft - 5000))
				player.updatePvPFlag(2);
			// Time didn't run out, keep PvP flag.
			else
				player.updatePvPFlag(1);
		}
	}
	
	/**
	 * Add the {@link Player} set as parameter to the {@link PvpFlagTaskManager}.
	 * @param player : The {@link Player} to add.
	 * @param time : The time in ms, after which the PvP flag is removed.
	 */
	public final void add(Player player, long time)
	{
		_players.put(player, System.currentTimeMillis() + time);
	}
	
	/**
	 * Remove the {@link Player} set as parameter from the {@link PvpFlagTaskManager}.
	 * @param player : The {@link Player} to remove.
	 * @param resetFlag : If true, the PvP flag is reset.
	 */
	public final void remove(Player player, boolean resetFlag)
	{
		_players.remove(player);
		
		if (resetFlag)
			player.updatePvPFlag(0);
	}
	
	public static final PvpFlagTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PvpFlagTaskManager INSTANCE = new PvpFlagTaskManager();
	}
}