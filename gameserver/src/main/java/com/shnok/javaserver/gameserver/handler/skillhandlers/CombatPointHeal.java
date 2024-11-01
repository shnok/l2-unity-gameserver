package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.handler.SkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

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