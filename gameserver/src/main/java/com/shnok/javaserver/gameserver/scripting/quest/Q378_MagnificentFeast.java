package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q378_MagnificentFeast extends Quest
{
	private static final String QUEST_NAME = "Q378_MagnificentFeast";
	
	// NPC
	private static final int RANSPO = 30594;
	
	// Items
	private static final int WINE_15 = 5956;
	private static final int WINE_30 = 5957;
	private static final int WINE_60 = 5958;
	private static final int MUSICAL_SCORE = 4421;
	private static final int SALAD_RECIPE = 1455;
	private static final int SAUCE_RECIPE = 1456;
	private static final int STEAK_RECIPE = 1457;
	private static final int RITRON_DESSERT = 5959;
	
	// Rewards
	private static final Map<String, int[]> REWARDS = HashMap.newHashMap(9);
	
	public Q378_MagnificentFeast()
	{
		super(378, "Magnificent Feast");
		
		REWARDS.put("9", new int[]
		{
			847,
			1,
			5700
		});
		REWARDS.put("10", new int[]
		{
			846,
			2,
			0
		});
		REWARDS.put("12", new int[]
		{
			909,
			1,
			25400
		});
		REWARDS.put("17", new int[]
		{
			846,
			2,
			1200
		});
		REWARDS.put("18", new int[]
		{
			879,
			1,
			6900
		});
		REWARDS.put("20", new int[]
		{
			890,
			2,
			8500
		});
		REWARDS.put("33", new int[]
		{
			879,
			1,
			8100
		});
		REWARDS.put("34", new int[]
		{
			910,
			1,
			0
		});
		REWARDS.put("36", new int[]
		{
			848,
			1,
			2200
		});
		
		addQuestStart(RANSPO);
		addTalkId(RANSPO);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30594-2.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30594-4a.htm"))
		{
			if (player.getInventory().hasItem(WINE_15))
			{
				st.setCond(2);
				st.set("score", 1);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, WINE_15, 1);
			}
			else
				htmltext = "30594-4.htm";
		}
		else if (event.equalsIgnoreCase("30594-4b.htm"))
		{
			if (player.getInventory().hasItem(WINE_30))
			{
				st.setCond(2);
				st.set("score", 2);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, WINE_30, 1);
			}
			else
				htmltext = "30594-4.htm";
		}
		else if (event.equalsIgnoreCase("30594-4c.htm"))
		{
			if (player.getInventory().hasItem(WINE_60))
			{
				st.setCond(2);
				st.set("score", 4);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, WINE_60, 1);
			}
			else
				htmltext = "30594-4.htm";
		}
		else if (event.equalsIgnoreCase("30594-6.htm"))
		{
			if (player.getInventory().hasItem(MUSICAL_SCORE))
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, MUSICAL_SCORE, 1);
			}
			else
				htmltext = "30594-5.htm";
		}
		else
		{
			int score = st.getInteger("score");
			if (event.equalsIgnoreCase("30594-8a.htm"))
			{
				if (player.getInventory().hasItem(SALAD_RECIPE))
				{
					st.setCond(4);
					st.set("score", score + 8);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, SALAD_RECIPE, 1);
				}
				else
					htmltext = "30594-8.htm";
			}
			else if (event.equalsIgnoreCase("30594-8b.htm"))
			{
				if (player.getInventory().hasItem(SAUCE_RECIPE))
				{
					st.setCond(4);
					st.set("score", score + 16);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, SAUCE_RECIPE, 1);
				}
				else
					htmltext = "30594-8.htm";
			}
			else if (event.equalsIgnoreCase("30594-8c.htm"))
			{
				if (player.getInventory().hasItem(STEAK_RECIPE))
				{
					st.setCond(4);
					st.set("score", score + 32);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, STEAK_RECIPE, 1);
				}
				else
					htmltext = "30594-8.htm";
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
				htmltext = (player.getStatus().getLevel() < 20) ? "30594-0.htm" : "30594-1.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "30594-3.htm";
				else if (cond == 2)
					htmltext = (!player.getInventory().hasItem(MUSICAL_SCORE)) ? "30594-5.htm" : "30594-5a.htm";
				else if (cond == 3)
					htmltext = "30594-7.htm";
				else if (cond == 4)
				{
					final String score = st.get("score");
					if (REWARDS.containsKey(score) && player.getInventory().hasItem(RITRON_DESSERT))
					{
						htmltext = "30594-10.htm";
						
						takeItems(player, RITRON_DESSERT, 1);
						giveItems(player, REWARDS.get(score)[0], REWARDS.get(score)[1]);
						
						int adena = REWARDS.get(score)[2];
						if (adena > 0)
							rewardItems(player, 57, adena);
						
						playSound(player, SOUND_FINISH);
						st.exitQuest(true);
					}
					else
						htmltext = "30594-9.htm";
				}
		}
		
		return htmltext;
	}
}