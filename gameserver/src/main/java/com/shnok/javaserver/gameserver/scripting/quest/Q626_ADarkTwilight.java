package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q626_ADarkTwilight extends Quest
{
	private static final String QUEST_NAME = "Q626_ADarkTwilight";
	
	// Items
	private static final int BLOOD_OF_SAINT = 7169;
	
	// NPC
	private static final int HIERARCH = 31517;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(14);
	
	public Q626_ADarkTwilight()
	{
		super(626, "A Dark Twilight");
		
		CHANCES.put(21520, 533000);
		CHANCES.put(21523, 566000);
		CHANCES.put(21524, 603000);
		CHANCES.put(21525, 603000);
		CHANCES.put(21526, 587000);
		CHANCES.put(21529, 606000);
		CHANCES.put(21530, 560000);
		CHANCES.put(21531, 669000);
		CHANCES.put(21532, 651000);
		CHANCES.put(21535, 672000);
		CHANCES.put(21536, 597000);
		CHANCES.put(21539, 739000);
		CHANCES.put(21540, 739000);
		CHANCES.put(21658, 669000);
		
		setItemsIds(BLOOD_OF_SAINT);
		
		addQuestStart(HIERARCH);
		addTalkId(HIERARCH);
		
		for (int npcId : CHANCES.keySet())
			addMyDying(npcId);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31517-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("reward1"))
		{
			if (player.getInventory().getItemCount(BLOOD_OF_SAINT) == 300)
			{
				htmltext = "31517-07.htm";
				takeItems(player, BLOOD_OF_SAINT, 300);
				rewardExpAndSp(player, 162773, 12500);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "31517-08.htm";
		}
		else if (event.equalsIgnoreCase("reward2"))
		{
			if (player.getInventory().getItemCount(BLOOD_OF_SAINT) == 300)
			{
				htmltext = "31517-07.htm";
				takeItems(player, BLOOD_OF_SAINT, 300);
				rewardItems(player, 57, 100000);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "31517-08.htm";
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
				htmltext = (player.getStatus().getLevel() < 60) ? "31517-02.htm" : "31517-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "31517-05.htm";
				else
					htmltext = "31517-04.htm";
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMember(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(st.getPlayer(), BLOOD_OF_SAINT, 1, 300, CHANCES.get(npc.getNpcId())))
			st.setCond(2);
	}
}