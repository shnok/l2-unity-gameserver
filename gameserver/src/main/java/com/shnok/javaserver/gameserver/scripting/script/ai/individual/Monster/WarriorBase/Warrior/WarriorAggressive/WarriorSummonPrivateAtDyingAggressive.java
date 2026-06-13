package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorAggressive;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorSummonPrivateAtDyingAggressive extends WarriorAggressive
{
	public WarriorSummonPrivateAtDyingAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorAggressive");
	}
	
	public WarriorSummonPrivateAtDyingAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21653,
		21655,
		21656,
		21654,
		21657,
		21652
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		if (topDesireTarget != null && topDesireTarget == attacker)
			npc._i_ai3 = attacker.getObjectId();
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (Rnd.get(100) < getNpcIntAIParam(npc, "SummonPrivateRate"))
		{
			createPrivates(npc);
			
			broadcastScriptEvent(npc, 10020, npc._i_ai3, 500);
			
			startQuestTimer("5001", npc, null, 1500);
		}
		super.onMyDying(npc, killer);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("5001"))
		{
			broadcastScriptEvent(npc, 10020, npc._i_ai3, 500);
			
			startQuestTimer("5001", npc, null, 1500);
			
			return super.onTimer(name, npc, player);
		}
		return null;
	}
}