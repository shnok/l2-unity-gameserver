package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushMissionNpc extends DefaultNpc
{
	public RoyalRushMissionNpc()
	{
		super("ai/individual");
	}
	
	public RoyalRushMissionNpc(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18150,
		18151,
		18152,
		18153,
		18154,
		18155,
		18156,
		18157
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimer("3000", npc, null, 300000);
		startQuestTimerAtFixedRate("3001", npc, null, 5000, 5000);
		
		npc.lookNeighbor();
		npc._i_ai0 = 0;
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
		{
			if (Rnd.get(100) < 80)
				((Npc) creature).sendScriptEvent(1234, npc.getObjectId(), 0);
		}
		else
		{
			npc.getAI().addFollowDesire(creature, 100);
			
			if (npc._i_ai0 == 0)
				npc.broadcastNpcSay(NpcStringId.ID_1010483, creature.getName());
			else
			{
				switch (Rnd.get(4))
				{
					case 0:
						npc.getAI().addCastDesire(creature, 4384, 1, 1000000);
						break;
					
					case 1:
						npc.getAI().addCastDesire(creature, 4385, 1, 1000000);
						break;
					
					case 2:
						npc.getAI().addCastDesire(creature, 4386, 1, 1000000);
						break;
					
					case 3:
						npc.getAI().addCastDesire(creature, 4387, 1, 1000000);
						break;
				}
			}
		}
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc.getAI().addFleeDesire(attacker, Config.MAX_DRIFT_RANGE, 100);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3000"))
		{
			if (!npc.isDead())
			{
				createOnePrivateEx(npc, getNpcIntAIParam(npc, "KeyBox"), npc.getX(), npc.getY(), npc.getZ(), 0, 0, true);
				
				npc._i_ai0 = 1;
				npc.broadcastNpcSay(NpcStringId.ID_1000503);
				npc.lookNeighbor();
			}
		}
		else if (name.equalsIgnoreCase("3001"))
		{
			npc.lookNeighbor();
			
			if (npc._i_ai0 == 0)
				npc.broadcastNpcSay(NpcStringId.ID_1010484);
		}
		
		return null;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (npc._i_ai0 == 0)
			createOnePrivateEx(npc, getNpcIntAIParam(npc, "StrongNPC"), npc.getX(), npc.getY(), npc.getZ(), 0, 0, true);
	}
}