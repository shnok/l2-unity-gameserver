package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class WizardHereticDDMagic2CurseAggressive extends WizardHereticDDMagic2Curse
{
	public WizardHereticDDMagic2CurseAggressive()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardHereticDDMagic2CurseAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22153,
		22191,
		22192
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
		{
			super.onSeeCreature(npc, creature);
			return;
		}
		if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory() && npc.getAI().getHateList().size() == 0)
		{
			if (npc.distance2D(creature) > 100)
			{
				if (npc.getCast().meetsHpMpConditions(creature, getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC)))
				{
					npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC), 1000000);
				}
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(creature, 1000);
				}
			}
			else if (npc.getCast().meetsHpMpConditions(creature, getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC)))
			{
				npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC), 1000000);
			}
			else
			{
				npc._i_ai0 = 1;
				npc.getAI().addAttackDesire(creature, 1000);
			}
			
			npc.getAI().getHateList().addDefaultHateInfo(creature);
			
			super.onSeeCreature(npc, creature);
		}
	}
}
