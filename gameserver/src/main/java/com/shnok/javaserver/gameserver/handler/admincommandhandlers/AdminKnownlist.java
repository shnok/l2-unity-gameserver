package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import com.shnok.javaserver.commons.data.Pagination;

import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;

public class AdminKnownlist implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_knownlist"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		
		if (command.startsWith("admin_knownlist"))
		{
			// Pick potential player's target or the player himself.
			final WorldObject targetWorldObject = getTarget(WorldObject.class, player, true);
			
			int page = 1;
			
			if (st.hasMoreTokens())
			{
				try
				{
					page = Integer.parseInt(st.nextToken());
				}
				catch (Exception e)
				{
					player.sendMessage("Usage: //knownlist [page]");
				}
			}
			
			showKnownlist(player, targetWorldObject, page);
		}
	}
	
	private static void showKnownlist(Player player, WorldObject worldObject, int page)
	{
		// Load static htm.
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/knownlist.htm");
		html.replace("%target%", worldObject.getName());
		
		int row = 0;
		
		// Generate data.
		final Pagination<WorldObject> list = new Pagination<>(worldObject.getKnownType(WorldObject.class).stream(), page, PAGE_LIMIT_15);
		for (WorldObject wo : list)
		{
			list.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
			list.append("<td width=160>", wo.getName(), "</td><td width=120>", wo.getClass().getSimpleName(), "</td>");
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
			
			row++;
		}
		
		list.generateSpace(20);
		list.generatePages("bypass admin_knownlist %page%");
		
		html.replace("%content%", list.getContent());
		
		player.sendPacket(html);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}