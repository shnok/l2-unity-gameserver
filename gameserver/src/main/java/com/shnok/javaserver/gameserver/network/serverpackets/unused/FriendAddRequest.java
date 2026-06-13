package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class FriendAddRequest extends L2GameServerPacket
{
	private final String _playerName;
	
	public FriendAddRequest(String playerName)
	{
		_playerName = playerName;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7d);
		writeS(_playerName);
	}
}