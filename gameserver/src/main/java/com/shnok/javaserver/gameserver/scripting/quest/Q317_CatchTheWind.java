package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q317_CatchTheWind extends Quest
{
	private static final String QUEST_NAME = "Q317_CatchTheWind";
	
	// Item
	private static final int WIND_SHARD = 1078;
	
	public Q317_CatchTheWind()
	{
		super(317, "Catch the Wind");
		
		setItemsIds(WIND_SHARD);
		
		addQuestStart(30361); // Rizraell
		addTalkId(30361);
		
		addMyDying(20036, 20044);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30361-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30361-08.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				htmltext = (player.getStatus().getLevel() < 18) ? "30361-02.htm" : "30361-03.htm";
				break;
			
			case STARTED:
				final int shards = player.getInventory().getItemCount(WIND_SHARD);
				if (shards == 0)
					htmltext = "30361-05.htm";
				else
				{
					htmltext = "30361-07.htm";
					takeItems(player, WIND_SHARD, -1);
					rewardItems(player, 57, 40 * shards + (shards >= 10 ? 2988 : 0));
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		dropItems(player, WIND_SHARD, 1, 0, 500000);
	}
}