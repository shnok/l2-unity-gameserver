package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.PartyMatchList;

public final class RequestOustFromPartyRoom extends L2GameClientPacket
{
	private int _targetId;
	
	@Override
	protected void readImpl()
	{
		_targetId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player target = World.getInstance().getPlayer(_targetId);
		if (target == null)
			return;
		
		final PartyMatchRoom room = PartyMatchRoomManager.getInstance().getRoom(target.getPartyRoom());
		if (room == null)
			return;
		
		// Abort if current Player isn't the leader of the target's PartyMatchRoom.
		if (!room.isLeader(player))
			return;
		
		// You can't dismiss a Party member.
		if (player.isInParty() && target.isInParty() && player.getParty().getLeaderObjectId() == target.getParty().getLeaderObjectId())
			player.sendPacket(SystemMessageId.CANNOT_DISMISS_PARTY_MEMBER);
		else
		{
			// Remove PartyMatchRoom member.
			room.removeMember(target);
			
			// Add the Player back on waiting list.
			PartyMatchRoomManager.getInstance().addWaitingPlayer(target);
			
			// Send Room list.
			target.sendPacket(new PartyMatchList(target, 1, 1));
			
			target.sendPacket(SystemMessageId.OUSTED_FROM_PARTY_ROOM);
		}
	}
}