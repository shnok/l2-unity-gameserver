package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import com.shnok.javaserver.commons.data.Pagination;
import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.sql.BookmarkTable;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.Bookmark;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;

public class AdminBookmark implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_bk",
		"admin_delbk"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		
		int page = 1;
		
		if (command.startsWith("admin_bk"))
		{
			if (st.hasMoreTokens())
			{
				final String param = st.nextToken();
				if (StringUtil.isDigit(param))
					page = Integer.parseInt(param);
				else
				{
					if (param.length() > 15)
					{
						player.sendMessage("The bookmark name is too long.");
						return;
					}
					
					if (BookmarkTable.getInstance().isExisting(param, player.getObjectId()))
					{
						player.sendMessage("The bookmark name already exists.");
						return;
					}
					
					BookmarkTable.getInstance().saveBookmark(param, player);
				}
			}
		}
		else if (command.startsWith("admin_delbk"))
		{
			if (!st.hasMoreTokens())
			{
				player.sendMessage("The command delbk must be followed by a valid name.");
				return;
			}
			
			final String param = st.nextToken();
			
			if (!BookmarkTable.getInstance().isExisting(param, player.getObjectId()))
			{
				player.sendMessage("That bookmark doesn't exist.");
				return;
			}
			
			BookmarkTable.getInstance().deleteBookmark(param, player.getObjectId());
		}
		showBookmarks(player, page);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * Show the basic HTM fed with generated data.
	 * @param player : The {@link Player} to test.
	 * @param page : The page id to show.
	 */
	private static void showBookmarks(Player player, int page)
	{
		// Load static htm.
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/bk.htm");
		
		int row = 0;
		
		// Generate data.
		final Pagination<Bookmark> list = new Pagination<>(BookmarkTable.getInstance().getBookmarks(player.getObjectId()).stream(), page, PAGE_LIMIT_15);
		for (Bookmark bk : list)
		{
			list.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
			list.append("<td width=230><a action=\"bypass -h admin_teleport ", bk.x(), " ", bk.y(), " ", bk.z(), "\">", bk.name(), " (", bk.x(), " ", bk.y(), " ", bk.z(), ")", "</a></td><td width=50><a action=\"bypass -h admin_delbk ", bk.name(), "\">Remove</a></td>");
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
			
			row++;
		}
		
		list.generateSpace(20);
		list.generatePages("bypass admin_bk %page%");
		
		html.replace("%content%", list.getContent());
		player.sendPacket(html);
	}
}