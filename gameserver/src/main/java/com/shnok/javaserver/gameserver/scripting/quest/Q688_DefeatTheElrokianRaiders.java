package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q688_DefeatTheElrokianRaiders extends Quest
{
	private static final String QUEST_NAME = "Q688_DefeatTheElrokianRaiders";
	
	// Item
	private static final int DINOSAUR_FANG_NECKLACE = 8785;
	
	// NPC
	private static final int DINN = 32105;
	
	// Monster
	private static final int ELROKI = 22214;
	
	public Q688_DefeatTheElrokianRaiders()
	{
		super(688, "Defeat the Elrokian Raiders!");
		
		setItemsIds(DINOSAUR_FANG_NECKLACE);
		
		addQuestStart(DINN);
		addTalkId(DINN);
		
		addMyDying(ELROKI);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32105-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32105-08.htm"))
		{
			final int count = player.getInventory().getItemCount(DINOSAUR_FANG_NECKLACE);
			if (count > 0)
			{
				takeItems(player, DINOSAUR_FANG_NECKLACE, -1);
				rewardItems(player, 57, count * 3000);
			}
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("32105-06.htm"))
		{
			final int count = player.getInventory().getItemCount(DINOSAUR_FANG_NECKLACE);
			
			takeItems(player, DINOSAUR_FANG_NECKLACE, -1);
			rewardItems(player, 57, count * 3000);
		}
		else if (event.equalsIgnoreCase("32105-07.htm"))
		{
			final int count = player.getInventory().getItemCount(DINOSAUR_FANG_NECKLACE);
			if (count >= 100)
			{
				takeItems(player, DINOSAUR_FANG_NECKLACE, 100);
				rewardItems(player, 57, 450000);
			}
			else
				htmltext = "32105-04.htm";
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
				htmltext = (player.getStatus().getLevel() < 75) ? "32105-00.htm" : "32105-01.htm";
				break;
			
			case STARTED:
				htmltext = (!player.getInventory().hasItem(DINOSAUR_FANG_NECKLACE)) ? "32105-04.htm" : "32105-05.htm";
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
		
		dropItems(st.getPlayer(), DINOSAUR_FANG_NECKLACE, 1, 0, 500000);
	}
}