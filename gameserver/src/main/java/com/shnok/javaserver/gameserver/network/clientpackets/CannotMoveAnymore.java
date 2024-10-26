package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;

public final class CannotMoveAnymore extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		readD(); // _x
		readD(); // _y
		readD(); // _z
		readD(); // _heading
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		player.getMove().stop();
	}
}