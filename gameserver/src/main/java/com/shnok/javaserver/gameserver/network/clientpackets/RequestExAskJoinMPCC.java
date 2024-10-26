package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.CommandChannel;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExAskJoinMPCC;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestExAskJoinMPCC extends L2GameClientPacket
{
	private String _targetName;
	
	@Override
	protected void readImpl()
	{
		_targetName = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player requestor = getClient().getPlayer();
		if (requestor == null)
			return;
		
		final Player target = World.getInstance().getPlayer(_targetName);
		if (target == null)
			return;
		
		final Party requestorParty = requestor.getParty();
		if (requestorParty == null)
			return;
		
		final Party targetParty = target.getParty();
		if (targetParty == null || requestorParty.equals(targetParty))
			return;
		
		if (!requestorParty.isLeader(requestor))
		{
			requestor.sendPacket(SystemMessageId.CANNOT_INVITE_TO_COMMAND_CHANNEL);
			return;
		}
		
		final CommandChannel requestorChannel = requestorParty.getCommandChannel();
		if (requestorChannel != null && !requestorChannel.isLeader(requestor))
		{
			requestor.sendPacket(SystemMessageId.CANNOT_INVITE_TO_COMMAND_CHANNEL);
			return;
		}
		
		final CommandChannel targetChannel = targetParty.getCommandChannel();
		if (targetChannel != null)
		{
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ALREADY_MEMBER_OF_COMMAND_CHANNEL).addCharName(target));
			return;
		}
		
		// Check the possibility to setup the CommandChannel.
		if (!CommandChannel.checkAuthority(requestor, false))
			return;
		
		// Get the target's party leader, and do whole actions on him.
		final Player targetLeader = targetParty.getLeader();
		if (!targetLeader.isProcessingRequest())
		{
			requestor.onTransactionRequest(targetLeader);
			targetLeader.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.COMMAND_CHANNEL_CONFIRM_FROM_S1).addCharName(requestor));
			targetLeader.sendPacket(new ExAskJoinMPCC(requestor.getName()));
		}
		else
			requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_BUSY_TRY_LATER).addCharName(targetLeader));
	}
}