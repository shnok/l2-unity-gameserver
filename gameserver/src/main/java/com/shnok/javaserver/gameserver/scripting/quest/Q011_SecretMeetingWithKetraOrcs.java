package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q011_SecretMeetingWithKetraOrcs extends Quest
{
	private static final String QUEST_NAME = "Q011_SecretMeetingWithKetraOrcs";
	
	// Npcs
	private static final int CADMON = 31296;
	private static final int LEON = 31256;
	private static final int WAHKAN = 31371;
	
	// Items
	private static final int MUNITIONS_BOX = 7231;
	
	public Q011_SecretMeetingWithKetraOrcs()
	{
		super(11, "Secret Meeting With Ketra Orcs");
		
		setItemsIds(MUNITIONS_BOX);
		
		addQuestStart(CADMON);
		addTalkId(CADMON, LEON, WAHKAN);
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
		else if (event.equalsIgnoreCase("31256-02.htm"))
		{
			giveItems(player, MUNITIONS_BOX, 1);
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31371-02.htm"))
		{
			takeItems(player, MUNITIONS_BOX, 1);
			rewardExpAndSp(player, 79787, 0);
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
					
					case LEON:
						if (cond == 1)
							htmltext = "31256-01.htm";
						else if (cond == 2)
							htmltext = "31256-03.htm";
						break;
					
					case WAHKAN:
						if (cond == 2)
							htmltext = "31371-01.htm";
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