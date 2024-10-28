package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PartySmallWindowDeleteAll extends L2GameServerPacket
{
	public static final PartySmallWindowDeleteAll STATIC_PACKET = new PartySmallWindowDeleteAll();
	
	private PartySmallWindowDeleteAll()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x50);
	}
}