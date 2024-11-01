package com.shnok.javaserver.gameserver.scripting.task;

import com.shnok.javaserver.gameserver.data.manager.CastleManager;
import com.shnok.javaserver.gameserver.scripting.ScheduledQuest;

public final class CastleTaxRefresh extends ScheduledQuest
{
	public CastleTaxRefresh()
	{
		super(-1, "task");
	}
	
	@Override
	public final void onStart()
	{
		CastleManager.getInstance().updateTaxes();
	}
	
	@Override
	public final void onEnd()
	{
		// Do nothing.
	}
}