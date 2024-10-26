package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

/**
 * Format: (ch)ddd
 */
public class ExConfirmCancelItem extends L2GameServerPacket
{
	private final int _itemObjId;
	private final int _itemId;
	private final int _itemAug1;
	private final int _itemAug2;
	private final int _price;
	
	public ExConfirmCancelItem(ItemInstance item, int price)
	{
		_itemObjId = item.getObjectId();
		_itemId = item.getItemId();
		_price = price;
		_itemAug1 = ((short) item.getAugmentation().getId());
		_itemAug2 = item.getAugmentation().getId() >> 16;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x56);
		writeD(_itemObjId);
		writeD(_itemId);
		writeD(_itemAug1);
		writeD(_itemAug2);
		writeQ(_price);
		writeD(0x01);
	}
}