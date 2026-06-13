package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.manager.DuelManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.CommandChannel;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestDuelAnswerStart extends L2GameClientPacket
{
	private boolean _isPartyDuel;
	private boolean _duelAccepted;
	
	@Override
	protected void readImpl()
	{
		_isPartyDuel = readD() == 1;
		readD(); // Not used.
		_duelAccepted = readD() == 1;
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
		
		if (_duelAccepted)
		{
			// Check if duel is possible.
			if (!requestor.canDuel())
			{
				player.sendPacket(requestor.getNoDuelReason());
				return;
			}
			
			if (!player.canDuel())
			{
				player.sendPacket(SystemMessageId.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
				return;
			}
			
			// Players musn't be too far.
			if (!requestor.isIn3DRadius(player, 2000))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_RECEIVE_A_DUEL_CHALLENGE_BECAUSE_S1_IS_TOO_FAR_AWAY).addCharName(requestor));
				return;
			}
			
			if (_isPartyDuel)
			{
				// Player must be a party leader, the target can't be of the same party.
				final Party requestorParty = requestor.getParty();
				if (requestorParty == null || !requestorParty.isLeader(requestor) || requestorParty.containsPlayer(player))
				{
					player.sendPacket(SystemMessageId.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
					return;
				}
				
				// Target must be in a party.
				final Party playerParty = player.getParty();
				if (playerParty == null)
				{
					player.sendPacket(SystemMessageId.SINCE_THE_PERSON_YOU_CHALLENGED_IS_NOT_CURRENTLY_IN_A_PARTY_THEY_CANNOT_DUEL_AGAINST_YOUR_PARTY);
					return;
				}
				
				// Check if every player is ready for a duel.
				for (Player member : requestorParty.getMembers())
				{
					if (member != requestor && !member.canDuel())
					{
						player.sendPacket(SystemMessageId.YOU_ARE_UNABLE_TO_REQUEST_A_DUEL_AT_THIS_TIME);
						return;
					}
				}
				
				for (Player member : playerParty.getMembers())
				{
					if (member != player && !member.canDuel())
					{
						player.sendPacket(SystemMessageId.THE_OPPOSING_PARTY_IS_CURRENTLY_UNABLE_TO_ACCEPT_A_CHALLENGE_TO_A_DUEL);
						return;
					}
				}
				
				// Drop command channels, for both requestor && player parties.
				final CommandChannel requestorChannel = requestorParty.getCommandChannel();
				if (requestorChannel != null)
					requestorChannel.removeParty(requestorParty);
				
				final CommandChannel playerChannel = playerParty.getCommandChannel();
				if (playerChannel != null)
					playerChannel.removeParty(playerParty);
				
				// Partymatching
				for (Player member : requestorParty.getMembers())
					member.removeMeFromPartyMatch();
				
				for (Player member : playerParty.getMembers())
					member.removeMeFromPartyMatch();
				
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_ACCEPTED_S1_CHALLENGE_TO_A_PARTY_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS).addCharName(requestor));
				requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_DUEL_AGAINST_THEIR_PARTY_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS).addCharName(player));
			}
			else
			{
				// Partymatching
				player.removeMeFromPartyMatch();
				requestor.removeMeFromPartyMatch();
				
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_ACCEPTED_S1_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS).addCharName(requestor));
				requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ACCEPTED_YOUR_CHALLENGE_TO_A_DUEL_THE_DUEL_WILL_BEGIN_IN_A_FEW_MOMENTS).addCharName(player));
			}
			
			DuelManager.getInstance().addDuel(requestor, player, _isPartyDuel);
		}
		else
		{
			if (_isPartyDuel)
				requestor.sendPacket(SystemMessageId.THE_OPPOSING_PARTY_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL);
			else
				requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_DECLINED_YOUR_CHALLENGE_TO_A_DUEL).addCharName(player));
		}
	}
}