package com.shnok.javaserver.gameserver.scripting.script.ai.boss.antharas;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class HeartOfWarding extends Quest
{
	private static final int GM_ID1 = 4;
	private static final int GM_ID2 = 5;
	private static final int GM_ID3 = 6;
	
	public HeartOfWarding()
	{
		super(-1, "ai/boss/antharas");
		
		addFirstTalkId(13001);
		addTalkId(13001);
		addCreated(13001);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return "13001-01.htm";
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3001"))
		{
			if (npc._i_ai1 <= 90)
			{
				final Npc c_ai0 = (Npc) npc._c_ai0;
				if (c_ai0 != null)
					c_ai0.sendScriptEvent(0, 0, 0);
			}
			else if (npc._i_ai1 <= 135)
			{
				final Npc c_ai1 = (Npc) npc._c_ai1;
				if (c_ai1 != null)
					c_ai1.sendScriptEvent(0, 0, 0);
			}
			else
			{
				final Npc c_ai2 = (Npc) npc._c_ai2;
				if (c_ai2 != null)
					c_ai2.sendScriptEvent(0, 0, 0);
			}
			
			npc._i_ai0 = 0;
			npc._i_ai1 = 0;
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = "";
		
		if (event.equalsIgnoreCase("enter"))
		{
			final int i0 = GlobalMemo.getInstance().getInteger(String.valueOf(GM_ID1), -1);
			final int i1 = GlobalMemo.getInstance().getInteger(String.valueOf(GM_ID2), -1);
			final int i2 = GlobalMemo.getInstance().getInteger(String.valueOf(GM_ID3), -1);
			
			if (i0 != -1 && i1 != -1 && i2 != -1)
			{
				final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature(String.valueOf(GM_ID1));
				final Npc c1 = (Npc) GlobalMemo.getInstance().getCreature(String.valueOf(GM_ID2));
				final Npc c2 = (Npc) GlobalMemo.getInstance().getCreature(String.valueOf(GM_ID3));
				
				if (!c0.isDead() && !c1.isDead() && !c2.isDead())
				{
					final int c0Value = c0.getSpawn().getSpawnData().getDBValue();
					final int c1Value = c1.getSpawn().getSpawnData().getDBValue();
					final int c2Value = c2.getSpawn().getSpawnData().getDBValue();
					
					if ((c0Value == 0 || c0Value == 1) && ((c1Value == 0 || c1Value == 1) && (c2Value == 0 || c2Value == 1)))
					{
						if (player.getInventory().hasItem(3865))
						{
							takeItems(player, 3865, 1);
							
							final int i3 = (179700 + Rnd.get(700));
							final int i4 = (113800 + Rnd.get(2100));
							
							player.teleportTo(i3, i4, -7709, 0);
							
							npc._i_ai1++;
							
							if (npc._i_ai0 == 0)
							{
								startQuestTimer("3001", npc, player, Config.WAIT_TIME_ANTHARAS);
								
								npc._c_ai0 = c0;
								npc._c_ai1 = c1;
								npc._c_ai2 = c2;
								
								npc._i_ai0 = 1;
							}
						}
						else
							htmltext = "13001-04.htm";
					}
					else if (c0Value == 2 || c0Value == 3 || c1Value == 2 || c1Value == 3 || c2Value == 2 || c2Value == 3)
						htmltext = "13001-03.htm";
				}
				else
					htmltext = "13001-02.htm";
			}
			else
				htmltext = "13001-02.htm";
		}
		return htmltext;
	}
}