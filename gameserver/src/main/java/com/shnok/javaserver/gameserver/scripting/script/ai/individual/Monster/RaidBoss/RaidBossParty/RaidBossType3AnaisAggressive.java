package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossParty;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RaidBossType3AnaisAggressive extends RaidBossType3
{
	public RaidBossType3AnaisAggressive()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossParty/RaidBossType3");
	}
	
	public RaidBossType3AnaisAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29096
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		if (npc.isInMyTerritory())
			npc.getAI().addAttackDesire(creature, 200);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (!npc.isInMyTerritory())
		{
			npc.teleportTo(new Location(112800, -76160, 10), 0);
			startQuestTimer("3001", npc, null, 1000);
		}
		super.onNoDesire(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (!npc.isInMyTerritory())
		{
			npc.teleportTo(new Location(112800, -76160, 10), 0);
			startQuestTimer("3001", npc, null, 1000);
		}
		
		super.onAttacked(npc, attacker, damage, null);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3001"))
		{
			npc.removeAllAttackDesire();
			npc.getAI().getHateList().cleanAllHate();
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (!caller.isInMyTerritory() && !called.isInMyTerritory())
		{
			called.teleportTo(new Location(112800, -76160, 10), 0);
			startQuestTimer("3001", called, null, 1000);
		}
		if (!caller.isInMyTerritory())
			return;
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (!npc.isInMyTerritory())
		{
			npc.teleportTo(new Location(112800, -76160, 10), 0);
			startQuestTimer("3001", npc, null, 1000);
		}
	}
}
