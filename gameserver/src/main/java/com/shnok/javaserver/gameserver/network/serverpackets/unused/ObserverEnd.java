package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ObserverEnd extends L2GameServerPacket
{
	private final Location _location;
	
	public ObserverEnd(Location loc)
	{
		_location = loc;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe0);
		
		writeLoc(_location);
	}
}