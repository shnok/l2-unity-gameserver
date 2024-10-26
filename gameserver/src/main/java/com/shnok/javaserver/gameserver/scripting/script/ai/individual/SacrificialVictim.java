package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class SacrificialVictim extends DefaultNpc
{
	public SacrificialVictim()
	{
		super("ai/individual");
	}
	
	public SacrificialVictim(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		32038
	};
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 3)
		{
			startQuestTimer("998", npc, null, 10000);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("998"))
		{
			npc.getAI().addSocialDesire(1, 7000, 1000);
			startQuestTimer("999", npc, player, 5000);
		}
		else if (name.equalsIgnoreCase("999"))
		{
			createOnePrivateEx(npc, 22145, npc.getX(), npc.getY(), npc.getZ(), 0, 0, true);
			npc.deleteMe();
		}
		
		return super.onTimer(name, npc, player);
	}
}
