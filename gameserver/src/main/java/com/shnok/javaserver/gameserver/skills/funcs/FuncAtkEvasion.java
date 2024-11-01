package com.shnok.javaserver.gameserver.skills.funcs;

import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncAtkEvasion extends Func
{
	private static final FuncAtkEvasion INSTANCE = new FuncAtkEvasion();
	
	private FuncAtkEvasion()
	{
		super(null, Stats.EVASION_RATE, 10, 0, null);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		return value + Formulas.BASE_EVASION_ACCURACY[effector.getStatus().getDEX()] + effector.getStatus().getLevel();
	}
	
	public static Func getInstance()
	{
		return INSTANCE;
	}
}