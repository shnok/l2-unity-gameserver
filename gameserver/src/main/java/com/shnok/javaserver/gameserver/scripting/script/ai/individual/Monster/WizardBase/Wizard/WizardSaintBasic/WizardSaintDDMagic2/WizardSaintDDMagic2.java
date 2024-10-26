package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardSaintBasic.WizardSaintDDMagic2;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardSaintBasic.WizardSaintBasic;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardSaintDDMagic2 extends WizardSaintBasic
{
	public WizardSaintDDMagic2()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardSaintBasic/WizardSaintDDMagic2");
	}
	
	public WizardSaintDDMagic2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21537,
		21527
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
			{
				if (!npc.getAI().getHateList().isEmpty() && npc.distance2D(attacker) > 100 && Rnd.get(100) < 80)
				{
					final L2Skill wLongRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
					if (npc.getCast().meetsHpMpConditions(attacker, wLongRangeDDMagic))
						npc.getAI().addCastDesire(attacker, wLongRangeDDMagic, 1000000, false);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(attacker, 1000);
					}
				}
			}
			else
			{
				double f0 = getHateRatio(npc, attacker);
				f0 = (((1. * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1. * damage) / (npc.getStatus().getLevel() + 7))));
				
				npc.getAI().addAttackDesire(attacker, f0 * 100);
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		if (mostHatedHI != null)
		{
			if (npc._i_ai0 == 0)
			{
				final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
				if (Rnd.get(100) < 33 && getAbnormalLevel(mostHatedHI, debuff) <= 0)
				{
					if (npc.getCast().meetsHpMpConditions(mostHatedHI, debuff))
						npc.getAI().addCastDesire(mostHatedHI, debuff, 1000000);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(mostHatedHI, 1000);
					}
				}
				
				if (Rnd.get(100) < 33 && npc.distance2D(mostHatedHI) < 40)
				{
					final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
					if (npc.getCast().meetsHpMpConditions(mostHatedHI, wShortRangeDDMagic))
						npc.getAI().addCastDesire(mostHatedHI, wShortRangeDDMagic, 1000000, false);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(mostHatedHI, 1000);
					}
				}
				else
				{
					final L2Skill wLongRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
					if (npc.getCast().meetsHpMpConditions(mostHatedHI, wLongRangeDDMagic))
						npc.getAI().addCastDesire(mostHatedHI, wLongRangeDDMagic, 1000000, false);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(mostHatedHI, 1000);
					}
				}
			}
			else
				npc.getAI().addAttackDesire(mostHatedHI, 1000);
		}
	}
}