package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q018_MeetingWithTheGoldenRam extends Quest
{
	private static final String QUEST_NAME = "Q018_MeetingWithTheGoldenRam";
	
	// Items
	private static final int SUPPLY_BOX = 7245;
	
	// NPCs
	private static final int DONAL = 31314;
	private static final int DAISY = 31315;
	private static final int ABERCROMBIE = 31555;
	
	public Q018_MeetingWithTheGoldenRam()
	{
		super(18, "Meeting with the Golden Ram");
		
		setItemsIds(SUPPLY_BOX);
		
		addQuestStart(DONAL);
		addTalkId(DONAL, DAISY, ABERCROMBIE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31314-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31315-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, SUPPLY_BOX, 1);
		}
		else if (event.equalsIgnoreCase("31555-02.htm"))
		{
			takeItems(player, SUPPLY_BOX, 1);
			rewardItems(player, 57, 15000);
			rewardExpAndSp(player, 50000, 0);
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
				htmltext = (player.getStatus().getLevel() < 66) ? "31314-02.htm" : "31314-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case DONAL:
						htmltext = "31314-04.htm";
						break;
					
					case DAISY:
						if (cond == 1)
							htmltext = "31315-01.htm";
						else if (cond == 2)
							htmltext = "31315-03.htm";
						break;
					
					case ABERCROMBIE:
						if (cond == 2)
							htmltext = "31555-01.htm";
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