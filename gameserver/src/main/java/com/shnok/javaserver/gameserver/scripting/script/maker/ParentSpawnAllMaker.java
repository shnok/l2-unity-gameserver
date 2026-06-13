package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.spawn.SpawnData;

public class ParentSpawnAllMaker extends DefaultUseDBMaker
{
	private static final String[] A_MAKERS =
	{
		"maker_nameA1",
		"maker_nameA2",
		"maker_nameA3",
		"maker_nameA4"
	};
	
	private static final String[] B_MAKERS =
	{
		"maker_nameB1",
		"maker_nameB2",
		"maker_nameB3",
		"maker_nameB4"
	};
	
	private static final String DEFAULT_NAME = "parent_spawn_all_default";
	
	public ParentSpawnAllMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		for (MultiSpawn ms : maker.getSpawns())
		{
			if (ms.getTotal() != ms.getSpawned())
				ms.loadDBNpcInfo();
		}
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		final String unique_npc = maker.getMakerMemo().get("unique_npc");
		
		if (npc.getTemplate().getAlias().equalsIgnoreCase(unique_npc))
		{
			if (maker.getMaximumNpc() - maker.getNpcsAlive() > 0)
				npc.scheduleRespawn(ms.calculateRespawnDelay() * 1000);
			
			for (String makerName : A_MAKERS)
			{
				final String name = maker.getMakerMemo().getOrDefault(makerName, DEFAULT_NAME);
				if (!name.equalsIgnoreCase(DEFAULT_NAME))
				{
					final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(name);
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
				}
			}
		}
	}
	
	@Override
	public void onNpcCreated(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		final String unique_npc = maker.getMakerMemo().get("unique_npc");
		
		if (npc.getTemplate().getAlias().equalsIgnoreCase(unique_npc))
		{
			for (String makerName : A_MAKERS)
			{
				final String name = maker.getMakerMemo().getOrDefault(makerName, DEFAULT_NAME);
				if (!name.equalsIgnoreCase(DEFAULT_NAME))
				{
					final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(name);
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
			}
			
			for (String makerName : B_MAKERS)
			{
				final String name = maker.getMakerMemo().getOrDefault(makerName, DEFAULT_NAME);
				if (!name.equalsIgnoreCase(DEFAULT_NAME))
				{
					final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(name);
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
			}
		}
	}
	
	@Override
	public void onNpcDBInfo(MultiSpawn ms, SpawnData spawnData, NpcMaker maker)
	{
		if (ms.getSpawnData() != null && !ms.getSpawnData().checkDead() && maker.getNpcsAlive() + ms.getTotal() <= maker.getMaximumNpc())
		{
			ms.doSpawn(false);
			
			for (String makerName : A_MAKERS)
			{
				final String name = maker.getMakerMemo().getOrDefault(makerName, DEFAULT_NAME);
				if (!name.equalsIgnoreCase(DEFAULT_NAME))
				{
					final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(name);
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
			}
			
			for (String makerName : B_MAKERS)
			{
				final String name = maker.getMakerMemo().getOrDefault(makerName, DEFAULT_NAME);
				if (!name.equalsIgnoreCase(DEFAULT_NAME))
				{
					final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(name);
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
			}
		}
	}
}