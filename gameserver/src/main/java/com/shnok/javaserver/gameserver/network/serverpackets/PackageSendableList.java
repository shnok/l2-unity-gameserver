package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;

public class PackageSendableList extends L2GameServerPacket
{
	private final ItemInstance[] _items;
	private final int _objectId;
	
	public PackageSendableList(ItemInstance[] items, int objectId)
	{
		_items = items;
		_objectId = objectId;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xC3);
		writeD(_objectId);
		writeD(getClient().getPlayer().getAdena());
		writeD(_items.length);
		
		for (ItemInstance temp : _items)
		{
			if (temp == null || temp.getItem() == null)
				continue;
			
			Item item = temp.getItem();
			
			writeH(item.getType1());
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(temp.getCount());
			writeH(item.getType2());
			writeH(temp.getCustomType1());
			writeD(item.getBodyPart());
			writeH(temp.getEnchantLevel());
			writeH(temp.getCustomType2());
			writeH(0x00);
			writeD(temp.getObjectId());
		}
	}
}