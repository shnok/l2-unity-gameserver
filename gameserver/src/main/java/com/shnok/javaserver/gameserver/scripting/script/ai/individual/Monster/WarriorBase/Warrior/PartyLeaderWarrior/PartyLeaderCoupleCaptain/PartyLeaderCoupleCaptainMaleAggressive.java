package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderCoupleCaptain;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderCoupleCaptainMaleAggressive extends PartyLeaderCoupleCaptain
{
	public PartyLeaderCoupleCaptainMaleAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior/PartyLeaderCoupleCaptain");
	}
	
	public PartyLeaderCoupleCaptainMaleAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22109
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (Rnd.get(100) < 33 && topDesireTarget == attacker)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
				
				if (Rnd.get(100) < 33)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL), 1000000);
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && Rnd.get(100) < 33)
			called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (target instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			if (Rnd.get(100) < 33)
				called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			
			if (Rnd.get(100) < 33)
				called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL), 1000000);
			
			super.onPartyAttacked(caller, called, target, damage);
		}
	}
}