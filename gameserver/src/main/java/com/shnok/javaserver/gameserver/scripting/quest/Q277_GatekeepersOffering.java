package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q277_GatekeepersOffering extends Quest
{
	private static final String QUEST_NAME = "Q277_GatekeepersOffering";
	
	// Item
	private static final int STARSTONE = 1572;
	
	// Reward
	private static final int GATEKEEPER_CHARM = 1658;
	
	public Q277_GatekeepersOffering()
	{
		super(277, "Gatekeeper's Offering");
		
		addQuestStart(30576); // Tamil
		addTalkId(30576);
		
		addMyDying(20333); // Graystone Golem
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30576-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30576-01.htm" : "30576-02.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 1)
					htmltext = "30576-04.htm";
				else
				{
					htmltext = "30576-05.htm";
					takeItems(player, STARSTONE, -1);
					rewardItems(player, GATEKEEPER_CHARM, 2);
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
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(player, STARSTONE, 1, 20, 500000))
			st.setCond(2);
	}
}