package com.shnok.javaserver.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;

/**
 * Destroys {@link Creature} corpse after specified time.
 */
public final class DecayTaskManager implements Runnable
{
	private final Map<Creature, Long> _creatures = new ConcurrentHashMap<>();
	
	protected DecayTaskManager()
	{
		// Run task each second.
		ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public final void run()
	{
		// List is empty, skip.
		if (_creatures.isEmpty())
			return;
		
		// Get current time.
		final long time = System.currentTimeMillis();
		
		// Loop all characters.
		for (Map.Entry<Creature, Long> entry : _creatures.entrySet())
		{
			final Creature creature = entry.getKey();
			
			// If decayed creature is a Summon, check if he is still linked to its owner. If not, decay task is canceled.
			if (creature instanceof Summon summon && summon.getOwner().getSummon() != creature)
			{
				_creatures.remove(creature);
				continue;
			}
			
			// Time hasn't passed yet, skip.
			if (time < entry.getValue())
				continue;
			
			// Decay the Creature.
			creature.onDecay();
			
			// Remove the entry.
			_creatures.remove(creature);
		}
	}
	
	public final Long get(Creature creature)
	{
		return _creatures.get(creature);
	}
	
	/**
	 * Adds a {@link Creature} to the {@link DecayTaskManager} with additional interval.
	 * @param creature : The {@link Creature} to be added.
	 * @param interval : Interval in seconds, after which the decay task is triggered.
	 */
	public final void add(Creature creature, int interval)
	{
		// If character is a Monster and is spoiled or seeded, double the corpse delay.
		if (creature instanceof Monster monster && (monster.getSpoilState().isSpoiled() || monster.getSeedState().isSeeded()))
			interval *= 2;
		
		_creatures.put(creature, System.currentTimeMillis() + interval * 1000);
	}
	
	/**
	 * Removes the {@link Creature} passed as parameter from the {@link DecayTaskManager}.
	 * @param creature : The {@link Creature} to be removed.
	 * @return True if an entry was successfully removed or false otherwise.
	 */
	public final boolean cancel(Creature creature)
	{
		return _creatures.remove(creature) != null;
	}
	
	public static final DecayTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		protected static final DecayTaskManager INSTANCE = new DecayTaskManager();
	}
}