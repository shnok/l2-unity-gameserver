package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class RequestUnEquipItem extends L2GameClientPacket
{
	private int _slot;
	
	@Override
	protected void readImpl()
	{
		_slot = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		// Prevent of unequiping a cursed weapon
		if (_slot == Item.SLOT_LR_HAND && player.isCursedWeaponEquipped())
			return;
		
		final ItemInstance item = player.getInventory().getItemFrom(_slot);
		if (item == null)
			return;
			
		// Prevent player from unequipping items in special conditions
		// Unequip item on advExt sends the error message if castingNow
		// This is rather stupid, since UseItem achieves the same effect and is allowed.
		if (player.getCast().isCastingNow() || player.isStunned() || player.isSleeping() || player.isParalyzed() || player.isAfraid() || player.isAlikeDead())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addItemName(item));
			return;
		}
		
		final ItemInstance[] unequipped = player.getInventory().unequipItemInBodySlotAndRecord(_slot);
		
		for (ItemInstance itm : unequipped)
			itm.unChargeAllShots();
		
		player.broadcastUserInfo();
		
		// this can be 0 if the user pressed the right mousebutton twice very fast
		if (unequipped.length > 0)
		{
			SystemMessage sm = null;
			if (unequipped[0].getEnchantLevel() > 0)
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED);
				sm.addNumber(unequipped[0].getEnchantLevel());
				sm.addItemName(unequipped[0]);
			}
			else
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED);
				sm.addItemName(unequipped[0]);
			}
			player.sendPacket(sm);
		}
	}
}