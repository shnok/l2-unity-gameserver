package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q631_DeliciousTopChoiceMeat extends Quest
{
	private static final String QUEST_NAME = "Q631_DeliciousTopChoiceMeat";
	
	// NPC
	private static final int TUNATUN = 31537;
	
	// Item
	private static final int TOP_QUALITY_MEAT = 7546;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(24);
	
	// Rewards
	private static final int[][] REWARDS =
	{
		{
			4039,
			15
		},
		{
			4043,
			15
		},
		{
			4044,
			15
		},
		{
			4040,
			10
		},
		{
			4042,
			10
		},
		{
			4041,
			5
		}
	};
	
	public Q631_DeliciousTopChoiceMeat()
	{
		super(631, "Delicious Top Choice Meat");
		
		CHANCES.put(21460, 601000);
		CHANCES.put(21461, 480000);
		CHANCES.put(21462, 447000);
		CHANCES.put(21463, 808000);
		CHANCES.put(21464, 447000);
		CHANCES.put(21465, 808000);
		CHANCES.put(21466, 447000);
		CHANCES.put(21467, 808000);
		CHANCES.put(21479, 477000);
		CHANCES.put(21480, 863000);
		CHANCES.put(21481, 477000);
		CHANCES.put(21482, 863000);
		CHANCES.put(21483, 477000);
		CHANCES.put(21484, 863000);
		CHANCES.put(21485, 477000);
		CHANCES.put(21486, 863000);
		CHANCES.put(21498, 509000);
		CHANCES.put(21499, 920000);
		CHANCES.put(21500, 509000);
		CHANCES.put(21501, 920000);
		CHANCES.put(21502, 509000);
		CHANCES.put(21503, 920000);
		CHANCES.put(21504, 509000);
		CHANCES.put(21505, 920000);
		
		setItemsIds(TOP_QUALITY_MEAT);
		
		addQuestStart(TUNATUN);
		addTalkId(TUNATUN);
		
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
		
		if (event.equalsIgnoreCase("31537-03.htm"))
		{
			if (player.getStatus().getLevel() >= 65)
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
			{
				htmltext = "31537-02.htm";
				st.exitQuest(true);
			}
		}
		else if (StringUtil.isDigit(event))
		{
			if (player.getInventory().getItemCount(TOP_QUALITY_MEAT) >= 120)
			{
				htmltext = "31537-06.htm";
				takeItems(player, TOP_QUALITY_MEAT, -1);
				
				int[] reward = REWARDS[Integer.parseInt(event)];
				rewardItems(player, reward[0], reward[1]);
				
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
			{
				st.setCond(1);
				htmltext = "31537-07.htm";
			}
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
				htmltext = "31537-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "31537-03a.htm";
				else if (cond == 2)
				{
					if (player.getInventory().getItemCount(TOP_QUALITY_MEAT) >= 120)
						htmltext = "31537-04.htm";
					else
					{
						st.setCond(1);
						htmltext = "31537-03a.htm";
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
		
		final QuestState st = getRandomPartyMember(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(st.getPlayer(), TOP_QUALITY_MEAT, 1, 120, CHANCES.get(npc.getNpcId())))
			st.setCond(2);
	}
}