package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.network.serverpackets.ExManagePartyRoomMember;
import com.shnok.javaserver.gameserver.network.serverpackets.JoinParty;

public final class RequestAnswerJoinParty extends L2GameClientPacket
{
	private int _response;
	
	@Override
	protected void readImpl()
	{
		_response = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player requestor = player.getActiveRequester();
		if (requestor == null)
			return;
		
		requestor.sendPacket(new JoinParty(_response));
		
		Party party = requestor.getParty();
		if (_response == 1)
		{
			if (party == null)
				party = new Party(requestor, player, requestor.getLootRule());
			else
				party.addPartyMember(player);
			
			if (requestor.isInPartyMatchRoom())
			{
				final PartyMatchRoom room = PartyMatchRoomManager.getInstance().getRoom(requestor.getPartyRoom());
				if (room != null)
				{
					if (player.isInPartyMatchRoom())
					{
						if (requestor.getPartyRoom() == player.getPartyRoom())
							room.broadcastPacket(new ExManagePartyRoomMember(player, room, 1));
					}
					else
					{
						room.addMember(player, room.getId());
						room.broadcastPacket(new ExManagePartyRoomMember(player, room, 1));
					}
				}
			}
		}
		
		// Must be kept out of "ok" answer, can't be merged with higher content.
		if (party != null)
			party.setPendingInvitation(false);
		
		player.setActiveRequester(null);
		requestor.onTransactionResponse();
	}
}