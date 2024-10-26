package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyPrivateWizard;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyPrivateWizardDD2Curse extends PartyPrivateWizardDD2
{
	public PartyPrivateWizardDD2Curse()
	{
		super("ai/individual/Monster/WizardBase/PartyPrivateWizard");
	}
	
	public PartyPrivateWizardDD2Curse(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20972
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null)
			{
				final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
				if (getAbnormalLevel(attacker, debuff) <= 0 && Rnd.get(100) < 33)
				{
					if (npc.getCast().meetsHpMpConditions(attacker, debuff))
						npc.getAI().addCastDesire(attacker, debuff, 1000000);
					else
					{
						npc._i_ai0 = 1;
						npc.getAI().addAttackDesire(attacker, 1000);
					}
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final HateList hateList = called.getAI().getHateList();
		
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && hateList.isEmpty())
		{
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
			{
				if (called.getCast().meetsHpMpConditions(attacker, debuff))
					called.getAI().addCastDesire(attacker, debuff, 1000000);
				else
				{
					called._i_ai0 = 1;
					called.getAI().addAttackDesire(attacker, 1000);
				}
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}