package com.shnok.javaserver.gameserver.network.serverpackets;

public class CharCreateOk extends L2GameServerPacket
{
	public static final CharCreateOk STATIC_PACKET = new CharCreateOk();
	
	private CharCreateOk()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x19);
		writeD(0x01);
	}
}