package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.HateList;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardDDMagic2CurseAggressive extends WizardDDMagic2Curse
{
	public WizardDDMagic2CurseAggressive()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardDDMagic2CurseAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20068,
		20817,
		21106,
		20564,
		20809,
		21406,
		21645,
		21144,
		21072,
		21156,
		21148,
		21160,
		21307,
		22121
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
		
		if (npc.getAI().getLifeTime() > 7 && hateList.isEmpty() && npc.isInMyTerritory())
		{
			if (npc.distance2D(creature) > 100)
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
			else
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
			
			hateList.addDefaultHateInfo(creature);
			
			super.onSeeCreature(npc, creature);
		}
	}
}