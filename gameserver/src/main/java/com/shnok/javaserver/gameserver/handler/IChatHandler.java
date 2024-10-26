package com.shnok.javaserver.gameserver.handler;

import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * Interface used by chat handlers.
 */
public interface IChatHandler
{
	/**
	 * Handle a specific type of chat message.
	 * @param type : The {@link SayType} associated to the message.
	 * @param player : The {@link Player} which send the message.
	 * @param target : The {@link String} target to send the message.
	 * @param text : The {@link String} used as message.
	 */
	public void handleChat(SayType type, Player player, String target, String text);
	
	/**
	 * @return The array of {@link SayType}s registered to this handler.
	 */
	public SayType[] getChatTypeList();
}
