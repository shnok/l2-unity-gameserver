package com.shnok.javaserver.gameserver.handler.chathandlers;

import net.sf.l2j.gameserver.data.manager.PartyMatchRoomManager;
import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.PartyMatchRoom;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

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