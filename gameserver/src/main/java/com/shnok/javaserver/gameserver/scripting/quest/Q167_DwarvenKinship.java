package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q167_DwarvenKinship extends Quest
{
	private static final String QUEST_NAME = "Q167_DwarvenKinship";
	
	// Items
	private static final int CARLON_LETTER = 1076;
	private static final int NORMAN_LETTER = 1106;
	
	// NPCs
	private static final int CARLON = 30350;
	private static final int NORMAN = 30210;
	private static final int HAPROCK = 30255;
	
	public Q167_DwarvenKinship()
	{
		super(167, "Dwarven Kinship");
		
		setItemsIds(CARLON_LETTER, NORMAN_LETTER);
		
		addQuestStart(CARLON);
		addTalkId(CARLON, HAPROCK, NORMAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30350-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, CARLON_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30255-03.htm"))
		{
			st.setCond(2);
			takeItems(player, CARLON_LETTER, 1);
			giveItems(player, NORMAN_LETTER, 1);
			rewardItems(player, 57, 2000);
		}
		else if (event.equalsIgnoreCase("30255-04.htm"))
		{
			takeItems(player, CARLON_LETTER, 1);
			rewardItems(player, 57, 3000);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("30210-02.htm"))
		{
			takeItems(player, NORMAN_LETTER, 1);
			rewardItems(player, 57, 20000);
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30350-02.htm" : "30350-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case CARLON:
						if (cond == 1)
							htmltext = "30350-05.htm";
						break;
					
					case HAPROCK:
						if (cond == 1)
							htmltext = "30255-01.htm";
						else if (cond == 2)
							htmltext = "30255-05.htm";
						break;
					
					case NORMAN:
						if (cond == 2)
							htmltext = "30210-01.htm";
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