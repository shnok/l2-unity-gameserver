package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.enums.items.CrystalType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class RequestCrystallizeItem extends L2GameClientPacket
{
	private int _objectId;
	private int _count;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_count = readD();
	}
	
	@Override
	protected void runImpl()
	{
		// Sanity check.
		if (_count <= 0)
			return;
		
		// Client must be attached to a player instance.
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		// Player mustn't be already crystallizing or in store mode.
		if (player.isOperating() || player.isCrystallizing())
		{
			player.sendPacket(SystemMessageId.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE);
			return;
		}
		
		// Player must own Crystallize skill.
		final int skillLevel = player.getSkillLevel(L2Skill.SKILL_CRYSTALLIZE);
		if (skillLevel <= 0)
		{
			player.sendPacket(SystemMessageId.CRYSTALLIZE_LEVEL_TOO_LOW);
			return;
		}
		
		// Item must exist on player inventory. It must be a crystallizable item.
		final ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		if (item == null || item.isHeroItem() || item.isShadowItem())
			return;
		
		if (!item.getItem().isCrystallizable() || item.getItem().getCrystalCount() <= 0 || item.getItem().getCrystalType() == CrystalType.NONE)
			return;
		
		// Sanity check for count.
		_count = Math.min(_count, item.getCount());
		
		// Check if the player can crystallize items and return if false.
		boolean canCrystallize = true;
		
		switch (item.getItem().getCrystalType())
		{
			case C:
				if (skillLevel <= 1)
					canCrystallize = false;
				break;
			
			case B:
				if (skillLevel <= 2)
					canCrystallize = false;
				break;
			
			case A:
				if (skillLevel <= 3)
					canCrystallize = false;
				break;
			
			case S:
				if (skillLevel <= 4)
					canCrystallize = false;
				break;
		}
		
		if (!canCrystallize)
		{
			player.sendPacket(SystemMessageId.CRYSTALLIZE_LEVEL_TOO_LOW);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		player.setCrystallizing(true);
		
		// Unequip the item if needed.
		if (item.isEquipped())
		{
			player.getInventory().unequipItemInSlotAndRecord(item.getLocationSlot());
			
			if (item.getEnchantLevel() > 0)
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED).addNumber(item.getEnchantLevel()).addItemName(item.getItemId()));
			else
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED).addItemName(item.getItemId()));
		}
		
		// Remove the item from inventory.
		ItemInstance removedItem = player.getInventory().destroyItem(_objectId, _count);
		
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CRYSTALLIZED).addItemName(removedItem.getItemId()));
		
		// add crystals
		player.addItem(item.getItem().getCrystalItemId(), item.getCrystalCount(), true);
		
		player.broadcastUserInfo();
		player.setCrystallizing(false);
	}
}