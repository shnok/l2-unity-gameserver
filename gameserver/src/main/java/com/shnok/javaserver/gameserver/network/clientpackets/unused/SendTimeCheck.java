package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public class SendTimeCheck extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private int _requestId;
	@SuppressWarnings("unused")
	private int _responseId;
	
	@Override
	protected void readImpl()
	{
		_requestId = readD();
		_responseId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		// Do nothing.
	}
}