package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.gameserver.model.actor.Npc;

public class Corpse extends DefaultNpc
{
	public Corpse()
	{
		super("ai/individual");
	}
	
	public Corpse(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18119,
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.doDie(npc);
	}
}
