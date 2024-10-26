package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class IceFairySirrMaker extends CloseDoorMaker
{
	public IceFairySirrMaker(String name)
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
			
			def0.sendScriptEvent(10001, maker.getMakerMemo().getInteger("i_ai0"), 0);
		}
		else if (name.equalsIgnoreCase("11040"))
		{
			def0.sendScriptEvent(11040, int1, 0);
		}
	}
	
	@Override
	public void onDoorEvent(Door door, NpcMaker maker)
	{
		if (!door.isOpened())
		{
			if (maker.getMakerMemo().getBool("enabled"))
				return;
			
			maker.getMakerMemo().set("enabled", true);
			
			for (MultiSpawn ms : maker.getSpawns())
			{
				long toSpawnCount = ms.getTotal() - ms.getSpawned();
				for (long i = 0; i < toSpawnCount; i++)
					if (maker.getMaximumNpc() - maker.getNpcsAlive() > 0)
						ms.doSpawn(false);
			}
			
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("schuttgart13_npc2314_1m1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("11037", maker0, 0, 0);
		}
		else
		{
			if (!maker.getMakerMemo().getBool("enabled"))
				return;
			
			maker.getMakerMemo().set("enabled", false);
			maker.deleteAll();
		}
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		maker.getMakerMemo().set("i_ai0", 0);
	}
}
