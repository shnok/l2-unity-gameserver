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

public class Q344_1000YearsTheEndOfLamentation extends Quest
{
	private static final String QUEST_NAME = "Q344_1000YearsTheEndOfLamentation";
	
	// NPCs
	private static final int GILMORE = 30754;
	private static final int RODEMAI = 30756;
	private static final int ORVEN = 30857;
	private static final int KAIEN = 30623;
	private static final int GARVARENTZ = 30704;
	
	// Items
	private static final int ARTICLE_DEAD_HERO = 4269;
	private static final int OLD_KEY = 4270;
	private static final int OLD_HILT = 4271;
	private static final int OLD_TOTEM = 4272;
	private static final int CRUCIFIX = 4273;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(20236, 380000);
		CHANCES.put(20237, 490000);
		CHANCES.put(20238, 460000);
		CHANCES.put(20239, 490000);
		CHANCES.put(20240, 530000);
		CHANCES.put(20272, 380000);
		CHANCES.put(20273, 490000);
		CHANCES.put(20274, 460000);
		CHANCES.put(20275, 490000);
		CHANCES.put(20276, 530000);
	}
	
	public Q344_1000YearsTheEndOfLamentation()
	{
		super(344, "1000 Years, the End of Lamentation");
		
		setItemsIds(ARTICLE_DEAD_HERO, OLD_KEY, OLD_HILT, OLD_TOTEM, CRUCIFIX);
		
		addQuestStart(GILMORE);
		addTalkId(GILMORE, RODEMAI, ORVEN, GARVARENTZ, KAIEN);
		
		addMyDying(20236, 20237, 20238, 20239, 20240, 20272, 20273, 20274, 20275, 20276);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30754-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30754-07.htm"))
		{
			if (st.getInteger("success") > 0)
			{
				st.setCond(1);
				st.unset("success");
				playSound(player, SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("30754-08.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30754-06.htm"))
		{
			if (!player.getInventory().hasItem(ARTICLE_DEAD_HERO))
				htmltext = "30754-06a.htm";
			else
			{
				final int amount = player.getInventory().getItemCount(ARTICLE_DEAD_HERO);
				
				takeItems(player, ARTICLE_DEAD_HERO, -1);
				giveItems(player, 57, amount * 60);
				
				// Special item, % based on actual number of qItems.
				if (Rnd.get(1000) < Math.min(10, Math.max(1, amount / 10)))
					htmltext = "30754-10.htm";
			}
		}
		else if (event.equalsIgnoreCase("30754-11.htm"))
		{
			final int random = Rnd.get(4);
			if (random < 1)
			{
				htmltext = "30754-12.htm";
				giveItems(player, OLD_KEY, 1);
			}
			else if (random < 2)
			{
				htmltext = "30754-13.htm";
				giveItems(player, OLD_HILT, 1);
			}
			else if (random < 3)
			{
				htmltext = "30754-14.htm";
				giveItems(player, OLD_TOTEM, 1);
			}
			else
				giveItems(player, CRUCIFIX, 1);
			
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
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
				htmltext = (player.getStatus().getLevel() < 48) ? "30754-01.htm" : "30754-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case GILMORE:
						if (cond == 1)
							htmltext = (player.getInventory().hasItem(ARTICLE_DEAD_HERO)) ? "30754-05.htm" : "30754-09.htm";
						else if (cond == 2)
							htmltext = (st.getInteger("success") > 0) ? "30754-16.htm" : "30754-15.htm";
						break;
					
					default:
						if (cond == 2)
						{
							if (st.getInteger("success") > 0)
								htmltext = npc.getNpcId() + "-02.htm";
							else
							{
								rewards(player, st, npc.getNpcId());
								htmltext = npc.getNpcId() + "-01.htm";
							}
						}
						break;
				}
				break;
		}
		return htmltext;
	}
	
	private static void rewards(Player player, QuestState st, int npcId)
	{
		switch (npcId)
		{
			case ORVEN:
				if (player.getInventory().hasItem(CRUCIFIX))
				{
					st.set("success", 1);
					takeItems(player, CRUCIFIX, -1);
					
					final int chance = Rnd.get(100);
					if (chance < 80)
						giveItems(player, 1875, 19);
					else if (chance < 95)
						giveItems(player, 952, 5);
					else
						giveItems(player, 2437, 1);
				}
				break;
			
			case GARVARENTZ:
				if (player.getInventory().hasItem(OLD_TOTEM))
				{
					st.set("success", 1);
					takeItems(player, OLD_TOTEM, -1);
					
					final int chance = Rnd.get(100);
					if (chance < 55)
						giveItems(player, 1882, 70);
					else if (chance < 99)
						giveItems(player, 1881, 50);
					else
						giveItems(player, 191, 1);
				}
				break;
			
			case KAIEN:
				if (player.getInventory().hasItem(OLD_HILT))
				{
					st.set("success", 1);
					takeItems(player, OLD_HILT, -1);
					
					final int chance = Rnd.get(100);
					if (chance < 60)
						giveItems(player, 1874, 25);
					else if (chance < 85)
						giveItems(player, 1887, 10);
					else if (chance < 99)
						giveItems(player, 951, 1);
					else
						giveItems(player, 133, 1);
				}
				break;
			
			case RODEMAI:
				if (player.getInventory().hasItem(OLD_KEY))
				{
					st.set("success", 1);
					takeItems(player, OLD_KEY, -1);
					
					final int chance = Rnd.get(100);
					if (chance < 80)
						giveItems(player, 1879, 55);
					else if (chance < 95)
						giveItems(player, 951, 1);
					else
						giveItems(player, 885, 1);
				}
				break;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		dropItems(player, ARTICLE_DEAD_HERO, 1, 0, CHANCES.get(npc.getNpcId()));
	}
}