package com.shnok.javaserver.gameserver.handler.usercommandhandlers;

import com.shnok.javaserver.gameserver.handler.IUserCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.CommandChannel;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class ChannelLeave implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		96
	};
	
	@Override
	public void useUserCommand(int id, Player player)
	{
		final Party party = player.getParty();
		if (party == null || !party.isLeader(player))
			return;
		
		final CommandChannel channel = party.getCommandChannel();
		if (channel == null)
			return;
		
		channel.removeParty(party);
		
		party.broadcastMessage(SystemMessageId.LEFT_COMMAND_CHANNEL);
		channel.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PARTY_LEFT_COMMAND_CHANNEL).addCharName(player));
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}