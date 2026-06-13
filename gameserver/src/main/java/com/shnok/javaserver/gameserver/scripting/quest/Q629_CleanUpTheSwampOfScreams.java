package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q629_CleanUpTheSwampOfScreams extends Quest
{
	private static final String QUEST_NAME = "Q629_CleanUpTheSwampOfScreams";
	
	// NPC
	private static final int PIERCE = 31553;
	
	// ITEMS
	private static final int TALON_OF_STAKATO = 7250;
	private static final int GOLDEN_RAM_COIN = 7251;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(10);
	
	public Q629_CleanUpTheSwampOfScreams()
	{
		super(629, "Clean up the Swamp of Screams");
		
		CHANCES.put(21508, 500000);
		CHANCES.put(21509, 431000);
		CHANCES.put(21510, 521000);
		CHANCES.put(21511, 576000);
		CHANCES.put(21512, 746000);
		CHANCES.put(21513, 530000);
		CHANCES.put(21514, 538000);
		CHANCES.put(21515, 545000);
		CHANCES.put(21516, 553000);
		CHANCES.put(21517, 560000);
		
		setItemsIds(TALON_OF_STAKATO, GOLDEN_RAM_COIN);
		
		addQuestStart(PIERCE);
		addTalkId(PIERCE);
		
		for (int npcId : CHANCES.keySet())
			addMyDying(npcId);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31553-1.htm"))
		{
			if (player.getStatus().getLevel() >= 66)
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
			{
				htmltext = "31553-0a.htm";
				st.exitQuest(true);
			}
		}
		else if (event.equalsIgnoreCase("31553-3.htm"))
		{
			if (player.getInventory().getItemCount(TALON_OF_STAKATO) >= 100)
			{
				takeItems(player, TALON_OF_STAKATO, 100);
				giveItems(player, GOLDEN_RAM_COIN, 20);
			}
			else
				htmltext = "31553-3a.htm";
		}
		else if (event.equalsIgnoreCase("31553-5.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
		
		if (!player.getInventory().hasAtLeastOneItem(7246, 7247))
			return "31553-6.htm";
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 66) ? "31553-0a.htm" : "31553-0.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().getItemCount(TALON_OF_STAKATO) >= 100) ? "31553-2.htm" : "31553-1a.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		dropItems(st.getPlayer(), TALON_OF_STAKATO, 1, 100, CHANCES.get(npc.getNpcId()));
	}
}