package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectNegate extends AbstractEffect
{
	public EffectNegate(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.NEGATE;
	}
	
	@Override
	public boolean onStart()
	{
		for (int negateSkillId : getSkill().getNegateId())
		{
			if (negateSkillId != 0)
				getEffected().stopSkillEffects(negateSkillId);
		}
		
		for (SkillType negateSkillType : getSkill().getNegateStats())
			getEffected().stopSkillEffects(negateSkillType, getSkill().getNegateLvl());
		
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}