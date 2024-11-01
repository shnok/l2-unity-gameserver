package com.shnok.javaserver.gameserver.network.loginserverpackets;

public class InitLS extends LoginServerBasePacket
{
	private final int _rev;
	private final byte[] _key;
	
	public InitLS(byte[] decrypt)
	{
		super(decrypt);
		
		_rev = readD();
		int size = readD();
		_key = readB(size);
	}
	
	public int getRevision()
	{
		return _rev;
	}
	
	public byte[] getRSAKey()
	{
		return _key;
	}
}