package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.data.xml.AnnouncementData;
import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class AdminAnnouncements implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_announce",
		"admin_ann",
		"admin_say",
		"admin_gmchat"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		if (command.startsWith("admin_announce"))
		{
			try
			{
				final String[] tokens = command.split(" ", 3);
				switch (tokens[1])
				{
					case "list":
						AnnouncementData.getInstance().listAnnouncements(player);
						break;
					
					case "all", "all_auto":
						final boolean isAuto = tokens[1].equalsIgnoreCase("all_auto");
						World.getInstance().getPlayers().forEach(p -> AnnouncementData.getInstance().showAnnouncements(p, isAuto));
						
						AnnouncementData.getInstance().listAnnouncements(player);
						break;
					
					case "add":
						String[] split = tokens[2].split(" ", 2); // boolean string
						boolean crit = Boolean.parseBoolean(split[0]);
						
						if (!AnnouncementData.getInstance().addAnnouncement(split[1], crit, false, -1, -1, -1))
							player.sendMessage("Invalid //announce message content ; can't be null or empty.");
						
						AnnouncementData.getInstance().listAnnouncements(player);
						break;
					
					case "add_auto":
						split = tokens[2].split(" ", 6); // boolean boolean int int int string
						crit = Boolean.parseBoolean(split[0]);
						final boolean auto = Boolean.parseBoolean(split[1]);
						final int idelay = Integer.parseInt(split[2]);
						final int delay = Integer.parseInt(split[3]);
						final int limit = Integer.parseInt(split[4]);
						final String msg = split[5];
						
						if (!AnnouncementData.getInstance().addAnnouncement(msg, crit, auto, idelay, delay, limit))
							player.sendMessage("Invalid //announce message content ; can't be null or empty.");
						
						AnnouncementData.getInstance().listAnnouncements(player);
						break;
					
					case "del":
						AnnouncementData.getInstance().delAnnouncement(Integer.parseInt(tokens[2]));
						AnnouncementData.getInstance().listAnnouncements(player);
						break;
					
					default:
						player.sendMessage("Possible //announce parameters : <list|all|add|add_auto|del>");
						break;
				}
			}
			catch (Exception e)
			{
				sendFile(player, "announce.htm");
			}
		}
		else if (command.startsWith("admin_ann") || command.startsWith("admin_say"))
			AnnouncementData.getInstance().handleAnnounce(command, 10, command.startsWith("admin_say"));
		else if (command.startsWith("admin_gmchat"))
		{
			try
			{
				AdminData.getInstance().broadcastToGMs(new CreatureSay(player, SayType.ALLIANCE, command.substring(13)));
			}
			catch (Exception e)
			{
				player.sendMessage("Invalid //gmchat message content ; can't be null or empty.");
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}