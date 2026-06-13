package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.Agit01Move1;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit01PartisanSorcerer2 extends Agit01Move1
{
	public Agit01PartisanSorcerer2()
	{
		super("ai/siegeablehall/Agit01Move1");
	}
	
	public Agit01PartisanSorcerer2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35380
	};
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && Rnd.get(100) < 10)
			called.getAI().addCastDesireHold(attacker, 4043, 1, 1000000);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		npc.getAI().addCastDesireHold(creature, 4043, 1, 1000000);
	}
}
