package com.shnok.javaserver.gameserver.scripting.task;

import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.scripting.ScheduledQuest;

public final class ClanLadderRefresh extends ScheduledQuest
{
	public ClanLadderRefresh()
	{
		super(-1, "task");
	}
	
	@Override
	public final void onStart()
	{
		ClanTable.getInstance().refreshClansLadder(true);
	}
	
	@Override
	public final void onEnd()
	{
		// Do nothing.
	}
}