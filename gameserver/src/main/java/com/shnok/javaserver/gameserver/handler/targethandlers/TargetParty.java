package com.shnok.javaserver.gameserver.handler.targethandlers;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.handler.ITargetHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class TargetParty implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.PARTY;
	}
	
	@Override
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill)
	{
		final Player player = caster.getActingPlayer();
		
		final List<Creature> list = new ArrayList<>();
		list.add(player);
		
		for (Playable playable : player.getKnownTypeInRadius(Playable.class, skill.getSkillRadius(), p -> !p.isDead()))
		{
			// Bypass other checks if target is Player's Summon.
			if (playable != player.getSummon())
			{
				// Target isn't a Party member, ignore it.
				if (!playable.isInSameParty(player))
					continue;
			}
			
			list.add(playable);
		}
		
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