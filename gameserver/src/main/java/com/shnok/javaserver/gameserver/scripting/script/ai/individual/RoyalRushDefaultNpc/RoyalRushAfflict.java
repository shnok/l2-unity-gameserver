package com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushAfflict extends RoyalRushDefaultNpc
{
	public RoyalRushAfflict()
	{
		super("ai/individual/RoyalRushDefaultNpc");
	}
	
	public RoyalRushAfflict(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18244,
		18245,
		18246,
		18247,
		18248,
		18249,
		18250,
		18251,
		18252,
		18253,
		18254,
		18255
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		final int i0 = Rnd.get(3);
		switch (i0)
		{
			case 0:
			{
				npc._i_ai0 = 0;
				startQuestTimer("3001", npc, null, 5000);
				break;
			}
			case 1:
			{
				npc._i_ai0 = 1;
				startQuestTimer("3002", npc, null, 5000);
				break;
			}
			case 2:
			{
				npc._i_ai0 = 2;
				startQuestTimer("3002", npc, null, 5000);
				break;
			}
		}
		
		npc.lookNeighbor();
		
		super.onCreated(npc);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player && Rnd.get(100) < 50)
			npc.getAI().addFollowDesire(creature, 100);
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
		if (name.equalsIgnoreCase("3001"))
		{
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.AFFLICT_SKILL1), 1000000);
		}
		if (name.equalsIgnoreCase("3002"))
		{
			if (npc._i_ai0 == 1)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.AFFLICT_SKILL2), 1000000);
			else if (npc._i_ai0 == 2)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.AFFLICT_SKILL3), 1000000);
			
			startQuestTimer("3003", npc, null, 5000);
		}
		if (name.equalsIgnoreCase("3003"))
		{
			npc.lookNeighbor();
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		switch (npc._i_ai0)
		{
			case 0:
			{
				npc.deleteMe();
				break;
			}
			case 1:
			{
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.AFFLICT_SKILL2), 1000000);
				break;
			}
			case 2:
			{
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.AFFLICT_SKILL3), 1000000);
				break;
			}
		}
	}
}
