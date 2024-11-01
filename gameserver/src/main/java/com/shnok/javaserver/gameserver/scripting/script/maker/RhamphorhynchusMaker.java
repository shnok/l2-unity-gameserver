package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class RhamphorhynchusMaker extends VelociraptorMaker
{
	public RhamphorhynchusMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onStart(NpcMaker maker)
	{
		maker.getMakerMemo().set("i_ai0", 0);
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_03m1");
		if (maker0 != null)
		{
			if (maker.getNpcsAlive() == 0 && maker.getMakerMemo().getInteger("i_ai0", 0) == 0)
				maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
		}
	}
}