package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.xml.DoorData;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class WarriorPassiveWeaknessMaker extends OpenDoorMaker
{
	public WarriorPassiveWeaknessMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onTimer(String name, NpcMaker maker)
	{
		if (name.equalsIgnoreCase("1000"))
		{
			
			String tDoorName1 = maker.getMakerMemo().getOrDefault("TDoorName1", "grave_pathway_1");
			String tDoorName2 = maker.getMakerMemo().getOrDefault("TDoorName2", "grave_pathway_2");
			
			String tDoorName3 = maker.getMakerMemo().get("TDoorName3");
			String tDoorName4 = maker.getMakerMemo().get("TDoorName4");
			
			String wallDoorNameB1 = maker.getMakerMemo().get("WallDoorNameB1");
			String wallDoorNameB2 = maker.getMakerMemo().get("WallDoorNameB2");
			String wallDoorNameB3 = maker.getMakerMemo().get("WallDoorNameB3");
			String wallDoorNameB4 = maker.getMakerMemo().get("WallDoorNameB4");
			String wallDoorNameB5 = maker.getMakerMemo().get("WallDoorNameB5");
			String wallDoorNameB6 = maker.getMakerMemo().get("WallDoorNameB6");
			String wallDoorNameB7 = maker.getMakerMemo().get("WallDoorNameB7");
			String wallDoorNameB8 = maker.getMakerMemo().get("WallDoorNameB8");
			String wallDoorNameB9 = maker.getMakerMemo().get("WallDoorNameB9");
			String wallDoorNameB10 = maker.getMakerMemo().get("WallDoorNameB10");
			
			DoorData.getInstance().getDoor(tDoorName1).closeMe();
			DoorData.getInstance().getDoor(tDoorName2).closeMe();
			
			if (tDoorName3 != null)
				DoorData.getInstance().getDoor(tDoorName3).closeMe();
			
			if (tDoorName4 != null)
				DoorData.getInstance().getDoor(tDoorName4).closeMe();
			
			if (wallDoorNameB1 != null)
				DoorData.getInstance().getDoor(wallDoorNameB1).openMe();
			
			if (wallDoorNameB2 != null)
				DoorData.getInstance().getDoor(wallDoorNameB2).openMe();
			
			if (wallDoorNameB3 != null)
				DoorData.getInstance().getDoor(wallDoorNameB3).openMe();
			
			if (wallDoorNameB4 != null)
				DoorData.getInstance().getDoor(wallDoorNameB4).openMe();
			
			if (wallDoorNameB5 != null)
				DoorData.getInstance().getDoor(wallDoorNameB5).openMe();
			
			if (wallDoorNameB6 != null)
				DoorData.getInstance().getDoor(wallDoorNameB6).openMe();
			
			if (wallDoorNameB7 != null)
				DoorData.getInstance().getDoor(wallDoorNameB7).openMe();
			
			if (wallDoorNameB8 != null)
				DoorData.getInstance().getDoor(wallDoorNameB8).openMe();
			
			if (wallDoorNameB9 != null)
				DoorData.getInstance().getDoor(wallDoorNameB9).openMe();
			
			if (wallDoorNameB10 != null)
				DoorData.getInstance().getDoor(wallDoorNameB10).openMe();
		}
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		if (maker.getNpcsAlive() == 0)
			ThreadPool.schedule(() -> onTimer("1000", maker), 500);
	}
}