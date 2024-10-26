package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.enums.PrivilegeType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestPledgeSetAcademyMaster extends L2GameClientPacket
{
	private String _currPlayerName;
	private int _set; // 1 set, 0 delete
	private String _targetPlayerName;
	
	@Override
	protected void readImpl()
	{
		_set = readD();
		_currPlayerName = readS();
		_targetPlayerName = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Clan clan = player.getClan();
		if (clan == null)
			return;
		
		if (!player.hasClanPrivileges(PrivilegeType.SP_MASTER_RIGHTS))
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_DISMISS_AN_APPRENTICE);
			return;
		}
		
		final ClanMember currentMember = clan.getClanMember(_currPlayerName);
		if (currentMember == null)
			return;
		
		final ClanMember targetMember = clan.getClanMember(_targetPlayerName);
		if (targetMember == null)
			return;
		
		ClanMember apprenticeMember;
		ClanMember sponsorMember;
		
		if (currentMember.getPledgeType() == Clan.SUBUNIT_ACADEMY)
		{
			apprenticeMember = currentMember;
			sponsorMember = targetMember;
		}
		else
		{
			apprenticeMember = targetMember;
			sponsorMember = currentMember;
		}
		
		final Player apprentice = apprenticeMember.getPlayerInstance();
		final Player sponsor = sponsorMember.getPlayerInstance();
		
		SystemMessage sm = null;
		if (_set == 0)
		{
			// test: do we get the current sponsor & apprentice from this packet or no?
			if (apprentice != null)
				apprentice.setSponsor(0);
			else
				// offline
				apprenticeMember.setApprenticeAndSponsor(0, 0);
			
			if (sponsor != null)
				sponsor.setApprentice(0);
			else
				// offline
				sponsorMember.setApprenticeAndSponsor(0, 0);
			
			apprenticeMember.saveApprenticeAndSponsor(0, 0);
			sponsorMember.saveApprenticeAndSponsor(0, 0);
			
			sm = SystemMessage.getSystemMessage(SystemMessageId.S2_CLAN_MEMBER_S1_APPRENTICE_HAS_BEEN_REMOVED);
		}
		else
		{
			if (apprenticeMember.getSponsor() != 0 || sponsorMember.getApprentice() != 0 || apprenticeMember.getApprentice() != 0 || sponsorMember.getSponsor() != 0)
			{
				player.sendMessage("Remove previous connections first.");
				return;
			}
			
			if (apprentice != null)
				apprentice.setSponsor(sponsorMember.getObjectId());
			else
				// offline
				apprenticeMember.setApprenticeAndSponsor(0, sponsorMember.getObjectId());
			
			if (sponsor != null)
				sponsor.setApprentice(apprenticeMember.getObjectId());
			else
				// offline
				sponsorMember.setApprenticeAndSponsor(apprenticeMember.getObjectId(), 0);
			
			// saving to database even if online, since both must match
			apprenticeMember.saveApprenticeAndSponsor(0, sponsorMember.getObjectId());
			sponsorMember.saveApprenticeAndSponsor(apprenticeMember.getObjectId(), 0);
			
			sm = SystemMessage.getSystemMessage(SystemMessageId.S2_HAS_BEEN_DESIGNATED_AS_APPRENTICE_OF_CLAN_MEMBER_S1);
		}
		sm.addString(sponsorMember.getName());
		sm.addString(apprenticeMember.getName());
		
		if (sponsor != player && sponsor != apprentice)
			player.sendPacket(sm);
		
		if (sponsor != null)
			sponsor.sendPacket(sm);
		
		if (apprentice != null)
			apprentice.sendPacket(sm);
		
		clan.broadcastToMembers(new PledgeShowMemberListUpdate(sponsorMember), new PledgeShowMemberListUpdate(apprenticeMember));
	}
}