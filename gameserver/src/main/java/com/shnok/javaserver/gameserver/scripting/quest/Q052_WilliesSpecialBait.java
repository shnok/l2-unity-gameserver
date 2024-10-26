package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q052_WilliesSpecialBait extends Quest
{
	private static final String QUEST_NAME = "Q052_WilliesSpecialBait";
	
	// Item
	private static final int TARLK_EYE = 7623;
	
	// Reward
	private static final int EARTH_FISHING_LURE = 7612;
	
	public Q052_WilliesSpecialBait()
	{
		super(52, "Willie's Special Bait");
		
		setItemsIds(TARLK_EYE);
		
		addQuestStart(31574); // Willie
		addTalkId(31574);
		
		addMyDying(20573); // Tarlk Basilik
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31574-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31574-07.htm"))
		{
			htmltext = "31574-06.htm";
			takeItems(player, TARLK_EYE, -1);
			rewardItems(player, EARTH_FISHING_LURE, 4);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
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
				htmltext = (player.getStatus().getLevel() < 48) ? "31574-02.htm" : "31574-01.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().getItemCount(TARLK_EYE) == 100) ? "31574-04.htm" : "31574-05.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(player, TARLK_EYE, 1, 100, 500000))
			st.setCond(2);
	}
}