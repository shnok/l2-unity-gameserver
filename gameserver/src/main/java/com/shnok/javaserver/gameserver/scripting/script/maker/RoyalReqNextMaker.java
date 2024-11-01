package com.shnok.javaserver.gameserver.scripting.script.maker;

import java.util.Calendar;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class RoyalReqNextMaker extends RoyalRushMaker
{
	public RoyalReqNextMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onNpcDeleted(Npc npc, MultiSpawn ms, NpcMaker maker)
	{
		final int bossMaker = maker.getMakerMemo().getInteger("BossMaker", -1);
		if (bossMaker == 1)
		{
			if (npc.getNpcId() == 25339 || npc.getNpcId() == 25342 || npc.getNpcId() == 25346 || npc.getNpcId() == 25349)
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(maker.getMakerMemo().get("next_maker_name"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1002", maker0, 0, 0);
			}
		}
		else
		{
			final int reqCount = maker.getMakerMemo().getInteger("req_count", -1);
			if (maker.getNpcsAlive() == reqCount)
			{
				final Calendar c = Calendar.getInstance();
				
				final int currentMinute = c.get(Calendar.MINUTE);
				if (currentMinute >= 0 && currentMinute <= 50)
				{
					final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(maker.getMakerMemo().get("next_maker_name"));
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
			}
		}
	}
}