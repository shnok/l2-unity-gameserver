package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

/**
 * The onKill section of that quest is directly written on Q605.
 */
public class Q606_WarWithVarkaSilenos extends Quest
{
	private static final String QUEST_NAME = "Q606_WarWithVarkaSilenos";
	
	// Items
	private static final int HORN_OF_BUFFALO = 7186;
	private static final int VARKA_MANE = 7233;
	
	public Q606_WarWithVarkaSilenos()
	{
		super(606, "War with Varka Silenos");
		
		setItemsIds(VARKA_MANE);
		
		addQuestStart(31370); // Kadun Zu Ketra
		addTalkId(31370);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31370-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31370-07.htm"))
		{
			if (player.getInventory().getItemCount(VARKA_MANE) >= 100)
			{
				playSound(player, SOUND_ITEMGET);
				takeItems(player, VARKA_MANE, 100);
				giveItems(player, HORN_OF_BUFFALO, 20);
			}
			else
				htmltext = "31370-08.htm";
		}
		else if (event.equalsIgnoreCase("31370-09.htm"))
		{
			takeItems(player, VARKA_MANE, -1);
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
				htmltext = (player.getStatus().getLevel() >= 74 && player.isAlliedWithKetra()) ? "31370-01.htm" : "31370-02.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasItem(VARKA_MANE)) ? "31370-04.htm" : "31370-05.htm";
				break;
		}
		
		return htmltext;
	}
}