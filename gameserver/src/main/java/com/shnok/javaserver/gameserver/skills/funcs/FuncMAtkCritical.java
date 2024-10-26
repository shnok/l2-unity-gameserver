package com.shnok.javaserver.gameserver.skills.funcs;

import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncMAtkCritical extends Func
{
	private static final FuncMAtkCritical INSTANCE = new FuncMAtkCritical();
	
	private FuncMAtkCritical()
	{
		super(null, Stats.MCRITICAL_RATE, 10, 0, null);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		if (!(effector instanceof Player) || (effector.getActiveWeaponInstance() != null))
			return value * Formulas.WIT_BONUS[effector.getStatus().getWIT()];
		
		return value;
	}
	
	public static Func getInstance()
	{
		return INSTANCE;
	}
}