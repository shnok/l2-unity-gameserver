package com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc.RoyalRushTriggerBoxBase;

import java.util.Calendar;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class RoyalRushTriggerBox1 extends RoyalRushTriggerBoxBase
{
	public RoyalRushTriggerBox1()
	{
		super("ai/individual/RoyalRushDefaultNpc/RoyalRushTriggerBoxBase");
	}
	
	public RoyalRushTriggerBox1(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		31468,
		31473,
		31478,
		31483
	};
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final int i0 = Calendar.getInstance().get(Calendar.MINUTE);
		
		if (i0 > 49 && i0 < 60)
		{
			npc.broadcastNpcSay(NpcStringId.ID_1010552);
			return null;
		}
		if (npc._i_ai0 == 1)
			return null;
		
		npc._i_ai0 = 1;
		
		switch (Rnd.get(3))
		{
			case 0:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "room_event_1", "1rd_type1_a"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
			case 1:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "room_event_2", "1rd_type2_a"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
			case 2:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "room_event_3", "1rd_type3_a"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
		}
		
		return super.onFirstTalk(npc, player);
	}
}
