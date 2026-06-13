package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.LoginServerThread;
import com.shnok.javaserver.gameserver.enums.PunishmentType;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;

public class AdminPunish implements IAdminCommandHandler
{
	private static final String UPDATE_BAN = "UPDATE characters SET punish_level=?, punish_timer=? WHERE char_name=?";
	private static final String UPDATE_JAIL = "UPDATE characters SET x=-114356, y=-249645, z=-2984, punish_level=?, punish_timer=? WHERE char_name=?";
	private static final String UPDATE_UNJAIL = "UPDATE characters SET x=17836, y=170178, z=-3507, punish_level=0, punish_timer=0 WHERE char_name=?";
	private static final String UPDATE_ACCESS = "UPDATE characters SET accesslevel=? WHERE char_name=?";
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ban",
		"admin_jail",
		"admin_kick",
		"admin_unban",
		"admin_unjail"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		if (command.startsWith("admin_ban"))
		{
			try
			{
				final String param = st.nextToken();
				
				String name = null;
				int duration = -1;
				Player targetPlayer = null;
				
				// One parameter, player name
				if (st.hasMoreTokens())
				{
					name = st.nextToken();
					targetPlayer = World.getInstance().getPlayer(name);
					
					// Second parameter, duration
					if (st.hasMoreTokens())
						duration = Integer.parseInt(st.nextToken());
				}
				// If there is no name, select target
				else
					targetPlayer = getTargetPlayer(player, false);
				
				// Can't ban yourself
				if (player == targetPlayer)
				{
					player.sendPacket(SystemMessageId.CANNOT_USE_ON_YOURSELF);
					return;
				}
				
				switch (param)
				{
					case "account":
						if (targetPlayer == null)
						{
							if (StringUtil.isEmpty(name))
							{
								player.sendMessage("Usage: //ban account [name].");
								return;
							}
							
							LoginServerThread.getInstance().sendAccessLevel(name, -100);
							player.sendMessage("Ban request sent for account " + name + ".");
						}
						else
						{
							targetPlayer.getPunishment().setType(PunishmentType.ACC, 0);
							player.sendMessage(targetPlayer.getAccountName() + " account is banned.");
						}
						break;
					
					case "chat":
						if (targetPlayer == null)
						{
							if (StringUtil.isEmpty(name))
							{
								player.sendMessage("Usage: //ban chat [name duration].");
								return;
							}
							
							banChatOfflinePlayer(player, name, duration, true);
						}
						else
						{
							if (targetPlayer.getPunishment().getType() != PunishmentType.NONE)
							{
								player.sendMessage(targetPlayer.getName() + " is already " + targetPlayer.getPunishment().getType().getDescription() + ", and can't receive another punishment.");
								return;
							}
							
							targetPlayer.getPunishment().setType(PunishmentType.CHAT, duration);
							
							player.sendMessage(targetPlayer.getName() + " is chat banned" + ((duration > 0) ? " for " + duration + " minutes." : "."));
						}
						break;
					
					case "player":
						changeCharAccessLevel(targetPlayer, name, player, -1);
						break;
				}
			}
			catch (Exception e)
			{
				player.sendMessage("Usage : //ban account|chat|player [name [time]]");
			}
		}
		else if (command.startsWith("admin_kick"))
		{
			try
			{
				final String param = st.nextToken();
				switch (param)
				{
					case "all":
						for (Player worldPlayer : World.getInstance().getPlayers())
						{
							if (worldPlayer.isGM())
								continue;
							
							worldPlayer.logout(false);
						}
						break;
					
					default:
						final Player targetPlayer = getTargetPlayer(player, param, false);
						if (targetPlayer == null)
						{
							player.sendPacket(SystemMessageId.INVALID_TARGET);
							return;
						}
						targetPlayer.logout(false);
						break;
				}
			}
			catch (Exception e)
			{
				player.sendMessage("Usage : //kick [all|name]");
			}
		}
		else if (command.startsWith("admin_jail"))
		{
			String name = null;
			int duration = -1;
			Player targetPlayer = null;
			
			if (st.hasMoreTokens())
			{
				name = st.nextToken();
				targetPlayer = World.getInstance().getPlayer(name);
				
				if (st.hasMoreTokens())
					duration = Integer.parseInt(st.nextToken());
			}
			else
				targetPlayer = getTargetPlayer(player, false);
			
			// Can't ban yourself
			if (player == targetPlayer)
			{
				player.sendPacket(SystemMessageId.CANNOT_USE_ON_YOURSELF);
				return;
			}
			
			if (targetPlayer == null)
				jailOfflinePlayer(player, name, duration);
			else
			{
				targetPlayer.getPunishment().setType(PunishmentType.JAIL, duration);
				player.sendMessage(targetPlayer.getName() + " is jailed" + ((duration > 0) ? " for " + duration + " minutes." : "."));
			}
		}
		else if (command.startsWith("admin_unban"))
		{
			try
			{
				final String param = st.nextToken();
				
				String name = null;
				Player targetPlayer = null;
				
				if (st.hasMoreTokens())
				{
					name = st.nextToken();
					targetPlayer = World.getInstance().getPlayer(name);
				}
				
				// Can't unban yourself.
				if (player == targetPlayer)
				{
					player.sendPacket(SystemMessageId.CANNOT_USE_ON_YOURSELF);
					return;
				}
				
				switch (param)
				{
					case "account":
						if (targetPlayer != null)
						{
							player.sendMessage(targetPlayer.getName() + " account isn't actually banned.");
							return;
						}
						
						LoginServerThread.getInstance().sendAccessLevel(name, 0);
						player.sendMessage("Unban request sent for account " + name + ".");
						break;
					
					case "chat":
						if (targetPlayer == null)
							banChatOfflinePlayer(player, name, 0, false);
						else
						{
							if (targetPlayer.isChatBanned())
							{
								targetPlayer.getPunishment().setType(PunishmentType.NONE, 0);
								player.sendMessage(targetPlayer.getName() + "'s chat ban has been lifted.");
							}
							else
								player.sendMessage(targetPlayer.getName() + " isn't currently chat banned.");
						}
						break;
					
					case "player":
						if (targetPlayer != null)
						{
							player.sendMessage(targetPlayer.getName() + " player isn't actually banned.");
							return;
						}
						
						changeCharAccessLevel(null, name, player, 0);
						break;
				}
			}
			catch (Exception e)
			{
				player.sendMessage("Usage : //unban account|chat|player name");
			}
		}
		else if (command.startsWith("admin_unjail"))
		{
			try
			{
				String name = null;
				Player targetPlayer;
				
				if (st.hasMoreTokens())
				{
					name = st.nextToken();
					targetPlayer = World.getInstance().getPlayer(name);
				}
				else
					targetPlayer = getTargetPlayer(player, false);
				
				if (targetPlayer == null)
					unjailOfflinePlayer(player, name);
				else
				{
					targetPlayer.getPunishment().setType(PunishmentType.NONE, 0);
					player.sendMessage(targetPlayer.getName() + " has been unjailed.");
				}
			}
			catch (Exception e)
			{
				player.sendMessage("Usage : //unjail name");
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private static void banChatOfflinePlayer(Player player, String playerName, int delay, boolean ban)
	{
		PunishmentType punishement;
		long value = 0;
		
		if (ban)
		{
			punishement = PunishmentType.CHAT;
			value = ((delay > 0) ? delay * 60000L : 60000);
		}
		else
			punishement = PunishmentType.NONE;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_BAN))
		{
			ps.setInt(1, punishement.ordinal());
			ps.setLong(2, value);
			ps.setString(3, playerName);
			ps.execute();
			
			final int count = ps.getUpdateCount();
			if (count == 0)
				player.sendMessage("This Player isn't found.");
			else if (ban)
				player.sendMessage(playerName + " is chat banned" + ((delay > 0) ? " for " + delay + " minutes." : "."));
			else
				player.sendMessage(playerName + "'s chat ban has been lifted.");
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't chatban offline Player.", e);
		}
	}
	
	private static void jailOfflinePlayer(Player player, String playerName, int delay)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_JAIL))
		{
			ps.setInt(1, PunishmentType.JAIL.ordinal());
			ps.setLong(2, ((delay > 0) ? delay * 60000L : 0));
			ps.setString(3, playerName);
			ps.execute();
			
			final int count = ps.getUpdateCount();
			if (count == 0)
				player.sendMessage("This Player isn't found.");
			else
				player.sendMessage(playerName + " has been jailed" + ((delay > 0) ? " for " + delay + " minutes." : "."));
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't jail offline Player.", e);
		}
	}
	
	private static void unjailOfflinePlayer(Player player, String playerName)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_UNJAIL))
		{
			ps.setString(1, playerName);
			ps.execute();
			
			final int count = ps.getUpdateCount();
			if (count == 0)
				player.sendMessage("This Player isn't found.");
			else
				player.sendMessage(playerName + " has been unjailed.");
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't unjail offline Player.", e);
		}
	}
	
	private static void changeCharAccessLevel(Player targetPlayer, String name, Player player, int lvl)
	{
		if (targetPlayer != null)
		{
			targetPlayer.setAccessLevel(lvl);
			targetPlayer.logout(false);
			
			player.sendMessage(targetPlayer.getName() + " has been banned.");
		}
		else
		{
			if (StringUtil.isEmpty(name))
			{
				player.sendMessage((lvl == 0) ? "Usage: //unban player [name]." : "Usage: //ban player [name].");
				return;
			}
			
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_ACCESS))
			{
				ps.setInt(1, lvl);
				ps.setString(2, name);
				ps.execute();
				
				final int count = ps.getUpdateCount();
				if (count == 0)
				{
					player.sendMessage("This Player isn't found, or the AccessLevel was unaltered.");
					return;
				}
				
				player.sendMessage(name + " now has an access level of " + lvl + ".");
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't change Player's AccessLevel.", e);
			}
		}
	}
}