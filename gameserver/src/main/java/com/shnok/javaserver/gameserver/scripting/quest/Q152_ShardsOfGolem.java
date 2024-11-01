package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q152_ShardsOfGolem extends Quest
{
	private static final String QUEST_NAME = "Q152_ShardsOfGolem";
	
	// Items
	private static final int HARRIS_RECEIPT_1 = 1008;
	private static final int HARRIS_RECEIPT_2 = 1009;
	private static final int GOLEM_SHARD = 1010;
	private static final int TOOL_BOX = 1011;
	
	// Reward
	private static final int WOODEN_BREASTPLATE = 23;
	
	// NPCs
	private static final int HARRIS = 30035;
	private static final int ALTRAN = 30283;
	
	// Mob
	private static final int STONE_GOLEM = 20016;
	
	public Q152_ShardsOfGolem()
	{
		super(152, "Shards of Golem");
		
		setItemsIds(HARRIS_RECEIPT_1, HARRIS_RECEIPT_2, GOLEM_SHARD, TOOL_BOX);
		
		addQuestStart(HARRIS);
		addTalkId(HARRIS, ALTRAN);
		
		addMyDying(STONE_GOLEM);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30035-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, HARRIS_RECEIPT_1, 1);
		}
		else if (event.equalsIgnoreCase("30283-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HARRIS_RECEIPT_1, 1);
			giveItems(player, HARRIS_RECEIPT_2, 1);
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
				htmltext = (player.getStatus().getLevel() < 10) ? "30035-01a.htm" : "30035-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case HARRIS:
						if (cond < 4)
							htmltext = "30035-03.htm";
						else if (cond == 4)
						{
							htmltext = "30035-04.htm";
							takeItems(player, HARRIS_RECEIPT_2, 1);
							takeItems(player, TOOL_BOX, 1);
							giveItems(player, WOODEN_BREASTPLATE, 1);
							rewardExpAndSp(player, 5000, 0);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ALTRAN:
						if (cond == 1)
							htmltext = "30283-01.htm";
						else if (cond == 2)
							htmltext = "30283-03.htm";
						else if (cond == 3)
						{
							htmltext = "30283-04.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GOLEM_SHARD, -1);
							giveItems(player, TOOL_BOX, 1);
						}
						else if (cond == 4)
							htmltext = "30283-05.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 2);
		if (st == null)
			return;
		
		if (dropItems(player, GOLEM_SHARD, 1, 5, 300000))
			st.setCond(3);
	}
}