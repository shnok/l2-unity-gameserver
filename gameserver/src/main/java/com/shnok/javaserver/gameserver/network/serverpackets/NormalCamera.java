package com.shnok.javaserver.gameserver.network.serverpackets;

public class NormalCamera extends L2GameServerPacket
{
	public static final NormalCamera STATIC_PACKET = new NormalCamera();
	
	private NormalCamera()
	{
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xc8);
	}
}