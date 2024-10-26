package com.shnok.javaserver.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ChooseInventoryItem;

public class EnchantScrolls implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player player))
			return;
		
		if (player.getActiveEnchantItem() == null)
			player.sendPacket(SystemMessageId.SELECT_ITEM_TO_ENCHANT);
		
		player.setActiveEnchantItem(item);
		player.sendPacket(new ChooseInventoryItem(item.getItemId()));
	}
}
