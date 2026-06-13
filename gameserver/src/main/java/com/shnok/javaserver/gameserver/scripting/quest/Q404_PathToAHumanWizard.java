package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q404_PathToAHumanWizard extends Quest
{
	private static final String QUEST_NAME = "Q404_PathToAHumanWizard";
	
	// Items
	private static final int MAP_OF_LUSTER = 1280;
	private static final int KEY_OF_FLAME = 1281;
	private static final int FLAME_EARING = 1282;
	private static final int BROKEN_BRONZE_MIRROR = 1283;
	private static final int WIND_FEATHER = 1284;
	private static final int WIND_BANGEL = 1285;
	private static final int RAMA_DIARY = 1286;
	private static final int SPARKLE_PEBBLE = 1287;
	private static final int WATER_NECKLACE = 1288;
	private static final int RUST_GOLD_COIN = 1289;
	private static final int RED_SOIL = 1290;
	private static final int EARTH_RING = 1291;
	private static final int BEAD_OF_SEASON = 1292;
	
	// NPCs
	private static final int PARINA = 30391;
	private static final int EARTH_SNAKE = 30409;
	private static final int WASTELAND_LIZARDMAN = 30410;
	private static final int FLAME_SALAMANDER = 30411;
	private static final int WIND_SYLPH = 30412;
	private static final int WATER_UNDINE = 30413;
	
	public Q404_PathToAHumanWizard()
	{
		super(404, "Path to a Human Wizard");
		
		setItemsIds(MAP_OF_LUSTER, KEY_OF_FLAME, FLAME_EARING, BROKEN_BRONZE_MIRROR, WIND_FEATHER, WIND_BANGEL, RAMA_DIARY, SPARKLE_PEBBLE, WATER_NECKLACE, RUST_GOLD_COIN, RED_SOIL, EARTH_RING);
		
		addQuestStart(PARINA);
		addTalkId(PARINA, EARTH_SNAKE, WASTELAND_LIZARDMAN, FLAME_SALAMANDER, WIND_SYLPH, WATER_UNDINE);
		
		addMyDying(20021, 20359, 27030);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30391-08.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30410-03.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BROKEN_BRONZE_MIRROR, 1);
			giveItems(player, WIND_FEATHER, 1);
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
		
		final int cond = st.getCond();
		switch (st.getState())
		{
			case CREATED:
				if (player.getClassId() != ClassId.HUMAN_MYSTIC)
					htmltext = (player.getClassId() == ClassId.HUMAN_WIZARD) ? "30391-02a.htm" : "30391-01.htm";
				else if (player.getStatus().getLevel() < 19)
					htmltext = "30391-02.htm";
				else if (player.getInventory().hasItem(BEAD_OF_SEASON))
					htmltext = "30391-03.htm";
				else
					htmltext = "30391-04.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case PARINA:
						if (cond < 13)
							htmltext = "30391-05.htm";
						else if (cond == 13)
						{
							htmltext = "30391-06.htm";
							takeItems(player, EARTH_RING, 1);
							takeItems(player, FLAME_EARING, 1);
							takeItems(player, WATER_NECKLACE, 1);
							takeItems(player, WIND_BANGEL, 1);
							giveItems(player, BEAD_OF_SEASON, 1);
							rewardExpAndSp(player, 3200, 2020);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case FLAME_SALAMANDER:
						if (cond == 1)
						{
							htmltext = "30411-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, MAP_OF_LUSTER, 1);
						}
						else if (cond == 2)
							htmltext = "30411-02.htm";
						else if (cond == 3)
						{
							htmltext = "30411-03.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, KEY_OF_FLAME, 1);
							takeItems(player, MAP_OF_LUSTER, 1);
							giveItems(player, FLAME_EARING, 1);
						}
						else if (cond > 3)
							htmltext = "30411-04.htm";
						break;
					
					case WIND_SYLPH:
						if (cond == 4)
						{
							htmltext = "30412-01.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, BROKEN_BRONZE_MIRROR, 1);
						}
						else if (cond == 5)
							htmltext = "30412-02.htm";
						else if (cond == 6)
						{
							htmltext = "30412-03.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, WIND_FEATHER, 1);
							giveItems(player, WIND_BANGEL, 1);
						}
						else if (cond > 6)
							htmltext = "30412-04.htm";
						break;
					
					case WASTELAND_LIZARDMAN:
						if (cond == 5)
							htmltext = "30410-01.htm";
						else if (cond > 5)
							htmltext = "30410-04.htm";
						break;
					
					case WATER_UNDINE:
						if (cond == 7)
						{
							htmltext = "30413-01.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, RAMA_DIARY, 1);
						}
						else if (cond == 8)
							htmltext = "30413-02.htm";
						else if (cond == 9)
						{
							htmltext = "30413-03.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, RAMA_DIARY, 1);
							takeItems(player, SPARKLE_PEBBLE, -1);
							giveItems(player, WATER_NECKLACE, 1);
						}
						else if (cond > 9)
							htmltext = "30413-04.htm";
						break;
					
					case EARTH_SNAKE:
						if (cond == 10)
						{
							htmltext = "30409-01.htm";
							st.setCond(11);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, RUST_GOLD_COIN, 1);
						}
						else if (cond == 11)
							htmltext = "30409-02.htm";
						else if (cond == 12)
						{
							htmltext = "30409-03.htm";
							st.setCond(13);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, RED_SOIL, 1);
							takeItems(player, RUST_GOLD_COIN, 1);
							giveItems(player, EARTH_RING, 1);
						}
						else if (cond > 12)
							htmltext = "30409-04.htm";
						break;
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
		
		switch (npc.getNpcId())
		{
			case 20359: // Ratman Warrior
				if (st.getCond() == 2 && dropItems(player, KEY_OF_FLAME, 1, 1, 800000))
					st.setCond(3);
				break;
			
			case 27030: // Water Seer
				if (st.getCond() == 8 && dropItems(player, SPARKLE_PEBBLE, 1, 2, 800000))
					st.setCond(9);
				break;
			
			case 20021: // Red Bear
				if (st.getCond() == 11 && dropItems(player, RED_SOIL, 1, 1, 200000))
					st.setCond(12);
				break;
		}
	}
}