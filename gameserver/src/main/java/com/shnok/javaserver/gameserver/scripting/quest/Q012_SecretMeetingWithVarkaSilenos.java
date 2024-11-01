package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q012_SecretMeetingWithVarkaSilenos extends Quest
{
	private static final String QUEST_NAME = "Q012_SecretMeetingWithVarkaSilenos";
	
	// NPCs
	private static final int CADMON = 31296;
	private static final int HELMUT = 31258;
	private static final int NARAN_ASHANUK = 31378;
	
	// Items
	private static final int MUNITIONS_BOX = 7232;
	
	public Q012_SecretMeetingWithVarkaSilenos()
	{
		super(12, "Secret Meeting With Varka Silenos");
		
		setItemsIds(MUNITIONS_BOX);
		
		addQuestStart(CADMON);
		addTalkId(CADMON, HELMUT, NARAN_ASHANUK);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31296-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31258-02.htm"))
		{
			giveItems(player, MUNITIONS_BOX, 1);
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31378-02.htm"))
		{
			takeItems(player, MUNITIONS_BOX, 1);
			rewardExpAndSp(player, 79761, 0);
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
				htmltext = (player.getStatus().getLevel() < 74) ? "31296-02.htm" : "31296-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case CADMON:
						if (cond == 1)
							htmltext = "31296-04.htm";
						break;
					
					case HELMUT:
						if (cond == 1)
							htmltext = "31258-01.htm";
						else if (cond == 2)
							htmltext = "31258-03.htm";
						break;
					
					case NARAN_ASHANUK:
						if (cond == 2)
							htmltext = "31378-01.htm";
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