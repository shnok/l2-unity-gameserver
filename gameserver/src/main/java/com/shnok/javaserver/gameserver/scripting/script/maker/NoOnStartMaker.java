package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class NoOnStartMaker extends DefaultMaker
{
	public NoOnStartMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		// Do nothing.
	}
}