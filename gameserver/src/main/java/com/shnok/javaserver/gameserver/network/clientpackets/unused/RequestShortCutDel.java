package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public final class RequestShortCutDel extends L2GameClientPacket
{
	private int _slot;
	private int _page;
	
	@Override
	protected void readImpl()
	{
		int id = readD();
		_slot = id % 12;
		_page = id / 12;
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (_page < 0 || _page > 9)
			return;
		
		player.getShortcutList().deleteShortcut(_slot, _page);
	}
}