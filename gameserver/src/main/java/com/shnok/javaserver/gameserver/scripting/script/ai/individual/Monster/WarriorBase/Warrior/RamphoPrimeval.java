package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class RamphoPrimeval extends Warrior
{
	public RamphoPrimeval()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public RamphoPrimeval(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22199
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai2 = 0;
		npc._i_ai3 = 1;
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		npc._i_quest0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (!npc.isInCombat())
		{
			npc._i_ai2 = 0;
			npc._i_ai3 = 1;
			
			if (getNpcIntAIParam(npc, "mobile_type") == 0)
				npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
		}
		
		if (npc._i_quest0 == 1)
			if (getElapsedTicks(npc._i_ai4) > 600)
				broadcastScriptEvent(npc, 11051, 0, 8000);
			
		super.onNoDesire(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		final double hpRatio = npc.getStatus().getHpRatio();
		
		if ((hpRatio * 100) <= getNpcIntAIParamOrDefault(npc, "HpChkRate4", 50))
			npc._i_ai3 = getNpcIntAIParamOrDefault(npc, "ProbMultiplier1", 2);
		else
			npc._i_ai3 = 1;
		
		if ((hpRatio * 100) <= getNpcIntAIParamOrDefault(npc, "HpChkRate5", 30))
		{
			if (npc._i_ai2 == 0)
			{
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF1), 10000000);
				
				npc._i_ai2 = 1;
			}
		}
		
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
		if (npc.distance2D(attacker) < getNpcIntAIParamOrDefault(npc, "LongRangeSkillDist", 100))
		{
			if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbLongRangeDDMagic1", 0) * npc._i_ai3))
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 10000000);
			else if (topDesireTarget != null)
			{
				if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbLongRangeDDMagic1", 0) * npc._i_ai3))
					npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 10000000);
				
				if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbPhysicalSpecial1", 0) * npc._i_ai3))
					npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1), 10000000);
				
				if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbPhysicalSpecial2", 0) * npc._i_ai3))
					npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 10000000);
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 11049)
		{
			npc._i_quest0 = 1;
		}
		else if (eventId == 10016)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 instanceof Player && getNpcIntAIParamOrDefault(npc, "BroadCastReception", 0) == 1)
			{
				npc.removeAllAttackDesire();
				
				onAttacked(npc, c0, 1000, null);
			}
		}
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (skill.getAggroPoints() > 0 && Rnd.get(100) <= getNpcIntAIParamOrDefault(npc, "ProbCond4", 30))
		{
			if (targets.length > 0 && targets[0] instanceof Player)
			{
				if (Rnd.get(2) == 1)
					npc.getAI().addCastDesire(caster, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1), 10000000);
				
				npc.getAI().addCastDesire(caster, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 10000000);
			}
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (creature instanceof Playable)
			return;
		
		if (getNpcIntAIParamOrDefault(npc, "ag_type", 0) == 1 && creature instanceof Player)
			if (!npc.isInCombat())
				npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 10000000);
			
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
}