package com.shnok.javaserver.gameserver.handler.usercommandhandlers;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.manager.CastleManager;
import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.handler.IUserCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;

public class SiegeStatus implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		99
	};
	
	private static final String IN_PROGRESS = "Castle Siege in Progress";
	private static final String OUTSIDE_ZONE = "Outside Castle Siege Zone";
	
	@Override
	public void useUserCommand(int id, Player player)
	{
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMessageId.ONLY_CLAN_LEADER_CAN_ISSUE_COMMANDS);
			return;
		}
		
		if (!player.isNoble())
		{
			player.sendPacket(SystemMessageId.ONLY_NOBLESSE_LEADER_CAN_VIEW_SIEGE_STATUS_WINDOW);
			return;
		}
		
		final Clan clan = player.getClan();
		
		final StringBuilder sb = new StringBuilder();
		
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			// Search on lists : as a clan can only be registered in a single siege, break after one case is found.
			if (!castle.getSiege().isInProgress() || !castle.getSiege().checkSides(clan, SiegeSide.ATTACKER, SiegeSide.DEFENDER, SiegeSide.OWNER))
				continue;
			
			for (Player member : clan.getOnlineMembers())
				StringUtil.append(sb, "<tr><td width=170>", member.getName(), "</td><td width=100>", (castle.getSiegeZone().isInsideZone(member)) ? IN_PROGRESS : OUTSIDE_ZONE, "</td></tr>");
			
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile("data/html/siege_status.htm");
			html.replace("%kills%", clan.getSiegeKills());
			html.replace("%deaths%", clan.getSiegeDeaths());
			html.replace("%content%", sb.toString());
			player.sendPacket(html);
			return;
		}
		
		player.sendPacket(SystemMessageId.ONLY_DURING_SIEGE);
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}