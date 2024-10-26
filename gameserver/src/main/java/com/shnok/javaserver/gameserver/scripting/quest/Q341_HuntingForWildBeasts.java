package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q341_HuntingForWildBeasts extends Quest
{
	private static final String QUEST_NAME = "Q341_HuntingForWildBeasts";
	
	// Item
	private static final int BEAR_SKIN = 4259;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(20021, 500000); // Red Bear
		CHANCES.put(20203, 900000); // Dion Grizzly
		CHANCES.put(20310, 500000); // Brown Bear
		CHANCES.put(20335, 700000); // Grizzly Bear
	}
	
	public Q341_HuntingForWildBeasts()
	{
		super(341, "Hunting for Wild Beasts");
		
		setItemsIds(BEAR_SKIN);
		
		addQuestStart(30078); // Pano
		addTalkId(30078);
		
		addMyDying(20021, 20203, 20310, 20335);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30078-02.htm"))
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
				htmltext = (player.getStatus().getLevel() < 20) ? "30078-00.htm" : "30078-01.htm";
				break;
			
			case STARTED:
				if (player.getInventory().getItemCount(BEAR_SKIN) < 20)
					htmltext = "30078-03.htm";
				else
				{
					htmltext = "30078-04.htm";
					takeItems(player, BEAR_SKIN, -1);
					rewardItems(player, 57, 3710);
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
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		dropItems(player, BEAR_SKIN, 1, 20, CHANCES.get(npc.getNpcId()));
	}
}