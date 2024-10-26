package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardHereticPriestAggressive extends Wizard
{
	public WizardHereticPriestAggressive()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public WizardHereticPriestAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22143,
		22151
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, attacker.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		if (attacker instanceof Playable)
		{
			final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
			final L2Skill wLongRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
			final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
			
			if (npc._i_ai0 == 0)
			{
				int i0 = npc.getAI().getHateList().isEmpty() ? 0 : 1;
				if (getNpcIntAIParam(npc, "Hold") == 1)
				{
					if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
					{
						npc.getAI().addCastDesireHold(attacker, debuff, 1000000);
					}
					if (npc.distance2D(attacker) > 40 && Rnd.get(100) < 80)
					{
						if (i0 == 1 || Rnd.get(100) < 2)
						{
							if (npc.getCast().meetsHpMpConditions(npc, wLongRangeDDMagic))
							{
								npc.getAI().addCastDesireHold(attacker, wLongRangeDDMagic, 1000000);
							}
							else
							{
								npc._i_ai0 = 1;
								if (npc.canAutoAttack(attacker))
								{
									npc.getAI().addAttackDesireHold(attacker, 1000);
								}
							}
						}
					}
					else if (i0 == 1 || Rnd.get(100) < 2)
					{
						if (npc.getCast().meetsHpMpConditions(npc, wShortRangeDDMagic))
						{
							npc.getAI().addCastDesireHold(attacker, wShortRangeDDMagic, 1000000);
						}
						else
						{
							npc._i_ai0 = 1;
							if (npc.canAutoAttack(attacker))
							{
								npc.getAI().addAttackDesireHold(attacker, 1000);
							}
						}
					}
				}
				else if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
				{
					npc.getAI().addCastDesireHold(attacker, debuff, 1000000);
				}
				if (npc.distance2D(attacker) > 100 && Rnd.get(100) < 80)
				{
					if (i0 == 1 || Rnd.get(100) < 2)
					{
						if (npc.getCast().meetsHpMpConditions(npc, wLongRangeDDMagic))
						{
							npc.getAI().addCastDesire(attacker, wLongRangeDDMagic, 1000000);
						}
						else
						{
							npc._i_ai0 = 1;
							if (npc.canAutoAttack(attacker))
							{
								npc.getAI().addAttackDesire(attacker, 1000);
							}
						}
					}
				}
				else if (i0 == 1 || Rnd.get(100) < 2)
				{
					if (npc.getCast().meetsHpMpConditions(npc, wShortRangeDDMagic))
					{
						npc.getAI().addCastDesire(attacker, wShortRangeDDMagic, 1000000);
					}
					else
					{
						npc._i_ai0 = 1;
						if (npc.canAutoAttack(attacker))
						{
							npc.getAI().addAttackDesire(attacker, 1000);
						}
					}
				}
			}
			else
			{
				double f0 = getHateRatio(npc, attacker);
				f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
				if (getNpcIntAIParam(npc, "Hold") == 1)
				{
					npc.getAI().addAttackDesireHold(attacker, f0 * 100);
				}
				else
				{
					npc.getAI().addAttackDesire(attacker, f0 * 100);
				}
			}
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		called.getAI().getHateList().refresh();
		
		if ((called.getAI().getLifeTime() > 7 && attacker instanceof Player) && called.getAI().getHateList().size() == 0)
		{
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			final L2Skill wLongRangeDDMagic = getNpcSkillByType(called, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
			final L2Skill wShortRangeDDMagic = getNpcSkillByType(called, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
			
			if (getNpcIntAIParam(called, "Hold") == 1)
			{
				if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
				{
					called.getAI().addCastDesireHold(attacker, debuff, 1000000);
				}
				if (called.distance2D(attacker) > 40 && Rnd.get(100) < 80)
				{
					if (called.getCast().meetsHpMpConditions(called, wLongRangeDDMagic))
					{
						called.getAI().addCastDesireHold(attacker, wLongRangeDDMagic, 1000000);
					}
					else
					{
						called._i_ai0 = 1;
						if (called.canAutoAttack(attacker))
						{
							called.getAI().addAttackDesireHold(attacker, 1000);
						}
					}
				}
				else if (called.getCast().meetsHpMpConditions(called, wShortRangeDDMagic))
				{
					called.getAI().addCastDesireHold(attacker, wShortRangeDDMagic, 1000000);
				}
				else
				{
					called._i_ai0 = 1;
					if (called.canAutoAttack(attacker))
					{
						called.getAI().addAttackDesireHold(attacker, 1000);
					}
				}
			}
			else if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
			{
				called.getAI().addCastDesireHold(attacker, debuff, 1000000);
			}
			if (called.distance2D(attacker) > 100 && Rnd.get(100) < 80)
			{
				if (called.getCast().meetsHpMpConditions(called, wLongRangeDDMagic))
				{
					called.getAI().addCastDesire(attacker, wLongRangeDDMagic, 1000000);
				}
				else
				{
					called._i_ai0 = 1;
					if (called.canAutoAttack(attacker))
					{
						called.getAI().addAttackDesire(attacker, 1000);
					}
				}
			}
			else if (called.getCast().meetsHpMpConditions(called, wShortRangeDDMagic))
			{
				called.getAI().addCastDesire(attacker, wShortRangeDDMagic, 1000000);
			}
			else
			{
				called._i_ai0 = 1;
				if (called.canAutoAttack(attacker))
				{
					called.getAI().addAttackDesire(attacker, 1000);
				}
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
		final L2Skill wLongRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
		final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
		
		if (npc._i_ai0 == 0)
		{
			int i0 = npc.getAI().getHateList().isEmpty() ? 0 : 1;
			if (getNpcIntAIParam(npc, "Hold") == 1)
			{
				if (Rnd.get(100) < 33 && getAbnormalLevel(creature, debuff) <= 0)
				{
					npc.getAI().addCastDesireHold(creature, debuff, 1000000);
				}
				if (npc.distance2D(creature) > 40 && Rnd.get(100) < 80)
				{
					if (i0 == 1 || Rnd.get(100) < 2)
					{
						if (npc.getCast().meetsHpMpConditions(npc, wLongRangeDDMagic))
						{
							npc.getAI().addCastDesireHold(creature, wLongRangeDDMagic, 1000000);
						}
						else
						{
							npc._i_ai0 = 1;
							if (npc.canAutoAttack(creature))
							{
								npc.getAI().addAttackDesireHold(creature, 1000);
							}
						}
					}
				}
				else if (i0 == 1 || Rnd.get(100) < 2)
				{
					if (npc.getCast().meetsHpMpConditions(npc, wShortRangeDDMagic))
					{
						npc.getAI().addCastDesireHold(creature, wShortRangeDDMagic, 1000000);
					}
					else
					{
						npc._i_ai0 = 1;
						if (npc.canAutoAttack(creature))
						{
							npc.getAI().addAttackDesireHold(creature, 1000);
						}
					}
				}
			}
			else if (Rnd.get(100) < 33 && getAbnormalLevel(creature, debuff) <= 0)
			{
				npc.getAI().addCastDesireHold(creature, debuff, 1000000);
			}
			if (npc.distance2D(creature) > 100 && Rnd.get(100) < 80)
			{
				if (i0 == 1 || Rnd.get(100) < 2)
				{
					if (npc.getCast().meetsHpMpConditions(npc, wLongRangeDDMagic))
					{
						npc.getAI().addCastDesire(creature, wLongRangeDDMagic, 1000000);
					}
					else
					{
						npc._i_ai0 = 1;
						if (npc.canAutoAttack(creature))
						{
							npc.getAI().addAttackDesire(creature, 1000);
						}
					}
				}
			}
			else if (i0 == 1 || Rnd.get(100) < 2)
			{
				if (npc.getCast().meetsHpMpConditions(npc, wShortRangeDDMagic))
				{
					npc.getAI().addCastDesire(creature, wShortRangeDDMagic, 1000000);
				}
				else
				{
					npc._i_ai0 = 1;
					if (npc.canAutoAttack(creature))
					{
						npc.getAI().addAttackDesire(creature, 1000);
					}
				}
			}
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable) || npc.isDead())
		{
			return;
		}
		
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, creature.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory() && npc.getAI().getHateList().isEmpty())
		{
			final L2Skill wLongRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
			final L2Skill wShortRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
			
			if (getNpcIntAIParam(npc, "Hold") == 1)
			{
				if (npc.distance2D(creature) > 40 && Rnd.get(100) < 80)
				{
					if (npc.getCast().meetsHpMpConditions(creature, wLongRangeDDMagic))
					{
						npc.getAI().addCastDesireHold(creature, wLongRangeDDMagic, 1000000);
					}
					else
					{
						npc._i_ai0 = 1;
						if (npc.canAutoAttack(creature))
						{
							npc.getAI().addAttackDesireHold(creature, 1000);
						}
					}
				}
				else if (npc.getCast().meetsHpMpConditions(creature, wShortRangeDDMagic))
				{
					npc.getAI().addCastDesireHold(creature, wShortRangeDDMagic, 1000000);
				}
				else
				{
					npc._i_ai0 = 1;
					if (npc.canAutoAttack(creature))
					{
						npc.getAI().addAttackDesireHold(creature, 1000);
					}
				}
			}
			else if (npc.distance2D(creature) > 100 && Rnd.get(100) < 80)
			{
				if (npc.getCast().meetsHpMpConditions(creature, wLongRangeDDMagic))
				{
					npc.getAI().addCastDesire(creature, wLongRangeDDMagic, 1000000);
				}
				else
				{
					npc._i_ai0 = 1;
					if (npc.canAutoAttack(creature))
					{
						npc.getAI().addAttackDesire(creature, 1000);
					}
				}
			}
			else if (npc.getCast().meetsHpMpConditions(creature, wShortRangeDDMagic))
			{
				npc.getAI().addCastDesire(creature, wShortRangeDDMagic, 1000000);
			}
			else
			{
				npc._i_ai0 = 1;
				npc.getAI().addAttackDesire(creature, 1000);
			}
			
			npc.getAI().getHateList().addDefaultHateInfo(creature);
		}
		
		final int playerClassID = creature.getActingPlayer().getClassId().getId();
		final boolean isInClericGroup = ClassId.isInGroup(creature.getActingPlayer(), "@cleric_group");
		int i0 = Rnd.get(100);
		
		if (i0 < 33 && (playerClassID == 5 || playerClassID == 90 || isInClericGroup))
		{
			npc.getAI().addCastDesireHold(creature, getNpcSkillByType(npc, NpcSkillType.HOLD_MAGIC), 1000000);
			
			final L2Skill wLongRangeDDMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
			
			if (npc.getCast().meetsHpMpConditions(creature, wLongRangeDDMagic))
			{
				npc.getAI().addCastDesireHold(creature, wLongRangeDDMagic, 1000000);
			}
			else
			{
				npc._i_ai0 = 1;
				if (npc.canAutoAttack(creature))
				{
					npc.getAI().addAttackDesireHold(creature, 1000);
				}
			}
		}
		i0 = Rnd.get(100);
		if (i0 < 33 && creature.getParty() != null && creature.getParty().getMembersCount() > getNpcIntAIParam(npc, "party_members") && creature instanceof Player && getNpcIntAIParam(npc, "NoPCTeleport") == 0 && npc.getSpawn().isInMyTerritory(creature))
		{
			final Creature c0 = creature.getActingPlayer().getSummon();
			final int i1 = Rnd.get(3);
			final int TelPosX1 = getNpcIntAIParam(npc, "TelPosX1");
			final int TelPosX2 = getNpcIntAIParam(npc, "TelPosX2");
			final int TelPosX3 = getNpcIntAIParam(npc, "TelPosX3");
			final int TelPosY1 = getNpcIntAIParam(npc, "TelPosY1");
			final int TelPosY2 = getNpcIntAIParam(npc, "TelPosY2");
			final int TelPosY3 = getNpcIntAIParam(npc, "TelPosY3");
			final int TelPosZ1 = getNpcIntAIParam(npc, "TelPosZ1");
			final int TelPosZ2 = getNpcIntAIParam(npc, "TelPosZ2");
			final int TelPosZ3 = getNpcIntAIParam(npc, "TelPosZ3");
			
			if (i1 == 0)
			{
				creature.teleportTo(TelPosX1, TelPosY1, TelPosZ1, 0);
				if (c0 != null)
				{
					c0.teleportTo(TelPosX1, TelPosY1, TelPosZ1, 0);
				}
			}
			else if (i1 == 1)
			{
				creature.teleportTo(TelPosX2, TelPosY2, TelPosZ2, 0);
				if (c0 != null)
				{
					c0.teleportTo(TelPosX2, TelPosY2, TelPosZ2, 0);
				}
			}
			else
			{
				creature.teleportTo(TelPosX3, TelPosY3, TelPosZ3, 0);
				if (c0 != null)
				{
					c0.teleportTo(TelPosX3, TelPosY3, TelPosZ3, 0);
				}
			}
		}
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onOutOfTerritory(Npc npc)
	{
		if (getNpcIntAIParam(npc, "Hold") == 0)
		{
			npc.removeAllAttackDesire();
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
			npc.getAI().getHateList().cleanAllHate();
			broadcastScriptEvent(npc, 10035, 0, getNpcIntAIParamOrDefault(npc, "DistNoDesire", 500));
		}
	}
}
