package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyLeaderWizard;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderWizardDD2 extends PartyLeaderWizard
{
	public PartyLeaderWizardDD2()
	{
		super("ai/individual/Monster/WizardBase/PartyLeaderWizard");
	}
	
	public PartyLeaderWizardDD2(String descr)
	{
		super(descr);
	}
	
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
				
				if ((npc.distance2D(attacker) > 100 && Rnd.get(100) < 80))
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
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		final HateList hateList = called.getAI().getHateList();
		hateList.refresh();
		
		if (target instanceof Playable && hateList.isEmpty())
		{
			if (called.distance2D(target) > 100)
			{
				final L2Skill wLongRangeDDMagic = getNpcSkillByType(called, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
				if (called.getCast().meetsHpMpConditions(target, wLongRangeDDMagic))
					called.getAI().addCastDesire(target, wLongRangeDDMagic, 1000000, false);
				else
				{
					called._i_ai0 = 1;
					
					called.getAI().addAttackDesire(target, 1000);
				}
			}
			else
			{
				final L2Skill wShortRangeDDMagic = getNpcSkillByType(called, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
				if (called.getCast().meetsHpMpConditions(target, wShortRangeDDMagic))
					called.getAI().addCastDesire(target, wShortRangeDDMagic, 1000000, false);
				else
				{
					called._i_ai0 = 1;
					
					called.getAI().addAttackDesire(target, 1000);
				}
			}
		}
		super.onPartyAttacked(caller, called, target, damage);
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