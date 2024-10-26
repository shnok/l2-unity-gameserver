package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q028_ChestCaughtWithABaitOfIcyAir extends Quest
{
	private static final String QUEST_NAME = "Q028_ChestCaughtWithABaitOfIcyAir";
	
	// NPCs
	private static final int OFULLE = 31572;
	private static final int KIKI = 31442;
	
	// Items
	private static final int BIG_YELLOW_TREASURE_CHEST = 6503;
	private static final int KIKI_LETTER = 7626;
	private static final int ELVEN_RING = 881;
	
	public Q028_ChestCaughtWithABaitOfIcyAir()
	{
		super(28, "Chest caught with a bait of icy air");
		
		setItemsIds(KIKI_LETTER);
		
		addQuestStart(OFULLE);
		addTalkId(OFULLE, KIKI);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31572-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31572-07.htm"))
		{
			if (player.getInventory().hasItem(BIG_YELLOW_TREASURE_CHEST))
			{
				st.setCond(2);
				takeItems(player, BIG_YELLOW_TREASURE_CHEST, 1);
				giveItems(player, KIKI_LETTER, 1);
			}
			else
				htmltext = "31572-08.htm";
		}
		else if (event.equalsIgnoreCase("31442-02.htm"))
		{
			if (player.getInventory().hasItem(KIKI_LETTER))
			{
				htmltext = "31442-02.htm";
				takeItems(player, KIKI_LETTER, 1);
				giveItems(player, ELVEN_RING, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "31442-03.htm";
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
				if (player.getStatus().getLevel() < 36)
					htmltext = "31572-02.htm";
				else
				{
					QuestState st2 = player.getQuestList().getQuestState("Q051_OFullesSpecialBait");
					if (st2 != null && st2.isCompleted())
						htmltext = "31572-01.htm";
					else
						htmltext = "31572-03.htm";
				}
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case OFULLE:
						if (cond == 1)
							htmltext = (!player.getInventory().hasItem(BIG_YELLOW_TREASURE_CHEST)) ? "31572-06.htm" : "31572-05.htm";
						else if (cond == 2)
							htmltext = "31572-09.htm";
						break;
					
					case KIKI:
						if (cond == 2)
							htmltext = "31442-01.htm";
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