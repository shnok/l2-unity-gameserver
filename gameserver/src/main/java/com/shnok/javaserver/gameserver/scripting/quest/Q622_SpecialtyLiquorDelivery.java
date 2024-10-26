package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q622_SpecialtyLiquorDelivery extends Quest
{
	private static final String QUEST_NAME = "Q622_SpecialtyLiquorDelivery";
	
	// Items
	private static final int SPECIAL_DRINK = 7197;
	private static final int FEE_OF_SPECIAL_DRINK = 7198;
	
	// NPCs
	private static final int JEREMY = 31521;
	private static final int PULIN = 31543;
	private static final int NAFF = 31544;
	private static final int CROCUS = 31545;
	private static final int KUBER = 31546;
	private static final int BEOLIN = 31547;
	private static final int LIETTA = 31267;
	
	// Rewards
	private static final int ADENA = 57;
	private static final int HASTE_POTION = 734;
	private static final int[] REWARDS =
	{
		6847,
		6849,
		6851
	};
	
	public Q622_SpecialtyLiquorDelivery()
	{
		super(622, "Specialty Liquor Delivery");
		
		setItemsIds(SPECIAL_DRINK, FEE_OF_SPECIAL_DRINK);
		
		addQuestStart(JEREMY);
		addTalkId(JEREMY, PULIN, NAFF, CROCUS, KUBER, BEOLIN, LIETTA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31521-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, SPECIAL_DRINK, 5);
		}
		else if (event.equalsIgnoreCase("31547-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SPECIAL_DRINK, 1);
			giveItems(player, FEE_OF_SPECIAL_DRINK, 1);
		}
		else if (event.equalsIgnoreCase("31546-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SPECIAL_DRINK, 1);
			giveItems(player, FEE_OF_SPECIAL_DRINK, 1);
		}
		else if (event.equalsIgnoreCase("31545-02.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SPECIAL_DRINK, 1);
			giveItems(player, FEE_OF_SPECIAL_DRINK, 1);
		}
		else if (event.equalsIgnoreCase("31544-02.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SPECIAL_DRINK, 1);
			giveItems(player, FEE_OF_SPECIAL_DRINK, 1);
		}
		else if (event.equalsIgnoreCase("31543-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SPECIAL_DRINK, 1);
			giveItems(player, FEE_OF_SPECIAL_DRINK, 1);
		}
		else if (event.equalsIgnoreCase("31521-06.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, FEE_OF_SPECIAL_DRINK, 5);
		}
		else if (event.equalsIgnoreCase("31267-02.htm"))
		{
			if (Rnd.get(5) < 1)
				giveItems(player, Rnd.get(REWARDS), 1);
			else
			{
				rewardItems(player, ADENA, 18800);
				rewardItems(player, HASTE_POTION, 1);
			}
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				htmltext = (player.getStatus().getLevel() < 68) ? "31521-03.htm" : "31521-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case JEREMY:
						if (cond < 6)
							htmltext = "31521-04.htm";
						else if (cond == 6)
							htmltext = "31521-05.htm";
						else if (cond == 7)
							htmltext = "31521-06.htm";
						break;
					
					case BEOLIN:
						if (cond == 1 && player.getInventory().getItemCount(SPECIAL_DRINK) == 5)
							htmltext = "31547-01.htm";
						else if (cond > 1)
							htmltext = "31547-03.htm";
						break;
					
					case KUBER:
						if (cond == 2 && player.getInventory().getItemCount(SPECIAL_DRINK) == 4)
							htmltext = "31546-01.htm";
						else if (cond > 2)
							htmltext = "31546-03.htm";
						break;
					
					case CROCUS:
						if (cond == 3 && player.getInventory().getItemCount(SPECIAL_DRINK) == 3)
							htmltext = "31545-01.htm";
						else if (cond > 3)
							htmltext = "31545-03.htm";
						break;
					
					case NAFF:
						if (cond == 4 && player.getInventory().getItemCount(SPECIAL_DRINK) == 2)
							htmltext = "31544-01.htm";
						else if (cond > 4)
							htmltext = "31544-03.htm";
						break;
					
					case PULIN:
						if (cond == 5 && player.getInventory().getItemCount(SPECIAL_DRINK) == 1)
							htmltext = "31543-01.htm";
						else if (cond > 5)
							htmltext = "31543-03.htm";
						break;
					
					case LIETTA:
						if (cond == 7)
							htmltext = "31267-01.htm";
						break;
				}
		}
		
		return htmltext;
	}
}