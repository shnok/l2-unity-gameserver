package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class RoyalRushWizardClanBuff extends WizardDDMagic2
{
	public RoyalRushWizardClanBuff()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public RoyalRushWizardClanBuff(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18138,
		18145,
		18171,
		18192,
		18227,
		21404
	};
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		called.getAI().getHateList().refresh();
		if ((called.getAI().getLifeTime() > 7 && attacker instanceof Playable) && called.getAI().getHateList().isEmpty())
		{
			if (called.getCast().meetsHpMpConditions(attacker, getNpcSkillByType(called, NpcSkillType.W_CLAN_BUFF)))
			{
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.W_CLAN_BUFF), 1000000);
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
