package com.shnok.javaserver.gameserver.handler.targethandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.handler.ITargetHandler;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class TargetSummon implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.SUMMON;
	}
	
	@Override
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill)
	{
		final Summon summon = caster.getSummon();
		if (summon == null)
			return EMPTY_TARGET_ARRAY;
		
		return new Creature[]
		{
			summon
		};
	}
	
	@Override
	public Creature getFinalTarget(Creature caster, Creature target, L2Skill skill)
	{
		final Summon summon = caster.getSummon();
		if (summon == null)
			return null;
		
		return summon;
	}
	
	@Override
	public boolean meetCastConditions(Playable caster, Creature target, L2Skill skill, boolean isCtrlPressed)
	{
		final Summon summon = caster.getSummon();
		if (summon == null || summon.isDead())
		{
			caster.sendPacket(SystemMessageId.INVALID_TARGET);
			return false;
		}
		
		return true;
	}
}