package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastEnchantPhysicalPoison;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastEnchantRangePhysicalPoisonAggressive extends WarriorCastEnchantPhysicalPoisonAggressive
{
	public WarriorCastEnchantRangePhysicalPoisonAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastEnchantPhysicalPoison");
	}
	
	public WarriorCastEnchantRangePhysicalPoisonAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21319
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			
			if (mostHated != null)
			{
				if (Rnd.get(100) < 33 && mostHated != attacker)
				{
					L2Skill rangePhysicalSpecial = getNpcSkillByType(npc, NpcSkillType.RANGE_PHYSICAL_SPECIAL);
					L2Skill physicalSpecial = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL);
					
					if (npc.getCast().meetsHpMpDisabledConditions(attacker, rangePhysicalSpecial))
					{
						npc.getAI().addCastDesire(attacker, rangePhysicalSpecial, 1000000);
					}
					else if (npc.getCast().meetsHpMpDisabledConditions(attacker, physicalSpecial))
					{
						npc.getAI().addCastDesire(attacker, physicalSpecial, 1000000);
					}
					else
					{
						npc._i_ai0 = 1;
						npc.getAI().addAttackDesire(attacker, 1000);
					}
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
}
