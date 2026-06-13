package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeShowMemberListDeleteAll extends L2GameServerPacket
{
	public static final PledgeShowMemberListDeleteAll STATIC_PACKET = new PledgeShowMemberListDeleteAll();
	
	private PledgeShowMemberListDeleteAll()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x82);
	}
}