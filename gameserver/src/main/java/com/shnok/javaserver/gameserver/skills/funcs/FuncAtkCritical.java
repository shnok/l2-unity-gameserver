package com.shnok.javaserver.gameserver.skills.funcs;

import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncAtkCritical extends Func
{
	private static final FuncAtkCritical INSTANCE = new FuncAtkCritical();
	
	private FuncAtkCritical()
	{
		super(null, Stats.CRITICAL_RATE, 10, 0, null);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		if (!(effector instanceof Summon))
			value *= Formulas.DEX_BONUS[effector.getStatus().getDEX()];
		
		return value * 10;
	}
	
	public static Func getInstance()
	{
		return INSTANCE;
	}
}