package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.manager.PartyMatchRoomManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.PartyMatchRoom;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExManagePartyRoomMember;
import net.sf.l2j.gameserver.network.serverpackets.ExPartyRoomMember;
import net.sf.l2j.gameserver.network.serverpackets.PartyMatchDetail;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

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