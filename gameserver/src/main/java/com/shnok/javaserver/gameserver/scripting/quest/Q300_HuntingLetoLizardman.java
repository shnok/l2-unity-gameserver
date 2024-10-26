package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q300_HuntingLetoLizardman extends Quest
{
	private static final String QUEST_NAME = "Q300_HuntingLetoLizardman";
	
	// Item
	private static final int BRACELET = 7139;
	
	// Monsters
	private static final int LETO_LIZARDMAN = 20577;
	private static final int LETO_LIZARDMAN_ARCHER = 20578;
	private static final int LETO_LIZARDMAN_SOLDIER = 20579;
	private static final int LETO_LIZARDMAN_WARRIOR = 20580;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(LETO_LIZARDMAN, 300000);
		CHANCES.put(LETO_LIZARDMAN_ARCHER, 320000);
		CHANCES.put(LETO_LIZARDMAN_SOLDIER, 350000);
		CHANCES.put(LETO_LIZARDMAN_WARRIOR, 650000);
		CHANCES.put(LETO_LIZARDMAN_OVERLORD, 700000);
	}
	
	public Q300_HuntingLetoLizardman()
	{
		super(300, "Hunting Leto Lizardman");
		
		setItemsIds(BRACELET);
		
		addQuestStart(30126); // Rath
		addTalkId(30126);
		
		addMyDying(LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER, LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR, LETO_LIZARDMAN_OVERLORD);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30126-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30126-05.htm"))
		{
			if (player.getInventory().getItemCount(BRACELET) >= 60)
			{
				htmltext = "30126-06.htm";
				takeItems(player, BRACELET, -1);
				
				final int luck = Rnd.get(3);
				if (luck == 0)
					rewardItems(player, 57, 30000);
				else if (luck == 1)
					rewardItems(player, 1867, 50);
				else if (luck == 2)
					rewardItems(player, 1872, 50);
				
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
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
				htmltext = (player.getStatus().getLevel() < 34) ? "30126-01.htm" : "30126-02.htm";
				break;
			
			case STARTED:
				htmltext = (st.getCond() == 1) ? "30126-04a.htm" : "30126-04.htm";
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
		
		if (dropItems(st.getPlayer(), BRACELET, 1, 60, CHANCES.get(npc.getNpcId())))
			st.setCond(2);
	}
}