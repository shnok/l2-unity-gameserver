package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCast2DebuffsAggressive extends Warrior
{
	public WarriorCast2DebuffsAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorCast2DebuffsAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21379
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			{
				npc._i_ai1 = 0;
				npc._i_ai2 = 0;
				
				return super.onTimer(name, npc, player);
			}
			
			if (npc._i_ai2 == 0)
			{
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				if (mostHated != null && Rnd.get(100) < 50)
				{
					final L2Skill debuff1 = getNpcSkillByType(npc, NpcSkillType.DEBUFF1);
					if (getAbnormalLevel(mostHated, debuff1) <= 0)
						npc.getAI().addCastDesire(mostHated, debuff1, 1000000);
				}
			}
			
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai2 = 0;
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
		{
			super.onSeeCreature(npc, creature);
			return;
		}
		
		if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory() && npc.getAI().getCurrentIntention().getType() == IntentionType.WANDER)
		{
			final L2Skill debuff1 = getNpcSkillByType(npc, NpcSkillType.DEBUFF1);
			if (Rnd.get(100) < 33 && getAbnormalLevel(creature, debuff1) <= 0)
				npc.getAI().addCastDesire(creature, debuff1, 1000000);
			
			final L2Skill debuff2 = getNpcSkillByType(npc, NpcSkillType.DEBUFF2);
			if (Rnd.get(100) < 33 && getAbnormalLevel(creature, debuff2) <= 0)
				npc.getAI().addCastDesire(creature, debuff2, 1000000);
		}
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		
		if (npc._i_ai1 == 1)
		{
			if (mostHated == attacker)
				npc._i_ai2 = 1;
		}
		else
		{
			startQuestTimer("2001", npc, null, 5000);
			npc._i_ai1 = 1;
		}
		
		if (attacker instanceof Playable && mostHated == attacker)
		{
			final L2Skill debuff1 = getNpcSkillByType(npc, NpcSkillType.DEBUFF1);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff1) <= 0)
				npc.getAI().addCastDesire(attacker, debuff1, 1000000);
			
			final L2Skill debuff2 = getNpcSkillByType(npc, NpcSkillType.DEBUFF2);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff2) <= 0)
				npc.getAI().addCastDesire(attacker, debuff2, 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			final L2Skill debuff1 = getNpcSkillByType(called, NpcSkillType.DEBUFF1);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff1) <= 0)
				called.getAI().addCastDesire(attacker, debuff1, 1000000);
			
			final L2Skill debuff2 = getNpcSkillByType(called, NpcSkillType.DEBUFF2);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff2) <= 0)
				called.getAI().addCastDesire(attacker, debuff2, 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}