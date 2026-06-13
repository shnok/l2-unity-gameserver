package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCast3SkillsApproach;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCast3SkillsApproachAggressive extends WarriorCast3SkillsApproach
{
	public WarriorCast3SkillsApproachAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCast3SkillsApproach");
	}
	
	public WarriorCast3SkillsApproachAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21309,
		20814,
		21273,
		20674,
		20811,
		21061,
		20820,
		20816,
		21116,
		20629,
		21402,
		21034,
		21641,
		20829,
		20827,
		21649,
		20853,
		20854,
		20861,
		21087,
		21089,
		22010,
		22110,
		22114
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		if (npc.getAI().getLifeTime() > 7 && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.isInMyTerritory())
		{
			if (Rnd.get(100) < 33)
			{
				final L2Skill physicalSpecial = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL);
				npc.getAI().addCastDesire(creature, physicalSpecial, 1000000);
			}
		}
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}