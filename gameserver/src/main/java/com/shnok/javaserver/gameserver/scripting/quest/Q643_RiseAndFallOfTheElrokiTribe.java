package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q643_RiseAndFallOfTheElrokiTribe extends Quest
{
	private static final String QUEST_NAME = "Q643_RiseAndFallOfTheElrokiTribe";
	
	// NPCs
	private static final int SINGSING = 32106;
	private static final int KARAKAWEI = 32117;
	
	// Items
	private static final int BONES = 8776;
	
	public Q643_RiseAndFallOfTheElrokiTribe()
	{
		super(643, "Rise and Fall of the Elroki Tribe");
		
		setItemsIds(BONES);
		
		addQuestStart(SINGSING);
		addTalkId(SINGSING, KARAKAWEI);
		
		addMyDying(22208, 22209, 22210, 22211, 22212, 22213, 22221, 22222, 22226, 22227);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32106-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32106-07.htm"))
		{
			final int count = player.getInventory().getItemCount(BONES);
			
			takeItems(player, BONES, count);
			rewardItems(player, 57, count * 1374);
		}
		else if (event.equalsIgnoreCase("32106-09.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("32117-03.htm"))
		{
			final int count = player.getInventory().getItemCount(BONES);
			if (count >= 300)
			{
				takeItems(player, BONES, 300);
				rewardItems(player, Rnd.get(8712, 8722), 5);
			}
			else
				htmltext = "32117-04.htm";
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
				htmltext = (player.getStatus().getLevel() < 75) ? "32106-00.htm" : "32106-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case SINGSING:
						htmltext = (player.getInventory().hasItem(BONES)) ? "32106-06.htm" : "32106-05.htm";
						break;
					
					case KARAKAWEI:
						htmltext = "32117-01.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		dropItems(st.getPlayer(), BONES, 1, 0, 750000);
	}
}