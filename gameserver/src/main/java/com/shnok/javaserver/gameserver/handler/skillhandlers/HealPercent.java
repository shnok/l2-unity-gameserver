package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.handler.SkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

public class HealPercent implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.HEAL_PERCENT,
		SkillType.MANAHEAL_PERCENT
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		final ISkillHandler handler = SkillHandler.getInstance().getHandler(SkillType.BUFF);
		if (handler != null)
			handler.useSkill(creature, skill, targets, item);
		
		final boolean isHp = skill.getSkillType() == SkillType.HEAL_PERCENT;
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (!targetCreature.canBeHealed())
				continue;
			
			double amount;
			if (isHp)
				amount = targetCreature.getStatus().addHp(targetCreature.getStatus().getMaxHp() * skill.getPower() / 100.);
			else
				amount = targetCreature.getStatus().addMp(targetCreature.getStatus().getMaxMp() * skill.getPower() / 100.);
			
			if (target instanceof Player targetPlayer)
			{
				SystemMessage sm;
				if (isHp)
				{
					if (creature != targetPlayer)
						sm = SystemMessage.getSystemMessage(SystemMessageId.S2_HP_RESTORED_BY_S1).addCharName(creature);
					else
						sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED);
				}
				else
				{
					if (creature != targetPlayer)
						sm = SystemMessage.getSystemMessage(SystemMessageId.S2_MP_RESTORED_BY_S1).addCharName(creature);
					else
						sm = SystemMessage.getSystemMessage(SystemMessageId.S1_MP_RESTORED);
				}
				sm.addNumber((int) amount);
				targetPlayer.sendPacket(sm);
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}