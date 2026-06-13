package com.shnok.javaserver.gameserver.scripting.script.maker;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class TyrannosaurusMaker extends VelociraptorMaker
{
	public TyrannosaurusMaker(String name)
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
		if (maker.getNpcsAlive() == 0)
			ThreadPool.schedule(() -> onTimer("1002", maker), 180000);
	}
	
	@Override
	public void onTimer(String name, NpcMaker maker)
	{
		if (name.equalsIgnoreCase("1002"))
		{
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("rune20_mb2017_04m1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("11042", maker0, 0, 0);
		}
		super.onTimer(name, maker);
	}
}