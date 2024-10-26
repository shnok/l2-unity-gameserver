package com.shnok.javaserver.gameserver.network.serverpackets;

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