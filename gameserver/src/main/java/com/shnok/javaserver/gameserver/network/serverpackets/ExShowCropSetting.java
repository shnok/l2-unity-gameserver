package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.model.manor.CropProcure;
import com.shnok.javaserver.gameserver.model.manor.Seed;

public class ExShowCropSetting extends L2GameServerPacket
{
	private final int _manorId;
	private final Set<Seed> _seeds;
	private final Map<Integer, CropProcure> _current = new HashMap<>();
	private final Map<Integer, CropProcure> _next = new HashMap<>();
	
	public ExShowCropSetting(int manorId)
	{
		final CastleManorManager manor = CastleManorManager.getInstance();
		
		_manorId = manorId;
		_seeds = manor.getSeedsForCastle(_manorId);
		for (Seed s : _seeds)
		{
			// Current period
			CropProcure cp = manor.getCropProcure(manorId, s.getCropId(), false);
			if (cp != null)
				_current.put(s.getCropId(), cp);
			
			// Next period
			cp = manor.getCropProcure(manorId, s.getCropId(), true);
			if (cp != null)
				_next.put(s.getCropId(), cp);
		}
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xFE);
		writeH(0x20);
		
		writeD(_manorId);
		writeD(_seeds.size());
		
		for (Seed s : _seeds)
		{
			writeD(s.getCropId());
			writeD(s.getLevel());
			writeC(1);
			writeD(s.getReward1());
			writeC(1);
			writeD(s.getReward2());
			
			writeD(s.getCropsLimit());
			writeD(0);
			writeD(s.getCropMinPrice());
			writeD(s.getCropMaxPrice());
			
			// Current period
			CropProcure cp = _current.get(s.getCropId());
			if (cp != null)
			{
				writeD(cp.getStartAmount());
				writeD(cp.getPrice());
				writeC(cp.getReward());
			}
			else
			{
				writeD(0);
				writeD(0);
				writeC(0);
			}
			
			// Next period
			cp = _next.get(s.getCropId());
			if (cp != null)
			{
				writeD(cp.getStartAmount());
				writeD(cp.getPrice());
				writeC(cp.getReward());
			}
			else
			{
				writeD(0);
				writeD(0);
				writeC(0);
			}
		}
	}
}