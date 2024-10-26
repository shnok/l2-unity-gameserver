package com.shnok.javaserver.gameserver.handler.targethandlers;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.handler.ITargetHandler;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class TargetClan implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.CLAN;
	}
	
	@Override
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill)
	{
		final List<Creature> list = new ArrayList<>();
		// TODO : Currently SkillTargetType.CLAN is used only by NPC skills.
		if (caster instanceof Attackable attackable)
		{
			list.add(caster);
			for (Attackable targetAttackable : caster.getKnownTypeInRadius(Attackable.class, skill.getSkillRadius()))
			{
				if (targetAttackable.isDead() || !ArraysUtil.contains(attackable.getTemplate().getClans(), targetAttackable.getTemplate().getClans()))
					continue;
				
				list.add(targetAttackable);
			}
		}
		
		if (list.isEmpty())
			return EMPTY_TARGET_ARRAY;
		
		return list.toArray(new Creature[list.size()]);
	}
	
	@Override
	public Creature getFinalTarget(Creature caster, Creature target, L2Skill skill)
	{
		return caster;
	}
	
	@Override
	public boolean meetCastConditions(Playable caster, Creature target, L2Skill skill, boolean isCtrlPressed)
	{
		return true;
	}
}