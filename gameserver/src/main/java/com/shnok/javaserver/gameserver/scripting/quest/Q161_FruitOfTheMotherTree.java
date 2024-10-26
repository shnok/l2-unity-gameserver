package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q161_FruitOfTheMotherTree extends Quest
{
	private static final String QUEST_NAME = "Q161_FruitOfTheMotherTree";
	
	// NPCs
	private static final int ANDELLIA = 30362;
	private static final int THALIA = 30371;
	
	// Items
	private static final int ANDELLIA_LETTER = 1036;
	private static final int MOTHERTREE_FRUIT = 1037;
	
	public Q161_FruitOfTheMotherTree()
	{
		super(161, "Fruit of the Mothertree");
		
		setItemsIds(ANDELLIA_LETTER, MOTHERTREE_FRUIT);
		
		addQuestStart(ANDELLIA);
		addTalkId(ANDELLIA, THALIA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30362-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ANDELLIA_LETTER, 1);
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
				if (player.getRace() != ClassRace.ELF)
					htmltext = "30362-00.htm";
				else if (player.getStatus().getLevel() < 3)
					htmltext = "30362-02.htm";
				else
					htmltext = "30362-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ANDELLIA:
						if (cond == 1)
							htmltext = "30362-05.htm";
						else if (cond == 2)
						{
							htmltext = "30362-06.htm";
							takeItems(player, MOTHERTREE_FRUIT, 1);
							rewardItems(player, 57, 1000);
							rewardExpAndSp(player, 1000, 0);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case THALIA:
						if (cond == 1)
						{
							htmltext = "30371-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ANDELLIA_LETTER, 1);
							giveItems(player, MOTHERTREE_FRUIT, 1);
						}
						else if (cond == 2)
							htmltext = "30371-02.htm";
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