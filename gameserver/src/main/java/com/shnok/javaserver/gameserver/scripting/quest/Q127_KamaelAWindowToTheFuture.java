package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExPlayScene;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q127_KamaelAWindowToTheFuture extends Quest
{
	private static final String QUEST_NAME = "Q127_KamaelAWindowToTheFuture";
	
	// NPCs
	private static final int DOMINIC = 31350;
	private static final int KLAUS = 30187;
	private static final int ALDER = 32092;
	private static final int AKLAN = 31288;
	private static final int OLTLIN = 30862;
	private static final int JURIS = 30113;
	private static final int RODEMAI = 30756;
	
	// Items
	private static final int MARK_DOMINIC = 8939;
	private static final int MARK_HUMAN = 8940;
	private static final int MARK_DWARF = 8941;
	private static final int MARK_ORC = 8944;
	private static final int MARK_DELF = 8943;
	private static final int MARK_ELF = 8942;
	
	public Q127_KamaelAWindowToTheFuture()
	{
		super(127, "Kamael: A Window to the Future");
		
		setItemsIds(MARK_DOMINIC, MARK_HUMAN, MARK_DWARF, MARK_ORC, MARK_DELF, MARK_ELF);
		
		addQuestStart(DOMINIC);
		addTalkId(DOMINIC, KLAUS, ALDER, AKLAN, OLTLIN, JURIS, RODEMAI);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31350-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, MARK_DOMINIC, 1);
		}
		else if (event.equalsIgnoreCase("31350-06.htm"))
		{
			takeItems(player, MARK_HUMAN, -1);
			takeItems(player, MARK_DWARF, -1);
			takeItems(player, MARK_ELF, -1);
			takeItems(player, MARK_DELF, -1);
			takeItems(player, MARK_ORC, -1);
			takeItems(player, MARK_DOMINIC, -1);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("30187-06.htm"))
			st.setCond(2);
		else if (event.equalsIgnoreCase("30187-08.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, MARK_HUMAN, 1);
		}
		else if (event.equalsIgnoreCase("32092-05.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, MARK_DWARF, 1);
		}
		else if (event.equalsIgnoreCase("31288-04.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, MARK_ORC, 1);
		}
		else if (event.equalsIgnoreCase("30862-04.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, MARK_DELF, 1);
		}
		else if (event.equalsIgnoreCase("30113-04.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, MARK_ELF, 1);
		}
		else if (event.equalsIgnoreCase("kamaelstory"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			player.sendPacket(ExPlayScene.STATIC_PACKET);
			return null;
		}
		else if (event.equalsIgnoreCase("30756-05.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
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
		
		npc.getNpcId();
		int cond = st.getCond();
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = "31350-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case KLAUS:
						if (cond == 1)
							htmltext = "30187-01.htm";
						else if (cond == 2)
							htmltext = "30187-06.htm";
						break;
					
					case ALDER:
						if (cond == 3)
							htmltext = "32092-01.htm";
						break;
					
					case AKLAN:
						if (cond == 4)
							htmltext = "31288-01.htm";
						break;
					
					case OLTLIN:
						if (cond == 5)
							htmltext = "30862-01.htm";
						break;
					
					case JURIS:
						if (cond == 6)
							htmltext = "30113-01.htm";
						break;
					
					case RODEMAI:
						if (cond == 7)
							htmltext = "30756-01.htm";
						else if (cond == 8)
							htmltext = "30756-04.htm";
						break;
					
					case DOMINIC:
						if (cond == 9)
							htmltext = "31350-05.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				return htmltext;
		}
		
		return htmltext;
	}
}