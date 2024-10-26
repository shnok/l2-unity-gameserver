package com.shnok.javaserver.gameserver.network.clientpackets;

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