package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public final class RequestPCCafeCouponUse extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		readS(); // Not used.
	}
	
	@Override
	protected void runImpl()
	{
		// Do nothing.
	}
}