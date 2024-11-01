package com.shnok.javaserver.gameserver.handler.chathandlers;

import com.shnok.javaserver.gameserver.data.manager.PartyMatchRoomManager;
import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.handler.IChatHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.PartyMatchRoom;
import com.shnok.javaserver.gameserver.network.serverpackets.CreatureSay;

public class ChatPartyMatchRoom implements IChatHandler
{
	private static final SayType[] COMMAND_IDS =
	{
		SayType.PARTYMATCH_ROOM
	};
	
	@Override
	public void handleChat(SayType type, Player player, String target, String text)
	{
		if (!player.isInPartyMatchRoom())
			return;
		
		final PartyMatchRoom room = PartyMatchRoomManager.getInstance().getRoom(player.getPartyRoom());
		if (room == null)
			return;
		
		room.broadcastPacket(new CreatureSay(player, type, text));
	}
	
	@Override
	public SayType[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}