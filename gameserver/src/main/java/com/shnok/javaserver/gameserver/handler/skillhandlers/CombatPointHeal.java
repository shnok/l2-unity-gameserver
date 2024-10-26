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

public class CombatPointHeal implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.COMBATPOINTHEAL
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		final ISkillHandler handler = SkillHandler.getInstance().getHandler(SkillType.BUFF);
		if (handler != null)
			handler.useSkill(creature, skill, targets, item);
		
		for (WorldObject obj : targets)
		{
			if (!(obj instanceof Player targetPlayer))
				continue;
			
			if (targetPlayer.isDead() || targetPlayer.isInvul())
				continue;
			
			double baseCp = skill.getPower();
			
			final double currentCp = targetPlayer.getStatus().getCp();
			final double maxCp = targetPlayer.getStatus().getMaxCp();
			
			if ((currentCp + baseCp) > maxCp)
				baseCp = maxCp - currentCp;
			
			targetPlayer.getStatus().setCp(baseCp + currentCp);
			
			if (creature instanceof Player player && player != targetPlayer)
				targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_CP_WILL_BE_RESTORED_BY_S1).addCharName(player).addNumber((int) baseCp));
			else
				targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CP_WILL_BE_RESTORED).addNumber((int) baseCp));
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}