package com.shnok.javaserver.gameserver.network.gameserverpackets;

public class AuthRequest extends GameServerBasePacket
{
	public AuthRequest(int id, boolean acceptAlternate, byte[] hexid, String host, int port, boolean reserveHost, int maxplayer)
	{
		writeC(0x01);
		writeC(id);
		writeC(acceptAlternate ? 0x01 : 0x00);
		writeC(reserveHost ? 0x01 : 0x00);
		writeS(host);
		writeH(port);
		writeD(maxplayer);
		writeD(hexid.length);
		writeB(hexid);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}