package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q661_MakingTheHarvestGroundsSafe extends Quest
{
	private static final String QUEST_NAME = "Q661_MakingTheHarvestGroundsSafe";
	
	// NPC
	private static final int NORMAN = 30210;
	
	// Items
	private static final int STING_OF_GIANT_POISON_BEE = 8283;
	private static final int CLOUDY_GEM = 8284;
	private static final int TALON_OF_YOUNG_ARANEID = 8285;
	
	// Reward
	private static final int ADENA = 57;
	
	// Monsters
	private static final int GIANT_POISON_BEE = 21095;
	private static final int CLOUDY_BEAST = 21096;
	private static final int YOUNG_ARANEID = 21097;
	
	public Q661_MakingTheHarvestGroundsSafe()
	{
		super(661, "Making the Harvest Grounds Safe");
		
		setItemsIds(STING_OF_GIANT_POISON_BEE, CLOUDY_GEM, TALON_OF_YOUNG_ARANEID);
		
		addQuestStart(NORMAN);
		addTalkId(NORMAN);
		
		addMyDying(GIANT_POISON_BEE, CLOUDY_BEAST, YOUNG_ARANEID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30210-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30210-04.htm"))
		{
			int item1 = player.getInventory().getItemCount(STING_OF_GIANT_POISON_BEE);
			int item2 = player.getInventory().getItemCount(CLOUDY_GEM);
			int item3 = player.getInventory().getItemCount(TALON_OF_YOUNG_ARANEID);
			int sum = 0;
			
			sum = (item1 * 57) + (item2 * 56) + (item3 * 60);
			
			if (item1 + item2 + item3 >= 10)
				sum += 2871;
			
			takeItems(player, STING_OF_GIANT_POISON_BEE, item1);
			takeItems(player, CLOUDY_GEM, item2);
			takeItems(player, TALON_OF_YOUNG_ARANEID, item3);
			rewardItems(player, ADENA, sum);
		}
		else if (event.equalsIgnoreCase("30210-06.htm"))
			st.exitQuest(true);
		
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
				htmltext = (player.getStatus().getLevel() < 21) ? "30210-01a.htm" : "30210-01.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasAtLeastOneItem(STING_OF_GIANT_POISON_BEE, CLOUDY_GEM, TALON_OF_YOUNG_ARANEID)) ? "30210-03.htm" : "30210-05.htm";
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
		
		dropItems(player, npc.getNpcId() - 12812, 1, 0, 500000);
	}
}