package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderWarrior extends Warrior
{
	private static final NpcStringId[] SUMMON_PRIVATES_SHOUTS =
	{
		NpcStringId.ID_1000294,
		NpcStringId.ID_1000403,
		NpcStringId.ID_1000404,
		NpcStringId.ID_1000405
	};
	
	private static final NpcStringId[] TARGET_SHOUTS =
	{
		NpcStringId.ID_1000291,
		NpcStringId.ID_1000398,
		NpcStringId.ID_1000399
	};
	
	public PartyLeaderWarrior()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderWarrior(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27113,
		27036,
		27093,
		27112,
		27068,
		27065,
		27062,
		27114,
		27110,
		27108
	};
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1007"))
		{
			final IntentionType currentIntention = npc.getAI().getCurrentIntention().getType();
			if (currentIntention != IntentionType.ATTACK && !npc.isInMyTerritory())
			{
				npc.instantTeleportTo(npc.getSpawnLocation(), 0);
				npc.removeAllAttackDesire();
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai2 == 0 && Rnd.get(100) < getNpcIntAIParam(npc, "SummonPrivateRate"))
		{
			createPrivates(npc);
			
			npc.broadcastNpcSay(Rnd.get(SUMMON_PRIVATES_SHOUTS));
			
			npc._i_ai2 = 1;
		}
		
		if (attacker instanceof Player && getNpcIntAIParam(npc, "ShoutTarget") == 1 && Rnd.get(100) < 50 && attacker.getStatus().getHpRatio() < 0.4)
		{
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (mostHated == attacker)
			{
				npc.broadcastNpcSay(Rnd.get(TARGET_SHOUTS), attacker.getName());
				
				npc.removeAllAttackDesire();
				npc.getAI().addAttackDesire(attacker, 1000);
				
				npc._flag = attacker.getObjectId();
				
				broadcastScriptEvent(npc, 10002, npc.getObjectId(), 300);
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (getNpcIntAIParam(npc, "SummonPrivateRate") == 0)
		{
			createPrivates(npc);
			
			npc._i_ai2 = 1;
		}
		else
			npc._i_ai2 = 0;
		
		npc._weightPoint = 10;
		
		startQuestTimerAtFixedRate("1007", npc, null, 120000, 120000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (called.isMaster() && target instanceof Playable)
		{
			double hateRatio = getHateRatio(called, target);
			hateRatio = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
			
			called.getAI().addAttackDesire(target, (int) (((hateRatio * damage) * caller._weightPoint) * 10));
		}
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller.getSpawn().getRespawnDelay() != 0)
		{
			if (called.isMaster() && !called.isDead())
				caller.scheduleRespawn(caller.getSpawn().getRespawnDelay() * 1000L);
		}
	}
}