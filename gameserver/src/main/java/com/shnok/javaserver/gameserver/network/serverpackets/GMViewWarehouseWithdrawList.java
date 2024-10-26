package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Set;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.model.pledge.Clan;

public class GMViewWarehouseWithdrawList extends L2GameServerPacket
{
	private final Set<ItemInstance> _items;
	private final String _playerName;
	private final int _money;
	
	public GMViewWarehouseWithdrawList(Player player)
	{
		_items = player.getWarehouse().getItems();
		_playerName = player.getName();
		_money = player.getWarehouse().getAdena();
	}
	
	public GMViewWarehouseWithdrawList(Clan clan)
	{
		_items = clan.getWarehouse().getItems();
		_playerName = clan.getLeaderName();
		_money = clan.getWarehouse().getAdena();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x95);
		writeS(_playerName);
		writeD(_money);
		writeH(_items.size());
		
		for (ItemInstance temp : _items)
		{
			Item item = temp.getItem();
			
			writeH(item.getType1());
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(temp.getCount());
			writeH(item.getType2());
			writeH(temp.getCustomType1());
			
			if (item.isEquipable())
			{
				writeD(item.getBodyPart());
				writeH(temp.getEnchantLevel());
				
				if (temp.isWeapon())
				{
					writeH(((Weapon) item).getSoulShotCount());
					writeH(((Weapon) item).getSpiritShotCount());
					
					if (temp.isAugmented())
					{
						writeD(0x0000FFFF & temp.getAugmentation().getId());
						writeD(temp.getAugmentation().getId() >> 16);
					}
					else
					{
						writeD(0);
						writeD(0);
					}
				}
				else
				{
					writeH(0);
					writeH(0);
					writeD(0);
					writeD(0);
				}
			}
			writeD(0);
		}
	}
}