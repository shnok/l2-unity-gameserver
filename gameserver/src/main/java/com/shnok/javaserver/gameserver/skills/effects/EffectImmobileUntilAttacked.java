package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.AiEventType;
import net.sf.l2j.gameserver.enums.skills.EffectFlag;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectImmobileUntilAttacked extends AbstractEffect
{
	public EffectImmobileUntilAttacked(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.IMMOBILE_UNTIL_ATTACKED;
	}
	
	@Override
	public boolean onStart()
	{
		// Abort attack, cast and move.
		getEffected().abortAll(false);
		
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
		
		return true;
	}
	
	@Override
	public void onExit()
	{
		getEffected().removeEffect(this);
		getEffected().stopSkillEffects(getSkill().getId());
		
		getEffected().getAI().notifyEvent(AiEventType.THINK, null, null);
		
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
	}
	
	@Override
	public boolean onActionTime()
	{
		getEffected().removeEffect(this);
		getEffected().stopSkillEffects(getSkill().getId());
		
		getEffected().getAI().notifyEvent(AiEventType.THINK, null, null);
		
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.MEDITATING.getMask();
	}
}