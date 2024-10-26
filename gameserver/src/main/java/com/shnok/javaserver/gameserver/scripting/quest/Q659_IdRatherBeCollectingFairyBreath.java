package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q659_IdRatherBeCollectingFairyBreath extends Quest
{
	private static final String QUEST_NAME = "Q659_IdRatherBeCollectingFairyBreath";
	
	// NPCs
	private static final int GALATEA = 30634;
	
	// Item
	private static final int FAIRY_BREATH = 8286;
	
	// Monsters
	private static final int SOBBING_WIND = 21023;
	private static final int BABBLING_WIND = 21024;
	private static final int GIGGLING_WIND = 21025;
	
	public Q659_IdRatherBeCollectingFairyBreath()
	{
		super(659, "I'd Rather Be Collecting Fairy Breath");
		
		setItemsIds(FAIRY_BREATH);
		
		addQuestStart(GALATEA);
		addTalkId(GALATEA);
		
		addMyDying(GIGGLING_WIND, BABBLING_WIND, SOBBING_WIND);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30634-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30634-06.htm"))
		{
			final int count = player.getInventory().getItemCount(FAIRY_BREATH);
			if (count > 0)
			{
				takeItems(player, FAIRY_BREATH, count);
				if (count < 10)
					rewardItems(player, 57, count * 50);
				else
					rewardItems(player, 57, count * 50 + 5365);
			}
		}
		else if (event.equalsIgnoreCase("30634-08.htm"))
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
				htmltext = (player.getStatus().getLevel() < 26) ? "30634-01.htm" : "30634-02.htm";
				break;
			
			case STARTED:
				htmltext = (!player.getInventory().hasItem(FAIRY_BREATH)) ? "30634-04.htm" : "30634-05.htm";
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
		
		dropItems(player, FAIRY_BREATH, 1, 0, 900000);
	}
}