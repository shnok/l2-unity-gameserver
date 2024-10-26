package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q119_LastImperialPrince extends Quest
{
	private static final String QUEST_NAME = "Q119_LastImperialPrince";
	
	// NPCs
	private static final int NAMELESS_SPIRIT = 31453;
	private static final int DEVORIN = 32009;
	
	// Item
	private static final int ANTIQUE_BROOCH = 7262;
	
	public Q119_LastImperialPrince()
	{
		super(119, "Last Imperial Prince");
		
		setItemsIds(ANTIQUE_BROOCH);
		
		addQuestStart(NAMELESS_SPIRIT);
		addTalkId(NAMELESS_SPIRIT, DEVORIN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31453-04.htm"))
		{
			if (player.getInventory().hasItem(ANTIQUE_BROOCH))
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
			{
				htmltext = "31453-04b.htm";
				st.exitQuest(true);
			}
		}
		else if (event.equalsIgnoreCase("32009-02.htm"))
		{
			if (!player.getInventory().hasItem(ANTIQUE_BROOCH))
			{
				htmltext = "31453-02a.htm";
				st.exitQuest(true);
			}
		}
		else if (event.equalsIgnoreCase("32009-03.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31453-07.htm"))
		{
			rewardItems(player, 57, 68787);
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
				htmltext = (!player.getInventory().hasItem(ANTIQUE_BROOCH) || player.getStatus().getLevel() < 74) ? "31453-00a.htm" : "31453-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case NAMELESS_SPIRIT:
						if (cond == 1)
							htmltext = "31453-04a.htm";
						else if (cond == 2)
							htmltext = "31453-05.htm";
						break;
					
					case DEVORIN:
						if (cond == 1)
							htmltext = "32009-01.htm";
						else if (cond == 2)
							htmltext = "32009-04.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = "31453-00b.htm";
				break;
		}
		
		return htmltext;
	}
}