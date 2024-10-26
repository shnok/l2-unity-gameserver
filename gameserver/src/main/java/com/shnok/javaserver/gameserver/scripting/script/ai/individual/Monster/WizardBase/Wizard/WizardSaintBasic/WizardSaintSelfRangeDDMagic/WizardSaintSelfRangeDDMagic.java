package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardSaintBasic.WizardSaintSelfRangeDDMagic;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardSaintBasic.WizardSaintBasic;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardSaintSelfRangeDDMagic extends WizardSaintBasic
{
	public WizardSaintSelfRangeDDMagic()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardSaintBasic/WizardSaintSelfRangeDDMagic");
	}
	
	public WizardSaintSelfRangeDDMagic(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21650
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Party party0 = attacker.getParty();
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
		if ((party0 != null || attacker != topDesireTarget) && npc.distance2D(attacker) < 40 && Rnd.get(100) < 33)
		{
			final L2Skill wSelfRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SELF_RANGE_DD_MAGIC);
			if (npc.getCast().meetsHpMpConditions(npc, wSelfRangeDDMagic))
				npc.getAI().addCastDesire(npc, wSelfRangeDDMagic, 1000000, false);
			else
			{
				npc._i_ai0 = 1;
				
				npc.getAI().addAttackDesire(attacker, 1000);
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
			{
				if (npc.distance2D(attacker) > 100 && Rnd.get(100) < 80 && (!npc.getAI().getHateList().isEmpty() || Rnd.get(100) < 2))
				{
					final L2Skill wMiddleRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_MIDDLE_RANGE_DD_MAGIC);
					if (npc.getCast().meetsHpMpConditions(attacker, wMiddleRangeDDMagic))
						npc.getAI().addCastDesire(attacker, wMiddleRangeDDMagic, 1000000, false);
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
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		if (mostHatedHI != null)
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
					npc.getAI().addCastDesire(mostHatedHI, wShortRangeDDMagic, 1000000);
				else
				{
					npc._i_ai0 = 1;
					
					npc.getAI().addAttackDesire(mostHatedHI, 1000);
				}
			}
			else
			{
				final L2Skill wMiddleRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_MIDDLE_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(mostHatedHI, wMiddleRangeDDMagic))
					npc.getAI().addCastDesire(mostHatedHI, wMiddleRangeDDMagic, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					
					npc.getAI().addAttackDesire(mostHatedHI, 1000);
				}
			}
		}
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
				
				if (npc.distance2D(mostHatedHI) < 100)
					npc.getAI().addFleeDesire(mostHatedHI, Config.MAX_DRIFT_RANGE, 1000000);
				else if (Rnd.get(100) < 33 && getAbnormalLevel(mostHatedHI, debuff) <= 0)
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
					final L2Skill wMiddleRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_MIDDLE_RANGE_DD_MAGIC);
					if (npc.getCast().meetsHpMpConditions(mostHatedHI, wMiddleRangeDDMagic))
						npc.getAI().addCastDesire(mostHatedHI, wMiddleRangeDDMagic, 1000000, false);
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