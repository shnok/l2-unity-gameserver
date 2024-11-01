package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorBerserkerPhysicalSpecial extends WarriorBerserker
{
	public WarriorBerserkerPhysicalSpecial()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorBerserkerPhysicalSpecial(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21410
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null && topDesireTarget == attacker)
			{
				if (Rnd.get(100) < 33)
				{
					final L2Skill physicalSpecial = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL);
					npc.getAI().addCastDesire(attacker, physicalSpecial, 1000000);
				}
				
				final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
				if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
					npc.getAI().addCastDesire(attacker, debuff, 1000000);
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			if (Rnd.get(100) < 33)
			{
				final L2Skill physicalSpecial = getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL);
				called.getAI().addCastDesire(attacker, physicalSpecial, 1000000);
			}
			
			if (Rnd.get(100) < 33)
			{
				final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
				called.getAI().addCastDesire(attacker, debuff, 1000000);
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onClanDied(Npc caller, Npc called, Creature killer)
	{
		if (caller != called && called._i_ai1 < getNpcIntAIParam(called, "MaxRoarCount"))
		{
			called._i_ai1++;
			
			final L2Skill furyBase = getNpcSkillByType(called, NpcSkillType.FURY);
			final L2Skill fury = SkillTable.getInstance().getInfo(furyBase.getId(), called._i_ai1);
			
			called.getAI().addCastDesire(called, fury, 1000000);
			
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				final L2Skill selfRangePhysicalSpecial = getNpcSkillByType(called, NpcSkillType.SELF_RANGE_PHYSICAL_SPECIAL);
				called.getAI().addCastDesire(called, selfRangePhysicalSpecial, 1000000);
			}
		}
		super.onClanDied(caller, called, killer);
	}
}