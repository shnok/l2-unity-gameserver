package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecialShotRange;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class WarriorPhysicalSpecialShotRangeAggressive extends WarriorPhysicalSpecialShotRange
{
	public WarriorPhysicalSpecialShotRangeAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecialShotRange");
	}
	
	public WarriorPhysicalSpecialShotRangeAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22053
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
		
		if (npc.distance2D(creature) > 200)
			npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_PHYSICAL_SPECIAL), 1000000);
		
		startQuestTimer("1002", npc, null, 5000);
		
		super.onSeeCreature(npc, creature);
	}
}