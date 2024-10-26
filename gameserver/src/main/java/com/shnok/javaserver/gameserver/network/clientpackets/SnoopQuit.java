package com.shnok.javaserver.gameserver.network.clientpackets;

public final class SnoopQuit extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		readD(); // Not used. Snoop system is broken on IL.
	}
	
	@Override
	protected void runImpl()
	{
		// Do nothing.
	}
}