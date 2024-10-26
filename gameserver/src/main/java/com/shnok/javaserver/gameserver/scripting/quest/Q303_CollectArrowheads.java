package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q303_CollectArrowheads extends Quest
{
	private static final String QUEST_NAME = "Q303_CollectArrowheads";
	
	// Item
	private static final int ORCISH_ARROWHEAD = 963;
	
	public Q303_CollectArrowheads()
	{
		super(303, "Collect Arrowheads");
		
		setItemsIds(ORCISH_ARROWHEAD);
		
		addQuestStart(30029); // Minia
		addTalkId(30029);
		
		addMyDying(20361);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30029-03.htm"))
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
				htmltext = (player.getStatus().getLevel() < 10) ? "30029-01.htm" : "30029-02.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 1)
					htmltext = "30029-04.htm";
				else
				{
					htmltext = "30029-05.htm";
					takeItems(player, ORCISH_ARROWHEAD, -1);
					rewardItems(player, 57, 1000);
					rewardExpAndSp(player, 2000, 0);
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
		
		if (dropItems(player, ORCISH_ARROWHEAD, 1, 10, 400000))
			st.setCond(2);
	}
}