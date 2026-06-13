package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.model.manor.Seed;
import com.shnok.javaserver.gameserver.model.manor.SeedProduction;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExShowSeedSetting extends L2GameServerPacket
{
	private final int _manorId;
	private final Set<Seed> _seeds;
	private final Map<Integer, SeedProduction> _current = new HashMap<>();
	private final Map<Integer, SeedProduction> _next = new HashMap<>();
	
	public ExShowSeedSetting(int manorId)
	{
		final CastleManorManager manor = CastleManorManager.getInstance();
		
		_manorId = manorId;
		_seeds = manor.getSeedsForCastle(_manorId);
		
		for (Seed s : _seeds)
		{
			// Current period
			SeedProduction sp = manor.getSeedProduct(manorId, s.getSeedId(), false);
			if (sp != null)
				_current.put(s.getSeedId(), sp);
			
			// Next period
			sp = manor.getSeedProduct(manorId, s.getSeedId(), true);
			if (sp != null)
				_next.put(s.getSeedId(), sp);
		}
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1F);
		
		writeD(_manorId);
		writeD(_seeds.size());
		
		for (Seed s : _seeds)
		{
			writeD(s.getSeedId());
			writeD(s.getLevel());
			writeC(1);
			writeD(s.getReward1());
			writeC(1);
			writeD(s.getReward2());
			
			writeD(s.getSeedsLimit());
			writeD(s.getSeedReferencePrice());
			writeD(s.getSeedMinPrice());
			writeD(s.getSeedMaxPrice());
			
			// Current period
			SeedProduction sp = _current.get(s.getSeedId());
			if (sp != null)
			{
				writeD(sp.getStartAmount());
				writeD(sp.getPrice());
			}
			else
			{
				writeD(0);
				writeD(0);
			}
			
			// Next period
			sp = _next.get(s.getSeedId());
			if (sp != null)
			{
				writeD(sp.getStartAmount());
				writeD(sp.getPrice());
			}
			else
			{
				writeD(0);
				writeD(0);
			}
		}
	}
}