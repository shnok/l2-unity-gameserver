package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorHereticPriestAggressive extends Warrior
{
	public WarriorHereticPriestAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorHereticPriestAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22142,
		22146,
		22175
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
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, attacker.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
		if (getNpcIntAIParam(npc, "Hold") == 0)
		{
			if (attacker instanceof Playable)
			{
				if (topDesireTarget != null)
				{
					if (topDesireTarget == attacker)
					{
						if (npc.distance2D(attacker) <= 40)
						{
							if (Rnd.get(100) < 33)
							{
								npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
							}
						}
						else if (npc.distance2D(attacker) < 500)
						{
							npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
						}
						else
						{
							npc.removeAttackDesire(topDesireTarget);
							if (npc.distance2D(attacker) <= 40)
							{
								if (Rnd.get(100) < 33)
								{
									npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
								}
							}
							else if (npc.distance2D(attacker) < 500)
							{
								npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
							}
						}
					}
				}
			}
			super.onAttacked(npc, attacker, damage, null);
		}
		else if (attacker instanceof Playable)
		{
			if (topDesireTarget != null)
			{
				if (topDesireTarget == attacker)
				{
					if (npc.distance2D(attacker) <= 40)
					{
						if (Rnd.get(100) < 33)
						{
							npc.getAI().addCastDesireHold(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
						}
					}
					else if (npc.distance2D(attacker) < 500)
					{
						npc.getAI().addCastDesireHold(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
					}
					else
					{
						npc.removeAttackDesire(topDesireTarget);
						if (npc.distance2D(attacker) <= 40)
						{
							if (Rnd.get(100) < 33)
							{
								npc.getAI().addCastDesireHold(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
							}
						}
						else if (npc.distance2D(attacker) < 500)
						{
							npc.getAI().addCastDesireHold(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
						}
					}
				}
				
				npc.getAI().addAttackDesireHold(attacker, 100);
			}
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final Creature topDesireTarget = called.getAI().getTopDesireTarget();
		
		if (getNpcIntAIParam(called, "Hold") == 0)
		{
			if (attacker instanceof Playable)
			{
				if (topDesireTarget != null)
				{
					if (topDesireTarget == attacker)
					{
						if (called.distance2D(attacker) <= 40)
						{
							if (Rnd.get(100) < 33)
							{
								called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
							}
						}
						else if (called.distance2D(attacker) < 500)
						{
							called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
						}
						else
						{
							called.removeAttackDesire(topDesireTarget);
							if (called.distance2D(attacker) <= 40)
							{
								if (Rnd.get(100) < 33)
								{
									called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
								}
							}
							else if (called.distance2D(attacker) < 500)
							{
								called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
							}
						}
					}
				}
				else if (called.distance2D(attacker) <= 40)
				{
					if (Rnd.get(100) < 33)
					{
						called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
					}
				}
				else if (called.distance2D(attacker) < 500)
				{
					called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
				}
			}
			super.onClanAttacked(caller, called, attacker, damage, skill);
		}
		else if (called.getAI().getLifeTime() > 7 && attacker instanceof Playable && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && called.getAI().getCurrentIntention().getType() != IntentionType.CAST)
		{
			if (called.distance2D(attacker) <= 40)
			{
				if (Rnd.get(100) < 33)
				{
					called.getAI().addCastDesireHold(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
				}
			}
			else if (called.distance2D(attacker) < 500)
			{
				called.getAI().addCastDesireHold(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
			}
			
			called.getAI().addAttackDesireHold(attacker, 100);
		}
		else if (topDesireTarget != null)
		{
			if (attacker != topDesireTarget)
			{
				if (called.distance2D(topDesireTarget) > 500)
				{
					called.removeAttackDesire(topDesireTarget);
					if (called.distance2D(attacker) <= 40)
					{
						if (Rnd.get(100) < 33)
						{
							called.getAI().addCastDesireHold(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
						}
					}
					else if (called.distance2D(attacker) < 500)
					{
						called.getAI().addCastDesireHold(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
					}
				}
			}
		}
		if (attacker instanceof Playable)
		{
			called.getAI().addAttackDesireHold(attacker, 100);
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			broadcastScriptEventEx(npc, 10033, creature.getObjectId(), npc.getObjectId(), getNpcIntAIParamOrDefault(npc, "HelpCastRange", 500));
		
		if (npc.distance2D(creature) <= 40)
		{
			npc.getAI().addCastDesireHold(creature, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		}
		else if (npc.distance2D(creature) < 500)
		{
			npc.getAI().addCastDesireHold(creature, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
		}
		
		if (!(creature instanceof Playable) || npc.isDead())
			return;
		
		if (getNpcIntAIParam(npc, "Hold") == 1)
		{
			if (npc.isInMyTerritory())
				npc.getAI().addAttackDesireHold(creature, 200);
		}
		else if (npc.isInMyTerritory())
		{
			npc.getAI().addAttackDesire(creature, 200);
		}
		
		int i0 = Rnd.get(100);
		
		final int playerClassID = creature.getActingPlayer().getClassId().getId();
		final boolean isInClericGroup = ClassId.isInGroup(creature.getActingPlayer(), "@cleric_group");
		
		if (i0 < 33 && creature instanceof Player && (playerClassID == 5 || playerClassID == 90 || isInClericGroup))
		{
			npc.getAI().addCastDesireHold(creature, getNpcSkillByType(npc, NpcSkillType.HOLD_MAGIC), 1000000);
			if (getNpcIntAIParam(npc, "Hold") == 0)
			{
				if (npc.distance2D(creature) <= 40)
				{
					if (Rnd.get(100) < 33)
					{
						npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
					}
				}
				else if (npc.distance2D(creature) < 500)
				{
					npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
				}
			}
			else if (npc.distance2D(creature) <= 40)
			{
				if (Rnd.get(100) < 33)
				{
					npc.getAI().addCastDesireHold(creature, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
				}
			}
			else if (npc.distance2D(creature) < 500)
			{
				npc.getAI().addCastDesireHold(creature, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
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
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (creature instanceof Playable)
		{
			if (getNpcIntAIParam(npc, "Hold") == 1)
			{
				npc.getAI().addAttackDesireHold(creature, (100));
			}
			else
			{
				npc.getAI().addAttackDesire(creature, (100));
			}
		}
	}
	
	@Override
	public void onOutOfTerritory(Npc npc)
	{
		if (getNpcIntAIParam(npc, "Hold") == 0)
		{
			npc.removeAllAttackDesire();
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 100);
			broadcastScriptEvent(npc, 10035, 0, getNpcIntAIParamOrDefault(npc, "DistNoDesire", 500));
		}
	}
}
