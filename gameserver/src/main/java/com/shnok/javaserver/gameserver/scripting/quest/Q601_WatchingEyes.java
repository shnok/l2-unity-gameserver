package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q601_WatchingEyes extends Quest
{
	private static final String QUEST_NAME = "Q601_WatchingEyes";
	
	// Items
	private static final int PROOF_OF_AVENGER = 7188;
	
	// Rewards
	private static final int[][] REWARDS =
	{
		{
			6699,
			90000,
			20
		},
		{
			6698,
			80000,
			40
		},
		{
			6700,
			40000,
			50
		},
		{
			0,
			230000,
			100
		}
	};
	
	public Q601_WatchingEyes()
	{
		super(601, "Watching Eyes");
		
		setItemsIds(PROOF_OF_AVENGER);
		
		addQuestStart(31683); // Eye of Argos
		addTalkId(31683);
		
		addMyDying(21306, 21308, 21309, 21310, 21311);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31683-03.htm"))
		{
			if (player.getStatus().getLevel() < 71)
				htmltext = "31683-02.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
		}
		else if (event.equalsIgnoreCase("31683-07.htm"))
		{
			takeItems(player, PROOF_OF_AVENGER, -1);
			
			final int random = Rnd.get(100);
			for (int[] element : REWARDS)
			{
				if (random < element[2])
				{
					rewardItems(player, 57, element[1]);
					
					if (element[0] != 0)
					{
						giveItems(player, element[0], 5);
						rewardExpAndSp(player, 120000, 10000);
					}
					break;
				}
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
				htmltext = "31683-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = (player.getInventory().hasItem(PROOF_OF_AVENGER)) ? "31683-05.htm" : "31683-04.htm";
				else if (cond == 2)
					htmltext = "31683-06.htm";
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
		
		if (dropItems(st.getPlayer(), PROOF_OF_AVENGER, 1, 100, 500000))
			st.setCond(2);
	}
}