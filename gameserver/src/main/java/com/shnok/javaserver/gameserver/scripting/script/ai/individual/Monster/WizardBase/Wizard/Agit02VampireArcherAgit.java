package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.SiegeSummon;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Agit02VampireArcherAgit extends Wizard
{
	public Agit02VampireArcherAgit()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public Agit02VampireArcherAgit(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35634
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		final L2Skill wFiendArcher = getNpcSkillByType(npc, NpcSkillType.W_FIEND_ARCHER);
		if (npc.getCast().meetsHpMpConditions(creature, wFiendArcher))
			npc.getAI().addCastDesire(creature, wFiendArcher, 1000000, false);
		else
		{
			npc._i_ai0 = 1;
			
			npc.getAI().addAttackDesire(creature, 1000);
		}
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			double f0 = getHateRatio(npc, attacker);
			f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
			
			final HateList hateList = npc.getAI().getHateList();
			if (hateList.isEmpty())
				hateList.addHateInfo(attacker, (f0 * 100) + 300);
			else
				hateList.addHateInfo(attacker, f0 * 100);
			
			if (!hateList.isEmpty())
			{
				final L2Skill wFiendArcher = getNpcSkillByType(npc, NpcSkillType.W_FIEND_ARCHER);
				if (npc.getCast().meetsHpMpConditions(attacker, wFiendArcher))
					npc.getAI().addCastDesire(attacker, wFiendArcher, 1000000, false);
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(attacker, 1000);
				}
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final HateList hateList = called.getAI().getHateList();
		hateList.refresh();
		
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && hateList.isEmpty())
		{
			final L2Skill wFiendArcher = getNpcSkillByType(called, NpcSkillType.W_FIEND_ARCHER);
			if (called.getCast().meetsHpMpConditions(attacker, wFiendArcher))
				called.getAI().addCastDesire(attacker, wFiendArcher, 1000000, false);
			else
			{
				called._i_ai0 = 1;
				called.getAI().addAttackDesire(attacker, 1000);
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onStaticObjectClanAttacked(Door caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof SiegeSummon)
		{
			called.getAI().addAttackDesire(attacker.getActingPlayer(), 5000);
			called.getAI().addAttackDesire(attacker, 1000);
		}
		else if (attacker instanceof Playable)
		{
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05 * 50));
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		if (mostHatedHI != null)
		{
			final L2Skill wFiendArcher = getNpcSkillByType(npc, NpcSkillType.W_FIEND_ARCHER);
			if (npc.getCast().meetsHpMpConditions(mostHatedHI, wFiendArcher))
				npc.getAI().addCastDesire(mostHatedHI, wFiendArcher, 1000000, false);
			else
			{
				npc._i_ai0 = 1;
				npc.getAI().addAttackDesire(mostHatedHI, 1000);
			}
		}
		
		super.onUseSkillFinished(npc, creature, skill, success);
	}
}