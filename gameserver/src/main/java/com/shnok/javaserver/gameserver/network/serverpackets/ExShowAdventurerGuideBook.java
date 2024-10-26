package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExShowAdventurerGuideBook extends L2GameServerPacket
{
	public static final ExShowAdventurerGuideBook STATIC_PACKET = new ExShowAdventurerGuideBook();
	
	private ExShowAdventurerGuideBook()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x37);
	}
}