package com.shnok.javaserver.gameserver.handler.chathandlers;

import net.sf.l2j.gameserver.data.manager.PetitionManager;
import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.model.Petition;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;

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