package com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc.RoyalRushTriggerBoxBase;

import java.util.Calendar;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class RoyalRushTriggerBox5 extends RoyalRushTriggerBoxBase
{
	public RoyalRushTriggerBox5()
	{
		super("ai/individual/RoyalRushDefaultNpc/RoyalRushTriggerBoxBase");
	}
	
	public RoyalRushTriggerBox5(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		31472,
		31477,
		31482,
		31487
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
		
		switch (Rnd.get(2))
		{
			case 0:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "room_event_1", "1rd_type1_e"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
			case 1:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParamOrDefault(npc, "room_event_2", "1rd_type2_e"));
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
