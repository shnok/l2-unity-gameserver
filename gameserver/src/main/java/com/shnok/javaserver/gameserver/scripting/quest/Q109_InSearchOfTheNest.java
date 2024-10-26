package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q109_InSearchOfTheNest extends Quest
{
	private static final String QUEST_NAME = "Q109_InSearchOfTheNest";
	
	// NPCs
	private static final int PIERCE = 31553;
	private static final int KAHMAN = 31554;
	private static final int SCOUT_CORPSE = 32015;
	
	// Items
	private static final int SCOUT_MEMO = 8083;
	private static final int RECRUIT_BADGE = 7246;
	private static final int SOLDIER_BADGE = 7247;
	
	public Q109_InSearchOfTheNest()
	{
		super(109, "In Search of the Nest");
		
		setItemsIds(SCOUT_MEMO);
		
		addQuestStart(PIERCE);
		addTalkId(PIERCE, SCOUT_CORPSE, KAHMAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31553-01.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32015-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, SCOUT_MEMO, 1);
		}
		else if (event.equalsIgnoreCase("31553-03.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SCOUT_MEMO, 1);
		}
		else if (event.equalsIgnoreCase("31554-02.htm"))
		{
			rewardItems(player, 57, 5168);
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
				// Must worn one or other Golden Ram Badge in order to be accepted.
				if (player.getStatus().getLevel() >= 66 && player.getInventory().hasAtLeastOneItem(RECRUIT_BADGE, SOLDIER_BADGE))
					htmltext = "31553-00.htm";
				else
					htmltext = "31553-00a.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case PIERCE:
						if (cond == 1)
							htmltext = "31553-01a.htm";
						else if (cond == 2)
							htmltext = "31553-02.htm";
						else if (cond == 3)
							htmltext = "31553-03.htm";
						break;
					
					case SCOUT_CORPSE:
						if (cond == 1)
							htmltext = "32015-01.htm";
						else if (cond == 2)
							htmltext = "32015-02.htm";
						break;
					
					case KAHMAN:
						if (cond == 3)
							htmltext = "31554-01.htm";
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