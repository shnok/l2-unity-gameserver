package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q364_JovialAccordion extends Quest
{
	private static final String QUEST_NAME = "Q364_JovialAccordion";
	
	// NPCs
	private static final int BARBADO = 30959;
	private static final int SWAN = 30957;
	private static final int SABRIN = 30060;
	private static final int XABER = 30075;
	private static final int CLOTH_CHEST = 30961;
	private static final int BEER_CHEST = 30960;
	
	// Items
	private static final int KEY_1 = 4323;
	private static final int KEY_2 = 4324;
	private static final int STOLEN_BEER = 4321;
	private static final int STOLEN_CLOTHES = 4322;
	private static final int ECHO = 4421;
	
	public Q364_JovialAccordion()
	{
		super(364, "Jovial Accordion");
		
		setItemsIds(KEY_1, KEY_2, STOLEN_BEER, STOLEN_CLOTHES);
		
		addQuestStart(BARBADO);
		addTalkId(BARBADO, SWAN, SABRIN, XABER, CLOTH_CHEST, BEER_CHEST);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30959-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("items", 0);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30957-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, KEY_1, 1);
			giveItems(player, KEY_2, 1);
		}
		else if (event.equalsIgnoreCase("30960-04.htm"))
		{
			if (player.getInventory().hasItem(KEY_2))
			{
				takeItems(player, KEY_2, 1);
				if (Rnd.nextBoolean())
				{
					htmltext = "30960-02.htm";
					giveItems(player, STOLEN_BEER, 1);
					playSound(player, SOUND_ITEMGET);
				}
			}
		}
		else if (event.equalsIgnoreCase("30961-04.htm"))
		{
			if (player.getInventory().hasItem(KEY_1))
			{
				takeItems(player, KEY_1, 1);
				if (Rnd.nextBoolean())
				{
					htmltext = "30961-02.htm";
					giveItems(player, STOLEN_CLOTHES, 1);
					playSound(player, SOUND_ITEMGET);
				}
			}
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30959-00.htm" : "30959-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				final int stolenItems = st.getInteger("items");
				
				switch (npc.getNpcId())
				{
					case BARBADO:
						if (cond == 1 || cond == 2)
							htmltext = "30959-03.htm";
						else if (cond == 3)
						{
							htmltext = "30959-04.htm";
							giveItems(player, ECHO, 1);
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case SWAN:
						if (cond == 1)
							htmltext = "30957-01.htm";
						else if (cond == 2)
						{
							if (stolenItems > 0)
							{
								st.setCond(3);
								playSound(player, SOUND_MIDDLE);
								
								if (stolenItems == 2)
								{
									htmltext = "30957-04.htm";
									rewardItems(player, 57, 100);
								}
								else
									htmltext = "30957-05.htm";
							}
							else
							{
								if (!player.getInventory().hasItem(KEY_1) && !player.getInventory().hasItem(KEY_2))
								{
									htmltext = "30957-06.htm";
									playSound(player, SOUND_FINISH);
									st.exitQuest(true);
								}
								else
									htmltext = "30957-03.htm";
							}
						}
						else if (cond == 3)
							htmltext = "30957-07.htm";
						break;
					
					case BEER_CHEST:
						htmltext = "30960-03.htm";
						if (cond == 2 && player.getInventory().hasItem(KEY_2))
							htmltext = "30960-01.htm";
						break;
					
					case CLOTH_CHEST:
						htmltext = "30961-03.htm";
						if (cond == 2 && player.getInventory().hasItem(KEY_1))
							htmltext = "30961-01.htm";
						break;
					
					case SABRIN:
						if (player.getInventory().hasItem(STOLEN_BEER))
						{
							htmltext = "30060-01.htm";
							st.set("items", stolenItems + 1);
							playSound(player, SOUND_ITEMGET);
							takeItems(player, STOLEN_BEER, 1);
						}
						else
							htmltext = "30060-02.htm";
						break;
					
					case XABER:
						if (player.getInventory().hasItem(STOLEN_CLOTHES))
						{
							htmltext = "30075-01.htm";
							st.set("items", stolenItems + 1);
							playSound(player, SOUND_ITEMGET);
							takeItems(player, STOLEN_CLOTHES, 1);
						}
						else
							htmltext = "30075-02.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
}