package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectIncreaseCharges extends AbstractEffect
{
	public EffectIncreaseCharges(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.INCREASE_CHARGES;
	}
	
	@Override
	public boolean onStart()
	{
		if (!(getEffected() instanceof Player targetPlayer))
			return false;
		
		targetPlayer.increaseCharges((int) getTemplate().getValue(), getCount());
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false; // abort effect even if count > 1
	}
}