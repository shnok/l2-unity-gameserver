package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeShowMemberListDelete;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestWithdrawPledge extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Do nothing.
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Clan clan = player.getClan();
		if (clan == null)
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
			return;
		}
		
		if (player.isClanLeader())
		{
			player.sendPacket(SystemMessageId.CLAN_LEADER_CANNOT_WITHDRAW);
			return;
		}
		
		if (player.isInCombat())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_LEAVE_DURING_COMBAT);
			return;
		}
		
		clan.removeClanMember(player.getObjectId(), System.currentTimeMillis() + Config.CLAN_JOIN_DAYS * 86400000L);
		clan.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_WITHDRAWN_FROM_THE_CLAN).addCharName(player));
		
		// Remove the player from the members list.
		if (clan.isSubPledgeLeader(player.getObjectId()))
			clan.broadcastClanStatus(); // refresh list
		else
			clan.broadcastToMembers(new PledgeShowMemberListDelete(player.getName()));
		
		player.sendPacket(SystemMessageId.YOU_HAVE_WITHDRAWN_FROM_CLAN);
		player.sendPacket(SystemMessageId.YOU_MUST_WAIT_BEFORE_JOINING_ANOTHER_CLAN);
		
		// Refresh surrounding Clan War tags.
		player.forEachKnownType(Player.class, attacker -> clan.getWarList().contains(attacker.getClanId()), Player::broadcastUserInfo);
	}
}