package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q623_TheFinestFood extends Quest
{
	private static final String QUEST_NAME = "Q623_TheFinestFood";
	
	// Items
	private static final int LEAF_OF_FLAVA = 7199;
	private static final int BUFFALO_MEAT = 7200;
	private static final int ANTELOPE_HORN = 7201;
	
	// NPC
	private static final int JEREMY = 31521;
	
	// Monsters
	private static final int FLAVA = 21316;
	private static final int BUFFALO = 21315;
	private static final int ANTELOPE = 21318;
	
	public Q623_TheFinestFood()
	{
		super(623, "The Finest Food");
		
		setItemsIds(LEAF_OF_FLAVA, BUFFALO_MEAT, ANTELOPE_HORN);
		
		addQuestStart(JEREMY);
		addTalkId(JEREMY);
		
		addMyDying(FLAVA, BUFFALO, ANTELOPE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31521-02.htm"))
		{
			if (player.getStatus().getLevel() >= 71)
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
				htmltext = "31521-03.htm";
		}
		else if (event.equalsIgnoreCase("31521-05.htm"))
		{
			takeItems(player, LEAF_OF_FLAVA, -1);
			takeItems(player, BUFFALO_MEAT, -1);
			takeItems(player, ANTELOPE_HORN, -1);
			
			int luck = Rnd.get(100);
			if (luck < 11)
			{
				rewardItems(player, 57, 25000);
				giveItems(player, 6849, 1);
			}
			else if (luck < 23)
			{
				rewardItems(player, 57, 65000);
				giveItems(player, 6847, 1);
			}
			else if (luck < 33)
			{
				rewardItems(player, 57, 25000);
				giveItems(player, 6851, 1);
			}
			else
			{
				rewardItems(player, 57, 73000);
				rewardExpAndSp(player, 230000, 18250);
			}
			
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
				htmltext = "31521-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "31521-06.htm";
				else if (cond == 2)
				{
					if (player.getInventory().getItemCount(LEAF_OF_FLAVA) >= 100 && player.getInventory().getItemCount(BUFFALO_MEAT) >= 100 && player.getInventory().getItemCount(ANTELOPE_HORN) >= 100)
						htmltext = "31521-04.htm";
					else
						htmltext = "31521-07.htm";
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMember(player, npc, 1);
		if (st == null)
			return;
		
		player = st.getPlayer();
		switch (npc.getNpcId())
		{
			case FLAVA:
				if (dropItemsAlways(player, LEAF_OF_FLAVA, 1, 100) && player.getInventory().getItemCount(BUFFALO_MEAT) >= 100 && player.getInventory().getItemCount(ANTELOPE_HORN) >= 100)
					st.setCond(2);
				break;
			
			case BUFFALO:
				if (dropItemsAlways(player, BUFFALO_MEAT, 1, 100) && player.getInventory().getItemCount(LEAF_OF_FLAVA) >= 100 && player.getInventory().getItemCount(ANTELOPE_HORN) >= 100)
					st.setCond(2);
				break;
			
			case ANTELOPE:
				if (dropItemsAlways(player, ANTELOPE_HORN, 1, 100) && player.getInventory().getItemCount(LEAF_OF_FLAVA) >= 100 && player.getInventory().getItemCount(BUFFALO_MEAT) >= 100)
					st.setCond(2);
				break;
		}
	}
}