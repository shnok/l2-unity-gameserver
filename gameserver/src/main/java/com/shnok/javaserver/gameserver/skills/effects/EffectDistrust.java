package com.shnok.javaserver.gameserver.skills.effects;

import java.util.List;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Chest;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectDistrust extends AbstractEffect
{
	public EffectDistrust(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.DISTRUST;
	}
	
	@Override
	public boolean onStart()
	{
		if (!(getEffected() instanceof Monster targetMonster))
			return false;
		
		final List<Monster> targetList = targetMonster.getKnownTypeInRadius(Monster.class, 600, a -> !(a instanceof Chest));
		if (targetList.isEmpty())
			return true;
		
		// Choosing randomly a new target
		final Monster target = Rnd.get(targetList);
		if (target == null)
			return true;
		
		// Add aggro to that target aswell. The aggro power is random.
		final int aggro = (5 + Rnd.get(5)) * getEffector().getStatus().getLevel();
		targetMonster.getAI().getAggroList().addDamageHate(target, 0, aggro);
		return true;
	}
	
	@Override
	public void onExit()
	{
		// Do nothing.
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}