package com.shnok.javaserver.gameserver.handler.targethandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.handler.ITargetHandler;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class TargetPartyOther implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.PARTY_OTHER;
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
		if (caster == target)
		{
			caster.sendPacket(SystemMessageId.CANNOT_USE_ON_YOURSELF);
			return false;
		}
		
		if (!(target instanceof Player targetPlayer) || target.isDead())
		{
			caster.sendPacket(SystemMessageId.INVALID_TARGET);
			return false;
		}
		
		if ((skill.getId() == 426 && targetPlayer.isMageClass()) || (skill.getId() == 427 && !targetPlayer.isMageClass()))
		{
			caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
			return false;
		}
		
		if (!caster.isInParty() || !caster.getParty().containsPlayer(targetPlayer))
		{
			caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
			return false;
		}
		
		return true;
	}
}