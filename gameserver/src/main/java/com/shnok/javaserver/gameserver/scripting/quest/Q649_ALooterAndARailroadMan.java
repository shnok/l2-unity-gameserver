package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q649_ALooterAndARailroadMan extends Quest
{
	private static final String QUEST_NAME = "Q649_ALooterAndARailroadMan";
	
	// Item
	private static final int THIEF_GUILD_MARK = 8099;
	
	// NPC
	private static final int OBI = 32052;
	
	public Q649_ALooterAndARailroadMan()
	{
		super(649, "A Looter and a Railroad Man");
		
		setItemsIds(THIEF_GUILD_MARK);
		
		addQuestStart(OBI);
		addTalkId(OBI);
		
		addMyDying(22017, 22018, 22019, 22021, 22022, 22023, 22024, 22026);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32052-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32052-3.htm"))
		{
			if (player.getInventory().getItemCount(THIEF_GUILD_MARK) < 200)
				htmltext = "32052-3a.htm";
			else
			{
				takeItems(player, THIEF_GUILD_MARK, -1);
				rewardItems(player, 57, 21698);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
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
				htmltext = (player.getStatus().getLevel() < 30) ? "32052-0a.htm" : "32052-0.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "32052-2a.htm";
				else if (cond == 2)
					htmltext = "32052-2.htm";
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
		
		if (dropItems(player, THIEF_GUILD_MARK, 1, 200, 800000))
			st.setCond(2);
	}
}