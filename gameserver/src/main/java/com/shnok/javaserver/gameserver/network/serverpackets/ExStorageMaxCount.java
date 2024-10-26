package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;

public class ExStorageMaxCount extends L2GameServerPacket
{
	private final int _inventoryLimit;
	private final int _warehouseLimit;
	private final int _freightLimit;
	private final int _privateSellLimit;
	private final int _privateBuyLimit;
	private final int _dwarfRecipeLimit;
	private final int _commonRecipeLimit;
	
	public ExStorageMaxCount(Player player)
	{
		_inventoryLimit = player.getStatus().getInventoryLimit();
		_warehouseLimit = player.getStatus().getWareHouseLimit();
		_freightLimit = player.getStatus().getFreightLimit();
		_privateSellLimit = player.getStatus().getPrivateSellStoreLimit();
		_privateBuyLimit = player.getStatus().getPrivateBuyStoreLimit();
		_dwarfRecipeLimit = player.getStatus().getDwarfRecipeLimit();
		_commonRecipeLimit = player.getStatus().getCommonRecipeLimit();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x2e);
		writeD(_inventoryLimit);
		writeD(_warehouseLimit);
		writeD(_freightLimit);
		writeD(_privateSellLimit);
		writeD(_privateBuyLimit);
		writeD(_dwarfRecipeLimit);
		writeD(_commonRecipeLimit);
	}
}