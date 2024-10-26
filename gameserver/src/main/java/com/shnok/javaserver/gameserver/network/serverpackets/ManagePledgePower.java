package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.pledge.Clan;

public class ManagePledgePower extends L2GameServerPacket
{
	private final int _rank;
	private final int _action;
	private final int _privs;
	
	public ManagePledgePower(Clan clan, int action, int rank)
	{
		_rank = rank;
		_action = action;
		_privs = clan.getPrivilegesByRank(_rank);
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x30);
		writeD(_rank);
		writeD(_action);
		writeD(_privs);
	}
}