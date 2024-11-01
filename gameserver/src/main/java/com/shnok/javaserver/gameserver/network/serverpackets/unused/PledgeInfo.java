package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeInfo extends L2GameServerPacket
{
	private final Clan _clan;
	
	public PledgeInfo(Clan clan)
	{
		_clan = clan;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x83);
		writeD(_clan.getClanId());
		writeS(_clan.getName());
		writeS(_clan.getAllyName());
	}
}