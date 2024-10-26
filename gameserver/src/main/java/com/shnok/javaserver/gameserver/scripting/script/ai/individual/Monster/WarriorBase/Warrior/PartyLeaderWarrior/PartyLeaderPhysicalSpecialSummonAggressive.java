package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;

public class PartyLeaderPhysicalSpecialSummonAggressive extends PartyLeaderPhysicalSpecialSummon
{
	public PartyLeaderPhysicalSpecialSummonAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderPhysicalSpecialSummonAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22032,
		22038,
		22039
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
