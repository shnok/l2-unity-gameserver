package com.shnok.javaserver.gameserver.network.gameserverpackets;

import com.shnok.javaserver.gameserver.network.SessionKey;

public class PlayerAuthRequest extends GameServerBasePacket
{
	public PlayerAuthRequest(String account, SessionKey key)
	{
		writeC(0x05);
		writeS(account);
		writeD(key.playOkId1());
		writeD(key.playOkId2());
		writeD(key.loginOkId1());
		writeD(key.loginOkId2());
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}