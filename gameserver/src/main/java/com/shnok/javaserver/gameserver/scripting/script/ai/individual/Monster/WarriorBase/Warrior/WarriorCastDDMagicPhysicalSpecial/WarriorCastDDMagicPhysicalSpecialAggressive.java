package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastDDMagicPhysicalSpecial;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastDDMagicPhysicalSpecialAggressive extends WarriorCastDDMagicPhysicalSpecial
{
	public WarriorCastDDMagicPhysicalSpecialAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastDDMagicPhysicalSpecial");
	}
	
	public WarriorCastDDMagicPhysicalSpecialAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21308,
		21392,
		21389,
		21429,
		21393
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
		{
			super.onSeeCreature(npc, creature);
			return;
		}
		
		if (npc.getAI().getLifeTime() > 7 && npc.getAI().getTopDesireTarget() == null && npc.isInMyTerritory())
		{
			if (npc.distance2D(creature) > 200)
			{
				final L2Skill longRangeDD = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
				npc.getAI().addCastDesire(creature, longRangeDD, 1000000);
			}
			
			tryToAttack(npc, creature);
			
			super.onSeeCreature(npc, creature);
		}
	}
}