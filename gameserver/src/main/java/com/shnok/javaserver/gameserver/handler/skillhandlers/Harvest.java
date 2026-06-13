package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.monster.SeedState;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Harvest implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.HARVEST
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (!(creature instanceof Player player))
			return;
		
		if (!(targets[0] instanceof Monster targetMonster))
		{
			player.sendPacket(SystemMessageId.THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN);
			return;
		}
		
		final SeedState seedState = targetMonster.getSeedState();
		if (!seedState.isSeeded())
		{
			player.sendPacket(SystemMessageId.THE_HARVEST_FAILED_BECAUSE_THE_SEED_WAS_NOT_SOWN);
			return;
		}
		
		if (seedState.isHarvested())
		{
			player.sendPacket(SystemMessageId.THE_HARVEST_HAS_FAILED);
			return;
		}
		
		if (!seedState.isAllowedToHarvest(player))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_HARVEST);
			return;
		}
		
		seedState.setHarvested();
		
		if (!calcSuccess(player, targetMonster))
		{
			player.sendPacket(SystemMessageId.THE_HARVEST_HAS_FAILED);
			return;
		}
		
		// Add item to the inventory.
		final IntIntHolder crop = seedState.getHarvestedCrop();
		player.addEarnedItem(crop.getId(), crop.getValue(), true);
		
		// Notify party members.
		if (player.isInParty())
		{
			SystemMessage sm;
			if (crop.getValue() > 1)
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HARVESTED_S3_S2S).addCharName(player).addItemName(crop.getId()).addNumber(crop.getValue());
			else
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HARVESTED_S2S).addCharName(player).addItemName(crop.getId());
			
			player.getParty().broadcastToPartyMembers(player, sm);
		}
	}
	
	private static boolean calcSuccess(Player player, Creature target)
	{
		int rate = 100;
		
		// Apply a 5% penalty for each level difference, above 5, between player and target levels.
		final int diff = Math.abs(player.getStatus().getLevel() - target.getStatus().getLevel());
		if (diff > 5)
			rate -= (diff - 5) * 5;
		
		// Success rate can't be lesser than 1%.
		return Rnd.get(100) < Math.max(1, rate);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}