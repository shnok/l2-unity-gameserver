package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExRequestHackShield extends L2GameServerPacket
{
	public static final ExRequestHackShield STATIC_PACKET = new ExRequestHackShield();
	
	private ExRequestHackShield()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x48);
	}
}