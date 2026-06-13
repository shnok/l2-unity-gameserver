package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;

public class WarriorAggressiveImmediate extends Warrior
{
	public WarriorAggressiveImmediate()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorAggressiveImmediate(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27178,
		20780,
		27151,
		27056,
		27082,
		27160,
		27138,
		27149,
		27135,
		27172,
		27088,
		27162,
		27168,
		27169,
		27170,
		27164,
		27044,
		27045,
		27157,
		27083,
		27152,
		27092,
		27080,
		27034,
		27033,
		27032,
		27089,
		27136,
		27161,
		27144,
		27148,
		27145,
		27147,
		27146,
		27035,
		27134,
		27159,
		27158,
		27163,
		27043,
		27081,
		27143,
		27094,
		27137,
		27150,
		27180,
		27194
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
		{
			return;
		}
		if (npc.isInMyTerritory())
		{
			npc.getAI().addAttackDesire(creature, 200);
		}
		super.onSeeCreature(npc, creature);
	}
}
