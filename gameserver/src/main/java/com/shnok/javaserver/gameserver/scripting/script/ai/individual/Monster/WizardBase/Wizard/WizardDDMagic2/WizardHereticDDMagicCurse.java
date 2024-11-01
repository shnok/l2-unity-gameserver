package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardHereticDDMagicCurse extends WizardDDMagic2Curse
{
	public WizardHereticDDMagicCurse()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardHereticDDMagicCurse(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22150
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai4 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if ((npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST) && npc._i_ai4 == 0)
			npc._i_ai4 = 1;
		
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			int i0 = npc.getAI().getHateList().isEmpty() ? 0 : 1;
			final L2Skill debuff1 = getNpcSkillByType(npc, NpcSkillType.DEBUFF1);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff1) <= 0 && i0 == 1)
			{
				if (npc.getCast().meetsHpMpConditions(attacker, debuff1))
				{
					npc.getAI().addCastDesire(attacker, debuff1, 1000000);
				}
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(attacker, 1000);
				}
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (Rnd.get(100) < 33)
			npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.DEBUFF1), 1000000);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if ((called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && called.getAI().getCurrentIntention().getType() != IntentionType.CAST) && called._i_ai4 == 0)
			called._i_ai4 = 1;
		
		if ((called.getAI().getLifeTime() > 7 && attacker instanceof Playable) && called.getAI().getHateList().size() == 0)
		{
			final L2Skill debuff1 = getNpcSkillByType(called, NpcSkillType.DEBUFF1);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff1) <= 0)
			{
				if (called.getCast().meetsHpMpConditions(attacker, debuff1))
				{
					called.getAI().addCastDesire(attacker, debuff1, 1000000);
				}
				else
				{
					called._i_ai0 = 1;
					called.getAI().addAttackDesire(attacker, 1000);
				}
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onOutOfTerritory(Npc npc)
	{
		if (npc._i_ai4 == 0)
		{
			npc.removeAllAttackDesire();
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10033 || eventId == 10002)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 != null)
			{
				if (eventId == 10033)
					npc._i_ai4 = 1;
				
				npc.removeAllAttackDesire();
				if (npc.distance2D(c0) > 100)
				{
					if (npc.getCast().meetsHpMpConditions(c0, getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC)))
					{
						npc.getAI().addCastDesire(c0, getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC), 1000000);
					}
					else
					{
						npc._i_ai0 = 1;
						npc.getAI().addAttackDesire(c0, 1000);
					}
				}
				else if (npc.getCast().meetsHpMpConditions(c0, getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC)))
				{
					npc.getAI().addCastDesire(c0, getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC), 1000000);
				}
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(c0, 1000);
				}
				if (c0 instanceof Playable)
				{
					npc.getAI().getHateList().addHateInfo(c0, 200);
				}
			}
		}
		else if (eventId == 10035)
		{
			npc._i_ai4 = 0;
			npc.removeAllAttackDesire();
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
		}
	}
}
