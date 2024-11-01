package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class SpawnItem extends L2GameServerPacket
{
	private final int _objectId;
	private final int _itemId;
	private final int _x;
	private final int _y;
	private final int _z;
	private final int _stackable;
	private final int _count;
	
	public SpawnItem(ItemInstance item)
	{
		_objectId = item.getObjectId();
		_itemId = item.getItemId();
		_x = item.getX();
		_y = item.getY();
		_z = item.getZ();
		_stackable = item.isStackable() ? 0x01 : 0x00;
		_count = item.getCount();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x0b);
		writeD(_objectId);
		writeD(_itemId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
		writeD(_stackable);
		writeD(_count);
		writeD(0x00);
	}
}