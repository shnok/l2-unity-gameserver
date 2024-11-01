package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.xml.DoorData;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class UndeadBandmasterMaker extends OpenDoorMaker
{
	public UndeadBandmasterMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		ThreadPool.schedule(() -> onTimer("1000", maker), 3000);
		
		super.onStart(maker);
	}
	
	@Override
	public void onDoorEvent(Door door, NpcMaker maker)
	{
		if (door.isOpened())
		{
			if (maker.getMakerMemo().getBool("enabled"))
				return;
			
			maker.getMakerMemo().set("enabled", true);
			
			for (MultiSpawn ms : maker.getSpawns())
			{
				final long toSpawnCount = ms.getTotal() - ms.getSpawned();
				
				for (long i = 0; i < toSpawnCount; i++)
				{
					if (maker.getMaximumNpc() - maker.getNpcsAlive() > 0)
					{
						ms.doSpawn(false);
						
						ThreadPool.schedule(() -> onTimer("1000", maker), 3000);
					}
				}
			}
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
	public void onTimer(String name, NpcMaker maker)
	{
		if (name.equalsIgnoreCase("1000"))
		{
			for (int i = 0; i <= 6; i += 2)
			{
				final MultiSpawn def0 = maker.getSpawns().get(i);
				if (def0 != null)
					def0.sendScriptEvent(10032, i, 0);
			}
		}
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		final int npcsAlive = maker.getNpcsAlive();
		
		// TODO: Zones by name and on/off
		if (npcsAlive == 6)
		{
			// gg::Area_SetOnOff(dmgzone1,0);
		}
		else if (npcsAlive == 4)
		{
			// gg::Area_SetOnOff(dmgzone2,0);
		}
		else if (npcsAlive == 2)
		{
			// gg::Area_SetOnOff(dmgzone3,0);
		}
		else if (npcsAlive == 0)
		{
			final String tDoorName3 = maker.getMakerMemo().getOrDefault("TDoorName3", "TDoorName3_default");
			if (!tDoorName3.equalsIgnoreCase("TDoorName3_default"))
				DoorData.getInstance().getDoor(tDoorName3).openMe();
			
			final String tDoorName4 = maker.getMakerMemo().getOrDefault("TDoorName4", "TDoorName4_default");
			if (!tDoorName4.equalsIgnoreCase("TDoorName4_default"))
				DoorData.getInstance().getDoor(tDoorName4).openMe();
			
			NpcMaker maker1 = SpawnManager.getInstance().getNpcMaker("godard32_2515_14m1");
			if (maker1 != null)
				maker1.getMaker().onMakerScriptEvent("1000", maker1, 0, 1);
			
			maker1 = SpawnManager.getInstance().getNpcMaker("godard32_2515_11m1");
			if (maker1 != null)
				maker1.getMaker().onMakerScriptEvent("1000", maker1, 0, 1);
			
			maker1 = SpawnManager.getInstance().getNpcMaker("godard32_2515_10m1");
			if (maker1 != null)
				maker1.getMaker().onMakerScriptEvent("1000", maker1, 0, 1);
			
			maker1 = SpawnManager.getInstance().getNpcMaker("godard32_2515_12m1");
			if (maker1 != null)
				maker1.getMaker().onMakerScriptEvent("1000", maker1, 0, 1);
			
			maker1 = SpawnManager.getInstance().getNpcMaker("godard32_2515_13m1");
			if (maker1 != null)
				maker1.getMaker().onMakerScriptEvent("1000", maker1, 0, 1);
			
			maker1 = SpawnManager.getInstance().getNpcMaker("godard32_2515_15m1");
			if (maker1 != null)
				maker1.getMaker().onMakerScriptEvent("1000", maker1, 0, 1);
			
			maker1 = SpawnManager.getInstance().getNpcMaker("godard32_2515_18m1");
			if (maker1 != null)
				maker1.getMaker().onMakerScriptEvent("1000", maker1, 0, 1);
		}
	}
}