package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.network.serverpackets.NewCharacterSuccess;

public final class RequestNewCharacter extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Do nothing.
	}
	
	@Override
	protected void runImpl()
	{
		sendPacket(NewCharacterSuccess.STATIC_PACKET);
	}
}