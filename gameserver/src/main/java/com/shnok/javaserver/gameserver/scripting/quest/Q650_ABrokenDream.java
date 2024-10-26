package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q650_ABrokenDream extends Quest
{
	private static final String QUEST_NAME = "Q650_ABrokenDream";
	
	// NPC
	private static final int GHOST = 32054;
	
	// Item
	private static final int DREAM_FRAGMENT = 8514;
	
	// Monsters
	private static final int CREWMAN = 22027;
	private static final int VAGABOND = 22028;
	
	public Q650_ABrokenDream()
	{
		super(650, "A Broken Dream");
		
		setItemsIds(DREAM_FRAGMENT);
		
		addQuestStart(GHOST);
		addTalkId(GHOST);
		addMyDying(CREWMAN, VAGABOND);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32054-01a.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32054-03.htm"))
		{
			if (!player.getInventory().hasItem(DREAM_FRAGMENT))
				htmltext = "32054-04.htm";
		}
		else if (event.equalsIgnoreCase("32054-05.htm"))
		{
			playSound(player, SOUND_GIVEUP);
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
				QuestState st2 = player.getQuestList().getQuestState("Q117_TheOceanOfDistantStars");
				if (st2 != null && st2.isCompleted() && player.getStatus().getLevel() >= 39)
					htmltext = "32054-01.htm";
				else
				{
					htmltext = "32054-00.htm";
					st.exitQuest(true);
				}
				break;
			
			case STARTED:
				htmltext = "32054-02.htm";
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
		
		dropItems(player, DREAM_FRAGMENT, 1, 0, 250000);
	}
}