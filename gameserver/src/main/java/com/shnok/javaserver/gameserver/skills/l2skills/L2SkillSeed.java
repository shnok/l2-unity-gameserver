package com.shnok.javaserver.gameserver.skills.l2skills;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.effects.EffectSeed;

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