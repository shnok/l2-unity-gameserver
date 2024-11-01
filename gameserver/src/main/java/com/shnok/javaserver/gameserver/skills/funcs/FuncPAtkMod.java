package com.shnok.javaserver.gameserver.skills.funcs;

import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncPAtkMod extends Func
{
	private static final FuncPAtkMod INSTANCE = new FuncPAtkMod();
	
	private FuncPAtkMod()
	{
		super(null, Stats.POWER_ATTACK, 10, 0, null);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		return value * Formulas.STR_BONUS[effector.getStatus().getSTR()] * effector.getStatus().getLevelMod();
	}
	
	public static Func getInstance()
	{
		return INSTANCE;
	}
}