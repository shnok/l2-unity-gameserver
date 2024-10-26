package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q020_BringUpWithLove extends Quest
{
	public static final String QUEST_NAME = "Q020_BringUpWithLove";
	
	// Item
	private static final int JEWEL_OF_INNOCENCE = 7185;
	
	public Q020_BringUpWithLove()
	{
		super(20, "Bring Up With Love");
		
		setItemsIds(JEWEL_OF_INNOCENCE);
		
		addQuestStart(31537); // Tunatun
		addTalkId(31537);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31537-09.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31537-12.htm"))
		{
			takeItems(player, JEWEL_OF_INNOCENCE, -1);
			rewardItems(player, 57, 68500);
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
				htmltext = (player.getStatus().getLevel() < 65) ? "31537-02.htm" : "31537-01.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 2)
					htmltext = "31537-11.htm";
				else
					htmltext = "31537-10.htm";
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}