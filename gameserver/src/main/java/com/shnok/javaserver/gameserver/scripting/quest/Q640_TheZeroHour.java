package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q640_TheZeroHour extends Quest
{
	private static final String QUEST_NAME = "Q640_TheZeroHour";
	
	// NPC
	private static final int KAHMAN = 31554;
	
	// Item
	private static final int FANG_OF_STAKATO = 8085;
	
	private static final int[][] REWARDS =
	{
		{
			12,
			4042,
			1
		},
		{
			6,
			4043,
			1
		},
		{
			6,
			4044,
			1
		},
		{
			81,
			1887,
			10
		},
		{
			33,
			1888,
			5
		},
		{
			30,
			1889,
			10
		},
		{
			150,
			5550,
			10
		},
		{
			131,
			1890,
			10
		},
		{
			123,
			1893,
			5
		}
	};
	
	public Q640_TheZeroHour()
	{
		super(640, "The Zero Hour");
		
		setItemsIds(FANG_OF_STAKATO);
		
		addQuestStart(KAHMAN);
		addTalkId(KAHMAN);
		
		// All "spiked" stakatos types, except babies and cannibalistic followers.
		addMyDying(22105, 22106, 22107, 22108, 22109, 22110, 22111, 22113, 22114, 22115, 22116, 22117, 22118, 22119, 22121);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31554-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31554-05.htm"))
		{
			if (!player.getInventory().hasItem(FANG_OF_STAKATO))
				htmltext = "31554-06.htm";
		}
		else if (event.equalsIgnoreCase("31554-08.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (StringUtil.isDigit(event))
		{
			final int[] reward = REWARDS[Integer.parseInt(event)];
			
			if (player.getInventory().getItemCount(FANG_OF_STAKATO) >= reward[0])
			{
				htmltext = "31554-09.htm";
				takeItems(player, FANG_OF_STAKATO, reward[0]);
				rewardItems(player, reward[1], reward[2]);
			}
			else
				htmltext = "31554-06.htm";
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
				if (player.getStatus().getLevel() < 66)
					htmltext = "31554-00.htm";
				else
				{
					QuestState st2 = player.getQuestList().getQuestState("Q109_InSearchOfTheNest");
					htmltext = (st2 != null && st2.isCompleted()) ? "31554-01.htm" : "31554-10.htm";
				}
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasItem(FANG_OF_STAKATO)) ? "31554-04.htm" : "31554-03.htm";
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
		
		dropItemsAlways(st.getPlayer(), FANG_OF_STAKATO, 1, 0);
	}
}