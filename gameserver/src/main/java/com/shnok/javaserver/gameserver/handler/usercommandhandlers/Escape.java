package com.shnok.javaserver.gameserver.handler.usercommandhandlers;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.handler.IUserCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PlaySound;

public class Escape implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		52
	};
	
	@Override
	public void useUserCommand(int id, Player player)
	{
		if (player.isInOlympiadMode() || player.isInObserverMode() || player.isFestivalParticipant() || player.isInJail() || player.isInsideZone(ZoneId.BOSS))
		{
			player.sendPacket(SystemMessageId.NO_UNSTUCK_PLEASE_SEND_PETITION);
			return;
		}
		
		// Official timer 5 minutes, for GM 1 second
		if (player.isGM())
			player.getAI().tryToCast(player, 2100, 1);
		else
		{
			player.sendPacket(new PlaySound("systemmsg_e.809"));
			player.sendPacket(SystemMessageId.STUCK_TRANSPORT_IN_FIVE_MINUTES);
			
			player.getAI().tryToCast(player, 2099, 1);
		}
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}