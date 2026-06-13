package com.shnok.javaserver.gameserver.handler.targethandlers;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.handler.ITargetHandler;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.actor.instance.Folk;
import com.shnok.javaserver.gameserver.model.actor.instance.Guard;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class TargetOne implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.ONE;
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
		if (target == null)
			return false;
		
		if (skill.isOffensive())
		{
			if (target.isDead() || target == caster)
			{
				caster.sendPacket(SystemMessageId.INVALID_TARGET);
				return false;
			}
			
			if (target instanceof Playable targetPlayable)
			{
				if (!caster.getActingPlayer().canCastOffensiveSkillOnPlayable(targetPlayable, skill, isCtrlPressed))
				{
					caster.sendPacket(SystemMessageId.INVALID_TARGET);
					return false;
				}
				
				if (caster.getActingPlayer().isInOlympiadMode() && !caster.getActingPlayer().isOlympiadStart())
				{
					caster.sendPacket(SystemMessageId.INVALID_TARGET);
					return false;
				}
				
				if (caster.isInsideZone(ZoneId.PEACE))
				{
					caster.sendPacket(SystemMessageId.CANT_ATK_PEACEZONE);
					return false;
				}
				
				if (targetPlayable.isInsideZone(ZoneId.PEACE))
				{
					caster.sendPacket(SystemMessageId.TARGET_IN_PEACEZONE);
					return false;
				}
			}
			else if (target instanceof Folk || target instanceof Guard)
			{
				// You can damage Folk and Guard with CTRL, but nothing else.
				if (!skill.isDamage() || !isCtrlPressed)
				{
					caster.sendPacket(SystemMessageId.INVALID_TARGET);
					return false;
				}
			}
			else if (target instanceof Door && !target.isAttackableBy(caster))
			{
				caster.sendPacket(SystemMessageId.INVALID_TARGET);
				return false;
			}
		}
		else
		{
			if (target instanceof Playable targetPlayable)
			{
				if (!caster.getActingPlayer().canCastBeneficialSkillOnPlayable(targetPlayable, skill, isCtrlPressed))
				{
					caster.sendPacket(SystemMessageId.INVALID_TARGET);
					return false;
				}
			}
			else if (target instanceof Monster && !isCtrlPressed)
			{
				caster.sendPacket(SystemMessageId.INVALID_TARGET);
				return false;
			}
		}
		return true;
	}
}