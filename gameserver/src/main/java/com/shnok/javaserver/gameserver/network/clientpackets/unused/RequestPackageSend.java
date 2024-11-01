package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Folk;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.itemcontainer.ItemContainer;
import com.shnok.javaserver.gameserver.model.itemcontainer.PcFreight;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestPackageSend extends L2GameClientPacket
{
	private int _objectId;
	private List<IntIntHolder> _items;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		
		int count = readD();
		if (count < 0 || count > Config.MAX_ITEM_IN_PACKET)
			return;
		
		_items = new ArrayList<>(count);
		
		for (int i = 0; i < count; i++)
		{
			int id = readD();
			int cnt = readD();
			
			_items.add(new IntIntHolder(id, cnt));
		}
	}
	
	@Override
	protected void runImpl()
	{
		if (_items == null || _items.isEmpty() || !Config.ALLOW_FREIGHT)
			return;
		
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		// player attempts to send freight to the different account
		if (!player.getAccountChars().containsKey(_objectId))
			return;
		
		final PcFreight freight = player.getDepositedFreight(_objectId);
		player.setActiveWarehouse(freight);
		
		final ItemContainer warehouse = player.getActiveWarehouse();
		if (warehouse == null)
			return;
		
		final Folk folk = player.getCurrentFolk();
		if ((folk == null || !player.isIn3DRadius(folk, Npc.INTERACTION_DISTANCE)) && !player.isGM())
			return;
		
		if (warehouse instanceof PcFreight && !player.getAccessLevel().allowTransaction())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		// Alt game - Karma punishment
		if (!Config.KARMA_PLAYER_CAN_USE_WH && player.getKarma() > 0)
			return;
		
		// Freight price from config or normal price per item slot (30)
		int fee = _items.size() * Config.FREIGHT_PRICE;
		int currentAdena = player.getAdena();
		int slots = 0;
		
		for (IntIntHolder i : _items)
		{
			int count = i.getValue();
			
			// Check validity of requested item
			ItemInstance item = player.checkItemManipulation(i.getId(), count);
			if (item == null)
			{
				i.setId(0);
				i.setValue(0);
				continue;
			}
			
			if (!item.isTradable() || item.isQuestItem())
				return;
			
			// Calculate needed adena and slots
			if (item.getItemId() == 57)
				currentAdena -= count;
			
			if (!item.isStackable())
				slots += count;
			else if (warehouse.getItemByItemId(item.getItemId()) == null)
				slots++;
		}
		
		// Item Max Limit Check
		if (!warehouse.validateCapacity(slots))
		{
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_EXCEEDED_QUANTITY_THAT_CAN_BE_INPUTTED));
			return;
		}
		
		// Check if enough adena and charge the fee
		if (currentAdena < fee || !player.reduceAdena(fee, false))
		{
			sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_NOT_ENOUGH_ADENA));
			return;
		}
		
		// Proceed to the transfer
		for (IntIntHolder i : _items)
		{
			int objectId = i.getId();
			int count = i.getValue();
			
			// check for an invalid item
			if (objectId == 0 && count == 0)
				continue;
			
			ItemInstance oldItem = player.getInventory().getItemByObjectId(objectId);
			if (oldItem == null || oldItem.isHeroItem())
				continue;
			
			player.getInventory().transferItem(objectId, count, warehouse);
		}
	}
}