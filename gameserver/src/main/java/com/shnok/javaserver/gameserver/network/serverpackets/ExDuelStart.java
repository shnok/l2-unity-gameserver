package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExDuelStart extends L2GameServerPacket
{
	private final int _isPartyDuel;
	
	public ExDuelStart(boolean isPartyDuel)
	{
		_isPartyDuel = isPartyDuel ? 1 : 0;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x4d);
		
		writeD(_isPartyDuel);
	}
}