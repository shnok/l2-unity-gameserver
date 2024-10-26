package com.shnok.javaserver.gameserver.skills;

import java.util.Set;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.skills.TriggerType;

public final class ChanceCondition
{
	private final TriggerType _triggerType;
	private final int _chance;
	
	private ChanceCondition(TriggerType trigger, int chance)
	{
		_triggerType = trigger;
		_chance = chance;
	}
	
	@Override
	public String toString()
	{
		return "ChanceCondition[" + _chance + ";" + _triggerType + "]";
	}
	
	public static ChanceCondition parse(StatSet set)
	{
		final TriggerType trigger = set.getEnum("chanceType", TriggerType.class, null);
		if (trigger == null)
			return null;
		
		final int chance = set.getInteger("activationChance", -1);
		
		return new ChanceCondition(trigger, chance);
	}
	
	public static ChanceCondition parse(String chanceType, int chance)
	{
		if (chanceType == null)
			return null;
		
		final TriggerType trigger = Enum.valueOf(TriggerType.class, chanceType);
		if (trigger == null)
			return null;
		
		return new ChanceCondition(trigger, chance);
	}
	
	public boolean trigger(Set<TriggerType> triggers)
	{
		return triggers.contains(_triggerType) && (_chance < 0 || Rnd.get(100) < _chance);
	}
}