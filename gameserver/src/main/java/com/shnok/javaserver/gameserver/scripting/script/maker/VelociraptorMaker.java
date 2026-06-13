package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class VelociraptorMaker extends DefaultMaker
{
	public VelociraptorMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		maker.getMakerMemo().set("i_ai0", 0);
	}
	
	@Override
	public void onMakerScriptEvent(String name, NpcMaker maker, int int1, int int2)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			int totalUnspawned = maker.getMaximumNpc() - maker.getNpcsAlive();
			if (totalUnspawned > 0)
			{
				for (MultiSpawn ms : maker.getSpawns())
				{
					long toSpawnCount = Math.min(totalUnspawned, ms.getTotal() - ms.getSpawned());
					for (long i = 0; i < toSpawnCount; i++)
					{
						ThreadPool.schedule(() ->
						{
							if (ms.getDecayed() > 0)
								ms.doRespawn();
							else
								ms.doSpawn(false);
						}, int1 * 1000L);
						
						ThreadPool.schedule(() -> onTimer("1001", maker), 5000);
						
						totalUnspawned--;
					}
				}
			}
			return;
		}
		else if (name.equalsIgnoreCase("11052"))
		{
			maker.getMakerMemo().set("i_ai0", 1);
		}
		else if (name.equalsIgnoreCase("11053"))
		{
			maker.getMakerMemo().set("i_ai0", 0);
		}
		super.onMakerScriptEvent(name, maker, int1, int2);
	}
	
	@Override
	public void onTimer(String name, NpcMaker maker)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			for (MultiSpawn ms : maker.getSpawns())
				ms.sendScriptEvent(11049, 0, 0);
			
			ThreadPool.schedule(() -> onTimer("78001", maker), 18000 * 1000L);
		}
		else if (name.equalsIgnoreCase("78001"))
		{
			if (maker.getMakerMemo().getInteger("i_ai0") == 0)
			{
				NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_01m1");
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("11052", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				
				maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_02m1");
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("11052", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				
				maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_03m1");
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("11052", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
				
				maker0 = SpawnManager.getInstance().getNpcMaker("rune16_npc2017_01m1");
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("11050", maker0, 0, 0);
			}
		}
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_02m1");
		if (maker0 != null && maker.getNpcsAlive() == 0 && maker.getMakerMemo().getInteger("i_ai0") == 0)
			maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
	}
}