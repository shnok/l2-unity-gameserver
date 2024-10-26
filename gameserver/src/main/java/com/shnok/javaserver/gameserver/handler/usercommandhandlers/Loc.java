package com.shnok.javaserver.gameserver.handler.usercommandhandlers;

import net.sf.l2j.gameserver.data.xml.RestartPointData;
import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.restart.RestartPoint;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class Loc implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		0
	};
	
	@Override
	public void useUserCommand(int id, Player player)
	{
		final RestartPoint rp = RestartPointData.getInstance().getCalculatedRestartPoint(player);
		if (rp != null)
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(rp.getLocName());
			if (sm != null)
				player.sendPacket(sm.addNumber(player.getX()).addNumber(player.getY()).addNumber(player.getZ()));
		}
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}