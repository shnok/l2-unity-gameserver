package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.model.manor.Seed;
import com.shnok.javaserver.gameserver.model.manor.SeedProduction;

public class ExShowSeedInfo extends L2GameServerPacket
{
	private final List<SeedProduction> _seeds;
	private final int _manorId;
	private final boolean _hideButtons;
	
	public ExShowSeedInfo(int manorId, boolean nextPeriod, boolean hideButtons)
	{
		_manorId = manorId;
		_hideButtons = hideButtons;
		
		final CastleManorManager manor = CastleManorManager.getInstance();
		_seeds = (nextPeriod && !manor.isManorApproved()) ? null : manor.getSeedProduction(manorId, nextPeriod);
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1C);
		writeC(_hideButtons ? 0x01 : 0x00);
		writeD(_manorId);
		writeD(0);
		
		if (_seeds == null)
		{
			writeD(0);
			return;
		}
		
		writeD(_seeds.size());
		for (SeedProduction seed : _seeds)
		{
			writeD(seed.getId());
			writeD(seed.getAmount());
			writeD(seed.getStartAmount());
			writeD(seed.getPrice());
			
			final Seed s = CastleManorManager.getInstance().getSeed(seed.getId());
			if (s == null)
			{
				writeD(0);
				writeC(0x01);
				writeD(0);
				writeC(0x01);
				writeD(0);
			}
			else
			{
				writeD(s.getLevel());
				writeC(0x01);
				writeD(s.getReward1());
				writeC(0x01);
				writeD(s.getReward2());
			}
		}
	}
}