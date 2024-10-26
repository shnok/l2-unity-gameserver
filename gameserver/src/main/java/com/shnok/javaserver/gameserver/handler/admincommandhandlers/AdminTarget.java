package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;

public class AdminTarget implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_target"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		if (command.startsWith("admin_target"))
		{
			try
			{
				final Player worldPlayer = World.getInstance().getPlayer(command.substring(13));
				if (worldPlayer == null)
				{
					player.sendPacket(SystemMessageId.CONTACT_CURRENTLY_OFFLINE);
					return;
				}
				
				worldPlayer.onAction(player, false, false);
			}
			catch (IndexOutOfBoundsException e)
			{
				player.sendPacket(SystemMessageId.INCORRECT_CHARACTER_NAME_TRY_AGAIN);
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}