package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

/**
 * The onKill section of that quest is directly written on Q611.
 */
public class Q612_WarWithKetraOrcs extends Quest
{
	private static final String QUEST_NAME = "Q612_WarWithKetraOrcs";
	
	// Items
	private static final int NEPENTHES_SEED = 7187;
	private static final int MOLAR_OF_KETRA_ORC = 7234;
	
	public Q612_WarWithKetraOrcs()
	{
		super(612, "War with Ketra Orcs");
		
		setItemsIds(MOLAR_OF_KETRA_ORC);
		
		addQuestStart(31377); // Ashas Varka Durai
		addTalkId(31377);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31377-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31377-07.htm"))
		{
			if (player.getInventory().getItemCount(MOLAR_OF_KETRA_ORC) >= 100)
			{
				playSound(player, SOUND_ITEMGET);
				takeItems(player, MOLAR_OF_KETRA_ORC, 100);
				giveItems(player, NEPENTHES_SEED, 20);
			}
			else
				htmltext = "31377-08.htm";
		}
		else if (event.equalsIgnoreCase("31377-09.htm"))
		{
			takeItems(player, MOLAR_OF_KETRA_ORC, -1);
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
				htmltext = (player.getStatus().getLevel() >= 74 && player.isAlliedWithVarka()) ? "31377-01.htm" : "31377-02.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasItem(MOLAR_OF_KETRA_ORC)) ? "31377-04.htm" : "31377-05.htm";
				break;
		}
		
		return htmltext;
	}
}