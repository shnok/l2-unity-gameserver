package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q324_SweetestVenom extends Quest
{
	private static final String QUEST_NAME = "Q324_SweetestVenom";
	
	// Item
	private static final int VENOM_SAC = 1077;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(20034, 220000);
		CHANCES.put(20038, 230000);
		CHANCES.put(20043, 250000);
	}
	
	public Q324_SweetestVenom()
	{
		super(324, "Sweetest Venom");
		
		setItemsIds(VENOM_SAC);
		
		addQuestStart(30351); // Astaron
		addTalkId(30351);
		
		addMyDying(20034, 20038, 20043);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30351-04.htm"))
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
				htmltext = (player.getStatus().getLevel() < 18) ? "30351-02.htm" : "30351-03.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 1)
					htmltext = "30351-05.htm";
				else
				{
					htmltext = "30351-06.htm";
					takeItems(player, VENOM_SAC, -1);
					rewardItems(player, 57, 5810);
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
		
		if (dropItems(player, VENOM_SAC, 1, 10, CHANCES.get(npc.getNpcId())))
			st.setCond(2);
	}
}