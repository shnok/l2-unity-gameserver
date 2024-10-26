package com.shnok.javaserver.gameserver.skills.funcs;

import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncMAtkMod extends Func
{
	private static final FuncMAtkMod INSTANCE = new FuncMAtkMod();
	
	private FuncMAtkMod()
	{
		super(null, Stats.MAGIC_ATTACK, 10, 0, null);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		final double intMod = Formulas.INT_BONUS[effector.getStatus().getINT()];
		final double lvlMod = effector.getStatus().getLevelMod();
		
		return value * ((lvlMod * lvlMod) * (intMod * intMod));
	}
	
	public static Func getInstance()
	{
		return INSTANCE;
	}
}