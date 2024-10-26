package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q319_ScentOfDeath extends Quest
{
	private static final String QUEST_NAME = "Q319_ScentOfDeath";
	
	// Item
	private static final int ZOMBIE_SKIN = 1045;
	
	public Q319_ScentOfDeath()
	{
		super(319, "Scent of Death");
		
		setItemsIds(ZOMBIE_SKIN);
		
		addQuestStart(30138); // Minaless
		addTalkId(30138);
		
		addMyDying(20015, 20020);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30138-04.htm"))
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
				htmltext = (player.getStatus().getLevel() < 11) ? "30138-02.htm" : "30138-03.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 1)
					htmltext = "30138-05.htm";
				else
				{
					htmltext = "30138-06.htm";
					takeItems(player, ZOMBIE_SKIN, -1);
					rewardItems(player, 57, 3350);
					rewardItems(player, 1060, 1);
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
		
		if (dropItems(player, ZOMBIE_SKIN, 1, 5, 200000))
			st.setCond(2);
	}
}