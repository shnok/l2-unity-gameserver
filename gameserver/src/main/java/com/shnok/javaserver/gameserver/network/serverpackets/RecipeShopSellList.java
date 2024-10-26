package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.records.ManufactureItem;

public class RecipeShopSellList extends L2GameServerPacket
{
	private final int _adena;
	
	private final int _objectId;
	private final int _mp;
	private final int _maxMp;
	private final List<ManufactureItem> _manufactureList;
	
	public RecipeShopSellList(Player buyer, Player manufacturer)
	{
		_adena = buyer.getAdena();
		
		_objectId = manufacturer.getObjectId();
		_mp = (int) manufacturer.getStatus().getMp();
		_maxMp = manufacturer.getStatus().getMaxMp();
		_manufactureList = new ArrayList<>(manufacturer.getManufactureList());
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xd9);
		writeD(_objectId);
		writeD(_mp);
		writeD(_maxMp);
		writeD(_adena);
		
		writeD(_manufactureList.size());
		
		for (ManufactureItem item : _manufactureList)
		{
			writeD(item.recipeId());
			writeD(0x00); // unknown
			writeD(item.cost());
		}
	}
}