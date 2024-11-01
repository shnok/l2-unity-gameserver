package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExClosePartyRoom extends L2GameServerPacket
{
	public static final ExClosePartyRoom STATIC_PACKET = new ExClosePartyRoom();
	
	private ExClosePartyRoom()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x0f);
	}
}