package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectAbortCast extends AbstractEffect
{
	public EffectAbortCast(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.ABORT_CAST;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() == null || getEffected() == getEffector() || getEffected().isRaidRelated())
			return false;
		
		if (getEffected().getCast().isCastingNow())
			getEffected().getCast().interrupt();
		
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}