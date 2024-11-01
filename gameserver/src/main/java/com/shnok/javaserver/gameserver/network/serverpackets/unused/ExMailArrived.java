package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExMailArrived extends L2GameServerPacket
{
	public static final ExMailArrived STATIC_PACKET = new ExMailArrived();
	
	private ExMailArrived()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x2d);
	}
}