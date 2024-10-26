package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q017_LightAndDarkness extends Quest
{
	private static final String QUEST_NAME = "Q017_LightAndDarkness";
	
	// Items
	private static final int BLOOD_OF_SAINT = 7168;
	
	// NPCs
	private static final int HIERARCH = 31517;
	private static final int SAINT_ALTAR_1 = 31508;
	private static final int SAINT_ALTAR_2 = 31509;
	private static final int SAINT_ALTAR_3 = 31510;
	private static final int SAINT_ALTAR_4 = 31511;
	
	public Q017_LightAndDarkness()
	{
		super(17, "Light and Darkness");
		
		setItemsIds(BLOOD_OF_SAINT);
		
		addQuestStart(HIERARCH);
		addTalkId(HIERARCH, SAINT_ALTAR_1, SAINT_ALTAR_2, SAINT_ALTAR_3, SAINT_ALTAR_4);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31517-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, BLOOD_OF_SAINT, 4);
		}
		else if (event.equalsIgnoreCase("31508-02.htm"))
		{
			if (player.getInventory().hasItem(BLOOD_OF_SAINT))
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BLOOD_OF_SAINT, 1);
			}
			else
				htmltext = "31508-03.htm";
		}
		else if (event.equalsIgnoreCase("31509-02.htm"))
		{
			if (player.getInventory().hasItem(BLOOD_OF_SAINT))
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BLOOD_OF_SAINT, 1);
			}
			else
				htmltext = "31509-03.htm";
		}
		else if (event.equalsIgnoreCase("31510-02.htm"))
		{
			if (player.getInventory().hasItem(BLOOD_OF_SAINT))
			{
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BLOOD_OF_SAINT, 1);
			}
			else
				htmltext = "31510-03.htm";
		}
		else if (event.equalsIgnoreCase("31511-02.htm"))
		{
			if (player.getInventory().hasItem(BLOOD_OF_SAINT))
			{
				st.setCond(5);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BLOOD_OF_SAINT, 1);
			}
			else
				htmltext = "31511-03.htm";
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
				htmltext = (player.getStatus().getLevel() < 61) ? "31517-03.htm" : "31517-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case HIERARCH:
						if (cond == 5)
						{
							htmltext = "31517-07.htm";
							rewardExpAndSp(player, 105527, 0);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						else
						{
							if (player.getInventory().hasItem(BLOOD_OF_SAINT))
								htmltext = "31517-05.htm";
							else
							{
								htmltext = "31517-06.htm";
								st.exitQuest(true);
							}
						}
						break;
					
					case SAINT_ALTAR_1:
						if (cond == 1)
							htmltext = "31508-01.htm";
						else if (cond > 1)
							htmltext = "31508-04.htm";
						break;
					
					case SAINT_ALTAR_2:
						if (cond == 2)
							htmltext = "31509-01.htm";
						else if (cond > 2)
							htmltext = "31509-04.htm";
						break;
					
					case SAINT_ALTAR_3:
						if (cond == 3)
							htmltext = "31510-01.htm";
						else if (cond > 3)
							htmltext = "31510-04.htm";
						break;
					
					case SAINT_ALTAR_4:
						if (cond == 4)
							htmltext = "31511-01.htm";
						else if (cond > 4)
							htmltext = "31511-04.htm";
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