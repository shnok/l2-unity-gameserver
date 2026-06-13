package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.AzitWateringGameManager;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;

public class AzitWateringGameManagerCircle1 extends AzitWateringGameManager
{
	public AzitWateringGameManagerCircle1()
	{
		super("ai/siegeablehall/AzitWateringGameManager");
		
		GAME_MASTER = 1;
		NUMBER_OF_CIRCLE = 1;
		AREA_DATA_DEBUFF = "rainbow_slow_1";
		
		addFirstTalkId(_npcIds);
		addTalkId(_npcIds);
	}
	
	public AzitWateringGameManagerCircle1(String descr)
	{
		super(descr);
		
		GAME_MASTER = 1;
		NUMBER_OF_CIRCLE = 1;
		AREA_DATA_DEBUFF = "rainbow_slow_1";
		
		addFirstTalkId(_npcIds);
		addTalkId(_npcIds);
	}
	
	protected final int[] _npcIds =
	{
		35596
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai3 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Npc npcCreature && npc._i_ai3 == 0)
		{
			final int npcId = npcCreature.getNpcId();
			if (npcId >= 35588 && npcId <= 35591)
			{
				npc._i_ai3 = creature.getObjectId();
				broadcastScriptEvent(npc, 10002, npc._i_ai3, 8000);
			}
		}
		
		super.onSeeCreature(npc, creature);
	}
}