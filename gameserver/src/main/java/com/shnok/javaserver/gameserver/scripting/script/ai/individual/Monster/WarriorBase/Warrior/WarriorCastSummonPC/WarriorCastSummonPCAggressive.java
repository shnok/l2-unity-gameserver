package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastSummonPC;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class WarriorCastSummonPCAggressive extends WarriorCastSummonPC
{
	public WarriorCastSummonPCAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastSummonPC");
	}
	
	public WarriorCastSummonPCAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20221
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}