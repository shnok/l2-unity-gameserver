package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class ExRegenMax extends L2GameServerPacket
{
	private final int _count;
	private final int _time;
	private final double _hpRegen;
	
	public ExRegenMax(int count, int time, double hpRegen)
	{
		_count = count;
		_time = time;
		_hpRegen = hpRegen * 0.66;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x01);
		writeD(1);
		writeD(_count);
		writeD(_time);
		writeF(_hpRegen);
	}
}