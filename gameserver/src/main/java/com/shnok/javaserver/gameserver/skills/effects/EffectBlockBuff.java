package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectBlockBuff extends AbstractEffect
{
	public EffectBlockBuff(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.BLOCK_BUFF;
	}
	
	@Override
	public boolean onStart()
	{
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}