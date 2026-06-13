package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class AzitWateringGroud extends DefaultNpc
{
	public AzitWateringGroud()
	{
		super("ai/siegeablehall");
		
		addFirstTalkId(_npcIds);
	}
	
	public AzitWateringGroud(String descr)
	{
		super(descr);
		
		addFirstTalkId(_npcIds);
	}
	
	protected final int[] _npcIds =
	{
		35588,
		35589,
		35590,
		35591
	};
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return null;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		broadcastScriptEventEx(npc, 5, 40000, killer.getObjectId(), 500);
	}
}
