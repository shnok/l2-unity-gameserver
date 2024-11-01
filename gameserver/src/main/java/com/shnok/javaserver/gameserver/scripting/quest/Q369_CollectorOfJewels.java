package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q369_CollectorOfJewels extends Quest
{
	private static final String QUEST_NAME = "Q369_CollectorOfJewels";
	
	// NPC
	private static final int NELL = 30376;
	
	// Items
	private static final int FLARE_SHARD = 5882;
	private static final int FREEZING_SHARD = 5883;
	
	// Reward
	private static final int ADENA = 57;
	
	// Droplist
	private static final Map<Integer, int[]> DROPLIST = new HashMap<>();
	{
		DROPLIST.put(20609, new int[]
		{
			FLARE_SHARD,
			630000
		});
		DROPLIST.put(20612, new int[]
		{
			FLARE_SHARD,
			770000
		});
		DROPLIST.put(20749, new int[]
		{
			FLARE_SHARD,
			850000
		});
		DROPLIST.put(20616, new int[]
		{
			FREEZING_SHARD,
			600000
		});
		DROPLIST.put(20619, new int[]
		{
			FREEZING_SHARD,
			730000
		});
		DROPLIST.put(20747, new int[]
		{
			FREEZING_SHARD,
			850000
		});
	}
	
	public Q369_CollectorOfJewels()
	{
		super(369, "Collector of Jewels");
		
		setItemsIds(FLARE_SHARD, FREEZING_SHARD);
		
		addQuestStart(NELL);
		addTalkId(NELL);
		
		for (int mob : DROPLIST.keySet())
			addMyDying(mob);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30376-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30376-07.htm"))
			playSound(player, SOUND_ITEMGET);
		else if (event.equalsIgnoreCase("30376-08.htm"))
		{
			st.exitQuest(true);
			playSound(player, SOUND_FINISH);
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
				htmltext = (player.getStatus().getLevel() < 25) ? "30376-01.htm" : "30376-02.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				final int flare = player.getInventory().getItemCount(FLARE_SHARD);
				final int freezing = player.getInventory().getItemCount(FREEZING_SHARD);
				
				if (cond == 1)
					htmltext = "30376-04.htm";
				else if (cond == 2 && flare >= 50 && freezing >= 50)
				{
					htmltext = "30376-05.htm";
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, FLARE_SHARD, -1);
					takeItems(player, FREEZING_SHARD, -1);
					rewardItems(player, ADENA, 12500);
				}
				else if (cond == 3)
					htmltext = "30376-09.htm";
				else if (cond == 4 && flare >= 200 && freezing >= 200)
				{
					htmltext = "30376-10.htm";
					takeItems(player, FLARE_SHARD, -1);
					takeItems(player, FREEZING_SHARD, -1);
					rewardItems(player, ADENA, 63500);
					playSound(player, SOUND_FINISH);
					st.exitQuest(true);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		player = st.getPlayer();
		final int cond = st.getCond();
		final int[] drop = DROPLIST.get(npc.getNpcId());
		
		if (cond == 1)
		{
			if (dropItems(player, drop[0], 1, 50, drop[1]) && player.getInventory().getItemCount((drop[0] == FLARE_SHARD) ? FREEZING_SHARD : FLARE_SHARD) >= 50)
				st.setCond(2);
		}
		else if (cond == 3 && dropItems(player, drop[0], 1, 200, drop[1]) && player.getInventory().getItemCount((drop[0] == FLARE_SHARD) ? FREEZING_SHARD : FLARE_SHARD) >= 200)
			st.setCond(4);
	}
}