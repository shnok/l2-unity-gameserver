package com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc;

import java.util.Calendar;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class RoyalRushDefaultNpc extends DefaultNpc
{
	public RoyalRushDefaultNpc()
	{
		super("ai/individual/RoyalRushDefaultNpc");
	}
	
	public RoyalRushDefaultNpc(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("3000", npc, null, 1000, 1000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3000"))
		{
			final int i0 = Calendar.getInstance().get(Calendar.MINUTE);
			if (i0 == 54)
				npc.deleteMe();
		}
		
		return null;
	}
}
