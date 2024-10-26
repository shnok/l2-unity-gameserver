package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.CommandChannel;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestExAcceptJoinMPCC extends L2GameClientPacket
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
		
		player.setActiveRequester(null);
		requestor.onTransactionResponse();
		
		final Party requestorParty = requestor.getParty();
		if (requestorParty == null)
			return;
		
		final Party targetParty = player.getParty();
		if (targetParty == null)
			return;
		
		if (_response == 1)
		{
			CommandChannel channel = requestorParty.getCommandChannel();
			if (channel == null)
			{
				// Check the possibility to setup the CommandChannel.
				if (!CommandChannel.checkAuthority(requestor, true))
					return;
				
				new CommandChannel(requestorParty, targetParty);
			}
			else
				channel.addParty(targetParty);
		}
		else
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DECLINED_CHANNEL_INVITATION).addCharName(player));
	}
}