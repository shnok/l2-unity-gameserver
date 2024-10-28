package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PlaySound;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SpecialCamera;

public class TriolCamera extends DefaultNpc
{
	public TriolCamera()
	{
		super("ai/individual");
	}
	
	public TriolCamera(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		13015
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 3)
		{
			npc.broadcastPacket(new PlaySound(1, "BS04_A", npc));
			npc._i_ai0 = arg1;
			startQuestTimer("1001", npc, null, 1000);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1500, 88, 89, 0, 10000, 0, 110, 1, 0));
			startQuestTimer("1002", npc, null, 300);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 1500, 88, 89, 0, 10000, 0, 110, 1, 0));
			startQuestTimer("1003", npc, null, 300);
		}
		else if (name.equalsIgnoreCase("1003"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 450, 88, 3, 5500, 10000, 0, 350, 1, 0));
			startQuestTimer("1004", npc, null, 9400);
		}
		else if (name.equalsIgnoreCase("1004"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 500, 88, 4, 5000, 10000, 0, 0, 1, 0));
			startQuestTimer("1005", npc, null, 5000);
		}
		else if (name.equalsIgnoreCase("1005"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 3000, 88, 4, 6000, 6900, 0, 15, 1, 0));
			startQuestTimer("1006", npc, null, 6000);
		}
		else if (name.equalsIgnoreCase("1006"))
		{
			if (npc._i_ai0 != 0)
				broadcastScriptEvent(npc, 4, npc._i_ai0, 4000);
		}
		
		return super.onTimer(name, npc, player);
	}
}
