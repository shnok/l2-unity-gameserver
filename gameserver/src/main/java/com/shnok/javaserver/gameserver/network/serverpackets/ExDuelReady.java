package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExDuelReady extends L2GameServerPacket
{
	private final int _isPartyDuel;
	
	public ExDuelReady(boolean isPartyDuel)
	{
		_isPartyDuel = isPartyDuel ? 1 : 0;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x4c);
		
		writeD(_isPartyDuel);
	}
}