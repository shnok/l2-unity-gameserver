package com.shnok.javaserver.gameserver.skills.funcs;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncMDefMod extends Func
{
	private static final FuncMDefMod INSTANCE = new FuncMDefMod();
	
	private FuncMDefMod()
	{
		super(null, Stats.MAGIC_DEFENCE, 10, 0, null);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		if (effector instanceof Player player)
		{
			if (player.getInventory().hasItemIn(Paperdoll.LFINGER))
				value -= 5;
			
			if (player.getInventory().hasItemIn(Paperdoll.RFINGER))
				value -= 5;
			
			if (player.getInventory().hasItemIn(Paperdoll.LEAR))
				value -= 9;
			
			if (player.getInventory().hasItemIn(Paperdoll.REAR))
				value -= 9;
			
			if (player.getInventory().hasItemIn(Paperdoll.NECK))
				value -= 13;
		}
		return value * Formulas.MEN_BONUS[effector.getStatus().getMEN()] * effector.getStatus().getLevelMod();
	}
	
	public static Func getInstance()
	{
		return INSTANCE;
	}
}