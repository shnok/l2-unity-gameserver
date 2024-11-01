package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardFiendArcher;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.HateList;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardFiendArcherAggressive extends WizardFiendArcher
{
	public WizardFiendArcherAggressive()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardFiendArcher");
	}
	
	public WizardFiendArcherAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20819,
		20841,
		21617,
		21618,
		21085,
		21619
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
			final L2Skill wFiendArcher = getNpcSkillByType(npc, NpcSkillType.W_FIEND_ARCHER);
			if (npc.getCast().meetsHpMpConditions(creature, wFiendArcher))
				npc.getAI().addCastDesire(creature, wFiendArcher, 1000000, false);
			else
			{
				npc._i_ai0 = 1;
				
				npc.getAI().addAttackDesire(creature, 1000);
			}
			
			hateList.addDefaultHateInfo(creature);
		}
		super.onSeeCreature(npc, creature);
	}
}