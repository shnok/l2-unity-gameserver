package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class AlarmDeviceMaker1 extends DefaultMaker
{
	public AlarmDeviceMaker1(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		maker.getMakerMemo().set("i_ai0", 1);
		
		ThreadPool.schedule(() -> onTimer("2000", maker), 3000);
	}
	
	@Override
	public void onTimer(String name, NpcMaker maker)
	{
		if (name.equalsIgnoreCase("2000"))
		{
			final MultiSpawn def0 = maker.getSpawns().get(0);
			if (def0 != null)
				def0.sendScriptEvent(10025, 1, 0);
		}
	}
	
	@Override
	public void onMakerScriptEvent(String name, NpcMaker maker, int int1, int int2)
	{
		if (name.equalsIgnoreCase("10008"))
		{
			maker.getMakerMemo().set("i_ai0", maker.getMakerMemo().getInteger("i_ai0") + 1);
			
			final MultiSpawn def0 = maker.getSpawns().get(0);
			if (def0 != null)
				def0.sendScriptEvent(10025, maker.getMakerMemo().getInteger("i_ai0"), 0);
		}
		else if (name.equalsIgnoreCase("1001"))
		{
			maker.getMakerMemo().set("i_ai0", 1);
			
			ThreadPool.schedule(() -> onTimer("2000", maker), 3000);
		}
		super.onMakerScriptEvent(name, maker, int1, int2);
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		if (maker.getNpcsAlive() == 0)
		{
			onMakerScriptEvent("10008", maker, 0, 0);
			
			NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("godard32_2515_19m1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("10008", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker("godard32_2515_20m1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("10008", maker0, 0, 0);
			
			maker0 = SpawnManager.getInstance().getNpcMaker("godard32_2515_21m1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("10008", maker0, 0, 0);
			
			maker.getMakerMemo().set("i_ai0", 0);
		}
	}
}