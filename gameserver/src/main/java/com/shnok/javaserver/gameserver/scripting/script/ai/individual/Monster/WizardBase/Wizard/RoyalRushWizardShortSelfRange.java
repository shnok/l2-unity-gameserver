package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.HateList;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushWizardShortSelfRange extends Wizard
{
	public RoyalRushWizardShortSelfRange()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public RoyalRushWizardShortSelfRange(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18140,
		18172,
		18194,
		18229
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
			{
				final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
				
				if (!npc.getAI().getHateList().isEmpty())
				{
					if (Rnd.get(100) < 33)
					{
						final L2Skill wSelfRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SELF_RANGE_DD_MAGIC);
						if (npc.getCast().meetsHpMpConditions(attacker, wSelfRangeDDMagic))
							npc.getAI().addCastDesire(npc, wSelfRangeDDMagic, 1000000);
						else
						{
							npc._i_ai0 = 1;
							npc.getAI().addAttackDesire(attacker, 1000);
						}
					}
					else if (npc.getCast().meetsHpMpConditions(attacker, wShortRangeDDMagic))
						npc.getAI().addCastDesire(attacker, wShortRangeDDMagic, 1000000);
					else
					{
						npc._i_ai0 = 1;
						npc.getAI().addAttackDesire(attacker, 1000);
					}
				}
				else if (Rnd.get(100) < 2)
				{
					if (npc.getCast().meetsHpMpConditions(attacker, wShortRangeDDMagic))
						npc.getAI().addCastDesire(attacker, wShortRangeDDMagic, 1000000);
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
			final L2Skill wShortRangeDdMagic = getNpcSkillByType(called, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
			if (called.getCast().meetsHpMpConditions(attacker, wShortRangeDdMagic))
				called.getAI().addCastDesire(attacker, wShortRangeDdMagic, 1000000);
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
		if (npc._i_ai0 != 1)
		{
			final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null)
			{
				final L2Skill wShortRangeDdMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(mostHatedHI, wShortRangeDdMagic))
					npc.getAI().addCastDesire(mostHatedHI, wShortRangeDdMagic, 1000000);
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(mostHatedHI, 1000);
				}
			}
		}
	}
}