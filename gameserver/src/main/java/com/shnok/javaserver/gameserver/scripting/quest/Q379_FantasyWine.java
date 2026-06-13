package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q379_FantasyWine extends Quest
{
	private static final String QUEST_NAME = "Q379_FantasyWine";
	
	// NPCs
	private static final int HARLAN = 30074;
	
	// Monsters
	private static final int ENKU_CHAMPION = 20291;
	private static final int ENKU_SHAMAN = 20292;
	
	// Items
	private static final int LEAF = 5893;
	private static final int STONE = 5894;
	
	public Q379_FantasyWine()
	{
		super(379, "Fantasy Wine");
		
		setItemsIds(LEAF, STONE);
		
		addQuestStart(HARLAN);
		addTalkId(HARLAN);
		
		addMyDying(ENKU_CHAMPION, ENKU_SHAMAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30074-3.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30074-6.htm"))
		{
			takeItems(player, LEAF, 80);
			takeItems(player, STONE, 100);
			
			final int rand = Rnd.get(10);
			if (rand < 3)
			{
				htmltext = "30074-6.htm";
				giveItems(player, 5956, 1);
			}
			else if (rand < 9)
			{
				htmltext = "30074-7.htm";
				giveItems(player, 5957, 1);
			}
			else
			{
				htmltext = "30074-8.htm";
				giveItems(player, 5958, 1);
			}
			
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30074-2a.htm"))
			st.exitQuest(true);
		
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
				htmltext = (player.getStatus().getLevel() < 20) ? "30074-0a.htm" : "30074-0.htm";
				break;
			
			case STARTED:
				final int leaf = player.getInventory().getItemCount(LEAF);
				final int stone = player.getInventory().getItemCount(STONE);
				
				if (leaf == 80 && stone == 100)
					htmltext = "30074-5.htm";
				else if (leaf == 80)
					htmltext = "30074-4a.htm";
				else if (stone == 100)
					htmltext = "30074-4b.htm";
				else
					htmltext = "30074-4.htm";
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
		
		if (npc.getNpcId() == ENKU_CHAMPION)
		{
			if (dropItemsAlways(player, LEAF, 1, 80) && player.getInventory().getItemCount(STONE) >= 100)
				st.setCond(2);
		}
		else if (dropItemsAlways(player, STONE, 1, 100) && player.getInventory().getItemCount(LEAF) >= 80)
			st.setCond(2);
	}
}