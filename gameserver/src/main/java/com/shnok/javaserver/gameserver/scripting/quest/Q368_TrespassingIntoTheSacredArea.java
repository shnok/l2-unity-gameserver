package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q368_TrespassingIntoTheSacredArea extends Quest
{
	private static final String QUEST_NAME = "Q368_TrespassingIntoTheSacredArea";
	
	// NPC
	private static final int RESTINA = 30926;
	
	// Item
	private static final int FANG = 5881;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(20794, 500000);
		CHANCES.put(20795, 770000);
		CHANCES.put(20796, 500000);
		CHANCES.put(20797, 480000);
	}
	
	public Q368_TrespassingIntoTheSacredArea()
	{
		super(368, "Trespassing into the Sacred Area");
		
		setItemsIds(FANG);
		
		addQuestStart(RESTINA);
		addTalkId(RESTINA);
		
		addMyDying(20794, 20795, 20796, 20797);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30926-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30926-05.htm"))
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
				htmltext = (player.getStatus().getLevel() < 36) ? "30926-01a.htm" : "30926-01.htm";
				break;
			
			case STARTED:
				final int fangs = player.getInventory().getItemCount(FANG);
				if (fangs == 0)
					htmltext = "30926-03.htm";
				else
				{
					final int reward = 250 * fangs + (fangs > 10 ? 5730 : 2000);
					
					htmltext = "30926-04.htm";
					takeItems(player, 5881, -1);
					rewardItems(player, 57, reward);
				}
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
		
		dropItems(st.getPlayer(), FANG, 1, 0, CHANCES.get(npc.getNpcId()));
	}
}