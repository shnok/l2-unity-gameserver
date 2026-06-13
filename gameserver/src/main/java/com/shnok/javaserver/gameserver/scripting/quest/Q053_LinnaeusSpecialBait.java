package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q053_LinnaeusSpecialBait extends Quest
{
	private static final String QUEST_NAME = "Q053_LinnaeusSpecialBait";
	
	// Item
	private static final int CRIMSON_DRAKE_HEART = 7624;
	
	// Reward
	private static final int FLAMING_FISHING_LURE = 7613;
	
	public Q053_LinnaeusSpecialBait()
	{
		super(53, "Linnaues' Special Bait");
		
		setItemsIds(CRIMSON_DRAKE_HEART);
		
		addQuestStart(31577); // Linnaeus
		addTalkId(31577);
		
		addMyDying(20670); // Crimson Drake
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31577-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31577-07.htm"))
		{
			htmltext = "31577-06.htm";
			takeItems(player, CRIMSON_DRAKE_HEART, -1);
			rewardItems(player, FLAMING_FISHING_LURE, 4);
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
				htmltext = (player.getStatus().getLevel() < 60) ? "31577-02.htm" : "31577-01.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().getItemCount(CRIMSON_DRAKE_HEART) == 100) ? "31577-04.htm" : "31577-05.htm";
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
		
		if (dropItems(player, CRIMSON_DRAKE_HEART, 1, 100, 500000))
			st.setCond(2);
	}
}