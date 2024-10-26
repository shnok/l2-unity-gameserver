package com.shnok.javaserver.gameserver.skills.funcs;

import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.basefuncs.Func;

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