package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q370_AnElderSowsSeeds extends Quest
{
	private static final String QUEST_NAME = "Q370_AnElderSowsSeeds";
	
	// NPC
	private static final int CASIAN = 30612;
	
	// Items
	private static final int SPELLBOOK_PAGE = 5916;
	private static final int CHAPTER_OF_FIRE = 5917;
	private static final int CHAPTER_OF_WATER = 5918;
	private static final int CHAPTER_OF_WIND = 5919;
	private static final int CHAPTER_OF_EARTH = 5920;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(20082, 86000);
		CHANCES.put(20084, 94000);
		CHANCES.put(20086, 90000);
		CHANCES.put(20089, 100000);
		CHANCES.put(20090, 202000);
	}
	
	public Q370_AnElderSowsSeeds()
	{
		super(370, "An Elder Sows Seeds");
		
		setItemsIds(SPELLBOOK_PAGE, CHAPTER_OF_FIRE, CHAPTER_OF_WATER, CHAPTER_OF_WIND, CHAPTER_OF_EARTH);
		
		addQuestStart(CASIAN);
		addTalkId(CASIAN);
		
		addMyDying(20082, 20084, 20086, 20089, 20090);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30612-3.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30612-6.htm"))
		{
			if (player.getInventory().hasItems(CHAPTER_OF_FIRE, CHAPTER_OF_WATER, CHAPTER_OF_WIND, CHAPTER_OF_EARTH))
			{
				htmltext = "30612-8.htm";
				takeItems(player, CHAPTER_OF_FIRE, 1);
				takeItems(player, CHAPTER_OF_WATER, 1);
				takeItems(player, CHAPTER_OF_WIND, 1);
				takeItems(player, CHAPTER_OF_EARTH, 1);
				rewardItems(player, 57, 3600);
			}
		}
		else if (event.equalsIgnoreCase("30612-9.htm"))
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
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 28) ? "30612-0a.htm" : "30612-0.htm";
				break;
			
			case STARTED:
				htmltext = "30612-4.htm";
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
		
		dropItems(st.getPlayer(), SPELLBOOK_PAGE, 1, 0, CHANCES.get(npc.getNpcId()));
	}
}