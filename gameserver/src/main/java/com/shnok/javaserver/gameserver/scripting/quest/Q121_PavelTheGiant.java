package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q121_PavelTheGiant extends Quest
{
	private static final String QUEST_NAME = "Q121_PavelTheGiant";
	
	// NPCs
	private static final int NEWYEAR = 31961;
	private static final int YUMI = 32041;
	
	public Q121_PavelTheGiant()
	{
		super(121, "Pavel the Giant");
		
		addQuestStart(NEWYEAR);
		addTalkId(NEWYEAR, YUMI);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31961-2.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32041-2.htm"))
		{
			rewardExpAndSp(player, 10000, 0);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
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
				htmltext = (player.getStatus().getLevel() < 46) ? "31961-1a.htm" : "31961-1.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case NEWYEAR:
						htmltext = "31961-2a.htm";
						break;
					
					case YUMI:
						htmltext = "32041-1.htm";
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