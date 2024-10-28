package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class TutorialEnableClientEvent extends L2GameServerPacket
{
	private final int _eventId;
	
	public TutorialEnableClientEvent(int event)
	{
		_eventId = event;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xa2);
		writeD(_eventId);
	}
}