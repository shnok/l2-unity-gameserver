package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.StopRotation;

public final class FinishRotating extends L2GameClientPacket
{
	private int _degree;
	
	@Override
	protected void readImpl()
	{
		_degree = readD();
		readD(); // Not used.
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		player.broadcastPacket(new StopRotation(player.getObjectId(), _degree, 0));
	}
}