package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Spoil implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.SPOIL
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		// Must be called by a Player.
		if (!(creature instanceof Player player))
			return;
		
		for (WorldObject target : targets)
		{
			// Target must be a Monster.
			if (!(target instanceof Monster targetMonster))
				continue;
			
			// Target must be dead.
			if (targetMonster.isDead())
				continue;
			
			// Target mustn't be already in spoil state.
			if (targetMonster.getSpoilState().isSpoiled())
			{
				player.sendPacket(SystemMessageId.ALREADY_SPOILED);
				continue;
			}
			
			// Calculate the spoil success rate.
			if (Formulas.calcMagicSuccess(player, targetMonster, skill))
			{
				targetMonster.getSpoilState().setSpoilerId(player.getObjectId());
				
				player.sendPacket(SystemMessageId.SPOIL_SUCCESS);
			}
			else
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetMonster).addSkillName(skill.getId()));
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}