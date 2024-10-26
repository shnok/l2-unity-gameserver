package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.enums.actors.OperateType;
import net.sf.l2j.gameserver.model.actor.Player;

public final class RequestPrivateStoreQuitSell extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Do nothing.
	}
	
	@Override
	protected void runImpl()
	{
		Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		player.setOperateType(OperateType.NONE);
		player.broadcastUserInfo();
	}
}