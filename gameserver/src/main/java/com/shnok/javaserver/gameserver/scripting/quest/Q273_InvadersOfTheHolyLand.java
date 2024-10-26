package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q273_InvadersOfTheHolyLand extends Quest
{
	private static final String QUEST_NAME = "Q273_InvadersOfTheHolyLand";
	
	// Items
	private static final int BLACK_SOULSTONE = 1475;
	private static final int RED_SOULSTONE = 1476;
	
	public Q273_InvadersOfTheHolyLand()
	{
		super(273, "Invaders of the Holy Land");
		
		setItemsIds(BLACK_SOULSTONE, RED_SOULSTONE);
		
		addQuestStart(30566); // Varkees
		addTalkId(30566);
		
		addMyDying(20311, 20312, 20313);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30566-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30566-07.htm"))
		{
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
				if (player.getRace() != ClassRace.ORC)
					htmltext = "30566-00.htm";
				else if (player.getStatus().getLevel() < 6)
					htmltext = "30566-01.htm";
				else
					htmltext = "30566-02.htm";
				break;
			
			case STARTED:
				final int red = player.getInventory().getItemCount(RED_SOULSTONE);
				final int black = player.getInventory().getItemCount(BLACK_SOULSTONE);
				
				if (red + black == 0)
					htmltext = "30566-04.htm";
				else
				{
					htmltext = (red == 0) ? "30566-05.htm" : "30566-06.htm";
					takeItems(player, BLACK_SOULSTONE, -1);
					takeItems(player, RED_SOULSTONE, -1);
					
					int reward = (black * 3) + (red * 10) + ((black >= 10) ? ((red >= 1) ? 1800 : 1500) : 0);
					
					rewardItems(player, 57, reward);
					rewardNewbieShots(player, 6000, 0);
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
		
		int probability = 77;
		if (npcId == 20311)
			probability = 90;
		else if (npcId == 20312)
			probability = 87;
		
		if (Rnd.get(100) <= probability)
			dropItemsAlways(player, BLACK_SOULSTONE, 1, 0);
		else
			dropItemsAlways(player, RED_SOULSTONE, 1, 0);
	}
}