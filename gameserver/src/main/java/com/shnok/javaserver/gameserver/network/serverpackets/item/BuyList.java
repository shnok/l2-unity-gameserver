package com.shnok.javaserver.gameserver.network.serverpackets.item;

import java.util.Collection;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.buylist.NpcBuyList;
import com.shnok.javaserver.gameserver.model.buylist.Product;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class BuyList extends L2GameServerPacket
{
	private final int _money;
	private final int _listId;
	private final Collection<Product> _list;
	private double _taxRate = 0;
	private final boolean _openTab;

	public BuyList(NpcBuyList list, int currentMoney, double taxRate)
	{
		_money = currentMoney;
		_listId = list.getListId();
		_list = list.values();
		_taxRate = taxRate;
		_openTab = true;
	}

	public BuyList(NpcBuyList list, int currentMoney, double taxRate, boolean openTab)
	{
		_money = currentMoney;
		_listId = list.getListId();
		_list = list.values();
		_taxRate = taxRate;
		_openTab = openTab;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x11);
		writeC(_openTab ? (byte) 1 : (byte) 0);
		writeD(_money);
		writeD(_listId);
		writeH(_list.size());
		
		for (Product product : _list)
		{
			if (product.getCount() > 0 || !product.hasLimitedStock())
			{
				writeH(product.getItem().getType1());
				writeD(product.getItemId());
				writeD(product.getItemId());
				writeD((product.getCount() < 0) ? 0 : product.getCount());
				writeH(product.getItem().getType2());
				writeH(0x00);
				writeD(product.getItem().getBodyPart());
				writeH(0x00);
				writeH(0x00);
				writeH(0x00);
				
				if (product.getItemId() >= 3960 && product.getItemId() <= 4026)
					writeD((int) (product.getPrice() * Config.RATE_SIEGE_GUARDS_PRICE * (1 + _taxRate)));
				else
					writeD((int) (product.getPrice() * (1 + _taxRate)));
			}
		}
	}
}
