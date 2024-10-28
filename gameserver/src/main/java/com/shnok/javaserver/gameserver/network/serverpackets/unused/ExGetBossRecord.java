package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

import java.util.Map;

public class ExGetBossRecord extends L2GameServerPacket
{
	private final Map<Integer, Integer> _bossRecordInfo;
	private final int _ranking;
	private final int _totalPoints;
	
	public ExGetBossRecord(int ranking, int totalScore, Map<Integer, Integer> list)
	{
		_ranking = ranking;
		_totalPoints = totalScore;
		_bossRecordInfo = list;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x33);
		writeD(_ranking);
		writeD(_totalPoints);
		if (_bossRecordInfo == null)
		{
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
		}
		else
		{
			writeD(_bossRecordInfo.size()); // list size
			for (Map.Entry<Integer, Integer> bossEntry : _bossRecordInfo.entrySet())
			{
				writeD(bossEntry.getKey());
				writeD(bossEntry.getValue());
				writeD(0x00); // Total points
			}
		}
	}
}