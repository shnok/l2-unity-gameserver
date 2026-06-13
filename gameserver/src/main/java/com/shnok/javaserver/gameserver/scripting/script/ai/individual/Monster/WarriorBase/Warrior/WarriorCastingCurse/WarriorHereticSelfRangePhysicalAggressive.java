package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingCurse;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class WarriorHereticSelfRangePhysicalAggressive extends WarriorHereticSelfRangePhysical
{
	public WarriorHereticSelfRangePhysicalAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingCurse");
	}
	
	public WarriorHereticSelfRangePhysicalAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22141
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable)
			tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}
