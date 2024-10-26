package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public class SellList extends L2GameServerPacket
{
	private final int _money;
	private final List<ItemInstance> _items;
	
	public SellList(int adena, List<ItemInstance> items)
	{
		_money = adena;
		_items = items;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x10);
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