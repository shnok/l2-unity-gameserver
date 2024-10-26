package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPriest;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;

public class WarriorPriestAggressive extends WarriorPriest
{
	public WarriorPriestAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPriest");
	}
	
	public WarriorPriestAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21306
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