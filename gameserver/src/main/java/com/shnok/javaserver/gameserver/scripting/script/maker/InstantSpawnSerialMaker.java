package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class InstantSpawnSerialMaker extends DefaultMaker
{
	public InstantSpawnSerialMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		maker.getMakerMemo().set("i_ai0", maker.getMakerMemo().getInteger("loop_cnt"));
		
		super.onStart(maker);
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		final int i0 = maker.getNpcsAlive();
		final int i_ai0 = maker.getMakerMemo().getInteger("i_ai0");
		
		if (i0 == 0)
		{
			if (i_ai0 > 0)
			{
				maker.getMakerMemo().set("i_ai0", i_ai0 - 1);
				maker.getMaker().onMakerScriptEvent("1001", maker, (int) ms.calculateRespawnDelay(), 0);
			}
			else if (i_ai0 == 0)
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(maker.getMakerMemo().get("maker_name"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1001", maker0, (int) ms.calculateRespawnDelay(), 1);
			}
		}
	}
	
	@Override
	public void onMakerScriptEvent(String name, NpcMaker maker, int int1, int int2)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			if (int2 == 1)
				maker.getMakerMemo().set("i_ai0", maker.getMakerMemo().getInteger("loop_cnt"));
			
			super.onMakerScriptEvent(name, maker, int1, int2);
		}
	}
}