package com.shnok.javaserver.gameserver.scripting.script.ai.boss.valakas;

import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.memo.GlobalMemo;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;

public class HeartOfVolcano extends DefaultNpc
{
	private static final int MAX_PLAYERS_ALLOWED = 200;
	
	public HeartOfVolcano()
	{
		super("ai/boss/valakas");
		
		addCreated(31385);
		addFirstTalkId(31385);
		addTalkId(31385);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (!npc.getSpawn().getDBLoaded())
		{
			npc._i_ai0 = 0;
			npc.getSpawn().getSpawnData().setDBValue(0);
		}
		else
			npc._i_ai0 = npc.getSpawn().getSpawnData().getDBValue();
		
		startQuestTimerAtFixedRate("2002", npc, null, 60000, 60000);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "heart_of_volcano001.htm";
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = "";
		
		if (event.equalsIgnoreCase("enter"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("3");
			if (c0 != null && !c0.isDead())
			{
				final int dbValue = c0.getSpawn().getSpawnData().getDBValue();
				if (dbValue == 0)
					c0.sendScriptEvent(0, 0, 0);
				
				if (dbValue == 0 || dbValue == 1)
				{
					if (npc._i_ai0 >= MAX_PLAYERS_ALLOWED)
						htmltext = "heart_of_volcano004.htm";
					else
					{
						final int telX = getNpcIntAIParam(npc, "TelPosX");
						final int telY = getNpcIntAIParam(npc, "TelPosY");
						final int telZ = getNpcIntAIParam(npc, "TelPosZ");
						
						player.teleportTo(telX, telY, telZ, 0);
						
						npc._i_ai0++;
						npc.getSpawn().getSpawnData().setDBValue(npc._i_ai0);
					}
				}
				else if (dbValue == 2 || dbValue == 3)
					htmltext = "heart_of_volcano003.htm";
			}
			else
				htmltext = "heart_of_volcano002.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2002"))
		{
			final int npcDBValue = npc.getSpawn().getSpawnData().getDBValue();
			if (npcDBValue != 0)
			{
				final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("3");
				if (c0 != null && !c0.isDead())
				{
					final int c0DBValue = c0.getSpawn().getSpawnData().getDBValue();
					if (c0DBValue != 0)
						return super.onTimer(name, npc, player);
				}
				
				npc.getSpawn().getSpawnData().setDBValue(0);
				npc._i_ai0 = 0;
			}
		}
		
		return super.onTimer(name, npc, player);
	}
}