package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class PartyLeaderSplitAggressive extends PartyLeaderSplit
{
	public PartyLeaderSplitAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public PartyLeaderSplitAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22094
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
