package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.pledge.SubPledge;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.JoinPledge;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListAdd;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListAll;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestAnswerJoinPledge extends L2GameClientPacket
{
	private int _answer;
	
	@Override
	protected void readImpl()
	{
		_answer = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player requestor = player.getRequest().getPartner();
		if (requestor == null)
			return;
		
		if (_answer == 0)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_DID_NOT_RESPOND_TO_S1_CLAN_INVITATION).addCharName(requestor));
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DID_NOT_RESPOND_TO_CLAN_INVITATION).addCharName(player));
		}
		else
		{
			if (!(requestor.getRequest().getRequestPacket() instanceof RequestJoinPledge rjp))
				return;
			
			final Clan clan = requestor.getClan();
			
			// we must double check this cause during response time conditions can be changed, i.e. another player could join clan
			if (clan.checkClanJoinCondition(requestor, player, rjp.getPledgeType()))
			{
				player.sendPacket(new JoinPledge(requestor.getClanId()));
				
				player.setPledgeType(rjp.getPledgeType());
				
				switch (rjp.getPledgeType())
				{
					case Clan.SUBUNIT_ACADEMY:
						player.setPowerGrade(9);
						player.setLvlJoinedAcademy(player.getStatus().getLevel());
						break;
					
					case Clan.SUBUNIT_ROYAL1, Clan.SUBUNIT_ROYAL2:
						player.setPowerGrade(7);
						break;
					
					case Clan.SUBUNIT_KNIGHT1, Clan.SUBUNIT_KNIGHT2, Clan.SUBUNIT_KNIGHT3, Clan.SUBUNIT_KNIGHT4:
						player.setPowerGrade(8);
						break;
					
					default:
						player.setPowerGrade(6);
				}
				
				clan.addClanMember(player);
				
				player.sendPacket(SystemMessageId.ENTERED_THE_CLAN);
				
				clan.broadcastToMembersExcept(player, SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_JOINED_CLAN).addCharName(player), new PledgeShowMemberListAdd(player));
				clan.broadcastToMembers(new PledgeShowInfoUpdate(clan));
				
				// this activates the clan tab on the new member
				player.sendPacket(new PledgeShowMemberListAll(clan, 0));
				for (SubPledge sp : player.getClan().getAllSubPledges())
					player.sendPacket(new PledgeShowMemberListAll(clan, sp.getId()));
				
				player.setClanJoinExpiryTime(0);
				player.broadcastUserInfo();
				
				// Refresh surrounding Clan War tags.
				player.forEachKnownType(Player.class, attacker -> clan.getWarList().contains(attacker.getClanId()), Player::broadcastUserInfo);
			}
		}
		player.getRequest().onRequestResponse();
	}
}