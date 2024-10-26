package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.MonsterAI;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardBase extends MonsterAI
{
	public WizardBase()
	{
		super("ai/individual/Monster/WizardBase");
	}
	
	public WizardBase(String descr)
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
		if (npc._param1 == 1000)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(npc._param2);
			if (c0 != null)
			{
				npc.getAI().addCastDesire(c0, 4663, 1, 1000000);
				npc.getAI().getHateList().addHateInfo(c0, 500);
			}
		}
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.getSpawn().getMemo().getInteger("CreviceOfDiminsion", 0) != 0 && !npc.getSpawn().isInMyTerritory(attacker))
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
		
		final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
		final double hpRatio = npc.getStatus().getHpRatio();
		final double attackerHpRatio = attacker.getStatus().getHpRatio();
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		
		if (getNpcIntAIParam(npc, "AttackLowLevel") == 1 && currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
			startQuestTimer("1", npc, null, 7000);
		
		if (attacker instanceof Playable && mostHatedHI != null && mostHatedHI != attacker && getNpcIntAIParam(npc, "AttackLowHP") == 1 && attackerHpRatio <= 0.3 && Rnd.get(100) < 10)
		{
			npc.getAI().getHateList().clear();
			
			double f0 = getHateRatio(npc, attacker);
			f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
			
			if (npc.getAI().getHateList().isEmpty())
				npc.getAI().getHateList().addHateInfo(attacker, (f0 * 100) + 300);
			else
				npc.getAI().getHateList().addHateInfo(attacker, (f0 * 100));
		}
		
		if (attacker instanceof Player)
		{
			final int helpHeroSilhouetteId = getNpcIntAIParam(npc, "HelpHeroSilhouette");
			if (helpHeroSilhouetteId != 0 && attackerHpRatio <= 0.2 && Rnd.get(100) < 3)
				createOnePrivateEx(npc, helpHeroSilhouetteId, npc.getX() + 80, npc.getY() + 80, npc.getZ(), npc.getHeading(), 0, false, 0, 0, npc.getObjectId());
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
		
		if (mostHatedHI != null && mostHatedHI == attacker && getNpcIntAIParam(npc, "ShoutTarget") != 0 && Rnd.get(100) < 5)
			broadcastScriptEvent(npc, 10016, attacker.getObjectId(), 300);
		
		final L2Skill selfExplosion = getNpcSkillByType(npc, NpcSkillType.SELF_EXPLOSION);
		if (selfExplosion != null && hpRatio < 0.5 && Rnd.get(100) < 5 && (10 - (int) (hpRatio * 10) > Rnd.get(100)))
			npc.getAI().addCastDesire(npc, selfExplosion, 3000000);
		
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
		if (getNpcIntAIParam(called, "AttackLowHP") == 1 && attacker.getStatus().getHpRatio() < 0.3 && Rnd.get(100) < 3)
		{
			final Creature mostHatedHI = called.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null && mostHatedHI != attacker)
			{
				called.getAI().getHateList().clear();
				
				if (attacker instanceof Playable)
				{
					double f0 = getHateRatio(called, attacker);
					f0 = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
					
					if (called.getAI().getHateList().isEmpty())
						called.getAI().getHateList().addHateInfo(attacker, (f0 * 100) + 300);
					else
						called.getAI().getHateList().addHateInfo(attacker, (f0 * 100));
				}
			}
		}
		
		final IntentionType currentIntention = called.getAI().getCurrentIntention().getType();
		if (getNpcIntAIParam(called, "AttackLowLevel") == 1 && currentIntention != IntentionType.ATTACK && currentIntention != IntentionType.CAST)
			startQuestTimer("1", called, null, 7000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		if (npc.isDead())
			return;
		
		if (getNpcIntAIParam(npc, "AttackLowLevel") == 1)
		{
			final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
			if ((currentIntention == IntentionType.ATTACK || currentIntention == IntentionType.CAST) && npc.distance2D(creature) < 300)
			{
				if ((creature.getStatus().getLevel() + 15) < npc.getStatus().getLevel())
				{
					npc.getAI().getHateList().clear();
					npc.getAI().getHateList().addHateInfo(creature, (7 * 100));
				}
				
				startQuestTimer("1", npc, null, 7000);
			}
		}
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if ((caster.getStatus().getLevel() + 15) < npc.getStatus().getLevel())
		{
			npc.getAI().getHateList().clear();
			
			if (skill.getAggroPoints() > 0 || skill.getPower(npc) > 0 || skill.isOffensive())
			{
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
				final double skillPower = Math.max(Math.max(skill.getAggroPoints(), skill.getPower(npc)), 20);
				
				double f0 = getHateRatio(npc, caster);
				f0 = (((1.0 * skillPower) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * skillPower) / (npc.getStatus().getLevel() + 7))));
				
				if (mostHated != null && mostHated == caster && currentIntention == IntentionType.ATTACK)
					npc.getAI().getHateList().addHateInfo(caster, (f0 * 150));
				else
					npc.getAI().getHateList().addHateInfo(caster, (f0 * 75));
			}
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10016)
		{
			if (Rnd.get(100) < 50)
			{
				npc.getAI().getHateList().clear();
				
				final Creature c0 = (Creature) World.getInstance().getObject(arg1);
				if (c0 instanceof Playable)
					npc.getAI().getHateList().addHateInfo(c0, 200);
			}
		}
		else if (eventId == 10020)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 != null)
				npc.lookNeighbor();
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