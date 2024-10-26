package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q261_CollectorsDream extends Quest
{
	private static final String QUEST_NAME = "Q261_CollectorsDream";
	
	// Items
	private static final int GIANT_SPIDER_LEG = 1087;
	
	public Q261_CollectorsDream()
	{
		super(261, "Collector's Dream");
		
		setItemsIds(GIANT_SPIDER_LEG);
		
		addQuestStart(30222); // Alshupes
		addTalkId(30222);
		
		addMyDying(20308, 20460, 20466);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30222-03.htm"))
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30222-01.htm" : "30222-02.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 2)
				{
					htmltext = "30222-05.htm";
					takeItems(player, GIANT_SPIDER_LEG, -1);
					rewardItems(player, 57, 1000);
					rewardExpAndSp(player, 2000, 0);
					playSound(player, SOUND_FINISH);
					st.exitQuest(true);
				}
				else
					htmltext = "30222-04.htm";
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
		
		if (dropItemsAlways(player, GIANT_SPIDER_LEG, 1, 8))
			st.setCond(2);
	}
}