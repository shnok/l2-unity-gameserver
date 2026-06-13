package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q112_WalkOfFate extends Quest
{
	private static final String QUEST_NAME = "Q112_WalkOfFate";
	
	// NPCs
	private static final int LIVINA = 30572;
	private static final int KARUDA = 32017;
	
	// Rewards
	private static final int ENCHANT_D = 956;
	
	public Q112_WalkOfFate()
	{
		super(112, "Walk of Fate");
		
		addQuestStart(LIVINA);
		addTalkId(LIVINA, KARUDA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30572-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32017-02.htm"))
		{
			giveItems(player, ENCHANT_D, 1);
			rewardItems(player, 57, 4665);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
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
				htmltext = (player.getStatus().getLevel() < 20) ? "30572-00.htm" : "30572-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case LIVINA:
						htmltext = "30572-03.htm";
						break;
					
					case KARUDA:
						htmltext = "32017-01.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}