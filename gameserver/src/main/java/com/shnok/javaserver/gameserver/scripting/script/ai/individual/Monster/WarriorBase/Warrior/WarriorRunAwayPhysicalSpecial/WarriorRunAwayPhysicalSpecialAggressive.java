package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorRunAwayPhysicalSpecial;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class WarriorRunAwayPhysicalSpecialAggressive extends WarriorRunAwayPhysicalSpecial
{
	public WarriorRunAwayPhysicalSpecialAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorRunAwayPhysicalSpecialAggressive");
	}
	
	public WarriorRunAwayPhysicalSpecialAggressive(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable)
			tryToAttack(npc, creature);
	}
}