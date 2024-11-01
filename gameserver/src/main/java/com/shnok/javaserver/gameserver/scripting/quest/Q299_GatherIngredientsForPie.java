package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q299_GatherIngredientsForPie extends Quest
{
	private static final String QUEST_NAME = "Q299_GatherIngredientsForPie";
	
	// NPCs
	private static final int LARA = 30063;
	private static final int BRIGHT = 30466;
	private static final int EMILY = 30620;
	
	// Items
	private static final int FRUIT_BASKET = 7136;
	private static final int AVELLAN_SPICE = 7137;
	private static final int HONEY_POUCH = 7138;
	
	public Q299_GatherIngredientsForPie()
	{
		super(299, "Gather Ingredients for Pie");
		
		setItemsIds(FRUIT_BASKET, AVELLAN_SPICE, HONEY_POUCH);
		
		addQuestStart(EMILY);
		addTalkId(EMILY, LARA, BRIGHT);
		
		addMyDying(20934, 20935); // Wasp Worker, Wasp Leader
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30620-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30620-3.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HONEY_POUCH, -1);
		}
		else if (event.equalsIgnoreCase("30063-1.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, AVELLAN_SPICE, 1);
		}
		else if (event.equalsIgnoreCase("30620-5.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, AVELLAN_SPICE, 1);
		}
		else if (event.equalsIgnoreCase("30466-1.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, FRUIT_BASKET, 1);
		}
		else if (event.equalsIgnoreCase("30620-7a.htm"))
		{
			if (player.getInventory().hasItem(FRUIT_BASKET))
			{
				htmltext = "30620-7.htm";
				takeItems(player, FRUIT_BASKET, 1);
				rewardItems(player, 57, 25000);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				st.setCond(5);
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
				htmltext = (player.getStatus().getLevel() < 34) ? "30620-0a.htm" : "30620-0.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case EMILY:
						if (cond == 1)
							htmltext = "30620-1a.htm";
						else if (cond == 2)
						{
							if (player.getInventory().getItemCount(HONEY_POUCH) >= 100)
								htmltext = "30620-2.htm";
							else
							{
								htmltext = "30620-2a.htm";
								st.exitQuest(true);
							}
						}
						else if (cond == 3)
							htmltext = "30620-3a.htm";
						else if (cond == 4)
						{
							if (player.getInventory().hasItem(AVELLAN_SPICE))
								htmltext = "30620-4.htm";
							else
							{
								htmltext = "30620-4a.htm";
								st.exitQuest(true);
							}
						}
						else if (cond == 5)
							htmltext = "30620-5a.htm";
						else if (cond == 6)
							htmltext = "30620-6.htm";
						break;
					
					case LARA:
						if (cond == 3)
							htmltext = "30063-0.htm";
						else if (cond > 3)
							htmltext = "30063-1a.htm";
						break;
					
					case BRIGHT:
						if (cond == 5)
							htmltext = "30466-0.htm";
						else if (cond > 5)
							htmltext = "30466-1a.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMember(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(st.getPlayer(), HONEY_POUCH, 1, 100, (npc.getNpcId() == 20934) ? 571000 : 625000))
			st.setCond(2);
	}
}