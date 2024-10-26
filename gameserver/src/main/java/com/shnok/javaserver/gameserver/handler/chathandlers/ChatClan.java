package com.shnok.javaserver.gameserver.handler.chathandlers;

import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class ChatClan implements IChatHandler
{
	private static final SayType[] COMMAND_IDS =
	{
		SayType.CLAN
	};
	
	@Override
	public void handleChat(SayType type, Player player, String target, String text)
	{
		final Clan clan = player.getClan();
		if (clan == null)
			return;
		
		clan.broadcastToMembers(new CreatureSay(player, type, text));
	}
	
	@Override
	public SayType[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}