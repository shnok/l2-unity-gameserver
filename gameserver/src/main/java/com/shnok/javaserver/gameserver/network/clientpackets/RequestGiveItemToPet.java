package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.items.EtcItemType;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;

public final class RequestGiveItemToPet extends L2GameClientPacket
{
	private int _objectId;
	private int _amount;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_amount = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (_amount <= 0)
			return;
		
		final Player player = getClient().getPlayer();
		if (player == null || !player.hasPet())
			return;
		
		// Alt game - Karma punishment
		if (!Config.KARMA_PLAYER_CAN_TRADE && player.getKarma() > 0)
		{
			player.sendMessage("You cannot trade in a chaotic state.");
			return;
		}
		
		if (player.isOperating())
		{
			player.sendPacket(SystemMessageId.CANNOT_PICKUP_OR_USE_ITEM_WHILE_TRADING);
			return;
		}
		
		if (player.isProcessingTransaction())
		{
			player.sendPacket(SystemMessageId.ALREADY_TRADING);
			return;
		}
		
		final ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		if (item == null || item.isAugmented())
			return;
		
		if (item.isHeroItem() || !item.isDropable() || !item.isDestroyable() || !item.isTradable() || item.getItem().getItemType() == EtcItemType.ARROW || item.getItem().getItemType() == EtcItemType.SHOT)
		{
			player.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return;
		}
		
		final Pet pet = (Pet) player.getSummon();
		if (pet.isDead())
		{
			player.sendPacket(SystemMessageId.CANNOT_GIVE_ITEMS_TO_DEAD_PET);
			return;
		}
		
		if (!player.isIn3DRadius(pet, Npc.INTERACTION_DISTANCE))
		{
			player.sendPacket(SystemMessageId.TARGET_TOO_FAR);
			return;
		}
		
		if (!pet.getInventory().validateCapacity(item))
		{
			player.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_ANY_MORE_ITEMS);
			return;
		}
		
		if (!pet.getInventory().validateWeight(item, _amount))
		{
			player.sendPacket(SystemMessageId.UNABLE_TO_PLACE_ITEM_YOUR_PET_IS_TOO_ENCUMBERED);
			return;
		}
		
		player.cancelActiveEnchant();
		
		player.transferItem(_objectId, _amount, pet);
	}
}