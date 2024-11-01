package com.shnok.javaserver.gameserver.scripting.task;

import com.shnok.javaserver.gameserver.Shutdown;
import com.shnok.javaserver.gameserver.scripting.ScheduledQuest;

public final class ServerRestart extends ScheduledQuest
{
	private static final int PERIOD = 600; // 10 minutes
	
	public ServerRestart()
	{
		super(-1, "task");
	}
	
	@Override
	public final void onStart()
	{
		new Shutdown(PERIOD, true).start();
	}
	
	@Override
	public final void onEnd()
	{
		// Do nothing.
	}
}