package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class AreadataSwitch extends DefaultNpc
{
	public AreadataSwitch()
	{
		super("ai/individual");
	}
	
	public AreadataSwitch(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		32058,
		32059,
		32060,
		32061,
		32062,
		32063,
		32064,
		32065,
		32066,
		32067,
		32068
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		// TODO: Area zone
		// gg::Area_SetOnOff(AreaName,1);
		npc.broadcastNpcSay(NpcStringId.ID_1010472);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		// TODO: Area zone
		// gg::Area_SetOnOff(AreaName,0);
		
		super.onMyDying(npc, killer);
	}
}
