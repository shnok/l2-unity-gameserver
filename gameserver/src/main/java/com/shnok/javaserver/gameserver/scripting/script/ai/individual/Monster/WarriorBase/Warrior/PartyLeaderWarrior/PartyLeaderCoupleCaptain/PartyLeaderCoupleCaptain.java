package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderCoupleCaptain;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderWarrior;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderCoupleCaptain extends PartyLeaderWarrior
{
	public PartyLeaderCoupleCaptain()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior/PartyLeaderCoupleCaptain");
	}
	
	public PartyLeaderCoupleCaptain(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			npc._c_ai0 = attacker;
			final int i6 = Rnd.get(100);
			if ((npc.distance2D(attacker) > 300 && i6 < 20) || (npc.distance2D(attacker) > 100 && i6 < 50))
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SUMMON_MAGIC), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (success && npc._c_ai0 != null && skill == getNpcSkillByType(npc, NpcSkillType.SUMMON_MAGIC))
		{
			npc._c_ai0.instantTeleportTo(npc.getX(), npc.getY(), npc.getZ(), 0);
			npc.getAI().addAttackDesire(npc._c_ai0, 100000);
		}
		
		super.onUseSkillFinished(npc, creature, skill, success);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (called != caller && called.getAI().getTopDesireTarget() != null && called.getStatus().getHpRatio() > 0.7)
		{
			createOnePrivateEx(called, getNpcIntAIParam(called, "silhouette"), called.getX(), called.getY(), called.getZ(), 0, 0, false, 1000, called.getAI().getTopDesireTarget().getObjectId(), 0);
			called.deleteMe();
		}
	}
}