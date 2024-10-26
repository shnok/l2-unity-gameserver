package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class RoyalRushHealer2 extends Wizard
{
	public RoyalRushHealer2()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public RoyalRushHealer2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18186,
		18221
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			if (Rnd.get(100) < 33)
			{
				final L2Skill wSelfRangeDebuff = getNpcSkillByType(npc, NpcSkillType.W_SELF_RANGE_DEBUFF);
				if (npc.getCast().meetsHpMpConditions(npc, wSelfRangeDebuff))
					npc.getAI().addCastDesire(npc, wSelfRangeDebuff, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(attacker, 1000);
				}
			}
			
			if (Rnd.get(100) < 33 && npc.distance2D(attacker) > 100)
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
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		if (mostHatedHI != null && npc._i_ai0 != 1)
		{
			if (Rnd.get(100) < 33 && npc.distance2D(mostHatedHI) > 100)
			{
				final L2Skill wLongRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(npc, wLongRangeDDMagic))
					npc.getAI().addCastDesire(mostHatedHI, wLongRangeDDMagic, 1000000, false);
			}
			else
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