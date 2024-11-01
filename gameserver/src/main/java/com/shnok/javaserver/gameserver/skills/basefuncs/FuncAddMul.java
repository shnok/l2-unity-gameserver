package com.shnok.javaserver.gameserver.skills.basefuncs;

import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.conditions.Condition;

/**
 * @see Func
 */
public class FuncAddMul extends Func
{
	public FuncAddMul(Object owner, Stats stat, double value, Condition cond)
	{
		super(owner, stat, 40, value, cond);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		// Condition does not exist or it fails, no change.
		if (getCond() != null && !getCond().test(effector, effected, skill))
			return value;
		
		// Update value.
		return value * (1 - (getValue() / 100));
	}
}