package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.TyrannoPrimeval;

import com.shnok.javaserver.gameserver.model.actor.Npc;

public class TyrannoPrimevalWalker extends TyrannoPrimeval
{
	public TyrannoPrimevalWalker()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/TyrannoPrimeval");
	}
	
	public TyrannoPrimevalWalker(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22217 // tyrannosaurus_s
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		super.onCreated(npc);
		
		npc.getAI().addMoveRouteDesire(getNpcStringAIParam(npc, "SuperPointName"), 50);
	}
}