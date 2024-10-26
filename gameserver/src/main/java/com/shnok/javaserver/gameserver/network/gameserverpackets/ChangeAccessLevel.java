package com.shnok.javaserver.gameserver.network.gameserverpackets;

public class ChangeAccessLevel extends GameServerBasePacket
{
	public ChangeAccessLevel(String player, int access)
	{
		writeC(0x04);
		writeD(access);
		writeS(player);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}