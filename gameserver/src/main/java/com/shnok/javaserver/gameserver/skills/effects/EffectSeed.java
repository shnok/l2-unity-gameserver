package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectSeed extends AbstractEffect
{
	private int _power = 1;
	
	public EffectSeed(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.SEED;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	public int getPower()
	{
		return _power;
	}
	
	public void increasePower()
	{
		_power++;
	}
}