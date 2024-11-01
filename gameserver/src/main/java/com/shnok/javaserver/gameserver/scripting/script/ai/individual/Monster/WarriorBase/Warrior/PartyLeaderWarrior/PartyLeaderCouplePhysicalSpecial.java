package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderCouplePhysicalSpecial extends PartyLeaderWarrior
{
	public PartyLeaderCouplePhysicalSpecial()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderCouplePhysicalSpecial(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21432
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget == attacker && npc.getStatus().getHpRatio() < 0.2 && Rnd.get(100) < 33)
			{
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
				
				npc._i_ai0 = 1;
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called._i_ai0 == 0 && Rnd.get(100) < 33)
		{
			called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			
			called._i_ai0 = 1;
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (called.isMaster() && caller.distance2D(called) < 100)
		{
			called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
			called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SELF_BUFF), 1000000);
			
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				called.removeAllAttackDesire();
				called.getAI().addAttackDesire(topDesireTarget, 1000);
			}
		}
		super.onPartyDied(caller, called);
	}
}