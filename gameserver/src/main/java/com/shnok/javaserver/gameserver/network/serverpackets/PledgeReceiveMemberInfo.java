package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.pledge.ClanMember;

public class PledgeReceiveMemberInfo extends L2GameServerPacket
{
	private final ClanMember _member;
	
	public PledgeReceiveMemberInfo(ClanMember member)
	{
		_member = member;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x3d);
		
		writeD(_member.getPledgeType());
		writeS(_member.getName());
		writeS(_member.getTitle());
		writeD(_member.getPowerGrade());
		
		// clan or subpledge name
		if (_member.getPledgeType() != 0)
			writeS((_member.getClan().getSubPledge(_member.getPledgeType())).getName());
		else
			writeS(_member.getClan().getName());
		
		writeS(_member.getApprenticeOrSponsorName());
	}
}