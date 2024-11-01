package com.shnok.javaserver.gameserver.skills.funcs;

import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncAtkAccuracy extends Func
{
	private static final FuncAtkAccuracy INSTANCE = new FuncAtkAccuracy();
	
	private FuncAtkAccuracy()
	{
		super(null, Stats.ACCURACY_COMBAT, 10, 0, null);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		final int level = effector.getStatus().getLevel();
		
		value += Formulas.BASE_EVASION_ACCURACY[effector.getStatus().getDEX()] + level;
		if (effector instanceof Summon)
			value += (level < 60) ? 4 : 5;
		
		return value;
	}
	
	public static Func getInstance()
	{
		return INSTANCE;
	}
}