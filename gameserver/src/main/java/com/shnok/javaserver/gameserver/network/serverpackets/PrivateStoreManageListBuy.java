package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.trade.TradeItem;
import com.shnok.javaserver.gameserver.model.trade.TradeList;

public class PrivateStoreManageListBuy extends L2GameServerPacket
{
	private final int _objectId;
	private final int _playerAdena;
	private final List<ItemInstance> _itemList;
	private final TradeList _buyList;
	
	public PrivateStoreManageListBuy(Player player)
	{
		player.getBuyList().updateItems(true);
		
		_objectId = player.getObjectId();
		_playerAdena = player.getAdena();
		_itemList = player.getInventory().getUniqueItems(false, false, true, true, false);
		_buyList = player.getBuyList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb7);
		writeD(_objectId);
		writeD(_playerAdena);
		
		writeD(_itemList.size());
		for (ItemInstance item : _itemList)
		{
			writeD(item.getItemId());
			writeH(item.getEnchantLevel());
			writeD(item.getCount());
			writeD(item.getReferencePrice());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeH(item.getItem().getType2());
		}
		
		writeD(_buyList.size());
		for (TradeItem item : _buyList)
		{
			writeD(item.getItem().getItemId());
			writeH(item.getEnchant());
			writeD(item.getQuantity());
			writeD(item.getItem().getReferencePrice());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeH(item.getItem().getType2());
			writeD(item.getPrice());
			writeD(item.getItem().getReferencePrice());
		}
	}
}