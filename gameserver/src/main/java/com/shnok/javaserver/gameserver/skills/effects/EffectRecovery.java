package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectRecovery extends AbstractEffect
{
	public EffectRecovery(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.BUFF;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() instanceof Player targetPlayer)
		{
			targetPlayer.reduceDeathPenaltyBuffLevel();
			return true;
		}
		return false;
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