package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q646_SignsOfRevolt extends Quest
{
	private static final String QUEST_NAME = "Q646_SignsOfRevolt";
	
	// NPC
	private static final int TORRANT = 32016;
	
	// Item
	private static final int CURSED_DOLL = 8087;
	
	// Rewards
	private static final int[][] REWARDS =
	{
		{
			1880,
			9
		},
		{
			1881,
			12
		},
		{
			1882,
			20
		},
		{
			57,
			21600
		}
	};
	
	public Q646_SignsOfRevolt()
	{
		super(646, "Signs Of Revolt");
		
		setItemsIds(CURSED_DOLL);
		
		addQuestStart(TORRANT);
		addTalkId(TORRANT);
		
		addMyDying(22029, 22030, 22031, 22032, 22033, 22034, 22035, 22036, 22037, 22038, 22039, 22040, 22041, 22042, 22043, 22044, 22045, 22047, 22049);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32016-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (StringUtil.isDigit(event))
		{
			htmltext = "32016-07.htm";
			takeItems(player, CURSED_DOLL, -1);
			
			final int[] reward = REWARDS[Integer.parseInt(event)];
			giveItems(player, reward[0], reward[1]);
			
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
				htmltext = (player.getStatus().getLevel() < 40) ? "32016-02.htm" : "32016-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "32016-04.htm";
				else if (cond == 2)
					htmltext = "32016-05.htm";
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
		
		if (dropItems(st.getPlayer(), CURSED_DOLL, 1, 180, 750000))
			st.setCond(2);
	}
}