package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public final class RequestAnswerJoinAlly extends L2GameClientPacket
{
	private int _response;
	
	@Override
	protected void readImpl()
	{
		_response = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player requestor = player.getRequest().getPartner();
		if (requestor == null)
			return;
		
		if (_response == 0)
		{
			player.sendPacket(SystemMessageId.YOU_DID_NOT_RESPOND_TO_ALLY_INVITATION);
			requestor.sendPacket(SystemMessageId.NO_RESPONSE_TO_ALLY_INVITATION);
		}
		else
		{
			if (!(requestor.getRequest().getRequestPacket() instanceof RequestJoinAlly))
				return;
			
			if (!Clan.checkAllyJoinCondition(requestor, player))
				return;
			
			player.getClan().setAllyId(requestor.getClan().getAllyId());
			player.getClan().setAllyName(requestor.getClan().getAllyName());
			player.getClan().setAllyPenaltyExpiryTime(0, 0);
			player.getClan().changeAllyCrest(requestor.getClan().getAllyCrestId(), true);
			player.getClan().updateClanInDB();
			
			player.sendPacket(SystemMessageId.YOU_ACCEPTED_ALLIANCE);
		}
		player.getRequest().onRequestResponse();
	}
}