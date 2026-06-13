package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExRestartClient extends L2GameServerPacket
{
	public static final ExRestartClient STATIC_PACKET = new ExRestartClient();
	
	private ExRestartClient()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x47);
	}
}