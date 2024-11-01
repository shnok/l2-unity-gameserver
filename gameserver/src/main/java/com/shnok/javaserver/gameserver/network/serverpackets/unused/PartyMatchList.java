package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.Collection;

import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PartyMatchList extends L2GameServerPacket
{
	private final Collection<PartyMatchRoom> _rooms;
	
	public PartyMatchList(Player player, int bbs, int levelMode)
	{
		_rooms = PartyMatchRoomManager.getInstance().getAvailableRooms(player, bbs, levelMode);
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x96);
		writeD((_rooms.isEmpty()) ? 0 : 1);
		writeD(_rooms.size());
		
		for (PartyMatchRoom room : _rooms)
		{
			writeD(room.getId());
			writeS(room.getTitle());
			writeD(room.getLocation());
			writeD(room.getMinLvl());
			writeD(room.getMaxLvl());
			writeD(room.getMembersCount());
			writeD(room.getMaxMembers());
			writeS(room.getLeader().getName());
		}
	}
}