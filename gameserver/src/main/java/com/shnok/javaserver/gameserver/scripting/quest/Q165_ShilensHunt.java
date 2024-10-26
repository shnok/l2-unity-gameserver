package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q165_ShilensHunt extends Quest
{
	private static final String QUEST_NAME = "Q165_ShilensHunt";
	
	// Monsters
	private static final int ASHEN_WOLF = 20456;
	private static final int YOUNG_BROWN_KELTIR = 20529;
	private static final int BROWN_KELTIR = 20532;
	private static final int ELDER_BROWN_KELTIR = 20536;
	
	// Items
	private static final int DARK_BEZOAR = 1160;
	private static final int LESSER_HEALING_POTION = 1060;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	{
		CHANCES.put(ASHEN_WOLF, 1000000);
		CHANCES.put(YOUNG_BROWN_KELTIR, 333333);
		CHANCES.put(BROWN_KELTIR, 333333);
		CHANCES.put(ELDER_BROWN_KELTIR, 666667);
	}
	
	public Q165_ShilensHunt()
	{
		super(165, "Shilen's Hunt");
		
		setItemsIds(DARK_BEZOAR);
		
		addQuestStart(30348); // Nelsya
		addTalkId(30348);
		
		addMyDying(ASHEN_WOLF, YOUNG_BROWN_KELTIR, BROWN_KELTIR, ELDER_BROWN_KELTIR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30348-03.htm"))
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
				if (player.getRace() != ClassRace.DARK_ELF)
					htmltext = "30348-00.htm";
				else if (player.getStatus().getLevel() < 3)
					htmltext = "30348-01.htm";
				else
					htmltext = "30348-02.htm";
				break;
			
			case STARTED:
				if (player.getInventory().getItemCount(DARK_BEZOAR) >= 13)
				{
					htmltext = "30348-05.htm";
					takeItems(player, DARK_BEZOAR, -1);
					rewardItems(player, LESSER_HEALING_POTION, 5);
					rewardExpAndSp(player, 1000, 0);
					playSound(player, SOUND_FINISH);
					st.exitQuest(false);
				}
				else
					htmltext = "30348-04.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(player, DARK_BEZOAR, 1, 13, CHANCES.get(npc.getNpcId())))
			st.setCond(2);
	}
}