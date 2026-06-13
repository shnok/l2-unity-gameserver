package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class RainbowMaker extends DefaultMaker
{
	public RainbowMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		if (!maker.getSpawns().isEmpty())
		{
			MultiSpawn def0 = maker.getSpawns().get(0);
			def0.doSpawn(false);
		}
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		// Do nothing.
	}
	
	@Override
	public void onMakerScriptEvent(String name, NpcMaker maker, int int1, int int2)
	{
		if (name.equalsIgnoreCase("onSiegeEnd"))
		{
			for (int i0 = 1; i0 < maker.getSpawns().size(); i0++)
			{
				MultiSpawn def0 = maker.getSpawns().get(i0);
				
				def0.doSpawn(false);
			}
		}
	}
}
