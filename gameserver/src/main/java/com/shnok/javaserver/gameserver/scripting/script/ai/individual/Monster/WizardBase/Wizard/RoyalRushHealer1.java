package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushHealer1 extends Wizard
{
	public RoyalRushHealer1()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public RoyalRushHealer1(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18132,
		18185,
		18220
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			if (Rnd.get(100) < 33)
			{
				final L2Skill wRangeDebuff = getNpcSkillByType(npc, NpcSkillType.W_RANGE_DEBUFF);
				if (npc.getCast().meetsHpMpConditions(attacker, wRangeDebuff))
					npc.getAI().addCastDesire(attacker, wRangeDebuff, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(attacker, 1000);
				}
			}
			else
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
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33)
		{
			final L2Skill wRangeHeal = getNpcSkillByType(called, NpcSkillType.W_RANGE_DEBUFF);
			if (called.getCast().meetsHpMpConditions(attacker, wRangeHeal))
				called.getAI().addCastDesire(attacker, wRangeHeal, 1000000, false);
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
				final L2Skill wSelfRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SELF_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(npc, wSelfRangeDDMagic))
					npc.getAI().addCastDesire(npc, wSelfRangeDDMagic, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(mostHatedHI, 1000);
				}
			}
		}
	}
}