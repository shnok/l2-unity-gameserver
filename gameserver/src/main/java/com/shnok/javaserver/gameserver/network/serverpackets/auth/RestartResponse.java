package com.shnok.javaserver.gameserver.network.serverpackets.auth;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class RestartResponse extends L2GameServerPacket
{
	private static final RestartResponse STATIC_PACKET_TRUE = new RestartResponse(true);
	private static final RestartResponse STATIC_PACKET_FALSE = new RestartResponse(false);
	
	public static final RestartResponse valueOf(boolean result)
	{
		return result ? STATIC_PACKET_TRUE : STATIC_PACKET_FALSE;
	}
	
	private final boolean _result;
	
	public RestartResponse(boolean result)
	{
		_result = result;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x5f);
		writeD(_result ? 1 : 0);
	}
}