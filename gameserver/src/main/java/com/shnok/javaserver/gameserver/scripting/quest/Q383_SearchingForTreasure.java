package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q383_SearchingForTreasure extends Quest
{
	private static final String QUEST_NAME = "Q383_SearchingForTreasure";
	
	// NPCs
	private static final int ESPEN = 30890;
	private static final int PIRATE_CHEST = 31148;
	
	// Items
	private static final int PIRATE_TREASURE_MAP = 5915;
	private static final int THIEF_KEY = 1661;
	
	public Q383_SearchingForTreasure()
	{
		super(383, "Searching for Treasure");
		
		addQuestStart(ESPEN);
		addTalkId(ESPEN, PIRATE_CHEST);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30890-04.htm"))
		{
			// Sell the map.
			if (player.getInventory().hasItem(PIRATE_TREASURE_MAP))
			{
				takeItems(player, PIRATE_TREASURE_MAP, 1);
				rewardItems(player, 57, 1000);
			}
			else
				htmltext = "30890-06.htm";
		}
		else if (event.equalsIgnoreCase("30890-07.htm"))
		{
			// Listen the story.
			if (player.getInventory().hasItem(PIRATE_TREASURE_MAP))
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
				htmltext = "30890-06.htm";
		}
		else if (event.equalsIgnoreCase("30890-11.htm"))
		{
			// Decipher the map.
			if (player.getInventory().hasItem(PIRATE_TREASURE_MAP))
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, PIRATE_TREASURE_MAP, 1);
			}
			else
				htmltext = "30890-06.htm";
		}
		else if (event.equalsIgnoreCase("31148-02.htm"))
		{
			if (player.getInventory().hasItem(THIEF_KEY))
			{
				takeItems(player, THIEF_KEY, 1);
				
				// Adena reward.
				int i1 = 0;
				
				int i0 = Rnd.get(100);
				if (i0 < 5)
					giveItems(player, 2450, 1);
				else if (i0 < 6)
					giveItems(player, 2451, 1);
				else if (i0 < 18)
					giveItems(player, 956, 1);
				else if (i0 < 28)
					giveItems(player, 952, 1);
				else
					i1 += 500;
				
				i0 = Rnd.get(1000);
				if (i0 < 25)
					giveItems(player, 4481, 1);
				else if (i0 < 50)
					giveItems(player, 4482, 1);
				else if (i0 < 75)
					giveItems(player, 4483, 1);
				else if (i0 < 100)
					giveItems(player, 4484, 1);
				else if (i0 < 125)
					giveItems(player, 4485, 1);
				else if (i0 < 150)
					giveItems(player, 4486, 1);
				else if (i0 < 175)
					giveItems(player, 4487, 1);
				else if (i0 < 200)
					giveItems(player, 4488, 1);
				else if (i0 < 225)
					giveItems(player, 4489, 1);
				else if (i0 < 250)
					giveItems(player, 4490, 1);
				else if (i0 < 275)
					giveItems(player, 4491, 1);
				else if (i0 < 300)
					giveItems(player, 4492, 1);
				else
					i1 += 300;
				
				i0 = Rnd.get(100);
				if (i0 < 4)
					giveItems(player, 1337, 1);
				else if (i0 < 8)
					giveItems(player, 1338, 2);
				else if (i0 < 12)
					giveItems(player, 1339, 2);
				else if (i0 < 16)
					giveItems(player, 3447, 2);
				else if (i0 < 20)
					giveItems(player, 3450, 1);
				else if (i0 < 25)
					giveItems(player, 3453, 1);
				else if (i0 < 27)
					giveItems(player, 3456, 1);
				else
					i1 += 500;
				
				i0 = Rnd.get(100);
				if (i0 < 20)
					giveItems(player, 4408, 1);
				else if (i0 < 40)
					giveItems(player, 4409, 1);
				else if (i0 < 60)
					giveItems(player, 4418, 1);
				else if (i0 < 80)
					giveItems(player, 4419, 1);
				else
					i1 += 500;
				
				rewardItems(player, 57, i1);
				
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "31148-03.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 42 || !player.getInventory().hasItem(PIRATE_TREASURE_MAP)) ? "30890-01.htm" : "30890-02.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ESPEN:
						if (cond == 1)
							htmltext = "30890-07a.htm";
						else
							htmltext = "30890-12.htm";
						break;
					
					case PIRATE_CHEST:
						if (cond == 2)
							htmltext = "31148-01.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
}