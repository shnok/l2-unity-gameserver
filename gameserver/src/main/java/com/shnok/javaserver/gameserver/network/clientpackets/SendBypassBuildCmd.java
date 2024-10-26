package com.shnok.javaserver.gameserver.network.clientpackets;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

public final class SendBypassBuildCmd extends L2GameClientPacket
{
	private static final Logger GMAUDIT_LOG = Logger.getLogger("gmaudit");
	
	private String _command;
	
	@Override
	protected void readImpl()
	{
		_command = readS();
		if (_command != null)
			_command = _command.trim();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		String command = "admin_" + _command.split(" ")[0];
		
		final IAdminCommandHandler ach = AdminCommandHandler.getInstance().getHandler(command);
		if (ach == null)
		{
			if (player.isGM())
				player.sendMessage("The command " + command.substring(6) + " doesn't exist.");
			
			return;
		}
		
		if (!AdminData.getInstance().hasAccess(command, player.getAccessLevel()))
		{
			player.sendMessage("You don't have the access right to use this command.");
			LOGGER.warn("{} tried to use admin command '{}', but has no access to use it.", player.getName(), command);
			return;
		}
		
		if (Config.GMAUDIT)
			GMAUDIT_LOG.info(player.getName() + " [" + player.getObjectId() + "] used '" + _command + "' command on: " + ((player.getTarget() != null) ? player.getTarget().getName() : "none"));
		
		ach.useAdminCommand("admin_" + _command, player);
	}
}