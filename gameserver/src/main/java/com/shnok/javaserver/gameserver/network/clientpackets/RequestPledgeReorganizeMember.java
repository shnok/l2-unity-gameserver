package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.ClanMember;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.PledgeReceiveMemberInfo;

public final class RequestPledgeReorganizeMember extends L2GameClientPacket
{
	private int _isMemberSelected;
	private String _memberName;
	private int _newPledgeType;
	private String _selectedMemberName;
	
	@Override
	protected void readImpl()
	{
		_isMemberSelected = readD();
		_memberName = readS();
		_newPledgeType = readD();
		_selectedMemberName = readS();
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
		
		if (!player.hasClanPrivileges(PrivilegeType.SP_MANAGE_RANKS))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		final ClanMember member1 = clan.getClanMember(_memberName);
		
		if (_isMemberSelected == 0)
		{
			if (member1 != null)
				player.sendPacket(new PledgeReceiveMemberInfo(member1)); // client changes affiliation info even if it fails, so we have to fix it manually
			return;
		}
		
		final ClanMember member2 = clan.getClanMember(_selectedMemberName);
		
		if (member1 == null || member1.getObjectId() == clan.getLeaderId() || member2 == null || member2.getObjectId() == clan.getLeaderId())
			return;
		
		// Do not send sub pledge leaders to other pledges than main
		if (clan.isSubPledgeLeader(member1.getObjectId()))
		{
			player.sendPacket(new PledgeReceiveMemberInfo(member1));
			return;
		}
		
		final int oldPledgeType = member1.getPledgeType();
		if (oldPledgeType == _newPledgeType)
			return;
		
		member1.setPledgeType(_newPledgeType);
		member2.setPledgeType(oldPledgeType);
		
		clan.broadcastClanStatus();
	}
}