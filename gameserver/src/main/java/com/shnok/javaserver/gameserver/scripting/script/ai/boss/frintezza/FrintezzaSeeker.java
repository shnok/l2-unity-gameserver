package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class FrintezzaSeeker extends DefaultNpc
{
	public FrintezzaSeeker()
	{
		super("ai/boss/frintezza");
	}
	
	public FrintezzaSeeker(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29059 // frintessa_seeker
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		if (npc.getSpawn().getDBLoaded())
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
			if (c0 != null && !c0.isDead())
			{
				if (c0.getSpawn().getSpawnData().getDBValue() > 1)
					npc.deleteMe();
			}
			else
				npc.deleteMe();
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("4");
		if (c0 != null && !c0.isDead() && c0.getSpawn().getSpawnData().getDBValue() <= 1)
		{
			c0.sendScriptEvent(0, 2, 0);
			
			npc.deleteMe();
		}
	}
}