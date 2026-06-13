package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExOrcMove extends L2GameServerPacket
{
	public static final ExOrcMove STATIC_PACKET = new ExOrcMove();
	
	private ExOrcMove()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x44);
	}
}