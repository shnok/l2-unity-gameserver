package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q211_TrialOfTheChallenger extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q211_TrialOfTheChallenger";
	
	private static final Location MITHRIL_MINES_LOC = new Location(176560, -184969, -3729);
	
	// Items
	private static final int LETTER_OF_KASH = 2628;
	private static final int WATCHER_EYE_1 = 2629;
	private static final int WATCHER_EYE_2 = 2630;
	private static final int SCROLL_OF_SHYSLASSYS = 2631;
	private static final int BROKEN_KEY = 2632;
	
	// Rewards
	private static final int ADENA = 57;
	private static final int ELVEN_NECKLACE_BEADS = 1904;
	private static final int WHITE_TUNIC_PATTERN = 1936;
	private static final int IRON_BOOTS_DESIGN = 1940;
	private static final int MANTICOR_SKIN_GAITERS_PATTERN = 1943;
	private static final int RIP_GAUNTLETS_PATTERN = 1946;
	private static final int TOME_OF_BLOOD_PAGE = 2030;
	private static final int MITHRIL_SCALE_GAITERS_MATERIAL = 2918;
	private static final int BRIGANDINE_GAUNTLETS_PATTERN = 2927;
	private static final int MARK_OF_CHALLENGER = 2627;
	
	// NPCs
	private static final int FILAUR = 30535;
	private static final int KASH = 30644;
	private static final int MARTIEN = 30645;
	private static final int RALDO = 30646;
	private static final int CHEST_OF_SHYSLASSYS = 30647;
	
	// Monsters
	private static final int SHYSLASSYS = 27110;
	private static final int GORR = 27112;
	private static final int BARAHAM = 27113;
	private static final int SUCCUBUS_QUEEN = 27114;
	
	public Q211_TrialOfTheChallenger()
	{
		super(211, "Trial of the Challenger");
		
		setItemsIds(LETTER_OF_KASH, WATCHER_EYE_1, WATCHER_EYE_2, SCROLL_OF_SHYSLASSYS, BROKEN_KEY);
		
		addQuestStart(KASH);
		addTalkId(FILAUR, KASH, MARTIEN, RALDO, CHEST_OF_SHYSLASSYS);
		
		addMyDying(SHYSLASSYS, GORR, BARAHAM, SUCCUBUS_QUEEN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// KASH
		if (event.equalsIgnoreCase("30644-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			
			if (giveDimensionalDiamonds35(player))
				htmltext = "30644-05a.htm";
		}
		// MARTIEN
		else if (event.equalsIgnoreCase("30645-02.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER_OF_KASH, 1);
		}
		// RALDO
		else if (event.equalsIgnoreCase("30646-04.htm") || event.equalsIgnoreCase("30646-06.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, WATCHER_EYE_2, 1);
		}
		// CHEST_OF_SHYSLASSYS
		else if (event.equalsIgnoreCase("30647-04.htm"))
		{
			if (player.getInventory().hasItem(BROKEN_KEY))
			{
				if (Rnd.get(10) < 2)
				{
					htmltext = "30647-03.htm";
					playSound(player, SOUND_JACKPOT);
					takeItems(player, BROKEN_KEY, 1);
					int chance = Rnd.get(100);
					if (chance > 90)
					{
						rewardItems(player, BRIGANDINE_GAUNTLETS_PATTERN, 1);
						rewardItems(player, IRON_BOOTS_DESIGN, 1);
						rewardItems(player, MANTICOR_SKIN_GAITERS_PATTERN, 1);
						rewardItems(player, MITHRIL_SCALE_GAITERS_MATERIAL, 1);
						rewardItems(player, RIP_GAUNTLETS_PATTERN, 1);
					}
					else if (chance > 70)
					{
						rewardItems(player, ELVEN_NECKLACE_BEADS, 1);
						rewardItems(player, TOME_OF_BLOOD_PAGE, 1);
					}
					else if (chance > 40)
						rewardItems(player, WHITE_TUNIC_PATTERN, 1);
					else
						rewardItems(player, IRON_BOOTS_DESIGN, 1);
				}
				else
				{
					htmltext = "30647-02.htm";
					takeItems(player, BROKEN_KEY, 1);
					rewardItems(player, ADENA, Rnd.get(1, 1000));
				}
			}
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
				if (player.getClassId() != ClassId.WARRIOR && player.getClassId() != ClassId.ELVEN_KNIGHT && player.getClassId() != ClassId.PALUS_KNIGHT && player.getClassId() != ClassId.ORC_RAIDER && player.getClassId() != ClassId.MONK)
					htmltext = "30644-02.htm";
				else if (player.getStatus().getLevel() < 35)
					htmltext = "30644-01.htm";
				else
					htmltext = "30644-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case KASH:
						if (cond == 1)
							htmltext = "30644-06.htm";
						else if (cond == 2)
						{
							htmltext = "30644-07.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SCROLL_OF_SHYSLASSYS, 1);
							giveItems(player, LETTER_OF_KASH, 1);
						}
						else if (cond == 3)
							htmltext = "30644-08.htm";
						else if (cond > 3)
							htmltext = "30644-09.htm";
						break;
					
					case CHEST_OF_SHYSLASSYS:
						htmltext = "30647-01.htm";
						break;
					
					case MARTIEN:
						if (cond == 3)
							htmltext = "30645-01.htm";
						else if (cond == 4)
							htmltext = "30645-03.htm";
						else if (cond == 5)
						{
							htmltext = "30645-04.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, WATCHER_EYE_1, 1);
						}
						else if (cond == 6)
							htmltext = "30645-05.htm";
						else if (cond == 7)
							htmltext = "30645-07.htm";
						else if (cond > 7)
							htmltext = "30645-06.htm";
						break;
					
					case RALDO:
						if (cond == 7)
							htmltext = "30646-01.htm";
						else if (cond == 8)
							htmltext = "30646-06a.htm";
						else if (cond == 10)
						{
							htmltext = "30646-07.htm";
							takeItems(player, BROKEN_KEY, 1);
							giveItems(player, MARK_OF_CHALLENGER, 1);
							rewardExpAndSp(player, 72394, 11250);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case FILAUR:
						if (cond == 8)
						{
							if (player.getStatus().getLevel() >= 36)
							{
								htmltext = "30535-01.htm";
								st.setCond(9);
								playSound(player, SOUND_MIDDLE);
							}
							else
								htmltext = "30535-03.htm";
						}
						else if (cond == 9)
						{
							htmltext = "30535-02.htm";
							player.getRadarList().addMarker(MITHRIL_MINES_LOC);
						}
						else if (cond == 10)
							htmltext = "30535-04.htm";
						break;
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
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case SHYSLASSYS:
				if (st.getCond() == 1)
				{
					st.setCond(2);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, BROKEN_KEY, 1);
					giveItems(player, SCROLL_OF_SHYSLASSYS, 1);
					addSpawn(CHEST_OF_SHYSLASSYS, npc, false, 200000, true);
				}
				break;
			
			case GORR:
				if (st.getCond() == 4 && dropItemsAlways(player, WATCHER_EYE_1, 1, 1))
					st.setCond(5);
				break;
			
			case BARAHAM:
				if (st.getCond() == 6 && dropItemsAlways(player, WATCHER_EYE_2, 1, 1))
					st.setCond(7);
				addSpawn(RALDO, npc, false, 100000, true);
				break;
			
			case SUCCUBUS_QUEEN:
				if (st.getCond() == 9)
				{
					st.setCond(10);
					playSound(player, SOUND_MIDDLE);
				}
				addSpawn(RALDO, npc, false, 100000, true);
				break;
		}
	}
}