package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class TradePressOwnOk extends L2GameServerPacket
{
	public static final TradePressOwnOk STATIC_PACKET = new TradePressOwnOk();
	
	private TradePressOwnOk()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x75);
	}
}