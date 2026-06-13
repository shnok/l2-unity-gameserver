package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Guard;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class GuardMoveAroundFixed extends GuardMoveAround
{
	public GuardMoveAroundFixed()
	{
		super("ai/individual/Guard");
	}
	
	public GuardMoveAroundFixed(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		31032,
		31033,
		31034,
		31035,
		31036
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("5001", npc, null, 300000, 300000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("5001"))
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
				npc.teleportTo(npc.getSpawn().getSpawnLocation(), 0);
		}
		
		return super.onTimer(name, npc, player);
	}
}