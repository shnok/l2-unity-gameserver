package com.shnok.javaserver.gameserver.handler.chathandlers;

import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.handler.IChatHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.serverpackets.CreatureSay;

public class ChatAlliance implements IChatHandler
{
	private static final SayType[] COMMAND_IDS =
	{
		SayType.ALLIANCE
	};
	
	@Override
	public void handleChat(SayType type, Player player, String target, String text)
	{
		final Clan clan = player.getClan();
		if (clan == null || clan.getAllyId() == 0)
			return;
		
		clan.broadcastToAllyMembers(new CreatureSay(player, type, text));
	}
	
	@Override
	public SayType[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}