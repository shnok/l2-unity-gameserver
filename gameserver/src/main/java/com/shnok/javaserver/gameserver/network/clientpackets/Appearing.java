package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;

public final class Appearing extends L2GameClientPacket
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
		
		if (player.isTeleporting())
			player.onTeleported();
		
		sendPacket(new UserInfo(player));
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}