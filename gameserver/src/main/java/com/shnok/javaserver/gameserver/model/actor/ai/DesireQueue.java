package com.shnok.javaserver.gameserver.model.actor.ai;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.gameserver.enums.IntentionType;

/**
 * Set of {@link Desire}s, which is used to determine what kind of {@link Intention} the owner should do.<br>
 * <br>
 * {@link #getLast()} method returns the {@link Desire} with the highest weight.<br>
 * <br>
 * If the {@link Desire} with a set of parameters is already in the list, only the weight will be added to the existing one.
 */
public class DesireQueue
{
	private static final int MAX_CAPACITY = 50;
	
	private final Set<Desire> _desires = ConcurrentHashMap.newKeySet();
	
	public Set<Desire> getDesires()
	{
		return _desires;
	}
	
	public void addOrUpdate(Desire desire)
	{
		final List<Desire> desires = _desires.stream().filter(d -> d.equals(desire)).toList();
		if (!desires.isEmpty())
			desires.forEach(d -> d.addWeight(desire.getWeight()));
		else if (_desires.size() < MAX_CAPACITY)
			_desires.add(desire);
	}
	
	public void decreaseWeightByType(IntentionType intentionType, double amount)
	{
		_desires.stream().filter(d -> d.getType() == intentionType).forEach(d ->
		{
			if (d.getWeight() - amount < 0)
				_desires.remove(d);
			else
				d.reduceWeight(amount);
		});
	}
	
	public Desire getLast()
	{
		if (_desires.isEmpty())
			return null;
		
		return _desires.stream().max(Comparator.comparingDouble(Desire::getWeight)).orElse(null);
	}
}