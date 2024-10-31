package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import java.util.EnumMap;
import java.util.Map;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.BuyListManager;
import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Merchant;
import com.shnok.javaserver.gameserver.model.buylist.NpcBuyList;
import com.shnok.javaserver.gameserver.model.buylist.Product;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.model.itemcontainer.Inventory;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ShopPreviewInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.actor.UserInfo;

public final class RequestPreviewItem extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private int _unk;
	private int _listId;
	private int _count;
	private int[] _items;
	
	@Override
	protected void readImpl()
	{
		_unk = readD();
		_listId = readD();
		_count = readD();
		
		if (_count < 0)
			_count = 0;
		else if (_count > 100)
			return; // prevent too long lists
			
		// Create _items table that will contain all ItemID to Wear
		_items = new int[_count];
		
		// Fill _items table with all ItemID to Wear
		for (int i = 0; i < _count; i++)
			_items[i] = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (_items == null)
			return;
		
		if (_count < 1 || _listId >= 4000000)
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Get the current player and return if null
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		// Check current target of the player and the INTERACTION_DISTANCE
		final WorldObject target = player.getTarget();
		if (!player.isGM() && (!(target instanceof Merchant) || !player.isIn3DRadius(target, Npc.INTERACTION_DISTANCE)))
			return;
		
		// Get the current merchant targeted by the player
		final Merchant merchant = (target instanceof Merchant targetMerchant) ? targetMerchant : null;
		if (merchant == null)
			return;
		
		final NpcBuyList buyList = BuyListManager.getInstance().getBuyList(_listId);
		if (buyList == null)
			return;
		
		long totalPrice = 0;
		
		_listId = buyList.getListId();
		
		final Map<Paperdoll, Integer> items = new EnumMap<>(Paperdoll.class);
		for (int i = 0; i < _count; i++)
		{
			int itemId = _items[i];
			
			final Product product = buyList.get(itemId);
			if (product == null)
				return;
			
			final Item template = product.getItem();
			if (template == null)
				continue;
			
			final Paperdoll slot = Inventory.getPaperdollIndex(template.getBodyPart());
			if (slot == Paperdoll.NULL)
				continue;
			
			if (items.containsKey(slot))
			{
				player.sendPacket(SystemMessageId.YOU_CAN_NOT_TRY_THOSE_ITEMS_ON_AT_THE_SAME_TIME);
				return;
			}
			items.put(slot, itemId);
			
			totalPrice += Config.WEAR_PRICE;
			if (totalPrice > Integer.MAX_VALUE)
				return;
		}
		
		// Charge buyer and add tax to castle treasury if not owned by npc clan because a Try On is not Free
		if (totalPrice < 0 || !player.reduceAdena((int) totalPrice, true))
		{
			player.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
			return;
		}
		
		if (!items.isEmpty())
		{
			player.sendPacket(new ShopPreviewInfo(items));
			
			// Schedule task
			ThreadPool.schedule(() ->
			{
				player.sendPacket(SystemMessageId.NO_LONGER_TRYING_ON);
				player.sendPacket(new UserInfo(player));
			}, Config.WEAR_DELAY * 1000L);
		}
	}
}