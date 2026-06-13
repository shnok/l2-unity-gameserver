package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.spawn.SpawnData;
import com.shnok.javaserver.gameserver.scripting.SpawnMaker;

public class DefaultMaker extends SpawnMaker
{
	public DefaultMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		if (maker.isOnStart() || !checkHasSpawnCondition(maker))
		{
			for (MultiSpawn ms : maker.getSpawns())
			{
				if (ms.getTotal() != ms.getSpawned())
				{
					if (ms.getSpawnData() != null)
						ms.loadDBNpcInfo();
					else
					{
						long toSpawnCount = ms.getTotal() - ms.getSpawned();
						for (long i = 0; i < toSpawnCount; i++)
							if (maker.getMaximumNpc() - maker.getNpcsAlive() > 0)
								ms.doSpawn(false);
					}
				}
			}
		}
	}
	
	@Override
	public void onNpcDBInfo(MultiSpawn ms, SpawnData spawnData, NpcMaker maker)
	{
		if (ms.getTotal() - ms.getSpawned() > 0)
			ms.doSpawn(false);
	}
	
	@Override
	public void onNpcCreated(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		if (checkHasSpawnCondition(maker))
			npc.deleteMe();
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		if (checkHasSpawnCondition(maker))
			return;
		
		if (npc.getSpawn().getRespawnDelay() != 0)
			npc.scheduleRespawn(npc.getSpawn().calculateRespawnDelay() * 1000);
	}
	
	@Override
	public void onMakerScriptEvent(String name, NpcMaker maker, int int1, int int2)
	{
		if (name.equalsIgnoreCase("1000"))
		{
			maker.deleteAll();
		}
		else if (name.equalsIgnoreCase("1001"))
		{
			if (checkHasSpawnCondition(maker))
				return;
			
			int totalUnspawned = maker.getMaximumNpc() - maker.getNpcsAlive();
			if (totalUnspawned > 0)
			{
				for (MultiSpawn ms : maker.getSpawns())
				{
					long toSpawnCount = Math.min(totalUnspawned, ms.getTotal() - ms.getSpawned());
					
					for (Npc npc : ms.getNpcs())
					{
						if (npc.isDecayed())
						{
							npc.scheduleRespawn(int1 * 1000L);
							toSpawnCount--;
							totalUnspawned--;
						}
					}
					
					for (long i = 0; i < toSpawnCount; i++)
					{
						ThreadPool.schedule(() ->
						{
							if (ms.getDecayed() > 0)
								ms.doRespawn();
							else
								ms.doSpawn(false);
						}, int1 * 1000L);
						
						totalUnspawned--;
					}
				}
			}
		}
	}
}