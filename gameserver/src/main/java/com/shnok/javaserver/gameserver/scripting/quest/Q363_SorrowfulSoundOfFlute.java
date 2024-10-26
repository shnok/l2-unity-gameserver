package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q363_SorrowfulSoundOfFlute extends Quest
{
	private static final String QUEST_NAME = "Q363_SorrowfulSoundOfFlute";
	
	// NPCs
	private static final int NANARIN = 30956;
	private static final int OPIX = 30595;
	private static final int ALDO = 30057;
	private static final int RANSPO = 30594;
	private static final int HOLVAS = 30058;
	private static final int BARBADO = 30959;
	private static final int POITAN = 30458;
	
	// Item
	private static final int NANARIN_FLUTE = 4319;
	private static final int BLACK_BEER = 4320;
	private static final int CLOTHES = 4318;
	
	// Reward
	private static final int THEME_OF_SOLITUDE = 4420;
	
	public Q363_SorrowfulSoundOfFlute()
	{
		super(363, "Sorrowful Sound of Flute");
		
		setItemsIds(NANARIN_FLUTE, BLACK_BEER, CLOTHES);
		
		addQuestStart(NANARIN);
		addTalkId(NANARIN, OPIX, ALDO, RANSPO, HOLVAS, BARBADO, POITAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30956-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30956-05.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, CLOTHES, 1);
		}
		else if (event.equalsIgnoreCase("30956-06.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, NANARIN_FLUTE, 1);
		}
		else if (event.equalsIgnoreCase("30956-07.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, BLACK_BEER, 1);
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30956-03.htm" : "30956-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case NANARIN:
						if (cond == 1)
							htmltext = "30956-02.htm";
						else if (cond == 2)
							htmltext = "30956-04.htm";
						else if (cond == 3)
							htmltext = "30956-08.htm";
						else if (cond == 4)
						{
							if (st.getInteger("success") == 1)
							{
								htmltext = "30956-09.htm";
								giveItems(player, THEME_OF_SOLITUDE, 1);
								playSound(player, SOUND_FINISH);
							}
							else
							{
								htmltext = "30956-10.htm";
								playSound(player, SOUND_GIVEUP);
							}
							st.exitQuest(true);
						}
						break;
					
					case OPIX, POITAN, ALDO, RANSPO, HOLVAS:
						htmltext = npc.getNpcId() + "-01.htm";
						if (cond == 1)
						{
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
						}
						break;
					
					case BARBADO:
						if (cond == 3)
						{
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							
							if (player.getInventory().hasItem(NANARIN_FLUTE))
							{
								htmltext = "30959-02.htm";
								st.set("success", 1);
							}
							else
								htmltext = "30959-01.htm";
							
							takeItems(player, BLACK_BEER, -1);
							takeItems(player, CLOTHES, -1);
							takeItems(player, NANARIN_FLUTE, -1);
						}
						else if (cond == 4)
							htmltext = "30959-03.htm";
						break;
				}
		}
		
		return htmltext;
	}
}