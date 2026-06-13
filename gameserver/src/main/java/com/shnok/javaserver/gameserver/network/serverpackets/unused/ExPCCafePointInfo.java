package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExPCCafePointInfo extends L2GameServerPacket
{
	private final int _score;
	private final int _modify;
	private final int _remainingTime;
	private final int _pointType;
	private final int _periodType;
	
	public ExPCCafePointInfo(int score, int modify, boolean addPoint, boolean pointType, int remainingTime)
	{
		_score = score;
		_modify = (addPoint) ? modify : modify * -1;
		_remainingTime = remainingTime;
		_pointType = (addPoint) ? (pointType ? 0 : 1) : 2;
		_periodType = 1;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x31);
		writeD(_score);
		writeD(_modify);
		writeC(_periodType);
		writeD(_remainingTime);
		writeC(_pointType);
	}
}