package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestStartPledgeWar extends L2GameClientPacket
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
		
		final Clan attackerClan = player.getClan();
		if (attackerClan == null)
			return;
		
		if (!player.hasClanPrivileges(PrivilegeType.SP_CLAN_WAR))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		final Clan attackedClan = ClanTable.getInstance().getClanByName(_pledgeName);
		if (attackedClan == null)
		{
			player.sendPacket(SystemMessageId.CLAN_WAR_CANNOT_DECLARED_CLAN_NOT_EXIST);
			return;
		}
		
		if (attackedClan.getClanId() == attackerClan.getClanId())
		{
			player.sendPacket(SystemMessageId.CANNOT_DECLARE_AGAINST_OWN_CLAN);
			return;
		}
		
		if (attackerClan.getWarList().size() >= 30)
		{
			player.sendPacket(SystemMessageId.TOO_MANY_CLAN_WARS);
			return;
		}
		
		if (attackerClan.getLevel() < 3 || attackerClan.getMembersCount() < Config.CLAN_MEMBERS_FOR_WAR)
		{
			player.sendPacket(SystemMessageId.CLAN_WAR_DECLARED_IF_CLAN_LVL3_OR_15_MEMBER);
			return;
		}
		
		if (!attackerClan.getAttackerList().contains(attackedClan.getClanId()) && (attackedClan.getLevel() < 3 || attackedClan.getMembersCount() < Config.CLAN_MEMBERS_FOR_WAR))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CLAN_CANNOT_DECLARE_WAR_TOO_LOW_LEVEL_OR_NOT_ENOUGH_MEMBERS).addString(attackedClan.getName()));
			return;
		}
		
		if (attackerClan.getAllyId() == attackedClan.getAllyId() && attackerClan.getAllyId() != 0)
		{
			player.sendPacket(SystemMessageId.CLAN_WAR_AGAINST_A_ALLIED_CLAN_NOT_WORK);
			return;
		}
		
		if (attackedClan.getDissolvingExpiryTime() > 0)
		{
			player.sendPacket(SystemMessageId.NO_CLAN_WAR_AGAINST_DISSOLVING_CLAN);
			return;
		}
		
		if (attackerClan.isAtWarWith(attackedClan.getClanId()))
		{
			player.sendPacket(SystemMessageId.WAR_ALREADY_DECLARED);
			return;
		}
		
		if (attackerClan.hasWarPenaltyWith(attackedClan.getClanId()))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_AT_WAR_WITH_S1_WAIT_5_DAYS).addString(attackedClan.getName()));
			return;
		}
		
		ClanTable.getInstance().storeClansWars(player.getClanId(), attackedClan.getClanId());
		
		for (Player member : attackedClan.getOnlineMembers())
			member.broadcastUserInfo();
		
		for (Player member : attackerClan.getOnlineMembers())
			member.broadcastUserInfo();
	}
}