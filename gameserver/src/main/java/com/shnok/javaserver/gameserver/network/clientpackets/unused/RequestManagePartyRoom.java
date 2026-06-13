package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExPartyRoomMember;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PartyMatchDetail;

public class RequestManagePartyRoom extends L2GameClientPacket
{
	private int _roomId;
	private int _maxMembers;
	private int _minLvl;
	private int _maxLvl;
	private int _lootType;
	private String _roomTitle;
	
	@Override
	protected void readImpl()
	{
		_roomId = readD();
		_maxMembers = readD();
		_minLvl = readD();
		_maxLvl = readD();
		_lootType = readD();
		_roomTitle = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (_roomId > 0)
		{
			// Verify if the PartyMatchRoom exists and if the Player requesting edits is the actual leader.
			final PartyMatchRoom room = PartyMatchRoomManager.getInstance().getRoom(_roomId);
			if (room != null && room.getLeader() == player)
			{
				room.setMaxMembers(_maxMembers);
				room.setMinLvl(_minLvl);
				room.setMaxLvl(_maxLvl);
				room.setLootType(_lootType);
				room.setTitle(_roomTitle);
				room.refreshLocation();
				
				for (Player member : room.getMembers())
				{
					member.sendPacket(new PartyMatchDetail(room));
					member.sendPacket(new ExPartyRoomMember(room, 2));
					member.sendPacket(SystemMessageId.PARTY_ROOM_REVISED);
				}
			}
		}
		// Remove Player from waiting list.
		else if (PartyMatchRoomManager.getInstance().removeWaitingPlayer(player))
		{
			// Generate a new PartyMatchRoom.
			final int newId = PartyMatchRoomManager.getInstance().getNewRoomId();
			final PartyMatchRoom room = new PartyMatchRoom(newId, _roomTitle, _lootType, _minLvl, _maxLvl, _maxMembers, player);
			
			// Add it to the manager.
			PartyMatchRoomManager.getInstance().addRoom(newId, room);
			
			// Compute Party members, if any.
			final Party party = player.getParty();
			if (party != null)
			{
				for (Player member : party.getMembers())
				{
					if (member == player)
						continue;
					
					room.addMember(member, newId);
				}
			}
			
			// Compute leader at the end.
			player.sendPacket(new PartyMatchDetail(room));
			player.sendPacket(new ExPartyRoomMember(room, 1));
			player.sendPacket(SystemMessageId.PARTY_ROOM_CREATED);
			player.setPartyRoom(newId);
			player.broadcastUserInfo();
		}
	}
}