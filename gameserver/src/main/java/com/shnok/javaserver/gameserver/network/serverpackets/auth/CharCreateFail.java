package com.shnok.javaserver.gameserver.network.serverpackets.auth;

import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class CharCreateFail extends L2GameServerPacket
{
	public static final CharCreateFail REASON_CREATION_FAILED = new CharCreateFail(0x00);
	public static final CharCreateFail REASON_TOO_MANY_CHARACTERS = new CharCreateFail(0x01);
	public static final CharCreateFail REASON_NAME_ALREADY_EXISTS = new CharCreateFail(0x02);
	public static final CharCreateFail REASON_16_ENG_CHARS = new CharCreateFail(0x03);
	public static final CharCreateFail REASON_INCORRECT_NAME = new CharCreateFail(0x04);
	public static final CharCreateFail REASON_CREATE_NOT_ALLOWED = new CharCreateFail(0x05);
	public static final CharCreateFail REASON_CHOOSE_ANOTHER_SVR = new CharCreateFail(0x06);
	
	private final int _error;
	
	public CharCreateFail(int errorCode)
	{
		_error = errorCode;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x1a);
		writeD(_error);
	}
}