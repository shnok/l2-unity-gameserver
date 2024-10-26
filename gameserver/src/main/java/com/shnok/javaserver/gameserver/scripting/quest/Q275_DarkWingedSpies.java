package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q275_DarkWingedSpies extends Quest
{
	private static final String QUEST_NAME = "Q275_DarkWingedSpies";
	
	// Monsters
	private static final int DARKWING_BAT = 20316;
	private static final int VARANGKA_TRACKER = 27043;
	
	// Items
	private static final int DARKWING_BAT_FANG = 1478;
	private static final int VARANGKA_PARASITE = 1479;
	
	public Q275_DarkWingedSpies()
	{
		super(275, "Dark Winged Spies");
		
		setItemsIds(DARKWING_BAT_FANG, VARANGKA_PARASITE);
		
		addQuestStart(30567); // Tantus
		addTalkId(30567);
		
		addMyDying(DARKWING_BAT, VARANGKA_TRACKER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30567-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
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
				if (player.getRace() != ClassRace.ORC)
					htmltext = "30567-00.htm";
				else if (player.getStatus().getLevel() < 11)
					htmltext = "30567-01.htm";
				else
					htmltext = "30567-02.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 1)
					htmltext = "30567-04.htm";
				else
				{
					htmltext = "30567-05.htm";
					takeItems(player, DARKWING_BAT_FANG, -1);
					takeItems(player, VARANGKA_PARASITE, -1);
					rewardItems(player, 57, 4200);
					playSound(player, SOUND_FINISH);
					st.exitQuest(true);
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case DARKWING_BAT:
				if (dropItemsAlways(player, DARKWING_BAT_FANG, 1, 70))
					st.setCond(2);
				else if (Rnd.get(100) < 10 && player.getInventory().getItemCount(DARKWING_BAT_FANG) > 10 && player.getInventory().getItemCount(DARKWING_BAT_FANG) < 66)
				{
					// Spawn of Varangka Tracker on the npc position.
					addSpawn(VARANGKA_TRACKER, npc, true, 0, true);
					
					giveItems(player, VARANGKA_PARASITE, 1);
				}
				break;
			
			case VARANGKA_TRACKER:
				if (player.getInventory().hasItem(VARANGKA_PARASITE))
				{
					takeItems(player, VARANGKA_PARASITE, -1);
					
					if (dropItemsAlways(player, DARKWING_BAT_FANG, 5, 70))
						st.setCond(2);
				}
				break;
		}
	}
}