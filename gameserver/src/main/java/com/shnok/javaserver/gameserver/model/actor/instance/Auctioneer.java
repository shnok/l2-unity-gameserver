package com.shnok.javaserver.gameserver.model.actor.instance;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.math.MathUtil;

import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.data.xml.RestartPointData;
import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.clanhall.Auction;
import com.shnok.javaserver.gameserver.model.residence.clanhall.Bidder;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHall;
import com.shnok.javaserver.gameserver.model.residence.clanhall.Seller;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;

public final class Auctioneer extends Folk
{
	private static final int PAGE_LIMIT = 15;
	
	public Auctioneer(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		
		final String actualCommand = st.nextToken();
		final String val = (st.hasMoreTokens()) ? st.nextToken() : "";
		
		// No Auctionable ClanHalls to bid on ; deny all possible actions.
		if (ClanHallManager.getInstance().getAuctionableClanHalls().isEmpty())
		{
			player.sendPacket(SystemMessageId.NO_CLAN_HALLS_UP_FOR_AUCTION);
			return;
		}
		
		// Only a few actions are possible for clanless people.
		if (actualCommand.equalsIgnoreCase("list"))
		{
			showAuctionsList(val, player);
			return;
		}
		else if (actualCommand.equalsIgnoreCase("bidding"))
		{
			if (val.isEmpty())
				return;
			
			try
			{
				final ClanHall ch = ClanHallManager.getInstance().getClanHall(Integer.parseInt(val));
				if (ch != null)
				{
					final Auction auction = ch.getAuction();
					if (auction != null)
					{
						final long remainingTime = auction.getEndDate() - System.currentTimeMillis();
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/auction/AgitAuctionInfo.htm");
						html.replace("%AGIT_NAME%", ch.getName());
						html.replace("%AGIT_SIZE%", ch.getSize());
						html.replace("%AGIT_LEASE%", ch.getLease());
						html.replace("%AGIT_LOCATION%", ch.getTownName());
						html.replace("%AGIT_AUCTION_END%", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(auction.getEndDate()));
						html.replace("%AGIT_AUCTION_REMAIN%", (remainingTime / 3600000) + " hours " + ((remainingTime / 60000) % 60) + " minutes");
						html.replace("%AGIT_AUCTION_MINBID%", auction.getMinimumBid());
						html.replace("%AGIT_AUCTION_COUNT%", auction.getBidders().size());
						html.replace("%AGIT_AUCTION_DESC%", ch.getDesc());
						html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_list");
						html.replace("%AGIT_LINK_BIDLIST%", "bypass -h npc_" + getObjectId() + "_bidlist " + ch.getId());
						html.replace("%AGIT_LINK_RE%", "bypass -h npc_" + getObjectId() + "_bid1 " + ch.getId());
						
						final Seller seller = auction.getSeller();
						if (seller == null)
						{
							html.replace("%OWNER_PLEDGE_NAME%", "");
							html.replace("%OWNER_PLEDGE_MASTER%", "");
						}
						else
						{
							html.replace("%OWNER_PLEDGE_NAME%", seller.getClanName());
							html.replace("%OWNER_PLEDGE_MASTER%", seller.getName());
						}
						
						player.sendPacket(html);
					}
				}
			}
			catch (Exception e)
			{
				// Do nothing.
			}
			return;
		}
		else if (actualCommand.equalsIgnoreCase("location"))
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/auction/map_agit_" + RestartPointData.getInstance().getAgitMap(player) + ".htm");
			html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_start");
			player.sendPacket(html);
			return;
		}
		else if (actualCommand.equalsIgnoreCase("start"))
		{
			showChatWindow(player);
			return;
		}
		// Commands allowed for clan members with privileges.
		else
		{
			final Clan clan = player.getClan();
			if (clan == null || !player.hasClanPrivileges(PrivilegeType.CHP_AUCTION))
			{
				showAuctionsList("1", player);
				player.sendPacket(SystemMessageId.CANNOT_PARTICIPATE_IN_AUCTION);
				return;
			}
			
			if (actualCommand.equalsIgnoreCase("bid"))
			{
				if (val.isEmpty())
					return;
				
				try
				{
					final int bid = (st.hasMoreTokens()) ? Math.min(Integer.parseInt(st.nextToken()), Integer.MAX_VALUE) : 0;
					
					final Auction auction = ClanHallManager.getInstance().getAuction(Integer.parseInt(val));
					if (auction != null)
						auction.setBid(player, bid);
				}
				catch (Exception e)
				{
					// Do nothing.
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("bid1"))
			{
				if (val.isEmpty())
					return;
				
				try
				{
					if (clan.getLevel() < 2)
					{
						showAuctionsList("1", player);
						player.sendPacket(SystemMessageId.AUCTION_ONLY_CLAN_LEVEL_2_HIGHER);
						return;
					}
					
					if (clan.hasClanHall())
					{
						showAuctionsList("1", player);
						player.sendPacket(SystemMessageId.CANNOT_PARTICIPATE_IN_AUCTION);
						return;
					}
					
					if (clan.getAuctionBiddedAt() > 0 && clan.getAuctionBiddedAt() != Integer.parseInt(val))
					{
						showAuctionsList("1", player);
						player.sendPacket(SystemMessageId.ALREADY_SUBMITTED_BID);
						return;
					}
					
					final ClanHall ch = ClanHallManager.getInstance().getClanHall(Integer.parseInt(val));
					if (ch == null)
						return;
					
					final Auction auction = ch.getAuction();
					if (auction == null)
						return;
					
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/auction/AgitBid1.htm");
					html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_bidding " + val);
					html.replace("%PLEDGE_ADENA%", clan.getWarehouse().getAdena());
					html.replace("%AGIT_AUCTION_MINBID%", auction.getMinimumBid());
					html.replace("npc_%objectId%_bid", "npc_" + getObjectId() + "_bid " + val);
					player.sendPacket(html);
				}
				catch (Exception e)
				{
					// Do nothing.
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("bidlist"))
			{
				try
				{
					final int auctionId = (val.isEmpty()) ? clan.getAuctionBiddedAt() : Integer.parseInt(val);
					
					final Auction auction = ClanHallManager.getInstance().getAuction(auctionId);
					if (auction == null)
						return;
					
					boolean isSeller = false;
					
					final Seller seller = auction.getSeller();
					if (seller != null)
						isSeller = seller.getClanName().equalsIgnoreCase(clan.getName());
					
					final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					final Collection<Bidder> bidders = auction.getBidders().values();
					
					final StringBuilder sb = new StringBuilder(bidders.size() * 150);
					for (Bidder bidder : bidders)
						StringUtil.append(sb, "<tr><td width=90 align=center>", bidder.getClanName(), "</td><td width=90 align=center>", bidder.getName(), "</td><td width=90 align=center>", sdf.format(bidder.getTime()), "</td></tr>");
					
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/auction/AgitBidderList.htm");
					html.replace("%AGIT_LIST%", sb.toString());
					html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + ((isSeller) ? "_selectedItems" : ("_bidding " + auctionId)));
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
				}
				catch (Exception e)
				{
					// Do nothing.
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("selectedItems"))
			{
				showSelectedItems(player);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("cancelBid"))
			{
				try
				{
					final Auction auction = ClanHallManager.getInstance().getAuction(clan.getAuctionBiddedAt());
					if (auction == null)
						return;
					
					final Bidder bidder = auction.getBidders().get(player.getClanId());
					if (bidder == null)
						return;
					
					final int bid = bidder.getBid();
					
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/auction/AgitBidCancel.htm");
					html.replace("%AGIT_BID%", bid);
					html.replace("%AGIT_BID_REMAIN%", (int) (bid * 0.9));
					html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_selectedItems");
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
				}
				catch (Exception e)
				{
					// Do nothing.
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("doCancelBid"))
			{
				final Auction auction = ClanHallManager.getInstance().getAuction(clan.getAuctionBiddedAt());
				if (auction != null)
				{
					auction.cancelBid(clan);
					player.sendPacket(SystemMessageId.CANCELED_BID);
				}
				return;
			}
			else if (actualCommand.equalsIgnoreCase("cancelAuction"))
			{
				final ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
				if (ch == null)
					return;
				
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/auction/AgitSaleCancel.htm");
				html.replace("%AGIT_DEPOSIT%", ch.getLease());
				html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_selectedItems");
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("doCancelAuction"))
			{
				final Auction auction = ClanHallManager.getInstance().getAuction(clan.getClanHallId());
				if (auction != null)
				{
					auction.cancelAuction();
					player.sendPacket(SystemMessageId.CANCELED_BID);
				}
				showChatWindow(player);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("sale"))
			{
				final ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
				if (ch == null)
					return;
				
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/auction/AgitSale1.htm");
				html.replace("%AGIT_DEPOSIT%", ch.getLease());
				html.replace("%AGIT_PLEDGE_ADENA%", clan.getWarehouse().getAdena());
				html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_selectedItems");
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
				return;
			}
			else if (actualCommand.equalsIgnoreCase("rebid"))
			{
				final ClanHall ch = ClanHallManager.getInstance().getClanHall(clan.getAuctionBiddedAt());
				if (ch == null)
					return;
				
				final Auction auction = ch.getAuction();
				if (auction == null)
					return;
				
				final Bidder bidder = auction.getBidders().get(player.getClanId());
				if (bidder == null)
					return;
				
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/auction/AgitBid2.htm");
				html.replace("%AGIT_AUCTION_BID%", bidder.getBid());
				html.replace("%AGIT_AUCTION_MINBID%", ch.getAuctionMin());
				html.replace("%AGIT_AUCTION_END%", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(auction.getEndDate()));
				html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_selectedItems");
				html.replace("npc_%objectId%_bid1", "npc_" + getObjectId() + "_bid1 " + ch.getId());
				player.sendPacket(html);
				return;
			}
			// Those bypasses check if CWH got enough Adena (case of sale auction type)
			else
			{
				if (clan.getWarehouse().getAdena() < ClanHallManager.getInstance().getClanHallByOwner(clan).getLease())
				{
					showSelectedItems(player);
					player.sendPacket(SystemMessageId.NOT_ENOUGH_ADENA_IN_CWH);
					return;
				}
				
				if (actualCommand.equalsIgnoreCase("auction"))
				{
					if (val.isEmpty())
						return;
					
					try
					{
						final int days = Integer.parseInt(val);
						final int bid = (st.hasMoreTokens()) ? Math.min(Integer.parseInt(st.nextToken()), Integer.MAX_VALUE) : 0;
						
						final ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
						if (ch == null)
							return;
						
						final Auction auction = ch.getAuction();
						if (auction == null)
							return;
						
						auction.setSeller(clan, bid);
						auction.setEndDate(days * 86400000L);
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/auction/AgitSale3.htm");
						html.replace("%x%", val);
						html.replace("%AGIT_AUCTION_END%", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(auction.getEndDate()));
						html.replace("%AGIT_AUCTION_MINBID%", ch.getAuctionMin());
						html.replace("%AGIT_AUCTION_MIN%", bid);
						html.replace("%AGIT_AUCTION_DESC%", ch.getDesc());
						html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_sale2");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					catch (Exception e)
					{
						// Do nothing.
					}
					return;
				}
				else if (actualCommand.equalsIgnoreCase("confirmAuction"))
				{
					final ClanHall ch = ClanHallManager.getInstance().getClanHall(clan.getClanHallId());
					if (ch == null)
						return;
					
					final Auction auction = ch.getAuction();
					if (auction == null || auction.getSeller() == null)
						return;
					
					if (auction.takeItem(player, ch.getLease()))
					{
						auction.confirmAuction();
						
						showSelectedItems(player);
						player.sendPacket(SystemMessageId.REGISTERED_FOR_CLANHALL);
					}
					return;
				}
				else if (actualCommand.equalsIgnoreCase("sale2"))
				{
					final ClanHall ch = ClanHallManager.getInstance().getClanHallByOwner(clan);
					if (ch == null)
						return;
					
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/auction/AgitSale2.htm");
					html.replace("%AGIT_LAST_PRICE%", ch.getLease());
					html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_sale");
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
					return;
				}
			}
		}
		super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/auction/auction.htm");
		html.replace("%objectId%", getObjectId());
		html.replace("%npcId%", getNpcId());
		html.replace("%npcname%", getName());
		player.sendPacket(html);
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		if (val == 0)
			return;
		
		super.showChatWindow(player, val);
	}
	
	private void showAuctionsList(String val, Player player)
	{
		// Retrieve the whole auctions list.
		List<ClanHall> chs = ClanHallManager.getInstance().getAuctionableClanHalls();
		
		final int page = (val.isEmpty()) ? 1 : Integer.parseInt(val);
		final int max = MathUtil.countPagesNumber(chs.size(), PAGE_LIMIT);
		
		// Cut auctions list up to page number.
		chs = chs.subList((page - 1) * PAGE_LIMIT, Math.min(page * PAGE_LIMIT, chs.size()));
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final StringBuilder sb = new StringBuilder(4000);
		
		sb.append("<table width=280>");
		
		// Auctions feeding.
		for (ClanHall ch : chs)
		{
			final Auction auction = ch.getAuction();
			StringUtil.append(sb, "<tr><td><font color=\"aaaaff\">", ch.getTownName(), "</font></td><td><font color=\"ffffaa\"><a action=\"bypass -h npc_", getObjectId(), "_bidding ", ch.getId(), "\">", ch.getName(), " [", auction.getBidders().size(), "]</a></font></td><td>", sdf.format(auction.getEndDate()), "</td><td><font color=\"aaffff\">", auction.getMinimumBid(), "</font></td></tr>");
		}
		
		sb.append("</table><table width=280><tr>");
		
		// Page feeding.
		for (int j = 1; j <= max; j++)
			StringUtil.append(sb, "<td align=center><a action=\"bypass -h npc_", getObjectId(), "_list ", j, "\"> Page ", j, " </a></td>");
		
		sb.append("</tr></table>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/auction/AgitAuctionList.htm");
		html.replace("%AGIT_LIST%", sb.toString());
		html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_start");
		player.sendPacket(html);
	}
	
	private void showSelectedItems(Player player)
	{
		final Clan clan = player.getClan();
		if (clan == null)
			return;
		
		if (!clan.hasClanHall() && clan.getAuctionBiddedAt() > 0)
		{
			final ClanHall ch = ClanHallManager.getInstance().getClanHall(clan.getAuctionBiddedAt());
			if (ch == null)
				return;
			
			final Auction auction = ch.getAuction();
			if (auction == null)
				return;
			
			final long remainingTime = auction.getEndDate() - System.currentTimeMillis();
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/auction/AgitBidInfo.htm");
			html.replace("%AGIT_NAME%", ch.getName());
			html.replace("%AGIT_SIZE%", ch.getSize());
			html.replace("%AGIT_LEASE%", ch.getLease());
			html.replace("%AGIT_LOCATION%", ch.getTownName());
			html.replace("%AGIT_AUCTION_END%", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(auction.getEndDate()));
			html.replace("%AGIT_AUCTION_REMAIN%", (remainingTime / 3600000) + " hours " + ((remainingTime / 60000) % 60) + " minutes");
			html.replace("%AGIT_AUCTION_MYBID%", auction.getBidders().get(player.getClanId()).getBid());
			html.replace("%AGIT_AUCTION_DESC%", ch.getDesc());
			html.replace("%objectId%", getObjectId());
			html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_start");
			
			final Seller seller = auction.getSeller();
			if (seller == null)
			{
				html.replace("%OWNER_PLEDGE_NAME%", "");
				html.replace("%OWNER_PLEDGE_MASTER%", "");
				html.replace("%AGIT_AUCTION_MINBID%", ch.getAuctionMin());
			}
			else
			{
				html.replace("%OWNER_PLEDGE_NAME%", seller.getClanName());
				html.replace("%OWNER_PLEDGE_MASTER%", seller.getName());
				html.replace("%AGIT_AUCTION_MINBID%", seller.getBid());
			}
			
			player.sendPacket(html);
			return;
		}
		
		if (clan.hasClanHall())
		{
			final ClanHall ch = ClanHallManager.getInstance().getClanHall(clan.getClanHallId());
			if (ch == null)
				return;
			
			final Auction auction = ch.getAuction();
			if (auction == null)
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			
			final Seller seller = auction.getSeller();
			if (seller != null)
			{
				final long remainingTime = auction.getEndDate() - System.currentTimeMillis();
				
				html.setFile("data/html/auction/AgitSaleInfo.htm");
				html.replace("%AGIT_NAME%", ch.getName());
				html.replace("%AGIT_OWNER_PLEDGE_NAME%", seller.getClanName());
				html.replace("%OWNER_PLEDGE_MASTER%", seller.getName());
				html.replace("%AGIT_SIZE%", ch.getSize());
				html.replace("%AGIT_LEASE%", ch.getLease());
				html.replace("%AGIT_LOCATION%", ch.getTownName());
				html.replace("%AGIT_AUCTION_END%", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(auction.getEndDate()));
				html.replace("%AGIT_AUCTION_REMAIN%", (remainingTime / 3600000) + " hours " + ((remainingTime / 60000) % 60) + " minutes");
				html.replace("%AGIT_AUCTION_MINBID%", seller.getBid());
				html.replace("%AGIT_AUCTION_BIDCOUNT%", auction.getBidders().size());
				html.replace("%AGIT_AUCTION_DESC%", ch.getDesc());
				html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_start");
				html.replace("%id%", ch.getId());
				html.replace("%objectId%", getObjectId());
			}
			else
			{
				html.setFile("data/html/auction/AgitInfo.htm");
				html.replace("%AGIT_NAME%", ch.getName());
				html.replace("%AGIT_OWNER_PLEDGE_NAME%", clan.getName());
				html.replace("%OWNER_PLEDGE_MASTER%", clan.getLeaderName());
				html.replace("%AGIT_SIZE%", ch.getSize());
				html.replace("%AGIT_LEASE%", ch.getLease());
				html.replace("%AGIT_LOCATION%", ch.getTownName());
				html.replace("%AGIT_LINK_BACK%", "bypass -h npc_" + getObjectId() + "_start");
				html.replace("%objectId%", getObjectId());
			}
			player.sendPacket(html);
			return;
		}
		
		showAuctionsList("1", player); // Force to display page 1.
		player.sendPacket(SystemMessageId.NO_OFFERINGS_OWN_OR_MADE_BID_FOR);
	}
}