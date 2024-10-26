package com.shnok.javaserver.gameserver.handler.targethandlers;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.handler.ITargetHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.Folk;
import net.sf.l2j.gameserver.model.actor.instance.Guard;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

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