package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PrimevalRoamer;

import net.sf.l2j.gameserver.model.actor.Npc;

public class PrimevalRoamerWalker extends PrimevalRoamer
{
	public PrimevalRoamerWalker()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior/PrimevalRoamer");
	}
	
	public PrimevalRoamerWalker(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22205,
		22202,
		22210,
		22198,
		22213
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		super.onCreated(npc);
		
		npc.getAI().addMoveRouteDesire(getNpcStringAIParam(npc, "SuperPointName"), 50);
	}
}