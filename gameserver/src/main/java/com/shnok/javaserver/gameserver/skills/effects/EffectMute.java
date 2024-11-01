package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectFlag;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectMute extends AbstractEffect
{
	public EffectMute(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.MUTE;
	}
	
	@Override
	public boolean onStart()
	{
		// Abort cast.
		if (getEffected().getCast().isCastingNow() && getEffected().getCast().getCurrentSkill().isMagic())
			getEffected().getCast().stop();
		
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
		
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.MUTED.getMask();
	}
}