package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior.PartyPrivateCoupleFollower;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyPrivateCoupleFollowerFemaleAggressive extends PartyPrivateCoupleFollower
{
	public PartyPrivateCoupleFollowerFemaleAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior/PartyPrivateCoupleFollower");
	}
	
	public PartyPrivateCoupleFollowerFemaleAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22108
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BUFF), 1000000);
		
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
			if (topDesireTarget != null && topDesireTarget == attacker)
			{
				if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF)) <= 0)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF), 1000000);
				
				if (Rnd.get(100) < 33)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
				
				if (getAbnormalLevel(attacker, getNpcSkillByType(npc, NpcSkillType.BUFF)) <= 0 && Rnd.get(100) < 33 && npc.getStatus().getHpRatio() > 0.5)
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BUFF), 1000000);
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if ((called.getAI().getLifeTime() > 7 && (attacker instanceof Playable)) && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			if (Rnd.get(100) < 33)
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, getNpcSkillByType(called, NpcSkillType.DEBUFF)) <= 0)
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.DEBUFF), 1000000);
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if ((called.getAI().getLifeTime() > 7 && (target instanceof Playable)) && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			if (Rnd.get(100) < 33)
				called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			
			if (Rnd.get(100) < 33 && getAbnormalLevel(target, getNpcSkillByType(called, NpcSkillType.DEBUFF)) <= 0)
				called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.DEBUFF), 1000000);
			
			if (getAbnormalLevel(target, getNpcSkillByType(called, NpcSkillType.BUFF)) <= 0 && Rnd.get(100) < 33 && called.getStatus().getHpRatio() > 0.5)
				called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.BUFF), 1000000);
		}
		
		super.onPartyAttacked(caller, called, target, damage);
	}
}
