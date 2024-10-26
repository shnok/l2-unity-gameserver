package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectCancelDebuff extends AbstractEffect
{
	public EffectCancelDebuff(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.CANCEL_DEBUFF;
	}
	
	@Override
	public boolean onStart()
	{
		return cancel(getEffector(), getEffected(), getSkill(), getTemplate().getEffectType());
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	private static boolean cancel(Creature caster, Creature target, L2Skill skill, SkillType effectType)
	{
		if (!(target instanceof Player targetPlayer) || targetPlayer.isDead())
			return false;
		
		final int cancelLvl = skill.getMagicLevel();
		int count = skill.getMaxNegatedEffects();
		double baseRate = Formulas.calcSkillVulnerability(caster, targetPlayer, skill, effectType);
		
		AbstractEffect effect;
		int lastCanceledSkillId = 0;
		final AbstractEffect[] effects = targetPlayer.getAllEffects();
		for (int i = effects.length; --i >= 0;)
		{
			effect = effects[i];
			if (effect == null)
				continue;
			
			if (!effect.getSkill().isDebuff() || !effect.getSkill().canBeDispeled())
			{
				effects[i] = null;
				continue;
			}
			
			if (effect.getSkill().getId() == lastCanceledSkillId)
			{
				effect.exit(); // this skill already canceled
				continue;
			}
			
			if (!calcCancelSuccess(effect, cancelLvl, (int) baseRate))
				continue;
			
			lastCanceledSkillId = effect.getSkill().getId();
			effect.exit();
			count--;
			
			if (count == 0)
				break;
		}
		
		if (count != 0)
		{
			lastCanceledSkillId = 0;
			for (int i = effects.length; --i >= 0;)
			{
				effect = effects[i];
				if (effect == null)
					continue;
				
				if (!effect.getSkill().isDebuff() || !effect.getSkill().canBeDispeled())
				{
					effects[i] = null;
					continue;
				}
				
				if (effect.getSkill().getId() == lastCanceledSkillId)
				{
					effect.exit(); // this skill already canceled
					continue;
				}
				
				if (!calcCancelSuccess(effect, cancelLvl, (int) baseRate))
					continue;
				
				lastCanceledSkillId = effect.getSkill().getId();
				effect.exit();
				count--;
				
				if (count == 0)
					break;
			}
		}
		return true;
	}
	
	private static boolean calcCancelSuccess(AbstractEffect effect, int cancelLvl, int baseRate)
	{
		int rate = 2 * (cancelLvl - effect.getSkill().getMagicLevel());
		rate += (effect.getPeriod() - effect.getTime()) / 1200;
		rate *= baseRate;
		
		if (rate < 25)
			rate = 25;
		else if (rate > 75)
			rate = 75;
		
		return Rnd.get(100) < rate;
	}
}