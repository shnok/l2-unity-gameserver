package com.shnok.javaserver.gameserver.network.serverpackets;

public class StartPledgeWar extends L2GameServerPacket
{
	private final String _pledgeName;
	private final String _playerName;
	
	public StartPledgeWar(String pledge, String charName)
	{
		_pledgeName = pledge;
		_playerName = charName;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x65);
		writeS(_playerName);
		writeS(_pledgeName);
	}
}