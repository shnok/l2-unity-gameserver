package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q001_LettersOfLove extends Quest
{
	private static final String QUEST_NAME = "Q001_LettersOfLove";
	
	// Npcs
	private static final int DARIN = 30048;
	private static final int ROXXY = 30006;
	private static final int BAULRO = 30033;
	
	// Items
	private static final int DARIN_LETTER = 687;
	private static final int ROXXY_KERCHIEF = 688;
	private static final int DARIN_RECEIPT = 1079;
	private static final int BAULRO_POTION = 1080;
	
	// Reward
	private static final int NECKLACE = 906;
	
	public Q001_LettersOfLove()
	{
		super(1, "Letters of Love");
		
		setItemsIds(DARIN_LETTER, ROXXY_KERCHIEF, DARIN_RECEIPT, BAULRO_POTION);
		
		addQuestStart(DARIN);
		addTalkId(DARIN, ROXXY, BAULRO);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30048-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, DARIN_LETTER, 1);
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
				htmltext = (player.getStatus().getLevel() < 2) ? "30048-01.htm" : "30048-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case DARIN:
						if (cond == 1)
							htmltext = "30048-07.htm";
						else if (cond == 2)
						{
							htmltext = "30048-08.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ROXXY_KERCHIEF, 1);
							giveItems(player, DARIN_RECEIPT, 1);
						}
						else if (cond == 3)
							htmltext = "30048-09.htm";
						else if (cond == 4)
						{
							htmltext = "30048-10.htm";
							takeItems(player, BAULRO_POTION, 1);
							giveItems(player, NECKLACE, 1);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ROXXY:
						if (cond == 1)
						{
							htmltext = "30006-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, DARIN_LETTER, 1);
							giveItems(player, ROXXY_KERCHIEF, 1);
						}
						else if (cond == 2)
							htmltext = "30006-02.htm";
						else if (cond > 2)
							htmltext = "30006-03.htm";
						break;
					
					case BAULRO:
						if (cond == 3)
						{
							htmltext = "30033-01.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, DARIN_RECEIPT, 1);
							giveItems(player, BAULRO_POTION, 1);
						}
						else if (cond == 4)
							htmltext = "30033-02.htm";
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