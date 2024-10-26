package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.ClanMember;

public class PledgeShowMemberListAll extends L2GameServerPacket
{
	private final Clan _clan;
	private final int _pledgeType;
	private final String _pledgeName;
	
	public PledgeShowMemberListAll(Clan clan, int pledgeType)
	{
		_clan = clan;
		_pledgeType = pledgeType;
		
		if (_pledgeType == 0) // main clan
			_pledgeName = clan.getName();
		else if (_clan.getSubPledge(_pledgeType) != null)
			_pledgeName = _clan.getSubPledge(_pledgeType).getName();
		else
			_pledgeName = "";
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x53);
		
		writeD((_pledgeType == 0) ? 0 : 1);
		writeD(_clan.getClanId());
		writeD(_pledgeType);
		writeS(_pledgeName);
		writeS(_clan.getSubPledgeLeaderName(_pledgeType));
		
		writeD(_clan.getCrestId());
		writeD(_clan.getLevel());
		writeD(_clan.getCastleId());
		writeD(_clan.getClanHallId());
		writeD(_clan.getRank());
		writeD(_clan.getReputationScore());
		writeD(_clan.getDissolvingExpiryTime() > 0 ? 3 : 0);
		writeD(0);
		writeD(_clan.getAllyId());
		writeS(_clan.getAllyName());
		writeD(_clan.getAllyCrestId());
		writeD(_clan.isAtWar() ? 1 : 0);
		writeD(_clan.getSubPledgeMembersCount(_pledgeType));
		
		for (ClanMember m : _clan.getMembers())
		{
			if (m.getPledgeType() != _pledgeType)
				continue;
			
			writeS(m.getName());
			writeD(m.getLevel());
			writeD(m.getClassId());
			writeD(m.getSex().ordinal());
			writeD(m.getRace().ordinal());
			writeD((m.isOnline()) ? m.getObjectId() : 0);
			writeD((m.getSponsor() != 0 || m.getApprentice() != 0) ? 1 : 0);
		}
	}
}