package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q352_HelpRoodRaiseANewPet extends Quest
{
	private static final String QUEST_NAME = "Q352_HelpRoodRaiseANewPet";
	
	// Items
	private static final int LIENRIK_EGG_1 = 5860;
	private static final int LIENRIK_EGG_2 = 5861;
	
	public Q352_HelpRoodRaiseANewPet()
	{
		super(352, "Help Rood Raise A New Pet!");
		
		setItemsIds(LIENRIK_EGG_1, LIENRIK_EGG_2);
		
		addQuestStart(31067); // Rood
		addTalkId(31067);
		
		addMyDying(20786, 20787, 21644, 21645);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31067-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31067-09.htm"))
		{
			playSound(player, SOUND_FINISH);
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
				htmltext = (player.getStatus().getLevel() < 39) ? "31067-00.htm" : "31067-01.htm";
				break;
			
			case STARTED:
				final int eggs1 = player.getInventory().getItemCount(LIENRIK_EGG_1);
				final int eggs2 = player.getInventory().getItemCount(LIENRIK_EGG_2);
				
				if (eggs1 + eggs2 == 0)
					htmltext = "31067-05.htm";
				else
				{
					int reward = 2000;
					if (eggs1 > 0 && eggs2 == 0)
					{
						htmltext = "31067-06.htm";
						reward += eggs1 * 34;
						
						takeItems(player, LIENRIK_EGG_1, -1);
						rewardItems(player, 57, reward);
					}
					else if (eggs1 == 0 && eggs2 > 0)
					{
						htmltext = "31067-08.htm";
						reward += eggs2 * 1025;
						
						takeItems(player, LIENRIK_EGG_2, -1);
						rewardItems(player, 57, reward);
					}
					else if (eggs1 > 0 && eggs2 > 0)
					{
						htmltext = "31067-08.htm";
						reward += (eggs1 * 34) + (eggs2 * 1025) + 2000;
						
						takeItems(player, LIENRIK_EGG_1, -1);
						takeItems(player, LIENRIK_EGG_2, -1);
						rewardItems(player, 57, reward);
					}
				}
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
		
		final int npcId = npc.getNpcId();
		final int random = Rnd.get(100);
		final int chance = (npcId == 20786 || npcId == 21644) ? 44 : 58;
		
		if (random < chance)
			dropItemsAlways(player, LIENRIK_EGG_1, 1, 0);
		else if (random < (chance + 4))
			dropItemsAlways(player, LIENRIK_EGG_2, 1, 0);
	}
}