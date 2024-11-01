package com.shnok.javaserver.gameserver.handler.targethandlers;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.handler.ITargetHandler;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.instance.Folk;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class TargetAura implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.AURA;
	}
	
	@Override
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill)
	{
		final List<Creature> list = new ArrayList<>();
		for (Creature creature : caster.getKnownTypeInRadius(Creature.class, skill.getSkillRadius()))
		{
			if (creature.isDead() || !GeoEngine.getInstance().canSeeTarget(caster, creature))
				continue;
			
			if (caster instanceof Playable playable && (creature instanceof Attackable || creature instanceof Playable))
			{
				if (creature.isAttackableWithoutForceBy(playable))
					list.add(creature);
			}
			else if (caster instanceof Attackable && creature instanceof Playable)
			{
				if (creature.isAttackableBy(caster))
					list.add(creature);
			}
			else if (caster instanceof Folk && creature instanceof Playable)
			{
				list.add(creature);
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
		if (skill.isOffensive() && caster.isInsideZone(ZoneId.PEACE))
		{
			caster.sendPacket(SystemMessageId.CANT_ATK_PEACEZONE);
			return false;
		}
		return true;
	}
}