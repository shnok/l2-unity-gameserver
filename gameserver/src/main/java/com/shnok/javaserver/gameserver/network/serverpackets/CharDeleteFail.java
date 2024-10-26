package com.shnok.javaserver.gameserver.network.serverpackets;

public class CharDeleteFail extends L2GameServerPacket
{
	public static final CharDeleteFail REASON_DELETION_FAILED = new CharDeleteFail(0x01);
	public static final CharDeleteFail REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER = new CharDeleteFail(0x02);
	public static final CharDeleteFail REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED = new CharDeleteFail(0x03);
	
	private final int _error;
	
	public CharDeleteFail(int errorCode)
	{
		_error = errorCode;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x24);
		writeD(_error);
	}
}