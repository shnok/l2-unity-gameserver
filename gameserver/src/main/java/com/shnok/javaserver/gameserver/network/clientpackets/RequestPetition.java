package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.PetitionManager;
import com.shnok.javaserver.gameserver.data.xml.AdminData;
import com.shnok.javaserver.gameserver.enums.petitions.PetitionType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.PlaySound;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestPetition extends L2GameClientPacket
{
	private String _content;
	private int _type;
	
	@Override
	protected void readImpl()
	{
		_content = readS();
		_type = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (!AdminData.getInstance().isGmOnline(false))
		{
			player.sendPacket(SystemMessageId.NO_GM_PROVIDING_SERVICE_NOW);
			player.sendPacket(new PlaySound("systemmsg_e.702"));
			return;
		}
		
		if (!Config.PETITIONING_ALLOWED)
		{
			player.sendPacket(SystemMessageId.GAME_CLIENT_UNABLE_TO_CONNECT_TO_PETITION_SERVER);
			return;
		}
		
		if (PetitionManager.getInstance().isActivePetition(player))
		{
			player.sendPacket(SystemMessageId.ONLY_ONE_ACTIVE_PETITION_AT_TIME);
			return;
		}
		
		final int serverPetitionCount = PetitionManager.getInstance().getActivePetitionsCount() + 1;
		if (serverPetitionCount > Config.MAX_PETITIONS_PENDING)
		{
			player.sendPacket(SystemMessageId.PETITION_SYSTEM_CURRENT_UNAVAILABLE);
			return;
		}
		
		final int playerPetitionCount = PetitionManager.getInstance().getPetitionsCount(player) + 1;
		if (playerPetitionCount > Config.MAX_PETITIONS_PER_PLAYER)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.WE_HAVE_RECEIVED_S1_PETITIONS_TODAY).addNumber(playerPetitionCount));
			return;
		}
		
		if (_content.length() > 255)
		{
			player.sendPacket(SystemMessageId.PETITION_MAX_CHARS_255);
			return;
		}
		
		final PetitionType type = PetitionType.VALUES[_type];
		final int petitionId = PetitionManager.getInstance().submitPetition(type, player, _content);
		
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PETITION_ACCEPTED_RECENT_NO_S1).addNumber(petitionId));
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SUBMITTED_YOUR_S1_TH_PETITION_S2_LEFT).addNumber(playerPetitionCount).addNumber(Config.MAX_PETITIONS_PER_PLAYER - playerPetitionCount));
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PETITION_ON_WAITING_LIST).addNumber(serverPetitionCount));
	}
}