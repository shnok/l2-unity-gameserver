package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q306_CrystalsOfFireAndIce extends Quest
{
	private static final String QUEST_NAME = "Q306_CrystalsOfFireAndIce";
	
	// Items
	private static final int FLAME_SHARD = 1020;
	private static final int ICE_SHARD = 1021;
	
	// Droplist (npcId, itemId, chance)
	private static final int[][] DROPLIST =
	{
		{
			20109,
			FLAME_SHARD,
			300000
		},
		{
			20110,
			ICE_SHARD,
			300000
		},
		{
			20112,
			FLAME_SHARD,
			400000
		},
		{
			20113,
			ICE_SHARD,
			400000
		},
		{
			20114,
			FLAME_SHARD,
			500000
		},
		{
			20115,
			ICE_SHARD,
			500000
		}
	};
	
	public Q306_CrystalsOfFireAndIce()
	{
		super(306, "Crystals of Fire and Ice");
		
		setItemsIds(FLAME_SHARD, ICE_SHARD);
		
		addQuestStart(30004); // Katerina
		addTalkId(30004);
		
		addMyDying(20109, 20110, 20112, 20113, 20114, 20115);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30004-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30004-06.htm"))
		{
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
				htmltext = (player.getStatus().getLevel() < 17) ? "30004-01.htm" : "30004-02.htm";
				break;
			
			case STARTED:
				final int totalItems = player.getInventory().getItemCount(FLAME_SHARD) + player.getInventory().getItemCount(ICE_SHARD);
				if (totalItems == 0)
					htmltext = "30004-04.htm";
				else
				{
					htmltext = "30004-05.htm";
					takeItems(player, FLAME_SHARD, -1);
					takeItems(player, ICE_SHARD, -1);
					rewardItems(player, 57, 30 * totalItems + ((totalItems > 10) ? 5000 : 0));
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
		
		for (int[] drop : DROPLIST)
		{
			if (npc.getNpcId() == drop[0])
			{
				dropItems(player, drop[1], 1, 0, drop[2]);
				break;
			}
		}
	}
}