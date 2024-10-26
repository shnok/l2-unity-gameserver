package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderSlowTypeAggressive extends PartyLeaderWarriorAggressive
{
	public PartyLeaderSlowTypeAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderSlowTypeAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22092
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable && npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory())
		{
			final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
			if (currentIntention != IntentionType.ATTACK && npc.distance2D(creature) > 100 && Rnd.get(100) < 10)
			{
				final L2Skill ddMagicSlow = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_SLOW);
				npc.getAI().addCastDesire(creature, ddMagicSlow, 1000000);
			}
		}
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		if (mostHated != null && attacker != mostHated)
		{
			final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
			if (currentIntention == IntentionType.ATTACK && npc.distance2D(mostHated) > 100 && npc.distance2D(attacker) < 100 && Rnd.get(100) < 80)
			{
				npc.removeAllAttackDesire();
				
				if (attacker instanceof Playable)
				{
					double hateRatio = getHateRatio(npc, attacker);
					hateRatio = ((((double) damage) / (npc.getStatus().getLevel() + 7)) + ((hateRatio / 100) * (((double) damage) / (npc.getStatus().getLevel() + 7))));
					
					npc.getAI().addAttackDesire(attacker, hateRatio * 30);
				}
			}
		}
		
		if (npc.distance2D(attacker) < 200 && Rnd.get(100) < 10)
		{
			final L2Skill ddMagicSlow = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_SLOW);
			npc.getAI().addCastDesire(attacker, ddMagicSlow, 1000000);
		}
		
		if (npc._i_ai0 == 1)
		{
			if (mostHated != null && mostHated == attacker)
				npc._i_ai1 = 1;
		}
		else
		{
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai0 = 1;
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
			if (currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
			{
				npc._i_ai0 = 0;
				npc._i_ai1 = 0;
				
				return super.onTimer(name, npc, player);
			}
			
			if (npc._i_ai1 == 0)
			{
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				if (mostHated != null && Rnd.get(100) < 50)
				{
					final L2Skill ddMagicSlow = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC_SLOW);
					npc.getAI().addCastDesire(mostHated, ddMagicSlow, 1000000);
				}
			}
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai0 = 0;
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Player && called.getAI().getLifeTime() > 7)
		{
			final IntentionType currentIntention = called.getAI().getCurrentIntention().getType();
			if (currentIntention != IntentionType.ATTACK && Rnd.get(100) < 10)
			{
				final L2Skill ddMagicSlow = getNpcSkillByType(called, NpcSkillType.DD_MAGIC_SLOW);
				called.getAI().addCastDesire(attacker, ddMagicSlow, 1000000);
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}