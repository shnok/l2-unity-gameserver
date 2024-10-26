package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.commons.network.ServerType;
import net.sf.l2j.commons.util.SysUtil;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.Shutdown;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class AdminMaintenance implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_server"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		
		if (!st.hasMoreTokens())
		{
			sendHtmlForm(player);
			return;
		}
		
		try
		{
			switch (st.nextToken())
			{
				case "shutdown":
					Shutdown.getInstance().startShutdown(player, null, Integer.parseInt(st.nextToken()), false);
					break;
				
				case "restart":
					Shutdown.getInstance().startShutdown(player, null, Integer.parseInt(st.nextToken()), true);
					break;
				
				case "abort":
					Shutdown.getInstance().abort(player);
					break;
				
				case "gmonly":
					LoginServerThread.getInstance().setServerType(ServerType.GM_ONLY);
					Config.SERVER_GMONLY = true;
					
					player.sendMessage("Server is now set as GMonly.");
					break;
				
				case "all":
					LoginServerThread.getInstance().setServerType(ServerType.AUTO);
					Config.SERVER_GMONLY = false;
					
					player.sendMessage("Server isn't set as GMonly anymore.");
					break;
				
				case "max":
					final int number = Integer.parseInt(st.nextToken());
					
					LoginServerThread.getInstance().setMaxPlayer(number);
					player.sendMessage("Server maximum player amount is set to " + number + ".");
					break;
			}
		}
		catch (Exception e)
		{
			player.sendMessage("Usage: //server <shutdown|restart|abort|gmonly|all|max>.");
		}
		sendHtmlForm(player);
	}
	
	private static void sendHtmlForm(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/maintenance.htm");
		html.replace("%count%", World.getInstance().getPlayers().size());
		html.replace("%used%", SysUtil.getUsedMemory());
		html.replace("%server_name%", LoginServerThread.getInstance().getServerName());
		html.replace("%status%", LoginServerThread.getInstance().getServerType().getName());
		html.replace("%max_players%", LoginServerThread.getInstance().getMaxPlayers());
		html.replace("%time%", GameTimeTaskManager.getInstance().getGameTimeFormated());
		player.sendPacket(html);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}