package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardSaintBasic.WizardSaintDDMagic2;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardSaintRangeDDMagic2Aggressive extends WizardSaintDDMagic2
{
	public WizardSaintRangeDDMagic2Aggressive()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardSaintBasic/WizardSaintDDMagic2");
	}
	
	public WizardSaintRangeDDMagic2Aggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21532,
		21523
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
		{
			super.onSeeCreature(npc, creature);
			return;
		}
		
		final HateList hateList = npc.getAI().getHateList();
		
		if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory() && hateList.isEmpty())
		{
			if (npc.distance2D(creature) < 40)
			{
				final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(creature, wShortRangeDDMagic))
					npc.getAI().addCastDesire(creature, wShortRangeDDMagic, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					
					npc.getAI().addAttackDesire(creature, 1000);
				}
			}
			else
			{
				final L2Skill wLongRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(creature, wLongRangeDDMagic))
					npc.getAI().addCastDesire(creature, wLongRangeDDMagic, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					
					npc.getAI().addAttackDesire(creature, 1000);
				}
			}
			
			hateList.addDefaultHateInfo(creature);
		}
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Party party0 = attacker.getParty();
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
		if ((party0 != null || topDesireTarget != attacker) && npc.distance2D(attacker) > 100)
		{
			if (Rnd.get(100) < 33)
			{
				final L2Skill wLongRangeDDMagic2 = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC2);
				if (npc.getCast().meetsHpMpConditions(attacker, wLongRangeDDMagic2))
					npc.getAI().addCastDesire(attacker, wLongRangeDDMagic2, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					
					npc.getAI().addAttackDesire(attacker, 1000);
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
}