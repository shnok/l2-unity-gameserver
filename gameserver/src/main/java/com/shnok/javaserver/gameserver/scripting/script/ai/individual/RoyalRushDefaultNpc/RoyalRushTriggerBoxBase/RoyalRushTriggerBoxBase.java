package com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc.RoyalRushTriggerBoxBase;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc.RoyalRushDefaultNpc;

public class RoyalRushTriggerBoxBase extends RoyalRushDefaultNpc
{
	public RoyalRushTriggerBoxBase()
	{
		super("ai/individual/RoyalRushDefaultNpc/RoyalRushTriggerBoxBase");
	}
	
	public RoyalRushTriggerBoxBase(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		npc.deleteMe();
		
		return null;
	}
}
