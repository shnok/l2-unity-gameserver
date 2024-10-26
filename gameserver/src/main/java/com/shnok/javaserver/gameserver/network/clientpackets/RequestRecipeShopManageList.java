package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;

public final class RequestRecipeShopManageList extends L2GameClientPacket
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
		
		player.tryOpenWorkshop(true);
	}
}