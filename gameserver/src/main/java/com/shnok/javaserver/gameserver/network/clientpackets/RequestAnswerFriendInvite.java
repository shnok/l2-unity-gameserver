package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.manager.RelationManager;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.FriendAddRequestResult;
import net.sf.l2j.gameserver.network.serverpackets.L2Friend;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestAnswerFriendInvite extends L2GameClientPacket
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
		
		final Player requestor = player.getActiveRequester();
		if (requestor == null)
			return;
		
		if (_response == 1)
		{
			// Player added to your friendlist
			RelationManager.getInstance().addToFriendList(requestor, player.getObjectId());
			requestor.sendPacket(FriendAddRequestResult.STATIC_ACCEPT);
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ADDED_TO_FRIENDS).addCharName(player));
			requestor.sendPacket(new L2Friend(player, 1));
			
			// has joined as friend.
			RelationManager.getInstance().addToFriendList(player, requestor.getObjectId());
			player.sendPacket(FriendAddRequestResult.STATIC_ACCEPT);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_JOINED_AS_FRIEND).addCharName(requestor));
			player.sendPacket(new L2Friend(requestor, 1));
			
		}
		else
			requestor.sendPacket(FriendAddRequestResult.STATIC_FAIL);
		
		player.setActiveRequester(null);
		requestor.onTransactionResponse();
	}
}