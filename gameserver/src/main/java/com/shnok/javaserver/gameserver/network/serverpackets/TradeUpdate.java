package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.trade.TradeItem;

public class TradeUpdate extends L2GameServerPacket
{
	private final TradeItem _item;
	private final int _quantity;
	
	public TradeUpdate(TradeItem tradeItem, int quantity)
	{
		_item = tradeItem;
		_quantity = quantity;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x74);
		
		writeH(0x01);
		writeH((_quantity > 0 && _item.getItem().isStackable()) ? 3 : 2);
		writeH(_item.getItem().getType1());
		writeD(_item.getObjectId());
		writeD(_item.getItem().getItemId());
		writeD(_quantity);
		writeH(_item.getItem().getType2());
		writeH(0x00);
		writeD(_item.getItem().getBodyPart());
		writeH(_item.getEnchant());
		writeH(0x00);
		writeH(0x00);
	}
}