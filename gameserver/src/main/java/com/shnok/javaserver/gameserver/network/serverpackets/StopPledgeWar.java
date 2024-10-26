package com.shnok.javaserver.gameserver.network.serverpackets;

public class StopPledgeWar extends L2GameServerPacket
{
	private final String _pledgeName;
	private final String _playerName;
	
	public StopPledgeWar(String pledge, String charName)
	{
		_pledgeName = pledge;
		_playerName = charName;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x67);
		writeS(_pledgeName);
		writeS(_playerName);
	}
}