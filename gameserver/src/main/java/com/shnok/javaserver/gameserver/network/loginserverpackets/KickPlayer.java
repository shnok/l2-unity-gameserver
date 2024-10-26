package com.shnok.javaserver.gameserver.network.loginserverpackets;

public class KickPlayer extends LoginServerBasePacket
{
	private final String _account;
	
	public KickPlayer(byte[] decrypt)
	{
		super(decrypt);
		
		_account = readS();
	}
	
	public String getAccount()
	{
		return _account;
	}
}