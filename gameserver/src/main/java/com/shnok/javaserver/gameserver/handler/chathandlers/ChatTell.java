package com.shnok.javaserver.gameserver.handler.chathandlers;

import com.shnok.javaserver.gameserver.data.manager.RelationManager;
import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.handler.IChatHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.CreatureSay;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class ChatTell implements IChatHandler
{
	private static final SayType[] COMMAND_IDS =
	{
		SayType.TELL
	};
	
	@Override
	public void handleChat(SayType type, Player player, String target, String text)
	{
		if (target == null)
			return;
		
		final Player targetPlayer = World.getInstance().getPlayer(target);
		if (targetPlayer == null || targetPlayer.getClient().isDetached())
		{
			player.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			return;
		}
		
		if (targetPlayer.isInJail() || targetPlayer.isChatBanned())
		{
			player.sendPacket(SystemMessageId.TARGET_IS_CHAT_BANNED);
			return;
		}
		
		if (!player.isGM())
		{
			if (targetPlayer.isBlockingAll())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_BLOCKED_EVERYTHING).addCharName(targetPlayer));
				return;
			}
			
			if (RelationManager.getInstance().isInBlockList(targetPlayer, player))
			{
				player.sendPacket(SystemMessageId.THE_PERSON_IS_IN_MESSAGE_REFUSAL_MODE);
				return;
			}
		}
		
		targetPlayer.sendPacket(new CreatureSay(player, type, text));
		player.sendPacket(new CreatureSay(player.getObjectId(), type, "->" + targetPlayer.getName(), text));
	}
	
	@Override
	public SayType[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}