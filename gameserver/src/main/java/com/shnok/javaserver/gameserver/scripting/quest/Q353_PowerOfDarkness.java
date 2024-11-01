package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q353_PowerOfDarkness extends Quest
{
	private static final String QUEST_NAME = "Q353_PowerOfDarkness";
	
	// Item
	private static final int STONE = 5862;
	
	public Q353_PowerOfDarkness()
	{
		super(353, "Power of Darkness");
		
		setItemsIds(STONE);
		
		addQuestStart(31044); // Galman
		addTalkId(31044);
		
		addMyDying(20244, 20245, 20283, 20284);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31044-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31044-08.htm"))
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
				htmltext = (player.getStatus().getLevel() < 55) ? "31044-01.htm" : "31044-02.htm";
				break;
			
			case STARTED:
				final int stones = player.getInventory().getItemCount(STONE);
				if (stones == 0)
					htmltext = "31044-05.htm";
				else
				{
					htmltext = "31044-06.htm";
					takeItems(player, STONE, -1);
					rewardItems(player, 57, 2500 + 230 * stones);
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
		
		dropItems(player, STONE, 1, 0, (npc.getNpcId() == 20244 || npc.getNpcId() == 20283) ? 480000 : 500000);
	}
}