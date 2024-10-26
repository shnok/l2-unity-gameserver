package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;

public class WarriorPhysicalSpecialAreaAggressive extends WarriorPhysicalSpecialAggressive
{
	public WarriorPhysicalSpecialAreaAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorPhysicalSpecialAreaAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22011,
		22012,
		22013,
		22014,
		22015,
		22016
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		// TODO: Area on/off
		// gg::Area_SetOnOff(AreaName,1);
		super.onCreated(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		// TODO: Area on/off
		// gg::Area_SetOnOff(AreaName,0);
		super.onMyDying(npc, killer);
	}
}
