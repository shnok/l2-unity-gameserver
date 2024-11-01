package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q009_IntoTheCityOfHumans extends Quest
{
	private static final String QUEST_NAME = "Q009_IntoTheCityOfHumans";
	
	// NPCs
	private static final int PETUKAI = 30583;
	private static final int TANAPI = 30571;
	private static final int TAMIL = 30576;
	
	// Rewards
	private static final int SOE_GIRAN = 7559;
	private static final int MARK_OF_TRAVELER = 7570;
	
	public Q009_IntoTheCityOfHumans()
	{
		super(9, "Into the City of Humans");
		
		addQuestStart(PETUKAI);
		addTalkId(PETUKAI, TANAPI, TAMIL);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30583-01.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30571-01.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30576-01.htm"))
		{
			giveItems(player, MARK_OF_TRAVELER, 1);
			rewardItems(player, SOE_GIRAN, 1);
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
				if (player.getStatus().getLevel() >= 3 && player.getRace() == ClassRace.ORC)
					htmltext = "30583-00.htm";
				else
					htmltext = "30583-00a.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case PETUKAI:
						if (cond == 1)
							htmltext = "30583-01a.htm";
						break;
					
					case TANAPI:
						if (cond == 1)
							htmltext = "30571-00.htm";
						else if (cond == 2)
							htmltext = "30571-01a.htm";
						break;
					
					case TAMIL:
						if (cond == 2)
							htmltext = "30576-00.htm";
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