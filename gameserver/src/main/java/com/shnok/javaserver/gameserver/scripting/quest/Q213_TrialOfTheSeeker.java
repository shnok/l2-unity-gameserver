package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q213_TrialOfTheSeeker extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q213_TrialOfTheSeeker";
	
	// Items
	private static final int DUFNER_LETTER = 2647;
	private static final int TERRY_ORDER_1 = 2648;
	private static final int TERRY_ORDER_2 = 2649;
	private static final int TERRY_LETTER = 2650;
	private static final int VIKTOR_LETTER = 2651;
	private static final int HAWKEYE_LETTER = 2652;
	private static final int MYSTERIOUS_RUNESTONE = 2653;
	private static final int OL_MAHUM_RUNESTONE = 2654;
	private static final int TUREK_RUNESTONE = 2655;
	private static final int ANT_RUNESTONE = 2656;
	private static final int TURAK_BUGBEAR_RUNESTONE = 2657;
	private static final int TERRY_BOX = 2658;
	private static final int VIKTOR_REQUEST = 2659;
	private static final int MEDUSA_SCALES = 2660;
	private static final int SHILEN_RUNESTONE = 2661;
	private static final int ANALYSIS_REQUEST = 2662;
	private static final int MARINA_LETTER = 2663;
	private static final int EXPERIMENT_TOOLS = 2664;
	private static final int ANALYSIS_RESULT = 2665;
	private static final int TERRY_ORDER_3 = 2666;
	private static final int LIST_OF_HOST = 2667;
	private static final int ABYSS_RUNESTONE_1 = 2668;
	private static final int ABYSS_RUNESTONE_2 = 2669;
	private static final int ABYSS_RUNESTONE_3 = 2670;
	private static final int ABYSS_RUNESTONE_4 = 2671;
	private static final int TERRY_REPORT = 2672;
	
	// Rewards
	private static final int MARK_OF_SEEKER = 2673;
	
	// NPCs
	private static final int TERRY = 30064;
	private static final int DUFNER = 30106;
	private static final int BRUNON = 30526;
	private static final int VIKTOR = 30684;
	private static final int MARINA = 30715;
	
	// Monsters
	private static final int NEER_GHOUL_BERSERKER = 20198;
	private static final int ANT_CAPTAIN = 20080;
	private static final int OL_MAHUM_CAPTAIN = 20211;
	private static final int TURAK_BUGBEAR_WARRIOR = 20249;
	private static final int TUREK_ORC_WARLORD = 20495;
	private static final int MEDUSA = 20158;
	private static final int ANT_WARRIOR_CAPTAIN = 20088;
	private static final int MARSH_STAKATO_DRONE = 20234;
	private static final int BREKA_ORC_OVERLORD = 20270;
	private static final int LETO_LIZARDMAN_WARRIOR = 20580;
	
	public Q213_TrialOfTheSeeker()
	{
		super(213, "Trial of the Seeker");
		
		setItemsIds(DUFNER_LETTER, TERRY_ORDER_1, TERRY_ORDER_2, TERRY_LETTER, VIKTOR_LETTER, HAWKEYE_LETTER, MYSTERIOUS_RUNESTONE, OL_MAHUM_RUNESTONE, TUREK_RUNESTONE, ANT_RUNESTONE, TURAK_BUGBEAR_RUNESTONE, TERRY_BOX, VIKTOR_REQUEST, MEDUSA_SCALES, SHILEN_RUNESTONE, ANALYSIS_REQUEST, MARINA_LETTER, EXPERIMENT_TOOLS, ANALYSIS_RESULT, TERRY_ORDER_3, LIST_OF_HOST, ABYSS_RUNESTONE_1, ABYSS_RUNESTONE_2, ABYSS_RUNESTONE_3, ABYSS_RUNESTONE_4, TERRY_REPORT);
		
		addQuestStart(DUFNER);
		addTalkId(TERRY, DUFNER, BRUNON, VIKTOR, MARINA);
		
		addMyDying(NEER_GHOUL_BERSERKER, ANT_CAPTAIN, OL_MAHUM_CAPTAIN, TURAK_BUGBEAR_WARRIOR, TUREK_ORC_WARLORD, ANT_WARRIOR_CAPTAIN, MARSH_STAKATO_DRONE, BREKA_ORC_OVERLORD, LETO_LIZARDMAN_WARRIOR, MEDUSA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// DUFNER
		if (event.equalsIgnoreCase("30106-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, DUFNER_LETTER, 1);
			
			if (giveDimensionalDiamonds35(player))
				htmltext = "30106-05a.htm";
		}
		// TERRY
		else if (event.equalsIgnoreCase("30064-03.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, DUFNER_LETTER, 1);
			giveItems(player, TERRY_ORDER_1, 1);
		}
		else if (event.equalsIgnoreCase("30064-06.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MYSTERIOUS_RUNESTONE, 1);
			takeItems(player, TERRY_ORDER_1, 1);
			giveItems(player, TERRY_ORDER_2, 1);
		}
		else if (event.equalsIgnoreCase("30064-10.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ANT_RUNESTONE, 1);
			takeItems(player, OL_MAHUM_RUNESTONE, 1);
			takeItems(player, TURAK_BUGBEAR_RUNESTONE, 1);
			takeItems(player, TUREK_RUNESTONE, 1);
			takeItems(player, TERRY_ORDER_2, 1);
			giveItems(player, TERRY_BOX, 1);
			giveItems(player, TERRY_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30064-18.htm"))
		{
			if (player.getStatus().getLevel() < 36)
			{
				htmltext = "30064-17.htm";
				playSound(player, SOUND_ITEMGET);
				takeItems(player, ANALYSIS_RESULT, 1);
				giveItems(player, TERRY_ORDER_3, 1);
			}
			else
			{
				st.setCond(16);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, ANALYSIS_RESULT, 1);
				giveItems(player, LIST_OF_HOST, 1);
			}
		}
		// VIKTOR
		else if (event.equalsIgnoreCase("30684-05.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, TERRY_LETTER, 1);
			giveItems(player, VIKTOR_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30684-11.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, TERRY_LETTER, 1);
			takeItems(player, TERRY_BOX, 1);
			takeItems(player, HAWKEYE_LETTER, 1);
			takeItems(player, VIKTOR_LETTER, 1);
			giveItems(player, VIKTOR_REQUEST, 1);
		}
		else if (event.equalsIgnoreCase("30684-15.htm"))
		{
			st.setCond(11);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, VIKTOR_REQUEST, 1);
			takeItems(player, MEDUSA_SCALES, 10);
			giveItems(player, ANALYSIS_REQUEST, 1);
			giveItems(player, SHILEN_RUNESTONE, 1);
		}
		// MARINA
		else if (event.equalsIgnoreCase("30715-02.htm"))
		{
			st.setCond(12);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SHILEN_RUNESTONE, 1);
			takeItems(player, ANALYSIS_REQUEST, 1);
			giveItems(player, MARINA_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30715-05.htm"))
		{
			st.setCond(14);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, EXPERIMENT_TOOLS, 1);
			giveItems(player, ANALYSIS_RESULT, 1);
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
				if (player.getClassId() == ClassId.ROGUE || player.getClassId() == ClassId.ELVEN_SCOUT || player.getClassId() == ClassId.ASSASSIN)
					htmltext = (player.getStatus().getLevel() < 35) ? "30106-02.htm" : "30106-03.htm";
				else
					htmltext = "30106-00.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case DUFNER:
						if (cond == 1)
							htmltext = "30106-06.htm";
						else if (cond > 1)
						{
							if (!player.getInventory().hasItem(TERRY_REPORT))
								htmltext = "30106-07.htm";
							else
							{
								htmltext = "30106-08.htm";
								takeItems(player, TERRY_REPORT, 1);
								giveItems(player, MARK_OF_SEEKER, 1);
								rewardExpAndSp(player, 72126, 11000);
								player.broadcastPacket(new SocialAction(player, 3));
								playSound(player, SOUND_FINISH);
								st.exitQuest(false);
							}
						}
						break;
					
					case TERRY:
						if (cond == 1)
							htmltext = "30064-01.htm";
						else if (cond == 2)
							htmltext = "30064-04.htm";
						else if (cond == 3)
							htmltext = "30064-05.htm";
						else if (cond == 4)
							htmltext = "30064-08.htm";
						else if (cond == 5)
							htmltext = "30064-09.htm";
						else if (cond == 6)
							htmltext = "30064-11.htm";
						else if (cond == 7)
						{
							htmltext = "30064-12.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, VIKTOR_LETTER, 1);
							giveItems(player, HAWKEYE_LETTER, 1);
						}
						else if (cond == 8)
							htmltext = "30064-13.htm";
						else if (cond > 8 && cond < 14)
							htmltext = "30064-14.htm";
						else if (cond == 14)
						{
							if (!player.getInventory().hasItem(TERRY_ORDER_3))
								htmltext = "30064-15.htm";
							else if (player.getStatus().getLevel() < 36)
								htmltext = "30064-20.htm";
							else
							{
								htmltext = "30064-21.htm";
								st.setCond(15);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, TERRY_ORDER_3, 1);
								giveItems(player, LIST_OF_HOST, 1);
							}
						}
						else if (cond == 15 || cond == 16)
							htmltext = "30064-22.htm";
						else if (cond == 17)
						{
							if (!player.getInventory().hasItem(TERRY_REPORT))
							{
								htmltext = "30064-23.htm";
								playSound(player, SOUND_MIDDLE);
								takeItems(player, LIST_OF_HOST, 1);
								takeItems(player, ABYSS_RUNESTONE_1, 1);
								takeItems(player, ABYSS_RUNESTONE_2, 1);
								takeItems(player, ABYSS_RUNESTONE_3, 1);
								takeItems(player, ABYSS_RUNESTONE_4, 1);
								giveItems(player, TERRY_REPORT, 1);
							}
							else
								htmltext = "30064-24.htm";
						}
						break;
					
					case VIKTOR:
						if (cond == 6)
							htmltext = "30684-01.htm";
						else if (cond == 7)
							htmltext = "30684-05.htm";
						else if (cond == 8)
							htmltext = "30684-12.htm";
						else if (cond == 9)
							htmltext = "30684-13.htm";
						else if (cond == 10)
							htmltext = "30684-14.htm";
						else if (cond == 11)
							htmltext = "30684-16.htm";
						else if (cond > 11)
							htmltext = "30684-17.htm";
						break;
					
					case MARINA:
						if (cond == 11)
							htmltext = "30715-01.htm";
						else if (cond == 12)
							htmltext = "30715-03.htm";
						else if (cond == 13)
							htmltext = "30715-04.htm";
						else if (player.getInventory().hasItem(ANALYSIS_RESULT))
							htmltext = "30715-06.htm";
						break;
					
					case BRUNON:
						if (cond == 12)
						{
							htmltext = "30526-01.htm";
							st.setCond(13);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, MARINA_LETTER, 1);
							giveItems(player, EXPERIMENT_TOOLS, 1);
						}
						else if (cond == 13)
							htmltext = "30526-02.htm";
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
		
		final int cond = st.getCond();
		
		switch (npc.getNpcId())
		{
			case NEER_GHOUL_BERSERKER:
				if (cond == 2 && dropItems(player, MYSTERIOUS_RUNESTONE, 1, 1, 100000))
					st.setCond(3);
				break;
			
			case ANT_CAPTAIN:
				if (cond == 4 && dropItems(player, ANT_RUNESTONE, 1, 1, 250000) && player.getInventory().hasItems(OL_MAHUM_RUNESTONE, TURAK_BUGBEAR_RUNESTONE, TUREK_RUNESTONE))
					st.setCond(5);
				break;
			
			case OL_MAHUM_CAPTAIN:
				if (cond == 4 && dropItems(player, OL_MAHUM_RUNESTONE, 1, 1, 250000) && player.getInventory().hasItems(ANT_RUNESTONE, TURAK_BUGBEAR_RUNESTONE, TUREK_RUNESTONE))
					st.setCond(5);
				break;
			
			case TURAK_BUGBEAR_WARRIOR:
				if (cond == 4 && dropItems(player, TURAK_BUGBEAR_RUNESTONE, 1, 1, 250000) && player.getInventory().hasItems(ANT_RUNESTONE, OL_MAHUM_RUNESTONE, TUREK_RUNESTONE))
					st.setCond(5);
				break;
			
			case TUREK_ORC_WARLORD:
				if (cond == 4 && dropItems(player, TUREK_RUNESTONE, 1, 1, 250000) && player.getInventory().hasItems(ANT_RUNESTONE, OL_MAHUM_RUNESTONE, TURAK_BUGBEAR_RUNESTONE))
					st.setCond(5);
				break;
			
			case MEDUSA:
				if (cond == 9 && dropItems(player, MEDUSA_SCALES, 1, 10, 300000))
					st.setCond(10);
				break;
			
			case MARSH_STAKATO_DRONE:
				if ((cond == 15 || cond == 16) && dropItems(player, ABYSS_RUNESTONE_1, 1, 1, 250000) && player.getInventory().hasItems(ABYSS_RUNESTONE_2, ABYSS_RUNESTONE_3, ABYSS_RUNESTONE_4))
					st.setCond(17);
				break;
			
			case BREKA_ORC_OVERLORD:
				if ((cond == 15 || cond == 16) && dropItems(player, ABYSS_RUNESTONE_2, 1, 1, 250000) && player.getInventory().hasItems(ABYSS_RUNESTONE_1, ABYSS_RUNESTONE_3, ABYSS_RUNESTONE_4))
					st.setCond(17);
				break;
			
			case ANT_WARRIOR_CAPTAIN:
				if ((cond == 15 || cond == 16) && dropItems(player, ABYSS_RUNESTONE_3, 1, 1, 250000) && player.getInventory().hasItems(ABYSS_RUNESTONE_1, ABYSS_RUNESTONE_2, ABYSS_RUNESTONE_4))
					st.setCond(17);
				break;
			
			case LETO_LIZARDMAN_WARRIOR:
				if ((cond == 15 || cond == 16) && dropItems(player, ABYSS_RUNESTONE_4, 1, 1, 250000) && player.getInventory().hasItems(ABYSS_RUNESTONE_1, ABYSS_RUNESTONE_2, ABYSS_RUNESTONE_3))
					st.setCond(17);
				break;
		}
	}
}