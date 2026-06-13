package com.shnok.javaserver.gameserver.scripting.task;

import com.shnok.javaserver.gameserver.data.manager.FestivalOfDarknessManager;
import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.scripting.ScheduledQuest;

public final class SevenSignsUpdate extends ScheduledQuest
{
	public SevenSignsUpdate()
	{
		super(-1, "task");
	}
	
	@Override
	public final void onStart()
	{
		if (!SevenSignsManager.getInstance().isSealValidationPeriod())
			FestivalOfDarknessManager.getInstance().saveFestivalData(false);
		
		SevenSignsManager.getInstance().saveSevenSignsData();
		SevenSignsManager.getInstance().saveSevenSignsStatus();
	}
	
	@Override
	public final void onEnd()
	{
		// Do nothing.
	}
}