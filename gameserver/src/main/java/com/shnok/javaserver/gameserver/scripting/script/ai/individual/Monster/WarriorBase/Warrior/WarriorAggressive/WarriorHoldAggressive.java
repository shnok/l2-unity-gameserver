package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorAggressive;

import com.shnok.javaserver.gameserver.model.actor.Npc;

public class WarriorHoldAggressive extends WarriorAggressive
{
	public WarriorHoldAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorAggressive");
	}
	
	public WarriorHoldAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21661,
		21684,
		21707,
		21730,
		21753,
		21776
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (npc.getPosition().clone().distance2D(npc.getSpawnLocation()) > 0)
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
}
