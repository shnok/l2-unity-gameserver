package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q031_SecretBuriedInTheSwamp extends Quest
{
	private static final String QUEST_NAME = "Q031_SecretBuriedInTheSwamp";
	
	// Item
	private static final int KRORIN_JOURNAL = 7252;
	
	// NPCs
	private static final int ABERCROMBIE = 31555;
	private static final int FORGOTTEN_MONUMENT_1 = 31661;
	private static final int FORGOTTEN_MONUMENT_2 = 31662;
	private static final int FORGOTTEN_MONUMENT_3 = 31663;
	private static final int FORGOTTEN_MONUMENT_4 = 31664;
	private static final int CORPSE_OF_DWARF = 31665;
	
	public Q031_SecretBuriedInTheSwamp()
	{
		super(31, "Secret Buried in the Swamp");
		
		setItemsIds(KRORIN_JOURNAL);
		
		addQuestStart(ABERCROMBIE);
		addTalkId(ABERCROMBIE, CORPSE_OF_DWARF, FORGOTTEN_MONUMENT_1, FORGOTTEN_MONUMENT_2, FORGOTTEN_MONUMENT_3, FORGOTTEN_MONUMENT_4);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31555-01.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31665-01.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, KRORIN_JOURNAL, 1);
		}
		else if (event.equalsIgnoreCase("31555-04.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31661-01.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31662-01.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31663-01.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31664-01.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31555-07.htm"))
		{
			takeItems(player, KRORIN_JOURNAL, 1);
			rewardItems(player, 57, 40000);
			rewardExpAndSp(player, 130000, 0);
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
				htmltext = (player.getStatus().getLevel() < 66) ? "31555-00a.htm" : "31555-00.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ABERCROMBIE:
						if (cond == 1)
							htmltext = "31555-02.htm";
						else if (cond == 2)
							htmltext = "31555-03.htm";
						else if (cond > 2 && cond < 7)
							htmltext = "31555-05.htm";
						else if (cond == 7)
							htmltext = "31555-06.htm";
						break;
					
					case CORPSE_OF_DWARF:
						if (cond == 1)
							htmltext = "31665-00.htm";
						else if (cond > 1)
							htmltext = "31665-02.htm";
						break;
					
					case FORGOTTEN_MONUMENT_1:
						if (cond == 3)
							htmltext = "31661-00.htm";
						else if (cond > 3)
							htmltext = "31661-02.htm";
						break;
					
					case FORGOTTEN_MONUMENT_2:
						if (cond == 4)
							htmltext = "31662-00.htm";
						else if (cond > 4)
							htmltext = "31662-02.htm";
						break;
					
					case FORGOTTEN_MONUMENT_3:
						if (cond == 5)
							htmltext = "31663-00.htm";
						else if (cond > 5)
							htmltext = "31663-02.htm";
						break;
					
					case FORGOTTEN_MONUMENT_4:
						if (cond == 6)
							htmltext = "31664-00.htm";
						else if (cond > 6)
							htmltext = "31664-02.htm";
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