package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PetitionVote extends L2GameServerPacket
{
	public static final PetitionVote STATIC_PACKET = new PetitionVote();
	
	private PetitionVote()
	{
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xf6);
	}
}