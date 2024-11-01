package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.HateList;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushWizardDDMagic2Hold extends Wizard
{
	public RoyalRushWizardDDMagic2Hold()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public RoyalRushWizardDDMagic2Hold(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18137,
		18170,
		18191,
		18226
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
			{
				final boolean isNullHate = npc.getAI().getHateList().isEmpty();
				if (npc.distance2D(attacker) > 100 && Rnd.get(100) < 80)
				{
					if (!isNullHate || Rnd.get(100) < 2)
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC), 1000000);
				}
				else if (!isNullHate || Rnd.get(100) < 2)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC), 1000000);
			}
			else
			{
				double f0 = getHateRatio(npc, attacker);
				f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
				npc.getAI().addAttackDesire(attacker, f0 * 100);
			}
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final HateList hateList = called.getAI().getHateList();
		hateList.refresh();
		
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && hateList.isEmpty())
		{
			if (caller.distance2D(attacker) > 100)
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.W_LONG_RANGE_DD_MAGIC), 1000000);
			else
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.W_SHORT_RANGE_DD_MAGIC), 1000000);
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		if (mostHatedHI != null && npc._i_ai0 != 1)
		{
			if (npc.distance2D(mostHatedHI) > 100)
				npc.getAI().addCastDesire(mostHatedHI, getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC), 1000000);
			else
				npc.getAI().addCastDesire(mostHatedHI, getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC), 1000000);
		}
	}
}