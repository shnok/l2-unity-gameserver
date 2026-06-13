package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExManagePartyRoomMember;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExPartyRoomMember;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PartyMatchDetail;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestJoinPartyRoom extends L2GameClientPacket
{
	private int _roomId;
	private int _bbs;
	private int _levelMode;
	
	@Override
	protected void readImpl()
	{
		_roomId = readD();
		_bbs = readD();
		_levelMode = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final PartyMatchRoom room;
		if (_roomId > 0)
			room = PartyMatchRoomManager.getInstance().getRoom(_roomId);
		else
			room = PartyMatchRoomManager.getInstance().getFirstAvailableRoom(player, _bbs, _levelMode);
		
		// Check Player entrance possibility.
		if (room == null || !room.checkEntrance(player))
		{
			player.sendPacket(SystemMessageId.CANT_ENTER_PARTY_ROOM);
			return;
		}
		
		// Remove Player from waiting list.
		if (PartyMatchRoomManager.getInstance().removeWaitingPlayer(player))
		{
			player.sendPacket(new PartyMatchDetail(room));
			player.sendPacket(new ExPartyRoomMember(room, 0));
			
			for (Player member : room.getMembers())
			{
				member.sendPacket(new ExManagePartyRoomMember(player, room, 0));
				member.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ENTERED_PARTY_ROOM).addCharName(player));
			}
			room.addMember(player, _roomId);
		}
	}
}