package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.data.manager.HeroManager;
import com.shnok.javaserver.gameserver.model.actor.Player;

public final class RequestWriteHeroWords extends L2GameClientPacket
{
	private String _message;
	
	@Override
	protected void readImpl()
	{
		_message = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null || !player.isHero())
			return;
		
		if (_message == null || _message.length() > 300)
			return;
		
		HeroManager.getInstance().setHeroMessage(player, _message);
	}
}