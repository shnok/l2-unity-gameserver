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

public class Q359_ForSleeplessDeadmen extends Quest
{
	private static final String QUEST_NAME = "Q359_ForSleeplessDeadmen";
	
	// Item
	private static final int REMAINS = 5869;
	
	// Monsters
	private static final int DOOM_SERVANT = 21006;
	private static final int DOOM_GUARD = 21007;
	private static final int DOOM_ARCHER = 21008;
	
	// Reward
	private static final int[] REWARDS =
	{
		6341,
		6342,
		6343,
		6344,
		6345,
		6346,
		5494,
		5495
	};
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	static
	{
		CHANCES.put(DOOM_SERVANT, 320000);
		CHANCES.put(DOOM_GUARD, 340000);
		CHANCES.put(DOOM_ARCHER, 420000);
	}
	
	public Q359_ForSleeplessDeadmen()
	{
		super(359, "For Sleepless Deadmen");
		
		setItemsIds(REMAINS);
		
		addQuestStart(30857); // Orven
		addTalkId(30857);
		
		addMyDying(DOOM_SERVANT, DOOM_GUARD, DOOM_ARCHER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30857-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30857-10.htm"))
		{
			giveItems(player, Rnd.get(REWARDS), 4);
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
				htmltext = (player.getStatus().getLevel() < 60) ? "30857-01.htm" : "30857-02.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "30857-07.htm";
				else if (cond == 2)
				{
					htmltext = "30857-08.htm";
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, REMAINS, -1);
				}
				else if (cond == 3)
					htmltext = "30857-09.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(player, REMAINS, 1, 60, CHANCES.get(npc.getNpcId())))
			st.setCond(2);
	}
}