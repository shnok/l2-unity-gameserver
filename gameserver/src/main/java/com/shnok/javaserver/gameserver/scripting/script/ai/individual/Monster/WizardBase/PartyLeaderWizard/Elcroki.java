package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyLeaderWizard;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Elcroki extends PartyLeaderWizard
{
	public Elcroki()
	{
		super("ai/individual/Monster/WizardBase/PartyLeaderWizard");
	}
	
	public Elcroki(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22214
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_quest0 = 0;
		
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (!npc.isInCombat())
		{
			if ((npc.getStatus().getHpRatio() * 100) >= getNpcIntAIParamOrDefault(npc, "HpChkRate2", 80))
			{
				if (npc._i_ai3 > 0)
				{
					broadcastScriptEvent(npc, 11039, 0, getNpcIntAIParamOrDefault(npc, "BroadCastRange", 300));
					
					npc._i_ai3 = 0;
					npc._i_ai4 = 0;
				}
				npc._i_quest0 = 0;
			}
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
		}
		super.onNoDesire(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final double hpRatio = npc.getStatus().getHpRatio();
		
		if (npc._i_quest0 == 0 && hpRatio * 100 <= getNpcIntAIParamOrDefault(npc, "HpChkRate3", 60))
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_BUFF1), 1000000);
		
		if (hpRatio * 100 <= getNpcIntAIParamOrDefault(npc, "HpChkRate5", 30) && Rnd.get(100) < getNpcIntAIParamOrDefault(npc, "ProbSelfRangeDeBuff1", 30))
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DEBUFF1), 1000000);
		
		if (Rnd.get(100) <= getNpcIntAIParamOrDefault(npc, "ProbCond4", 30))
			broadcastScriptEvent(npc, 10002, npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "BroadCastRange", 300));
		
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai0 == 0)
			{
				final HateList h0 = npc.getAI().getHateList();
				final int i0 = !h0.isEmpty() ? 1 : 0;
				
				if (npc.distance3D(attacker) > getNpcIntAIParamOrDefault(npc, "LongRangeSkillDist", 100))
				{
					if (i0 == 1 || Rnd.get(100) < 2)
					{
						if (npc.getCast().meetsHpMpConditions(attacker, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1)))
							npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 1000000);
						else
						{
							npc._i_ai0 = 1;
							
							npc.getAI().addAttackDesire(attacker, 1000);
						}
					}
				}
				else if (i0 == 1 || Rnd.get(100) < 2)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC1), 1000000);
				else if (Rnd.get(100) < 2)
				{
					if (npc.getCast().meetsHpMpConditions(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC1)))
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC1), 1000000);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(attacker, 1000);
					}
				}
			}
			else
			{
				double f0 = getHateRatio(npc, attacker);
				f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
				
				npc.getAI().addAttackDesire(attacker, f0 * 100);
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
			if (called.distance3D(attacker) > getNpcIntAIParamOrDefault(called, "LongRangeSkillDist", 100))
			{
				if (called.getCast().meetsHpMpConditions(attacker, getNpcSkillByType(called, NpcSkillType.LONG_RANGE_DD_MAGIC1)))
					called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.LONG_RANGE_DD_MAGIC1), 1000000);
				else
				{
					called._i_ai0 = 1;
					
					called.getAI().addAttackDesire(attacker, 1000);
				}
			}
			else
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.SELF_RANGE_DD_MAGIC1), 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		final HateList hateList = called.getAI().getHateList();
		hateList.refresh();
		
		if (target instanceof Playable && hateList.isEmpty())
		{
			if (called.distance3D(target) > getNpcIntAIParamOrDefault(called, "LongRangeSkillDist", 100))
			{
				if (called.getCast().meetsHpMpConditions(target, getNpcSkillByType(called, NpcSkillType.LONG_RANGE_DD_MAGIC1)))
					called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.LONG_RANGE_DD_MAGIC1), 1000000);
				else
				{
					called._i_ai0 = 1;
					
					called.getAI().addAttackDesire(target, 1000);
				}
			}
			else
				called.getAI().addCastDesire(target, getNpcSkillByType(called, NpcSkillType.SELF_RANGE_DD_MAGIC1), 1000000);
		}
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (success && skill == getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_BUFF1))
			npc._i_quest0 = 1;
		
		if (npc._i_ai0 != 1)
		{
			final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null)
			{
				if (npc.distance3D(mostHatedHI) > getNpcIntAIParamOrDefault(npc, "LongRangeSkillDist", 100))
				{
					if (npc.getCast().meetsHpMpConditions(mostHatedHI, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1)))
						npc.getAI().addCastDesire(mostHatedHI, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 1000000);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(mostHatedHI, 1000);
					}
				}
				npc.getAI().addCastDesire(mostHatedHI, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC1), 1000000);
			}
		}
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller != called)
		{
			if (called._i_ai3 > 0)
				called._i_ai3--;
			
			if (called.isInCombat() && called._i_ai3 < 2 && called._i_ai4 < getNpcIntAIParamOrDefault(called, "max_spawn_privates", 5))
			{
				final Creature topDesireTarget = called.getAI().getTopDesireTarget();
				final int silhouette1 = getNpcIntAIParamOrDefault(called, "silhouette1", 0);
				
				if (Rnd.get(2) == 0)
				{
					if (topDesireTarget != null)
						createOnePrivateEx(called, silhouette1, called.getX() + Rnd.get(100), called.getY() + Rnd.get(100), called.getZ(), 0, 0, false, 1000, topDesireTarget.getObjectId(), 0);
					else
						createOnePrivateEx(called, silhouette1, called.getX() + Rnd.get(100), called.getY() + Rnd.get(100), called.getZ(), 0, 0, false, 0, 0, 0);
					
					called._i_ai3++;
					called._i_ai4++;
				}
				
				if (topDesireTarget != null)
					createOnePrivateEx(called, silhouette1, called.getX() + Rnd.get(100), called.getY() + Rnd.get(100), called.getZ(), 0, 0, false, 1000, topDesireTarget.getObjectId(), 0);
				else
					createOnePrivateEx(called, silhouette1, called.getX() + Rnd.get(100), called.getY() + Rnd.get(100), called.getZ(), 0, 0, false, 0, 0, 0);
				
				called._i_ai3++;
				called._i_ai4++;
			}
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player && getNpcIntAIParam(npc, "ag_type") == 1 && npc.isInMyTerritory())
		{
			final HateList hateList = npc.getAI().getHateList();
			if (hateList.isEmpty())
			{
				final int silhouette1 = getNpcIntAIParam(npc, "silhouette1");
				final int silhouette2 = getNpcIntAIParam(npc, "silhouette2");
				final int maxSpawnPrivates = getNpcIntAIParamOrDefault(npc, "max_spawn_privates", 5);
				
				if (Rnd.get(2) == 0 && npc._i_ai3 < 2 && npc._i_ai4 < maxSpawnPrivates)
				{
					createOnePrivateEx(npc, silhouette1, npc.getX() + Rnd.get(100), npc.getY() + Rnd.get(100), npc.getZ(), npc.getHeading(), 0, false, 1000, creature.getObjectId(), 0);
					
					npc._i_ai3++;
					npc._i_ai4++;
				}
				else if (npc._i_ai3 < 2 && npc._i_ai4 < maxSpawnPrivates)
				{
					createOnePrivateEx(npc, silhouette2, npc.getX() + Rnd.get(100), npc.getY() + Rnd.get(100), npc.getZ(), npc.getHeading(), 0, false, 1000, creature.getObjectId(), 0);
					
					npc._i_ai3++;
					npc._i_ai4++;
				}
				
				if (Rnd.get(2) == 0 && npc._i_ai3 < 2 && npc._i_ai4 < maxSpawnPrivates)
				{
					createOnePrivateEx(npc, silhouette1, npc.getX() + Rnd.get(100), npc.getY() + Rnd.get(100), npc.getZ(), npc.getHeading(), 0, false, 1000, creature.getObjectId(), 0);
					
					npc._i_ai3++;
					npc._i_ai4++;
				}
				else if (npc._i_ai3 < 2 && npc._i_ai4 < maxSpawnPrivates)
				{
					createOnePrivateEx(npc, silhouette2, npc.getX() + Rnd.get(100), npc.getY() + Rnd.get(100), npc.getZ(), npc.getHeading(), 0, false, 1000, creature.getObjectId(), 0);
					
					npc._i_ai3++;
					npc._i_ai4++;
				}
				
				if (npc.distance3D(creature) > getNpcIntAIParamOrDefault(npc, "LongRangeSkillDist", 100))
				{
					if (npc.getCast().meetsHpMpConditions(creature, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1)))
						npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 1000000);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(creature, 1000);
					}
				}
				
				npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC1), 1000000);
				
				hateList.addDefaultHateInfo(creature);
				
				super.onSeeCreature(npc, creature);
			}
		}
	}
}