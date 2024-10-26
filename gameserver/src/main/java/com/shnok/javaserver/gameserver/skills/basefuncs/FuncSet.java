package com.shnok.javaserver.gameserver.skills.basefuncs;

import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.conditions.Condition;

/**
 * @see Func
 */
public class FuncSet extends Func
{
	public FuncSet(Object owner, Stats stat, double value, Condition cond)
	{
		super(owner, stat, 0, value, cond);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		// Condition does not exist or it fails, no change.
		if (getCond() != null && !getCond().test(effector, effected, skill))
			return value;
		
		// Update value.
		return getValue();
	}
}