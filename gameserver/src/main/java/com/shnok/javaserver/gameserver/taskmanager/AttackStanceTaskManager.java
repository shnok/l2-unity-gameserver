package com.shnok.javaserver.gameserver.taskmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Cubic;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.AutoAttackStop;

/**
 * Turns off attack stance of {@link Creature} after ATTACK_STANCE_PERIOD (set to 15sec by default).
 */
public final class AttackStanceTaskManager implements Runnable
{
	private static final long ATTACK_STANCE_PERIOD = 15000; // 15 seconds
	
	private final Map<Creature, Long> _creatures = new ConcurrentHashMap<>();
	
	protected AttackStanceTaskManager()
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
			// Time hasn't passed yet, skip.
			if (time < entry.getValue())
				continue;
			
			// Get character.
			final Creature creature = entry.getKey();
			
			// Stop character attack stance animation.
			creature.broadcastPacket(new AutoAttackStop(creature.getObjectId()));
			
			if (creature instanceof Player player)
			{
				// Stop summon attack stance animation.
				final Summon summon = player.getSummon();
				if (summon != null)
					summon.broadcastPacket(new AutoAttackStop(summon.getObjectId()));
			}
			
			// Remove task.
			_creatures.remove(creature);
		}
	}
	
	/**
	 * Add a {@link Creature} to the {@link AttackStanceTaskManager}.
	 * @param creature : The Creature to add.
	 */
	public final void add(Creature creature)
	{
		if (creature instanceof Playable)
		{
			for (Cubic cubic : creature.getActingPlayer().getCubicList())
				if (cubic.getId() != Cubic.LIFE_CUBIC)
					cubic.doAction();
		}
		
		_creatures.put(creature, System.currentTimeMillis() + ATTACK_STANCE_PERIOD);
	}
	
	/**
	 * @param creature : The Creature to remove.
	 * @return true if the {@link Creature} was successfully dropped from the {@link AttackStanceTaskManager}.
	 */
	public final boolean remove(Creature creature)
	{
		if (creature instanceof Summon)
			creature = creature.getActingPlayer();
		
		return _creatures.remove(creature) != null;
	}
	
	/**
	 * @param creature : The Creature to test.
	 * @return true if a {@link Creature} is registered in the {@link AttackStanceTaskManager}, false otherwise.
	 */
	public final boolean isInAttackStance(Creature creature)
	{
		if (creature instanceof Summon)
			creature = creature.getActingPlayer();
		
		return _creatures.containsKey(creature);
	}
	
	public static final AttackStanceTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final AttackStanceTaskManager INSTANCE = new AttackStanceTaskManager();
	}
}