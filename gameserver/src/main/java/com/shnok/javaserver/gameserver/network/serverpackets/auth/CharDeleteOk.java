package com.shnok.javaserver.gameserver.network.serverpackets.auth;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class CharDeleteOk extends L2GameServerPacket
{
	public static final CharDeleteOk STATIC_PACKET = new CharDeleteOk();
	
	private CharDeleteOk()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x23);
	}
}
