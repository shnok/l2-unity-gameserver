package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderCoupleCaptain;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderCoupleCaptainNurseAggressive extends PartyLeaderCoupleCaptain
{
	public PartyLeaderCoupleCaptainNurseAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior/PartyLeaderCoupleCaptain");
	}
	
	public PartyLeaderCoupleCaptainNurseAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22118
	};
	
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
				if (npc.distance2D(attacker) < 200 && Rnd.get(100) < 33 && topDesireTarget == attacker)
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
				
				if (npc.distance2D(attacker) > 100 && Rnd.get(100) < 20 && topDesireTarget == attacker)
					npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() > 7 && (attacker instanceof Playable))
		{
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (called.distance2D(attacker) < 200 && Rnd.get(100) < 33 && topDesireTarget == attacker)
					called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
				
				if (called.distance2D(attacker) > 100 && Rnd.get(100) < 20 && topDesireTarget == attacker)
					called.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(called, NpcSkillType.DD_MAGIC), 1000000);
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
