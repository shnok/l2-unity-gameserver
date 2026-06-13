package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q002_WhatWomenWant extends Quest
{
	private static final String QUEST_NAME = "Q002_WhatWomenWant";
	
	// NPCs
	private static final int ARUJIEN = 30223;
	private static final int MIRABEL = 30146;
	private static final int HERBIEL = 30150;
	private static final int GREENIS = 30157;
	
	// Items
	private static final int ARUJIEN_LETTER_1 = 1092;
	private static final int ARUJIEN_LETTER_2 = 1093;
	private static final int ARUJIEN_LETTER_3 = 1094;
	private static final int POETRY_BOOK = 689;
	private static final int GREENIS_LETTER = 693;
	
	// Rewards
	private static final int MYSTICS_EARRING = 113;
	
	public Q002_WhatWomenWant()
	{
		super(2, "What Women Want");
		
		setItemsIds(ARUJIEN_LETTER_1, ARUJIEN_LETTER_2, ARUJIEN_LETTER_3, POETRY_BOOK, GREENIS_LETTER);
		
		addQuestStart(ARUJIEN);
		addTalkId(ARUJIEN, MIRABEL, HERBIEL, GREENIS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30223-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ARUJIEN_LETTER_1, 1);
		}
		else if (event.equalsIgnoreCase("30223-08.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ARUJIEN_LETTER_3, 1);
			giveItems(player, POETRY_BOOK, 1);
		}
		else if (event.equalsIgnoreCase("30223-09.htm"))
		{
			takeItems(player, ARUJIEN_LETTER_3, 1);
			rewardItems(player, 57, 450);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
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
				if (player.getRace() != ClassRace.ELF && player.getRace() != ClassRace.HUMAN)
					htmltext = "30223-00.htm";
				else if (player.getStatus().getLevel() < 2)
					htmltext = "30223-01.htm";
				else
					htmltext = "30223-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ARUJIEN:
						if (player.getInventory().hasItem(ARUJIEN_LETTER_1))
							htmltext = "30223-05.htm";
						else if (player.getInventory().hasItem(ARUJIEN_LETTER_3))
							htmltext = "30223-07.htm";
						else if (player.getInventory().hasItem(ARUJIEN_LETTER_2))
							htmltext = "30223-06.htm";
						else if (player.getInventory().hasItem(POETRY_BOOK))
							htmltext = "30223-11.htm";
						else if (player.getInventory().hasItem(GREENIS_LETTER))
						{
							htmltext = "30223-10.htm";
							takeItems(player, GREENIS_LETTER, 1);
							giveItems(player, MYSTICS_EARRING, 1);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case MIRABEL:
						if (cond == 1)
						{
							htmltext = "30146-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ARUJIEN_LETTER_1, 1);
							giveItems(player, ARUJIEN_LETTER_2, 1);
						}
						else if (cond > 1)
							htmltext = "30146-02.htm";
						break;
					
					case HERBIEL:
						if (cond == 2)
						{
							htmltext = "30150-01.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ARUJIEN_LETTER_2, 1);
							giveItems(player, ARUJIEN_LETTER_3, 1);
						}
						else if (cond > 2)
							htmltext = "30150-02.htm";
						break;
					
					case GREENIS:
						if (cond < 4)
							htmltext = "30157-01.htm";
						else if (cond == 4)
						{
							htmltext = "30157-02.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, POETRY_BOOK, 1);
							giveItems(player, GREENIS_LETTER, 1);
						}
						else if (cond == 5)
							htmltext = "30157-03.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}