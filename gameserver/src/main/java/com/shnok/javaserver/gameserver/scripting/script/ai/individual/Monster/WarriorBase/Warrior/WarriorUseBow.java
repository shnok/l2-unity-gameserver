package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WarriorUseBow extends Warrior
{
	public WarriorUseBow()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorUseBow(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20011,
		20094,
		20296,
		20423,
		20614,
		21133,
		20385,
		20273,
		20463,
		27060,
		20469,
		20361,
		20578,
		27125,
		20053,
		20063,
		20209,
		20006,
		20045,
		20518,
		20102,
		20051,
		20784,
		20496,
		20447,
		22003,
		22031,
		22040
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai2 == 0 && npc.distance2D(attacker) < 100)
		{
			startQuestTimer("100002", npc, null, 2000);
			
			npc._i_ai2 = 1;
			npc._c_ai1 = attacker;
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("100002"))
		{
			npc.getAI().addFleeDesire(npc._c_ai1, Config.MAX_DRIFT_RANGE, 100000);
			
			npc._i_ai2 = 0;
		}
		
		return super.onTimer(name, npc, player);
	}
}