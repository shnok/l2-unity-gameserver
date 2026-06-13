package com.shnok.javaserver.gameserver.network.serverpackets.auth;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class LeaveWorld extends L2GameServerPacket
{
	public static final LeaveWorld STATIC_PACKET = new LeaveWorld();
	
	private LeaveWorld()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7e);
	}
}