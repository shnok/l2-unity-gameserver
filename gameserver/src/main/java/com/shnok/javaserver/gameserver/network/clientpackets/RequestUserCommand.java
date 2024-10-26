package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.handler.IUserCommandHandler;
import net.sf.l2j.gameserver.handler.UserCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

public class RequestUserCommand extends L2GameClientPacket
{
	private int _commandId;
	
	@Override
	protected void readImpl()
	{
		_commandId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final IUserCommandHandler handler = UserCommandHandler.getInstance().getHandler(_commandId);
		if (handler != null)
			handler.useUserCommand(_commandId, player);
	}
}