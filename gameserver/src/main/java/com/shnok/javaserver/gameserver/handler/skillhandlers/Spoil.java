package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

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