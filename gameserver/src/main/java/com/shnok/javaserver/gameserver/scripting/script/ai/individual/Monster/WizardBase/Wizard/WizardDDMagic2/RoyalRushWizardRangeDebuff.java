package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushWizardRangeDebuff extends WizardDDMagic2
{
	public RoyalRushWizardRangeDebuff()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public RoyalRushWizardRangeDebuff(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18139,
		18193,
		18228
	};
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called._i_ai0 == 0 && Rnd.get(100) < 33)
		{
			if (called.getCast().meetsHpMpConditions(attacker, getNpcSkillByType(called, NpcSkillType.W_RANGE_DEBUFF)))
			{
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.W_RANGE_DEBUFF), 1000000);
			}
			else
			{
				called._i_ai0 = 1;
				called.getAI().addAttackDesire(attacker, 1000);
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
