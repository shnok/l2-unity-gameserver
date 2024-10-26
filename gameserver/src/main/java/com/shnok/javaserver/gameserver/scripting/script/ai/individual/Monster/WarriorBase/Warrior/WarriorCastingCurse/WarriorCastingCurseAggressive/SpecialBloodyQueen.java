package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingCurse.WarriorCastingCurseAggressive;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class SpecialBloodyQueen extends WarriorCastingCurseAggressive
{
	public SpecialBloodyQueen()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingCurse/WarriorCastingCurseAggressive");
	}
	
	public SpecialBloodyQueen(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21084
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
			{
				final int i1 = Rnd.get(100);
				
				final double dist = npc.distance2D(attacker);
				if (dist > 300)
				{
					if (i1 < 80)
					{
						final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
						npc.getAI().addCastDesire(attacker, ddMagic, 1000000);
						
						npc._i_ai0 = 1;
					}
				}
				else if (dist > 100)
				{
					final Creature toDesireTarget = npc.getAI().getTopDesireTarget();
					if (toDesireTarget != null)
					{
						if ((toDesireTarget == attacker && i1 < 70) || i1 < 30)
						{
							final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
							npc.getAI().addCastDesire(attacker, ddMagic, 1000000);
							
							npc._i_ai0 = 1;
						}
					}
				}
				else if (i1 < 10)
				{
					final Creature toDesireTarget = npc.getAI().getTopDesireTarget();
					if (toDesireTarget != null && toDesireTarget == attacker)
					{
						final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
						npc.getAI().addCastDesire(attacker, ddMagic, 1000000);
						
						npc._i_ai0 = 1;
					}
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			if (called.distance2D(attacker) > 100 && called._i_ai0 == 0)
			{
				if (Rnd.get(100) < 40)
				{
					final L2Skill ddMagic = getNpcSkillByType(called, NpcSkillType.DD_MAGIC);
					called.getAI().addCastDesire(attacker, ddMagic, 1000000);
					
					called._i_ai0 = 1;
				}
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory())
		{
			if (npc.distance2D(creature) > 100 && npc._i_ai0 == 0)
			{
				if (Rnd.get(100) < 20)
				{
					final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
					npc.getAI().addCastDesire(creature, ddMagic, 1000000);
					
					npc._i_ai0 = 1;
				}
			}
		}
		super.onSeeCreature(npc, creature);
	}
}