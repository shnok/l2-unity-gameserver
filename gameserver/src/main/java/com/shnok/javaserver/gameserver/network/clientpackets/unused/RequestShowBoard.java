package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.communitybbs.CommunityBoard;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public final class RequestShowBoard extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		readD(); // Not used for security reason.
	}
	
	@Override
	protected void runImpl()
	{
		CommunityBoard.getInstance().handleCommands(getClient(), Config.BBS_DEFAULT);
	}
}