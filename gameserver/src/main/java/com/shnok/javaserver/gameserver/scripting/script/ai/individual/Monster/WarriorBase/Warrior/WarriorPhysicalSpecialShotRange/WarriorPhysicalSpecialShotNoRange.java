package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecialShotRange;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;

public class WarriorPhysicalSpecialShotNoRange extends WarriorPhysicalSpecialShotRange
{
	public WarriorPhysicalSpecialShotNoRange()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecialShotRange");
	}
	
	public WarriorPhysicalSpecialShotNoRange(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22066,
		22067,
		22068,
		22069,
		22070,
		22071,
		22072,
		22073
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		// TODO: impl when zones are done
		
		// gg::Area_SetOnOff(AreaName1,1);
		// gg::Area_SetOnOff(AreaName2,1);
		// gg::Area_SetOnOff(AreaName3,1);
		// gg::Area_SetOnOff(AreaName4,1);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		// TODO: impl when zones are done
		
		// gg::Area_SetOnOff(AreaName1,0);
		// gg::Area_SetOnOff(AreaName2,0);
		// gg::Area_SetOnOff(AreaName3,0);
		// gg::Area_SetOnOff(AreaName4,0);
		
		super.onMyDying(npc, killer);
	}
}
