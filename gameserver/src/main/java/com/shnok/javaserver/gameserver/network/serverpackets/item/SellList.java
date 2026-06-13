package com.shnok.javaserver.gameserver.network.serverpackets.item;

import java.util.List;

import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class SellList extends L2GameServerPacket
{
	private final int _money;
	private final List<ItemInstance> _items;
	private final boolean _openTab;
	
	public SellList(int adena, List<ItemInstance> items, boolean openTab)
	{
		_money = adena;
		_items = items;
		_openTab = openTab;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x10);
		writeC(_openTab ? (byte) 1 : (byte) 0);
		writeD(_money);
		writeD(0x00);
		writeH(_items.size());
		
		for (ItemInstance item : _items)
		{
			writeH(item.getItem().getType1());
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(item.getCount());
			writeH(item.getItem().getType2());
			writeH(item.getCustomType1());
			writeD(item.getItem().getBodyPart());
			writeH(item.getEnchantLevel());
			writeH(item.getCustomType2());
			writeH(0x00);
			writeD(item.getItem().getReferencePrice() / 2);
		}
	}
}
