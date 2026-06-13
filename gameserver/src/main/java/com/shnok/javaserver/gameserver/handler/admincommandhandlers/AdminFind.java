package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.data.Pagination;
import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.xml.ItemData;
import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.network.GameClient;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;

public class AdminFind implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_find"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		if (command.startsWith("admin_find"))
		{
			final int paramCount = st.countTokens();
			if (paramCount < 1)
			{
				listPlayers(player, 1, "");
				return;
			}
			
			final String param = st.nextToken();
			final String nameIpOrPage = (paramCount > 1) ? st.nextToken() : "";
			final String search = (paramCount > 2) ? st.nextToken().toLowerCase() : "";
			
			switch (param)
			{
				case "player":
					if (paramCount < 2)
					{
						listPlayers(player, 1, "");
						return;
					}
					
					try
					{
						if (StringUtil.isDigit(nameIpOrPage))
							listPlayers(player, Integer.parseInt(nameIpOrPage), search);
						else
							listPlayers(player, 1, nameIpOrPage);
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //find player name");
						listPlayers(player, 1, "");
					}
					break;
				
				case "ip":
					if (paramCount < 2)
					{
						listPlayersPerIp(player, "127.0.0.1");
						return;
					}
					
					try
					{
						listPlayersPerIp(player, nameIpOrPage);
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //find ip 111.222.333.444");
					}
					break;
				
				case "account":
					if (paramCount < 2)
					{
						listPlayersPerAccount(player, player.getName());
						return;
					}
					
					try
					{
						listPlayersPerAccount(player, nameIpOrPage);
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //find account name");
					}
					break;
				
				case "dualbox":
					try
					{
						final int multibox = Integer.parseInt(nameIpOrPage);
						if (multibox < 1)
						{
							player.sendMessage("Usage: //find dualbox [number > 0]");
							return;
						}
						
						listDualbox(player, multibox);
					}
					catch (Exception e)
					{
						listDualbox(player, 2);
					}
					break;
				
				case "item":
					if (paramCount < 2)
					{
						listItems(player, 1, "");
						return;
					}
					
					try
					{
						if (StringUtil.isDigit(nameIpOrPage))
							listItems(player, Integer.parseInt(nameIpOrPage), command.split(nameIpOrPage, 2)[1].trim());
						else
							listItems(player, 1, command.split(param, 2)[1].trim());
					}
					catch (Exception e)
					{
						listItems(player, 1, command.split(param, 2)[1].trim());
					}
					break;
				
				case "npc":
					if (paramCount < 2)
					{
						listNpcs(player, 1, "");
						return;
					}
					
					try
					{
						if (StringUtil.isDigit(nameIpOrPage))
							listNpcs(player, Integer.parseInt(nameIpOrPage), command.split(nameIpOrPage, 2)[1].trim());
						else
							listNpcs(player, 1, command.split(param, 2)[1].trim());
					}
					catch (Exception e)
					{
						listNpcs(player, 1, command.split(param, 2)[1].trim());
					}
					break;
				
				default:
					player.sendMessage("Usage: //find [account|dualbox|ip|item|npc|player name/id]");
					break;
			}
		}
	}
	
	/**
	 * Find all {@link Player}s and paginate them, then send back the results to the {@link Player}.
	 * @param player : The {@link Player} to send back results.
	 * @param page : The page to show.
	 * @param search : The {@link String} used as search.
	 */
	private static void listPlayers(Player player, int page, String search)
	{
		// Generate data.
		final Pagination<Player> list = new Pagination<>(World.getInstance().getPlayers().stream(), page, PAGE_LIMIT_12, p -> p.getName().toLowerCase().contains(search));
		list.append("<html><body>");
		
		list.generateSearch("bypass admin_find player", 45);
		list.append("<br1><table width=280 height=26><tr><td width=140>Name</td><td width=120>Class</td><td width=20>Lvl</td></tr></table>");
		
		for (Player targetPlayer : list)
		{
			list.append(((list.indexOf(targetPlayer) % 2) == 0 ? "<table width=280 height=22 bgcolor=000000><tr>" : "<table width=280><tr>"));
			list.append("<td width=140><a action=\"bypass -h admin_debug ", targetPlayer.getName(), "\">", targetPlayer.getName(), "</a></td><td width=120>", targetPlayer.getTemplate().getClassName(), "</td><td width=20>", targetPlayer.getStatus().getLevel(), "</td>");
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
		}
		list.generateSpace(22);
		list.generatePages("bypass admin_find player %page% " + search);
		list.append("</body></html>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(list.getContent());
		player.sendPacket(html);
	}
	
	/**
	 * List all {@link Player}s attached to an IP and send results to the {@link Player} set as parameter.
	 * @param player : The {@link Player} who requested the action.
	 * @param ipAdress : The {@link String} used as tested IP.
	 * @throws IllegalArgumentException if the IP is malformed.
	 */
	private static void listPlayersPerIp(Player player, String ipAdress) throws IllegalArgumentException
	{
		boolean findDisconnected = false;
		
		if (ipAdress.equals("disconnected"))
			findDisconnected = true;
		else
		{
			if (!ipAdress.matches("^(?:(?:[0-9]|[1-9][0-9]|1[0-9][0-9]|2(?:[0-4][0-9]|5[0-5]))\\.){3}(?:[0-9]|[1-9][0-9]|1[0-9][0-9]|2(?:[0-4][0-9]|5[0-5]))$"))
				throw new IllegalArgumentException("Malformed IPv4 number");
		}
		
		int charactersFound = 0;
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/ipfind.htm");
		
		final StringBuilder sb = new StringBuilder(1000);
		for (Player worldPlayer : World.getInstance().getPlayers())
		{
			final GameClient client = worldPlayer.getClient();
			if (client.isDetached())
			{
				if (!findDisconnected)
					continue;
			}
			else
			{
				if (findDisconnected)
					continue;
				
				if (!client.getConnection().getInetAddress().getHostAddress().equals(ipAdress))
					continue;
			}
			
			StringUtil.append(sb, "<tr><td><a action=\"bypass -h admin_debug ", worldPlayer.getName(), "\">", worldPlayer.getName(), "</a></td><td>", worldPlayer.getTemplate().getClassName(), "</td><td>", worldPlayer.getStatus().getLevel(), "</td></tr>");
			
			if (charactersFound++ > 20)
				break;
		}
		
		if (charactersFound > 20)
			html.replace("%number%", "more than 20");
		else
			html.replace("%number%", charactersFound);
		
		html.replace("%ip%", ipAdress);
		html.replace("%results%", sb.toString());
		player.sendPacket(html);
	}
	
	/**
	 * List all characters names attached to an ONLINE {@link Player} name and send results to the {@link Player} set as parameter.
	 * @param player : The {@link Player} who requested the action.
	 * @param name : The {@link String} name to test.
	 */
	private static void listPlayersPerAccount(Player player, String name)
	{
		final Player worldPlayer = World.getInstance().getPlayer(name);
		if (worldPlayer == null)
		{
			player.sendPacket(SystemMessageId.TARGET_CANT_FOUND);
			return;
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/accountinfo.htm");
		html.replace("%name%", name);
		html.replace("%characters%", String.join("<br1>", worldPlayer.getAccountChars().values()));
		html.replace("%account%", worldPlayer.getAccountName());
		player.sendPacket(html);
	}
	
	/**
	 * Test multiboxing {@link Player}s and send results to the {@link Player} set as parameter.
	 * @param player : The {@link Player} who requested the action.
	 * @param multibox : The tested value to trigger multibox.
	 */
	private static void listDualbox(Player player, int multibox)
	{
		final Map<String, List<Player>> ips = new HashMap<>();
		final Map<String, Integer> dualboxIPs = new HashMap<>();
		
		for (Player worldPlayer : World.getInstance().getPlayers())
		{
			final GameClient client = worldPlayer.getClient();
			if (client == null || client.isDetached())
				continue;
			
			final String ip = client.getConnection().getInetAddress().getHostAddress();
			
			final List<Player> list = ips.computeIfAbsent(ip, k -> new ArrayList<>());
			list.add(worldPlayer);
			
			if (list.size() >= multibox)
			{
				Integer count = dualboxIPs.get(ip);
				if (count == null)
					dualboxIPs.put(ip, multibox);
				else
					dualboxIPs.put(ip, count++);
			}
		}
		
		final List<String> keys = dualboxIPs.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey).toList();
		
		final StringBuilder sb = new StringBuilder();
		for (String dualboxIP : keys)
			StringUtil.append(sb, "<a action=\"bypass -h admin_find ip ", dualboxIP, "\">", dualboxIP, " (", dualboxIPs.get(dualboxIP), ")</a><br1>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/dualbox.htm");
		html.replace("%multibox%", multibox);
		html.replace("%results%", sb.toString());
		player.sendPacket(html);
	}
	
	private static void listItems(Player player, int page, String search)
	{
		// Generate data.
		final Pagination<Item> list = new Pagination<>(Arrays.asList(ItemData.getInstance().getTemplates()).stream(), page, PAGE_LIMIT_7, item -> item != null && matches(item.getName(), search));
		list.append("<html><body>");
		
		list.generateSearch("bypass admin_find item", 45);
		
		for (Item item : list)
		{
			list.append(((list.indexOf(item) % 2) == 0 ? "<table width=280 height=22 bgcolor=000000><tr>" : "<table width=280><tr>"));
			list.append("<td width=36 height=41 align=center><table><tr><td><img src=icon.noimage width=32 height=32></td></tr></table></td>");
			list.append("<td width=160>", StringUtil.trimAndDress(item.getName(), 28), "<br1><font color=\"B09878\">Item Id:</font> <font color=BDB76B>", item.getItemId(), (item.isQuestItem() ? " (Quest)" : ""), "</font></td>");
			list.append("<td><edit var=\"amount_", item.getItemId(), "\" width=52 type=number></td>");
			list.append("<td><button action=\"bypass admin_give ", item.getItemId(), " $amount_", item.getItemId(), "\" width=32 height=32 back=L2UI_CH3.mapbutton_zoomin2 fore=L2UI_CH3.mapbutton_zoomin1></td>");
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
		}
		
		list.generateSpace(42);
		list.generatePages("bypass admin_find item %page% " + search);
		list.append("</body></html>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(list.getContent());
		player.sendPacket(html);
	}
	
	private static void listNpcs(Player player, int page, String search)
	{
		// Generate data.
		final Pagination<NpcTemplate> list = new Pagination<>(NpcData.getInstance().getTemplates().stream(), page, PAGE_LIMIT_7, npc -> npc != null && !npc.getName().isEmpty() && matches(npc.getName(), search), Comparator.comparing(NpcTemplate::getName));
		list.append("<html><body>");
		
		list.generateSearch("bypass admin_find npc", 45);
		
		for (NpcTemplate template : list)
		{
			list.append(((list.indexOf(template) % 2) == 0 ? "<table width=280 height=22 bgcolor=000000><tr>" : "<table width=280><tr>"));
			list.append("<td width=216 height=41><font color=\"B09878\">", template.getTitle(), "</font><br1>", template.getName(), "</td>");
			list.append("<td><button action=\"bypass admin_spawn ", template.getNpcId(), "\" width=32 height=32 back=L2UI_CH3.mapbutton_zoomin2 fore=L2UI_CH3.mapbutton_zoomin1></td>");
			list.append("<td><button action=\"bypass admin_list_spawns ", template.getNpcId(), "\" width=32 height=32 back=L2UI_CH3.mapicon_mark_light fore=L2UI_CH3.mapicon_mark></td>");
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
		}
		
		list.generateSpace(42);
		list.generatePages("bypass admin_find npc %page% " + search);
		list.append("</body></html>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(list.getContent());
		player.sendPacket(html);
	}
	
	public static boolean matches(String name, String search)
	{
		return Arrays.stream(search.toLowerCase().split(" ")).allMatch(result -> name.toLowerCase().contains(result));
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}