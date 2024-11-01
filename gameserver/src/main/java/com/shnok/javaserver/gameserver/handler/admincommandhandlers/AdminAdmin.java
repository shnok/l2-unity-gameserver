package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.awt.Color;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.data.Pagination;
import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.cache.HtmCache;
import com.shnok.javaserver.gameserver.data.manager.BuyListManager;
import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.xml.AdminData;
import com.shnok.javaserver.gameserver.data.xml.BoatData;
import com.shnok.javaserver.gameserver.data.xml.RestartPointData;
import com.shnok.javaserver.gameserver.data.xml.WalkerRouteData;
import com.shnok.javaserver.gameserver.enums.TeleportMode;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.boat.BoatItinerary;
import com.shnok.javaserver.gameserver.model.buylist.NpcBuyList;
import com.shnok.javaserver.gameserver.model.location.WalkerLocation;
import com.shnok.javaserver.gameserver.model.records.AdminCommand;
import com.shnok.javaserver.gameserver.model.restart.RestartArea;
import com.shnok.javaserver.gameserver.model.restart.RestartPoint;
import com.shnok.javaserver.gameserver.model.spawn.ASpawn;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.BuyList;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.CameraMode;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class AdminAdmin implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_admin",
		"admin_buy",
		"admin_camera",
		"admin_gmlist",
		"admin_gmoff",
		"admin_help",
		"admin_link",
		"admin_msg",
		"admin_show"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		if (command.startsWith("admin_admin"))
			showMainPage(player, command);
		else if (command.startsWith("admin_camera"))
		{
			if (player.getTeleportMode() != TeleportMode.CAMERA_MODE)
			{
				player.setTeleportMode(TeleportMode.CAMERA_MODE);
				player.getAppearance().setVisible(false);
				
				player.sendPacket(new CameraMode(1));
			}
			else
			{
				player.setTeleportMode(TeleportMode.NONE);
				player.getAppearance().setVisible(true);
				
				player.sendPacket(new CameraMode(0));
			}
			player.teleportTo(player.getPosition(), 0);
		}
		else if (command.startsWith("admin_gmlist"))
			player.sendMessage((AdminData.getInstance().showOrHideGm(player)) ? "Removed from GMList." : "Registered into GMList.");
		else
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			
			if (command.startsWith("admin_buy"))
			{
				if (!st.hasMoreTokens())
				{
					sendFile(player, "gmshops.htm");
					return;
				}
				
				try
				{
					final NpcBuyList list = BuyListManager.getInstance().getBuyList(Integer.parseInt(st.nextToken()));
					if (list == null)
					{
						player.sendMessage("Invalid buylist id.");
						return;
					}
					
					player.sendPacket(new BuyList(list, player.getAdena(), 0));
				}
				catch (Exception e)
				{
					player.sendMessage("Invalid buylist id.");
				}
			}
			else if (command.startsWith("admin_gmoff"))
			{
				int duration = 1;
				if (st.hasMoreTokens())
				{
					try
					{
						duration = Integer.parseInt(st.nextToken());
					}
					catch (Exception e)
					{
						player.sendMessage("Invalid timer set for //gm ; default time is used.");
					}
				}
				
				// We keep the previous level to rehabilitate it later.
				final int previousAccessLevel = player.getAccessLevel().getLevel();
				
				player.setAccessLevel(0);
				player.sendMessage("You no longer have GM status, but will be rehabilitated after " + duration + " minutes.");
				
				ThreadPool.schedule(() ->
				{
					if (!player.isOnline())
						return;
					
					player.setAccessLevel(previousAccessLevel);
					player.sendMessage("Your previous access level has been rehabilitated.");
				}, duration * 60000L);
			}
			else if (command.startsWith("admin_help"))
			{
				try
				{
					final int page = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;
					final String search = (st.hasMoreTokens()) ? st.nextToken().toLowerCase() : "";
					
					sendHelp(player, page, search);
				}
				catch (Exception e)
				{
					sendHelp(player, 1, "");
				}
			}
			else if (command.startsWith("admin_link"))
			{
				try
				{
					sendFile(player, st.nextToken());
				}
				catch (Exception e)
				{
					sendFile(player, "main_menu.htm");
				}
			}
			else if (command.startsWith("admin_msg"))
			{
				try
				{
					player.sendPacket(SystemMessage.getSystemMessage(Integer.parseInt(st.nextToken())));
				}
				catch (Exception e)
				{
					player.sendMessage("Usage: //msg sysMsgId");
				}
			}
			else if (command.startsWith("admin_show"))
			{
				final Creature targetCreature = getTargetCreature(player, true);
				
				ExServerPrimitive debug;
				
				try
				{
					switch (st.nextToken().toLowerCase())
					{
						case "boat":
							debug = player.getDebugPacket("BOAT");
							debug.reset();
							
							for (BoatItinerary itinerary : BoatData.getInstance().getItineraries())
								itinerary.visualize(debug);
							
							debug.sendTo(player);
							break;
						
						case "clear":
							if (targetCreature instanceof Player targetPlayer)
								targetPlayer.clearDebugPackets();
							break;
						
						case "door":
							debug = player.getDebugPacket("DOOR");
							debug.reset();
							
							player.forEachKnownType(Door.class, d -> d.getTemplate().visualizeDoor(debug));
							
							debug.sendTo(player);
							break;
						
						case "html":
							NpcHtmlMessage.SHOW_FILE = !NpcHtmlMessage.SHOW_FILE;
							break;
						
						case "move":
							// Toggle debug move.
							boolean move = !targetCreature.getMove().isDebugMove();
							targetCreature.getMove().setDebugMove(move);
							
							if (move)
							{
								// Send info messages.
								player.sendMessage("Debug move enabled on " + targetCreature.getName());
								if (player != targetCreature)
									targetCreature.sendMessage("Debug move was enabled.");
							}
							else
							{
								// Send info messages.
								player.sendMessage("Debug move disabled on " + targetCreature.getName());
								if (player != targetCreature)
									targetCreature.sendMessage("Debug move was disabled.");
								
								// Clear debug move packet to all GMs.
								World.getInstance().getPlayers().stream().filter(Player::isGM).forEach(p ->
								{
									final ExServerPrimitive debugMove = p.getDebugPacket("MOVE" + targetCreature.getObjectId());
									debugMove.reset();
									debugMove.sendTo(p);
								});
								
								// Clear debug move packet to self.
								if (targetCreature instanceof Player targetPlayer)
								{
									final ExServerPrimitive debugMove = targetPlayer.getDebugPacket("MOVE" + targetPlayer.getObjectId());
									debugMove.reset();
									debugMove.sendTo(targetPlayer);
								}
							}
							break;
						
						case "path":
							// Toggle debug move.
							boolean path = !targetCreature.getMove().isDebugPath();
							targetCreature.getMove().setDebugPath(path);
							
							if (path)
							{
								// Send info messages.
								player.sendMessage("Debug path enabled on " + targetCreature.getName());
								if (player != targetCreature)
									targetCreature.sendMessage("Debug path was enabled.");
							}
							else
							{
								// Send info messages.
								player.sendMessage("Debug path disabled on " + targetCreature.getName());
								if (player != targetCreature)
									targetCreature.sendMessage("Debug path was disabled.");
								
								// Clear debug move packet to all GMs.
								World.getInstance().getPlayers().stream().filter(Player::isGM).forEach(p ->
								{
									final ExServerPrimitive debugPath = p.getDebugPacket("PATH" + targetCreature.getObjectId());
									debugPath.reset();
									debugPath.sendTo(p);
								});
								
								// Clear debug move packet to self.
								if (targetCreature instanceof Player targetPlayer)
								{
									final ExServerPrimitive debugPath = targetPlayer.getDebugPacket("PATH" + targetPlayer.getObjectId());
									debugPath.reset();
									debugPath.sendTo(targetPlayer);
								}
							}
							break;
						
						case "restart":
							if (!st.hasMoreTokens())
								return;
							
							final String subCommand = st.nextToken();
							switch (subCommand)
							{
								case "area":
									debug = player.getDebugPacket("RESTART_AREA");
									debug.reset();
									
									for (RestartArea ra : RestartPointData.getInstance().getRestartAreas())
										ra.getZone().visualizeZone("", debug);
									
									debug.sendTo(player);
									break;
								
								case "point":
									debug = player.getDebugPacket("RESTART_POINT");
									debug.reset();
									
									for (RestartPoint rp : RestartPointData.getInstance().getRestartPoints())
										rp.visualizeZone(debug);
									
									debug.sendTo(player);
									break;
							}
							break;
						
						case "walker":
							if (!st.hasMoreTokens())
							{
								sendWalkerInfos(player, 1);
								return;
							}
							
							int page = 1;
							String param = st.nextToken();
							if (StringUtil.isDigit(param))
							{
								page = Integer.parseInt(param);
								
								if (!st.hasMoreTokens())
								{
									sendWalkerInfos(player, page);
									return;
								}
								
								param = st.nextToken();
							}
							
							final List<WalkerLocation> route = WalkerRouteData.getInstance().getWalkerRoute(param, param);
							if (route.isEmpty())
							{
								player.sendMessage("The npcId " + param + " isn't linked to any WalkerRoute.");
								return;
							}
							
							debug = player.getDebugPacket("WALKER");
							debug.reset();
							
							// Draw the path.
							for (int i = 0; i < route.size(); i++)
							{
								final int nextIndex = i + 1;
								debug.addLine("Segment #" + nextIndex, Color.YELLOW, true, route.get(i), (nextIndex == route.size()) ? route.get(0) : route.get(nextIndex));
							}
							
							debug.sendTo(player);
							
							sendWalkerInfos(player, page);
							break;
						
						default:
							player.sendMessage("Usage : //show <clear|door|html|move|path|restart|walker>");
							break;
					}
				}
				catch (Exception e)
				{
					player.sendMessage("Usage : //show <clear|door|html|move|path|restart|walker>");
				}
			}
		}
	}
	
	/**
	 * Send to the {@link Player} all {@link AdminCommand}s informations.
	 * @param player : The Player used as reference.
	 * @param page : The current page we are checking.
	 * @param search : The {@link String} used as search.
	 */
	private static void sendHelp(Player player, int page, String search)
	{
		// Generate data.
		final Pagination<AdminCommand> list = new Pagination<>(AdminData.getInstance().getAdminCommands().stream(), page, PAGE_LIMIT_7, ac -> ac.name().substring(6).contains(search) || ac.params().contains(search));
		list.append("<html><body>");
		
		list.generateSearch("bypass admin_help", 45);
		
		for (AdminCommand command : list)
		{
			list.append(((list.indexOf(command) % 2) == 0 ? "<table width=280 height=41 bgcolor=000000><tr>" : "<table width=280 height=41><tr>"));
			
			// Write the admin command in gold color, with "//".
			list.append("<td width=280 height=34><font color=\"LEVEL\">//", command.name().substring(6), "</font>");
			
			// If params exist, write them in blue in the same line than command.
			if (!command.params().isBlank())
				list.append(" <font color=\"33cccc\">", command.params(), "</font>");
			
			// Pass a line, then write the description.
			list.append("<br1>", command.desc(), "</td>");
			
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
		}
		list.generateSpace(42);
		list.generatePages("bypass admin_help %page% " + search);
		list.append("</body></html>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(list.getContent());
		player.sendPacket(html);
	}
	
	private static void sendWalkerInfos(Player player, int page)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/walker.htm");
		
		int row = 0;
		
		// Generate data.
		final Pagination<Entry<String, List<WalkerLocation>>> list = new Pagination<>(WalkerRouteData.getInstance().getWalkerRoutes().values().stream().flatMap(routes -> routes.entrySet().stream()), page, PAGE_LIMIT_15);
		for (Entry<String, List<WalkerLocation>> route : list)
		{
			list.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
			
			final ASpawn aSpawn = SpawnManager.getInstance().getSpawn(route.getKey());
			if (aSpawn == null)
			{
				final String teleLoc = route.getValue().get(0).toString().replace(",", "");
				
				list.append("<td width=150><a action=\"bypass admin_teleport ", teleLoc, "\">Unspawned</a></td><td width=40>-</td><td width=50 align=right>-</td><td width=40 align=right><a action=\"bypass admin_show walker ", page, " ", route.getKey(), "\">Show</a></td>");
			}
			else
			{
				final Npc npc = aSpawn.getNpc();
				if (npc == null)
				{
					final String teleLoc = route.getValue().get(0).toString().replace(",", "");
					
					list.append("<td width=150><a action=\"bypass admin_teleport ", teleLoc, "\">Unspawned</a></td><td width=40>-</td><td width=50 align=right>-</td><td width=40 align=right><a action=\"bypass admin_show walker ", page, " ", route.getKey(), "\">Show</a></td>");
				}
				else
				{
					final String teleLoc = route.getValue().get(npc.getAI().getIndex()).toString().replace(",", "");
					
					list.append("<td width=150><a action=\"bypass admin_teleport ", teleLoc, "\">", StringUtil.trimAndDress(npc.getName(), 25), "</a></td><td width=40>", npc.getAI().getIndex(), " / ", route.getValue().size(), "</td><td width=50 align=right>", ((npc.isReversePath()) ? "Reverse" : "Regular"), "</td><td width=40 align=right><a action=\"bypass admin_show walker ", page, " ", route.getKey(), "\">Show</a></td>");
				}
			}
			
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
			
			row++;
		}
		
		list.generateSpace(20);
		list.generatePages("bypass admin_show walker %page%");
		
		html.replace("%content%", list.getContent());
		player.sendPacket(html);
	}
	
	private void showMainPage(Player player, String command)
	{
		String filename = "main";
		
		final StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		if (st.hasMoreTokens())
		{
			final String param = st.nextToken();
			if (StringUtil.isDigit(param))
			{
				final int mode = Integer.parseInt(param);
				if (mode == 2)
					filename = "game";
				else if (mode == 3)
					filename = "effects";
				else if (mode == 4)
					filename = "server";
			}
			else if (HtmCache.getInstance().isLoadable("data/html/admin/" + param + "_menu.htm"))
				filename = param;
		}
		
		sendFile(player, filename + "_menu.htm");
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}