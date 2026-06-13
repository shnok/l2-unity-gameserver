package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExFishingEnd extends L2GameServerPacket
{
	private final boolean _win;
	private final int _playerId;
	
	public ExFishingEnd(boolean win, int playerId)
	{
		_win = win;
		_playerId = playerId;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x14);
		writeD(_playerId);
		writeC((_win) ? 1 : 0);
	}
}