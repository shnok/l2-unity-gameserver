package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;

public final class RequestDismissAlly extends L2GameClientPacket
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
		
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
			return;
		}
		player.getClan().dissolveAlly(player);
	}
}