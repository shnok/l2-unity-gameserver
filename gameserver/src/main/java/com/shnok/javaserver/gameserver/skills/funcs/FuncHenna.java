package com.shnok.javaserver.gameserver.skills.funcs;

import net.sf.l2j.gameserver.enums.actors.HennaType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncHenna extends Func
{
	private static final FuncHenna STR = new FuncHenna(HennaType.STR);
	private static final FuncHenna CON = new FuncHenna(HennaType.CON);
	private static final FuncHenna DEX = new FuncHenna(HennaType.DEX);
	private static final FuncHenna INT = new FuncHenna(HennaType.INT);
	private static final FuncHenna MEN = new FuncHenna(HennaType.MEN);
	private static final FuncHenna WIT = new FuncHenna(HennaType.WIT);
	
	private final HennaType _type;
	
	private FuncHenna(HennaType type)
	{
		super(null, type.getStats(), 10, 0, null);
		
		_type = type;
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		if (effector instanceof Player player)
			return value + player.getHennaList().getStat(_type);
		
		return value;
	}
	
	public static final FuncHenna getSTR()
	{
		return STR;
	}
	
	public static final FuncHenna getCON()
	{
		return CON;
	}
	
	public static final FuncHenna getDEX()
	{
		return DEX;
	}
	
	public static final FuncHenna getINT()
	{
		return INT;
	}
	
	public static final FuncHenna getMEN()
	{
		return MEN;
	}
	
	public static final FuncHenna getWIT()
	{
		return WIT;
	}
}