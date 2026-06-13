package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q110_ToThePrimevalIsle extends Quest
{
	private static final String QUEST_NAME = "Q110_ToThePrimevalIsle";
	
	// NPCs
	private static final int ANTON = 31338;
	private static final int MARQUEZ = 32113;
	
	// Item
	private static final int ANCIENT_BOOK = 8777;
	
	public Q110_ToThePrimevalIsle()
	{
		super(110, "To the Primeval Isle");
		
		setItemsIds(ANCIENT_BOOK);
		
		addQuestStart(ANTON);
		addTalkId(ANTON, MARQUEZ);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31338-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ANCIENT_BOOK, 1);
		}
		else if (event.equalsIgnoreCase("32113-03.htm") && player.getInventory().hasItem(ANCIENT_BOOK))
		{
			takeItems(player, ANCIENT_BOOK, 1);
			rewardItems(player, 57, 169380);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 75) ? "31338-00.htm" : "31338-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case ANTON:
						htmltext = "31338-01c.htm";
						break;
					
					case MARQUEZ:
						htmltext = "32113-01.htm";
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