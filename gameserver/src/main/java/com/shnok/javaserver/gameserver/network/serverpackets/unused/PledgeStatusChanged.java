package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * format ddddd
 */
public class PledgeStatusChanged extends L2GameServerPacket
{
	private final Clan _clan;
	
	public PledgeStatusChanged(Clan clan)
	{
		_clan = clan;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xcd);
		writeD(_clan.getLeaderId());
		writeD(_clan.getClanId());
		writeD(_clan.getCrestId());
		writeD(_clan.getAllyId());
		writeD(_clan.getAllyCrestId());
		writeD(0);
		writeD(0);
	}
}