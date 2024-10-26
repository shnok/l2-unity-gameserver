package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q030_ChestCaughtWithABaitOfFire extends Quest
{
	private static final String QUEST_NAME = "Q030_ChestCaughtWithABaitOfFire";
	
	// NPCs
	private static final int LINNAEUS = 31577;
	private static final int RUKAL = 30629;
	
	// Items
	private static final int RED_TREASURE_BOX = 6511;
	private static final int MUSICAL_SCORE = 7628;
	private static final int NECKLACE_OF_PROTECTION = 916;
	
	public Q030_ChestCaughtWithABaitOfFire()
	{
		super(30, "Chest caught with a bait of fire");
		
		setItemsIds(MUSICAL_SCORE);
		
		addQuestStart(LINNAEUS);
		addTalkId(LINNAEUS, RUKAL);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31577-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31577-07.htm"))
		{
			if (player.getInventory().hasItem(RED_TREASURE_BOX))
			{
				st.setCond(2);
				takeItems(player, RED_TREASURE_BOX, 1);
				giveItems(player, MUSICAL_SCORE, 1);
			}
			else
				htmltext = "31577-08.htm";
		}
		else if (event.equalsIgnoreCase("30629-02.htm"))
		{
			if (player.getInventory().hasItem(MUSICAL_SCORE))
			{
				htmltext = "30629-02.htm";
				takeItems(player, MUSICAL_SCORE, 1);
				giveItems(player, NECKLACE_OF_PROTECTION, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
			else
				htmltext = "30629-03.htm";
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
				if (player.getStatus().getLevel() < 60)
					htmltext = "31577-02.htm";
				else
				{
					QuestState st2 = player.getQuestList().getQuestState("Q053_LinnaeusSpecialBait");
					if (st2 != null && st2.isCompleted())
						htmltext = "31577-01.htm";
					else
						htmltext = "31577-03.htm";
				}
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case LINNAEUS:
						if (cond == 1)
							htmltext = (!player.getInventory().hasItem(RED_TREASURE_BOX)) ? "31577-06.htm" : "31577-05.htm";
						else if (cond == 2)
							htmltext = "31577-09.htm";
						break;
					
					case RUKAL:
						if (cond == 2)
							htmltext = "30629-01.htm";
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