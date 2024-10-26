package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardCastClanBuffRangeCurse extends WizardDDMagic2
{
	public WizardCastClanBuffRangeCurse()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardCastClanBuffRangeCurse(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21430
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai2 = 0;
		npc._i_ai3 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (npc._i_ai3 == 0 && Rnd.get(100) < 33 && npc.getStatus().getHpRatio() > 0.5)
			{
				final L2Skill buff1 = getNpcSkillByType(npc, NpcSkillType.BUFF1);
				npc.getAI().addCastDesire(npc, buff1, 1000000);
				
				final L2Skill buff2 = getNpcSkillByType(npc, NpcSkillType.BUFF2);
				npc.getAI().addCastDesire(npc, buff2, 1000000);
				
				npc._i_ai3 = 1;
			}
			
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null && topDesireTarget != attacker)
			{
				final L2Skill rangeDebuff = getNpcSkillByType(npc, NpcSkillType.RANGE_DEBUFF);
				if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, rangeDebuff) <= 0)
				{
					if (npc.getCast().meetsHpMpConditions(attacker, rangeDebuff))
						npc.getAI().addCastDesire(npc, rangeDebuff, 1000000);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(attacker, 1000);
					}
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			if (called._i_ai3 == 0 && Rnd.get(100) < 33 && called.getStatus().getHpRatio() > 0.5)
			{
				final L2Skill buff1 = getNpcSkillByType(called, NpcSkillType.BUFF1);
				called.getAI().addCastDesire(called, buff1, 1000000);
				
				final L2Skill buff2 = getNpcSkillByType(called, NpcSkillType.BUFF2);
				called.getAI().addCastDesire(called, buff2, 1000000);
				
				called._i_ai3 = 1;
			}
			
			final L2Skill rangeDebuff = getNpcSkillByType(called, NpcSkillType.RANGE_DEBUFF);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, rangeDebuff) <= 0)
			{
				if (called.getCast().meetsHpMpConditions(attacker, rangeDebuff))
					called.getAI().addCastDesire(called, rangeDebuff, 1000000);
				else
				{
					called._i_ai0 = 1;
					
					called.getAI().addAttackDesire(attacker, 1000);
				}
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}