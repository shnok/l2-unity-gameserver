package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorPhysicalSpecialHoldAggressive extends WarriorPhysicalSpecial
{
	public WarriorPhysicalSpecialHoldAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorPhysicalSpecialHoldAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22137,
		22138,
		22194
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable)
			npc.getAI().addAttackDesireHold(creature, 50);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK || npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEvent(npc, 10001, attacker.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 400));
		
		if (attacker instanceof Playable)
		{
			if (npc.distance2D(attacker) < 80)
			{
				if (Rnd.get(100) < 33)
					npc.getAI().addCastDesireHold(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			}
			else
			{
				npc.getAI().addCastDesireHold(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
			}
			
			double f0 = getHateRatio(npc, attacker);
			f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
			npc.getAI().addAttackDesireHold(attacker, f0 * 100);
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (called.distance2D(attacker) < 80)
			{
				if (Rnd.get(100) < 33)
					called.getAI().addCastDesireHold(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			}
			else
			{
				called.getAI().addCastDesireHold(attacker, getNpcSkillByType(called, NpcSkillType.DD_MAGIC), 1000000);
			}
			
			double f0 = getHateRatio(called, attacker);
			f0 = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
			called.getAI().addAttackDesireHold(attacker, f0 * 100);
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		// Do Nothing
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		if (!npc.getSpawnLocation().equals(x, y, z))
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
		else
			npc.getAI().addDoNothingDesire(40, 30);
	}
	
	@Override
	public void onAttackFinished(Npc npc, Creature target)
	{
		if (target.isDead() && target instanceof Summon)
		{
			if (npc.distance2D(target.getActingPlayer()) < 80)
			{
				if (Rnd.get(100) < 33)
					npc.getAI().addCastDesireHold(target.getActingPlayer(), getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			}
			else
			{
				npc.getAI().addCastDesireHold(target.getActingPlayer(), getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
			}
			
			npc.getAI().addAttackDesireHold(target.getActingPlayer(), 50);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (creature.isDead() && creature instanceof Summon)
		{
			if (npc.distance2D(creature.getActingPlayer()) < 80)
			{
				if (Rnd.get(100) < 33)
					npc.getAI().addCastDesireHold(creature.getActingPlayer(), getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
			}
			else
			{
				npc.getAI().addCastDesireHold(creature.getActingPlayer(), getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
			}
			
			npc.getAI().addAttackDesireHold(creature.getActingPlayer(), 50);
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0)
		{
			double f0 = getHateRatio(npc, caster);
			f0 = (((1.0 * 1) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * 1) / (npc.getStatus().getLevel() + 7))));
			npc.getAI().addAttackDesireHold(caster.getActingPlayer(), f0 * 150);
		}
	}
}
