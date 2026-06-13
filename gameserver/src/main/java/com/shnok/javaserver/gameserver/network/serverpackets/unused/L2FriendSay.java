package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class L2FriendSay extends L2GameServerPacket
{
	private final String _receiver;
	private final String _sender;
	private final String _message;
	private final int _failureReasonMsg;
	
	public L2FriendSay(String sender, String reciever, String message, int failureReasonMsg)
	{
		_receiver = reciever;
		_sender = sender;
		_message = message;
		_failureReasonMsg = failureReasonMsg;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfd);
		writeD(_failureReasonMsg);
		writeS(_receiver);
		writeS(_sender);
		writeS(_message);
	}
}