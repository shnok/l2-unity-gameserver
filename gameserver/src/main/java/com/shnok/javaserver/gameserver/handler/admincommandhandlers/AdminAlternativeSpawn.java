package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.awt.Color;
import java.util.List;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.data.Pagination;
import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.spawn.Territory;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class AdminAlternativeSpawn implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_maker",
		"admin_terr",
		"admin_spawn_event",
		"admin_despawn_event"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken();
		
		int page = 1;
		
		// NpcMaker info
		if (actualCommand.equals("admin_maker"))
		{
			if (!st.hasMoreTokens())
			{
				showNpcMakersInfo(player, page);
				return;
			}
			
			final int paramCount = st.countTokens();
			if (paramCount == 1)
			{
				actualCommand = st.nextToken();
				if (StringUtil.isDigit(actualCommand))
				{
					page = Integer.parseInt(actualCommand);
					showNpcMakersInfo(player, page);
				}
				else
				{
					final NpcMaker maker = SpawnManager.getInstance().getNpcMaker(actualCommand);
					if (maker == null)
					{
						player.sendMessage("Maker \"" + actualCommand + "\" doesn't exist.");
						return;
					}
					
					showNpcMakerInfo(player, maker);
				}
			}
			else if (paramCount >= 2)
			{
				actualCommand = st.nextToken();
				
				final String name = st.nextToken();
				final NpcMaker maker = SpawnManager.getInstance().getNpcMaker(name);
				if (maker == null)
				{
					player.sendMessage("Maker \"" + name + "\" doesn't exist.");
					return;
				}
				
				switch (actualCommand)
				{
					case "delete":
						maker.deleteAll();
						showNpcMakerInfo(player, maker);
						break;
					
					case "respawn":
						maker.respawnAll();
						showNpcMakerInfo(player, maker);
						break;
					
					case "scripts":
						showMakerScripts(player, maker);
						break;
					
					case "spawn":
						// more tokens present -> disable respawn (//maker_spawn NAME x)
						maker.getMaker().onMakerScriptEvent("1001", maker, 0, 0);
						showNpcMakerInfo(player, maker);
						break;
					
					case "switch":
						showNpcMakerInfo(player, maker);
						break;
					
					default:
						player.sendMessage("Usage: //maker [delete|respawn|scripts|spawn|switch <name>]");
						break;
				}
			}
		}
		// Territory
		else if (actualCommand.equals("admin_terr"))
		{
			final int paramCount = st.countTokens();
			if (paramCount == 0)
				showNpcMakersInfo(player, page);
			else if (paramCount == 1)
			{
				actualCommand = st.nextToken();
				switch (actualCommand)
				{
					case "visual_clear":
						final ExServerPrimitive debug = player.getDebugPacket("TERR");
						debug.reset();
						debug.sendTo(player);
						showNpcMakersInfo(player, page);
						break;
				}
			}
			else if (paramCount >= 2)
			{
				actualCommand = st.nextToken();
				
				final String name = st.nextToken();
				final Territory territory = SpawnManager.getInstance().getTerritory(name);
				if (territory == null)
				{
					player.sendMessage("Territory \"" + name + "\" doesn't exist.");
					return;
				}
				
				switch (actualCommand)
				{
					case "goto":
						player.teleToLocation(territory.getRandomLocation());
						showNpcMakersInfo(player, page);
						break;
					
					case "test":
						ExServerPrimitive debug = player.getDebugPacket("TERR");
						debug.reset();
						
						for (int i = 0; i < 10000; i++)
							debug.addPoint(Color.GREEN, territory.getRandomLocation());
						
						debug.sendTo(player);
						
						showNpcMakersInfo(player, page);
						break;
					
					case "visual":
						debug = player.getDebugPacket("TERR");
						debug.reset();
						
						territory.visualize(null, debug, player.getZ());
						debug.sendTo(player);
						
						showNpcMakersInfo(player, page);
						break;
				}
			}
			else
				player.sendMessage("Usage: //terr goto|visual|visual_clear name");
		}
		// Event spawns
		else if (actualCommand.equals("admin_spawn_event"))
		{
			if (!st.hasMoreTokens())
			{
				player.sendMessage("Usage: //spawn_event <event_name>");
				return;
			}
			
			String event = st.nextToken();
			// more tokens present -> spawns once (//spawn_event EVENT x)
			long total = SpawnManager.getInstance().spawnEventNpcs(event, true);
			player.sendMessage("Spawned " + total + " \"" + event + "\" NPCs.");
		}
		else if (actualCommand.equals("admin_despawn_event"))
		{
			if (!st.hasMoreTokens())
			{
				player.sendMessage("Usage: //despawn_event <event_name>");
				return;
			}
			
			String event = st.nextToken();
			long total = SpawnManager.getInstance().despawnEventNpcs(event, true);
			player.sendMessage("Despawned " + total + " \"" + event + "\" NPCs.");
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private static void showNpcMakersInfo(Player player, int page)
	{
		// Load static htm.
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(0);
		adminReply.setFile("data/html/admin/spawnlist/maker_check.htm");
		
		List<NpcMaker> npcMakers = SpawnManager.getInstance().getNpcMakers(player.getPosition());
		
		// Generate data.
		final Pagination<Territory> list = new Pagination<>(npcMakers.stream().map(NpcMaker::getTerritory).distinct(), page, PAGE_LIMIT_1);
		for (Territory territory : list)
		{
			list.append("<table><tr><td width=280><font color=LEVEL>", territory.getName(), "</font></td></tr></table>");
			list.append("<table><tr><td width=170>min=", territory.getMinZ(), ", max=", territory.getMaxZ(), "</td><td width=110><a action=\"bypass -h admin_terr goto ", territory.getName(), "\">Go To</a> | <a action=\"bypass -h admin_terr test ", territory.getName(), "\">Test</a> | <a action=\"bypass -h admin_terr visual ", territory.getName(), "\">Visualize</a></td></tr></table>");
			
			int row = 0;
			
			npcMakers = npcMakers.stream().filter(nm -> nm.getTerritory().getName().equals(territory.getName())).toList();
			for (NpcMaker nm : npcMakers)
			{
				list.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
				list.append("<td width=10></td><td width=270><a action=\"bypass -h admin_maker ", nm.getName(), "\">", nm.getName(), "</a></td>");
				list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
				
				row++;
			}
		}
		
		list.generatePages("bypass admin_maker %page%");
		
		adminReply.replace("%TERRITORIESLIST%", list.getContent());
		
		player.sendPacket(adminReply);
	}
	
	private static void showNpcMakerInfo(Player player, NpcMaker maker)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(0);
		adminReply.setFile("data/html/admin/spawnlist/maker_info.htm");
		
		adminReply.replace("%TERRNAME%", maker.getTerritory().getName());
		adminReply.replace("%BANTERRNAME%", (maker.getBannedTerritory() == null) ? "" : maker.getBannedTerritory().getName());
		adminReply.replace("%MAKERNAME%", maker.getName());
		adminReply.replace("%MAKERSPAWN%", maker.getMaker().getMakerName());
		adminReply.replace("%MAKEREVENT%", (maker.getEvent() == null) ? "" : maker.getEvent());
		adminReply.replace("%MAKERSTART%", "" + maker.isOnStart());
		adminReply.replace("%MAKERRESPAWN%", maker.getMaker().getMakerName());
		adminReply.replace("%MAKERCNTA%", maker.getNpcsAlive());
		adminReply.replace("%MAKERCNTD%", maker.getNpcsDead());
		adminReply.replace("%MAKERCNTT%", maker.getMaximumNpc());
		
		final List<MultiSpawn> spawns = maker.getSpawns();
		if (spawns.isEmpty())
			adminReply.replace("%NPCSLIST%", "<br><br><center>No NPCs at this NPC maker.</center>");
		else
		{
			final StringBuilder sb = new StringBuilder("<table><tr><td width=35><font color=\"LEVEL\">ID:</font></td><td width=130><font color=\"LEVEL\">Name:</font></td><td width=35><font color=\"LEVEL\">NPCs:</font></td><td width=70><font color=\"LEVEL\">Resp / Rnd:</font></td></tr>");
			for (MultiSpawn spawn : spawns)
				StringUtil.append(sb, "<tr><td>", spawn.getTemplate().getNpcId(), "</td><td>", spawn.getTemplate().getName(), "</td><td>", spawn.getSpawned(), " / ", spawn.getTotal(), "</td><td>", StringUtil.getTimeStamp(spawn.getRespawnDelay()), " / ", StringUtil.getTimeStamp(spawn.getRespawnRandom()), "</td></tr>");
			
			sb.append("</table>");
			adminReply.replace("%NPCSLIST%", sb.toString());
		}
		
		player.sendPacket(adminReply);
	}
	
	private static void showMakerScripts(Player player, NpcMaker maker)
	{
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(0);
		adminReply.setFile("data/html/admin/spawnlist/maker_scripts.htm");
		
		adminReply.replace("%MAKERNAME%", maker.getName());
		adminReply.replace("%MAKERSPAWN%", maker.getMaker().getMakerName());
		adminReply.replace("%MAKEREVENT%", (maker.getEvent() == null) ? "" : maker.getEvent());
		adminReply.replace("%MAKERSTART%", "" + maker.isOnStart());
		
		final List<Quest> quests = maker.getQuestEvents();
		if (quests.isEmpty())
			adminReply.replace("%SCRIPTS%", "<br><br><center>This NPC maker isn't affected by scripts.</center>");
		else
		{
			final StringBuilder sb = new StringBuilder("<br><font color=\"LEVEL\">ON_MAKER_NPCS_KILLED</font><br1>");
			
			for (Quest quest : quests)
				StringUtil.append(sb, quest.getName(), "<br1>");
			
			adminReply.replace("%SCRIPTS%", sb.toString());
		}
		
		player.sendPacket(adminReply);
	}
}