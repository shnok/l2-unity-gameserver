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

public class Q162_CurseOfTheUndergroundFortress extends Quest
{
	private static final String QUEST_NAME = "Q162_CurseOfTheUndergroundFortress";
	
	// Monsters
	private static final int SHADE_HORROR = 20033;
	private static final int DARK_TERROR = 20345;
	private static final int MIST_TERROR = 20371;
	private static final int DUNGEON_SKELETON_ARCHER = 20463;
	private static final int DUNGEON_SKELETON = 20464;
	private static final int DREAD_SOLDIER = 20504;
	
	// Items
	private static final int BONE_FRAGMENT = 1158;
	private static final int ELF_SKULL = 1159;
	
	// Rewards
	private static final int BONE_SHIELD = 625;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(6);
	
	public Q162_CurseOfTheUndergroundFortress()
	{
		super(162, "Curse of the Underground Fortress");
		
		CHANCES.put(SHADE_HORROR, 250000);
		CHANCES.put(DARK_TERROR, 260000);
		CHANCES.put(MIST_TERROR, 230000);
		CHANCES.put(DUNGEON_SKELETON_ARCHER, 250000);
		CHANCES.put(DUNGEON_SKELETON, 230000);
		CHANCES.put(DREAD_SOLDIER, 260000);
		
		setItemsIds(BONE_FRAGMENT, ELF_SKULL);
		
		addQuestStart(30147); // Unoren
		addTalkId(30147);
		
		addMyDying(SHADE_HORROR, DARK_TERROR, MIST_TERROR, DUNGEON_SKELETON_ARCHER, DUNGEON_SKELETON, DREAD_SOLDIER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30147-04.htm"))
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
				if (player.getRace() == ClassRace.DARK_ELF)
					htmltext = "30147-00.htm";
				else if (player.getStatus().getLevel() < 12)
					htmltext = "30147-01.htm";
				else
					htmltext = "30147-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				if (cond == 1)
					htmltext = "30147-05.htm";
				else if (cond == 2)
				{
					htmltext = "30147-06.htm";
					takeItems(player, ELF_SKULL, -1);
					takeItems(player, BONE_FRAGMENT, -1);
					giveItems(player, BONE_SHIELD, 1);
					rewardItems(player, 57, 24000);
					playSound(player, SOUND_FINISH);
					st.exitQuest(false);
				}
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
		
		final int npcId = npc.getNpcId();
		
		switch (npcId)
		{
			case DUNGEON_SKELETON, DUNGEON_SKELETON_ARCHER, DREAD_SOLDIER:
				if (dropItems(player, BONE_FRAGMENT, 1, 10, CHANCES.get(npcId)) && player.getInventory().getItemCount(ELF_SKULL) >= 3)
					st.setCond(2);
				break;
			
			case SHADE_HORROR, DARK_TERROR, MIST_TERROR:
				if (dropItems(player, ELF_SKULL, 1, 3, CHANCES.get(npcId)) && player.getInventory().getItemCount(BONE_FRAGMENT) >= 10)
					st.setCond(2);
				break;
		}
	}
}