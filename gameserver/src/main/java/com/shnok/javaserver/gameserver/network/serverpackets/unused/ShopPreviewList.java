package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.Collection;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.buylist.NpcBuyList;
import com.shnok.javaserver.gameserver.model.buylist.Product;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ShopPreviewList extends L2GameServerPacket
{
	private final int _listId;
	private final int _money;
	private final int _expertise;
	private final Collection<Product> _list;
	
	public ShopPreviewList(NpcBuyList list, int currentMoney, int expertiseIndex)
	{
		_listId = list.getListId();
		_list = list.values();
		_money = currentMoney;
		_expertise = expertiseIndex;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xef);
		writeC(0xc0); // ?
		writeC(0x13); // ?
		writeC(0x00); // ?
		writeC(0x00); // ?
		writeD(_money); // current money
		writeD(_listId);
		
		int newlength = 0;
		for (Product product : _list)
		{
			if (product.getItem().getCrystalType().getId() <= _expertise && product.getItem().isEquipable())
				newlength++;
		}
		writeH(newlength);
		
		for (Product product : _list)
		{
			if (product.getItem().getCrystalType().getId() <= _expertise && product.getItem().isEquipable())
			{
				writeD(product.getItemId());
				writeH(product.getItem().getType2()); // item type2
				
				if (product.getItem().getType1() != Item.TYPE1_ITEM_QUESTITEM_ADENA)
					writeH(product.getItem().getBodyPart()); // slot
				else
					writeH(0x00); // slot
					
				writeD(Config.WEAR_PRICE);
			}
		}
	}
}