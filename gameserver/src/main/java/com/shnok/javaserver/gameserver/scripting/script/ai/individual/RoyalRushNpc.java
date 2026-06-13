package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import java.util.Calendar;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcSay;

public class RoyalRushNpc extends DefaultNpc
{
	public RoyalRushNpc()
	{
		super("ai/individual/RoyalRushNpc");
	}
	
	public RoyalRushNpc(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		31921,
		31922,
		31923,
		31924
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("3000", npc, null, 1000, 1000);
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		
		final int lock_npc_id1 = getNpcIntAIParam(npc, "lock_npc_id1");
		final int lock_npc_id2 = getNpcIntAIParam(npc, "lock_npc_id2");
		final int lock_npc_id3 = getNpcIntAIParam(npc, "lock_npc_id3");
		final int lock_npc_id4 = getNpcIntAIParam(npc, "lock_npc_id4");
		final int lock_npc_id5 = getNpcIntAIParam(npc, "lock_npc_id5");
		final int lock_x1 = getNpcIntAIParam(npc, "lock_x1");
		final int lock_y1 = getNpcIntAIParam(npc, "lock_y1");
		final int lock_z1 = getNpcIntAIParam(npc, "lock_z1");
		final int lock_d1 = getNpcIntAIParam(npc, "lock_d1");
		final int lock_x2 = getNpcIntAIParam(npc, "lock_x2");
		final int lock_y2 = getNpcIntAIParam(npc, "lock_y2");
		final int lock_z2 = getNpcIntAIParam(npc, "lock_z2");
		final int lock_d2 = getNpcIntAIParam(npc, "lock_d2");
		final int lock_x3 = getNpcIntAIParam(npc, "lock_x3");
		final int lock_y3 = getNpcIntAIParam(npc, "lock_y3");
		final int lock_z3 = getNpcIntAIParam(npc, "lock_z3");
		final int lock_d3 = getNpcIntAIParam(npc, "lock_d3");
		final int lock_x4 = getNpcIntAIParam(npc, "lock_x4");
		final int lock_y4 = getNpcIntAIParam(npc, "lock_y4");
		final int lock_z4 = getNpcIntAIParam(npc, "lock_z4");
		final int lock_d4 = getNpcIntAIParam(npc, "lock_d4");
		final int lock_x5 = getNpcIntAIParam(npc, "lock_x5");
		final int lock_y5 = getNpcIntAIParam(npc, "lock_y5");
		final int lock_z5 = getNpcIntAIParam(npc, "lock_z5");
		final int lock_d5 = getNpcIntAIParam(npc, "lock_d5");
		
		if (lock_x1 != 0)
		{
			createOnePrivateEx(npc, lock_npc_id1, lock_x1, lock_y1, lock_z1, lock_d1, 0, false);
			createOnePrivateEx(npc, lock_npc_id2, lock_x2, lock_y2, lock_z2, lock_d2, 0, false);
			createOnePrivateEx(npc, lock_npc_id3, lock_x3, lock_y3, lock_z3, lock_d3, 0, false);
			createOnePrivateEx(npc, lock_npc_id4, lock_x4, lock_y4, lock_z4, lock_d4, 0, false);
			createOnePrivateEx(npc, lock_npc_id5, lock_x5, lock_y5, lock_z5, lock_d5, 0, false);
		}
		else
		{
			createOnePrivateEx(npc, lock_npc_id1, 182727, -85493, -7200, -32584, 0, false, 1, 0, 0);
			createOnePrivateEx(npc, lock_npc_id2, 184547, -85479, -7200, -32584, 0, false, 2, 0, 0);
			createOnePrivateEx(npc, lock_npc_id3, 186349, -85473, -7200, -32584, 0, false, 3, 0, 0);
			createOnePrivateEx(npc, lock_npc_id4, 188154, -85463, -7200, -32584, 0, false, 4, 0, 0);
			createOnePrivateEx(npc, lock_npc_id5, 189947, -85466, -7200, -32584, 0, false, 5, 0, 0);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3001"))
		{
			if (npc._i_ai1 == 1)
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParam(npc, "room_trigger_1"));
				if (maker0 != null)
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				
				npc._i_ai1 = 0;
			}
		}
		else if (name.equalsIgnoreCase("3000"))
		{
			final int i0 = Calendar.getInstance().get(Calendar.MINUTE);
			final int i1 = Calendar.getInstance().get(Calendar.SECOND);
			final int shoutMsg = getNpcIntAIParam(npc, "ShoutMsg");
			if (i0 == 0 && i1 < 5)
			{
				if (npc._i_ai1 == 0)
				{
					final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParam(npc, "room_trigger_1"));
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					
					npc._i_ai1 = 1;
					startQuestTimer("3001", npc, null, 5000);
				}
			}
			else if ((i0 == 5 || i0 == 10 || i0 == 15 || i0 == 20 || i0 == 25 || i0 == 30 || i0 == 35 || i0 == 40 || i0 == 45) && i1 == 0)
			{
				if (shoutMsg == 1)
					npc.broadcastPacketInRadius(new NpcSay(npc, SayType.SHOUT, NpcStringId.ID_1000455.getMessage() + i0 + NpcStringId.ID_1000456.getMessage()), 11500);
			}
			else if (i0 == 50 && i1 == 0)
			{
				if (shoutMsg == 1)
					npc.broadcastPacketInRadius(new NpcSay(npc, SayType.SHOUT, NpcStringId.ID_1000457.getMessage()), 11500);
				
				final int escapeTelPosX = getNpcIntAIParam(npc, "EscapeTelPosX");
				final int escapeTelPosY = getNpcIntAIParam(npc, "EscapeTelPosY");
				final int escapeTelPosZ = getNpcIntAIParam(npc, "EscapeTelPosZ");
				if (escapeTelPosX != 0 && escapeTelPosY != 0 && escapeTelPosZ != 0)
					npc.getSpawn().instantTeleportInMyTerritory(escapeTelPosX, escapeTelPosY, escapeTelPosZ, 100);
				
			}
			else if (i0 == 54 && i1 >= 0 && i1 <= 30)
			{
				npc._av_quest0.set(0);
			}
			else if (i0 == 55 && i1 == 0)
			{
				npc.broadcastPacketInRadius(new NpcSay(npc, SayType.SHOUT, NpcStringId.ID_1000500.getMessage()), 11500);
				npc.broadcastPacketInRadius(new NpcSay(npc, SayType.SHOUT, NpcStringId.ID_1000501.getMessage()), 11500);
			}
		}
		
		return null;
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		switch (eventId)
		{
			case 1001:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParam(npc, "room_trigger_2"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
			case 1002:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParam(npc, "room_trigger_3"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
			case 1003:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParam(npc, "room_trigger_4"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
			case 1004:
			{
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(getNpcStringAIParam(npc, "room_trigger_5"));
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
			case 1005:
			{
				final int i0 = Rnd.get(4) + 1;
				final String s0 = getNpcStringAIParam(npc, "room_trigger_boss") + "_type" + i0;
				final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker(s0);
				if (maker0 != null)
				{
					maker0.getMaker().onMakerScriptEvent("1000", maker0, 0, 0);
					maker0.getMaker().onMakerScriptEvent("1001", maker0, 0, 0);
				}
				break;
			}
		}
	}
}
