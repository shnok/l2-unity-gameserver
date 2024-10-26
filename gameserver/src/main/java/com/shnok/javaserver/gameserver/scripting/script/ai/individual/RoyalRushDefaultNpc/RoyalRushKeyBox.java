package com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class RoyalRushKeyBox extends RoyalRushDefaultNpc
{
	public RoyalRushKeyBox()
	{
		super("ai/individual/RoyalRushDefaultNpc");
	}
	
	public RoyalRushKeyBox(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		31455,
		31456,
		31457,
		31458,
		31459,
		31460,
		31461,
		31462,
		31463,
		31464,
		31465,
		31466,
		31467
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		if (npc._i_ai0 == 0)
		{
			giveItems(player, 7260, 1);
			npc._i_ai0 = 1;
		}
		
		npc.deleteMe();
		
		return null;
	}
}
