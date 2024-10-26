package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q329_CuriosityOfADwarf extends Quest
{
	private static final String QUEST_NAME = "Q329_CuriosityOfADwarf";
	
	// Items
	private static final int GOLEM_HEARTSTONE = 1346;
	private static final int BROKEN_HEARTSTONE = 1365;
	
	public Q329_CuriosityOfADwarf()
	{
		super(329, "Curiosity of a Dwarf");
		
		addQuestStart(30437); // Rolento
		addTalkId(30437);
		
		addMyDying(20083, 20085); // Granite golem, Puncher
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30437-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30437-06.htm"))
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
				htmltext = (player.getStatus().getLevel() < 33) ? "30437-01.htm" : "30437-02.htm";
				break;
			
			case STARTED:
				final int golem = player.getInventory().getItemCount(GOLEM_HEARTSTONE);
				final int broken = player.getInventory().getItemCount(BROKEN_HEARTSTONE);
				
				if (golem + broken == 0)
					htmltext = "30437-04.htm";
				else
				{
					htmltext = "30437-05.htm";
					takeItems(player, GOLEM_HEARTSTONE, -1);
					takeItems(player, BROKEN_HEARTSTONE, -1);
					rewardItems(player, 57, broken * 50 + golem * 1000 + ((golem + broken > 10) ? 1183 : 0));
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
		
		final int chance = Rnd.get(100);
		if (chance < 2)
			dropItemsAlways(player, GOLEM_HEARTSTONE, 1, 0);
		else if (chance < ((npc.getNpcId() == 20083) ? 44 : 50))
			dropItemsAlways(player, BROKEN_HEARTSTONE, 1, 0);
	}
}