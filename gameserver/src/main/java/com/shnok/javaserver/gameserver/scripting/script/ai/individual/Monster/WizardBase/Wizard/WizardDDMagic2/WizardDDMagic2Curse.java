package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardDDMagic2Curse extends WizardDDMagic2
{
	public WizardDDMagic2Curse()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardDDMagic2Curse(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20258,
		20781,
		20792,
		20556,
		21577,
		20644,
		21101,
		20581,
		20266,
		20587,
		20685,
		20067,
		22127
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null && getAbnormalLevel(attacker, skill) <= 0 && Rnd.get(100) < 33)
			{
				final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
				if (npc.getCast().meetsHpMpConditions(attacker, debuff))
					npc.getAI().addCastDesire(attacker, debuff, 1000000);
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
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			final Creature mostHatedHI = called.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null && getAbnormalLevel(attacker, skill) <= 0 && Rnd.get(100) < 33)
			{
				final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
				if (called.getCast().meetsHpMpConditions(attacker, debuff))
					called.getAI().addCastDesire(attacker, debuff, 1000000);
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