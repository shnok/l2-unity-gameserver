package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.trade.TradeItem;
import net.sf.l2j.gameserver.model.trade.TradeList;

public class PrivateStoreManageListSell extends L2GameServerPacket
{
	private final int _objectId;
	private final int _playerAdena;
	private final boolean _packageSale;
	private final List<TradeItem> _itemList;
	private final TradeList _sellList;
	
	public PrivateStoreManageListSell(Player player, boolean isPackageSale)
	{
		player.getSellList().updateItems(false);
		
		_objectId = player.getObjectId();
		_playerAdena = player.getAdena();
		_packageSale = player.getSellList().isPackaged() || isPackageSale;
		_itemList = player.getInventory().getItemsToSell();
		_sellList = player.getSellList();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x9a);
		writeD(_objectId);
		writeD(_packageSale ? 1 : 0);
		writeD(_playerAdena);
		
		writeD(_itemList.size());
		for (TradeItem item : _itemList)
		{
			writeD(item.getItem().getType2());
			writeD(item.getObjectId());
			writeD(item.getItem().getItemId());
			writeD(item.getCount());
			writeH(0x00);
			writeH(item.getEnchant());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeD(item.getPrice());
		}
		
		writeD(_sellList.size());
		for (TradeItem item : _sellList)
		{
			writeD(item.getItem().getType2());
			writeD(item.getObjectId());
			writeD(item.getItem().getItemId());
			writeD(item.getCount());
			writeH(0x00);
			writeH(item.getEnchant());
			writeH(0x00);
			writeD(item.getItem().getBodyPart());
			writeD(item.getPrice());
			writeD(item.getItem().getReferencePrice());
		}
	}
}