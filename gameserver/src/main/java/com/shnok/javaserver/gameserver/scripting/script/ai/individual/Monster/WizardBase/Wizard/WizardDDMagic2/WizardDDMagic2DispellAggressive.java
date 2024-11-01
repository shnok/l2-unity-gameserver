package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardDDMagic2DispellAggressive extends WizardDDMagic2Aggressive
{
	public WizardDDMagic2DispellAggressive()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardDDMagic2DispellAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21663,
		21686,
		21709,
		21732,
		21755,
		21778
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null && Rnd.get(100) < 33)
			{
				final L2Skill dispell = getNpcSkillByType(npc, NpcSkillType.DISPELL);
				if (npc.getCast().meetsHpMpConditions(attacker, dispell))
					npc.getAI().addCastDesire(attacker, dispell, 1000000);
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