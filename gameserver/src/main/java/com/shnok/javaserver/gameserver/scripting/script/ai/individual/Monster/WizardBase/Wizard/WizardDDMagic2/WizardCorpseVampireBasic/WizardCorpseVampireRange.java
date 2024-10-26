package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2.WizardCorpseVampireBasic;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardCorpseVampireRange extends WizardCorpseVampireBasic
{
	public WizardCorpseVampireRange()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2/WizardCorpseVampireBasic");
	}
	
	public WizardCorpseVampireRange(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21591
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				final Party party0 = attacker.getParty();
				if (party0 != null)
				{
					if (Rnd.get(100) < 33)
					{
						final L2Skill wSelfRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SELF_RANGE_DD_MAGIC);
						if (npc.getCast().meetsHpMpConditions(attacker, wSelfRangeDDMagic))
							npc.getAI().addCastDesire(attacker, wSelfRangeDDMagic, 1000000, false);
						else
						{
							npc._i_ai0 = 1;
							
							npc.getAI().addAttackDesire(attacker, 1000);
						}
					}
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
}