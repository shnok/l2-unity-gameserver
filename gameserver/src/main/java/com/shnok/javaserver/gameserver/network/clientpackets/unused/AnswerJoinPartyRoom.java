package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExManagePartyRoomMember;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExPartyRoomMember;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PartyMatchDetail;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class AnswerJoinPartyRoom extends L2GameClientPacket
{
	private int _answer; // 1 or 0
	
	@Override
	protected void readImpl()
	{
		_answer = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player partner = player.getActiveRequester();
		if (partner == null || World.getInstance().getPlayer(partner.getObjectId()) == null)
		{
			// Partner hasn't be found, cancel the invitation
			player.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			player.setActiveRequester(null);
			return;
		}
		
		// If answer is positive, join the requester's PartyRoom.
		if (_answer == 1 && !partner.isRequestExpired())
		{
			final PartyMatchRoom room = PartyMatchRoomManager.getInstance().getRoom(partner.getPartyRoom());
			if (room == null)
				return;
			
			// Check Player entrance possibility.
			if (!room.checkEntrance(player))
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
				room.addMember(player, partner.getPartyRoom());
			}
		}
		// Else, send a message to requester.
		else
			partner.sendPacket(SystemMessageId.PARTY_MATCHING_REQUEST_NO_RESPONSE);
		
		// reset transaction timers
		player.setActiveRequester(null);
		partner.onTransactionResponse();
	}
}