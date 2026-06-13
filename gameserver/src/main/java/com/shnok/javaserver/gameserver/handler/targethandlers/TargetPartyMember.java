package com.shnok.javaserver.gameserver.handler.targethandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.handler.ITargetHandler;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class TargetPartyMember implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.PARTY_MEMBER;
	}
	
	@Override
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill)
	{
		return new Creature[]
		{
			target
		};
	}
	
	@Override
	public Creature getFinalTarget(Creature caster, Creature target, L2Skill skill)
	{
		return target;
	}
	
	@Override
	public boolean meetCastConditions(Playable caster, Creature target, L2Skill skill, boolean isCtrlPressed)
	{
		// Always work on ourself.
		if (caster == target)
			return true;
		
		// For Summon Friend
		if (skill.getId() == 1403)
		{
			// Doesn't work on non Player targets, or on dead targets.
			if (!(target instanceof Player) || target.isDead())
			{
				caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
				return false;
			}
		}
		// For regular skills
		else
		{
			// Always work on self Summon.
			final Summon summon = caster.getSummon();
			if (summon != null && target == summon)
				return true;
			
			// Doesn't work on non Playable targets, or on dead targets.
			if (!(target instanceof Playable) || target.isDead())
			{
				caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
				return false;
			}
		}
		
		// Target isn't a Party member, ignore it.
		if (!caster.isInParty() || !caster.getParty().containsPlayer(target.getActingPlayer()))
		{
			caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
			return false;
		}
		
		return true;
	}
}