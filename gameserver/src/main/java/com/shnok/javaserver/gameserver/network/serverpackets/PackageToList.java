package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Map;

public class PackageToList extends L2GameServerPacket
{
	private final Map<Integer, String> _players;
	
	public PackageToList(Map<Integer, String> players)
	{
		_players = players;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xC2);
		writeD(_players.size());
		for (Map.Entry<Integer, String> entry : _players.entrySet())
		{
			writeD(entry.getKey());
			writeS(entry.getValue());
		}
	}
}