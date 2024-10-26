package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q375_WhisperOfDreams_Part2 extends Quest
{
	private static final String QUEST_NAME = "Q375_WhisperOfDreams_Part2";
	
	// NPCs
	private static final int MANAKIA = 30515;
	
	// Monsters
	private static final int KARIK = 20629;
	private static final int CAVE_HOWLER = 20624;
	
	// Items
	private static final int MYSTERIOUS_STONE = 5887;
	private static final int KARIK_HORN = 5888;
	private static final int CAVE_HOWLER_SKULL = 5889;
	
	// Rewards : A grade robe recipes
	private static final int[] REWARDS =
	{
		5348,
		5350,
		5352
	};
	
	public Q375_WhisperOfDreams_Part2()
	{
		super(375, "Whisper of Dreams, Part 2");
		
		setItemsIds(KARIK_HORN, CAVE_HOWLER_SKULL);
		
		addQuestStart(MANAKIA);
		addTalkId(MANAKIA);
		
		addMyDying(KARIK, CAVE_HOWLER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Manakia
		if (event.equalsIgnoreCase("30515-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			takeItems(player, MYSTERIOUS_STONE, 1);
		}
		else if (event.equalsIgnoreCase("30515-07.htm"))
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
				htmltext = (!player.getInventory().hasItem(MYSTERIOUS_STONE) || player.getStatus().getLevel() < 60) ? "30515-01.htm" : "30515-02.htm";
				break;
			
			case STARTED:
				if (player.getInventory().getItemCount(KARIK_HORN) >= 100 && player.getInventory().getItemCount(CAVE_HOWLER_SKULL) >= 100)
				{
					htmltext = "30515-05.htm";
					playSound(player, SOUND_MIDDLE);
					takeItems(player, KARIK_HORN, 100);
					takeItems(player, CAVE_HOWLER_SKULL, 100);
					giveItems(player, Rnd.get(REWARDS), 1);
				}
				else
					htmltext = "30515-04.htm";
				break;
		}
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		// Drop horn or skull to anyone.
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case KARIK:
				dropItemsAlways(st.getPlayer(), KARIK_HORN, 1, 100);
				break;
			
			case CAVE_HOWLER:
				dropItems(st.getPlayer(), CAVE_HOWLER_SKULL, 1, 100, 900000);
				break;
		}
	}
}