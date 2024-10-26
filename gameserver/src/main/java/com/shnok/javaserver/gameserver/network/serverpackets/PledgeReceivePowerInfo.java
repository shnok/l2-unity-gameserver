package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.pledge.ClanMember;

public class PledgeReceivePowerInfo extends L2GameServerPacket
{
	private final int _powerGrade;
	private final String _name;
	private final int _privs;
	
	public PledgeReceivePowerInfo(ClanMember member)
	{
		_powerGrade = member.getPowerGrade();
		_name = member.getName();
		_privs = member.getClan().getPrivilegesByRank(_powerGrade);
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x3c);
		
		writeD(_powerGrade);
		writeS(_name);
		writeD(_privs);
	}
}