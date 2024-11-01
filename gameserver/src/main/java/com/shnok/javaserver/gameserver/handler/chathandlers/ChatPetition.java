package com.shnok.javaserver.gameserver.handler.chathandlers;

import com.shnok.javaserver.gameserver.data.manager.PetitionManager;
import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.handler.IChatHandler;
import com.shnok.javaserver.gameserver.model.Petition;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;

public class ChatPetition implements IChatHandler
{
	private static final SayType[] COMMAND_IDS =
	{
		SayType.PETITION_PLAYER,
		SayType.PETITION_GM
	};
	
	@Override
	public void handleChat(SayType type, Player player, String target, String text)
	{
		final Petition petition = PetitionManager.getInstance().getPetitionInProcess(player);
		if (petition == null)
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_IN_PETITION_CHAT);
			return;
		}
		
		petition.sendMessage(player, text);
	}
	
	@Override
	public SayType[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}