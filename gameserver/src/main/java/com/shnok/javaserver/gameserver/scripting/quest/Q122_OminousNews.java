package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q122_OminousNews extends Quest
{
	private static final String QUEST_NAME = "Q122_OminousNews";
	
	// NPCs
	private static final int MOIRA = 31979;
	private static final int KARUDA = 32017;
	
	public Q122_OminousNews()
	{
		super(122, "Ominous News");
		
		addQuestStart(MOIRA);
		addTalkId(MOIRA, KARUDA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31979-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32017-02.htm"))
		{
			rewardItems(player, 57, 1695);
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
				htmltext = (player.getStatus().getLevel() < 20) ? "31979-01.htm" : "31979-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case MOIRA:
						htmltext = "31979-03.htm";
						break;
					
					case KARUDA:
						htmltext = "32017-01.htm";
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