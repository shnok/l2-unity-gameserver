package com.shnok.javaserver.gameserver.handler.itemhandlers;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ScrollsOfResurrection implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		final WorldObject obj = playable.getTarget();
		if (!(obj instanceof Creature targetCreature))
		{
			playable.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		if (targetCreature.isDead())
		{
			if (targetCreature instanceof Player targetPlayer)
			{
				// Check if the target isn't in a active siege zone.
				if (targetPlayer.isInsideZone(ZoneId.SIEGE) && targetPlayer.getSiegeState() == 0)
				{
					playable.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANNOT_BE_RESURRECTED_DURING_SIEGE));
					return;
				}
				
				// Check if the target is in a festival.
				if (targetPlayer.isFestivalParticipant())
				{
					playable.sendMessage("You may not resurrect participants in a festival.");
					return;
				}
				
				if (targetPlayer.isReviveRequested())
				{
					final Player player = (Player) playable;
					
					if (targetPlayer.isRevivingPet())
						player.sendPacket(SystemMessageId.CANNOT_RES_MASTER);
					else
						player.sendPacket(SystemMessageId.RES_HAS_ALREADY_BEEN_PROPOSED);
					
					return;
				}
			}
			else if (targetCreature instanceof Pet targetPet)
			{
				final Player player = (Player) playable;
				
				if (targetPet.getOwner() != player)
				{
					if (targetPet.getOwner().isReviveRequested())
					{
						if (targetPet.getOwner().isRevivingPet())
							player.sendPacket(SystemMessageId.RES_HAS_ALREADY_BEEN_PROPOSED);
						else
							player.sendPacket(SystemMessageId.CANNOT_RES_PET2);
						
						return;
					}
				}
			}
		}
		
		final IntIntHolder[] skills = item.getEtcItem().getSkills();
		if (skills == null)
		{
			LOGGER.warn("{} doesn't have any registered skill for handler.", item.getName());
			return;
		}
		
		for (IntIntHolder skillInfo : skills)
		{
			if (skillInfo == null)
				continue;
			
			final L2Skill itemSkill = skillInfo.getSkill();
			if (itemSkill == null)
				continue;
			
			// Scroll consumption is made on skill call, not on item call.
			playable.getAI().tryToCast(targetCreature, itemSkill);
		}
	}
}