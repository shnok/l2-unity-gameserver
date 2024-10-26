package com.shnok.javaserver.gameserver.network.serverpackets;

/**
 * format (c) dd
 */
public class SetSummonRemainTime extends L2GameServerPacket
{
	private final int _maxTime;
	private final int _remainingTime;
	
	public SetSummonRemainTime(int maxTime, int remainingTime)
	{
		_remainingTime = remainingTime;
		_maxTime = maxTime;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xd1);
		writeD(_maxTime);
		writeD(_remainingTime);
	}
}