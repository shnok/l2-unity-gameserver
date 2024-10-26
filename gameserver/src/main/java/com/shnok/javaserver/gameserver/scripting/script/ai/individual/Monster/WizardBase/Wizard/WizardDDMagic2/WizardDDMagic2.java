package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.Wizard;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardDDMagic2 extends Wizard
{
	public WizardDDMagic2()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardDDMagic2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20092,
		20331,
		20426,
		20427,
		20428,
		20429,
		21257,
		21268,
		21281,
		21289,
		21681,
		21704,
		21727,
		21750,
		21773,
		21796,
		21024,
		20269,
		21277,
		20197,
		21110,
		20265,
		20059,
		20055,
		21640,
		21592,
		22021,
		22036,
		22044,
		22047,
		22081,
		22082,
		22034,
		22042,
		18147
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
			{
				int i0 = 0;
				
				final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
				if (mostHatedHI != null)
					i0 = 1;
				
				if (npc.distance2D(attacker) > 100 && Rnd.get(100) < 80)
				{
					if (i0 == 1 || Rnd.get(100) < 2)
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
				else if (i0 == 1 || Rnd.get(100) < 2)
				{
					final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
					if (npc.getCast().meetsHpMpConditions(attacker, wShortRangeDDMagic))
						npc.getAI().addCastDesire(attacker, wShortRangeDDMagic, 1000000, false);
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
				f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
				
				npc.getAI().addAttackDesire(attacker, f0 * 100);
			}
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final HateList hateList = called.getAI().getHateList();
		hateList.refresh();
		
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && hateList.isEmpty())
		{
			if (called.distance2D(attacker) > 100)
			{
				final L2Skill wLongRangeDDMagic = getNpcSkillByType(called, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
				if (called.getCast().meetsHpMpConditions(attacker, wLongRangeDDMagic))
					called.getAI().addCastDesire(attacker, wLongRangeDDMagic, 1000000, false);
				else
				{
					called._i_ai0 = 1;
					
					called.getAI().addAttackDesire(attacker, 1000);
				}
			}
			else
			{
				final L2Skill wShortRangeDDMagic = getNpcSkillByType(called, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
				if (called.getCast().meetsHpMpConditions(attacker, wShortRangeDDMagic))
					called.getAI().addCastDesire(attacker, wShortRangeDDMagic, 1000000, false);
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
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (npc._i_ai0 != 1)
		{
			final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null)
			{
				if (npc.distance2D(mostHatedHI) > 100)
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
				else
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
			}
		}
	}
}