package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardSaintBasic;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.Wizard;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardSaintBasic extends Wizard
{
	public WizardSaintBasic()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardSaintBasic");
	}
	
	public WizardSaintBasic(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21521,
		21536,
		21520,
		21530,
		21533
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai4 = 0;
		
		final L2Skill buff = getNpcSkillByType(npc, NpcSkillType.BUFF);
		npc.getAI().addCastDesire(npc, buff, 1000000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		double f0 = getHateRatio(npc, attacker);
		f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
		
		if (attacker instanceof Playable)
		{
			if (npc.getAI().getHateList().isEmpty())
				npc.getAI().getHateList().addHateInfo(attacker, (f0 * 100) + 300);
			else
				npc.getAI().getHateList().addHateInfo(attacker, f0 * 100);
		}
		
		if (getNpcIntAIParam(npc, "silhouette") != 0 && npc._i_ai4 == 0)
		{
			final double hpRatio = npc.getStatus().getHpRatio();
			if (hpRatio < 0.7 && hpRatio > 0.5 && Rnd.get(100) < 30)
			{
				npc._i_ai4 = 1;
				npc._c_ai0 = attacker;
				
				createOnePrivateEx(npc, getNpcIntAIParam(npc, "silhouette"), npc.getX(), npc.getY(), npc.getY(), npc.getHeading(), 0, false, 1000, attacker.getObjectId(), 0);
				
				npc.deleteMe();
				return;
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
			{
				final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
				if (mostHatedHI != null)
				{
					final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
					if (npc.getCast().meetsHpMpConditions(attacker, wShortRangeDDMagic))
						npc.getAI().addCastDesire(attacker, wShortRangeDDMagic, 1000000, false);
					else
					{
						npc._i_ai0 = 1;
						npc.getAI().addAttackDesire(attacker, 1000);
					}
					
					final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
					if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
					{
						if (npc.getCast().meetsHpMpConditions(attacker, debuff))
							npc.getAI().addCastDesire(attacker, debuff, 1000000);
						else
						{
							npc._i_ai0 = 1;
							npc.getAI().addAttackDesire(attacker, 1000);
						}
					}
				}
			}
			else
				npc.getAI().addAttackDesire(attacker, f0 * 100);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getHateList().isEmpty())
		{
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			final L2Skill wShortRangeDDMagic = getNpcSkillByType(called, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
			
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
			{
				if (called.getCast().meetsHpMpConditions(attacker, debuff))
					called.getAI().addCastDesire(attacker, debuff, 1000000, false);
				else
				{
					called._i_ai0 = 1;
					called.getAI().addAttackDesire(attacker, 1000);
				}
			}
			else if (called.getCast().meetsHpMpConditions(attacker, wShortRangeDDMagic))
				called.getAI().addCastDesire(attacker, wShortRangeDDMagic, 1000000, false);
			else
			{
				called._i_ai0 = 1;
				called.getAI().addAttackDesire(attacker, 1000);
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		if (mostHatedHI != null)
		{
			if (npc._i_ai0 == 0)
			{
				final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(mostHatedHI, wShortRangeDDMagic))
					npc.getAI().addCastDesire(mostHatedHI, wShortRangeDDMagic, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(mostHatedHI, 1000);
				}
				
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
			}
			else if (mostHatedHI instanceof Playable)
				npc.getAI().addAttackDesire(mostHatedHI, 1000);
		}
	}
}