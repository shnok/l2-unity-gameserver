package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.model.actor.Player;

public final class RequestReplySurrenderPledgeWar extends L2GameClientPacket
{
	private int _answer;
	
	@Override
	protected void readImpl()
	{
		_answer = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player requestor = player.getActiveRequester();
		if (requestor == null)
			return;
		
		if (_answer == 1)
		{
			requestor.applyDeathPenalty(false, false);
			ClanTable.getInstance().deleteClansWars(requestor.getClanId(), player.getClanId());
		}
		
		player.onTransactionRequest(requestor);
	}
}