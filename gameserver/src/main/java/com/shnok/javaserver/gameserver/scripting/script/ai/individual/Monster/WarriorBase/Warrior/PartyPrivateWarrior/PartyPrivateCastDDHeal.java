package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyPrivateCastDDHeal extends PartyPrivateWarrior
{
	public PartyPrivateCastDDHeal()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior");
	}
	
	public PartyPrivateCastDDHeal(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20948,
		20943,
		20987,
		20975,
		20764,
		20981,
		21059,
		21074,
		21080
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33 && npc.getStatus().getHpRatio() < 0.7)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL), 1000000);
		
		if (attacker instanceof Playable && npc.distance2D(attacker) > 100)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget == attacker && Rnd.get(100) < 33)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (caller != called && Rnd.get(100) < 33 && caller.getStatus().getHpRatio() < 0.7)
			called.getAI().addCastDesire(caller, getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
		
		if (target instanceof Playable && called.distance2D(target) > 100 && Rnd.get(100) < 33)
			called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.DD_MAGIC), 1000000);
		
		super.onPartyAttacked(caller, called, target, damage);
	}
}