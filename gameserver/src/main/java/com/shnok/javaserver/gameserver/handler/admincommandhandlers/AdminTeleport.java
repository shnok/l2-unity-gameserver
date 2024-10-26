package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import com.shnok.javaserver.gameserver.enums.RestartType;
import com.shnok.javaserver.gameserver.enums.TeleportMode;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;

public class AdminTeleport implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_instant_move", // alt+G menu
		"admin_recall", // alt+G menu
		"admin_sendhome", // alt+G menu
		"admin_tele",
		"admin_teleport",
		"admin_teleportto" // alt+G menu
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		if (command.equals("admin_tele"))
		{
			sendFile(player, "teleports.htm");
			return;
		}
		
		final StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		if (command.startsWith("admin_instant_move"))
		{
			if (!st.hasMoreTokens())
				player.setTeleportMode(TeleportMode.ONE_TIME);
			else
			{
				try
				{
					final int mode = Integer.parseInt(st.nextToken());
					if (mode < 0 || mode > 2)
					{
						player.sendMessage("Usage: //instant_move [0|1|2]");
						return;
					}
					
					player.setTeleportMode(TeleportMode.VALUES[mode]);
				}
				catch (Exception e)
				{
					player.sendMessage("Usage: //instant_move [0|1|2]");
				}
			}
		}
		else if (command.startsWith("admin_recall"))
		{
			if (!st.hasMoreTokens())
			{
				player.sendPacket(SystemMessageId.INVALID_TARGET);
				return;
			}
			
			final String param = st.nextToken();
			switch (param)
			{
				case "clan":
					if (!st.hasMoreTokens())
					{
						player.sendPacket(SystemMessageId.INVALID_TARGET);
						return;
					}
					
					Player worldPlayer = World.getInstance().getPlayer(st.nextToken());
					if (worldPlayer == null)
					{
						player.sendPacket(SystemMessageId.INVALID_TARGET);
						return;
					}
					
					final Clan clan = worldPlayer.getClan();
					if (clan == null)
						worldPlayer.teleportTo(player.getPosition(), 0);
					else
					{
						for (Player clanMember : clan.getOnlineMembers())
							clanMember.teleportTo(player.getPosition(), 0);
					}
					
					// Clean target to avoid to get "ghost" target upon worldPlayer.
					player.setTarget(null);
					break;
				
				case "party":
					if (!st.hasMoreTokens())
					{
						player.sendPacket(SystemMessageId.INVALID_TARGET);
						return;
					}
					
					worldPlayer = World.getInstance().getPlayer(st.nextToken());
					if (worldPlayer == null)
					{
						player.sendPacket(SystemMessageId.INVALID_TARGET);
						return;
					}
					
					final Party party = worldPlayer.getParty();
					if (party == null)
						worldPlayer.teleportTo(player.getPosition(), 0);
					else
					{
						for (Player partyMember : party.getMembers())
							partyMember.teleportTo(player.getPosition(), 0);
					}
					
					// Clean target to avoid to get "ghost" target upon worldPlayer.
					player.setTarget(null);
					break;
				
				default:
					worldPlayer = World.getInstance().getPlayer(param);
					if (worldPlayer == null)
					{
						player.sendPacket(SystemMessageId.INVALID_TARGET);
						return;
					}
					
					worldPlayer.teleportTo(player.getPosition(), 0);
					
					// Clean target to avoid to get "ghost" target upon worldPlayer.
					player.setTarget(null);
					break;
			}
		}
		else if (command.startsWith("admin_sendhome"))
		{
			Player targetPlayer;
			
			if (st.hasMoreTokens())
			{
				targetPlayer = World.getInstance().getPlayer(st.nextToken());
				if (targetPlayer == null)
				{
					player.sendPacket(SystemMessageId.INVALID_TARGET);
					return;
				}
			}
			else
				targetPlayer = getTargetPlayer(player, true);
			
			targetPlayer.teleportTo(RestartType.TOWN);
			targetPlayer.setIsIn7sDungeon(false);
		}
		else if (command.startsWith("admin_teleportto"))
		{
			if (!st.hasMoreTokens())
			{
				player.sendPacket(SystemMessageId.INVALID_TARGET);
				return;
			}
			
			final Player worldPlayer = World.getInstance().getPlayer(st.nextToken());
			if (worldPlayer == null)
			{
				player.sendPacket(SystemMessageId.INVALID_TARGET);
				return;
			}
			
			player.teleportTo(worldPlayer.getPosition(), 0);
		}
		else if (command.startsWith("admin_teleport"))
		{
			try
			{
				final int x = Integer.parseInt(st.nextToken());
				final int y = Integer.parseInt(st.nextToken());
				final int z = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : GeoEngine.getInstance().getHeight(x, y, player.getZ());
				
				player.teleportTo(x, y, z, 0);
			}
			catch (Exception e)
			{
				sendFile(player, "teleports.htm");
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}