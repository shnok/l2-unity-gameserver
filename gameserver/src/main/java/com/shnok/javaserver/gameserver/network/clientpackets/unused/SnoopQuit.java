package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

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