package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.data.manager.CastleManorManager;
import net.sf.l2j.gameserver.model.manor.CropProcure;
import net.sf.l2j.gameserver.model.residence.castle.Castle;

public class ExShowProcureCropDetail extends L2GameServerPacket
{
	private final Map<Integer, CropProcure> _castleCrops = new HashMap<>();
	
	private final int _cropId;
	
	public ExShowProcureCropDetail(int cropId)
	{
		_cropId = cropId;
		
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			final CropProcure cropItem = CastleManorManager.getInstance().getCropProcure(castle.getId(), cropId, false);
			if (cropItem != null && cropItem.getAmount() > 0)
				_castleCrops.put(castle.getId(), cropItem);
		}
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xFE);
		writeH(0x22);
		
		writeD(_cropId);
		writeD(_castleCrops.size());
		
		for (Map.Entry<Integer, CropProcure> entry : _castleCrops.entrySet())
		{
			final CropProcure crop = entry.getValue();
			
			writeD(entry.getKey());
			writeD(crop.getAmount());
			writeD(crop.getPrice());
			writeC(crop.getReward());
		}
	}
}