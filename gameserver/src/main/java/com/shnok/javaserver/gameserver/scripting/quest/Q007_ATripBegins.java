package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q007_ATripBegins extends Quest
{
	private static final String QUEST_NAME = "Q007_ATripBegins";
	
	// NPCs
	private static final int MIRABEL = 30146;
	private static final int ARIEL = 30148;
	private static final int ASTERIOS = 30154;
	
	// Items
	private static final int ARIEL_RECO = 7572;
	
	// Rewards
	private static final int SOE_GIRAN = 7559;
	private static final int MARK_TRAVELER = 7570;
	
	public Q007_ATripBegins()
	{
		super(7, "A Trip Begins");
		
		setItemsIds(ARIEL_RECO);
		
		addQuestStart(MIRABEL);
		addTalkId(MIRABEL, ARIEL, ASTERIOS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30146-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30148-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, ARIEL_RECO, 1);
		}
		else if (event.equalsIgnoreCase("30154-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ARIEL_RECO, 1);
		}
		else if (event.equalsIgnoreCase("30146-06.htm"))
		{
			giveItems(player, MARK_TRAVELER, 1);
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
				if (player.getRace() != ClassRace.ELF)
					htmltext = "30146-01.htm";
				else if (player.getStatus().getLevel() < 3)
					htmltext = "30146-01a.htm";
				else
					htmltext = "30146-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case MIRABEL:
						if (cond == 1 || cond == 2)
							htmltext = "30146-04.htm";
						else if (cond == 3)
							htmltext = "30146-05.htm";
						break;
					
					case ARIEL:
						if (cond == 1)
							htmltext = "30148-01.htm";
						else if (cond == 2)
							htmltext = "30148-03.htm";
						break;
					
					case ASTERIOS:
						if (cond == 2)
							htmltext = "30154-01.htm";
						else if (cond == 3)
							htmltext = "30154-03.htm";
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