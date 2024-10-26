package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.spawn.SpawnData;

public class FarmMaker extends DefaultMaker
{
	public FarmMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		if (maker.getSpawns().size() > 1)
		{
			MultiSpawn def0 = maker.getSpawns().get(0);
			def0.doSpawn(false);
			
			for (int i = 1; i < maker.getSpawns().size(); i++)
			{
				maker.getSpawns().get(i).loadDBNpcInfo();
			}
		}
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		// Do nothing.
	}
	
	@Override
	public void onNpcDBInfo(MultiSpawn ms, SpawnData spawnData, NpcMaker maker)
	{
		if (ms.getTotal() - ms.getSpawned() > 0)
			if (ms.getSpawnData() != null && !ms.getSpawnData().checkDead())
				if (ms.getSpawnData().checkAlive(ms.getSpawnLocation(), ms.getTemplate().getBaseHpMax(0), ms.getTemplate().getBaseMpMax(0)))
					ms.doSpawn(true);
	}
	
	@Override
	public void onMakerScriptEvent(String name, NpcMaker maker, int int1, int int2)
	{
		if (name.equalsIgnoreCase("onSiegeStart"))
		{
			for (int i0 = 1; i0 < (maker.getSpawns().size() - 1); i0++)
			{
				MultiSpawn def0 = maker.getSpawns().get(i0);
				long toSpawnCount = def0.getTotal() - def0.getSpawned();
				
				for (long i = 0; i < toSpawnCount; i++)
				{
					if (def0.getDecayed() > 0)
						def0.doRespawn();
					else
						def0.doSpawn(false);
				}
			}
		}
		else if (name.equalsIgnoreCase("onFlagWarFinalEvent"))
		{
			for (int i0 = 1; i0 < maker.getSpawns().size(); i0++)
			{
				MultiSpawn def0 = maker.getSpawns().get(i0);
				long toSpawnCount = def0.getTotal() - def0.getSpawned();
				
				for (long i = 0; i < toSpawnCount; i++)
				{
					if (def0.getDecayed() > 0)
						def0.doRespawn();
					else
						def0.doSpawn(false);
				}
			}
		}
		else if (name.equalsIgnoreCase("onSiegeEnd"))
		{
			for (int i0 = 1; i0 < maker.getSpawns().size(); i0++)
			{
				MultiSpawn def0 = maker.getSpawns().get(i0);
				def0.doDelete();
			}
		}
	}
}
