package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.CommandChannel;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExDuelAskStart;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestDuelStart extends L2GameClientPacket
{
	private String _targetName;
	private boolean _isPartyDuel;
	
	@Override
	protected void readImpl()
	{
		_targetName = readS();
		_isPartyDuel = readD() == 1;
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player target = World.getInstance().getPlayer(_targetName);
		if (target == null || player == target)
		{
			player.sendPacket(SystemMessageId.THERE_IS_NO_OPPONENT_TO_RECEIVE_YOUR_CHALLENGE_FOR_A_DUEL);
			return;
		}
		
		// Check if duel is possible.
		if (!player.canDuel())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
			return;
		}
		
		if (!target.canDuel())
		{
			player.sendPacket(target.getNoDuelReason());
			return;
		}
		
		// Players musn't be too far.
		if (!player.isIn3DRadius(target, 2000))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_RECEIVE_A_DUEL_CHALLENGE_BECAUSE_S1_IS_TOO_FAR_AWAY).addCharName(target));
			return;
		}
		
		// Duel is a party duel.
		if (_isPartyDuel)
		{
			// Player must be a party leader, the target can't be of the same party.
			final Party playerParty = player.getParty();
			if (playerParty == null || !playerParty.isLeader(player) || playerParty.containsPlayer(target))
			{
				player.sendPacket(SystemMessageId.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
				return;
			}
			
			// Target must be in a party.
			final Party targetParty = target.getParty();
			if (targetParty == null)
			{
				player.sendPacket(SystemMessageId.SINCE_THE_PERSON_YOU_CHALLENGED_IS_NOT_CURRENTLY_IN_A_PARTY_THEY_CANNOT_DUEL_AGAINST_YOUR_PARTY);
				return;
			}
			
			// Check if every player is ready for a duel.
			for (Player member : playerParty.getMembers())
			{
				if (member != player && !member.canDuel())
				{
					player.sendPacket(SystemMessageId.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
					return;
				}
			}
			
			for (Player member : targetParty.getMembers())
			{
				if (member != target && !member.canDuel())
				{
					player.sendPacket(SystemMessageId.THE_OPPOSING_PARTY_IS_CURRENTLY_UNABLE_TO_ACCEPT_A_CHALLENGE_TO_A_DUEL);
					return;
				}
			}
			
			final Player partyLeader = targetParty.getLeader();
			
			// Send request to targetChar's party leader.
			if (!partyLeader.isProcessingRequest())
			{
				// Drop command channels, for both requestor && player parties.
				final CommandChannel playerChannel = playerParty.getCommandChannel();
				if (playerChannel != null)
					playerChannel.removeParty(playerParty);
				
				final CommandChannel targetChannel = targetParty.getCommandChannel();
				if (targetChannel != null)
					targetChannel.removeParty(targetParty);
				
				// Partymatching
				for (Player member : playerParty.getMembers())
					member.removeMeFromPartyMatch();
				
				for (Player member : targetParty.getMembers())
					member.removeMeFromPartyMatch();
				
				player.onTransactionRequest(partyLeader);
				partyLeader.sendPacket(new ExDuelAskStart(player.getName(), _isPartyDuel));
				
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PARTY_HAS_BEEN_CHALLENGED_TO_A_DUEL).addCharName(partyLeader));
				target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PARTY_HAS_CHALLENGED_YOUR_PARTY_TO_A_DUEL).addCharName(player));
			}
			else
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_BUSY_TRY_LATER).addCharName(partyLeader));
		}
		// 1vs1 duel.
		else
		{
			if (!target.isProcessingRequest())
			{
				// Partymatching
				player.removeMeFromPartyMatch();
				target.removeMeFromPartyMatch();
				
				player.onTransactionRequest(target);
				target.sendPacket(new ExDuelAskStart(player.getName(), _isPartyDuel));
				
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_CHALLENGED_TO_A_DUEL).addCharName(target));
				target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_CHALLENGED_YOU_TO_A_DUEL).addCharName(player));
			}
			else
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_BUSY_TRY_LATER).addCharName(target));
		}
	}
}