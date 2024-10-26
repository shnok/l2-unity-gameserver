package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.model.location.TowerSpawnLocation;
import net.sf.l2j.gameserver.model.residence.castle.Castle;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SiegeInfo;

public class AdminSiege implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_castle",
		"admin_siege"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		command = st.nextToken();
		
		String param = null;
		Castle castle = null;
		
		final int paramCount = st.countTokens();
		if (paramCount == 1)
			castle = CastleManager.getInstance().getCastleByAlias(st.nextToken());
		else if (paramCount == 2)
		{
			param = st.nextToken();
			castle = CastleManager.getInstance().getCastleByAlias(st.nextToken());
		}
		
		if (castle == null)
		{
			showCastleSelectPage(player);
			return;
		}
		
		if (param == null)
		{
			showCastleSelectPage(player, castle);
			return;
		}
		
		if (command.startsWith("admin_castle"))
		{
			switch (param)
			{
				case "set":
					final Player targetPlayer = getTargetPlayer(player, false);
					if (targetPlayer == null || targetPlayer.getClan() == null)
						player.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					else if (targetPlayer.getClan().hasCastle())
						player.sendMessage(targetPlayer.getName() + "'s clan already owns a castle.");
					else
						castle.setOwner(targetPlayer.getClan());
					break;
				
				case "remove":
					if (castle.getOwnerId() > 0)
						castle.removeOwner();
					else
						player.sendMessage("This castle does not have an owner.");
					break;
				
				case "certificates":
					castle.setLeftCertificates(300, true);
					player.sendMessage(castle.getName() + "'s castle certificates are reset.");
					break;
				
				case "tax":
					castle.updateTaxes();
					player.sendMessage(castle.getName() + "'s taxes have been updated.");
					break;
				
				default:
					player.sendMessage("Usage: //castle [set|remove|certificates|tax castleName].");
					break;
			}
		}
		else if (command.startsWith("admin_siege"))
		{
			switch (param)
			{
				case "attack":
					Player targetPlayer = getTargetPlayer(player, false);
					if (targetPlayer == null)
						player.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					else
						castle.getSiege().registerAttacker(targetPlayer);
					break;
				
				case "clear":
					castle.getSiege().clearAllClans();
					break;
				
				case "defend":
					targetPlayer = getTargetPlayer(player, false);
					if (targetPlayer == null)
						player.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					else
						castle.getSiege().registerDefender(targetPlayer);
					break;
				
				case "end":
					castle.getSiege().endSiege();
					break;
				
				case "list":
					player.sendPacket(new SiegeInfo(castle));
					return;
				
				case "start":
					castle.getSiege().startSiege();
					break;
				
				default:
					player.sendMessage("Usage: //siege [attack|clear|defend|end|list|start castleName].");
					break;
			}
		}
		showCastleSelectPage(player, castle);
	}
	
	/**
	 * Show detailed informations of a {@link Castle} to a {@link Player}.
	 * @param player : The {@link Player} who requested the action.
	 * @param castle : The {@link Castle} to show informations.
	 */
	private static void showCastleSelectPage(Player player, Castle castle)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/castle.htm");
		html.replace("%castleName%", castle.getName());
		html.replace("%castleAlias%", castle.getAlias());
		html.replace("%circletId%", castle.getCircletId());
		html.replace("%artifactId%", castle.getArtifacts().toString());
		html.replace("%ticketsNumber%", castle.getTickets().size());
		html.replace("%droppedTicketsNumber%", castle.getDroppedTickets().size());
		html.replace("%npcsNumber%", castle.getNpcs().size());
		html.replace("%certificates%", castle.getLeftCertificates());
		html.replace("%parent%", castle.getParentId());
		html.replace("%aliveLifeTowers%", castle.getAliveLifeTowerCount());
		
		html.replace("%defaultTax%", castle.getDefaultTaxRate());
		html.replace("%currentTax%", castle.getCurrentTaxPercent());
		html.replace("%nextTax%", castle.getNextTaxPercent());
		html.replace("%taxSysgetRate%", castle.getTaxSysgetRate());
		html.replace("%taxRevenue%", StringUtil.formatNumber(castle.getTaxRevenue()));
		html.replace("%tributeRate%", StringUtil.formatNumber(castle.getTributeRate()));
		html.replace("%seedIncome%", StringUtil.formatNumber(castle.getSeedIncome()));
		html.replace("%treasury%", StringUtil.formatNumber(castle.getTreasury()));
		
		final StringBuilder sb = new StringBuilder();
		int index = 1;
		
		// Feed artifacts infos.
		for (SpawnLocation spawn : castle.getArtifacts())
		{
			StringUtil.append(sb, "<a action=\"bypass -h admin_teleport ", spawn.toString().replace(",", ""), "\">[", index, "]</a>&nbsp;&nbsp;");
			index++;
		}
		
		html.replace("%artifacts%", sb.toString());
		
		// Cleanup the sb and the index to reuse it.
		sb.setLength(0);
		index = 1;
		
		// Feed Control Tower infos.
		for (TowerSpawnLocation spawn : castle.getControlTowers())
		{
			StringUtil.append(sb, "<a action=\"bypass -h admin_teleport ", spawn.toString().replace(",", ""), "\">[", index, "]</a>&nbsp;&nbsp;");
			index++;
		}
		
		html.replace("%ct%", sb.toString());
		
		player.sendPacket(html);
	}
	
	/**
	 * Show the complete list of {@link Castle}s.
	 * @param player : The {@link Player} who requested the action.
	 */
	private static void showCastleSelectPage(Player player)
	{
		int row = 0;
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/castles.htm");
		
		final StringBuilder sb = new StringBuilder();
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			sb.append(((row % 2) == 0 ? "<table width=270 bgcolor=000000><tr>" : "<table width=270><tr>"));
			
			StringUtil.append(sb, "<td width=100><a action=\"bypass -h admin_siege ", castle.getAlias(), "\">", castle.getName(), "</a></td><td width=130>", castle.getSiege().getStatus(), "</td><td width=40 align=right><a action=\"bypass admin_siege list ", castle.getAlias(), "\">View</a></td>");
			
			sb.append("</tr></table><img src=\"L2UI.SquareGray\" width=270 height=1>");
			row++;
		}
		html.replace("%castles%", sb.toString());
		player.sendPacket(html);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}