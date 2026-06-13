package com.shnok.javaserver.gameserver.skills.l2skills;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.effects.EffectSeed;

public class L2SkillSeed extends L2Skill
{
	public L2SkillSeed(StatSet set)
	{
		super(set);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (creature.isAlikeDead())
			return;
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (targetCreature.isAlikeDead() && getTargetType() != SkillTargetType.CORPSE_MOB)
				continue;
			
			EffectSeed oldEffect = (EffectSeed) targetCreature.getFirstEffect(getId());
			if (oldEffect == null)
				getEffects(creature, targetCreature);
			else
				oldEffect.increasePower();
			
			for (AbstractEffect effect : targetCreature.getAllEffects())
				if (effect.getEffectType() == EffectType.SEED)
					effect.rescheduleEffect();
		}
	}
}