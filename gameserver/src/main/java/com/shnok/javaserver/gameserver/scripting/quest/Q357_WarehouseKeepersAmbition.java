package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q357_WarehouseKeepersAmbition extends Quest
{
	private static final String QUEST_NAME = "Q357_WarehouseKeepersAmbition";
	
	// Item
	private static final int JADE_CRYSTAL = 5867;
	
	// Monsters
	private static final int FOREST_RUNNER = 20594;
	private static final int FLINE_ELDER = 20595;
	private static final int LIELE_ELDER = 20596;
	private static final int VALLEY_TREANT_ELDER = 20597;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(FOREST_RUNNER, 400000);
		CHANCES.put(FLINE_ELDER, 410000);
		CHANCES.put(LIELE_ELDER, 440000);
		CHANCES.put(VALLEY_TREANT_ELDER, 650000);
	}
	
	public Q357_WarehouseKeepersAmbition()
	{
		super(357, "Warehouse Keeper's Ambition");
		
		setItemsIds(JADE_CRYSTAL);
		
		addQuestStart(30686); // Silva
		addTalkId(30686);
		
		addMyDying(FOREST_RUNNER, FLINE_ELDER, LIELE_ELDER, VALLEY_TREANT_ELDER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30686-2.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30686-7.htm"))
		{
			final int count = player.getInventory().getItemCount(JADE_CRYSTAL);
			if (count == 0)
				htmltext = "30686-4.htm";
			else
			{
				int reward = (count * 425) + 3500;
				if (count >= 100)
					reward += 7400;
				
				takeItems(player, JADE_CRYSTAL, -1);
				rewardItems(player, 57, reward);
			}
		}
		else if (event.equalsIgnoreCase("30686-8.htm"))
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
				htmltext = (player.getStatus().getLevel() < 47) ? "30686-0a.htm" : "30686-0.htm";
				break;
			
			case STARTED:
				htmltext = (!player.getInventory().hasItem(JADE_CRYSTAL)) ? "30686-4.htm" : "30686-6.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		dropItems(st.getPlayer(), JADE_CRYSTAL, 1, 0, CHANCES.get(npc.getNpcId()));
	}
}