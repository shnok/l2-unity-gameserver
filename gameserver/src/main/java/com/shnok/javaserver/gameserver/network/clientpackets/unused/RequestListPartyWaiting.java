package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExPartyRoomMember;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PartyMatchDetail;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PartyMatchList;

public final class RequestListPartyWaiting extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private int _auto;
	private int _bbs;
	private int _lvl;
	
	@Override
	protected void readImpl()
	{
		_auto = readD();
		_bbs = readD();
		_lvl = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (player.isInPartyMatchRoom())
		{
			final PartyMatchRoom room = PartyMatchRoomManager.getInstance().getRoom(player.getPartyRoom());
			if (room == null)
				return;
			
			player.sendPacket(new PartyMatchDetail(room));
			player.sendPacket(new ExPartyRoomMember(room, 2));
			player.broadcastUserInfo();
		}
		else
		{
			if (player.getParty() != null && !player.getParty().isLeader(player))
			{
				player.sendPacket(SystemMessageId.CANT_VIEW_PARTY_ROOMS);
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			// Add to waiting list.
			PartyMatchRoomManager.getInstance().addWaitingPlayer(player);
			
			// Send Room list.
			player.sendPacket(new PartyMatchList(player, _bbs, _lvl));
		}
	}
}