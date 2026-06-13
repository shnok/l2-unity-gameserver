package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.Agit01PartisanHealer1;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit01PartisanHealer1 extends DefaultNpc
{
	public Agit01PartisanHealer1()
	{
		super("ai/siegeablehall/Agit01PartisanHealer1");
	}
	
	public Agit01PartisanHealer1(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35369
	};
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if ((caller.getNpcId() == 35368 || caller.getNpcId() == 35375) && caller.getStatus().getHpRatio() < 0.6 && Rnd.get(100) < 20)
			called.getAI().addCastDesire(caller, 4044, 1, 1000000);
	}
}