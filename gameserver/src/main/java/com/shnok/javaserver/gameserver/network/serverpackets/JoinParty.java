package com.shnok.javaserver.gameserver.network.serverpackets;

/**
 * format cd
 */
public final class JoinParty extends L2GameServerPacket
{
	private final int _response;
	
	public JoinParty(int response)
	{
		_response = response;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x3a);
		writeD(_response);
	}
}