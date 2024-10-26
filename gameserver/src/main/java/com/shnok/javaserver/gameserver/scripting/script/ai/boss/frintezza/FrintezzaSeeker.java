package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.memo.GlobalMemo;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;

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