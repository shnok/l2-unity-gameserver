package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public class DropItem extends L2GameServerPacket
{
	private final ItemInstance _item;
	private final int _charObjId;
	
	public DropItem(ItemInstance item, int playerObjId)
	{
		_item = item;
		_charObjId = playerObjId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x0c);
		writeD(_charObjId);
		writeD(_item.getObjectId());
		writeD(_item.getItemId());
		
		writeD(_item.getX());
		writeD(_item.getY());
		writeD(_item.getZ());
		
		// only show item count if it is a stackable item
		if (_item.isStackable())
			writeD(0x01);
		else
			writeD(0x00);
		writeD(_item.getCount());
		
		writeD(1); // unknown
	}
}