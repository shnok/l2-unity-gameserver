package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q336_CoinsOfMagic extends Quest
{
	private static final String QUEST_NAME = "Q336_CoinsOfMagic";
	
	// Npcs
	private static final int PANO = 30078;
	private static final int COLLOB = 30092;
	private static final int RALFORD = 30165;
	private static final int HAGGER = 30183;
	private static final int STAN = 30200;
	private static final int SORINT = 30232;
	private static final int LORAIN = 30673;
	private static final int DUNING = 30688;
	private static final int PAGE = 30696;
	private static final int BERNARD = 30702;
	private static final int FERRIS = 30847;
	
	// Mobs
	private static final int BLOODY_GHOST = 20960;
	private static final int WAILING_OF_SPLENDOR_1 = 21539;
	private static final int WAILING_OF_SPLENDOR_2 = 21540;
	private static final int CLAWS_OF_SPLENDOR_1 = 21521;
	private static final int CLAWS_OF_SPLENDOR_2 = 21522;
	private static final int FORMOR = 20568;
	private static final int FORMOR_ELDER = 20569;
	private static final int DARK_GUARD = 20959;
	private static final int HUNGERED_CORPSE = 20954;
	private static final int OEL_MAHUM = 20161;
	private static final int OEL_MAHUM_WARRIOR = 20575;
	private static final int OEL_MAHUM_WITCH_DOCTOR = 20576;
	private static final int TARLK_BUGBEAR_HIGH_WARRIOR = 20572;
	private static final int VANOR_SILENOS_SHAMAN = 20685;
	
	private static final int ANTELOPE_1 = 21278;
	private static final int ANTELOPE_2 = 21279;
	private static final int ANTELOPE_3 = 21280;
	private static final int BUFFALO_1 = 21286;
	private static final int BUFFALO_2 = 21287;
	private static final int BUFFALO_3 = 21288;
	private static final int DOOM_KNIGHT = 20674;
	private static final int DOOM_SERVANT = 21006;
	private static final int HEADLESS_KNIGHT_1 = 20146;
	private static final int HEADLESS_KNIGHT_2 = 20280;
	private static final int KOOKABURRA_1 = 21274;
	private static final int KOOKABURRA_2 = 21275;
	private static final int KOOKABURRA_3 = 21276;
	private static final int MALRUK_SUCCUBUS_TUREN_1 = 20245;
	private static final int MALRUK_SUCCUBUS_TUREN_2 = 20284;
	private static final int ROYAL_CAVE_SERVANT_1 = 20240;
	private static final int ROYAL_CAVE_SERVANT_2 = 20276;
	private static final int SHACKLE_1 = 20235;
	private static final int SHACKLE_2 = 20279;
	private static final int TIMAK_ORK = 20583;
	
	private static final int BANDERSNATCH_1 = 21282;
	private static final int BANDERSNATCH_2 = 21283;
	private static final int BANDERSNATCH_3 = 21284;
	private static final int PUNISHMENT_OF_SPLENDOR_1 = 21531;
	private static final int PUNISHMENT_OF_SPLENDOR_2 = 21658;
	private static final int WISDOM_OF_SPLENDOR = 21526;
	private static final int DOOM_ARCHER = 21008;
	private static final int GRAVE_LICH = 21003;
	private static final int HATAR_HANESHEE = 20663;
	private static final int LAKIN = 20604;
	private static final int NIHIL_INVADER = 20957;
	private static final int TORTURED_UNDEAD = 20678;
	private static final int TIMAK_ORK_ARCHER = 20584;
	private static final int TIMAK_ORK_SHAMAN = 20587;
	private static final int TIMAK_ORK_SOLDIER = 20585;
	
	private static final int HARIT_LIZARDMAN_SHAMAN = 20644;
	private static final int HARIT_LIZARDMAN_MATRIARCH = 20645;
	
	// Items
	private static final int BLOOD_MEDUSA = 3472;
	private static final int BLOOD_WEREWOLF = 3473;
	private static final int BLOOD_BASILISK = 3474;
	private static final int BLOOD_DREVANUL = 3475;
	private static final int BLOOD_SUCCUBUS = 3476;
	private static final int BLOOD_DRAGON = 3477;
	private static final int BELETH_BLOOD_DRAGON = 3478;
	private static final int MANAK_BLOOD_WEREWOLF = 3479;
	private static final int NIA_BLOOD_MEDUSA = 3480;
	private static final int GOLD_DRAGON = 3481;
	private static final int GOLD_WYVERN = 3482;
	private static final int GOLD_KNIGHT = 3483;
	private static final int GOLD_GIANT = 3484;
	private static final int GOLD_DRAKE = 3485;
	private static final int GOLD_WYRM = 3486;
	private static final int BELETH_GOLD_DRAGON = 3487;
	private static final int MANAK_GOLD_GIANT = 3488;
	private static final int NIA_GOLD_WYVERN = 3489;
	private static final int SILVER_UNICORN = 3490;
	private static final int SILVER_FAIRY = 3491;
	private static final int SILVER_DRYAD = 3492;
	private static final int SILVER_DRAGON = 3493;
	private static final int SILVER_GOLEM = 3494;
	private static final int SILVER_UNDINE = 3495;
	private static final int BELETH_SILVER_DRAGON = 3496;
	private static final int MANAK_SILVER_DRYAD = 3497;
	private static final int NIA_SILVER_FAIRY = 3498;
	
	private static final int COIN_DIAGRAM = 3811;
	private static final int KALDIS_GOLD_DRAGON = 3812;
	private static final int COIN_COLLECTOR_MEMBERSHIP_1 = 3813;
	private static final int COIN_COLLECTOR_MEMBERSHIP_2 = 3814;
	private static final int COIN_COLLECTOR_MEMBERSHIP_3 = 3815;
	
	// Reward
	private static final int SWORD_OF_LIMIT = 132;
	private static final int DEMON_STAFF = 206;
	private static final int DARK_SCREAMER = 233;
	private static final int WIDOW_MAKER = 303;
	private static final int DEMON_STOCKINGS = 472;
	private static final int MOONSTONE_EARRING = 852;
	private static final int NASSEN_EARRING = 855;
	private static final int RING_OF_BINDING = 886;
	private static final int NECKLACE_OF_PROTECTION = 916;
	private static final int FULL_PLATE_HELMET = 2414;
	private static final int DEMON_BOOTS = 2435;
	private static final int DEMON_GLOVES = 2459;
	
	/**
	 * Second multiplier for required items
	 */
	private static final Map<Integer, int[]> MULTIPLIER_2ND = new HashMap<>();
	{
		MULTIPLIER_2ND.put(1, new int[]
		{
			5,
			25,
			100
		});
		MULTIPLIER_2ND.put(2, new int[]
		{
			5,
			25,
			50
		});
		MULTIPLIER_2ND.put(3, new int[]
		{
			10,
			50,
			100
		});
	}
	
	/**
	 * Third multiplier for required items
	 */
	private static final Map<Integer, int[]> MULTIPLIER_3RD = new HashMap<>();
	{
		MULTIPLIER_3RD.put(1, new int[]
		{
			4,
			8,
			9
		});
		MULTIPLIER_3RD.put(2, new int[]
		{
			3,
			7,
			9
		});
		MULTIPLIER_3RD.put(3, new int[]
		{
			4,
			8,
			9
		});
	}
	
	/**
	 * Counts for rewarded items
	 */
	private static final Map<Integer, Integer> COUNTS = new HashMap<>();
	{
		COUNTS.put(0, 1);
		COUNTS.put(1, 5);
		COUNTS.put(2, 10);
	}
	
	// NPC Id, Item ID, Chance
	private static final Map<Integer, IntIntHolder> DROPLIST_FIRST = new HashMap<>();
	{
		DROPLIST_FIRST.put(HARIT_LIZARDMAN_SHAMAN, new IntIntHolder(KALDIS_GOLD_DRAGON, 630000));
		DROPLIST_FIRST.put(HARIT_LIZARDMAN_MATRIARCH, new IntIntHolder(KALDIS_GOLD_DRAGON, 630000));
	}
	
	// NPC Id, Item ID, Chance
	private static final Map<Integer, IntIntHolder> DROPLIST_SECOND = new HashMap<>();
	{
		// 3490 itemId - 14 npcIds
		DROPLIST_SECOND.put(BLOODY_GHOST, new IntIntHolder(SILVER_UNICORN, 80000));
		DROPLIST_SECOND.put(WAILING_OF_SPLENDOR_1, new IntIntHolder(SILVER_UNICORN, 210000));
		DROPLIST_SECOND.put(WAILING_OF_SPLENDOR_2, new IntIntHolder(SILVER_UNICORN, 210000));
		DROPLIST_SECOND.put(CLAWS_OF_SPLENDOR_1, new IntIntHolder(SILVER_UNICORN, 150000));
		DROPLIST_SECOND.put(CLAWS_OF_SPLENDOR_2, new IntIntHolder(SILVER_UNICORN, 150000));
		DROPLIST_SECOND.put(FORMOR, new IntIntHolder(SILVER_UNICORN, 110000));
		DROPLIST_SECOND.put(FORMOR_ELDER, new IntIntHolder(SILVER_UNICORN, 120000));
		DROPLIST_SECOND.put(DARK_GUARD, new IntIntHolder(SILVER_UNICORN, 150000));
		DROPLIST_SECOND.put(HUNGERED_CORPSE, new IntIntHolder(SILVER_UNICORN, 100000));
		DROPLIST_SECOND.put(OEL_MAHUM, new IntIntHolder(SILVER_UNICORN, 80000));
		DROPLIST_SECOND.put(OEL_MAHUM_WARRIOR, new IntIntHolder(SILVER_UNICORN, 90000));
		DROPLIST_SECOND.put(OEL_MAHUM_WITCH_DOCTOR, new IntIntHolder(SILVER_UNICORN, 200000));
		DROPLIST_SECOND.put(TARLK_BUGBEAR_HIGH_WARRIOR, new IntIntHolder(SILVER_UNICORN, 80000));
		DROPLIST_SECOND.put(VANOR_SILENOS_SHAMAN, new IntIntHolder(SILVER_UNICORN, 70000));
		
		// 3482 itemId - 20 npcIds
		DROPLIST_SECOND.put(ANTELOPE_1, new IntIntHolder(GOLD_WYVERN, 100000));
		DROPLIST_SECOND.put(ANTELOPE_2, new IntIntHolder(GOLD_WYVERN, 100000));
		DROPLIST_SECOND.put(ANTELOPE_3, new IntIntHolder(GOLD_WYVERN, 100000));
		DROPLIST_SECOND.put(BUFFALO_1, new IntIntHolder(GOLD_WYVERN, 110000));
		DROPLIST_SECOND.put(BUFFALO_2, new IntIntHolder(GOLD_WYVERN, 110000));
		DROPLIST_SECOND.put(BUFFALO_3, new IntIntHolder(GOLD_WYVERN, 110000));
		DROPLIST_SECOND.put(DOOM_KNIGHT, new IntIntHolder(GOLD_WYVERN, 210000));
		DROPLIST_SECOND.put(DOOM_SERVANT, new IntIntHolder(GOLD_WYVERN, 140000));
		DROPLIST_SECOND.put(HEADLESS_KNIGHT_1, new IntIntHolder(GOLD_WYVERN, 80000));
		DROPLIST_SECOND.put(HEADLESS_KNIGHT_2, new IntIntHolder(GOLD_WYVERN, 85000));
		DROPLIST_SECOND.put(KOOKABURRA_1, new IntIntHolder(GOLD_WYVERN, 110000));
		DROPLIST_SECOND.put(KOOKABURRA_2, new IntIntHolder(GOLD_WYVERN, 110000));
		DROPLIST_SECOND.put(KOOKABURRA_3, new IntIntHolder(GOLD_WYVERN, 100000));
		DROPLIST_SECOND.put(MALRUK_SUCCUBUS_TUREN_1, new IntIntHolder(GOLD_WYVERN, 100000));
		DROPLIST_SECOND.put(MALRUK_SUCCUBUS_TUREN_2, new IntIntHolder(GOLD_WYVERN, 100000));
		DROPLIST_SECOND.put(ROYAL_CAVE_SERVANT_1, new IntIntHolder(GOLD_WYVERN, 100000));
		DROPLIST_SECOND.put(ROYAL_CAVE_SERVANT_2, new IntIntHolder(GOLD_WYVERN, 100000));
		DROPLIST_SECOND.put(SHACKLE_1, new IntIntHolder(GOLD_WYVERN, 70000));
		DROPLIST_SECOND.put(SHACKLE_2, new IntIntHolder(GOLD_WYVERN, 70000));
		DROPLIST_SECOND.put(TIMAK_ORK, new IntIntHolder(GOLD_WYVERN, 80000));
		
		// 3472 itemId - 15 npcIds
		DROPLIST_SECOND.put(BANDERSNATCH_1, new IntIntHolder(BLOOD_MEDUSA, 90000));
		DROPLIST_SECOND.put(BANDERSNATCH_2, new IntIntHolder(BLOOD_MEDUSA, 90000));
		DROPLIST_SECOND.put(BANDERSNATCH_3, new IntIntHolder(BLOOD_MEDUSA, 90000));
		DROPLIST_SECOND.put(PUNISHMENT_OF_SPLENDOR_1, new IntIntHolder(BLOOD_MEDUSA, 160000));
		DROPLIST_SECOND.put(PUNISHMENT_OF_SPLENDOR_2, new IntIntHolder(BLOOD_MEDUSA, 160000));
		DROPLIST_SECOND.put(WISDOM_OF_SPLENDOR, new IntIntHolder(BLOOD_MEDUSA, 160000));
		DROPLIST_SECOND.put(DOOM_ARCHER, new IntIntHolder(BLOOD_MEDUSA, 160000));
		DROPLIST_SECOND.put(GRAVE_LICH, new IntIntHolder(BLOOD_MEDUSA, 90000));
		DROPLIST_SECOND.put(HATAR_HANESHEE, new IntIntHolder(BLOOD_MEDUSA, 70000));
		DROPLIST_SECOND.put(LAKIN, new IntIntHolder(BLOOD_MEDUSA, 60000));
		DROPLIST_SECOND.put(NIHIL_INVADER, new IntIntHolder(BLOOD_MEDUSA, 110000));
		DROPLIST_SECOND.put(TORTURED_UNDEAD, new IntIntHolder(BLOOD_MEDUSA, 80000));
		DROPLIST_SECOND.put(TIMAK_ORK_ARCHER, new IntIntHolder(BLOOD_MEDUSA, 140000));
		DROPLIST_SECOND.put(TIMAK_ORK_SHAMAN, new IntIntHolder(BLOOD_MEDUSA, 130000));
		DROPLIST_SECOND.put(TIMAK_ORK_SOLDIER, new IntIntHolder(BLOOD_MEDUSA, 140000));
	}
	
	// Talk ID, HTML number
	private static final Map<Integer, Integer> DIALOGUES = new HashMap<>();
	{
		DIALOGUES.put(21, 23);
		DIALOGUES.put(25, 24);
		DIALOGUES.put(37, 25);
		DIALOGUES.put(41, 26);
		DIALOGUES.put(61, 27);
		DIALOGUES.put(29, 28);
		DIALOGUES.put(45, 29);
		DIALOGUES.put(53, 30);
		DIALOGUES.put(57, 31);
		DIALOGUES.put(22, 32);
		DIALOGUES.put(26, 33);
		DIALOGUES.put(38, 34);
		DIALOGUES.put(42, 35);
		DIALOGUES.put(62, 36);
		DIALOGUES.put(30, 37);
		DIALOGUES.put(46, 38);
		DIALOGUES.put(54, 39);
		DIALOGUES.put(58, 40);
		DIALOGUES.put(23, 41);
		DIALOGUES.put(27, 42);
		DIALOGUES.put(39, 43);
		DIALOGUES.put(43, 44);
		DIALOGUES.put(63, 45);
		DIALOGUES.put(31, 46);
		DIALOGUES.put(47, 47);
		DIALOGUES.put(55, 48);
		DIALOGUES.put(59, 49);
	}
	
	/**
	 * First exchange list for Collob, Ferris, Pano.<br>
	 * <br>
	 * Npc ID, Parameter ID, Item IDs Array {2 require, 1 reward}
	 */
	private static final Map<Integer, Map<Integer, int[]>> EXCHANGE_1ST = new HashMap<>();
	{
		// Initialize temporary Map.
		Map<Integer, int[]> tempMap = new HashMap<>();
		
		// Collob Exchange List.
		EXCHANGE_1ST.put(COLLOB, tempMap);
		tempMap.put(1, new int[]
		{
			BLOOD_BASILISK,
			BLOOD_SUCCUBUS,
			BLOOD_DRAGON
		});
		tempMap.put(2, new int[]
		{
			SILVER_DRYAD,
			SILVER_UNDINE,
			SILVER_DRAGON
		});
		tempMap.put(3, new int[]
		{
			GOLD_GIANT,
			GOLD_WYRM,
			GOLD_DRAGON
		});
		tempMap.put(4, new int[]
		{
			GOLD_GIANT,
			GOLD_WYRM,
			BELETH_GOLD_DRAGON
		});
		
		// Ferris Exchange List.
		tempMap = new HashMap<>();
		
		EXCHANGE_1ST.put(FERRIS, tempMap);
		tempMap.put(1, new int[]
		{
			BLOOD_BASILISK,
			BLOOD_SUCCUBUS,
			BLOOD_DRAGON
		});
		tempMap.put(2, new int[]
		{
			SILVER_DRYAD,
			SILVER_UNDINE,
			SILVER_DRAGON
		});
		tempMap.put(3, new int[]
		{
			GOLD_GIANT,
			GOLD_WYRM,
			GOLD_DRAGON
		});
		tempMap.put(4, new int[]
		{
			BLOOD_BASILISK,
			BLOOD_SUCCUBUS,
			BELETH_BLOOD_DRAGON
		});
		
		// Pano Exchange List.
		tempMap = new HashMap<>();
		
		EXCHANGE_1ST.put(PANO, tempMap);
		tempMap.put(1, new int[]
		{
			BLOOD_BASILISK,
			BLOOD_SUCCUBUS,
			BLOOD_DRAGON
		});
		tempMap.put(2, new int[]
		{
			SILVER_UNDINE,
			SILVER_DRYAD,
			SILVER_DRAGON
		});
		tempMap.put(3, new int[]
		{
			GOLD_GIANT,
			GOLD_WYRM,
			GOLD_DRAGON
		});
		tempMap.put(4, new int[]
		{
			SILVER_DRYAD,
			SILVER_UNDINE,
			BELETH_SILVER_DRAGON
		});
	}
	
	/**
	 * First request list for Collob, Ferris, Pano.<br>
	 * <br>
	 * Npc ID, Parameter ID, Item IDs Array
	 */
	private static final Map<Integer, Map<Integer, int[]>> REQUEST_1ST = new HashMap<>();
	{
		// Initialize temporary Map.
		Map<Integer, int[]> tempMap = new HashMap<>();
		
		// Collob Request List.
		REQUEST_1ST.put(COLLOB, tempMap);
		tempMap.put(1, new int[]
		{
			BLOOD_SUCCUBUS
		});
		tempMap.put(2, new int[]
		{
			SILVER_UNDINE
		});
		tempMap.put(3, new int[]
		{
			GOLD_WYRM
		});
		tempMap.put(4, new int[]
		{
			GOLD_WYRM,
			GOLD_GIANT
		});
		
		// Ferris Request List.
		tempMap = new HashMap<>();
		
		REQUEST_1ST.put(FERRIS, tempMap);
		tempMap.put(1, new int[]
		{
			BLOOD_BASILISK
		});
		tempMap.put(2, new int[]
		{
			SILVER_DRYAD
		});
		tempMap.put(3, new int[]
		{
			GOLD_GIANT
		});
		tempMap.put(4, new int[]
		{
			BLOOD_BASILISK,
			BLOOD_SUCCUBUS
		});
		
		// Pano Request List.
		tempMap = new HashMap<>();
		
		REQUEST_1ST.put(PANO, tempMap);
		tempMap.put(1, new int[]
		{
			BLOOD_BASILISK
		});
		tempMap.put(2, new int[]
		{
			SILVER_DRYAD
		});
		tempMap.put(3, new int[]
		{
			GOLD_GIANT
		});
		tempMap.put(4, new int[]
		{
			SILVER_DRYAD,
			SILVER_UNDINE
		});
	}
	
	/**
	 * Second exchange list for Ralford, Stan, Duning.<br>
	 * <br>
	 * Npc ID, Parameter ID, Item IDs Array {2 require, 1 reward}
	 */
	private static final Map<Integer, Map<Integer, int[]>> EXCHANGE_2ND = new HashMap<>();
	{
		// Initialize temporary Map.
		Map<Integer, int[]> tempMap = new HashMap<>();
		
		// Ralford Exchange List.
		EXCHANGE_2ND.put(RALFORD, tempMap);
		tempMap.put(1, new int[]
		{
			GOLD_DRAKE,
			GOLD_KNIGHT,
			GOLD_WYRM
		});
		tempMap.put(2, new int[]
		{
			SILVER_GOLEM,
			SILVER_FAIRY,
			SILVER_DRYAD
		});
		tempMap.put(3, new int[]
		{
			SILVER_GOLEM,
			SILVER_FAIRY,
			SILVER_UNDINE
		});
		tempMap.put(4, new int[]
		{
			BLOOD_WEREWOLF,
			BLOOD_DREVANUL,
			MANAK_BLOOD_WEREWOLF
		});
		
		// Stan Exchange List.
		tempMap = new HashMap<>();
		
		EXCHANGE_2ND.put(STAN, tempMap);
		tempMap.put(1, new int[]
		{
			GOLD_KNIGHT,
			GOLD_DRAKE,
			GOLD_GIANT
		});
		tempMap.put(2, new int[]
		{
			BLOOD_WEREWOLF,
			BLOOD_DREVANUL,
			BLOOD_BASILISK
		});
		tempMap.put(3, new int[]
		{
			SILVER_FAIRY,
			SILVER_GOLEM,
			SILVER_DRYAD
		});
		tempMap.put(4, new int[]
		{
			SILVER_FAIRY,
			SILVER_GOLEM,
			MANAK_SILVER_DRYAD
		});
		
		// Duning Exchange List.
		tempMap = new HashMap<>();
		
		EXCHANGE_2ND.put(DUNING, tempMap);
		tempMap.put(1, new int[]
		{
			GOLD_DRAKE,
			GOLD_KNIGHT,
			GOLD_GIANT
		});
		tempMap.put(2, new int[]
		{
			BLOOD_DREVANUL,
			BLOOD_WEREWOLF,
			BLOOD_SUCCUBUS
		});
		tempMap.put(3, new int[]
		{
			SILVER_GOLEM,
			SILVER_FAIRY,
			SILVER_UNDINE
		});
		tempMap.put(4, new int[]
		{
			GOLD_KNIGHT,
			GOLD_DRAKE,
			MANAK_GOLD_GIANT
		});
	}
	
	/**
	 * Second request list for Ralford, Stan, Duning.<br>
	 * <br>
	 * Npc ID, Parameter ID, Item IDs Array
	 */
	private static final Map<Integer, Map<Integer, int[]>> REQUEST_2ND = new HashMap<>();
	{
		// Initialize temporary Map.
		Map<Integer, int[]> tempMap = new HashMap<>();
		
		// Ralford Request List.
		REQUEST_2ND.put(RALFORD, tempMap);
		tempMap.put(1, new int[]
		{
			GOLD_DRAKE
		});
		tempMap.put(2, new int[]
		{
			SILVER_FAIRY
		});
		tempMap.put(3, new int[]
		{
			SILVER_GOLEM
		});
		tempMap.put(4, new int[]
		{
			BLOOD_WEREWOLF,
			BLOOD_DREVANUL
		});
		
		// Stan Request List.
		tempMap = new HashMap<>();
		
		REQUEST_2ND.put(STAN, tempMap);
		tempMap.put(1, new int[]
		{
			GOLD_KNIGHT
		});
		tempMap.put(2, new int[]
		{
			BLOOD_WEREWOLF
		});
		tempMap.put(3, new int[]
		{
			SILVER_FAIRY
		});
		tempMap.put(4, new int[]
		{
			SILVER_FAIRY,
			SILVER_GOLEM
		});
		
		// Duning Request List.
		tempMap = new HashMap<>();
		
		REQUEST_2ND.put(DUNING, tempMap);
		tempMap.put(1, new int[]
		{
			GOLD_DRAKE
		});
		tempMap.put(2, new int[]
		{
			BLOOD_DREVANUL
		});
		tempMap.put(3, new int[]
		{
			SILVER_GOLEM
		});
		tempMap.put(4, new int[]
		{
			GOLD_DRAKE,
			GOLD_KNIGHT
		});
	}
	
	/**
	 * Third exchange list for Lorain, Page, Hagger.<br>
	 * <br>
	 * Npc ID, Parameter ID, Taken Item ID, Givin Item ID
	 */
	private static final Map<Integer, Map<Integer, IntIntHolder>> EXCHANGE_3RD = new HashMap<>();
	{
		// Initialize temporary Map.
		Map<Integer, IntIntHolder> tempMap = new HashMap<>();
		
		// Lorain Exchange List.
		EXCHANGE_3RD.put(LORAIN, tempMap);
		tempMap.put(1, new IntIntHolder(GOLD_WYVERN, GOLD_KNIGHT));
		tempMap.put(2, new IntIntHolder(SILVER_UNICORN, SILVER_GOLEM));
		tempMap.put(3, new IntIntHolder(BLOOD_MEDUSA, BLOOD_DREVANUL));
		tempMap.put(4, new IntIntHolder(GOLD_WYVERN, NIA_GOLD_WYVERN));
		
		// Page Exchange List.
		tempMap = new HashMap<>();
		
		EXCHANGE_3RD.put(PAGE, tempMap);
		tempMap.put(1, new IntIntHolder(GOLD_WYVERN, GOLD_KNIGHT));
		tempMap.put(2, new IntIntHolder(SILVER_UNICORN, SILVER_FAIRY));
		tempMap.put(3, new IntIntHolder(BLOOD_MEDUSA, BLOOD_WEREWOLF));
		tempMap.put(4, new IntIntHolder(BLOOD_MEDUSA, NIA_BLOOD_MEDUSA));
		
		// Hagger Exchange List.
		tempMap = new HashMap<>();
		
		EXCHANGE_3RD.put(HAGGER, tempMap);
		tempMap.put(1, new IntIntHolder(GOLD_WYVERN, GOLD_DRAKE));
		tempMap.put(2, new IntIntHolder(SILVER_UNICORN, SILVER_GOLEM));
		tempMap.put(3, new IntIntHolder(BLOOD_MEDUSA, BLOOD_WEREWOLF));
		tempMap.put(4, new IntIntHolder(SILVER_UNICORN, NIA_SILVER_FAIRY));
	}
	
	/**
	 * Third request list for Lorain, Page, Hagger.<br>
	 * <br>
	 * Request Item ID by parameter 40, 30, 20, 10.
	 */
	private static final Map<Integer, int[]> REQUEST_3RD = new HashMap<>();
	{
		REQUEST_3RD.put(LORAIN, new int[]
		{
			GOLD_WYVERN,
			BLOOD_MEDUSA,
			SILVER_UNICORN,
			GOLD_WYVERN
		});
		REQUEST_3RD.put(PAGE, new int[]
		{
			BLOOD_MEDUSA,
			BLOOD_MEDUSA,
			SILVER_UNICORN,
			GOLD_WYVERN
		});
		REQUEST_3RD.put(HAGGER, new int[]
		{
			SILVER_UNICORN,
			BLOOD_MEDUSA,
			SILVER_UNICORN,
			GOLD_WYVERN
		});
	}
	
	public Q336_CoinsOfMagic()
	{
		super(336, "Coins of Magic");
		
		setItemsIds(COIN_DIAGRAM, KALDIS_GOLD_DRAGON, COIN_COLLECTOR_MEMBERSHIP_1, COIN_COLLECTOR_MEMBERSHIP_2, COIN_COLLECTOR_MEMBERSHIP_3);
		
		addQuestStart(SORINT);
		addTalkId(PANO, COLLOB, RALFORD, HAGGER, STAN, SORINT, LORAIN, DUNING, PAGE, BERNARD, FERRIS);
		
		for (Integer npcId : DROPLIST_FIRST.keySet())
			addMyDying(npcId);
		
		for (Integer npcId : DROPLIST_SECOND.keySet())
			addMyDying(npcId);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		int flag = st.getInteger("flag");
		
		final int npcId = npc.getNpcId();
		
		if (event.equalsIgnoreCase("30232-05.htm"))
		{
			if (!player.getInventory().hasItem(COIN_DIAGRAM))
				giveItems(player, COIN_DIAGRAM, 1);
			
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("state", 1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("upgrade"))
		{
			if (player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_3))
			{
				if (player.getInventory().hasItems(BLOOD_WEREWOLF, BLOOD_DREVANUL, GOLD_KNIGHT, GOLD_DRAKE, SILVER_FAIRY, SILVER_GOLEM))
				{
					htmltext = "30232-16.htm";
					st.setCond(9);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, COIN_COLLECTOR_MEMBERSHIP_3, -1);
					takeItems(player, BLOOD_WEREWOLF, 1);
					takeItems(player, BLOOD_DREVANUL, 1);
					takeItems(player, GOLD_KNIGHT, 1);
					takeItems(player, GOLD_DRAKE, 1);
					takeItems(player, SILVER_FAIRY, 1);
					takeItems(player, SILVER_GOLEM, 1);
					giveItems(player, COIN_COLLECTOR_MEMBERSHIP_2, 1);
				}
				else
				{
					htmltext = "30232-13.htm";
					st.setCond(8);
				}
			}
			else if (player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_2))
			{
				if (player.getInventory().hasItems(BLOOD_BASILISK, BLOOD_SUCCUBUS, GOLD_GIANT, GOLD_WYRM, SILVER_DRYAD, SILVER_UNDINE))
				{
					htmltext = "30232-17.htm";
					st.setCond(11);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, COIN_COLLECTOR_MEMBERSHIP_2, -1);
					takeItems(player, BLOOD_BASILISK, 1);
					takeItems(player, BLOOD_SUCCUBUS, 1);
					takeItems(player, GOLD_GIANT, 1);
					takeItems(player, GOLD_WYRM, 1);
					takeItems(player, SILVER_DRYAD, 1);
					takeItems(player, SILVER_UNDINE, 1);
					giveItems(player, COIN_COLLECTOR_MEMBERSHIP_1, 1);
				}
				else
				{
					htmltext = "30232-14.htm";
					st.setCond(10);
				}
			}
			else if (player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_1))
				htmltext = "30232-15.htm";
		}
		else if (event.equalsIgnoreCase("30232-22.htm"))
			st.setCond(4);
		else if (event.equalsIgnoreCase("30232-23.htm"))
			st.setCond(5);
		else if (event.startsWith("exchange "))
		{
			final int item = Integer.parseInt(event.replace("exchange ", ""));
			if (item == 206 && player.getInventory().hasItems(BELETH_BLOOD_DRAGON, SILVER_DRAGON) && player.getInventory().getItemCount(GOLD_WYRM) > 12)
			{
				htmltext = "30232-24a.htm";
				takeItems(player, BELETH_BLOOD_DRAGON, 1);
				takeItems(player, SILVER_DRAGON, 1);
				takeItems(player, GOLD_WYRM, 13);
				giveItems(player, DEMON_STAFF, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 233 && player.getInventory().hasItems(BLOOD_DRAGON, GOLD_GIANT, BELETH_GOLD_DRAGON, SILVER_DRYAD))
			{
				htmltext = "30232-24b.htm";
				takeItems(player, BLOOD_DRAGON, 1);
				takeItems(player, GOLD_GIANT, 1);
				takeItems(player, BELETH_GOLD_DRAGON, 1);
				takeItems(player, SILVER_DRYAD, 1);
				giveItems(player, DARK_SCREAMER, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 303 && player.getInventory().getItemCount(BLOOD_BASILISK) > 1 && player.getInventory().hasItems(BLOOD_SUCCUBUS, GOLD_DRAGON, BELETH_SILVER_DRAGON))
			{
				htmltext = "30232-24c.htm";
				takeItems(player, BLOOD_BASILISK, 2);
				takeItems(player, BLOOD_SUCCUBUS, 1);
				takeItems(player, GOLD_DRAGON, 1);
				takeItems(player, BELETH_SILVER_DRAGON, 1);
				giveItems(player, WIDOW_MAKER, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 132 && player.getInventory().hasItems(BLOOD_DRAGON, GOLD_DRAGON, SILVER_DRAGON, SILVER_UNDINE))
			{
				htmltext = "30232-24d.htm";
				takeItems(player, BLOOD_DRAGON, 1);
				takeItems(player, GOLD_DRAGON, 1);
				takeItems(player, SILVER_DRAGON, 1);
				takeItems(player, SILVER_UNDINE, 1);
				giveItems(player, SWORD_OF_LIMIT, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 2435 && player.getInventory().hasItem(MANAK_GOLD_GIANT))
			{
				htmltext = "30232-24e.htm";
				takeItems(player, MANAK_GOLD_GIANT, 1);
				giveItems(player, DEMON_BOOTS, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 472 && player.getInventory().hasItems(SILVER_DRYAD, MANAK_SILVER_DRYAD))
			{
				htmltext = "30232-24f.htm";
				takeItems(player, SILVER_DRYAD, 1);
				takeItems(player, MANAK_SILVER_DRYAD, 1);
				giveItems(player, DEMON_STOCKINGS, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 2459 && player.getInventory().hasItem(MANAK_GOLD_GIANT))
			{
				htmltext = "30232-24g.htm";
				takeItems(player, MANAK_GOLD_GIANT, 1);
				giveItems(player, DEMON_GLOVES, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 2414 && player.getInventory().hasItems(MANAK_BLOOD_WEREWOLF, GOLD_GIANT, GOLD_WYRM))
			{
				htmltext = "30232-24h.htm";
				takeItems(player, MANAK_BLOOD_WEREWOLF, 1);
				takeItems(player, GOLD_GIANT, 1);
				takeItems(player, GOLD_WYRM, 1);
				giveItems(player, FULL_PLATE_HELMET, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 852 && player.getInventory().getItemCount(NIA_BLOOD_MEDUSA) > 1 && player.getInventory().getItemCount(GOLD_DRAKE) > 1 && player.getInventory().getItemCount(BLOOD_DREVANUL) > 1 && player.getInventory().getItemCount(GOLD_KNIGHT) > 2)
			{
				htmltext = "30232-24i.htm";
				takeItems(player, NIA_BLOOD_MEDUSA, 2);
				takeItems(player, GOLD_DRAKE, 2);
				takeItems(player, BLOOD_DREVANUL, 2);
				takeItems(player, GOLD_KNIGHT, 3);
				giveItems(player, MOONSTONE_EARRING, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 855 && player.getInventory().getItemCount(NIA_BLOOD_MEDUSA) > 6 && player.getInventory().getItemCount(GOLD_KNIGHT) > 4 && player.getInventory().getItemCount(BLOOD_DREVANUL) > 4 && player.getInventory().getItemCount(SILVER_GOLEM) > 4)
			{
				htmltext = "30232-24j.htm";
				takeItems(player, NIA_BLOOD_MEDUSA, 7);
				takeItems(player, GOLD_KNIGHT, 5);
				takeItems(player, BLOOD_DREVANUL, 5);
				takeItems(player, SILVER_GOLEM, 5);
				giveItems(player, NASSEN_EARRING, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 886 && player.getInventory().getItemCount(NIA_GOLD_WYVERN) > 4 && player.getInventory().getItemCount(SILVER_GOLEM) > 3 && player.getInventory().getItemCount(GOLD_DRAKE) > 3 && player.getInventory().getItemCount(BLOOD_DREVANUL) > 3)
			{
				htmltext = "30232-24k.htm";
				takeItems(player, NIA_GOLD_WYVERN, 5);
				takeItems(player, SILVER_GOLEM, 4);
				takeItems(player, GOLD_DRAKE, 4);
				takeItems(player, BLOOD_DREVANUL, 4);
				giveItems(player, RING_OF_BINDING, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else if (item == 916 && player.getInventory().getItemCount(NIA_SILVER_FAIRY) > 4 && player.getInventory().getItemCount(SILVER_FAIRY) > 2 && player.getInventory().getItemCount(GOLD_KNIGHT) > 2 && player.getInventory().getItemCount(BLOOD_DREVANUL) > 2)
			{
				htmltext = "30232-24l.htm";
				takeItems(player, NIA_SILVER_FAIRY, 5);
				takeItems(player, SILVER_FAIRY, 3);
				takeItems(player, GOLD_KNIGHT, 3);
				takeItems(player, BLOOD_DREVANUL, 3);
				giveItems(player, NECKLACE_OF_PROTECTION, 1);
				playSound(player, SOUND_MIDDLE);
			}
			else
				htmltext = "30232-24.htm";
		}
		else if (event.equalsIgnoreCase("30232-18a.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30702-03.htm") || event.equalsIgnoreCase("30702-04.htm"))
		{
			st.setCond(2);
			st.set("state", 2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30702-06.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("reply_1"))
		{
			htmltext = npcId + "-02.htm";
			st.set("param2", 11);
			st.set("level", getExchangerLevel(npcId));
		}
		else if (event.equalsIgnoreCase("reply_2"))
		{
			htmltext = npcId + "-03.htm";
			st.set("param2", 21);
			st.set("level", getExchangerLevel(npcId));
		}
		else if (event.equalsIgnoreCase("reply_3"))
		{
			htmltext = npcId + "-04.htm";
			st.set("param2", 31);
			st.set("level", getExchangerLevel(npcId));
		}
		else if (event.equalsIgnoreCase("reply_4"))
		{
			htmltext = npcId + "-05.htm";
			st.set("param2", 42);
			st.set("level", getExchangerLevel(npcId));
		}
		else if (event.equalsIgnoreCase("reply_6") || event.equalsIgnoreCase("reply_7") || event.equalsIgnoreCase("reply_8"))
		{
			final int index = (Integer.parseInt(event.replace("reply_", "")) + 4) % 10;
			final int param2 = st.getInteger("param2");
			final int level = st.getInteger("level");
			final int point = index + 1;
			final int multiplier = MULTIPLIER_3RD.get(level)[index];
			int itemId = -1;
			int page = 11;
			boolean trigger = false;
			
			if (level == 3)
			{
				if (npcId == PAGE)
				{
					if (index == 1)
						page = 12;
					else if (index == 2)
						page = 13;
				}
				
				final int[] request = REQUEST_3RD.get(npcId);
				if (param2 > 40)
					itemId = request[0];
				else if (param2 > 30)
					itemId = request[1];
				else if (param2 > 20)
					itemId = request[2];
				else
					itemId = request[3];
				
				final int requere = multiplier * (param2 % 10);
				if (player.getInventory().getItemCount(itemId) >= requere)
				{
					trigger = true;
					takeItems(player, itemId, requere);
				}
				else
					htmltext = npcId + "-10.htm";
			}
			else if (level == 2)
			{
				boolean exists = true;
				final int[] items = REQUEST_2ND.get(npcId).get(param2 / 10);
				final int requere = param2 > 40 ? multiplier : multiplier * (param2 % 10);
				for (int i = 0; i < items.length; i++)
				{
					if (player.getInventory().getItemCount(items[i]) < requere)
						exists = false;
				}
				
				if (exists)
				{
					for (int i = 0; i < items.length; i++)
						takeItems(player, items[i], requere);
					
					trigger = true;
				}
				else
					htmltext = npcId + "-10.htm";
			}
			else if (level == 1)
			{
				boolean exists = true;
				final int[] items = REQUEST_1ST.get(npcId).get(param2 / 10);
				final int requere = param2 > 40 ? multiplier : multiplier * (param2 % 10);
				for (int i = 0; i < items.length; i++)
				{
					if (player.getInventory().getItemCount(items[i]) < requere)
						exists = false;
				}
				
				if (exists)
				{
					for (int i = 0; i < items.length; i++)
						takeItems(player, items[i], requere);
					
					trigger = true;
				}
				else
					htmltext = npcId + "-10.htm";
			}
			
			if (trigger)
			{
				int param1 = Rnd.get(3) + 1;
				param1 += (Rnd.get(3) + 1) * 4;
				param1 += (Rnd.get(3) + 1) * 16;
				st.set("param1", param1);
				st.set("flag", 1);
				st.set("point", point);
				htmltext = npcId + "-" + page + ".htm";
			}
		}
		else if (event.equalsIgnoreCase("reply_10") || event.equalsIgnoreCase("reply_11") || event.equalsIgnoreCase("reply_12"))
		{
			final int index = Integer.parseInt(event.replace("reply_", "")) % 10;
			final int param2 = st.getInteger("param2");
			final int level = st.getInteger("level");
			final int multiplier = MULTIPLIER_2ND.get(level)[index];
			final int count = COUNTS.get(index);
			final int trigger = param2 / 10;
			
			if (trigger > 0)
			{
				final int neededAmount = multiplier * (param2 % 10);
				
				if (level == 3)
				{
					final IntIntHolder data = EXCHANGE_3RD.get(npcId).get(trigger);
					if (player.getInventory().getItemCount(data.getId()) >= neededAmount)
					{
						htmltext = npcId + "-07.htm";
						takeItems(player, data.getId(), neededAmount);
						giveItems(player, data.getValue(), count);
						playSound(player, SOUND_MIDDLE);
					}
					else
						htmltext = npcId + "-10.htm";
				}
				else if (level == 2)
				{
					final int[] data = EXCHANGE_2ND.get(npcId).get(trigger);
					if (player.getInventory().getItemCount(data[0]) >= neededAmount && player.getInventory().getItemCount(data[1]) >= neededAmount)
					{
						htmltext = npcId + "-07.htm";
						takeItems(player, data[0], neededAmount);
						takeItems(player, data[1], neededAmount);
						giveItems(player, data[2], count);
						playSound(player, SOUND_MIDDLE);
					}
					else
						htmltext = npcId + "-10.htm";
				}
				else if (level == 1)
				{
					final int[] data = EXCHANGE_1ST.get(npcId).get(trigger);
					if (player.getInventory().getItemCount(data[0]) >= neededAmount && player.getInventory().getItemCount(data[1]) >= neededAmount)
					{
						htmltext = npcId + "-07.htm";
						takeItems(player, data[0], neededAmount);
						takeItems(player, data[1], neededAmount);
						giveItems(player, data[2], count);
						playSound(player, SOUND_MIDDLE);
					}
					else
						htmltext = npcId + "-10.htm";
				}
			}
		}
		else if (event.equalsIgnoreCase("reply_13") && flag == 1)
		{
			st.set("flag", 16);
			htmltext = npcId + "-14.htm";
		}
		else if (event.equalsIgnoreCase("reply_14") && flag == 1)
		{
			st.set("flag", 32);
			htmltext = npcId + "-15.htm";
		}
		else if (event.equalsIgnoreCase("reply_15") && flag == 1)
		{
			st.set("flag", 48);
			htmltext = npcId + "-16.htm";
		}
		else if (event.equalsIgnoreCase("reply_16"))
		{
			st.set("flag", flag + 4);
			htmltext = npcId + "-17.htm";
		}
		else if (event.equalsIgnoreCase("reply_17"))
		{
			st.set("flag", flag + 8);
			htmltext = npcId + "-18.htm";
		}
		else if (event.equalsIgnoreCase("reply_18"))
		{
			st.set("flag", flag + 12);
			htmltext = npcId + "-19.htm";
		}
		else if (event.equalsIgnoreCase("reply_19") || event.equalsIgnoreCase("reply_20") || event.equalsIgnoreCase("reply_21"))
		{
			final int multiplier = (Integer.parseInt(event.replace("reply_", "")) + 2) % 20;
			final int level = st.getInteger("level");
			flag += multiplier;
			int point = st.getInteger("point");
			int param1 = st.getInteger("param1");
			int param2 = st.getInteger("param2");
			int param3 = 0;
			
			if (param1 == flag && point >= 0)
			{
				point = 0;
				param1 = 0;
				
				if (level == 3)
					giveItems(player, EXCHANGE_3RD.get(npcId).get(param2 / 10).getValue(), 1);
				else if (level == 2)
					giveItems(player, EXCHANGE_2ND.get(npcId).get(param2 / 10)[2], 1);
				else if (level == 1)
					giveItems(player, EXCHANGE_1ST.get(npcId).get(param2 / 10)[2], 1);
				
				htmltext = npcId + "-20.htm";
			}
			else if (point == 0)
			{
				htmltext = npcId + "-" + DIALOGUES.get(param1) + ".htm";
				param1 = 0;
			}
			else
			{
				int i0 = param1 % 4;
				int i1 = param1 / 4;
				int i2 = i1 / 4;
				i1 = i1 % 4;
				int i3 = flag % 4;
				int i4 = flag / 4;
				int i5 = i4 / 4;
				i4 = i4 % 4;
				
				if (i0 == i3)
					param3 += 1;
				
				if (i1 == i4)
					param3 += 1;
				
				if (i2 == i5)
					param3 += 1;
				
				if (param3 == 1)
					htmltext = npcId + "-50.htm";
				else if (param3 == 2)
					htmltext = npcId + "-51.htm";
				else if (param3 == 0)
					htmltext = npcId + "-52.htm";
				
				flag = 1;
				point -= 1;
			}
			
			st.set("flag", flag);
			st.set("point", point);
			st.set("param1", param1);
			st.set("param2", param2);
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
				htmltext = (player.getStatus().getLevel() < 40) ? "30232-01.htm" : "30232-02.htm";
				break;
			
			case STARTED:
				final int state = st.getInteger("state");
				final int npcId = npc.getNpcId();
				switch (npcId)
				{
					case SORINT:
						if (state == 1 || state == 2)
						{
							if (player.getInventory().hasItem(KALDIS_GOLD_DRAGON))
							{
								htmltext = "30232-07.htm";
								st.setCond(4);
								st.set("state", 3);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, COIN_DIAGRAM, -1);
								takeItems(player, KALDIS_GOLD_DRAGON, -1);
								giveItems(player, COIN_COLLECTOR_MEMBERSHIP_3, 1);
							}
							else
								htmltext = "30232-06.htm";
						}
						else if (state == 3)
						{
							if (player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_3))
								htmltext = "30232-10.htm";
							else if (player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_2))
								htmltext = "30232-11.htm";
							else if (player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_1))
								htmltext = "30232-12.htm";
						}
						break;
					
					case BERNARD:
						if (state == 1)
							htmltext = "30702-01.htm";
						else if (state == 2)
							htmltext = "30702-02a.htm";
						else
							htmltext = "30702-05.htm";
						break;
					
					case LORAIN, PAGE, HAGGER:
						if (isCollector(player, 3))
						{
							htmltext = npcId + "-01.htm";
							unset(st);
						}
						break;
					
					case RALFORD, STAN, DUNING:
						if (isCollector(player, 2))
						{
							htmltext = npcId + "-01.htm";
							unset(st);
						}
						else if (player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_3))
							htmltext = npcId + "-54.htm";
						break;
					
					case COLLOB, FERRIS, PANO:
						if (isCollector(player, 1))
						{
							htmltext = npcId + "-01.htm";
							unset(st);
						}
						else if (player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_3) || player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_2))
							htmltext = npcId + "-54.htm";
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
		if (player == null)
			return;
		
		if (DROPLIST_FIRST.containsKey(npc.getNpcId()))
		{
			final QuestState st = getRandomPartyMember(player, npc, 2);
			if (st == null)
				return;
			
			final IntIntHolder drop = DROPLIST_FIRST.get(npc.getNpcId());
			if (dropItems(st.getPlayer(), drop.getId(), 1, 1, drop.getValue()))
				st.setCond(3);
		}
		else if (DROPLIST_SECOND.containsKey(npc.getNpcId()))
		{
			final QuestState st = getRandomPartyMember(player, npc, "state", "3");
			if (st == null)
				return;
			
			final IntIntHolder drop = DROPLIST_SECOND.get(npc.getNpcId());
			dropItems(st.getPlayer(), drop.getId(), 1, 0, drop.getValue());
		}
	}
	
	/**
	 * @param player : The Player to check.
	 * @param level : The level to check.
	 * @return true if the given {@link QuestState} can handle the tested level, based on inventory content.
	 */
	private static boolean isCollector(Player player, int level)
	{
		switch (level)
		{
			case 1:
				return player.getInventory().hasItem(COIN_COLLECTOR_MEMBERSHIP_1);
			
			case 2:
				return player.getInventory().hasAtLeastOneItem(COIN_COLLECTOR_MEMBERSHIP_1, COIN_COLLECTOR_MEMBERSHIP_2);
			
			default:
				return player.getInventory().hasAtLeastOneItem(COIN_COLLECTOR_MEMBERSHIP_1, COIN_COLLECTOR_MEMBERSHIP_2, COIN_COLLECTOR_MEMBERSHIP_3);
		}
	}
	
	/**
	 * Reset secondary variables for a given {@link QuestState}.
	 * @param st : The QuestState to affect.
	 */
	private static void unset(QuestState st)
	{
		st.unset("flag");
		st.unset("level");
		st.unset("point");
		st.unset("param1");
		st.unset("param2");
	}
	
	/**
	 * @param npcId : The npcId to check.
	 * @return the associated exchanger grade based on npcId.
	 */
	private static int getExchangerLevel(int npcId)
	{
		switch (npcId)
		{
			case LORAIN, PAGE, HAGGER:
				return 3;
			
			case RALFORD, STAN, DUNING:
				return 2;
			
			case COLLOB, FERRIS, PANO:
				return 1;
			
			default:
				return 0;
		}
	}
}