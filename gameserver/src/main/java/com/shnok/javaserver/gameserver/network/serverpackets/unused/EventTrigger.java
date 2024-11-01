package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class EventTrigger extends L2GameServerPacket
{
	private int _trapId;
	private boolean _active;
	
	public EventTrigger(int trapId, boolean active)
	{
		_trapId = trapId;
		_active = active;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xCF);
		writeD(_trapId);
		writeC(_active ? 1 : 0);
	}
}