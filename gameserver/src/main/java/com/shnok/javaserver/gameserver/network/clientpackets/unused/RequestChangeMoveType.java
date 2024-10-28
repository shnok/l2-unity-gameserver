package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public final class RequestChangeMoveType extends L2GameClientPacket
{
	private boolean _typeRun;
	
	@Override
	protected void readImpl()
	{
		_typeRun = readD() == 1;
	}
	
	@Override
	protected void runImpl()
	{
		// Get player.
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		// Player is mounted, do not allow to change movement type.
		if (player.isMounted())
			return;
		
		// Change movement type.
		if (_typeRun)
			player.forceRunStance();
		else
			player.forceWalkStance();
	}
}