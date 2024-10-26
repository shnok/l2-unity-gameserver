package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q371_ShriekOfGhosts extends Quest
{
	private static final String QUEST_NAME = "Q371_ShriekOfGhosts";
	
	// NPCs
	private static final int REVA = 30867;
	private static final int PATRIN = 30929;
	
	// Item
	private static final int URN = 5903;
	private static final int PORCELAIN = 6002;
	
	// Drop chances
	private static final Map<Integer, int[]> CHANCES = HashMap.newHashMap(3);
	
	public Q371_ShriekOfGhosts()
	{
		super(371, "Shriek of Ghosts");
		
		CHANCES.put(20818, new int[]
		{
			38,
			43
		});
		CHANCES.put(20820, new int[]
		{
			48,
			56
		});
		CHANCES.put(20824, new int[]
		{
			50,
			58
		});
		
		setItemsIds(URN, PORCELAIN);
		
		addQuestStart(REVA);
		addTalkId(REVA, PATRIN);
		
		addMyDying(20818, 20820, 20824);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30867-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30867-07.htm"))
		{
			int urns = player.getInventory().getItemCount(URN);
			if (urns > 0)
			{
				takeItems(player, URN, urns);
				if (urns >= 100)
				{
					urns += 13;
					htmltext = "30867-08.htm";
				}
				else
					urns += 7;
				rewardItems(player, 57, urns * 1000);
			}
		}
		else if (event.equalsIgnoreCase("30867-10.htm"))
		{
			playSound(player, SOUND_GIVEUP);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("APPR"))
		{
			if (player.getInventory().hasItem(PORCELAIN))
			{
				int chance = Rnd.get(100);
				
				takeItems(player, PORCELAIN, 1);
				
				if (chance < 2)
				{
					giveItems(player, 6003, 1);
					htmltext = "30929-03.htm";
				}
				else if (chance < 32)
				{
					giveItems(player, 6004, 1);
					htmltext = "30929-04.htm";
				}
				else if (chance < 62)
				{
					giveItems(player, 6005, 1);
					htmltext = "30929-05.htm";
				}
				else if (chance < 77)
				{
					giveItems(player, 6006, 1);
					htmltext = "30929-06.htm";
				}
				else
					htmltext = "30929-07.htm";
			}
			else
				htmltext = "30929-02.htm";
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
				htmltext = (player.getStatus().getLevel() < 59) ? "30867-01.htm" : "30867-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case REVA:
						if (player.getInventory().hasItem(URN))
							htmltext = (player.getInventory().hasItem(PORCELAIN)) ? "30867-05.htm" : "30867-04.htm";
						else
							htmltext = "30867-06.htm";
						break;
					
					case PATRIN:
						htmltext = "30929-01.htm";
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
		
		final int[] chances = CHANCES.get(npc.getNpcId());
		final int random = Rnd.get(100);
		
		if (random < chances[1])
			dropItemsAlways(st.getPlayer(), (random < chances[0]) ? URN : PORCELAIN, 1, 0);
	}
}