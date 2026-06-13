package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class HallKeeperDefaultMaker extends DefaultMaker
{
	public HallKeeperDefaultMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		// Disables onStart
	}
}
