package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.SubPledge;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeReceiveSubPledgeCreated extends L2GameServerPacket
{
	private final SubPledge _subPledge;
	private final Clan _clan;
	
	public PledgeReceiveSubPledgeCreated(SubPledge subPledge, Clan clan)
	{
		_subPledge = subPledge;
		_clan = clan;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x3f);
		
		writeD(0x01);
		writeD(_subPledge.getId());
		writeS(_subPledge.getName());
		writeS(_clan.getSubPledgeLeaderName(_subPledge.getId()));
	}
}