package com.shnok.javaserver.gameserver.scripting.task;

import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.scripting.ScheduledQuest;

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