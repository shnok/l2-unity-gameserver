package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q654_JourneyToASettlement extends Quest
{
	private static final String QUEST_NAME = "Q654_JourneyToASettlement";
	
	// Item
	private static final int ANTELOPE_SKIN = 8072;
	
	// Reward
	private static final int FORCE_FIELD_REMOVAL_SCROLL = 8073;
	
	public Q654_JourneyToASettlement()
	{
		super(654, "Journey to a Settlement");
		
		setItemsIds(ANTELOPE_SKIN);
		
		addQuestStart(31453); // Nameless Spirit
		addTalkId(31453);
		
		addMyDying(21294, 21295); // Canyon Antelope, Canyon Antelope Slave
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31453-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31453-03.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31453-06.htm"))
		{
			takeItems(player, ANTELOPE_SKIN, -1);
			giveItems(player, FORCE_FIELD_REMOVAL_SCROLL, 1);
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
				QuestState prevSt = player.getQuestList().getQuestState("Q119_LastImperialPrince");
				htmltext = (prevSt == null || !prevSt.isCompleted() || player.getStatus().getLevel() < 74) ? "31453-00.htm" : "31453-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "31453-02.htm";
				else if (cond == 2)
					htmltext = "31453-04.htm";
				else if (cond == 3)
					htmltext = "31453-05.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 2);
		if (st == null)
			return;
		
		if (dropItems(player, ANTELOPE_SKIN, 1, 1, 50000))
			st.setCond(3);
	}
}