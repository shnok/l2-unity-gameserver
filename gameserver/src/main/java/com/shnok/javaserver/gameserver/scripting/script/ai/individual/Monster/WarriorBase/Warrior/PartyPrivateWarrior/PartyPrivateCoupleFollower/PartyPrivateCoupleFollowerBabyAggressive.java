package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior.PartyPrivateCoupleFollower;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyPrivateCoupleFollowerBabyAggressive extends PartyPrivateCoupleFollower
{
	public PartyPrivateCoupleFollowerBabyAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior/PartyPrivateCoupleFollower");
	}
	
	public PartyPrivateCoupleFollowerBabyAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22120
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
			if (topDesireTarget != null && topDesireTarget == attacker && Rnd.get(100) < 33)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if ((called.getAI().getLifeTime() > 7 && (attacker instanceof Playable)) && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && Rnd.get(100) < 33)
			called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
