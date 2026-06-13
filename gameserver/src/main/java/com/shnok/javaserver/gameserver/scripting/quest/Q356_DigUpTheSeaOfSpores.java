package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q356_DigUpTheSeaOfSpores extends Quest
{
	private static final String QUEST_NAME = "Q356_DigUpTheSeaOfSpores";
	
	// Items
	private static final int HERB_SPORE = 5866;
	private static final int CARN_SPORE = 5865;
	
	// Monsters
	private static final int ROTTING_TREE = 20558;
	private static final int SPORE_ZOMBIE = 20562;
	
	public Q356_DigUpTheSeaOfSpores()
	{
		super(356, "Dig Up the Sea of Spores!");
		
		setItemsIds(HERB_SPORE, CARN_SPORE);
		
		addQuestStart(30717); // Gauen
		addTalkId(30717);
		
		addMyDying(ROTTING_TREE, SPORE_ZOMBIE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30717-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30717-17.htm"))
		{
			takeItems(player, HERB_SPORE, -1);
			takeItems(player, CARN_SPORE, -1);
			rewardItems(player, 57, 20950);
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30717-14.htm"))
		{
			takeItems(player, HERB_SPORE, -1);
			takeItems(player, CARN_SPORE, -1);
			rewardExpAndSp(player, 35000, 2600);
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30717-12.htm"))
		{
			takeItems(player, HERB_SPORE, -1);
			rewardExpAndSp(player, 24500, 0);
		}
		else if (event.equalsIgnoreCase("30717-13.htm"))
		{
			takeItems(player, CARN_SPORE, -1);
			rewardExpAndSp(player, 0, 1820);
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
				htmltext = (player.getStatus().getLevel() < 43) ? "30717-01.htm" : "30717-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				if (cond == 1)
					htmltext = "30717-07.htm";
				else if (cond == 2)
				{
					if (player.getInventory().getItemCount(HERB_SPORE) >= 50)
						htmltext = "30717-08.htm";
					else if (player.getInventory().getItemCount(CARN_SPORE) >= 50)
						htmltext = "30717-09.htm";
					else
						htmltext = "30717-07.htm";
				}
				else if (cond == 3)
					htmltext = "30717-10.htm";
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
		
		final int cond = st.getCond();
		if (cond < 3)
		{
			switch (npc.getNpcId())
			{
				case ROTTING_TREE:
					if (dropItems(player, HERB_SPORE, 1, 50, 630000))
						st.setCond((cond == 2) ? 3 : 2);
					break;
				
				case SPORE_ZOMBIE:
					if (dropItems(player, CARN_SPORE, 1, 50, 760000))
						st.setCond((cond == 2) ? 3 : 2);
					break;
			}
		}
	}
}