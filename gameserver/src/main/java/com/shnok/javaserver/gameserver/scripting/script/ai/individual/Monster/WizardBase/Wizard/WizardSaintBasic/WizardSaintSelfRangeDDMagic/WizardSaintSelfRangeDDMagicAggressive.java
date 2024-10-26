package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardSaintBasic.WizardSaintSelfRangeDDMagic;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardSaintSelfRangeDDMagicAggressive extends WizardSaintSelfRangeDDMagic
{
	public WizardSaintSelfRangeDDMagicAggressive()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardSaintBasic/WizardSaintSelfRangeDDMagic");
	}
	
	public WizardSaintSelfRangeDDMagicAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21535,
		21529
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
				final L2Skill wMiddleRangeMagic = getNpcSkillByType(npc, NpcSkillType.W_MIDDLE_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(creature, wMiddleRangeMagic))
					npc.getAI().addCastDesire(creature, wMiddleRangeMagic, 1000000, false);
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
}