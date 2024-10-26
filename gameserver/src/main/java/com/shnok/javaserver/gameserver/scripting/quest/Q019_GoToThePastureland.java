package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q019_GoToThePastureland extends Quest
{
	private static final String QUEST_NAME = "Q019_GoToThePastureland";
	
	// Items
	private static final int YOUNG_WILD_BEAST_MEAT = 7547;
	
	// NPCs
	private static final int VLADIMIR = 31302;
	private static final int TUNATUN = 31537;
	
	public Q019_GoToThePastureland()
	{
		super(19, "Go to the Pastureland!");
		
		setItemsIds(YOUNG_WILD_BEAST_MEAT);
		
		addQuestStart(VLADIMIR);
		addTalkId(VLADIMIR, TUNATUN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31302-01.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, YOUNG_WILD_BEAST_MEAT, 1);
		}
		else if (event.equalsIgnoreCase("019_finish"))
		{
			if (player.getInventory().hasItem(YOUNG_WILD_BEAST_MEAT))
			{
				htmltext = "31537-01.htm";
				takeItems(player, YOUNG_WILD_BEAST_MEAT, 1);
				rewardItems(player, 57, 30000);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "31537-02.htm";
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
				htmltext = (player.getStatus().getLevel() < 63) ? "31302-03.htm" : "31302-00.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case VLADIMIR:
						htmltext = "31302-02.htm";
						break;
					
					case TUNATUN:
						htmltext = "31537-00.htm";
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