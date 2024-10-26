package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q015_SweetWhispers extends Quest
{
	private static final String QUEST_NAME = "Q015_SweetWhispers";
	
	// NPCs
	private static final int VLADIMIR = 31302;
	private static final int HIERARCH = 31517;
	private static final int MYSTERIOUS_NECRO = 31518;
	
	public Q015_SweetWhispers()
	{
		super(15, "Sweet Whispers");
		
		addQuestStart(VLADIMIR);
		addTalkId(VLADIMIR, HIERARCH, MYSTERIOUS_NECRO);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31302-01.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31518-01.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31517-01.htm"))
		{
			rewardExpAndSp(player, 60217, 0);
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
				htmltext = (player.getStatus().getLevel() < 60) ? "31302-00a.htm" : "31302-00.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case VLADIMIR:
						htmltext = "31302-01a.htm";
						break;
					
					case MYSTERIOUS_NECRO:
						if (cond == 1)
							htmltext = "31518-00.htm";
						else if (cond == 2)
							htmltext = "31518-01a.htm";
						break;
					
					case HIERARCH:
						if (cond == 2)
							htmltext = "31517-00.htm";
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