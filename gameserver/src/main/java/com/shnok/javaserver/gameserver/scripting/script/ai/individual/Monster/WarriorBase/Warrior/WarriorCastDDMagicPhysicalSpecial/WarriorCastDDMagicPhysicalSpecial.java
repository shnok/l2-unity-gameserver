package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastDDMagicPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastDDMagicPhysicalSpecial extends Warrior
{
	public WarriorCastDDMagicPhysicalSpecial()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastDDMagicPhysicalSpecial");
	}
	
	public WarriorCastDDMagicPhysicalSpecial(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22046,
		22049
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			
			if (mostHated != null)
			{
				if (npc.distance2D(attacker) > 200 && mostHated == attacker)
				{
					L2Skill longRangeDD = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
					
					npc.getAI().addCastDesire(attacker, longRangeDD, 1000000);
				}
				if (Rnd.get(100) < 33 && mostHated != attacker && npc.distance2D(attacker) < 200)
				{
					L2Skill physicalSpecialRange = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL_RANGE);
					
					npc.getAI().addCastDesire(attacker, physicalSpecialRange, 1000000);
				}
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() > 7 && attacker instanceof Playable)
		{
			Creature mostHated = called.getAI().getAggroList().getMostHatedCreature();
			
			if (mostHated != null)
			{
				if (called.distance2D(attacker) < 200 && Rnd.get(100) < 33 && mostHated != attacker)
				{
					L2Skill physicalSpecialRange = getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL_RANGE);
					
					called.getAI().addCastDesire(attacker, physicalSpecialRange, 1000000);
				}
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		
		if (mostHated != null)
		{
			if (npc.distance2D(mostHated) > 200 && mostHated instanceof Player)
			{
				L2Skill longRangeDD = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
				
				npc.getAI().addCastDesire(mostHated, longRangeDD, 1000000);
			}
		}
		
		super.onUseSkillFinished(npc, creature, skill, success);
	}
}
