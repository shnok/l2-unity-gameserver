package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q047_IntoTheDarkForest extends Quest
{
	private static final String QUEST_NAME = "Q047_IntoTheDarkForest";
	
	// NPCs
	private static final int GALLADUCCI = 30097;
	private static final int GENTLER = 30094;
	private static final int SANDRA = 30090;
	private static final int DUSTIN = 30116;
	
	// Items
	private static final int ORDER_DOCUMENT_1 = 7563;
	private static final int ORDER_DOCUMENT_2 = 7564;
	private static final int ORDER_DOCUMENT_3 = 7565;
	private static final int MAGIC_SWORD_HILT = 7568;
	private static final int GEMSTONE_POWDER = 7567;
	private static final int PURIFIED_MAGIC_NECKLACE = 7566;
	private static final int MARK_OF_TRAVELER = 7570;
	private static final int SCROLL_OF_ESCAPE_SPECIAL = 7556;
	
	public Q047_IntoTheDarkForest()
	{
		super(47, "Into the Dark Forest");
		
		setItemsIds(ORDER_DOCUMENT_1, ORDER_DOCUMENT_2, ORDER_DOCUMENT_3, MAGIC_SWORD_HILT, GEMSTONE_POWDER, PURIFIED_MAGIC_NECKLACE);
		
		addQuestStart(GALLADUCCI);
		addTalkId(GALLADUCCI, SANDRA, DUSTIN, GENTLER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30097-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ORDER_DOCUMENT_1, 1);
		}
		else if (event.equalsIgnoreCase("30094-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ORDER_DOCUMENT_1, 1);
			giveItems(player, MAGIC_SWORD_HILT, 1);
		}
		else if (event.equalsIgnoreCase("30097-06.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MAGIC_SWORD_HILT, 1);
			giveItems(player, ORDER_DOCUMENT_2, 1);
		}
		else if (event.equalsIgnoreCase("30090-02.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ORDER_DOCUMENT_2, 1);
			giveItems(player, GEMSTONE_POWDER, 1);
		}
		else if (event.equalsIgnoreCase("30097-09.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, GEMSTONE_POWDER, 1);
			giveItems(player, ORDER_DOCUMENT_3, 1);
		}
		else if (event.equalsIgnoreCase("30116-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ORDER_DOCUMENT_3, 1);
			giveItems(player, PURIFIED_MAGIC_NECKLACE, 1);
		}
		else if (event.equalsIgnoreCase("30097-12.htm"))
		{
			takeItems(player, MARK_OF_TRAVELER, -1);
			takeItems(player, PURIFIED_MAGIC_NECKLACE, 1);
			rewardItems(player, SCROLL_OF_ESCAPE_SPECIAL, 1);
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
				if (player.getRace() == ClassRace.DARK_ELF && player.getStatus().getLevel() >= 3)
				{
					if (player.getInventory().hasItem(MARK_OF_TRAVELER))
						htmltext = "30097-02.htm";
					else
						htmltext = "30097-01.htm";
				}
				else
					htmltext = "30097-01a.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case GALLADUCCI:
						if (cond == 1)
							htmltext = "30097-04.htm";
						else if (cond == 2)
							htmltext = "30097-05.htm";
						else if (cond == 3)
							htmltext = "30097-07.htm";
						else if (cond == 4)
							htmltext = "30097-08.htm";
						else if (cond == 5)
							htmltext = "30097-10.htm";
						else if (cond == 6)
							htmltext = "30097-11.htm";
						break;
					
					case GENTLER:
						if (cond == 1)
							htmltext = "30094-01.htm";
						else if (cond > 1)
							htmltext = "30094-03.htm";
						break;
					
					case SANDRA:
						if (cond == 3)
							htmltext = "30090-01.htm";
						else if (cond > 3)
							htmltext = "30090-03.htm";
						break;
					
					case DUSTIN:
						if (cond == 5)
							htmltext = "30116-01.htm";
						else if (cond == 6)
							htmltext = "30116-03.htm";
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