package com.shnok.javaserver.gameserver.scripting.task;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.scripting.ScheduledQuest;

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