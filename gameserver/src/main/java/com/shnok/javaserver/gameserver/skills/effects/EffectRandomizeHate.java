package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectRandomizeHate extends AbstractEffect
{
	public EffectRandomizeHate(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.RANDOMIZE_HATE;
	}
	
	@Override
	public boolean onStart()
	{
		if (!(getEffected() instanceof Attackable targetAttackable))
			return false;
			
		// if (targetAttackable.isUnresponsive()) TODO
		// return false;
		
		targetAttackable.getAI().getAggroList().randomizeAttack();
		
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