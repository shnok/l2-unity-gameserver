package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Chest;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Unlock implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.UNLOCK,
		SkillType.UNLOCK_SPECIAL,
		SkillType.DELUXE_KEY_UNLOCK // Skill ids: 2065, 2229
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		// Must be called by a Player.
		if (!(creature instanceof Player player))
			return;
		
		if (targets[0] instanceof Door doorTarget)
		{
			if (!doorTarget.isUnlockable() && skill.getSkillType() != SkillType.UNLOCK_SPECIAL)
			{
				player.sendPacket(SystemMessageId.UNABLE_TO_UNLOCK_DOOR);
				return;
			}
			
			if (!doorTarget.isOpened() && Formulas.doorUnlock(skill))
				doorTarget.openMe();
			else
				player.sendPacket(SystemMessageId.FAILED_TO_UNLOCK_DOOR);
		}
		else if (targets[0] instanceof Chest chestTarget)
		{
			if (chestTarget.isDead() || chestTarget.isInteracted())
				return;
			
			if (!chestTarget.isBox())
			{
				chestTarget.getAI().addAttackDesire(player, 200);
				return;
			}
			
			chestTarget.setInteracted();
			
			if (Formulas.chestUnlock(skill, chestTarget.getStatus().getLevel()))
			{
				// Add some hate, so Monster#calculateRewards is evaluated properly.
				chestTarget.getAI().getAggroList().addDamageHate(player, 0, 200);
				chestTarget.doDie(player);
			}
			else
				chestTarget.deleteMe();
		}
		else
			player.sendPacket(SystemMessageId.INVALID_TARGET);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}