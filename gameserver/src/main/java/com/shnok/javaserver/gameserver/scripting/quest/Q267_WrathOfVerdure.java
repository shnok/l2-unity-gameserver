package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q267_WrathOfVerdure extends Quest
{
	private static final String QUEST_NAME = "Q267_WrathOfVerdure";
	
	// Items
	private static final int GOBLIN_CLUB = 1335;
	
	// Reward
	private static final int SILVERY_LEAF = 1340;
	
	public Q267_WrathOfVerdure()
	{
		super(267, "Wrath of Verdure");
		
		setItemsIds(GOBLIN_CLUB);
		
		addQuestStart(31853); // Bremec
		addTalkId(31853);
		
		addMyDying(20325); // Goblin
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31853-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31853-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				if (player.getRace() != ClassRace.ELF)
					htmltext = "31853-00.htm";
				else if (player.getStatus().getLevel() < 4)
					htmltext = "31853-01.htm";
				else
					htmltext = "31853-02.htm";
				break;
			
			case STARTED:
				final int count = player.getInventory().getItemCount(GOBLIN_CLUB);
				if (count > 0)
				{
					htmltext = "31853-05.htm";
					takeItems(player, GOBLIN_CLUB, -1);
					rewardItems(player, SILVERY_LEAF, count);
				}
				else
					htmltext = "31853-04.htm";
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
		
		dropItems(player, GOBLIN_CLUB, 1, 0, 500000);
	}
}