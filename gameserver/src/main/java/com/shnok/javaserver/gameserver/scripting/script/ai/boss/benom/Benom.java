package com.shnok.javaserver.gameserver.scripting.script.ai.boss.benom;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossStandard;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Benom extends RaidBossStandard
{
	// Important : the heading is used as offset.
	private static final SpawnLocation[] TARGET_TELEPORTS =
	{
		new SpawnLocation(12860, -49158, -976, 650),
		new SpawnLocation(14878, -51339, 1024, 100),
		new SpawnLocation(15674, -49970, 864, 100),
		new SpawnLocation(15696, -48326, 864, 100),
		new SpawnLocation(14873, -46956, 1024, 100),
		new SpawnLocation(12157, -49135, -1088, 650),
		new SpawnLocation(12875, -46392, -288, 200),
		new SpawnLocation(14087, -46706, -288, 200),
		new SpawnLocation(14086, -51593, -288, 200),
		new SpawnLocation(12864, -51898, -288, 200),
		new SpawnLocation(15538, -49153, -1056, 200),
		new SpawnLocation(17001, -49149, -1064, 650)
	};
	
	private final List<Creature> _targets = new ArrayList<>();
	
	public Benom()
	{
		super("ai/boss/benom");
	}
	
	public Benom(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29054 // benom
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastNpcShout(NpcStringId.ID_1010623);
		
		_targets.clear();
		
		npc._i_quest0 = 0;
		npc._i_quest1 = 0;
		
		npc._i_ai0 = 0;
		npc._i_ai1 = npc.getX();
		npc._i_ai2 = npc.getY();
		npc._i_ai3 = npc.getZ();
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			npc._i_ai1 = npc.getX();
			npc._i_ai2 = npc.getY();
			npc._i_ai3 = npc.getZ();
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			if (!npc.isInMyTerritory() && Rnd.get(2) < 1 && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				npc.getAI().getAggroList().cleanAllHate();
				npc.teleportTo(11563, -49152, -537, 0);
				npc.getAI().addMoveToDesire(new Location(11563, -49152, -537), 1000000);
			}
			
			if (Rnd.get(5) < 1)
				npc.getAI().getAggroList().randomizeAttack();
			
			startQuestTimer("1002", npc, null, 120000);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
			npc.getAI().addAttackDesire(attacker, (((((double) damage) / npc.getStatus().getMaxHp()) / 0.05) * 2000));
		
		if (npc._i_ai0 == 1)
		{
			if (Rnd.get((25 * 25)) < 1)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_CANCEL_A1), 1000000, false);
			
			if (npc.getStatus().getHpRatio() < 0.333 && Rnd.get((25 * 25)) < 1)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_CANCEL_A2), 1000000, false);
		}
		
		if (npc.distance2D(attacker) > 300 && Rnd.get((25 * 10)) < 1)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
		
		if (Rnd.get((25 * 10)) < 1)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		if (npc._i_quest1 == 1)
			npc.getAI().addAttackDesire(creature, 200);
		
		if (creature instanceof Player && npc._i_ai0 == 1 && npc._i_quest0 < 10 && Rnd.get(3) < 1 && !_targets.contains(creature))
		{
			_targets.add(creature);
			
			npc._i_quest0++;
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill.getId() == 4222 && npc._i_ai0 == 1)
		{
			npc.teleportTo(npc._i_ai1, npc._i_ai2, npc._i_ai3, 0);
			npc.getAI().getAggroList().cleanAllHate();
		}
		else if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_CANCEL_A1))
		{
			final SpawnLocation loc = Rnd.get(TARGET_TELEPORTS);
			creature.teleportTo(loc, loc.getHeading());
			
			npc.getAI().getAggroList().stopHate(creature);
		}
		else if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_CANCEL_A2))
		{
			SpawnLocation loc = Rnd.get(TARGET_TELEPORTS);
			creature.teleportTo(loc, loc.getHeading());
			
			npc.getAI().getAggroList().stopHate(creature);
			
			if (npc._i_quest0 > 0)
			{
				for (Creature c : _targets)
				{
					if (c != creature && creature.isIn3DRadius(c, 250))
					{
						loc = Rnd.get(TARGET_TELEPORTS);
						creature.teleportTo(loc, loc.getHeading());
						
						npc.getAI().getAggroList().stopHate(c);
					}
				}
			}
			
			npc._i_quest0 = 0;
			
			_targets.clear();
			
			npc.lookNeighbor();
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10100)
		{
			npc.teleportTo(11563, -49152, -537, 0);
			
			npc._i_ai0 = arg1;
			npc._i_quest1 = 1;
			
			startQuestTimer("1002", npc, null, 120000);
			startQuestTimer("1001", npc, null, 5000);
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		_targets.clear();
		
		npc.broadcastNpcShout(NpcStringId.ID_1010626);
		
		if (npc._i_ai0 == 0)
			createOnePrivateEx(npc, 29055, 12589, -49044, -3008, 0, 0, true);
	}
}