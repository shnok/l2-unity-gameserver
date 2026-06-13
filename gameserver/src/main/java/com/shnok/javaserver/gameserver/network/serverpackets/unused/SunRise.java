package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class SunRise extends L2GameServerPacket
{
	public static final SunRise STATIC_PACKET = new SunRise();
	
	private SunRise()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x1c);
	}
}
