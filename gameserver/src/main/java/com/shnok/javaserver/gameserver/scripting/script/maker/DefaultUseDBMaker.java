package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.spawn.SpawnData;

public class DefaultUseDBMaker extends DefaultMaker
{
	public DefaultUseDBMaker(String name)
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
	public void onNpcDBInfo(MultiSpawn ms, SpawnData spawnData, NpcMaker maker)
	{
		if (ms.getTotal() - ms.getSpawned() > 0)
			ms.doSpawn(false);
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		ms.setDBLoaded(false);
		
		if (maker.getNpcsAlive() == 0)
		{
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(maker.getMakerMemo().get("maker_name"));
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
		}
		
		if (maker.getMaximumNpc() - maker.getNpcsAlive() > 0)
		{
			ThreadPool.schedule(() ->
			{
				if (ms.getDecayed() > 0)
					ms.doRespawn();
				else
					ms.doSpawn(false);
			}, ms.calculateRespawnDelay() * 1000);
		}
	}
}