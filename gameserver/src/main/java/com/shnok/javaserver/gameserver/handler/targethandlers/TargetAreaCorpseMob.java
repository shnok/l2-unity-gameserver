package com.shnok.javaserver.gameserver.handler.targethandlers;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.handler.ITargetHandler;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;

public class TargetAreaCorpseMob implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.AREA_CORPSE_MOB;
	}
	
	@Override
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill)
	{
		final List<Creature> list = new ArrayList<>();
		list.add(target);
		
		for (Creature creature : target.getKnownTypeInRadius(Creature.class, skill.getSkillRadius()))
		{
			if (creature == caster || !GeoEngine.getInstance().canSeeTarget(target, creature))
				continue;
			
			if (skill.getId() == 444)
			{
				if (creature instanceof Attackable && creature.isDead())
					list.add(creature);
				
				continue;
			}
			else if (creature.isDead())
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
		}
		
		return list.toArray(new Creature[list.size()]);
	}
	
	@Override
	public Creature getFinalTarget(Creature caster, Creature target, L2Skill skill)
	{
		return target;
	}
	
	@Override
	public boolean meetCastConditions(Playable caster, Creature target, L2Skill skill, boolean isCtrlPressed)
	{
		// Verify if a corpse exists. Can't do anything on Pets.
		final Long time = DecayTaskManager.getInstance().get(target);
		if (time == null || target instanceof Pet)
		{
			caster.sendPacket(SystemMessageId.INVALID_TARGET);
			return false;
		}
		
		// Harvest can be done anytime.
		if (skill.getSkillType() == SkillType.HARVEST)
		{
			if (!(target instanceof Monster))
			{
				caster.sendPacket(SystemMessageId.THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN);
				return false;
			}
			
			return true;
		}
		
		// Cast NpcTemplate over Creature template since only pets, servitors and NPCs are stored into DecayTaskManager, and all inherits from NpcTemplate.
		final NpcTemplate template = (NpcTemplate) target.getTemplate();
		
		// Check last corpse action time. That behavior exists whenever the target isn't seeded or spoiled.
		final boolean isSeededOrSpoiled = target instanceof Monster targetMonster && (targetMonster.getSeedState().isSeeded() || targetMonster.getSpoilState().isSpoiled());
		if (!isSeededOrSpoiled && System.currentTimeMillis() >= time - (template.getCorpseTime() * 1000 / 2))
		{
			caster.sendPacket(SystemMessageId.CORPSE_TOO_OLD_SKILL_NOT_USED);
			return false;
		}
		
		// Sweep always end as true if the target is a Monster ; otherwise, it's always false.
		if (skill.getSkillType() == SkillType.SWEEP && !(target instanceof Monster))
		{
			caster.sendPacket(SystemMessageId.SWEEPER_FAILED_TARGET_NOT_SPOILED);
			return false;
		}
		
		return true;
	}
}