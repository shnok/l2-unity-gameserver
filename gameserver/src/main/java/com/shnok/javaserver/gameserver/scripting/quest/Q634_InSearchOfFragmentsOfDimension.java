package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q634_InSearchOfFragmentsOfDimension extends Quest
{
	private static final String QUEST_NAME = "Q634_InSearchOfFragmentsOfDimension";
	
	// Items
	private static final int DIMENSION_FRAGMENT = 7079;
	
	public Q634_InSearchOfFragmentsOfDimension()
	{
		super(634, "In Search of Fragments of Dimension");
		
		// Dimensional Gate Keepers.
		for (int i = 31494; i < 31508; i++)
		{
			addQuestStart(i);
			addTalkId(i);
		}
		
		// All mobs.
		for (int i = 21208; i < 21256; i++)
			addMyDying(i);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("05.htm"))
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
				htmltext = (player.getStatus().getLevel() < 20) ? "01a.htm" : "01.htm";
				break;
			
			case STARTED:
				htmltext = "03.htm";
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
		
		dropItems(st.getPlayer(), DIMENSION_FRAGMENT, (int) (npc.getStatus().getLevel() * 0.15 + 1.6), -1, 900000);
	}
}