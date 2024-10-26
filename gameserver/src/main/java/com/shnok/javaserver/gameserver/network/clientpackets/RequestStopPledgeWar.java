package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.enums.PrivilegeType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.SystemMessageId;

public final class RequestStopPledgeWar extends L2GameClientPacket
{
	private String _pledgeName;
	
	@Override
	protected void readImpl()
	{
		_pledgeName = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Clan playerClan = player.getClan();
		if (playerClan == null)
			return;
		
		final Clan clan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (clan == null)
			return;
		
		if (!player.hasClanPrivileges(PrivilegeType.SP_CLAN_WAR))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		if (!playerClan.isAtWarWith(clan.getClanId()))
		{
			player.sendPacket(SystemMessageId.NOT_INVOLVED_IN_WAR);
			return;
		}
		
		for (Player member : playerClan.getOnlineMembers())
		{
			if (member.isInCombat())
			{
				player.sendPacket(SystemMessageId.CANT_STOP_CLAN_WAR_WHILE_IN_COMBAT);
				return;
			}
		}
		
		ClanTable.getInstance().deleteClansWars(playerClan.getClanId(), clan.getClanId());
		
		for (Player member : clan.getOnlineMembers())
			member.broadcastUserInfo();
		
		for (Player member : playerClan.getOnlineMembers())
			member.broadcastUserInfo();
	}
}