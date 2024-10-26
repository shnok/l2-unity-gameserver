package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class SculptureIceFairyMaker extends DefaultMaker
{
	public SculptureIceFairyMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		maker.getMakerMemo().set("i_ai0", 0);
		
		super.onStart(maker);
	}
	
	@Override
	public void onMakerScriptEvent(String name, NpcMaker maker, int int1, int int2)
	{
		final MultiSpawn def0 = maker.getSpawns().get(0);
		
		if (name.equalsIgnoreCase("10005"))
		{
			maker.getMakerMemo().set("i_ai0", maker.getMakerMemo().getInteger("i_ai0") + 1);
			
			if (def0 != null)
				def0.sendScriptEvent(10001, maker.getMakerMemo().getInteger("i_ai0"), 0);
		}
		else if (name.equalsIgnoreCase("10025"))
		{
			maker.getMakerMemo().set("i_ai0", 0);
			
			if (def0 != null)
				def0.sendScriptEvent(10001, maker.getMakerMemo().getInteger("i_ai0"), 0);
		}
	}
}