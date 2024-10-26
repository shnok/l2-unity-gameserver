package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q029_ChestCaughtWithABaitOfEarth extends Quest
{
	private static final String QUEST_NAME = "Q029_ChestCaughtWithABaitOfEarth";
	
	// NPCs
	private static final int WILLIE = 31574;
	private static final int ANABEL = 30909;
	
	// Items
	private static final int SMALL_PURPLE_TREASURE_CHEST = 6507;
	private static final int SMALL_GLASS_BOX = 7627;
	private static final int PLATED_LEATHER_GLOVES = 2455;
	
	public Q029_ChestCaughtWithABaitOfEarth()
	{
		super(29, "Chest caught with a bait of earth");
		
		setItemsIds(SMALL_GLASS_BOX);
		
		addQuestStart(WILLIE);
		addTalkId(WILLIE, ANABEL);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31574-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31574-07.htm"))
		{
			if (player.getInventory().hasItem(SMALL_PURPLE_TREASURE_CHEST))
			{
				st.setCond(2);
				takeItems(player, SMALL_PURPLE_TREASURE_CHEST, 1);
				giveItems(player, SMALL_GLASS_BOX, 1);
			}
			else
				htmltext = "31574-08.htm";
		}
		else if (event.equalsIgnoreCase("30909-02.htm"))
		{
			if (player.getInventory().hasItem(SMALL_GLASS_BOX))
			{
				htmltext = "30909-02.htm";
				takeItems(player, SMALL_GLASS_BOX, 1);
				giveItems(player, PLATED_LEATHER_GLOVES, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "30909-03.htm";
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
				if (player.getStatus().getLevel() < 48)
					htmltext = "31574-02.htm";
				else
				{
					QuestState st2 = player.getQuestList().getQuestState("Q052_WilliesSpecialBait");
					if (st2 != null && st2.isCompleted())
						htmltext = "31574-01.htm";
					else
						htmltext = "31574-03.htm";
				}
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case WILLIE:
						if (cond == 1)
							htmltext = (!player.getInventory().hasItem(SMALL_PURPLE_TREASURE_CHEST)) ? "31574-06.htm" : "31574-05.htm";
						else if (cond == 2)
							htmltext = "31574-09.htm";
						break;
					
					case ANABEL:
						if (cond == 2)
							htmltext = "30909-01.htm";
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