package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q027_ChestCaughtWithABaitOfWind extends Quest
{
	private static final String QUEST_NAME = "Q027_ChestCaughtWithABaitOfWind";
	
	// NPCs
	private static final int LANOSCO = 31570;
	private static final int SHALING = 31434;
	
	// Items
	private static final int LARGE_BLUE_TREASURE_CHEST = 6500;
	private static final int STRANGE_BLUEPRINT = 7625;
	private static final int BLACK_PEARL_RING = 880;
	
	public Q027_ChestCaughtWithABaitOfWind()
	{
		super(27, "Chest caught with a bait of wind");
		
		setItemsIds(STRANGE_BLUEPRINT);
		
		addQuestStart(LANOSCO);
		addTalkId(LANOSCO, SHALING);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31570-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31570-07.htm"))
		{
			if (player.getInventory().hasItem(LARGE_BLUE_TREASURE_CHEST))
			{
				st.setCond(2);
				takeItems(player, LARGE_BLUE_TREASURE_CHEST, 1);
				giveItems(player, STRANGE_BLUEPRINT, 1);
			}
			else
				htmltext = "31570-08.htm";
		}
		else if (event.equalsIgnoreCase("31434-02.htm"))
		{
			if (player.getInventory().hasItem(STRANGE_BLUEPRINT))
			{
				htmltext = "31434-02.htm";
				takeItems(player, STRANGE_BLUEPRINT, 1);
				giveItems(player, BLACK_PEARL_RING, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "31434-03.htm";
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
				if (player.getStatus().getLevel() < 27)
					htmltext = "31570-02.htm";
				else
				{
					QuestState st2 = player.getQuestList().getQuestState("Q050_LanoscosSpecialBait");
					if (st2 != null && st2.isCompleted())
						htmltext = "31570-01.htm";
					else
						htmltext = "31570-03.htm";
				}
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case LANOSCO:
						if (cond == 1)
							htmltext = (!player.getInventory().hasItem(LARGE_BLUE_TREASURE_CHEST)) ? "31570-06.htm" : "31570-05.htm";
						else if (cond == 2)
							htmltext = "31570-09.htm";
						break;
					
					case SHALING:
						if (cond == 2)
							htmltext = "31434-01.htm";
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