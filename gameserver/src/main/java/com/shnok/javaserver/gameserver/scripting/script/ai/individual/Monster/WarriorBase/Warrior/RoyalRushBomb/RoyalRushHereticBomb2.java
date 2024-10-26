package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.RoyalRushBomb;

import net.sf.l2j.gameserver.model.actor.Npc;

public class RoyalRushHereticBomb2 extends RoyalRushHereticBomb
{
	public RoyalRushHereticBomb2()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/RoyalRushBomb");
	}
	
	public RoyalRushHereticBomb2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22195
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
}
