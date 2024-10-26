package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q266_PleasOfPixies extends Quest
{
	private static final String QUEST_NAME = "Q266_PleasOfPixies";
	
	// Items
	private static final int PREDATOR_FANG = 1334;
	
	// Rewards
	private static final int GLASS_SHARD = 1336;
	private static final int EMERALD = 1337;
	private static final int BLUE_ONYX = 1338;
	private static final int ONYX = 1339;
	
	public Q266_PleasOfPixies()
	{
		super(266, "Pleas of Pixies");
		
		setItemsIds(PREDATOR_FANG);
		
		addQuestStart(31852); // Murika
		addTalkId(31852);
		
		addMyDying(20525, 20530, 20534, 20537);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31852-03.htm"))
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
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getRace() != ClassRace.ELF)
					htmltext = "31852-00.htm";
				else if (player.getStatus().getLevel() < 3)
					htmltext = "31852-01.htm";
				else
					htmltext = "31852-02.htm";
				break;
			
			case STARTED:
				if (player.getInventory().getItemCount(PREDATOR_FANG) < 100)
					htmltext = "31852-04.htm";
				else
				{
					htmltext = "31852-05.htm";
					takeItems(player, PREDATOR_FANG, -1);
					
					final int n = Rnd.get(100);
					if (n < 10)
					{
						playSound(player, SOUND_JACKPOT);
						rewardItems(player, EMERALD, 1);
					}
					else if (n < 30)
						rewardItems(player, BLUE_ONYX, 1);
					else if (n < 60)
						rewardItems(player, ONYX, 1);
					else
						rewardItems(player, GLASS_SHARD, 1);
					
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
			case 20525:
				if (dropItemsAlways(player, PREDATOR_FANG, Rnd.get(2, 3), 100))
					st.setCond(2);
				break;
			
			case 20530:
				if (dropItems(player, PREDATOR_FANG, 1, 100, 800000))
					st.setCond(2);
				break;
			
			case 20534:
				if (dropItems(player, PREDATOR_FANG, (Rnd.get(3) == 0) ? 1 : 2, 100, 600000))
					st.setCond(2);
				break;
			
			case 20537:
				if (dropItemsAlways(player, PREDATOR_FANG, 2, 100))
					st.setCond(2);
				break;
		}
	}
}