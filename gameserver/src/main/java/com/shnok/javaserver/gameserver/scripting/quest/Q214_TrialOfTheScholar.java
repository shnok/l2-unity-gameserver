package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q214_TrialOfTheScholar extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q214_TrialOfTheScholar";
	
	// Items
	private static final int MIRIEN_SIGIL_1 = 2675;
	private static final int MIRIEN_SIGIL_2 = 2676;
	private static final int MIRIEN_SIGIL_3 = 2677;
	private static final int MIRIEN_INSTRUCTION = 2678;
	private static final int MARIA_LETTER_1 = 2679;
	private static final int MARIA_LETTER_2 = 2680;
	private static final int LUCAS_LETTER = 2681;
	private static final int LUCILLA_HANDBAG = 2682;
	private static final int CRETA_LETTER_1 = 2683;
	private static final int CRETA_PAINTING_1 = 2684;
	private static final int CRETA_PAINTING_2 = 2685;
	private static final int CRETA_PAINTING_3 = 2686;
	private static final int BROWN_SCROLL_SCRAP = 2687;
	private static final int CRYSTAL_OF_PURITY_1 = 2688;
	private static final int HIGH_PRIEST_SIGIL = 2689;
	private static final int GRAND_MAGISTER_SIGIL = 2690;
	private static final int CRONOS_SIGIL = 2691;
	private static final int SYLVAIN_LETTER = 2692;
	private static final int SYMBOL_OF_SYLVAIN = 2693;
	private static final int JUREK_LIST = 2694;
	private static final int MONSTER_EYE_DESTROYER_SKIN = 2695;
	private static final int SHAMAN_NECKLACE = 2696;
	private static final int SHACKLE_SCALP = 2697;
	private static final int SYMBOL_OF_JUREK = 2698;
	private static final int CRONOS_LETTER = 2699;
	private static final int DIETER_KEY = 2700;
	private static final int CRETA_LETTER_2 = 2701;
	private static final int DIETER_LETTER = 2702;
	private static final int DIETER_DIARY = 2703;
	private static final int RAUT_LETTER_ENVELOPE = 2704;
	private static final int TRIFF_RING = 2705;
	private static final int SCRIPTURE_CHAPTER_1 = 2706;
	private static final int SCRIPTURE_CHAPTER_2 = 2707;
	private static final int SCRIPTURE_CHAPTER_3 = 2708;
	private static final int SCRIPTURE_CHAPTER_4 = 2709;
	private static final int VALKON_REQUEST = 2710;
	private static final int POITAN_NOTES = 2711;
	private static final int STRONG_LIQUOR = 2713;
	private static final int CRYSTAL_OF_PURITY_2 = 2714;
	private static final int CASIAN_LIST = 2715;
	private static final int GHOUL_SKIN = 2716;
	private static final int MEDUSA_BLOOD = 2717;
	private static final int FETTERED_SOUL_ICHOR = 2718;
	private static final int ENCHANTED_GARGOYLE_NAIL = 2719;
	private static final int SYMBOL_OF_CRONOS = 2720;
	
	// Rewards
	private static final int MARK_OF_SCHOLAR = 2674;
	
	// NPCs
	private static final int SYLVAIN = 30070;
	private static final int LUCAS = 30071;
	private static final int VALKON = 30103;
	private static final int DIETER = 30111;
	private static final int JUREK = 30115;
	private static final int EDROC = 30230;
	private static final int RAUT = 30316;
	private static final int POITAN = 30458;
	private static final int MIRIEN = 30461;
	private static final int MARIA = 30608;
	private static final int CRETA = 30609;
	private static final int CRONOS = 30610;
	private static final int TRIFF = 30611;
	private static final int CASIAN = 30612;
	
	// Monsters
	private static final int MONSTER_EYE_DESTROYER = 20068;
	private static final int MEDUSA = 20158;
	private static final int GHOUL = 20201;
	private static final int SHACKLE_1 = 20235;
	private static final int SHACKLE_2 = 20279;
	private static final int BREKA_ORC_SHAMAN = 20269;
	private static final int FETTERED_SOUL = 20552;
	private static final int GRANDIS = 20554;
	private static final int ENCHANTED_GARGOYLE = 20567;
	private static final int LETO_LIZARDMAN_WARRIOR = 20580;
	
	public Q214_TrialOfTheScholar()
	{
		super(214, "Trial Of The Scholar");
		
		setItemsIds(MIRIEN_SIGIL_1, MIRIEN_SIGIL_2, MIRIEN_SIGIL_3, MIRIEN_INSTRUCTION, MARIA_LETTER_1, MARIA_LETTER_2, LUCAS_LETTER, LUCILLA_HANDBAG, CRETA_LETTER_1, CRETA_PAINTING_1, CRETA_PAINTING_2, CRETA_PAINTING_3, BROWN_SCROLL_SCRAP, CRYSTAL_OF_PURITY_1, HIGH_PRIEST_SIGIL, GRAND_MAGISTER_SIGIL, CRONOS_SIGIL, SYLVAIN_LETTER, SYMBOL_OF_SYLVAIN, JUREK_LIST, MONSTER_EYE_DESTROYER_SKIN, SHAMAN_NECKLACE, SHACKLE_SCALP, SYMBOL_OF_JUREK, CRONOS_LETTER, DIETER_KEY, CRETA_LETTER_2, DIETER_LETTER, DIETER_DIARY, RAUT_LETTER_ENVELOPE, TRIFF_RING, SCRIPTURE_CHAPTER_1, SCRIPTURE_CHAPTER_2, SCRIPTURE_CHAPTER_3, SCRIPTURE_CHAPTER_4, VALKON_REQUEST, POITAN_NOTES, STRONG_LIQUOR, CRYSTAL_OF_PURITY_2, CASIAN_LIST, GHOUL_SKIN, MEDUSA_BLOOD, FETTERED_SOUL_ICHOR, ENCHANTED_GARGOYLE_NAIL, SYMBOL_OF_CRONOS);
		
		addQuestStart(MIRIEN);
		addTalkId(MIRIEN, SYLVAIN, LUCAS, VALKON, DIETER, JUREK, EDROC, RAUT, POITAN, MARIA, CRETA, CRONOS, TRIFF, CASIAN);
		
		addMyDying(MONSTER_EYE_DESTROYER, MEDUSA, GHOUL, SHACKLE_1, SHACKLE_2, BREKA_ORC_SHAMAN, FETTERED_SOUL, GRANDIS, ENCHANTED_GARGOYLE, LETO_LIZARDMAN_WARRIOR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// MIRIEN
		if (event.equalsIgnoreCase("30461-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, MIRIEN_SIGIL_1, 1);
			
			if (giveDimensionalDiamonds35(player))
				htmltext = "30461-04a.htm";
		}
		else if (event.equalsIgnoreCase("30461-09.htm"))
		{
			if (player.getStatus().getLevel() < 36)
			{
				playSound(player, SOUND_ITEMGET);
				giveItems(player, MIRIEN_INSTRUCTION, 1);
			}
			else
			{
				htmltext = "30461-10.htm";
				st.setCond(19);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, MIRIEN_SIGIL_2, 1);
				takeItems(player, SYMBOL_OF_JUREK, 1);
				giveItems(player, MIRIEN_SIGIL_3, 1);
			}
		}
		// SYLVAIN
		else if (event.equalsIgnoreCase("30070-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, HIGH_PRIEST_SIGIL, 1);
			giveItems(player, SYLVAIN_LETTER, 1);
		}
		// MARIA
		else if (event.equalsIgnoreCase("30608-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SYLVAIN_LETTER, 1);
			giveItems(player, MARIA_LETTER_1, 1);
		}
		else if (event.equalsIgnoreCase("30608-08.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRETA_LETTER_1, 1);
			giveItems(player, LUCILLA_HANDBAG, 1);
		}
		else if (event.equalsIgnoreCase("30608-14.htm"))
		{
			st.setCond(13);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BROWN_SCROLL_SCRAP, -1);
			takeItems(player, CRETA_PAINTING_3, 1);
			giveItems(player, CRYSTAL_OF_PURITY_1, 1);
		}
		// JUREK
		else if (event.equalsIgnoreCase("30115-03.htm"))
		{
			st.setCond(16);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, GRAND_MAGISTER_SIGIL, 1);
			giveItems(player, JUREK_LIST, 1);
		}
		// LUCAS
		else if (event.equalsIgnoreCase("30071-04.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRETA_PAINTING_2, 1);
			giveItems(player, CRETA_PAINTING_3, 1);
		}
		// CRETA
		else if (event.equalsIgnoreCase("30609-05.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MARIA_LETTER_2, 1);
			giveItems(player, CRETA_LETTER_1, 1);
		}
		else if (event.equalsIgnoreCase("30609-09.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LUCILLA_HANDBAG, 1);
			giveItems(player, CRETA_PAINTING_1, 1);
		}
		else if (event.equalsIgnoreCase("30609-14.htm"))
		{
			st.setCond(22);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, DIETER_KEY, 1);
			giveItems(player, CRETA_LETTER_2, 1);
		}
		// CRONOS
		else if (event.equalsIgnoreCase("30610-10.htm"))
		{
			st.setCond(20);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, CRONOS_LETTER, 1);
			giveItems(player, CRONOS_SIGIL, 1);
		}
		else if (event.equalsIgnoreCase("30610-14.htm"))
		{
			st.setCond(31);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRONOS_SIGIL, 1);
			takeItems(player, DIETER_DIARY, 1);
			takeItems(player, SCRIPTURE_CHAPTER_1, 1);
			takeItems(player, SCRIPTURE_CHAPTER_2, 1);
			takeItems(player, SCRIPTURE_CHAPTER_3, 1);
			takeItems(player, SCRIPTURE_CHAPTER_4, 1);
			takeItems(player, TRIFF_RING, 1);
			giveItems(player, SYMBOL_OF_CRONOS, 1);
		}
		// DIETER
		else if (event.equalsIgnoreCase("30111-05.htm"))
		{
			st.setCond(21);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRONOS_LETTER, 1);
			giveItems(player, DIETER_KEY, 1);
		}
		else if (event.equalsIgnoreCase("30111-09.htm"))
		{
			st.setCond(23);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRETA_LETTER_2, 1);
			giveItems(player, DIETER_DIARY, 1);
			giveItems(player, DIETER_LETTER, 1);
		}
		// EDROC
		else if (event.equalsIgnoreCase("30230-02.htm"))
		{
			st.setCond(24);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, DIETER_LETTER, 1);
			giveItems(player, RAUT_LETTER_ENVELOPE, 1);
		}
		// RAUT
		else if (event.equalsIgnoreCase("30316-02.htm"))
		{
			st.setCond(25);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, RAUT_LETTER_ENVELOPE, 1);
			giveItems(player, SCRIPTURE_CHAPTER_1, 1);
			giveItems(player, STRONG_LIQUOR, 1);
		}
		// TRIFF
		else if (event.equalsIgnoreCase("30611-04.htm"))
		{
			st.setCond(26);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, STRONG_LIQUOR, 1);
			giveItems(player, TRIFF_RING, 1);
		}
		// VALKON
		else if (event.equalsIgnoreCase("30103-04.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, VALKON_REQUEST, 1);
		}
		// CASIAN
		else if (event.equalsIgnoreCase("30612-04.htm"))
		{
			st.setCond(28);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, CASIAN_LIST, 1);
		}
		else if (event.equalsIgnoreCase("30612-07.htm"))
		{
			st.setCond(30);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CASIAN_LIST, 1);
			takeItems(player, ENCHANTED_GARGOYLE_NAIL, -1);
			takeItems(player, FETTERED_SOUL_ICHOR, -1);
			takeItems(player, GHOUL_SKIN, -1);
			takeItems(player, MEDUSA_BLOOD, -1);
			takeItems(player, POITAN_NOTES, 1);
			giveItems(player, SCRIPTURE_CHAPTER_4, 1);
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
				if (player.getClassId() != ClassId.HUMAN_WIZARD && player.getClassId() != ClassId.ELVEN_WIZARD && player.getClassId() != ClassId.DARK_WIZARD)
					htmltext = "30461-01.htm";
				else if (player.getStatus().getLevel() < 35)
					htmltext = "30461-02.htm";
				else
					htmltext = "30461-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case MIRIEN:
						if (cond < 14)
							htmltext = "30461-05.htm";
						else if (cond == 14)
						{
							htmltext = "30461-06.htm";
							st.setCond(15);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, MIRIEN_SIGIL_1, 1);
							takeItems(player, SYMBOL_OF_SYLVAIN, 1);
							giveItems(player, MIRIEN_SIGIL_2, 1);
						}
						else if (cond > 14 && cond < 18)
							htmltext = "30461-07.htm";
						else if (cond == 18)
						{
							if (!player.getInventory().hasItem(MIRIEN_INSTRUCTION))
								htmltext = "30461-08.htm";
							else
							{
								if (player.getStatus().getLevel() < 36)
									htmltext = "30461-11.htm";
								else
								{
									htmltext = "30461-12.htm";
									st.setCond(19);
									playSound(player, SOUND_MIDDLE);
									takeItems(player, MIRIEN_INSTRUCTION, 1);
									takeItems(player, MIRIEN_SIGIL_2, 1);
									takeItems(player, SYMBOL_OF_JUREK, 1);
									giveItems(player, MIRIEN_SIGIL_3, 1);
								}
							}
						}
						else if (cond > 18 && cond < 31)
							htmltext = "30461-13.htm";
						else if (cond == 31)
						{
							htmltext = "30461-14.htm";
							takeItems(player, MIRIEN_SIGIL_3, 1);
							takeItems(player, SYMBOL_OF_CRONOS, 1);
							giveItems(player, MARK_OF_SCHOLAR, 1);
							rewardExpAndSp(player, 80265, 30000);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case SYLVAIN:
						if (cond == 1)
							htmltext = "30070-01.htm";
						else if (cond < 13)
							htmltext = "30070-03.htm";
						else if (cond == 13)
						{
							htmltext = "30070-04.htm";
							st.setCond(14);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CRYSTAL_OF_PURITY_1, 1);
							takeItems(player, HIGH_PRIEST_SIGIL, 1);
							giveItems(player, SYMBOL_OF_SYLVAIN, 1);
						}
						else if (cond == 14)
							htmltext = "30070-05.htm";
						else if (cond > 14)
							htmltext = "30070-06.htm";
						break;
					
					case MARIA:
						if (cond == 2)
							htmltext = "30608-01.htm";
						else if (cond == 3)
							htmltext = "30608-03.htm";
						else if (cond == 4)
						{
							htmltext = "30608-04.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LUCAS_LETTER, 1);
							giveItems(player, MARIA_LETTER_2, 1);
						}
						else if (cond == 5)
							htmltext = "30608-05.htm";
						else if (cond == 6)
							htmltext = "30608-06.htm";
						else if (cond == 7)
							htmltext = "30608-09.htm";
						else if (cond == 8)
						{
							htmltext = "30608-10.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CRETA_PAINTING_1, 1);
							giveItems(player, CRETA_PAINTING_2, 1);
						}
						else if (cond == 9)
							htmltext = "30608-11.htm";
						else if (cond == 10)
						{
							htmltext = "30608-12.htm";
							st.setCond(11);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 11)
							htmltext = "30608-12.htm";
						else if (cond == 12)
							htmltext = "30608-13.htm";
						else if (cond == 13)
							htmltext = "30608-15.htm";
						else if (player.getInventory().hasAtLeastOneItem(SYMBOL_OF_SYLVAIN, MIRIEN_SIGIL_2))
							htmltext = "30608-16.htm";
						else if (cond > 18)
						{
							if (!player.getInventory().hasItem(VALKON_REQUEST))
								htmltext = "30608-17.htm";
							else
							{
								htmltext = "30608-18.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, VALKON_REQUEST, 1);
								giveItems(player, CRYSTAL_OF_PURITY_2, 1);
							}
						}
						break;
					
					case JUREK:
						if (cond == 15)
							htmltext = "30115-01.htm";
						else if (cond == 16)
							htmltext = "30115-04.htm";
						else if (cond == 17)
						{
							htmltext = "30115-05.htm";
							st.setCond(18);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GRAND_MAGISTER_SIGIL, 1);
							takeItems(player, JUREK_LIST, 1);
							takeItems(player, MONSTER_EYE_DESTROYER_SKIN, -1);
							takeItems(player, SHACKLE_SCALP, -1);
							takeItems(player, SHAMAN_NECKLACE, -1);
							giveItems(player, SYMBOL_OF_JUREK, 1);
						}
						else if (cond == 18)
							htmltext = "30115-06.htm";
						else if (cond > 18)
							htmltext = "30115-07.htm";
						break;
					
					case LUCAS:
						if (cond == 3)
						{
							htmltext = "30071-01.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, MARIA_LETTER_1, 1);
							giveItems(player, LUCAS_LETTER, 1);
						}
						else if (cond > 3 && cond < 9)
							htmltext = "30071-02.htm";
						else if (cond == 9)
							htmltext = "30071-03.htm";
						else if (cond == 10 || cond == 11)
							htmltext = "30071-05.htm";
						else if (cond == 12)
							htmltext = "30071-06.htm";
						else if (cond > 12)
							htmltext = "30071-07.htm";
						break;
					
					case CRETA:
						if (cond == 5)
							htmltext = "30609-01.htm";
						else if (cond == 6)
							htmltext = "30609-06.htm";
						else if (cond == 7)
							htmltext = "30609-07.htm";
						else if (cond > 7 && cond < 13)
							htmltext = "30609-10.htm";
						else if (cond >= 13 && cond < 19)
							htmltext = "30609-11.htm";
						else if (cond == 21)
							htmltext = "30609-12.htm";
						else if (cond > 21)
							htmltext = "30609-15.htm";
						break;
					
					case CRONOS:
						if (cond == 19)
							htmltext = "30610-01.htm";
						else if (cond > 19 && cond < 30)
							htmltext = "30610-11.htm";
						else if (cond == 30)
							htmltext = "30610-12.htm";
						else if (cond == 31)
							htmltext = "30610-15.htm";
						break;
					
					case DIETER:
						if (cond == 20)
							htmltext = "30111-01.htm";
						else if (cond == 21)
							htmltext = "30111-06.htm";
						else if (cond == 22)
							htmltext = "30111-07.htm";
						else if (cond == 23)
							htmltext = "30111-10.htm";
						else if (cond == 24)
							htmltext = "30111-11.htm";
						else if (cond > 24 && cond < 31)
							htmltext = (!player.getInventory().hasItems(SCRIPTURE_CHAPTER_1, SCRIPTURE_CHAPTER_2, SCRIPTURE_CHAPTER_3, SCRIPTURE_CHAPTER_4)) ? "30111-12.htm" : "30111-13.htm";
						else if (cond == 31)
							htmltext = "30111-15.htm";
						break;
					
					case EDROC:
						if (cond == 23)
							htmltext = "30230-01.htm";
						else if (cond == 24)
							htmltext = "30230-03.htm";
						else if (cond > 24)
							htmltext = "30230-04.htm";
						break;
					
					case RAUT:
						if (cond == 24)
							htmltext = "30316-01.htm";
						else if (cond == 25)
							htmltext = "30316-04.htm";
						else if (cond > 25)
							htmltext = "30316-05.htm";
						break;
					
					case TRIFF:
						if (cond == 25)
							htmltext = "30611-01.htm";
						else if (cond > 25)
							htmltext = "30611-05.htm";
						break;
					
					case VALKON:
						if (player.getInventory().hasItem(TRIFF_RING))
						{
							if (!player.getInventory().hasItem(SCRIPTURE_CHAPTER_2))
							{
								if (!player.getInventory().hasItem(VALKON_REQUEST))
								{
									if (!player.getInventory().hasItem(CRYSTAL_OF_PURITY_2))
										htmltext = "30103-01.htm";
									else
									{
										htmltext = "30103-06.htm";
										playSound(player, SOUND_ITEMGET);
										takeItems(player, CRYSTAL_OF_PURITY_2, 1);
										giveItems(player, SCRIPTURE_CHAPTER_2, 1);
									}
								}
								else
									htmltext = "30103-05.htm";
							}
							else
								htmltext = "30103-07.htm";
						}
						break;
					
					case POITAN:
						if (cond == 26 || cond == 27)
						{
							if (!player.getInventory().hasItem(POITAN_NOTES))
							{
								htmltext = "30458-01.htm";
								playSound(player, SOUND_ITEMGET);
								giveItems(player, POITAN_NOTES, 1);
							}
							else
								htmltext = "30458-02.htm";
						}
						else if (cond == 28 || cond == 29)
							htmltext = "30458-03.htm";
						else if (cond == 30)
							htmltext = "30458-04.htm";
						break;
					
					case CASIAN:
						if ((cond == 26 || cond == 27) && player.getInventory().hasItem(POITAN_NOTES))
						{
							if (player.getInventory().hasItems(SCRIPTURE_CHAPTER_1, SCRIPTURE_CHAPTER_2, SCRIPTURE_CHAPTER_3))
								htmltext = "30612-02.htm";
							else
							{
								htmltext = "30612-01.htm";
								if (cond == 26)
								{
									st.setCond(27);
									playSound(player, SOUND_MIDDLE);
								}
							}
						}
						else if (cond == 28)
							htmltext = "30612-05.htm";
						else if (cond == 29)
							htmltext = "30612-06.htm";
						else if (cond == 30)
							htmltext = "30612-08.htm";
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
			case LETO_LIZARDMAN_WARRIOR:
				if (st.getCond() == 11 && dropItems(player, BROWN_SCROLL_SCRAP, 1, 5, 500000))
					st.setCond(12);
				break;
			
			case SHACKLE_1, SHACKLE_2:
				if (st.getCond() == 16 && dropItems(player, SHACKLE_SCALP, 1, 2, 500000))
					if (player.getInventory().getItemCount(MONSTER_EYE_DESTROYER_SKIN) == 5 && player.getInventory().getItemCount(SHAMAN_NECKLACE) == 5)
						st.setCond(17);
				break;
			
			case MONSTER_EYE_DESTROYER:
				if (st.getCond() == 16 && dropItems(player, MONSTER_EYE_DESTROYER_SKIN, 1, 5, 500000))
					if (player.getInventory().getItemCount(SHACKLE_SCALP) == 2 && player.getInventory().getItemCount(SHAMAN_NECKLACE) == 5)
						st.setCond(17);
				break;
			
			case BREKA_ORC_SHAMAN:
				if (st.getCond() == 16 && dropItems(player, SHAMAN_NECKLACE, 1, 5, 500000))
					if (player.getInventory().getItemCount(SHACKLE_SCALP) == 2 && player.getInventory().getItemCount(MONSTER_EYE_DESTROYER_SKIN) == 5)
						st.setCond(17);
				break;
			
			case GRANDIS:
				if (player.getInventory().hasItem(TRIFF_RING))
					dropItems(player, SCRIPTURE_CHAPTER_3, 1, 1, 300000);
				break;
			
			case MEDUSA:
				if (st.getCond() == 28 && dropItemsAlways(player, MEDUSA_BLOOD, 1, 12))
					if (player.getInventory().getItemCount(GHOUL_SKIN) == 10 && player.getInventory().getItemCount(FETTERED_SOUL_ICHOR) == 5 && player.getInventory().getItemCount(ENCHANTED_GARGOYLE_NAIL) == 5)
						st.setCond(29);
				break;
			
			case GHOUL:
				if (st.getCond() == 28 && dropItemsAlways(player, GHOUL_SKIN, 1, 10))
					if (player.getInventory().getItemCount(MEDUSA_BLOOD) == 12 && player.getInventory().getItemCount(FETTERED_SOUL_ICHOR) == 5 && player.getInventory().getItemCount(ENCHANTED_GARGOYLE_NAIL) == 5)
						st.setCond(29);
				break;
			
			case FETTERED_SOUL:
				if (st.getCond() == 28 && dropItemsAlways(player, FETTERED_SOUL_ICHOR, 1, 5))
					if (player.getInventory().getItemCount(MEDUSA_BLOOD) == 12 && player.getInventory().getItemCount(GHOUL_SKIN) == 10 && player.getInventory().getItemCount(ENCHANTED_GARGOYLE_NAIL) == 5)
						st.setCond(29);
				break;
			
			case ENCHANTED_GARGOYLE:
				if (st.getCond() == 28 && dropItemsAlways(player, ENCHANTED_GARGOYLE_NAIL, 1, 5))
					if (player.getInventory().getItemCount(MEDUSA_BLOOD) == 12 && player.getInventory().getItemCount(GHOUL_SKIN) == 10 && player.getInventory().getItemCount(FETTERED_SOUL_ICHOR) == 5)
						st.setCond(29);
				break;
		}
	}
}