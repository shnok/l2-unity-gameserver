package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectManaHealOverTime extends AbstractEffect
{
	public EffectManaHealOverTime(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.MANA_HEAL_OVER_TIME;
	}
	
	@Override
	public boolean onActionTime()
	{
		if (!getEffected().canBeHealed())
			return false;
		
		getEffected().getStatus().addMp(getTemplate().getValue());
		return true;
	}
}