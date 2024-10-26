package com.shnok.javaserver.gameserver.handler.targethandlers;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.handler.ITargetHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

public class TargetCorpseAlly implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.CORPSE_ALLY;
	}
	
	@Override
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill)
	{
		final Player player = caster.getActingPlayer();
		
		final List<Player> list = new ArrayList<>();
		
		if (player.getClan() != null)
		{
			for (Player targetPlayer : player.getKnownTypeInRadius(Player.class, skill.getSkillRadius(), p -> p.isDead()))
			{
				// Target isn't a clan or alliance member, ignore it.
				if (!targetPlayer.isInSameClan(player) && !targetPlayer.isInSameAlly(player))
					continue;
				
				// Target isn't sharing same Duel team, ignore it.
				if (player.isInDuel() && (player.getDuelId() != targetPlayer.getDuelId() || player.getTeam() != targetPlayer.getTeam()))
					continue;
				
				list.add(targetPlayer);
			}
		}
		
		if (list.isEmpty())
			return new Creature[]
			{
				caster
			};
		
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
		final Player player = caster.getActingPlayer();
		if (player.isInOlympiadMode())
		{
			caster.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THIS_SKILL_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
			return false;
		}
		return true;
	}
}