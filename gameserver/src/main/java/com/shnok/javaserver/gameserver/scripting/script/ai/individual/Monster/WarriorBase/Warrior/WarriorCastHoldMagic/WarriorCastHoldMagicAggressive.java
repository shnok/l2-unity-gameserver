package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastHoldMagic;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class WarriorCastHoldMagicAggressive extends WarriorCastHoldMagic
{
	public WarriorCastHoldMagicAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastHoldMagic");
	}
	
	public WarriorCastHoldMagicAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20134,
		20105,
		20144,
		20057,
		20171,
		20351,
		20419,
		18008
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