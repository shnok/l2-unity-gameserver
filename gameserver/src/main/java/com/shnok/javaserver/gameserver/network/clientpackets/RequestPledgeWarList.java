package com.shnok.javaserver.gameserver.network.clientpackets;

import java.util.Set;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.serverpackets.PledgeReceiveWarList;

public final class RequestPledgeWarList extends L2GameClientPacket
{
	private int _page;
	private int _tab;
	
	@Override
	protected void readImpl()
	{
		_page = readD();
		_tab = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Clan clan = player.getClan();
		if (clan == null)
			return;
		
		final Set<Integer> list;
		if (_tab == 0)
			list = clan.getWarList();
		else
		{
			list = clan.getAttackerList();
			
			// The page, reaching the biggest section, should send back to 0.
			_page = Math.max(0, (_page > list.size() / 13) ? 0 : _page);
		}
		
		player.sendPacket(new PledgeReceiveWarList(list, _tab, _page));
	}
}