package com.shnok.javaserver.gameserver.model.actor.container.attackable;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.container.npc.AggroInfo;
import com.shnok.javaserver.gameserver.model.actor.instance.SiegeGuard;

public class AggroList extends ConcurrentHashMap<Creature, AggroInfo>
{
	private static final long serialVersionUID = 1L;
	
	private final Npc _owner;
	
	public AggroList(Npc owner)
	{
		super();
		
		_owner = owner;
	}
	
	/**
	 * Add damage and hate to the {@link AggroInfo} of the {@link Attackable} owner, linked to the {@link Creature} attacker.
	 * @param attacker : The {@link Creature} which dealt damages.
	 * @param damage : The amount of damages done.
	 * @param aggro : The hate to add.
	 */
	public void addDamageHate(Creature attacker, double damage, double aggro)
	{
		if (attacker == null)
			return;
		
		// Can't add friendly Guard as attacker.
		if (_owner instanceof SiegeGuard && attacker instanceof SiegeGuard)
			return;
		
		// Get or create the AggroInfo of the attacker.
		final AggroInfo ai = computeIfAbsent(attacker, AggroInfo::new);
		ai.addDamage(damage);
		ai.addHate(aggro);
		ai.setTimestamp(System.currentTimeMillis());
	}
	
	/**
	 * @return The most hated {@link AggroInfo} of the {@link Attackable} owner, or null if none is found.
	 */
	public AggroInfo getMostHated()
	{
		if (isEmpty() || _owner.isAlikeDead())
			return null;
		
		return values().stream().filter(ai -> ai.getHate() > 0).max(Comparator.comparing(AggroInfo::getHate)).orElse(null);
	}
	
	/**
	 * @return The most hated {@link Creature} of the {@link Attackable} owner, or null if none is found.
	 */
	public Creature getMostHatedCreature()
	{
		final AggroInfo ai = getMostHated();
		return (ai == null) ? null : ai.getAttacker();
	}
	
	/**
	 * @param target : The {@link Creature} whose hate level must be returned.
	 * @return The hate level of the {@link Attackable} owner against the {@link Creature} set as target.
	 */
	public double getHate(Creature target)
	{
		final AggroInfo ai = get(target);
		return (ai == null) ? 0 : ai.getHate();
	}
	
	/**
	 * Clear the hate of a {@link Creature} target without removing it from the {@link AggroList}.<br>
	 * <br>
	 * If none most hated {@link Creature} is found anymore, return the {@link Attackable} owner back to peace.
	 * @param target : The {@link Creature} to clean hate.
	 */
	public void stopHate(Creature target)
	{
		// If empty or no target, return doing nothing.
		if (target == null || isEmpty())
			return;
		
		// Retrieve the AggroInfo related to the target, and stop the hate.
		final AggroInfo ai = get(target);
		if (ai != null)
			ai.stopHate();
		
		// Retrieve the most hated target. If null, return the owner back to peace.
		if (getMostHated() == null && _owner.getAI().getHateList().getMostHatedCreature() == null)
			_owner.getAI().setBackToPeace();
		
		_owner.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.ATTACK && d.getFinalTarget() == target);
	}
	
	/**
	 * Reduce hate for the whole {@link AggroList}.<br>
	 * <br>
	 * If none most hated {@link Creature} is found anymore, return the {@link Attackable} owner back to peace.
	 * @param amount : The amount of hate to remove.
	 */
	public void reduceAllHate(double amount)
	{
		// If empty, return doing nothing.
		if (isEmpty())
			return;
		
		// Process the hate decrease.
		for (AggroInfo ai : values())
			ai.addHate(-amount);
		
		// Retrieve the most hated target. If null, return the owner back to peace.
		if (getMostHated() == null && _owner.getAI().getHateList().getMostHatedCreature() == null)
			_owner.getAI().setBackToPeace();
	}
	
	/**
	 * Clear the hate values of all registered aggroed {@link Creature}s, without dropping them.
	 */
	public void cleanAllHate()
	{
		for (AggroInfo ai : values())
			ai.stopHate();
		
		_owner.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.ATTACK);
	}
	
	/**
	 * Method used when the {@link Attackable} owner can't attack his current target (immobilize state, for exemple).
	 * <ul>
	 * <li>If the {@link AggroList} is filled, pickup a new {@link Creature} from it.</li>
	 * <li>If the {@link AggroList} isn't filled, check if the {@link Attackable} owner is aggro type and pickup a new {@link Creature} using his knownlist.</li>
	 * </ul>
	 * @param range : The range to check. If set to 0, don't use the distance check.
	 * @return A {@link Creature} used as target, or null if no conditions are met.
	 */
	public Creature reconsiderTarget(int range)
	{
		// If AggroList got less than 2 entries, don't bother editing anything.
		if (size() > 1)
		{
			final AggroInfo mostHated = getMostHated();
			
			// No most hated is found, pick any AggroInfo matching conditions and add 2000 aggro.
			// A most hated is found, stop its hate, pick any AggroInfo matching conditions and add old most hated hate.
			for (AggroInfo ai : values())
			{
				// We can't add back the old most hated in the conditions.
				if (mostHated != null && mostHated.getAttacker() == ai.getAttacker())
					continue;
				
				// Don't bother with 0 hate people.
				if (ai.getHate() <= 0)
					continue;
				
				if (range > 0 && !_owner.isIn3DRadius(ai.getAttacker(), range))
					continue;
				
				if (!_owner.canAutoAttack(ai.getAttacker()))
					continue;
				
				// Add 2000 if no most hated was existing.
				if (mostHated == null)
					addDamageHate(ai.getAttacker(), 0, 2000);
				// Stop to hate the most hated and add previous most hated aggro to that new victim.
				else
				{
					_owner.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.ATTACK && d.getFinalTarget() == mostHated.getAttacker());
					mostHated.stopHate();
					addDamageHate(ai.getAttacker(), 0, mostHated.getHate());
				}
				return ai.getAttacker();
			}
		}
		
		// If AggroList gave nothing, then verify first if the Attackable owner is aggressive, and then pickup any Creature matching conditions from his knownlist.
		if (!(_owner instanceof SiegeGuard) && _owner.isAggressive())
		{
			for (Creature creature : _owner.getKnownTypeInRadius(Creature.class, _owner.getTemplate().getAggroRange()))
			{
				if (range > 0 && !_owner.isIn3DRadius(creature, range))
					continue;
				
				if (!_owner.canAutoAttack(creature))
					continue;
				
				// Only 1 aggro, as the AggroList is supposed to be cleaned. Simulate an aggro range entrance.
				addDamageHate(creature, 0, 1);
				return creature;
			}
		}
		
		// Return null if no new victim has been found.
		return null;
	}
	
	/**
	 * Pick the most hated {@link AggroInfo}, then choose another {@link AggroInfo} and set it as the highest hated.
	 */
	public void randomizeAttack()
	{
		// If AggroList got less than 2 entries, don't bother editing anything.
		if (size() < 2)
			return;
		
		final AggroInfo mostHated = getMostHated();
		if (mostHated == null)
			return;
		
		// Pick any AggroInfo matching conditions. Set the chosen AggroInfo to the hate of most hated, and add 200 over it.
		final AggroInfo ai = values().stream().filter(a -> a != mostHated && a.getHate() > 0 && _owner.canAutoAttack(a.getAttacker(), Config.PARTY_RANGE, true)).findAny().orElse(null);
		if (ai == null)
			return;
		
		addDamageHate(ai.getAttacker(), 0, (mostHated.getHate() - ai.getHate()) + 200);
		
		_owner.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.ATTACK);
		
		for (AggroInfo aggroInfo : values())
			_owner.getAI().addAttackDesire(aggroInfo.getAttacker(), (int) aggroInfo.getDamage(), (int) aggroInfo.getHate(), false);
	}
	
	/**
	 * Drop invalid entries from this {@link AggroList}, such as :
	 * <ul>
	 * <li>Dead and alike {@link Creature}s got their hate stopped.</li>
	 * <li>Invisible and unknown {@link Creature}s are simply dropped from the {@link AggroList}.</li>
	 * </ul>
	 */
	public void refresh()
	{
		if (isEmpty())
		{
			_owner.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.ATTACK);
			return;
		}
		
		for (AggroInfo ai : values())
		{
			final Creature creature = ai.getAttacker();
			
			if (creature.isAlikeDead())
			{
				_owner.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.ATTACK && d.getFinalTarget() == creature);
				ai.stopHate();
			}
			else if (!creature.isVisible() || !_owner.knows(creature) || (creature.getActingPlayer() != null && !creature.getActingPlayer().getAppearance().isVisible()))
			{
				_owner.getAI().getDesireQueue().removeIf(d -> d.getType() == IntentionType.ATTACK && d.getFinalTarget() == creature);
				remove(creature);
			}
		}
	}
}