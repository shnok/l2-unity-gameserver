package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q006_StepIntoTheFuture extends Quest
{
	private static final String QUEST_NAME = "Q006_StepIntoTheFuture";
	
	// NPCs
	private static final int ROXXY = 30006;
	private static final int BAULRO = 30033;
	private static final int SIR_COLLIN = 30311;
	
	// Items
	private static final int BAULRO_LETTER = 7571;
	
	// Rewards
	private static final int SOE_GIRAN = 7559;
	private static final int MARK_TRAVELER = 7570;
	
	public Q006_StepIntoTheFuture()
	{
		super(6, "Step into the Future");
		
		setItemsIds(BAULRO_LETTER);
		
		addQuestStart(ROXXY);
		addTalkId(ROXXY, BAULRO, SIR_COLLIN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30006-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30033-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, BAULRO_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30311-02.htm"))
		{
			if (player.getInventory().hasItem(BAULRO_LETTER))
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BAULRO_LETTER, 1);
			}
			else
				htmltext = "30311-03.htm";
		}
		else if (event.equalsIgnoreCase("30006-06.htm"))
		{
			giveItems(player, MARK_TRAVELER, 1);
			rewardItems(player, SOE_GIRAN, 1);
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
				if (player.getRace() != ClassRace.HUMAN || player.getStatus().getLevel() < 3)
					htmltext = "30006-01.htm";
				else
					htmltext = "30006-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ROXXY:
						if (cond == 1 || cond == 2)
							htmltext = "30006-04.htm";
						else if (cond == 3)
							htmltext = "30006-05.htm";
						break;
					
					case BAULRO:
						if (cond == 1)
							htmltext = "30033-01.htm";
						else if (cond == 2)
							htmltext = "30033-03.htm";
						else
							htmltext = "30033-04.htm";
						break;
					
					case SIR_COLLIN:
						if (cond == 2)
							htmltext = "30311-01.htm";
						else if (cond == 3)
							htmltext = "30311-03a.htm";
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