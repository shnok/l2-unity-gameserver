package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q642_APowerfulPrimevalCreature extends Quest
{
	private static final String QUEST_NAME = "Q642_APowerfulPrimevalCreature";
	
	// Items
	private static final int DINOSAUR_TISSUE = 8774;
	private static final int DINOSAUR_EGG = 8775;
	
	private static final int ANCIENT_EGG = 18344;
	
	// Rewards
	private static final int[] REWARDS =
	{
		8690,
		8692,
		8694,
		8696,
		8698,
		8700,
		8702,
		8704,
		8706,
		8708,
		8710
	};
	
	public Q642_APowerfulPrimevalCreature()
	{
		super(642, "A Powerful Primeval Creature");
		
		setItemsIds(DINOSAUR_TISSUE, DINOSAUR_EGG);
		
		addQuestStart(32105); // Dinn
		addTalkId(32105);
		
		// Dinosaurs + egg
		addMyDying(22196, 22197, 22198, 22199, 22200, 22201, 22202, 22203, 22204, 22205, 22218, 22219, 22220, 22223, 22224, 22225, ANCIENT_EGG);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32105-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32105-08.htm"))
		{
			if (player.getInventory().getItemCount(DINOSAUR_TISSUE) >= 150 && player.getInventory().hasItem(DINOSAUR_EGG))
				htmltext = "32105-06.htm";
		}
		else if (event.equalsIgnoreCase("32105-07.htm"))
		{
			final int tissues = player.getInventory().getItemCount(DINOSAUR_TISSUE);
			if (tissues > 0)
			{
				takeItems(player, DINOSAUR_TISSUE, -1);
				rewardItems(player, 57, tissues * 5000);
			}
			else
				htmltext = "32105-08.htm";
		}
		else if (event.contains("event_"))
		{
			if (player.getInventory().getItemCount(DINOSAUR_TISSUE) >= 150 && player.getInventory().hasItem(DINOSAUR_EGG))
			{
				htmltext = "32105-07.htm";
				
				takeItems(player, DINOSAUR_TISSUE, 150);
				takeItems(player, DINOSAUR_EGG, 1);
				rewardItems(player, 57, 44000);
				giveItems(player, REWARDS[Integer.parseInt(event.split("_")[1])], 1);
			}
			else
				htmltext = "32105-08.htm";
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
				htmltext = (!player.getInventory().hasItem(DINOSAUR_TISSUE)) ? "32105-08.htm" : "32105-05.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		if (npc.getNpcId() == ANCIENT_EGG)
		{
			if (Rnd.get(100) < 1)
			{
				giveItems(player, DINOSAUR_EGG, 1);
				
				if (player.getInventory().getItemCount(DINOSAUR_TISSUE) >= 150)
					playSound(player, SOUND_MIDDLE);
				else
					playSound(player, SOUND_ITEMGET);
			}
		}
		else if (Rnd.get(100) < 33)
		{
			rewardItems(player, DINOSAUR_TISSUE, 1);
			
			if (player.getInventory().getItemCount(DINOSAUR_TISSUE) >= 150 && player.getInventory().hasItem(DINOSAUR_EGG))
				playSound(player, SOUND_MIDDLE);
			else
				playSound(player, SOUND_ITEMGET);
		}
	}
}