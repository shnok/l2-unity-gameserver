package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q639_GuardiansOfTheHolyGrail extends Quest
{
	private static final String QUEST_NAME = "Q639_GuardiansOfTheHolyGrail";
	
	// NPCs
	private static final int DOMINIC = 31350;
	private static final int GREMORY = 32008;
	private static final int HOLY_GRAIL = 32028;
	
	// Items
	private static final int SCRIPTURE = 8069;
	private static final int WATER_BOTTLE = 8070;
	private static final int HOLY_WATER_BOTTLE = 8071;
	
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(22122, 760000);
		CHANCES.put(22123, 750000);
		CHANCES.put(22124, 590000);
		CHANCES.put(22125, 580000);
		CHANCES.put(22126, 590000);
		CHANCES.put(22127, 580000);
		CHANCES.put(22128, 170000);
		CHANCES.put(22129, 590000);
		CHANCES.put(22130, 850000);
		CHANCES.put(22131, 920000);
		CHANCES.put(22132, 580000);
		CHANCES.put(22133, 930000);
		CHANCES.put(22134, 230000);
		CHANCES.put(22135, 580000);
	}
	
	public Q639_GuardiansOfTheHolyGrail()
	{
		super(639, "Guardians of the Holy Grail");
		
		setItemsIds(SCRIPTURE, WATER_BOTTLE, HOLY_WATER_BOTTLE);
		
		addQuestStart(DOMINIC);
		addTalkId(DOMINIC, GREMORY, HOLY_GRAIL);
		
		for (int id : CHANCES.keySet())
			addMyDying(id);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// DOMINIC
		if (event.equalsIgnoreCase("31350-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31350-08.htm"))
		{
			final int count = player.getInventory().getItemCount(SCRIPTURE);
			
			takeItems(player, SCRIPTURE, -1);
			rewardItems(player, 57, 1625 * count + ((count >= 10) ? 33940 : 0));
		}
		else if (event.equalsIgnoreCase("31350-09.htm"))
		{
			playSound(player, SOUND_GIVEUP);
			st.exitQuest(true);
		}
		// GREMORY
		else if (event.equalsIgnoreCase("32008-05.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, WATER_BOTTLE, 1);
		}
		else if (event.equalsIgnoreCase("32008-09.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HOLY_WATER_BOTTLE, 1);
		}
		else if (event.equalsIgnoreCase("32008-12.htm"))
		{
			if (player.getInventory().getItemCount(SCRIPTURE) >= 4000)
			{
				htmltext = "32008-11.htm";
				takeItems(player, SCRIPTURE, 4000);
				rewardItems(player, 959, 1);
			}
		}
		else if (event.equalsIgnoreCase("32008-14.htm"))
		{
			if (player.getInventory().getItemCount(SCRIPTURE) >= 400)
			{
				htmltext = "32008-13.htm";
				takeItems(player, SCRIPTURE, 400);
				rewardItems(player, 960, 1);
			}
		}
		// HOLY GRAIL
		else if (event.equalsIgnoreCase("32028-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, WATER_BOTTLE, 1);
			giveItems(player, HOLY_WATER_BOTTLE, 1);
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
				htmltext = (player.getStatus().getLevel() < 73) ? "31350-02.htm" : "31350-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case DOMINIC:
						htmltext = (player.getInventory().hasItem(SCRIPTURE)) ? "31350-05.htm" : "31350-06.htm";
						break;
					
					case GREMORY:
						if (cond == 1)
							htmltext = "32008-01.htm";
						else if (cond == 2)
							htmltext = "32008-06.htm";
						else if (cond == 3)
							htmltext = "32008-08.htm";
						else if (cond == 4)
							htmltext = "32008-10.htm";
						break;
					
					case HOLY_GRAIL:
						if (cond == 2)
							htmltext = "32028-01.htm";
						else if (cond > 2)
							htmltext = "32028-03.htm";
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
		
		dropItems(st.getPlayer(), SCRIPTURE, 1, 0, CHANCES.get(npc.getNpcId()));
	}
}