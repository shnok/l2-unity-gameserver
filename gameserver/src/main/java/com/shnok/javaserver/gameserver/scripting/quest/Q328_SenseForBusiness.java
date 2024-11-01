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

public class Q328_SenseForBusiness extends Quest
{
	private static final String QUEST_NAME = "Q328_SenseForBusiness";
	
	// Items
	private static final int MONSTER_EYE_LENS = 1366;
	private static final int MONSTER_EYE_CARCASS = 1347;
	private static final int BASILISK_GIZZARD = 1348;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(20055, 48);
		CHANCES.put(20059, 52);
		CHANCES.put(20067, 68);
		CHANCES.put(20068, 76);
		CHANCES.put(20070, 500000);
		CHANCES.put(20072, 510000);
	}
	
	public Q328_SenseForBusiness()
	{
		super(328, "Sense for Business");
		
		setItemsIds(MONSTER_EYE_LENS, MONSTER_EYE_CARCASS, BASILISK_GIZZARD);
		
		addQuestStart(30436); // Sarien
		addTalkId(30436);
		
		addMyDying(20055, 20059, 20067, 20068, 20070, 20072);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30436-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30436-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				htmltext = (player.getStatus().getLevel() < 21) ? "30436-01.htm" : "30436-02.htm";
				break;
			
			case STARTED:
				final int carcasses = player.getInventory().getItemCount(MONSTER_EYE_CARCASS);
				final int lenses = player.getInventory().getItemCount(MONSTER_EYE_LENS);
				final int gizzards = player.getInventory().getItemCount(BASILISK_GIZZARD);
				
				final int all = carcasses + lenses + gizzards;
				
				if (all == 0)
					htmltext = "30436-04.htm";
				else
				{
					htmltext = "30436-05.htm";
					takeItems(player, MONSTER_EYE_CARCASS, -1);
					takeItems(player, MONSTER_EYE_LENS, -1);
					takeItems(player, BASILISK_GIZZARD, -1);
					rewardItems(player, 57, (25 * carcasses) + (1000 * lenses) + (60 * gizzards) + ((all >= 10) ? 618 : 0));
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
		final int chance = CHANCES.get(npcId);
		
		if (npcId < 20069)
		{
			final int rnd = Rnd.get(100);
			if (rnd < (chance + 1))
				dropItemsAlways(player, (rnd < chance) ? MONSTER_EYE_CARCASS : MONSTER_EYE_LENS, 1, 0);
		}
		else
			dropItems(player, BASILISK_GIZZARD, 1, 0, chance);
	}
}