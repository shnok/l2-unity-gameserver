package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExPlayScene extends L2GameServerPacket
{
	public static final ExPlayScene STATIC_PACKET = new ExPlayScene();
	
	private ExPlayScene()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x5B);
	}
}