package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class ExDuelAskStart extends L2GameServerPacket
{
	private final String _requestor;
	private final int _isPartyDuel;
	
	public ExDuelAskStart(String requestor, boolean isPartyDuel)
	{
		_requestor = requestor;
		_isPartyDuel = isPartyDuel ? 1 : 0;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x4b);
		
		writeS(_requestor);
		writeD(_isPartyDuel);
	}
}