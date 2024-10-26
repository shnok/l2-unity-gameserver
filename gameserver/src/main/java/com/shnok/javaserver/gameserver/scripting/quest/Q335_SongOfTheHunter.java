package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q335_SongOfTheHunter extends Quest
{
	private static final String QUEST_NAME = "Q335_SongOfTheHunter";
	
	// Npc
	private static final int GREY = 30744;
	private static final int TOR = 30745;
	private static final int CYBELLIN = 30746;
	
	// Items
	private static final int ADENA = 57;
	
	// Quest items
	private static final int CYBELLIN_DAGGER = 3471;
	private static final int LICENSE_1 = 3692;
	private static final int LICENSE_2 = 3693;
	private static final int LAUREL_LEAF_PIN = 3694;
	private static final int TEST_INSTRUCTIONS_1 = 3695;
	private static final int TEST_INSTRUCTIONS_2 = 3696;
	private static final int CYBELLIN_REQUEST = 3697;
	
	// Grey's request
	private static final int BASILISK_SCALE = 3709;
	private static final int KARUT_WEED = 3710;
	private static final int HAKA_HEAD = 3711;
	private static final int JAKA_HEAD = 3712;
	private static final int MARKA_HEAD = 3713;
	private static final int ALEPH_SKIN = 3714;
	private static final int INDIGO_RUNESTONE = 3715;
	private static final int SPORESEA_SEED = 3716;
	private static final int ORC_TOTEM = 3717;
	private static final int TRISALIM_SILK = 3718;
	private static final int AMBROSIUS_FRUIT = 3719;
	private static final int BALEFIRE_CRYSTAL = 3720;
	private static final int IMPERIAL_ARROWHEAD = 3721;
	private static final int ATHU_HEAD = 3722;
	private static final int LANKA_HEAD = 3723;
	private static final int TRISKA_HEAD = 3724;
	private static final int MOTURA_HEAD = 3725;
	private static final int KALATH_HEAD = 3726;
	
	// Tor's request - Request items
	private static final int REQUEST_1ST_1C = 3727;
	private static final int REQUEST_1ST_2C = 3728;
	private static final int REQUEST_1ST_3C = 3729;
	private static final int REQUEST_1ST_4C = 3730;
	private static final int REQUEST_1ST_5C = 3731;
	private static final int REQUEST_1ST_6C = 3732;
	private static final int REQUEST_1ST_7C = 3733;
	private static final int REQUEST_1ST_8C = 3734;
	private static final int REQUEST_1ST_9C = 3735;
	private static final int REQUEST_1ST_10C = 3736;
	private static final int REQUEST_1ST_11C = 3737;
	private static final int REQUEST_1ST_12C = 3738;
	private static final int REQUEST_1ST_1B = 3739;
	private static final int REQUEST_1ST_2B = 3740;
	private static final int REQUEST_1ST_3B = 3741;
	private static final int REQUEST_1ST_4B = 3742;
	private static final int REQUEST_1ST_5B = 3743;
	private static final int REQUEST_1ST_6B = 3744;
	private static final int REQUEST_1ST_1A = 3745;
	private static final int REQUEST_1ST_2A = 3746;
	private static final int REQUEST_1ST_3A = 3747;
	private static final int REQUEST_2ND_1C = 3748;
	private static final int REQUEST_2ND_2C = 3749;
	private static final int REQUEST_2ND_3C = 3750;
	private static final int REQUEST_2ND_4C = 3751;
	private static final int REQUEST_2ND_5C = 3752;
	private static final int REQUEST_2ND_6C = 3753;
	private static final int REQUEST_2ND_7C = 3754;
	private static final int REQUEST_2ND_8C = 3755;
	private static final int REQUEST_2ND_9C = 3756;
	private static final int REQUEST_2ND_10C = 3757;
	private static final int REQUEST_2ND_11C = 3758;
	private static final int REQUEST_2ND_12C = 3759;
	private static final int REQUEST_2ND_1B = 3760;
	private static final int REQUEST_2ND_2B = 3761;
	private static final int REQUEST_2ND_3B = 3762;
	private static final int REQUEST_2ND_4B = 3763;
	private static final int REQUEST_2ND_5B = 3764;
	private static final int REQUEST_2ND_6B = 3765;
	private static final int REQUEST_2ND_1A = 3766;
	private static final int REQUEST_2ND_2A = 3767;
	private static final int REQUEST_2ND_3A = 3768;
	
	// Tor's request - Drop items
	private static final int CHARM_OF_KADESH = 3769;
	private static final int TIMAK_JADE_NECKLACE = 3770;
	private static final int ENCHANTED_GOLEM_SHARD = 3771;
	private static final int GIANT_MONSTER_EYE_MEAT = 3772;
	private static final int DIRE_WYRM_EGG = 3773;
	private static final int GRD_BASILISK_TALON = 3774;
	private static final int REVENANT_CHAINS = 3775;
	private static final int WINDSUS_TUSK = 3776;
	private static final int GRANDIS_SKULL = 3777;
	private static final int TAIK_OBSIDIAN_AMULET = 3778;
	private static final int KARUL_BUGBEAR_HEAD = 3779;
	private static final int TAMLIN_IVORY_CHARM = 3780;
	private static final int FANG_OF_NARAK = 3781;
	private static final int ENCHANTED_GARGOYLE_HORN = 3782;
	private static final int COILED_SERPENT_TOTEM = 3783;
	private static final int TOTEM_OF_KADESH = 3784;
	private static final int KAIKI_HEAD = 3785;
	private static final int KRONBE_VENOM_SAC = 3786;
	private static final int EVA_CHARM = 3787;
	private static final int TITAN_TABLET = 3788;
	private static final int BOOK_OF_SHUNAIMAN = 3789;
	private static final int ROTTING_TREE_SPORES = 3790;
	private static final int TRISALIM_VENOM_SAC = 3791;
	private static final int TAIK_ORC_TOTEM = 3792;
	private static final int HARIT_BARBED_NECKLACE = 3793;
	private static final int COIN_OF_OLD_EMPIRE = 3794;
	private static final int SKIN_OF_FARCRAN = 3795;
	private static final int TEMPEST_SHARD = 3796;
	private static final int TSUNAMI_SHARD = 3797;
	private static final int SATYR_MANE = 3798;
	private static final int HAMADRYAD_SHARD = 3799;
	private static final int VANOR_SILENOS_MANE = 3800;
	private static final int TALK_BUGBEAR_TOTEM = 3801;
	private static final int OKUN_HEAD = 3802;
	private static final int KAKRAN_HEAD = 3803;
	private static final int NARCISSUS_SOULSTONE = 3804;
	private static final int DEPRIVE_EYE = 3805;
	private static final int UNICORN_HORN = 3806;
	private static final int KERUNO_GOLD_MANE = 3807;
	private static final int SKULL_OF_EXECUTED = 3808;
	private static final int BUST_OF_TRAVIS = 3809;
	private static final int SWORD_OF_CADMUS = 3810;
	
	// Cybellin's request - Blood Crystals
	private static final int BLOOD_CRYSTAL_01 = 3698;
	private static final int BLOOD_CRYSTAL_02 = 3699;
	private static final int BLOOD_CRYSTAL_03 = 3700;
	private static final int BLOOD_CRYSTAL_04 = 3701;
	private static final int BLOOD_CRYSTAL_05 = 3702;
	private static final int BLOOD_CRYSTAL_06 = 3703;
	private static final int BLOOD_CRYSTAL_07 = 3704;
	private static final int BLOOD_CRYSTAL_08 = 3705;
	private static final int BLOOD_CRYSTAL_09 = 3706;
	private static final int BLOOD_CRYSTAL_10 = 3707;
	private static final int BROKEN_BLOOD = 3708;
	
	// Monsters
	private static final int BREKA_ORC_SHAMAN = 20269;
	private static final int BREKA_ORC_WARRIOR = 20271;
	private static final int GUARDIAN_BASILISK = 20550;
	private static final int FETTERED_SOUL = 20552;
	private static final int WINDSUS = 20553;
	private static final int GRANDIS = 20554;
	private static final int GIANT_FUNGUS = 20555;
	private static final int GIANT_MONSTEREYE = 20556;
	private static final int DIRE_WYRM = 20557;
	private static final int ROTTING_TREE = 20558;
	private static final int TRISALIM_SPIDER = 20560;
	private static final int TRISALIM_TARANTULA = 20561;
	private static final int SPORE_ZOMBIE = 20562;
	private static final int MANASHEN_GARGOYLE = 20563;
	private static final int ENCHANTED_STONE_GOLEM = 20565;
	private static final int ENCHANTED_GARGOYLE = 20567;
	private static final int TARLK_BUGBEAR_WARRIOR = 20571;
	private static final int LETO_LIZARDMAN_ARCHER = 20578;
	private static final int LETO_LIZARDMAN_SOLDIER = 20579;
	private static final int LETO_LIZARDMAN_SHAMAN = 20581;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int TIMAK_ORC_WARRIOR = 20586;
	private static final int TIMAK_ORC_OVERLORD = 20588;
	private static final int FLINE = 20589;
	private static final int LIELE = 20590;
	private static final int VALLEY_TREANT = 20591;
	private static final int SATYR = 20592;
	private static final int UNICORN = 20593;
	private static final int FOREST_RUNNER = 20594;
	private static final int VALLEY_TREANT_ELDER = 20597;
	private static final int SATYR_ELDER = 20598;
	private static final int UNICORN_ELDER = 20599;
	private static final int KARUL_BUGBEAR = 20600;
	private static final int TAMLIN_ORC = 20601;
	private static final int TAMLIN_ORC_ARCHER = 20602;
	private static final int KRONBE_SPIDER = 20603;
	private static final int TAIK_ORC_ARCHER = 20631;
	private static final int TAIK_ORC_WARRIOR = 20632;
	private static final int TAIK_ORC_SHAMAN = 20633;
	private static final int TAIK_ORC_CAPTAIN = 20634;
	private static final int MIRROR = 20639;
	private static final int HARIT_LIZARDMAN_GRUNT = 20641;
	private static final int HARIT_LIZARDMAN_ARCHER = 20642;
	private static final int HARIT_LIZARDMAN_WARRIOR = 20643;
	private static final int GRAVE_WANDERER = 20659;
	private static final int ARCHER_OF_GREED = 20660;
	private static final int HATAR_RATMAN_THIEF = 20661;
	private static final int HATAR_RATMAN_BOSS = 20662;
	private static final int DEPRIVE = 20664;
	private static final int FARCRAN = 20667;
	private static final int TAIRIM = 20675;
	private static final int JUDGE_OF_MARSH = 20676;
	private static final int VANOR_SILENOS_GRUNT = 20682;
	private static final int VANOR_SILENOS_SCOUT = 20683;
	private static final int VANOR_SILENOS_WARRIOR = 20684;
	private static final int VANOR_SILENOS_CHIEFTAIN = 20686;
	private static final int BREKA_OVERLORD_HAKA = 27140;
	private static final int BREKA_OVERLORD_JAKA = 27141;
	private static final int BREKA_OVERLORD_MARKA = 27142;
	private static final int WINDSUS_ALEPH = 27143;
	private static final int TARLK_RAIDER_ATHU = 27144;
	private static final int TARLK_RAIDER_LANKA = 27145;
	private static final int TARLK_RAIDER_TRISKA = 27146;
	private static final int TARLK_RAIDER_MOTURA = 27147;
	private static final int TARLK_RAIDER_KALATH = 27148;
	private static final int GREMLIN_FILCHER = 27149;
	private static final int BLACK_LEGION_STORMTROOPER = 27150;
	private static final int LETO_SHAMAN_KETZ = 27156;
	private static final int LETO_CHIEF_NARAK = 27157;
	private static final int TIMAK_RAIDER_KAIKEE = 27158;
	private static final int TIMAK_OVERLORD_OKUN = 27159;
	private static final int GOK_MAGOK = 27160;
	private static final int TAIK_OVERLORD_KAKRAN = 27161;
	private static final int HATAR_CHIEFTAIN_KUBEL = 27162;
	private static final int VANOR_ELDER_KERUNOS = 27163;
	private static final int KARUL_CHIEF_OROOTO = 27164;
	
	/**
	 * Grey's test 1 and 2 requirements.
	 */
	class GreyRequirement
	{
		private final int[] _ids;
		private final int _required;
		
		/**
		 * @param ids : Required item ID(s).
		 * @param required : Required item amount.
		 */
		GreyRequirement(int required, int... ids)
		{
			_ids = ids;
			_required = required;
		}
		
		final int[] getIds()
		{
			return _ids;
		}
		
		final int getRequired()
		{
			return _required;
		}
	}
	
	/**
	 * Requirements for Grey's test 1 - Apprentice become Hunter license 1.
	 */
	private static final List<GreyRequirement> GREY_TEST_1_REQUIREMENTS = new ArrayList<>(6);
	{
		GREY_TEST_1_REQUIREMENTS.add(new GreyRequirement(40, BASILISK_SCALE));
		GREY_TEST_1_REQUIREMENTS.add(new GreyRequirement(20, KARUT_WEED));
		GREY_TEST_1_REQUIREMENTS.add(new GreyRequirement(1, HAKA_HEAD, JAKA_HEAD, MARKA_HEAD));
		GREY_TEST_1_REQUIREMENTS.add(new GreyRequirement(1, ALEPH_SKIN));
		GREY_TEST_1_REQUIREMENTS.add(new GreyRequirement(20, INDIGO_RUNESTONE));
		GREY_TEST_1_REQUIREMENTS.add(new GreyRequirement(30, SPORESEA_SEED));
	}
	
	/**
	 * Requirements for Grey's test 2 - Hunter license 1 become Hunter license 2.
	 */
	private static final List<GreyRequirement> GREY_TEST_2_REQUIREMENTS = new ArrayList<>(6);
	{
		GREY_TEST_2_REQUIREMENTS.add(new GreyRequirement(20, ORC_TOTEM));
		GREY_TEST_2_REQUIREMENTS.add(new GreyRequirement(20, TRISALIM_SILK));
		GREY_TEST_2_REQUIREMENTS.add(new GreyRequirement(30, AMBROSIUS_FRUIT));
		GREY_TEST_2_REQUIREMENTS.add(new GreyRequirement(20, BALEFIRE_CRYSTAL));
		GREY_TEST_2_REQUIREMENTS.add(new GreyRequirement(20, IMPERIAL_ARROWHEAD));
		GREY_TEST_2_REQUIREMENTS.add(new GreyRequirement(1, ATHU_HEAD, LANKA_HEAD, TRISKA_HEAD, MOTURA_HEAD, KALATH_HEAD));
	}
	
	/**
	 * Droplist for Grey's test 1 - Apprentice become Hunter license 1:<br>
	 * Key : The monster npc ID.<br>
	 * Value : Monster drop data:<br>
	 * [1] : The dropped quest item.<br>
	 * [2] : The required amount of quest item.<br>
	 * [3] : The chance.
	 */
	private static final Map<Integer, int[]> GREY_TEST_1_DROPLIST = new HashMap<>(9);
	{
		GREY_TEST_1_DROPLIST.put(GUARDIAN_BASILISK, new int[]
		{
			BASILISK_SCALE,
			40,
			900000 // 75% C4, 90% GF
		});
		GREY_TEST_1_DROPLIST.put(LETO_LIZARDMAN_SHAMAN, new int[]
		{
			KARUT_WEED,
			20,
			600000 // 50% C4, 60% GF
		});
		GREY_TEST_1_DROPLIST.put(BREKA_OVERLORD_HAKA, new int[]
		{
			HAKA_HEAD,
			1,
			1000000
		});
		GREY_TEST_1_DROPLIST.put(BREKA_OVERLORD_JAKA, new int[]
		{
			JAKA_HEAD,
			1,
			1000000
		});
		GREY_TEST_1_DROPLIST.put(BREKA_OVERLORD_MARKA, new int[]
		{
			MARKA_HEAD,
			1,
			1000000
		});
		GREY_TEST_1_DROPLIST.put(WINDSUS_ALEPH, new int[]
		{
			ALEPH_SKIN,
			1,
			1000000
		});
		GREY_TEST_1_DROPLIST.put(MANASHEN_GARGOYLE, new int[]
		{
			INDIGO_RUNESTONE,
			20,
			600000 // 50% C4, 60% GF
		});
		GREY_TEST_1_DROPLIST.put(ENCHANTED_STONE_GOLEM, new int[]
		{
			INDIGO_RUNESTONE,
			20,
			620000 // 50% C4 , 62% GF
		});
		GREY_TEST_1_DROPLIST.put(GIANT_FUNGUS, new int[]
		{
			SPORESEA_SEED,
			30,
			840000 // 70% C4, 84% GF
		});
	}
	
	/**
	 * Droplist for Grey's test 2 - Hunter license 1 become Hunter license 2:<br>
	 * Key : The monster npc ID.<br>
	 * Value : Monster drop data:<br>
	 * [1] : The dropped quest item.<br>
	 * [2] : The required amount of quest item.<br>
	 * [3] : The chance.
	 */
	private static final Map<Integer, int[]> GREY_TEST_2_DROPLIST = new HashMap<>(12);
	{
		GREY_TEST_2_DROPLIST.put(TIMAK_ORC_WARRIOR, new int[]
		{
			ORC_TOTEM,
			20,
			600000 // 50% C4, 60% GF
		});
		GREY_TEST_2_DROPLIST.put(TRISALIM_SPIDER, new int[]
		{
			TRISALIM_SILK,
			20,
			600000 // 50% C4, 60% GF
		});
		GREY_TEST_2_DROPLIST.put(TRISALIM_TARANTULA, new int[]
		{
			TRISALIM_SILK,
			20,
			600000 // 50% C4, 60% GF
		});
		GREY_TEST_2_DROPLIST.put(VALLEY_TREANT, new int[]
		{
			AMBROSIUS_FRUIT,
			30,
			850000 // 75% C4, 85% GF
		});
		GREY_TEST_2_DROPLIST.put(VALLEY_TREANT_ELDER, new int[]
		{
			AMBROSIUS_FRUIT,
			30,
			850000 // 75% C4, 85% GF
		});
		GREY_TEST_2_DROPLIST.put(TAIRIM, new int[]
		{
			BALEFIRE_CRYSTAL,
			20,
			600000 // 50% C4, 60% GF
		});
		GREY_TEST_2_DROPLIST.put(ARCHER_OF_GREED, new int[]
		{
			IMPERIAL_ARROWHEAD,
			20,
			600000 // 50% C4, 60% GF
		});
		GREY_TEST_2_DROPLIST.put(TARLK_RAIDER_ATHU, new int[]
		{
			ATHU_HEAD,
			1,
			1000000
		});
		GREY_TEST_2_DROPLIST.put(TARLK_RAIDER_LANKA, new int[]
		{
			LANKA_HEAD,
			1,
			1000000
		});
		GREY_TEST_2_DROPLIST.put(TARLK_RAIDER_TRISKA, new int[]
		{
			TRISKA_HEAD,
			1,
			1000000
		});
		GREY_TEST_2_DROPLIST.put(TARLK_RAIDER_MOTURA, new int[]
		{
			MOTURA_HEAD,
			1,
			1000000
		});
		GREY_TEST_2_DROPLIST.put(TARLK_RAIDER_KALATH, new int[]
		{
			KALATH_HEAD,
			1,
			1000000
		});
	}
	
	/**
	 * Tor's request 1 and 2 requirements and reward data.
	 */
	class TorRequestReward
	{
		private final int _id;
		private final int _required;
		private final int _reward;
		
		/**
		 * @param id : Required item ID.
		 * @param required : Required item amount.
		 * @param reward : Adena reward.
		 */
		TorRequestReward(int id, int required, int reward)
		{
			_id = id;
			_required = required;
			_reward = reward;
		}
		
		final int getId()
		{
			return _id;
		}
		
		final int getRequired()
		{
			return _required;
		}
		
		final int getReward()
		{
			return _reward;
		}
	}
	
	/**
	 * Requirements and reward for Tor's request 1 and 2 - Hunting specific monsters under Hunter license 1 or 2.
	 */
	private static final Map<Integer, TorRequestReward> TOR_REQUEST_REWARD = new HashMap<>(42);
	{
		// Tor's request 1 (Hunter license 1)
		TOR_REQUEST_REWARD.put(REQUEST_1ST_1C, new TorRequestReward(CHARM_OF_KADESH, 40, 2090));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_2C, new TorRequestReward(TIMAK_JADE_NECKLACE, 50, 6340));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_3C, new TorRequestReward(ENCHANTED_GOLEM_SHARD, 50, 9480));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_4C, new TorRequestReward(GIANT_MONSTER_EYE_MEAT, 30, 9110));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_5C, new TorRequestReward(DIRE_WYRM_EGG, 40, 8690));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_6C, new TorRequestReward(GRD_BASILISK_TALON, 100, 9480));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_7C, new TorRequestReward(REVENANT_CHAINS, 50, 11280));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_8C, new TorRequestReward(WINDSUS_TUSK, 30, 9640));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_9C, new TorRequestReward(GRANDIS_SKULL, 100, 9180));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_10C, new TorRequestReward(TAIK_OBSIDIAN_AMULET, 50, 5160));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_11C, new TorRequestReward(KARUL_BUGBEAR_HEAD, 30, 3140));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_12C, new TorRequestReward(TAMLIN_IVORY_CHARM, 40, 3160));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_1B, new TorRequestReward(FANG_OF_NARAK, 1, 6370));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_2B, new TorRequestReward(ENCHANTED_GARGOYLE_HORN, 50, 19080));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_3B, new TorRequestReward(COILED_SERPENT_TOTEM, 50, 17730));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_4B, new TorRequestReward(TOTEM_OF_KADESH, 1, 5790));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_5B, new TorRequestReward(KAIKI_HEAD, 1, 8560));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_6B, new TorRequestReward(KRONBE_VENOM_SAC, 30, 8320));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_1A, new TorRequestReward(EVA_CHARM, 30, 30310));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_2A, new TorRequestReward(TITAN_TABLET, 1, 27540));
		TOR_REQUEST_REWARD.put(REQUEST_1ST_3A, new TorRequestReward(BOOK_OF_SHUNAIMAN, 1, 20560));
		// Tor's request 2 (Hunter license 2)
		TOR_REQUEST_REWARD.put(REQUEST_2ND_1C, new TorRequestReward(ROTTING_TREE_SPORES, 40, 6850));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_2C, new TorRequestReward(TRISALIM_VENOM_SAC, 40, 7250));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_3C, new TorRequestReward(TAIK_ORC_TOTEM, 50, 7160));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_4C, new TorRequestReward(HARIT_BARBED_NECKLACE, 40, 6580));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_5C, new TorRequestReward(COIN_OF_OLD_EMPIRE, 20, 10100));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_6C, new TorRequestReward(SKIN_OF_FARCRAN, 30, 13000));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_7C, new TorRequestReward(TEMPEST_SHARD, 40, 7660));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_8C, new TorRequestReward(TSUNAMI_SHARD, 40, 7660));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_9C, new TorRequestReward(SATYR_MANE, 40, 11260));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_10C, new TorRequestReward(HAMADRYAD_SHARD, 40, 7660));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_11C, new TorRequestReward(VANOR_SILENOS_MANE, 30, 8810));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_12C, new TorRequestReward(TALK_BUGBEAR_TOTEM, 30, 7350));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_1B, new TorRequestReward(OKUN_HEAD, 1, 8760));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_2B, new TorRequestReward(KAKRAN_HEAD, 1, 9380));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_3B, new TorRequestReward(NARCISSUS_SOULSTONE, 40, 17820));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_4B, new TorRequestReward(DEPRIVE_EYE, 20, 17540));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_5B, new TorRequestReward(UNICORN_HORN, 20, 14160));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_6B, new TorRequestReward(KERUNO_GOLD_MANE, 1, 15960));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_1A, new TorRequestReward(SKULL_OF_EXECUTED, 20, 39100));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_2A, new TorRequestReward(BUST_OF_TRAVIS, 1, 39550));
		TOR_REQUEST_REWARD.put(REQUEST_2ND_3A, new TorRequestReward(SWORD_OF_CADMUS, 10, 41200));
	}
	
	/**
	 * Tor's request 1 and 2 drop data.<br>
	 * Note: Dropped item id and count is defined in {@link TorRequestReward}.
	 */
	class TorRequestDroplist
	{
		private final int _request;
		private final int _monster;
		private final int _chance;
		private final NpcStringId _message;
		
		/**
		 * Note:<br>
		 * When chance is 5.000.000, it has 100% chance to drop 5 items.<br>
		 * When chance is 1.600.000, it has 60% chance to drop 2 items and 40% to drop 1 item.
		 * @param request : The request item ID.
		 * @param monster : The monster npc ID.
		 * @param chance : The chance is defined in 1.000.000 for 100%.
		 */
		TorRequestDroplist(int request, int monster, int chance)
		{
			_request = request;
			_monster = monster;
			_chance = chance;
			_message = null;
		}
		
		/**
		 * Note:<br>
		 * When chance is 5.000.000, it has 100% chance to drop 5 items.<br>
		 * When chance is 1.600.000, it has 60% chance to drop 2 items and 40% to drop 1 item.
		 * @param request : The request item ID.
		 * @param monster : The monster npc ID.
		 * @param chance : The chance is defined in 1.000.000 for 100%.
		 * @param message : The message of dying monster (optional).
		 */
		TorRequestDroplist(int request, int monster, int chance, NpcStringId message)
		{
			_request = request;
			_monster = monster;
			_chance = chance;
			_message = message;
		}
		
		final boolean test(int request, int monster)
		{
			return _request == request && _monster == monster;
		}
		
		final int getMonster()
		{
			return _monster;
		}
		
		final int getChance()
		{
			return _chance;
		}
		
		final void tryToSpeak(Npc npc)
		{
			if (_message != null)
				npc.broadcastNpcSay(_message);
		}
	}
	
	/**
	 * Drop data for Tor's request 1 and 2 - Hunting specific monsters under Hunter license 1 or 2.<br>
	 * Drop chances are taken from GF.
	 */
	private static final List<TorRequestDroplist> TOR_REQUEST_DROPLIST = new ArrayList<>();
	{
		// Tor's request 1 (Hunter license 1)
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_1C, LETO_LIZARDMAN_ARCHER, 900000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_1C, LETO_LIZARDMAN_SOLDIER, 930000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_2C, TIMAK_ORC_OVERLORD, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_2C, TIMAK_ORC_WARRIOR, 950000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_3C, ENCHANTED_STONE_GOLEM, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_4C, GIANT_MONSTEREYE, 600000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_5C, DIRE_WYRM, 900000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_6C, GUARDIAN_BASILISK, 1600000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_7C, FETTERED_SOUL, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_8C, WINDSUS, 630000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_9C, GRANDIS, 2000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_10C, TAIK_ORC_ARCHER, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_10C, TAIK_ORC_WARRIOR, 930000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_11C, KARUL_BUGBEAR, 600000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_12C, TAMLIN_ORC, 720000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_12C, TAMLIN_ORC_ARCHER, 900000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_1B, LETO_CHIEF_NARAK, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_2B, ENCHANTED_GARGOYLE, 600000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_3B, BREKA_ORC_SHAMAN, 930000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_3B, BREKA_ORC_WARRIOR, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_4B, LETO_SHAMAN_KETZ, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_5B, TIMAK_RAIDER_KAIKEE, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_6B, KRONBE_SPIDER, 600000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_1A, SPORE_ZOMBIE, 600000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_2A, GOK_MAGOK, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_1ST_3A, KARUL_CHIEF_OROOTO, 1000000));
		// Tor's request 2 (Hunter license 2)
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_1C, ROTTING_TREE, 770000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_2C, TRISALIM_SPIDER, 760000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_2C, TRISALIM_TARANTULA, 850000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_3C, TAIK_ORC_CAPTAIN, 990000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_3C, TAIK_ORC_SHAMAN, 630000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_4C, HARIT_LIZARDMAN_ARCHER, 980000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_4C, HARIT_LIZARDMAN_GRUNT, 980000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_4C, HARIT_LIZARDMAN_WARRIOR, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_5C, HATAR_RATMAN_BOSS, 620000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_5C, HATAR_RATMAN_THIEF, 600000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_5C, GREMLIN_FILCHER, 3000000, NpcStringId.ID_33513));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_6C, FARCRAN, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_7C, FLINE, 590000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_7C, GREMLIN_FILCHER, 5000000, NpcStringId.ID_33513));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_8C, LIELE, 610000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_8C, GREMLIN_FILCHER, 5000000, NpcStringId.ID_33513));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_9C, SATYR, 900000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_9C, SATYR_ELDER, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_10C, FOREST_RUNNER, 740000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_10C, GREMLIN_FILCHER, 5000000, NpcStringId.ID_33513));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_11C, VANOR_SILENOS_GRUNT, 800000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_11C, VANOR_SILENOS_SCOUT, 950000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_11C, VANOR_SILENOS_WARRIOR, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_12C, TARLK_BUGBEAR_WARRIOR, 730000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_1B, TIMAK_OVERLORD_OKUN, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_2B, TAIK_OVERLORD_KAKRAN, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_3B, MIRROR, 960000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_3B, GREMLIN_FILCHER, 5000000, NpcStringId.ID_33513));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_4B, DEPRIVE, 870000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_5B, UNICORN, 780000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_5B, UNICORN_ELDER, 960000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_6B, VANOR_ELDER_KERUNOS, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_1A, GRAVE_WANDERER, 830000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_2A, HATAR_CHIEFTAIN_KUBEL, 1000000));
		TOR_REQUEST_DROPLIST.add(new TorRequestDroplist(REQUEST_2ND_3A, JUDGE_OF_MARSH, 740000));
	}
	
	/**
	 * Tor's request 1 and 2 spawn data.
	 */
	class TorRequestSpawn
	{
		private final int _request;
		private final int _monster;
		private final int _chance;
		private final int _spawnId;
		private final int _spawnCount;
		private final NpcStringId _message;
		private final boolean _reached;
		
		/**
		 * @param request : The request item ID.
		 * @param monster : The monster npc ID.
		 * @param chance : The chance is defined in 100 for 100%.
		 * @param spawnId : The spawned monster npc ID.
		 * @param spawnCount : The spawned monster count.
		 */
		TorRequestSpawn(int request, int monster, int chance, int spawnId, int spawnCount)
		{
			_request = request;
			_monster = monster;
			_chance = chance;
			_spawnId = spawnId;
			_spawnCount = spawnCount;
			_message = null;
			_reached = false;
		}
		
		/**
		 * @param request : The request item ID.
		 * @param monster : The monster npc ID.
		 * @param chance : The chance is defined in 100 for 100%.
		 * @param spawnId : The spawned monster npc ID.
		 * @param spawnCount : The spawned monster count.
		 * @param message : The message of dying monster (optional).
		 */
		TorRequestSpawn(int request, int monster, int chance, int spawnId, int spawnCount, NpcStringId message)
		{
			_request = request;
			_monster = monster;
			_chance = chance;
			_spawnId = spawnId;
			_spawnCount = spawnCount;
			_message = message;
			_reached = false;
		}
		
		/**
		 * @param request : The request item ID.
		 * @param monster : The monster npc ID.
		 * @param chance : The chance is defined in 100 for 100%.
		 * @param spawnId : The spawned monster npc ID.
		 * @param spawnCount : The spawned monster count.
		 * @param message : The message of dying monster (optional).
		 * @param reached : The identifier of reaching quest item count.
		 */
		TorRequestSpawn(int request, int monster, int chance, int spawnId, int spawnCount, NpcStringId message, boolean reached)
		{
			_request = request;
			_monster = monster;
			_chance = chance;
			_spawnId = spawnId;
			_spawnCount = spawnCount;
			_message = message;
			_reached = reached;
		}
		
		final boolean test(int request, int monster)
		{
			return _request == request && _monster == monster;
		}
		
		final int getMonster()
		{
			return _monster;
		}
		
		final int getChance()
		{
			return _chance;
		}
		
		final boolean isReached()
		{
			return _reached;
		}
		
		final void spawn(Npc npc, Player player)
		{
			for (int i = 0; i < _spawnCount; i++)
			{
				final Npc newNpc = addSpawn(_spawnId, npc, true, 300000, true);
				newNpc.getAI().addAttackDesire(player, 200);
				
				if (_message != null)
					newNpc.broadcastNpcSay(_message);
			}
		}
	}
	
	/**
	 * Spawn data for Tor's request 1 and 2 - Hunting specific monsters under Hunter license 1 or 2.<br>
	 * Note: Spawn chances are taken from GF.
	 */
	private static final List<TorRequestSpawn> TOR_REQUEST_SPAWN = new ArrayList<>();
	{
		// Tor's request 1 (Hunter license 1)
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_1ST_1B, LETO_LIZARDMAN_OVERLORD, 20, LETO_CHIEF_NARAK, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_1ST_4B, LETO_LIZARDMAN_SHAMAN, 20, LETO_SHAMAN_KETZ, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_1ST_5B, TIMAK_ORC_WARRIOR, 20, TIMAK_RAIDER_KAIKEE, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_1ST_2A, GRANDIS, 20, GOK_MAGOK, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_1ST_2A, GOK_MAGOK, 50, BLACK_LEGION_STORMTROOPER, 2, NpcStringId.ID_33511, true));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_1ST_3A, KARUL_BUGBEAR, 20, KARUL_CHIEF_OROOTO, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_1ST_3A, KARUL_CHIEF_OROOTO, 50, BLACK_LEGION_STORMTROOPER, 2, NpcStringId.ID_33511, true));
		// Tor's request 2 (Hunter license 2)
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_5C, HATAR_RATMAN_BOSS, 10, GREMLIN_FILCHER, 1, NpcStringId.ID_33512));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_5C, HATAR_RATMAN_THIEF, 10, GREMLIN_FILCHER, 1, NpcStringId.ID_33512));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_7C, FLINE, 10, GREMLIN_FILCHER, 1, NpcStringId.ID_33512));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_8C, LIELE, 10, GREMLIN_FILCHER, 1, NpcStringId.ID_33512));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_10C, FOREST_RUNNER, 10, GREMLIN_FILCHER, 1, NpcStringId.ID_33512));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_1B, TIMAK_ORC_OVERLORD, 10, TIMAK_OVERLORD_OKUN, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_2B, TAIK_ORC_CAPTAIN, 20, TAIK_OVERLORD_KAKRAN, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_3B, MIRROR, 10, GREMLIN_FILCHER, 1, NpcStringId.ID_33512));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_6B, VANOR_SILENOS_CHIEFTAIN, 20, VANOR_ELDER_KERUNOS, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_2A, HATAR_RATMAN_BOSS, 20, HATAR_CHIEFTAIN_KUBEL, 1));
		TOR_REQUEST_SPAWN.add(new TorRequestSpawn(REQUEST_2ND_2A, HATAR_CHIEFTAIN_KUBEL, 50, BLACK_LEGION_STORMTROOPER, 2, NpcStringId.ID_33511, true));
	}
	
	private static final String[] TOR_REQUEST_1_MENU =
	{
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3727\">C: 40 Totems of Kadesh</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3728\">C: 50 Jade Necklaces of Timak</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3729\">C: 50 Enchanted Golem Shards</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3730\">C: 30 Pieces Monster Eye Meat</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3731\">C: 40 Eggs of Dire Wyrm</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3732\">C: 100 Claws of Guardian Basilisk</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3733\">C: 50 Revenant Chains</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3734\">C: 30 Windsus Tusks</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3735\">C: 100 Skulls of Grandis</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3736\">C: 50 Taik Obsidian Amulets</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3737\">C: 30 Heads of Karul Bugbear</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3738\">C: 40 Ivory Charms of Tamlin</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3739\">B: Situation Preparation - Leto Chief</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3740\">B: 50 Enchanted Gargoyle Horns</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3741\">B: 50 Coiled Serpent Totems</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3742\">B: Situation Preparation - Sorcerer Catch of Leto</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3743\">B: Situation Preparation - Timak Raider Kaikee</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3744\">B: 30 Kronbe Venom Sacs</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3745\">A: 30 Charms of Eva</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3746\">A: Titan's Tablet</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3747\">A: Book of Shunaiman</a><br>",
	};
	
	private static final String[] TOR_REQUEST_2_MENU =
	{
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3748\">C: 40 Rotted Tree Spores</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3749\">C: 40 Trisalim Venom Sacs</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3750\">C: 50 Totems of Taik Orc</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3751\">C: 40 Harit Barbed Necklaces</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3752\">C: 20 Coins of Ancient Empire</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3753\">C: 30 Skins of Farkran</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3754\">C: 40 Tempest Shards</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3755\">C: 40 Tsunami Shards</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3756\">C: 40 Satyr Manes</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3757\">C: 40 Hamadryad Shards</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3758\">C: 30 Manes of Vanor Silenos</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3759\">C: 30 Totems of Tarlk Bugbears</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3760\">B: Situation Preparation - Overlord Okun of Timak</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3761\">B: Situation Preparation - Overlord Kakran of Taik</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3762\">B: 40 Narcissus Soulstones</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3763\">B: 20 Eyes of Deprived</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3764\">B: 20 Unicorn Horns</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3765\">B: Kerunos's Gold Mane</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3766\">A: 20 Skull of Executed</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3767\">A: Bust of Travis</a><br>",
		"<a action=\"bypass -h Quest Q335_SongOfTheHunter 3768\">A: 10 Swords of Cadmus</a><br>",
	};
	
	/**
	 * Droplist for Cybellin's request - Blood Crystals (Hunter license 1 and 2)<br>
	 * Since all values are 60% (50% on C4), it is hardcoded on onKill section.
	 */
	private static final int[] CYBELLIN_REQUEST_DROPLIST =
	{
		LETO_LIZARDMAN_ARCHER,
		LETO_LIZARDMAN_SOLDIER,
		LETO_LIZARDMAN_SHAMAN,
		LETO_LIZARDMAN_OVERLORD,
		HARIT_LIZARDMAN_GRUNT,
		HARIT_LIZARDMAN_ARCHER,
		HARIT_LIZARDMAN_WARRIOR
	};
	
	/**
	 * Rewards for Cybellin's request - Blood Crystals (Hunter license 1 and 2):<br>
	 * Key : The returned stage of Blood Crystal.<br>
	 * Value : Adena reward.<br>
	 */
	private static final Map<Integer, Integer> CYBELLIN_REQUEST_REWARDS = new HashMap<>();
	{
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_01, 0);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_02, 3400);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_03, 6800);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_04, 13600);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_05, 37200);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_06, 54400);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_07, 108800);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_08, 217600);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_09, 435200);
		CYBELLIN_REQUEST_REWARDS.put(BLOOD_CRYSTAL_10, 870400);
	}
	
	public Q335_SongOfTheHunter()
	{
		super(335, "Song of the Hunter");
		
		setItemsIds(CYBELLIN_DAGGER, LICENSE_1, LICENSE_2, LAUREL_LEAF_PIN, TEST_INSTRUCTIONS_1, TEST_INSTRUCTIONS_2, CYBELLIN_REQUEST, BASILISK_SCALE, KARUT_WEED, HAKA_HEAD, JAKA_HEAD, MARKA_HEAD, ALEPH_SKIN, INDIGO_RUNESTONE, SPORESEA_SEED, ORC_TOTEM, TRISALIM_SILK, AMBROSIUS_FRUIT, BALEFIRE_CRYSTAL, IMPERIAL_ARROWHEAD, ATHU_HEAD, LANKA_HEAD, TRISKA_HEAD, MOTURA_HEAD, KALATH_HEAD, BLOOD_CRYSTAL_01, BLOOD_CRYSTAL_02, BLOOD_CRYSTAL_03, BLOOD_CRYSTAL_04, BLOOD_CRYSTAL_05, BLOOD_CRYSTAL_06, BLOOD_CRYSTAL_07, BLOOD_CRYSTAL_08, BLOOD_CRYSTAL_09, BLOOD_CRYSTAL_10, BROKEN_BLOOD, REQUEST_1ST_1C, REQUEST_1ST_2C, REQUEST_1ST_3C, REQUEST_1ST_4C, REQUEST_1ST_5C, REQUEST_1ST_6C, REQUEST_1ST_7C, REQUEST_1ST_8C, REQUEST_1ST_9C, REQUEST_1ST_10C, REQUEST_1ST_11C, REQUEST_1ST_12C, REQUEST_1ST_1B, REQUEST_1ST_2B, REQUEST_1ST_3B, REQUEST_1ST_4B, REQUEST_1ST_5B, REQUEST_1ST_6B, REQUEST_1ST_1A, REQUEST_1ST_2A, REQUEST_1ST_3A, REQUEST_2ND_1C, REQUEST_2ND_2C, REQUEST_2ND_3C, REQUEST_2ND_4C, REQUEST_2ND_5C, REQUEST_2ND_6C, REQUEST_2ND_7C, REQUEST_2ND_8C, REQUEST_2ND_9C, REQUEST_2ND_10C, REQUEST_2ND_11C, REQUEST_2ND_12C, REQUEST_2ND_1B, REQUEST_2ND_2B, REQUEST_2ND_3B, REQUEST_2ND_4B, REQUEST_2ND_5B, REQUEST_2ND_6B, REQUEST_2ND_1A, REQUEST_2ND_2A, REQUEST_2ND_3A, CHARM_OF_KADESH, TIMAK_JADE_NECKLACE, ENCHANTED_GOLEM_SHARD, GIANT_MONSTER_EYE_MEAT, DIRE_WYRM_EGG, GRD_BASILISK_TALON, REVENANT_CHAINS, WINDSUS_TUSK, GRANDIS_SKULL, TAIK_OBSIDIAN_AMULET, KARUL_BUGBEAR_HEAD, TAMLIN_IVORY_CHARM, FANG_OF_NARAK, ENCHANTED_GARGOYLE_HORN, COILED_SERPENT_TOTEM, TOTEM_OF_KADESH, KAIKI_HEAD, KRONBE_VENOM_SAC, EVA_CHARM, TITAN_TABLET, BOOK_OF_SHUNAIMAN, ROTTING_TREE_SPORES, TRISALIM_VENOM_SAC, TAIK_ORC_TOTEM, HARIT_BARBED_NECKLACE, COIN_OF_OLD_EMPIRE, SKIN_OF_FARCRAN, TEMPEST_SHARD, TSUNAMI_SHARD, SATYR_MANE, HAMADRYAD_SHARD, VANOR_SILENOS_MANE, TALK_BUGBEAR_TOTEM, OKUN_HEAD, KAKRAN_HEAD, NARCISSUS_SOULSTONE, DEPRIVE_EYE, UNICORN_HORN, KERUNO_GOLD_MANE, SKULL_OF_EXECUTED, BUST_OF_TRAVIS, SWORD_OF_CADMUS);
		
		addQuestStart(GREY);
		addTalkId(GREY, TOR, CYBELLIN);
		
		addEventIds(GREY_TEST_1_DROPLIST.keySet(), EventHandler.MY_DYING);
		addEventIds(GREY_TEST_2_DROPLIST.keySet(), EventHandler.MY_DYING);
		addEventIds(TOR_REQUEST_DROPLIST.stream().map(TorRequestDroplist::getMonster).toList(), EventHandler.MY_DYING);
		addEventIds(TOR_REQUEST_SPAWN.stream().map(TorRequestSpawn::getMonster).toList(), EventHandler.MY_DYING);
		addEventIds(CYBELLIN_REQUEST_DROPLIST, EventHandler.MY_DYING);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Grey
		if (event.equalsIgnoreCase("30744-03.htm"))
		{
			st.setCond(1);
			st.setState(QuestStatus.STARTED);
			giveItems(player, TEST_INSTRUCTIONS_1, 1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30744-09.htm"))
		{
			// Has Tor's request, skip.
			if (st.getInteger("tor") > 0)
				htmltext = "30744-09a.htm";
			// Start Grey's test 2.
			else
			{
				st.set("grey2", 1);
				giveItems(player, TEST_INSTRUCTIONS_2, 1);
				playSound(player, SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("30744-16.htm"))
		{
			if (player.getInventory().getItemCount(LAUREL_LEAF_PIN) >= 20)
			{
				htmltext = "30744-17.htm";
				rewardItems(player, ADENA, 20000);
			}
			
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		// Tor
		else if (event.equalsIgnoreCase("30745-02.htm"))
		{
			// Grey's test 2 is running, can't start Tor's request
			if (st.getInteger("grey2") > 0)
				htmltext = "30745-03.htm";
		}
		else if (event.equalsIgnoreCase("Tor_list_1"))
		{
			// 12x C
			// 6x B
			// 3x A
			// Index = 0-11 = C, 12-17 = B, 18-20 = A
			
			if (st.getInteger("tor") == 0)
			{
				int reply0;
				int reply1;
				int reply2;
				int reply3;
				int reply4;
				
				int offer = st.getInteger("torOffer");
				
				// No offer exists.
				if (offer == 0)
				{
					// Generate new offer.
					int leafPins = player.getInventory().getItemCount(LAUREL_LEAF_PIN);
					do
					{
						if (leafPins == 0)
						{
							reply0 = Rnd.get(12); // C
							reply1 = Rnd.get(12); // C
							reply2 = Rnd.get(12); // C
							reply3 = Rnd.get(12); // C
						}
						else if (leafPins < 4)
						{
							if (Rnd.get(100) < 20)
							{
								reply0 = Rnd.get(6) + 12; // B
								reply2 = Rnd.get(6); // low C
								reply3 = Rnd.get(6) + 6; // high C
							}
							else // 5x C
							{
								reply0 = Rnd.get(12); // C
								reply2 = Rnd.get(12); // C
								reply3 = Rnd.get(12); // C
							}
							reply1 = Rnd.get(12); // C
						}
						else
						{
							if (Rnd.get(100) < 20)
								reply0 = Rnd.get(6) + 12; // B
							else
								reply0 = Rnd.get(12); // C
							if (Rnd.get(100) < 5)
								reply1 = Rnd.get(3) + 18; // A
							else
								reply1 = Rnd.get(12); // C
							reply2 = Rnd.get(6); // low C
							reply3 = Rnd.get(6) + 6; // high C
						}
						reply4 = Rnd.get(12); // C
					}
					while (reply0 == reply1 || reply0 == reply2 || reply0 == reply3 || reply0 == reply4 || reply1 == reply2 || reply1 == reply3 || reply1 == reply4 || reply2 == reply3 || reply2 == reply4 || reply3 == reply4);
					
					// Store new offer.
					offer = reply0 | reply1 << 4 | reply2 << 8 | reply3 << 12 | reply4 << 16;
					st.set("torOffer", offer);
				}
				// Offer exists.
				else
				{
					// Parse options.
					reply0 = offer & 0x0F;
					reply1 = (offer >> 4) & 0x0F;
					reply2 = (offer >> 8) & 0x0F;
					reply3 = (offer >> 12) & 0x0F;
					reply4 = (offer >> 16) & 0x0F;
				}
				
				htmltext = getHtmlText("30745-16.htm").replace("%reply0%", TOR_REQUEST_1_MENU[reply0]).replace("%reply1%", TOR_REQUEST_1_MENU[reply1]).replace("%reply2%", TOR_REQUEST_1_MENU[reply2]).replace("%reply3%", TOR_REQUEST_1_MENU[reply3]).replace("%reply4%", TOR_REQUEST_1_MENU[reply4]);
			}
		}
		else if (event.equalsIgnoreCase("Tor_list_2"))
		{
			if (st.getInteger("tor") == 0)
			{
				int reply0;
				int reply1;
				int reply2;
				int reply3;
				int reply4;
				
				int offer = st.getInteger("torOffer");
				
				// No offer exists.
				if (offer == 0)
				{
					// Generate new offer.
					int leafPins = player.getInventory().getItemCount(LAUREL_LEAF_PIN);
					do
					{
						if (leafPins == 0)
						{
							reply0 = Rnd.get(12); // C
							reply1 = Rnd.get(12); // C
						}
						else if (leafPins < 4)
						{
							if (Rnd.get(100) < 20)
								reply0 = Rnd.get(6) + 12; // B
							else
								reply0 = Rnd.get(12); // C
							reply1 = Rnd.get(12); // C
						}
						else
						{
							if (Rnd.get(100) < 20)
								reply0 = Rnd.get(6) + 12; // B
							else
								reply0 = Rnd.get(12); // C
							if (Rnd.get(100) < 5)
								reply1 = Rnd.get(3) + 18; // A
							else
								reply1 = Rnd.get(12); // C
						}
						reply2 = Rnd.get(6); // low C
						reply3 = Rnd.get(6) + 6; // high C
						reply4 = Rnd.get(12); // C
					}
					while (reply0 == reply1 || reply0 == reply2 || reply0 == reply3 || reply0 == reply4 || reply1 == reply2 || reply1 == reply3 || reply1 == reply4 || reply2 == reply3 || reply2 == reply4 || reply3 == reply4);
					
					// Store new offer.
					offer = reply0 | reply1 << 4 | reply2 << 8 | reply3 << 12 | reply4 << 16;
					st.set("torOffer", offer);
				}
				// Offer exists.
				else
				{
					// Parse options.
					reply0 = offer & 0x0F;
					reply1 = (offer >> 4) & 0x0F;
					reply2 = (offer >> 8) & 0x0F;
					reply3 = (offer >> 12) & 0x0F;
					reply4 = (offer >> 16) & 0x0F;
				}
				
				htmltext = getHtmlText("30745-16.htm").replace("%reply0%", TOR_REQUEST_2_MENU[reply0]).replace("%reply1%", TOR_REQUEST_2_MENU[reply1]).replace("%reply2%", TOR_REQUEST_2_MENU[reply2]).replace("%reply3%", TOR_REQUEST_2_MENU[reply3]).replace("%reply4%", TOR_REQUEST_2_MENU[reply4]);
			}
		}
		else if (event.equalsIgnoreCase("30745-05b.htm"))
		{
			final int request = st.getInteger("tor");
			if (request > 0)
			{
				takeItems(player, request, -1);
				takeItems(player, TOR_REQUEST_REWARD.get(request).getId(), -1);
				
				takeItems(player, LAUREL_LEAF_PIN, 1);
				
				st.unset("tor");
				playSound(player, SOUND_GIVEUP);
			}
		}
		else if (StringUtil.isDigit(event) && event.length() == 4)
		{
			final int request = Integer.parseInt(event);
			
			st.set("tor", request);
			giveItems(player, request, 1);
			
			htmltext = "30745-" + request + ".htm";
		}
		// Cybellin
		else if (event.equalsIgnoreCase("30746-03.htm"))
		{
			boolean sound = false;
			
			if (!player.getInventory().hasItem(CYBELLIN_REQUEST))
			{
				giveItems(player, CYBELLIN_REQUEST, 1);
				sound = true;
			}
			
			if (!player.getInventory().hasItem(CYBELLIN_DAGGER))
			{
				giveItems(player, CYBELLIN_DAGGER, 1);
				sound = true;
			}
			
			takeItems(player, BROKEN_BLOOD, -1);
			if (!player.getInventory().hasItem(BLOOD_CRYSTAL_01))
			{
				giveItems(player, BLOOD_CRYSTAL_01, 1);
				sound = true;
			}
			
			st.set("cybellin", 1);
			
			if (sound)
				playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30746-06.htm"))
		{
			for (Entry<Integer, Integer> entry : CYBELLIN_REQUEST_REWARDS.entrySet())
			{
				final int bloodCrystal = entry.getKey();
				
				if (player.getInventory().hasItem(bloodCrystal))
				{
					takeItems(player, bloodCrystal, -1);
					rewardItems(player, ADENA, entry.getValue());
					break;
				}
			}
		}
		else if (event.equalsIgnoreCase("30746-10.htm"))
		{
			st.unset("cybellin");
			
			// only destroy Blood Crystal - Stage 1 (other levels are kept).
			takeItems(player, BLOOD_CRYSTAL_01, -1);
			takeItems(player, CYBELLIN_REQUEST, -1);
			takeItems(player, CYBELLIN_DAGGER, -1);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		final QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		final int cond = st.getCond();
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getStatus().getLevel() >= 35)
					htmltext = "30744-02.htm";
				else
					htmltext = "30744-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case GREY:
						if (cond == 1)
						{
							// Check Apprentice become Hunter license 1
							if (passedGreyRequest(player, GREY_TEST_1_REQUIREMENTS))
							{
								htmltext = "30744-06.htm";
								st.setCond(2);
								
								for (GreyRequirement tr : GREY_TEST_1_REQUIREMENTS)
									for (int item : tr.getIds())
										takeItems(player, item, -1);
									
								takeItems(player, TEST_INSTRUCTIONS_1, -1);
								giveItems(player, LICENSE_1, 1);
								playSound(player, SOUND_MIDDLE);
							}
							else
								htmltext = "30744-05.htm";
						}
						else if (cond == 2)
						{
							// Hunter license 1 below 45 lvl
							if (player.getStatus().getLevel() < 45)
								htmltext = "30744-07.htm";
							// Hunter license 1 above 45 lvl, without started test 2
							else if (st.getInteger("grey2") == 0)
								htmltext = "30744-08.htm";
							// Hunter license 1 above 45 lvl, with started test 2
							else
							{
								// Check Hunter license 1 become Hunter license 2
								if (passedGreyRequest(player, GREY_TEST_2_REQUIREMENTS))
								{
									htmltext = "30744-12.htm";
									st.setCond(3);
									st.unset("grey2");
									
									for (GreyRequirement tr : GREY_TEST_2_REQUIREMENTS)
										for (int item : tr.getIds())
											takeItems(player, item, -1);
										
									takeItems(player, TEST_INSTRUCTIONS_2, -1);
									takeItems(player, LICENSE_1, -1);
									giveItems(player, LICENSE_2, 1);
									playSound(player, SOUND_MIDDLE);
								}
								else
									htmltext = "30744-11.htm";
							}
						}
						else if (cond == 3)
							htmltext = "30744-14.htm";
						break;
					
					case TOR:
						final int request = st.getInteger("tor");
						
						// Apprentice
						if (cond == 1)
							htmltext = "30745-01a.htm";
						// Hunter license 1 or 2
						else
						{
							TorRequestReward reward = TOR_REQUEST_REWARD.get(request);
							
							// Request doesn't exist.
							if (reward == null)
							{
								if (cond == 2)
								{
									// hunting license 1 - multiple options
									if (player.getStatus().getLevel() >= 45)
									{
										if (player.getInventory().hasItem(TEST_INSTRUCTIONS_2))
											htmltext = "30745-03.htm";
										else
											htmltext = "30745-03a.htm";
									}
									else
										htmltext = "30745-01b.htm";
								}
								else
									// hunting license 2, just show html
									htmltext = "30745-03b.htm";
							}
							// Request exists and reward requirements has been met.
							else if (player.getInventory().getItemCount(reward.getId()) >= reward.getRequired())
							{
								// Show reward html depending on hunting license
								htmltext = cond == 2 ? "30745-06a.htm" : "30745-06b.htm";
								
								st.set("tor", 0);
								st.set("torOffer", 0);
								takeItems(player, request, -1);
								takeItems(player, reward.getId(), -1);
								giveItems(player, LAUREL_LEAF_PIN, 1);
								rewardItems(player, ADENA, reward.getReward());
								playSound(player, SOUND_MIDDLE);
							}
							// Request exists, but reward requirements are not met.
							else
								htmltext = "30745-05.htm";
						}
						break;
					
					case CYBELLIN:
						// Apprentice
						if (cond == 1)
							htmltext = "30746-01.htm";
						// Hunter license 1 or 2
						else
						{
							// Doesn't have Cybellin request
							if (st.getInteger("cybellin") == 0)
								htmltext = "30746-02.htm";
							// Has Blood Crystal 1
							else if (player.getInventory().hasItem(BLOOD_CRYSTAL_01))
								htmltext = "30746-04.htm";
							// Has Blood Crystal 2-9
							else if (player.getInventory().hasAtLeastOneItem(BLOOD_CRYSTAL_02, BLOOD_CRYSTAL_03, BLOOD_CRYSTAL_04, BLOOD_CRYSTAL_05, BLOOD_CRYSTAL_06, BLOOD_CRYSTAL_07, BLOOD_CRYSTAL_08, BLOOD_CRYSTAL_09))
								htmltext = "30746-05.htm";
							// Has Blood Crystal 10
							else if (player.getInventory().hasItem(BLOOD_CRYSTAL_10))
							{
								htmltext = "30746-05a.htm";
								takeItems(player, BLOOD_CRYSTAL_10, -1);
								rewardItems(player, ADENA, CYBELLIN_REQUEST_REWARDS.get(BLOOD_CRYSTAL_10));
							}
							// Has Broken Blood
							else if (player.getInventory().hasItem(BROKEN_BLOOD))
							{
								htmltext = "30746-09.htm";
								takeItems(player, BROKEN_BLOOD, -1);
							}
							// Has no Blood Crystal, neither Broken Blood
							else
								htmltext = "30746-08.htm";
						}
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
		
		final int npcId = npc.getNpcId();
		final int cond = st.getCond();
		
		// Apprentice (Solving Grey's request 1 - Apprentice and getting Hunter license 1)
		if (cond == 1)
		{
			// Check quest item drop list
			if (GREY_TEST_1_DROPLIST.containsKey(npcId))
			{
				int[] dropData = GREY_TEST_1_DROPLIST.get(npcId);
				dropItems(player, dropData[0], 1, dropData[1], dropData[2]);
			}
			// Breka Orc Warrior summons sequentially 3 Breka Bandits
			else if (npcId == BREKA_ORC_WARRIOR && Rnd.get(100) < 20) // 10% C4, 20% GF
			{
				if (!player.getInventory().hasItem(HAKA_HEAD))
					addSpawn(BREKA_OVERLORD_HAKA, npc, true, 300000, true);
				else if (!player.getInventory().hasItem(JAKA_HEAD))
					addSpawn(BREKA_OVERLORD_JAKA, npc, true, 300000, true);
				else if (!player.getInventory().hasItem(MARKA_HEAD))
					addSpawn(BREKA_OVERLORD_MARKA, npc, true, 300000, true);
			}
			// Windsus summons Windsus Aleph
			else if (npcId == WINDSUS && !player.getInventory().hasItem(ALEPH_SKIN) && Rnd.get(100) < 20) // 10% C4, 20% GF
				addSpawn(WINDSUS_ALEPH, npc, true, 300000, true);
		}
		// Hunter license 1 or 2
		else
		{
			// Solving Grey's request 2 - Getting Hunter license 2
			if (cond == 2 && st.getInteger("grey2") > 0)
			{
				final int[] dropData = GREY_TEST_2_DROPLIST.get(npcId);
				if (dropData != null)
					dropItems(player, dropData[0], 1, dropData[1], dropData[2]);
				else if (npcId == TARLK_BUGBEAR_WARRIOR && Rnd.get(100) < 20) // 10% C4, 20% GF
				{
					if (!player.getInventory().hasItem(ATHU_HEAD))
						addSpawn(TARLK_RAIDER_ATHU, npc, true, 300000, true);
					else if (!player.getInventory().hasItem(LANKA_HEAD))
						addSpawn(TARLK_RAIDER_LANKA, npc, true, 300000, true);
					else if (!player.getInventory().hasItem(TRISKA_HEAD))
						addSpawn(TARLK_RAIDER_TRISKA, npc, true, 300000, true);
					else if (!player.getInventory().hasItem(MOTURA_HEAD))
						addSpawn(TARLK_RAIDER_MOTURA, npc, true, 300000, true);
					else if (!player.getInventory().hasItem(KALATH_HEAD))
						addSpawn(TARLK_RAIDER_KALATH, npc, true, 300000, true);
				}
			}
			
			// Solving Tor's request
			final int request = st.getInteger("tor");
			if (request > 0)
			{
				// Get request and reward data.
				final TorRequestReward reward = TOR_REQUEST_REWARD.get(request);
				
				// Dropping quest item.
				final TorRequestDroplist trd = getTorRequestDroplist(request, npcId);
				if (trd != null && !dropItems(player, reward.getId(), 1, reward.getRequired(), trd.getChance()))
					trd.tryToSpeak(npc);
					
				// Spawn another NPC when :
				// Marked as reached and collected quest item reaches required quantity.
				// Marked as non-reached and collected quest item are below required quantity.
				final TorRequestSpawn trs = getTorRequestSpawn(request, npcId);
				if (trs != null && Rnd.get(100) < trs.getChance() && trs.isReached() == (player.getInventory().getItemCount(reward.getId()) >= reward.getRequired()))
					trs.spawn(npc, player);
			}
			
			// Solving Cybellin's request ; the proper monster was killed using Cybellin's Dagger.
			if (st.getInteger("cybellin") > 0 && ArraysUtil.contains(CYBELLIN_REQUEST_DROPLIST, npc.getNpcId()) && player.getInventory().getItemIdFrom(Paperdoll.RHAND) == CYBELLIN_DAGGER)
			{
				final boolean isSuccessful = Rnd.get(100) < 60;
				
				for (int bloodCrystal : CYBELLIN_REQUEST_REWARDS.keySet())
				{
					if (bloodCrystal == BLOOD_CRYSTAL_10 || !player.getInventory().hasItem(bloodCrystal))
						continue;
					
					// Delete actual crystal.
					takeItems(player, bloodCrystal, -1);
					
					// Success, stage up first found crystal.
					if (isSuccessful)
					{
						// Reward upgraded crystal.
						giveItems(player, bloodCrystal + 1, 1);
						
						// Play sound for upgrades above stage 6.
						if (bloodCrystal >= BLOOD_CRYSTAL_06)
							playSound(player, SOUND_JACKPOT);
					}
					// Fail, give broken crystal.
					else
						giveItems(player, BROKEN_BLOOD, 1);
					
					break;
				}
			}
		}
	}
	
	/**
	 * @param player : The {@link Player} to check.
	 * @param requirements : The reward data container to check data.
	 * @return True, when given requirement have been met at least in 3 cases.
	 */
	private static boolean passedGreyRequest(Player player, List<GreyRequirement> requirements)
	{
		int passed = 0;
		
		for (GreyRequirement e : requirements)
		{
			int count = e.getRequired();
			
			boolean pass = true;
			for (int itemId : e.getIds())
			{
				if (player.getInventory().getItemCount(itemId) < count)
					pass = false;
			}
			
			if (pass)
				passed++;
		}
		
		return passed >= 3;
	}
	
	/**
	 * @param request : The request item ID.
	 * @param monster : The killed monster NPC ID.
	 * @return The proper {@link TorRequestDroplist} for given request and monster.
	 */
	private static final TorRequestDroplist getTorRequestDroplist(int request, int monster)
	{
		return TOR_REQUEST_DROPLIST.stream().filter(trd -> trd.test(request, monster)).findFirst().orElse(null);
	}
	
	/**
	 * @param request : The request item ID.
	 * @param monster : The killed monster NPC ID.
	 * @return The proper {@link TorRequestSpawn} for given request and monster.
	 */
	private static final TorRequestSpawn getTorRequestSpawn(int request, int monster)
	{
		return TOR_REQUEST_SPAWN.stream().filter(trs -> trs.test(request, monster)).findFirst().orElse(null);
	}
}