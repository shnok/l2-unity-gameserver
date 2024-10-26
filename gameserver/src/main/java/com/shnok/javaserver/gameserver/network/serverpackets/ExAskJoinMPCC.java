package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExAskJoinMPCC extends L2GameServerPacket
{
	private final String _requestorName;
	
	public ExAskJoinMPCC(String requestorName)
	{
		_requestorName = requestorName;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x27);
		writeS(_requestorName);
	}
}