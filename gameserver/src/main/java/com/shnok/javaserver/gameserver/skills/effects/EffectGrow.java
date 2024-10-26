package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.AbnormalEffect;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectGrow extends AbstractEffect
{
	public EffectGrow(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
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
		if (getEffected() instanceof Npc targetNpc)
		{
			targetNpc.setCollisionRadius(targetNpc.getCollisionRadius() * 1.19);
			targetNpc.startAbnormalEffect(AbnormalEffect.GROW);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		if (getEffected() instanceof Npc targetNpc)
		{
			targetNpc.setCollisionRadius(targetNpc.getTemplate().getCollisionRadius());
			targetNpc.stopAbnormalEffect(AbnormalEffect.GROW);
		}
	}
}