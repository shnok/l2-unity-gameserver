package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushPartyPrivateHate extends PartyPrivateWarrior
{
	public RoyalRushPartyPrivateHate()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior");
	}
	
	public RoyalRushPartyPrivateHate(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21435
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF), 1000000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null && Rnd.get(100) < 33 && topDesireTarget == attacker)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.MOB_HATE), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (Rnd.get(100) < 10)
			called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.MOB_HATE), 1000000);
		
		super.onPartyAttacked(caller, called, target, damage);
	}
}
