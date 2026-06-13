package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.manager.HeroManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

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