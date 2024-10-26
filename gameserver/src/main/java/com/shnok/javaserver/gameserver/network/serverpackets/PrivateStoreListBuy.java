package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.trade.TradeItem;

public class PrivateStoreListBuy extends L2GameServerPacket
{
	private final int _objectId;
	private final int _playerAdena;
	private final List<TradeItem> _items;
	
	public PrivateStoreListBuy(Player player, Player storePlayer)
	{
		_objectId = storePlayer.getObjectId();
		_playerAdena = player.getAdena();
		_items = storePlayer.getBuyList().getAvailableItems(player.getInventory());
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb8);
		writeD(_objectId);
		writeD(_playerAdena);
		writeD(_items.size());
		
		for (TradeItem item : _items)
		{
			writeD(item.getObjectId());
			writeD(item.getItem().getItemId());
			writeH(item.getEnchant());
			writeD(item.getCount());
			writeD(item.getItem().getReferencePrice());
			writeH(0);
			writeD(item.getItem().getBodyPart());
			writeH(item.getItem().getType2());
			writeD(item.getPrice());
			writeD(item.getQuantity());
		}
	}
}