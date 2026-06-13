package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class WarehouseDepositList extends L2GameServerPacket
{
	public static final int PRIVATE = 1;
	public static final int CLAN = 2;
	public static final int CASTLE = 3; // not sure
	public static final int FREIGHT = 4; // not sure
	
	private final int _playerAdena;
	private final List<ItemInstance> _items;
	private final int _whType;
	
	public WarehouseDepositList(Player player, int type)
	{
		_whType = type;
		_playerAdena = player.getAdena();
		_items = new ArrayList<>();
		
		final boolean isPrivate = _whType == PRIVATE;
		for (ItemInstance temp : player.getInventory().getAvailableItems(true, isPrivate, false))
		{
			if (temp != null && temp.isDepositable(isPrivate))
				_items.add(temp);
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x41);
		writeH(_whType);
		writeD(_playerAdena);
		writeH(_items.size());
		
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
			if (temp.isAugmented())
			{
				writeD(0x0000FFFF & temp.getAugmentation().getId());
				writeD(temp.getAugmentation().getId() >> 16);
			}
			else
				writeQ(0x00);
		}
		_items.clear();
	}
}