package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.AzitWateringGameManager;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;

public class AzitWateringGameManagerCircle3 extends AzitWateringGameManagerCircle1
{
	public AzitWateringGameManagerCircle3()
	{
		super("ai/siegeablehall/AzitWateringGameManager");
		
		NUMBER_OF_CIRCLE = 3;
		AREA_DATA_DEBUFF = "rainbow_slow_3";
		
		addFirstTalkId(_npcIds);
		addTalkId(_npcIds);
	}
	
	public AzitWateringGameManagerCircle3(String descr)
	{
		super(descr);
		
		NUMBER_OF_CIRCLE = 3;
		AREA_DATA_DEBUFF = "rainbow_slow_3";
		
		addFirstTalkId(_npcIds);
		addTalkId(_npcIds);
	}
	
	protected final int[] _npcIds =
	{
		35598
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Npc npcCreature && npc._i_ai3 == 0)
		{
			final int npcId = npcCreature.getNpcId();
			if (npcId >= 35588 && npcId <= 35591)
			{
				npc._i_ai3 = creature.getObjectId();
				broadcastScriptEvent(npc, 10004, npc._i_ai3, 8000);
			}
		}
		
		super.onSeeCreature(npc, creature);
	}
}