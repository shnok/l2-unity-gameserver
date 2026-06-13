package com.shnok.javaserver.gameserver.scripting.script.ai.boss.baium;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class BaiumAngelicVortex extends Quest
{
	public BaiumAngelicVortex()
	{
		super(-1, "ai/boss/baium");
		
		addFirstTalkId(31862);
		addTalkId(31862);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		String htmltext = "";
		
		htmltext = "31862-00.htm";
		
		return htmltext;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = "";
		
		if (event.equalsIgnoreCase("baium_story"))
		{
			htmltext = "31862-02.htm";
			
			return htmltext;
		}
		else if (event.equalsIgnoreCase("enter"))
		{
			Creature c0 = GlobalMemo.getInstance().getCreature("2");
			
			if (player.isFlying())
				htmltext = "31862-05.htm";
			else if (c0 != null && !c0.isDead())
			{
				if (((Npc) c0).getSpawn().getSpawnData().getDBValue() == 0)
				{
					if (player.getInventory().hasItem(4295))
					{
						takeItems(player, 4295, 1);
						player.teleportTo(114077, 15882, 10078, 0);
					}
					else
					{
						htmltext = "31862-03.htm";
					}
				}
				else
				{
					htmltext = "31862-01.htm";
				}
			}
			else
				htmltext = "31862-04.htm";
		}
		return htmltext;
	}
}
