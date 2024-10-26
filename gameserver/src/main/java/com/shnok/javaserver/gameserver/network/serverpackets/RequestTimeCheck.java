package com.shnok.javaserver.gameserver.network.serverpackets;

public class RequestTimeCheck extends L2GameServerPacket
{
	private final int _requestId;
	
	public RequestTimeCheck(int requestId)
	{
		_requestId = requestId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xbb);
		writeD(_requestId);
	}
}