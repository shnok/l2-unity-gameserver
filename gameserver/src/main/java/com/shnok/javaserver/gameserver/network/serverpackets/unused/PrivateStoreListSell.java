package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.trade.TradeItem;
import com.shnok.javaserver.gameserver.model.trade.TradeList;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PrivateStoreListSell extends L2GameServerPacket
{
	private final int _objectId;
	private final int _playerAdena;
	private final TradeList _items;
	
	public PrivateStoreListSell(Player player, Player storePlayer)
	{
		_objectId = storePlayer.getObjectId();
		_playerAdena = player.getAdena();
		_items = storePlayer.getSellList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x9b);
		writeD(_objectId);
		writeD(_items.isPackaged() ? 1 : 0);
		writeD(_playerAdena);
		writeD(_items.size());
		
		for (TradeItem item : _items)
		{
			writeD(item.getItem().getType2());
			writeD(item.getObjectId());
			writeD(item.getItem().getItemId());
			writeD(item.getCount());
			writeH(0x00);
			writeH(item.getEnchant());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeD(item.getPrice()); // your price
			writeD(item.getItem().getReferencePrice()); // store price
		}
	}
}