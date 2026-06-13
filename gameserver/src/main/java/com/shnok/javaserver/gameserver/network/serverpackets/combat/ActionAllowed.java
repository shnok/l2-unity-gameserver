package com.shnok.javaserver.gameserver.network.serverpackets.combat;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class ActionAllowed extends L2GameServerPacket
{
	public static final ActionAllowed STATIC_PACKET = new ActionAllowed();

	private ActionAllowed()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x08);
	}
}