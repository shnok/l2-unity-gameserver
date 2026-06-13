package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.ClassType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterAI;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class WarriorBase extends MonsterAI
{
	public WarriorBase()
	{
		super("ai/individual/Monster/WarriorBase");
	}
	
	public WarriorBase(String descr)
	{
		super(descr);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1"))
		{
			if (getNpcIntAIParam(npc, "AttackLowLevel") == 1)
				npc.lookNeighbor();
		}
		else if (name.equalsIgnoreCase("2"))
		{
			if (getNpcIntAIParam(npc, "IsVs") == 1)
				npc._c_ai0 = npc;
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (getNpcIntAIParam(npc, "IsVs") == 1)
			npc._c_ai0 = npc;
		
		if (npc._param1 == 1000)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(npc._param2);
			if (c0 != null)
			{
				npc.getAI().addCastDesire(c0, 4663, 1, 10000);
				npc.getAI().addAttackDesire(c0, 500);
			}
		}
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (getNpcIntAIParam(npc, "CreviceOfDiminsion") != 0 && !npc.getSpawn().isInMyTerritory(attacker))
		{
			npc.getAI().getAggroList().stopHate(attacker);
			return;
		}
		
		if (!npc.isStunned() && !npc.isParalyzed())
		{
			final int ssRate = getNpcIntAIParam(npc, "SoulShotRate");
			if (ssRate != 0 && Rnd.get(100) < ssRate)
				npc.rechargeShots(true, false);
			
			final int spsRate = getNpcIntAIParam(npc, "SpiritShotRate");
			if (spsRate != 0 && Rnd.get(100) < spsRate)
				npc.rechargeShots(false, true);
		}
		
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
		final double hpRatio = npc.getStatus().getHpRatio();
		final double attackerHpRatio = attacker.getStatus().getHpRatio();
		
		if (getNpcIntAIParam(npc, "AttackLowLevel") == 1 && currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
			startQuestTimer("1", npc, null, 7000);
		
		if (mostHated != null && mostHated != attacker)
		{
			if (getNpcIntAIParam(npc, "AttackLowHP") == 1 && attackerHpRatio <= 0.3 && Rnd.get(100) < 10)
			{
				npc.removeAllAttackDesire();
				
				if (attacker instanceof Playable)
					npc.getAI().addAttackDesire(attacker, (1 * 100));
				
				switch (Rnd.get(3))
				{
					case 0:
						npc.broadcastNpcSay(NpcStringId.ID_1000307);
						break;
					
					case 1:
						npc.broadcastNpcSay(NpcStringId.ID_1000427);
						break;
					
					case 2:
						npc.broadcastNpcSay(NpcStringId.ID_1000428);
						break;
				}
			}
			
			if (getNpcIntAIParam(npc, "IsVs") == 1 && attacker instanceof Player && npc._c_ai0 == npc)
			{
				switch (Rnd.get(5))
				{
					case 0:
						npc.broadcastNpcSay(NpcStringId.ID_1000288, attacker.getName());
						break;
					
					case 1:
						npc.broadcastNpcSay(NpcStringId.ID_1000388, attacker.getName());
						break;
					
					case 2:
						npc.broadcastNpcSay(NpcStringId.ID_1000389);
						break;
					
					case 3:
						npc.broadcastNpcSay(NpcStringId.ID_1000390);
						break;
					
					case 4:
						npc.broadcastNpcSay(NpcStringId.ID_1000391);
						break;
				}
				
				npc._c_ai0 = attacker;
				
				startQuestTimer("2", npc, null, 20000);
				
				broadcastScriptEvent(npc, 10001, attacker.getObjectId(), 600);
			}
		}
		
		final L2Skill specialSkill = getNpcSkillByType(npc, NpcSkillType.SPECIAL_SKILL);
		if (specialSkill != null && hpRatio <= 0.3 && Rnd.get(100) < 10 && getAbnormalLevel(npc, specialSkill) <= 0)
		{
			switch (Rnd.get(4))
			{
				case 0:
					npc.broadcastNpcSay(NpcStringId.ID_1000290);
					break;
				
				case 1:
					npc.broadcastNpcSay(NpcStringId.ID_1000395);
					break;
				
				case 2:
					npc.broadcastNpcSay(NpcStringId.ID_1000396);
					break;
				
				case 3:
					npc.broadcastNpcSay(NpcStringId.ID_1000397);
					break;
			}
			
			npc.getAI().addCastDesire(npc, specialSkill, 1000000);
		}
		
		if (attacker instanceof Player)
		{
			final int helpHeroSilhouetteId = getNpcIntAIParam(npc, "HelpHeroSilhouette");
			if (helpHeroSilhouetteId != 0 && attackerHpRatio <= 0.2 && Rnd.get(100) < 3)
				createOnePrivateEx(npc, helpHeroSilhouetteId, npc.getX() + 80, npc.getY() + 80, npc.getZ(), npc.getHeading(), 0, false, 0, 0, npc.getObjectId());
		}
		
		if (mostHated != null && mostHated == attacker && getNpcIntAIParam(npc, "ShoutTarget") != 0 && Rnd.get(100) < 5)
			broadcastScriptEvent(npc, 10016, attacker.getObjectId(), 300);
		
		final L2Skill selfExplosion = getNpcSkillByType(npc, NpcSkillType.SELF_EXPLOSION);
		if (selfExplosion != null && hpRatio < 0.5 && Rnd.get(100) < 5 && (10 - (int) (hpRatio * 10) > Rnd.get(100)))
			npc.getAI().addCastDesire(npc, selfExplosion, 1000000);
		
		final int isTransform = getNpcIntAIParam(npc, "IsTransform");
		if (isTransform > 0)
		{
			switch (npc._param3)
			{
				case 0:
					if (npc._param3 < isTransform && hpRatio < 0.7 && hpRatio > 0.5 && Rnd.get(100) < 30)
					{
						switch (Rnd.get(3))
						{
							case 0:
								npc.broadcastNpcSay(NpcStringId.ID_1000406);
								break;
							
							case 1:
								npc.broadcastNpcSay(NpcStringId.ID_1000407);
								break;
							
							case 2:
								npc.broadcastNpcSay(NpcStringId.ID_1000408);
								break;
						}
						
						createOnePrivateEx(npc, getNpcIntAIParam(npc, "step1"), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), 0, false, 1000, attacker.getObjectId(), 1);
						npc.deleteMe();
					}
					break;
				
				case 1:
					if (npc._param3 < isTransform && hpRatio < 0.5 && hpRatio > 0.3 && Rnd.get(100) < 20)
					{
						switch (Rnd.get(3))
						{
							case 0:
								npc.broadcastNpcSay(NpcStringId.ID_1000409);
								break;
							
							case 1:
								npc.broadcastNpcSay(NpcStringId.ID_1000410);
								break;
							
							case 2:
								npc.broadcastNpcSay(NpcStringId.ID_1000411);
								break;
						}
						
						createOnePrivateEx(npc, getNpcIntAIParam(npc, "step2"), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), 0, false, 1000, attacker.getObjectId(), 2);
						npc.deleteMe();
					}
					break;
				
				case 2:
					if (npc._param3 < isTransform && hpRatio < 0.3 && hpRatio > 0.05 && Rnd.get(100) < 10)
					{
						switch (Rnd.get(3))
						{
							case 0:
								npc.broadcastNpcSay(NpcStringId.ID_1000412);
								break;
							
							case 1:
								npc.broadcastNpcSay(NpcStringId.ID_1000413);
								break;
							
							case 2:
								npc.broadcastNpcSay(NpcStringId.ID_1000414);
								break;
						}
						
						createOnePrivateEx(npc, getNpcIntAIParam(npc, "step3"), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), 0, false, 1000, attacker.getObjectId(), 3);
						npc.deleteMe();
					}
					break;
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final Creature mostHated = called.getAI().getAggroList().getMostHatedCreature();
		if (mostHated != null && mostHated != attacker && getNpcIntAIParam(called, "AttackLowHP") == 1 && attacker.getStatus().getHpRatio() < 0.3 && Rnd.get(100) < 3)
		{
			called.removeAllAttackDesire();
			
			if (attacker instanceof Playable)
				called.getAI().addAttackDesire(attacker, 100);
		}
		
		final IntentionType currentIntention = called.getAI().getCurrentIntention().getType();
		if (getNpcIntAIParam(called, "AttackLowLevel") == 1 && currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
			startQuestTimer("1", called, null, 7000);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable) || npc.isDead())
			return;
		
		final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
		
		switch (getNpcIntAIParam(npc, "HalfAggressive"))
		{
			case 1:
				if (currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST && GameTimeTaskManager.getInstance().getGameHour() >= 5)
					tryToAttack(npc, creature);
				
				return;
			
			case 2:
				if (currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST && GameTimeTaskManager.getInstance().getGameHour() < 5)
					tryToAttack(npc, creature);
				
				return;
			
			default:
				final int randomAggressive = getNpcIntAIParam(npc, "RandomAggressive");
				if (randomAggressive > 0)
				{
					if (creature instanceof Player && Rnd.get(100) < randomAggressive)
					{
						if (currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
							tryToAttack(npc, creature);
						
						return;
					}
					else if (currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
						npc.removeAllAttackDesire();
				}
				break;
		}
		
		if (getNpcIntAIParam(npc, "AttackLowLevel") == 1 && ((currentIntention == IntentionType.ATTACK || currentIntention == IntentionType.CAST) && npc.isIn2DRadius(creature, 300)))
		{
			if ((creature.getStatus().getLevel() + 15) < npc.getStatus().getLevel())
			{
				npc.removeAllAttackDesire();
				npc.getAI().addAttackDesire(creature, (7 * 100));
			}
			
			startQuestTimer("1", npc, null, 7000);
		}
		
		if (creature instanceof Player && currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
		{
			if (getNpcIntAIParam(npc, "IsVs") == 1)
			{
				final int levelDifference = Math.abs(creature.getStatus().getLevel() - npc.getStatus().getLevel());
				if (levelDifference <= 2)
				{
					switch (Rnd.get(5))
					{
						case 0:
							npc.broadcastNpcSay(NpcStringId.ID_1000287, creature.getName());
							break;
						
						case 1:
							npc.broadcastNpcSay(NpcStringId.ID_1000384, creature.getName());
							break;
						
						case 2:
							npc.broadcastNpcSay(NpcStringId.ID_1000385, creature.getName());
							break;
						
						case 3:
							npc.broadcastNpcSay(NpcStringId.ID_1000386, creature.getName());
							break;
						
						case 4:
							npc.broadcastNpcSay(NpcStringId.ID_1000387, creature.getName());
							break;
					}
					
					tryToAttack(npc, creature);
				}
			}
			
			if (getNpcIntAIParam(npc, "DaggerBackAttack") == 1 && Rnd.get(100) < 50 && npc.isIn2DRadius(creature, 100) && npc.isBehind(creature))
			{
				switch (Rnd.get(4))
				{
					case 0:
						npc.broadcastNpcSay(NpcStringId.ID_1000286, creature.getName());
						break;
					
					case 1:
						npc.broadcastNpcSay(NpcStringId.ID_1000381, creature.getName());
						break;
					
					case 2:
						npc.broadcastNpcSay(NpcStringId.ID_1000382);
						break;
					
					case 3:
						npc.broadcastNpcSay(NpcStringId.ID_1000383);
						break;
				}
				
				tryToAttack(npc, creature);
			}
		}
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
		
		if ((caster.getStatus().getLevel() + 15) < npc.getStatus().getLevel())
		{
			npc.removeAllAttackDesire();
			
			if (caster == mostHated)
			{
				if (currentIntention == IntentionType.ATTACK && (skill.getAggroPoints() > 0 || skill.getPower(npc) > 0 || skill.isOffensive()))
				{
					double skillPower = Math.max(Math.max(skill.getAggroPoints(), skill.getPower(npc)), 20);
					double hateRatio = getHateRatio(npc, caster);
					hateRatio = (((1.0 * skillPower) / (npc.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * skillPower) / (npc.getStatus().getLevel() + 7))));
					
					npc.getAI().addAttackDesire(caster, hateRatio * 150);
				}
				
				if (npc.getStatus().getHpRatio() != 1.0 && npc.getMove().getGeoPathFailCount() >= 10)
				{
					npc.abortAll(false);
					npc.instantTeleportTo(caster.getX(), caster.getY(), caster.getZ(), 0);
				}
			}
		}
		
		if (mostHated instanceof Player mostHatedPlayer && mostHatedPlayer.getClassId().getType() != ClassType.MYSTIC && currentIntention == IntentionType.ATTACK)
		{
			final int swapPosition = getNpcIntAIParam(npc, "SwapPosition");
			if (swapPosition != 0 && Rnd.get(100) < swapPosition)
			{
				final double casterDistance = npc.distance2D(caster);
				if (npc.distance2D(mostHated) < casterDistance && casterDistance < 900)
				{
					final Location casterLoc = caster.getPosition().clone();
					final Location mostHatedLoc = mostHated.getPosition().clone();
					
					caster.abortAll(false);
					caster.instantTeleportTo(mostHatedLoc, 0);
					
					mostHated.abortAll(false);
					mostHated.instantTeleportTo(casterLoc, 0);
					
					npc.getAI().addAttackDesire(caster, 1000);
				}
			}
		}
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (npc.isDead())
			return;
		
		if (eventId == 10001)
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc._c_ai0 != (Creature) World.getInstance().getObject(arg1))
			{
				switch (Rnd.get(3))
				{
					case 0:
						npc.broadcastNpcSay(NpcStringId.ID_1000392);
						break;
					
					case 1:
						npc.broadcastNpcSay(NpcStringId.ID_1000393);
						break;
					
					case 2:
						npc.broadcastNpcSay(NpcStringId.ID_1000394);
						break;
				}
				
				npc.getAI().addAttackDesire((Creature) World.getInstance().getObject(arg1), 1000000);
			}
		}
		else if (eventId == 10016)
		{
			if (Rnd.get(100) < 50)
			{
				npc.removeAllAttackDesire();
				
				final Creature c0 = (Creature) World.getInstance().getObject(arg1);
				if (c0 instanceof Playable)
					npc.getAI().addAttackDesire(c0, 100);
			}
		}
		else if (eventId == 10020)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 instanceof Playable)
				npc.getAI().addAttackDesire(c0, 100);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		final L2Skill selfExplosion = getNpcSkillByType(npc, NpcSkillType.SELF_EXPLOSION);
		if (selfExplosion != null && skill.getId() == selfExplosion.getId())
			npc.doDie(npc);
	}
}