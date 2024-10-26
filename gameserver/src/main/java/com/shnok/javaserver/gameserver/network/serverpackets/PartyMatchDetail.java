package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.group.PartyMatchRoom;

public class PartyMatchDetail extends L2GameServerPacket
{
	private final PartyMatchRoom _room;
	
	public PartyMatchDetail(PartyMatchRoom room)
	{
		_room = room;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x97);
		writeD(_room.getId());
		writeD(_room.getMaxMembers());
		writeD(_room.getMinLvl());
		writeD(_room.getMaxLvl());
		writeD(_room.getLootType());
		writeD(_room.getLocation());
		writeS(_room.getTitle());
	}
}