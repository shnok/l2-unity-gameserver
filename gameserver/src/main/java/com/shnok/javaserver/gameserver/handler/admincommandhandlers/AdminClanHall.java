package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.SpawnType;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.clanhall.Auction;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHall;
import com.shnok.javaserver.gameserver.model.residence.clanhall.SiegableHall;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;

public class AdminClanHall implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ch"
	};
	
	private static final String[] LOCS =
	{
		"Aden",
		"Dion",
		"Giran",
		"Gludin",
		"Gludio",
		"Goddard",
		"Oren",
		"Rune",
		"Schuttgart"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		try
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			
			String param = null;
			String param2 = null;
			
			ClanHall ch = null;
			
			final int paramCount = st.countTokens();
			if (paramCount == 1)
				ch = ClanHallManager.getInstance().getClanHall(Integer.parseInt(st.nextToken()));
			else if (paramCount == 2)
			{
				param = st.nextToken();
				ch = ClanHallManager.getInstance().getClanHall(Integer.parseInt(st.nextToken()));
			}
			else if (paramCount == 3)
			{
				param = st.nextToken();
				param2 = st.nextToken();
				ch = ClanHallManager.getInstance().getClanHall(Integer.parseInt(st.nextToken()));
			}
			
			// Hold ClanHall reference, if not existing or chId is set to 0, we return the whole list of existing ClanHalls.
			if (ch == null)
			{
				showClanHallSelectPage(player);
				return;
			}
			
			// No command token, we show the individual ClanHall informations.
			if (param == null)
			{
				showClanHallSelectPage(player, ch);
				return;
			}
			
			switch (param)
			{
				case "set":
					final Player targetPlayer = getTargetPlayer(player, false);
					if (targetPlayer == null || targetPlayer.getClan() == null)
						player.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					else if (!ch.isFree())
						player.sendMessage("This ClanHall isn't free.");
					else if (targetPlayer.getClan().hasClanHall())
						player.sendMessage("Your target already owns a ClanHall.");
					else
						ch.setOwner(targetPlayer.getClan());
					break;
				
				case "remove":
					if (ch.isFree())
						player.sendMessage("This ClanHall is already free.");
					else
						ch.free();
					break;
				
				case "open":
					ch.openDoors();
					break;
				
				case "close":
					ch.closeDoors();
					break;
				
				case "teleportto":
					player.teleportTo(ch.getRndSpawn(SpawnType.OWNER), 0);
					break;
				
				case "end":
					final Auction auction = ch.getAuction();
					if (auction == null)
						player.sendMessage("This ClanHall doesn't hold an auction.");
					else
						auction.endAuction();
					break;
				
				case "siege":
					if (param2 == null || StringUtil.isDigit(param2))
						player.sendMessage("Usage: //ch siege start|end chId.");
					else if (!(ch instanceof SiegableHall sh))
						player.sendMessage("This ClanHall isn't siegable.");
					else
					{
						switch (param2)
						{
							case "start":
								if (sh.isInSiege())
									player.sendMessage("This ClanHall is already besieged.");
								else
									sh.getSiege().instantSiege();
								break;
							
							case "end":
								if (!sh.isInSiege())
									player.sendMessage("This ClanHall isn't currently besieged.");
								else
									sh.getSiege().endSiege();
								break;
						}
					}
					break;
			}
			showClanHallSelectPage(player, ch);
		}
		catch (Exception e)
		{
			player.sendMessage("Usage: //ch [set|remove|open|close|teleportto|end|siege chId].");
		}
	}
	
	/**
	 * Show detailed informations of a {@link ClanHall} to a {@link Player}.
	 * @param player : The {@link Player} who requested the action.
	 * @param ch : The {@link ClanHall} to show informations.
	 */
	private static void showClanHallSelectPage(Player player, ClanHall ch)
	{
		String clanName = "None";
		if (!ch.isFree())
		{
			final Clan clan = ClanTable.getInstance().getClan(ch.getOwnerId());
			if (clan != null)
				clanName = clan.getName();
		}
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final StringBuilder sb = new StringBuilder();
		
		final Auction auction = ch.getAuction();
		if (auction == null)
			sb.append("<tr><td>This clanhall doesn't have Auction.</td></tr>");
		else if (auction.getHighestBidder() == null)
			StringUtil.append(sb, "<tr><td width=150>Bid: 0</td><td width=120>Bidders: ", auction.getBidders().size(), "</td></tr><tr><td>End date: ", sdf.format(auction.getEndDate()), "</td><td>Winner: none</td></tr>");
		else
			StringUtil.append(sb, "<tr><td width=150>Bid: ", StringUtil.formatNumber(auction.getHighestBidder().getBid()), "</td><td width=120>Bidders: ", auction.getBidders().size(), "</td></tr><tr><td>End date: ", sdf.format(auction.getEndDate()), "</td><td>Winner: ", auction.getHighestBidder().getName(), "</td></tr>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/clanhall.htm");
		html.replace("%name%", ch.getName());
		html.replace("%id%", ch.getId());
		html.replace("%size%", ch.getSize());
		html.replace("%grade%", ch.getGrade());
		html.replace("%lease%", StringUtil.formatNumber(ch.getLease()));
		html.replace("%loc%", ch.getTownName());
		html.replace("%desc%", ch.getDesc());
		html.replace("%defaultbid%", StringUtil.formatNumber(ch.getAuctionMin()));
		html.replace("%owner%", clanName);
		html.replace("%paid%", ch.getPaid());
		html.replace("%paiduntil%", sdf.format(ch.getPaidUntil()));
		html.replace("%auction", sb.toString());
		player.sendPacket(html);
	}
	
	/**
	 * Show the complete list of {@link ClanHall}s (listed, unlisted, owned, siegable or auctionable).
	 * @param player : The {@link Player} who requested the action.
	 */
	private static void showClanHallSelectPage(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/clanhalls.htm");
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final StringBuilder sb = new StringBuilder();
		
		for (String loc : LOCS)
		{
			StringUtil.append(sb, "<font color=\"LEVEL\">", loc, "</font><br><table width=270><tr><td width=130>Clan Hall Name</td><td width=70>End Date</td><td width=70>Min Bid</td></tr>");
			
			for (ClanHall ch : ClanHallManager.getInstance().getClanHallsByLocation(loc))
			{
				final Auction auction = ch.getAuction();
				if (auction == null)
					StringUtil.append(sb, "<tr><td><a action=\"bypass -h admin_ch ", ch.getId(), "\">", ch.getName(), ((ch.isFree()) ? "" : "*"), "</a></td><td>-</td><td>-</td></tr>");
				else
					StringUtil.append(sb, "<tr><td><a action=\"bypass -h admin_ch ", ch.getId(), "\">", ch.getName(), ((ch.isFree()) ? "" : "*"), " [", auction.getBidders().size(), "]</a></td><td>", sdf.format(auction.getEndDate()), "</td><td>", auction.getMinimumBid(), "</td></tr>");
			}
			
			sb.append("</table>");
		}
		
		html.replace("%AGIT_LIST%", sb.toString());
		player.sendPacket(html);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}