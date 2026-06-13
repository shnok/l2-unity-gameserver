package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushPartyLeader extends PartyLeaderWarrior
{
	public RoyalRushPartyLeader()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public RoyalRushPartyLeader(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21434
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
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (Rnd.get(100) < 33 && topDesireTarget == attacker)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1), 1000000);
				
				if (Rnd.get(100) < 33 && npc._i_ai0 == 0)
				{
					createOnePrivateEx(npc, getNpcIntAIParam(npc, "SummonSlave"), attacker.getX(), attacker.getY(), attacker.getZ(), 0, 0, true, 1000, topDesireTarget.getObjectId(), 0);
					npc._i_ai0 = 1;
				}
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
}
