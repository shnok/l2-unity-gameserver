package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q648_AnIceMerchantsDream extends Quest
{
	private static final String QUEST_NAME = "Q648_AnIceMerchantsDream";
	private static final String qn2 = "Q115_TheOtherSideOfTruth";
	
	// Items
	private static final int SILVER_HEMOCYTE = 8057;
	private static final int SILVER_ICE_CRYSTAL = 8077;
	private static final int BLACK_ICE_CRYSTAL = 8078;
	
	// Rewards
	private static final Map<String, int[]> REWARDS = new HashMap<>();
	{
		REWARDS.put("a", new int[]
		{
			SILVER_ICE_CRYSTAL,
			23,
			1894 // Crafted Leather
		});
		REWARDS.put("b", new int[]
		{
			SILVER_ICE_CRYSTAL,
			6,
			1881 // Coarse Bone Powder
		});
		REWARDS.put("c", new int[]
		{
			SILVER_ICE_CRYSTAL,
			8,
			1880 // Steel
		});
		REWARDS.put("d", new int[]
		{
			BLACK_ICE_CRYSTAL,
			1800,
			729 // Scroll: Enchant Weapon (A-Grade)
		});
		REWARDS.put("e", new int[]
		{
			BLACK_ICE_CRYSTAL,
			240,
			730 // Scroll: Enchant Armor (A-Grade)
		});
		REWARDS.put("f", new int[]
		{
			BLACK_ICE_CRYSTAL,
			500,
			947 // Scroll: Enchant Weapon (B-Grade)
		});
		REWARDS.put("g", new int[]
		{
			BLACK_ICE_CRYSTAL,
			80,
			948 // Scroll: Enchant Armor (B-Grade)
		});
	}
	
	// NPCs
	private static final int RAFFORTY = 32020;
	private static final int ICE_SHELF = 32023;
	
	// Drop chances
	private static final Map<Integer, int[]> CHANCES = new HashMap<>();
	{
		CHANCES.put(22080, new int[]
		{
			285000,
			48000
		}); // Massive Maze Bandersnatch
		CHANCES.put(22081, new int[]
		{
			443000,
			0
		}); // Lost Watcher
		CHANCES.put(22082, new int[]
		{
			510000,
			0
		}); // Elder Lost Watcher
		CHANCES.put(22084, new int[]
		{
			477000,
			49000
		}); // Panthera
		CHANCES.put(22085, new int[]
		{
			420000,
			43000
		}); // Lost Gargoyle
		CHANCES.put(22086, new int[]
		{
			490000,
			50000
		}); // Lost Gargoyle Youngling
		CHANCES.put(22087, new int[]
		{
			787000,
			81000
		}); // Pronghorn Spirit
		CHANCES.put(22088, new int[]
		{
			480000,
			49000
		}); // Pronghorn
		CHANCES.put(22089, new int[]
		{
			550000,
			56000
		}); // Ice Tarantula
		CHANCES.put(22090, new int[]
		{
			570000,
			58000
		}); // Frost Tarantula
		CHANCES.put(22092, new int[]
		{
			623000,
			0
		}); // Frost Iron Golem
		CHANCES.put(22093, new int[]
		{
			910000,
			93000
		}); // Lost Buffalo
		CHANCES.put(22094, new int[]
		{
			553000,
			57000
		}); // Frost Buffalo
		CHANCES.put(22096, new int[]
		{
			593000,
			61000
		}); // Ursus
		CHANCES.put(22097, new int[]
		{
			693000,
			71000
		}); // Lost Yeti
		CHANCES.put(22098, new int[]
		{
			717000,
			74000
		}); // Frost Yeti
	}
	
	public Q648_AnIceMerchantsDream()
	{
		super(648, "An Ice Merchant's Dream");
		
		setItemsIds(SILVER_HEMOCYTE, SILVER_ICE_CRYSTAL, BLACK_ICE_CRYSTAL);
		
		addQuestStart(RAFFORTY, ICE_SHELF);
		addTalkId(RAFFORTY, ICE_SHELF);
		
		for (int npcId : CHANCES.keySet())
			addMyDying(npcId);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// RAFFORTY
		if (event.equalsIgnoreCase("32020-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32020-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(2);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32020-14.htm") || event.equalsIgnoreCase("32020-15.htm"))
		{
			final int black = player.getInventory().getItemCount(BLACK_ICE_CRYSTAL);
			final int silver = player.getInventory().getItemCount(SILVER_ICE_CRYSTAL);
			if (silver + black > 0)
			{
				takeItems(player, BLACK_ICE_CRYSTAL, -1);
				takeItems(player, SILVER_ICE_CRYSTAL, -1);
				rewardItems(player, 57, (silver * 300) + (black * 1200));
			}
			else
				htmltext = "32020-16a.htm";
		}
		else if (event.startsWith("32020-17"))
		{
			final int[] reward = REWARDS.get(event.substring(8, 9));
			if (player.getInventory().getItemCount(reward[0]) >= reward[1])
			{
				takeItems(player, reward[0], reward[1]);
				rewardItems(player, reward[2], 1);
			}
			else
				htmltext = "32020-15a.htm";
		}
		else if (event.equalsIgnoreCase("32020-20.htm") || event.equalsIgnoreCase("32020-22.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		// ICE SHELF
		else if (event.equalsIgnoreCase("32023-05.htm"))
		{
			if (st.getInteger("exCond") == 0)
				st.set("exCond", (Rnd.get(4) + 1) * 10);
		}
		else if (event.startsWith("32023-06-"))
		{
			final int exCond = st.getInteger("exCond");
			if (exCond > 0)
			{
				htmltext = "32023-06.htm";
				st.set("exCond", exCond + (event.endsWith("chisel") ? 1 : 2));
				playSound(player, "ItemSound2.broken_key");
				takeItems(player, SILVER_ICE_CRYSTAL, 1);
			}
		}
		else if (event.startsWith("32023-07-"))
		{
			final int exCond = st.getInteger("exCond");
			if (exCond > 0)
			{
				final int val = exCond / 10;
				if (val == (exCond - (val * 10) + (event.endsWith("knife") ? 0 : 2)))
				{
					htmltext = "32023-07.htm";
					playSound(player, "ItemSound3.sys_enchant_success");
					rewardItems(player, BLACK_ICE_CRYSTAL, 1);
				}
				else
				{
					htmltext = "32023-08.htm";
					playSound(player, "ItemSound3.sys_enchant_failed");
				}
				st.set("exCond", 0);
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
				if (npc.getNpcId() == RAFFORTY)
				{
					if (player.getStatus().getLevel() < 53)
						htmltext = "32020-01.htm";
					else
					{
						QuestState st2 = player.getQuestList().getQuestState(qn2);
						htmltext = (st2 != null && st2.isCompleted()) ? "32020-02.htm" : "32020-03.htm";
					}
				}
				else
					htmltext = "32023-01.htm";
				break;
			
			case STARTED:
				if (npc.getNpcId() == RAFFORTY)
				{
					final boolean hasItem = (player.getInventory().hasAtLeastOneItem(SILVER_ICE_CRYSTAL, BLACK_ICE_CRYSTAL));
					QuestState st2 = player.getQuestList().getQuestState(qn2);
					if (st2 != null && st2.isCompleted())
					{
						htmltext = (hasItem) ? "32020-11.htm" : "32020-09.htm";
						if (st.getCond() == 1)
						{
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
						}
					}
					else
						htmltext = (hasItem) ? "32020-10.htm" : "32020-08.htm";
				}
				else
				{
					if (!player.getInventory().hasItem(SILVER_ICE_CRYSTAL))
						htmltext = "32023-02.htm";
					else
					{
						if ((st.getInteger("exCond") % 10) == 0)
						{
							htmltext = "32023-03.htm";
							st.set("exCond", 0);
						}
						else
							htmltext = "32023-04.htm";
					}
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int[] chance = CHANCES.get(npc.getNpcId());
		
		dropItems(st.getPlayer(), SILVER_ICE_CRYSTAL, 1, 0, chance[0]);
		
		if (st.getCond() == 2 && chance[1] > 0)
			dropItems(st.getPlayer(), SILVER_HEMOCYTE, 1, 0, chance[1]);
	}
}