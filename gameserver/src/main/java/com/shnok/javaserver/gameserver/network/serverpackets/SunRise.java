package com.shnok.javaserver.gameserver.network.serverpackets;

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
