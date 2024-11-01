package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q043_HelpTheSister extends Quest
{
	private static final String QUEST_NAME = "Q043_HelpTheSister";
	
	// NPCs
	private static final int COOPER = 30829;
	private static final int GALLADUCCI = 30097;
	
	// Items
	private static final int CRAFTED_DAGGER = 220;
	private static final int MAP_PIECE = 7550;
	private static final int MAP = 7551;
	private static final int PET_TICKET = 7584;
	
	// Monsters
	private static final int SPECTER = 20171;
	private static final int SORROW_MAIDEN = 20197;
	
	public Q043_HelpTheSister()
	{
		super(43, "Help the Sister!");
		
		setItemsIds(MAP_PIECE, MAP);
		
		addQuestStart(COOPER);
		addTalkId(COOPER, GALLADUCCI);
		
		addMyDying(SPECTER, SORROW_MAIDEN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30829-01.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30829-03.htm") && player.getInventory().hasItem(CRAFTED_DAGGER))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRAFTED_DAGGER, 1);
		}
		else if (event.equalsIgnoreCase("30829-05.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MAP_PIECE, 30);
			giveItems(player, MAP, 1);
		}
		else if (event.equalsIgnoreCase("30097-06.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MAP, 1);
		}
		else if (event.equalsIgnoreCase("30829-07.htm"))
		{
			giveItems(player, PET_TICKET, 1);
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
				htmltext = (player.getStatus().getLevel() < 26) ? "30829-00a.htm" : "30829-00.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case COOPER:
						if (cond == 1)
							htmltext = (!player.getInventory().hasItem(CRAFTED_DAGGER)) ? "30829-01a.htm" : "30829-02.htm";
						else if (cond == 2)
							htmltext = "30829-03a.htm";
						else if (cond == 3)
							htmltext = "30829-04.htm";
						else if (cond == 4)
							htmltext = "30829-05a.htm";
						else if (cond == 5)
							htmltext = "30829-06.htm";
						break;
					
					case GALLADUCCI:
						if (cond == 4)
							htmltext = "30097-05.htm";
						else if (cond == 5)
							htmltext = "30097-06a.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 2);
		if (st == null)
			return;
		
		if (dropItemsAlways(player, MAP_PIECE, 1, 30))
			st.setCond(3);
	}
}