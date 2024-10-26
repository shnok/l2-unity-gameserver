package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderPabelgolemAggressive extends PartyLeaderWarriorAggressive
{
	public PartyLeaderPabelgolemAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderPabelgolemAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22060,
		22074
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Party party0 = attacker.getParty();
		
		if (party0 == null && Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		else if (party0 != null && Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (Rnd.get(100) < 20)
			called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.DEBUFF), 1000000);
		
		super.onPartyAttacked(caller, called, target, damage);
	}
}
