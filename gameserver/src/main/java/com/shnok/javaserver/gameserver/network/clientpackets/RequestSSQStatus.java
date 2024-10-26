package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.manager.SevenSignsManager;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SSQStatus;

public final class RequestSSQStatus extends L2GameClientPacket
{
	private int _page;
	
	@Override
	protected void readImpl()
	{
		_page = readC();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if ((SevenSignsManager.getInstance().isSealValidationPeriod() || SevenSignsManager.getInstance().isCompResultsPeriod()) && _page == 4)
			return;
		
		player.sendPacket(new SSQStatus(player.getObjectId(), _page));
	}
}