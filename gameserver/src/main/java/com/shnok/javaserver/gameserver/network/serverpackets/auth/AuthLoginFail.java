package com.shnok.javaserver.gameserver.network.serverpackets.auth;

import com.shnok.javaserver.gameserver.enums.FailReason;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class AuthLoginFail extends L2GameServerPacket
{
	private final FailReason _reason;
	
	public AuthLoginFail(FailReason reason)
	{
		_reason = reason;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x14);
		writeD(_reason.ordinal());
	}
}