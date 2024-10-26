package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardFiendArcherRunAway extends Wizard
{
	public WizardFiendArcherRunAway()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public WizardFiendArcherRunAway(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21514,
		21509
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai3 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		if (topDesireTarget != null)
		{
			npc._c_ai0 = topDesireTarget;
			
			if (npc.isRooted() && npc.distance2D(topDesireTarget) > 40)
			{
				if (npc._i_ai3 == 1)
				{
					npc._i_ai3 = 3;
					
					npc.removeAllDesire();
				}
			}
		}
		else
			npc._c_ai0 = attacker;
		
		final Location fleeLoc = npc.getSpawn().getFleeLocation();
		if (fleeLoc != null && npc._i_ai3 == 0 && npc.getStatus().getHpRatio() < 0.5 && Rnd.get(100) < 10)
		{
			npc._i_ai3 = 1;
			
			npc.removeAllDesire();
			npc.getAI().addMoveToDesire(fleeLoc, 2000);
			return;
		}
		
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
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc._i_ai3 == 2 && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				if (npc._c_ai0 != null)
				{
					npc.getAI().addAttackDesire(npc._c_ai0, 1000);
					
					broadcastScriptEvent(npc, 10000, npc._c_ai0.getObjectId(), 400);
				}
				npc._i_ai3 = 3;
			}
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			if (npc._i_ai0 == 2)
			{
				if (npc.getStatus().getMaxHp() == npc.getStatus().getHp())
					npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 5000);
				else
					startQuestTimer("2002", npc, null, 1000);
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		if (mostHatedHI != null)
		{
			if (npc.distance2D(mostHatedHI) < 100)
				npc.getAI().addFleeDesire(mostHatedHI, Config.MAX_DRIFT_RANGE, 1000);
			
			final L2Skill wFiendArcher = getNpcSkillByType(npc, NpcSkillType.W_FIEND_ARCHER);
			if (npc.getCast().meetsHpMpConditions(mostHatedHI, wFiendArcher))
				npc.getAI().addCastDesire(mostHatedHI, wFiendArcher, 1000000, false);
			else
			{
				npc._i_ai0 = 1;
				
				npc.getAI().addAttackDesire(mostHatedHI, 1000);
			}
		}
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		if (npc._i_ai3 == 1)
		{
			final Location fleeLoc = npc.getSpawn().getFleeLocation();
			if (fleeLoc.equals(x, y, z))
			{
				npc.getAI().getDesireQueue().clear();
				npc.getAI().addWanderDesire(5, 50);
				
				npc._i_ai3 = 2;
				
				if (Rnd.get(100) < 50)
					startQuestTimer("2001", npc, null, 1000);
				else
					startQuestTimer("2002", npc, null, 1000);
			}
		}
		else if (npc._i_ai3 == 3)
		{
			if (npc.getSpawnLocation().equals(x, y, z))
				npc._i_ai3 = 0;
		}
		else
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
		}
	}
}