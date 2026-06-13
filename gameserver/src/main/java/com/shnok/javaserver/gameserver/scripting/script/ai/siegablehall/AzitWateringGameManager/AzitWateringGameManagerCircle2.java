package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.AzitWateringGameManager;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;

public class AzitWateringGameManagerCircle2 extends AzitWateringGameManagerCircle1
{
	public AzitWateringGameManagerCircle2()
	{
		super("ai/siegeablehall/AzitWateringGameManager");
		
		NUMBER_OF_CIRCLE = 2;
		AREA_DATA_DEBUFF = "rainbow_slow_2";
		
		addFirstTalkId(_npcIds);
		addTalkId(_npcIds);
	}
	
	public AzitWateringGameManagerCircle2(String descr)
	{
		super(descr);
		
		NUMBER_OF_CIRCLE = 2;
		AREA_DATA_DEBUFF = "rainbow_slow_2";
		
		addFirstTalkId(_npcIds);
		addTalkId(_npcIds);
	}
	
	protected final int[] _npcIds =
	{
		35597
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
				broadcastScriptEvent(npc, 10003, npc._i_ai3, 8000);
			}
		}
		
		super.onSeeCreature(npc, creature);
	}
}