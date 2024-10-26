package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectSpoil extends AbstractEffect
{
	public EffectSpoil(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.SPOIL;
	}
	
	@Override
	public boolean onStart()
	{
		if (!(getEffector() instanceof Player player))
			return false;
		
		if (!(getEffected() instanceof Monster targetMonster))
			return false;
		
		if (targetMonster.isDead())
			return false;
		
		if (targetMonster.getSpoilState().isSpoiled())
		{
			player.sendPacket(SystemMessageId.ALREADY_SPOILED);
			return false;
		}
		
		if (Formulas.calcMagicSuccess(player, targetMonster, getSkill()))
		{
			targetMonster.getSpoilState().setSpoilerId(player.getObjectId());
			player.sendPacket(SystemMessageId.SPOIL_SUCCESS);
		}
		
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}