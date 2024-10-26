package com.shnok.javaserver.gameserver.handler.itemhandlers;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillUse;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PetFoods implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		final int itemId = item.getItemId();
		
		switch (itemId)
		{
			case 2515: // Wolf's food
				useFood(playable, 2048, item);
				break;
			
			case 4038: // Hatchling's food
				useFood(playable, 2063, item);
				break;
			
			case 5168: // Strider's food
				useFood(playable, 2101, item);
				break;
			
			case 5169: // ClanHall / Castle Strider's food
				useFood(playable, 2102, item);
				break;
			
			case 6316: // Wyvern's food
				useFood(playable, 2180, item);
				break;
			
			case 7582: // Baby Pet's food
				useFood(playable, 2048, item);
				break;
		}
	}
	
	private static boolean useFood(Playable playable, int magicId, ItemInstance item)
	{
		final L2Skill skill = SkillTable.getInstance().getInfo(magicId, 1);
		if (skill != null)
		{
			if (playable instanceof Pet pet)
			{
				if (pet.destroyItem(item.getObjectId(), 1, false))
				{
					// Send visual effect.
					playable.broadcastPacket(new MagicSkillUse(playable, playable, magicId, 1, 0, 0));
					
					// Put current value.
					pet.setCurrentFed(pet.getCurrentFed() + (skill.getFeed() * Config.PET_FOOD_RATE));
					
					// If pet is still hungry, send an alert.
					if (pet.checkAutoFeedState())
						pet.getOwner().sendPacket(SystemMessageId.YOUR_PET_ATE_A_LITTLE_BUT_IS_STILL_HUNGRY);
					
					return true;
				}
			}
			else if (playable instanceof Player player)
			{
				final int itemId = item.getItemId();
				
				if (player.isMounted() && player.getPetTemplate().canEatFood(itemId))
				{
					if (player.destroyItem(item.getObjectId(), 1, false))
					{
						player.broadcastPacket(new MagicSkillUse(playable, playable, magicId, 1, 0, 0));
						player.setCurrentFeed(player.getCurrentFeed() + (skill.getFeed() * Config.PET_FOOD_RATE));
					}
					return true;
				}
				
				playable.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addItemName(itemId));
				return false;
			}
		}
		return false;
	}
}