package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorAggressive.WarriorAggressive;

public class AzitWateringCrazyYeti extends WarriorAggressive
{
	public AzitWateringCrazyYeti()
	{
		super("ai/siegeablehall");
	}
	
	public AzitWateringCrazyYeti(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35592
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastNpcShout(NpcStringId.ID_1010627);
		
		super.onCreated(npc);
	}
}
