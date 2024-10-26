package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;

public class PartySmallWindowDelete extends L2GameServerPacket
{
	private final Player _member;
	
	public PartySmallWindowDelete(Player member)
	{
		_member = member;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x51);
		writeD(_member.getObjectId());
		writeS(_member.getName());
	}
}