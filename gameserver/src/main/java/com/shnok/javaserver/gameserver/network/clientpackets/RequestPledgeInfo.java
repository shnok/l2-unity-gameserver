package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.serverpackets.PledgeInfo;
import net.sf.l2j.gameserver.network.serverpackets.PledgeStatusChanged;

public final class RequestPledgeInfo extends L2GameClientPacket
{
	private int _clanId;
	
	@Override
	protected void readImpl()
	{
		_clanId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Clan clan = ClanTable.getInstance().getClan(_clanId);
		if (clan == null)
			return;
		
		player.sendPacket(new PledgeInfo(clan));
		player.sendPacket(new PledgeStatusChanged(clan));
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}