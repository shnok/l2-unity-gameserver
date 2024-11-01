package com.shnok.javaserver.gameserver.network.loginserverpackets;

public class AuthResponse extends LoginServerBasePacket
{
	private final int _serverId;
	private final String _serverName;
	
	public AuthResponse(byte[] decrypt)
	{
		super(decrypt);
		
		_serverId = readC();
		_serverName = readS();
	}
	
	public int getServerId()
	{
		return _serverId;
	}
	
	public String getServerName()
	{
		return _serverName;
	}
}