package com.shnok.javaserver.gameserver.enums.skills;

import java.util.NoSuchElementException;

public enum AbnormalEffect
{
	NULL("null", 0x0),
	BLEEDING("bleeding", 0x000001),
	POISON("poison", 0x000002),
	REDCIRCLE("redcircle", 0x000004),
	ICE("ice", 0x000008),
	WIND("wind", 0x000010),
	FEAR("fear", 0x000020),
	STUN("stun", 0x000040),
	SLEEP("sleep", 0x000080),
	MUTED("mute", 0x000100),
	ROOT("root", 0x000200),
	HOLD_1("hold1", 0x000400),
	HOLD_2("hold2", 0x000800),
	UNKNOWN_13("unknown13", 0x001000),
	BIG_HEAD("bighead", 0x002000),
	FLAME("flame", 0x004000),
	CHANGE_TEXTURE("changetexture", 0x008000),
	GROW("grow", 0x010000),
	FLOATING_ROOT("floatroot", 0x020000),
	DANCE_STUNNED("dancestun", 0x040000),
	FIREROOT_STUN("firerootstun", 0x080000),
	STEALTH("stealth", 0x100000),
	IMPRISIONING_1("imprison1", 0x200000),
	IMPRISIONING_2("imprison2", 0x400000),
	MAGIC_CIRCLE("magiccircle", 0x800000);
	
	private final int _mask;
	private final String _name;
	
	private AbnormalEffect(String name, int mask)
	{
		_name = name;
		_mask = mask;
	}
	
	public final int getMask()
	{
		return _mask;
	}
	
	public final String getName()
	{
		return _name;
	}
	
	public static AbnormalEffect getByName(String name)
	{
		for (AbnormalEffect eff : AbnormalEffect.values())
		{
			if (eff.getName().equals(name))
				return eff;
		}
		throw new NoSuchElementException("AbnormalEffect not found for name: '" + name + "'.\n Please check " + AbnormalEffect.class.getCanonicalName());
	}
}