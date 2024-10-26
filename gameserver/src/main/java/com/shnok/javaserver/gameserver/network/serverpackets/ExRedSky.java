package com.shnok.javaserver.gameserver.network.serverpackets;

public class ExRedSky extends L2GameServerPacket
{
	private final int _duration;
	
	public ExRedSky(int duration)
	{
		_duration = duration;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x40);
		writeD(_duration);
	}
}