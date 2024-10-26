package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q008_AnAdventureBegins extends Quest
{
	private static final String QUEST_NAME = "Q008_AnAdventureBegins";
	
	// NPCs
	private static final int JASMINE = 30134;
	private static final int ROSELYN = 30355;
	private static final int HARNE = 30144;
	
	// Items
	private static final int ROSELYN_NOTE = 7573;
	
	// Rewards
	private static final int SOE_GIRAN = 7559;
	private static final int MARK_TRAVELER = 7570;
	
	public Q008_AnAdventureBegins()
	{
		super(8, "An Adventure Begins");
		
		setItemsIds(ROSELYN_NOTE);
		
		addQuestStart(JASMINE);
		addTalkId(JASMINE, ROSELYN, HARNE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30134-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30355-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, ROSELYN_NOTE, 1);
		}
		else if (event.equalsIgnoreCase("30144-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ROSELYN_NOTE, 1);
		}
		else if (event.equalsIgnoreCase("30134-06.htm"))
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
				if (player.getStatus().getLevel() >= 3 && player.getRace() == ClassRace.DARK_ELF)
					htmltext = "30134-02.htm";
				else
					htmltext = "30134-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case JASMINE:
						if (cond == 1 || cond == 2)
							htmltext = "30134-04.htm";
						else if (cond == 3)
							htmltext = "30134-05.htm";
						break;
					
					case ROSELYN:
						if (cond == 1)
							htmltext = "30355-01.htm";
						else if (cond == 2)
							htmltext = "30355-03.htm";
						break;
					
					case HARNE:
						if (cond == 2)
							htmltext = "30144-01.htm";
						else if (cond == 3)
							htmltext = "30144-03.htm";
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