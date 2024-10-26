package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.data.manager.CastleManorManager;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.PcInventory;
import net.sf.l2j.gameserver.model.manor.CropProcure;
import net.sf.l2j.gameserver.model.manor.Seed;

public class ExShowSellCropList extends L2GameServerPacket
{
	private int _manorId = 1;
	private final Map<Integer, ItemInstance> _cropsItems;
	private final Map<Integer, CropProcure> _castleCrops;
	
	public ExShowSellCropList(PcInventory inventory, int manorId)
	{
		_manorId = manorId;
		_castleCrops = new HashMap<>();
		_cropsItems = new HashMap<>();
		
		for (int cropId : CastleManorManager.getInstance().getCropIds())
		{
			final ItemInstance item = inventory.getItemByItemId(cropId);
			if (item != null)
				_cropsItems.put(cropId, item);
		}
		
		for (CropProcure crop : CastleManorManager.getInstance().getCropProcure(_manorId, false))
		{
			if (_cropsItems.containsKey(crop.getId()) && crop.getAmount() > 0)
				_castleCrops.put(crop.getId(), crop);
		}
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xFE);
		writeH(0x21);
		
		writeD(_manorId);
		writeD(_cropsItems.size());
		
		for (ItemInstance item : _cropsItems.values())
		{
			final Seed seed = CastleManorManager.getInstance().getSeedByCrop(item.getItemId());
			
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeD(seed.getLevel());
			writeC(1);
			writeD(seed.getReward1());
			writeC(1);
			writeD(seed.getReward2());
			
			final CropProcure crop = _castleCrops.get(item.getItemId());
			if (crop != null)
			{
				writeD(_manorId);
				writeD(crop.getAmount());
				writeD(crop.getPrice());
				writeC(crop.getReward());
			}
			else
			{
				writeD(0xFFFFFFFF);
				writeD(0);
				writeD(0);
				writeC(0);
			}
			writeD(item.getCount());
		}
	}
}