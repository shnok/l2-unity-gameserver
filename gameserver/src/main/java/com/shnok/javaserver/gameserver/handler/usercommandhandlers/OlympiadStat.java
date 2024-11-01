package com.shnok.javaserver.gameserver.handler.usercommandhandlers;

import com.shnok.javaserver.gameserver.handler.IUserCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.olympiad.Olympiad;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadNoble;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class OlympiadStat implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		109
	};
	
	@Override
	public void useUserCommand(int id, Player player)
	{
		if (!player.isNoble())
		{
			player.sendPacket(SystemMessageId.NOBLESSE_ONLY);
			return;
		}
		
		final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.THE_CURRENT_RECORD_FOR_THIS_OLYMPIAD_SESSION_IS_S1_MATCHES_S2_WINS_S3_DEFEATS_YOU_HAVE_EARNED_S4_OLYMPIAD_POINTS);
		
		final OlympiadNoble noble = Olympiad.getInstance().getNoble(player.getObjectId());
		if (noble == null)
		{
			sm.addNumber(0);
			sm.addNumber(0);
			sm.addNumber(0);
			sm.addNumber(0);
		}
		else
		{
			sm.addNumber(noble.getCompDone());
			sm.addNumber(noble.getCompWon());
			sm.addNumber(noble.getCompLost());
			sm.addNumber(noble.getPoints());
		}
		player.sendPacket(sm);
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}