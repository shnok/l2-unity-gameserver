package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q051_OFullesSpecialBait extends Quest
{
	private static final String QUEST_NAME = "Q051_OFullesSpecialBait";
	
	// Item
	private static final int LOST_BAIT = 7622;
	
	// Reward
	private static final int ICY_AIR_LURE = 7611;
	
	public Q051_OFullesSpecialBait()
	{
		super(51, "O'Fulle's Special Bait");
		
		setItemsIds(LOST_BAIT);
		
		addQuestStart(31572); // O'Fulle
		addTalkId(31572);
		
		addMyDying(20552); // Fettered Soul
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31572-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31572-07.htm"))
		{
			htmltext = "31572-06.htm";
			takeItems(player, LOST_BAIT, -1);
			rewardItems(player, ICY_AIR_LURE, 4);
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
				htmltext = (player.getStatus().getLevel() < 36) ? "31572-02.htm" : "31572-01.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().getItemCount(LOST_BAIT) == 100) ? "31572-04.htm" : "31572-05.htm";
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
		
		if (dropItemsAlways(player, LOST_BAIT, 1, 100))
			st.setCond(2);
	}
}