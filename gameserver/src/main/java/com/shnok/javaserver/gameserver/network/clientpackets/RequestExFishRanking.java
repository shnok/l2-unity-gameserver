package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.FishingChampionshipManager;
import com.shnok.javaserver.gameserver.model.actor.Player;

public final class RequestExFishRanking extends L2GameClientPacket
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
		
		if (Config.ALLOW_FISH_CHAMPIONSHIP)
			FishingChampionshipManager.getInstance().showMidResult(player);
	}
}