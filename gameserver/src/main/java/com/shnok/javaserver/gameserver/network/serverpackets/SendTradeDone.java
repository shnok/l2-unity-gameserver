package com.shnok.javaserver.gameserver.network.serverpackets;

public class SendTradeDone extends L2GameServerPacket
{
	public static final SendTradeDone FAIL_STATIC_PACKET = new SendTradeDone(0);
	public static final SendTradeDone SUCCESS_STATIC_PACKET = new SendTradeDone(1);
	
	private final int _value;
	
	private SendTradeDone(int value)
	{
		_value = value;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x22);
		writeD(_value);
	}
}