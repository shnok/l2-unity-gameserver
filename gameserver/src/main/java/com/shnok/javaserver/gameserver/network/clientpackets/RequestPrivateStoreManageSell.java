package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;

public final class RequestPrivateStoreManageSell extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Do nothing.
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		player.tryOpenPrivateSellStore(false);
	}
}