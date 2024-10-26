package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderCannibaliAggressiva extends PartyLeaderWarriorAggressive
{
	public PartyLeaderCannibaliAggressiva()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderCannibaliAggressiva(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22113
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai3 = 0;
		npc._c_ai0 = npc;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc._c_ai0 = attacker;
		if (attacker instanceof Playable)
		{
			if (damage == 0)
				damage = 1;
			
			npc.getAI().addAttackDesire(attacker, ((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 1000);
		}
		
		if (npc.getStatus().getHpRatio() < 0.5 && npc._i_ai1 == 0)
		{
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BUFF), 1000000);
			npc._i_ai1 = 1;
		}
		
		if (Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1), 1000000);
		
		final double dist = npc.distance2D(attacker);
		if (dist > 300 && dist < 1000 && Rnd.get(100) < 10)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SUMMON_MAGIC), 1000000);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (called != caller)
		{
			if (caller.getStatus().getHpRatio() < 0.33)
			{
				called._i_ai3 = 1;
				called._c_ai1 = caller;
			}
		}
		else if (called._i_ai0 == 0)
		{
			final double hpRatio = called.getStatus().getHpRatio();
			if (hpRatio < 0.7 && called._i_ai3 == 1)
			{
				called._i_ai0 = 1;
				called.getAI().addCastDesire(called._c_ai1, getNpcSkillByType(called, NpcSkillType.EFFECT_SKILL), 1000000);
			}
			
			if (hpRatio < 0.33 && !caller.isDead())
			{
				called._i_ai0 = 1;
				called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.EFFECT_SKILL2), 1000000);
			}
		}
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (npc._c_ai0 != null)
		{
			if (skill == getNpcSkillByType(npc, NpcSkillType.SUMMON_MAGIC) && npc.distance2D(npc._c_ai0) < 1000 && Rnd.get(100) < 10)
			{
				npc._c_ai0.instantTeleportTo(npc.getPosition().clone(), 0);
				npc.getAI().addAttackDesire(npc._c_ai0, 100000);
			}
			
			if (skill == getNpcSkillByType(npc, NpcSkillType.EFFECT_SKILL))
			{
				createOnePrivateEx(npc, 22114, npc.getX(), npc.getY(), npc.getZ(), 0, 0, true, 1000, npc._c_ai0.getObjectId(), 0);
				npc.deleteMe();
				broadcastScriptEvent(npc, 10028, npc.getObjectId(), 500);
			}
		}
		
		if (skill == getNpcSkillByType(npc, NpcSkillType.EFFECT_SKILL2))
			broadcastScriptEvent(npc, 10028, npc.getObjectId(), 500);
	}
}