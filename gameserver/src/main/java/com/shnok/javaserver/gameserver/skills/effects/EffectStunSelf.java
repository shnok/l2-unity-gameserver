package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectFlag;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectStunSelf extends AbstractEffect
{
	public EffectStunSelf(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.STUN_SELF;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() instanceof Playable targetPlayable)
			targetPlayable.getAI().tryToIdle();
		
		// Refresh abnormal effects.
		getEffector().updateAbnormalEffect();
		
		return true;
	}
	
	@Override
	public void onExit()
	{
		// Refresh abnormal effects.
		getEffector().updateAbnormalEffect();
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public boolean isSelfEffectType()
	{
		return true;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.STUNNED.getMask();
	}
}