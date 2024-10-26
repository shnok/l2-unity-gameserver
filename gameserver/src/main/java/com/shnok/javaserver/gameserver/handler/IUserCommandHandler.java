package com.shnok.javaserver.gameserver.handler;

import com.shnok.javaserver.gameserver.model.actor.Player;

public interface IUserCommandHandler
{
	/**
	 * This is the worker method that is called when a {@link Player} uses a user command.
	 * @param id : The command id to launch.
	 * @param player : The Player who is requesting the command.
	 */
	public void useUserCommand(int id, Player player);
	
	/**
	 * @return all known user commands this handler can process.
	 */
	public int[] getUserCommandList();
}