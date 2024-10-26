package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q432_BirthdayPartySong extends Quest
{
	private static final String QUEST_NAME = "Q432_BirthdayPartySong";
	
	// NPC
	private static final int OCTAVIA = 31043;
	
	// Item
	private static final int RED_CRYSTAL = 7541;
	
	public Q432_BirthdayPartySong()
	{
		super(432, "Birthday Party Song");
		
		setItemsIds(RED_CRYSTAL);
		
		addQuestStart(OCTAVIA);
		addTalkId(OCTAVIA);
		
		addMyDying(21103);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31043-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31043-06.htm"))
		{
			if (player.getInventory().getItemCount(RED_CRYSTAL) == 50)
			{
				htmltext = "31043-05.htm";
				takeItems(player, RED_CRYSTAL, -1);
				rewardItems(player, 7061, 25);
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
				htmltext = (player.getStatus().getLevel() < 31) ? "31043-00.htm" : "31043-01.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().getItemCount(RED_CRYSTAL) < 50) ? "31043-03.htm" : "31043-04.htm";
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
		
		if (dropItems(st.getPlayer(), RED_CRYSTAL, 1, 50, 500000))
			st.setCond(2);
	}
}