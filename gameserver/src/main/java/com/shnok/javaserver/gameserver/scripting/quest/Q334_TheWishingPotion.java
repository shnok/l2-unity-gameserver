package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q334_TheWishingPotion extends Quest
{
	private static final String QUEST_NAME = "Q334_TheWishingPotion";
	
	// Npcs
	private static final int TORAI = 30557;
	private static final int ALCHEMIST_MATILD = 30738;
	private static final int FAIRY_RUPINA = 30742;
	private static final int CHEST_OF_WISDOM = 30743;
	
	// Mobs
	private static final int WHISPERING_WIND = 20078;
	private static final int ANT_SOLDIER = 20087;
	private static final int ANT_WARRIOR_CAPTAIN = 20088;
	private static final int SILENOS = 20168;
	private static final int TYRANT = 20192;
	private static final int TYRANT_KINGPIN = 20193;
	private static final int AMBER_BASILISK = 20199;
	private static final int HORROR_MIST_RIPPER = 20227;
	private static final int TURAK_BUGBEAR = 20248;
	private static final int TURAK_BUGBEAR_WARRIOR = 20249;
	private static final int GLASS_JAGUAR = 20250;
	private static final int GRIMA = 27135;
	private static final int SUCCUBUS_OF_SEDUCTION = 27136;
	private static final int GREAT_DEMON_KING = 27138;
	private static final int SECRET_KEEPER_TREE = 27139;
	private static final int ALEXANDRO_SANCHES = 27153;
	private static final int BONAPARTERIUS = 27154;
	private static final int RAMSEBALIUS = 27155;
	
	// Quest Items
	private static final int ALCHEMY_TEXT = 3678;
	private static final int SECRET_BOOK = 3679;
	private static final int FIRST_POTION_RECIPE = 3680;
	private static final int SECOND_POTION_RECIPE = 3681;
	private static final int MATILD_ORB = 3682;
	private static final int FORBIDDEN_LOVE_SCROLL = 3683;
	private static final int AMBER_SCALE = 3684;
	private static final int WIND_SOULSTONE = 3685;
	private static final int GLASS_EYE = 3686;
	private static final int HORROR_ECTOPLASM = 3687;
	private static final int SILENOS_HORN = 3688;
	private static final int ANT_SOLDIER_APHID = 3689;
	private static final int TYRANTS_CHITIN = 3690;
	private static final int BUGBEAR_BLOOD = 3691;
	
	// Reward
	private static final int ADENA = 57;
	private static final int NECKLACE_OF_GRACE = 931;
	private static final int DEMON_TUNIC_FABRIC = 1979;
	private static final int DEMON_HOSE_PATTERN = 1980;
	private static final int DEMON_BOOTS_FABRIC = 2952;
	private static final int DEMON_GLOVES_FABRIC = 2953;
	private static final int SUMMON_STORM_CUBIC = 3039;
	private static final int SUMMON_PHANTOM_CUBIC = 3041;
	private static final int SUMMON_LIFE_CUBIC = 3045;
	private static final int SUMMON_REANIMATED_MAN = 3057;
	private static final int SUMMON_CORRUPTED_MAN = 3058;
	private static final int CORPSE_BURST = 3059;
	private static final int FORGET = 3060;
	private static final int CURSE_DISCORD = 3061;
	private static final int CURSE_FEAR = 3062;
	private static final int ANCHOR = 3063;
	private static final int SILENCE = 3064;
	private static final int DEATH_SPIKE = 3065;
	private static final int CURSE_DEATH_LINK = 3066;
	private static final int VAMPIRIC_CLAW = 3067;
	private static final int VITALIZE = 3068;
	private static final int REPOSE = 3069;
	private static final int HOLD_UNDEAD = 3070;
	private static final int REQUIEM = 3071;
	private static final int PURIFY = 3072;
	private static final int MIGHT_OF_HEAVEN = 3073;
	private static final int SURRENDER_TO_WIND = 3074;
	private static final int BLAZING_CIRCLE = 3075;
	private static final int PROMINENCE = 3076;
	private static final int BLAZING_SKIN = 3077;
	private static final int DECAY = 3078;
	private static final int CANCEL = 3079;
	private static final int SLEEPING_CLOUD = 3080;
	private static final int AURA_FLARE = 3081;
	private static final int SURRENDER_TO_WATER = 3082;
	private static final int FROST_WALL = 3083;
	private static final int FREEZING_SHACKLE = 3084;
	private static final int HYDRO_BLAST = 3085;
	private static final int FROST_BOLT = 3086;
	private static final int ICE_DAGGER = 3087;
	private static final int FREEZING_SKIN = 3088;
	private static final int TEMPEST = 3089;
	private static final int HURRICANE = 3090;
	private static final int SERVITOR_MAGIC_SHIELD = 3091;
	private static final int SERVITOR_PHYSICAL_SHIELD = 3092;
	private static final int SERVITOR_HASTE = 3093;
	private static final int INVIGOR = 3094;
	private static final int MAGIC_BARRIER = 3095;
	private static final int BLESSED_BODY = 3096;
	private static final int BLESSED_SOUL = 3097;
	private static final int RETURN = 3098;
	private static final int HASTE = 3099;
	private static final int GUIDANCE = 3100;
	private static final int DEATH_WHISPER = 3101;
	private static final int BLESS_SHIELD = 3102;
	private static final int PAAGRIO_WISDOM = 3103;
	private static final int PAAGRIO_GLORY = 3104;
	private static final int SEAL_OF_WINTER = 3105;
	private static final int SEAL_OF_FLAME = 3106;
	private static final int SEAL_OF_GLOOM = 3107;
	private static final int SEAL_OF_MIRAGE = 3108;
	private static final int SEAL_OF_SILENCE = 3109;
	private static final int SEAL_OF_SCOURGE = 3110;
	private static final int SEAL_OF_SUSPENSION = 3111;
	private static final int PAAGRIO_VISION = 3112;
	private static final int PAAGRIO_PROTECTION = 3113;
	private static final int STEAL_ESSENCE = 3114;
	private static final int FREEZING_FLAME = 3115;
	private static final int CHANT_OF_FURY = 3116;
	private static final int CHANT_OF_EVASION = 3117;
	private static final int CHANT_OF_RAGE = 3118;
	private static final int GREATER_HEAL = 3429;
	private static final int GREATER_BATTLE_HEAL = 3430;
	private static final int GREATER_GROUP_HEAL = 3431;
	private static final int WISH_POTION = 3467;
	private static final int ANCIENT_CROWN = 3468;
	private static final int CERTIFICATE_OF_ROYALTY = 3469;
	private static final int MASS_RESSURECTION = 3941;
	private static final int PARTY_RETURN = 3942;
	private static final int HEART_OF_PAAGRIO = 3943;
	private static final int RESTORE_LIFE = 4200;
	private static final int RESIST_SHOCK = 4201;
	private static final int PAAGRIO_TACK = 4204;
	private static final int PAAGRIO_RAGE = 4205;
	private static final int TRANSFER_PAIN = 4206;
	private static final int MANA_REGENERATION = 4207;
	private static final int CURSE_GLOOM = 4208;
	private static final int MUSICAL_SCORE_THEME_OF_LOVE = 4408;
	private static final int MUSICAL_SCORE_THEME_OF_BATTLE = 4409;
	private static final int SOLAR_FLARE = 4907;
	private static final int SHADOW_FLARE = 4909;
	private static final int CURSE_DISEASE = 4911;
	private static final int BENEDICTION = 4912;
	private static final int WORD_OF_FEAR = 4913;
	private static final int SERENADE_OF_EVA = 4914;
	private static final int AURA_BOLT = 4917;
	private static final int SUMMON_KAI_THE_CAT = 4918;
	private static final int SUMMON_MERROW_THE_UNICORN = 4919;
	private static final int SUMMON_SOULLESS = 4920;
	private static final int SUMMON_BINDING_CUBIC = 4922;
	private static final int SUMMON_AQUA_CUBIC = 4923;
	private static final int SUMMON_SPARK_CUBIC = 4924;
	private static final int PAAGRIO_HASTE = 4925;
	private static final int SOUL_GUARD = 4926;
	private static final int CHANT_OF_REVENGE = 4927;
	private static final int SEED_OF_FIRE = 4928;
	private static final int SEED_OF_WATER = 4929;
	private static final int SEED_OF_WIND = 4930;
	private static final int AURA_SYMPHONY = 4931;
	private static final int INFERNO = 4932;
	private static final int BLIZZARD = 4933;
	private static final int DEMON_WIND = 4934;
	private static final int ELEMENTAL_ASSAULT = 5013;
	
	private static final int[] MATILD_REWARD_LIST_1 =
	{
		AURA_FLARE,
		PROMINENCE,
		BLAZING_CIRCLE,
		SURRENDER_TO_WIND,
		AURA_BOLT,
		BLAZING_SKIN,
		SLEEPING_CLOUD,
		CANCEL,
		DECAY,
		SEED_OF_FIRE,
		AURA_SYMPHONY,
		INFERNO,
		ELEMENTAL_ASSAULT,
		VAMPIRIC_CLAW,
		SILENCE,
		CURSE_DISCORD,
		CURSE_FEAR,
		SUMMON_CORRUPTED_MAN,
		TRANSFER_PAIN,
		DEATH_SPIKE,
		FORGET,
		ANCHOR,
		CURSE_GLOOM,
		SUMMON_REANIMATED_MAN,
		CORPSE_BURST,
		CURSE_DEATH_LINK,
		CURSE_DISEASE,
		SUMMON_KAI_THE_CAT,
		SERVITOR_PHYSICAL_SHIELD,
		SUMMON_STORM_CUBIC,
		SUMMON_BINDING_CUBIC,
		SERVITOR_MAGIC_SHIELD,
		SERVITOR_HASTE,
		GREATER_GROUP_HEAL
	};
	
	private static final int[] MATILD_REWARD_LIST_2 =
	{
		GREATER_BATTLE_HEAL,
		GREATER_HEAL,
		MIGHT_OF_HEAVEN,
		MASS_RESSURECTION,
		REQUIEM,
		REPOSE,
		PURIFY,
		RESTORE_LIFE,
		VITALIZE,
		HOLD_UNDEAD,
		BENEDICTION,
		GUIDANCE,
		DEATH_WHISPER,
		RETURN,
		INVIGOR,
		BLESS_SHIELD,
		WORD_OF_FEAR,
		MAGIC_BARRIER,
		BLESSED_BODY,
		BLESSED_SOUL,
		HASTE,
		HYDRO_BLAST,
		FROST_BOLT,
		SURRENDER_TO_WATER,
		SOLAR_FLARE,
		FREEZING_SKIN,
		MANA_REGENERATION,
		ICE_DAGGER,
		FREEZING_SHACKLE,
		FROST_WALL,
		SEED_OF_WATER,
		BLIZZARD,
		SUMMON_MERROW_THE_UNICORN,
		SUMMON_LIFE_CUBIC
	};
	
	private static final int[] MATILD_REWARD_LIST_3 =
	{
		SUMMON_AQUA_CUBIC,
		RESIST_SHOCK,
		SERENADE_OF_EVA,
		PARTY_RETURN,
		HURRICANE,
		SHADOW_FLARE,
		TEMPEST,
		SEED_OF_WIND,
		DEMON_WIND,
		SUMMON_SOULLESS,
		SUMMON_PHANTOM_CUBIC,
		SUMMON_SPARK_CUBIC,
		STEAL_ESSENCE,
		SEAL_OF_WINTER,
		SEAL_OF_SCOURGE,
		PAAGRIO_GLORY,
		PAAGRIO_PROTECTION,
		PAAGRIO_WISDOM,
		PAAGRIO_TACK,
		SEAL_OF_MIRAGE,
		SOUL_GUARD,
		PAAGRIO_VISION,
		SEAL_OF_GLOOM,
		PAAGRIO_RAGE,
		SEAL_OF_SILENCE,
		SEAL_OF_SUSPENSION,
		SEAL_OF_FLAME,
		PAAGRIO_HASTE,
		CHANT_OF_EVASION,
		FREEZING_FLAME,
		CHANT_OF_RAGE,
		CHANT_OF_FURY,
		CHANT_OF_REVENGE
	};
	
	private static final int[] CHEST_REWARD_LIST_1 =
	{
		AURA_FLARE,
		PROMINENCE,
		BLAZING_CIRCLE,
		SURRENDER_TO_WIND,
		AURA_BOLT,
		BLAZING_SKIN,
		SLEEPING_CLOUD,
		CANCEL,
		DECAY,
		SEED_OF_FIRE,
		AURA_SYMPHONY,
		INFERNO,
		ELEMENTAL_ASSAULT,
		VAMPIRIC_CLAW,
		SILENCE,
		CURSE_DISCORD,
		CURSE_FEAR,
		SUMMON_CORRUPTED_MAN,
		TRANSFER_PAIN,
		DEATH_SPIKE,
		FORGET,
		ANCHOR,
		CURSE_GLOOM,
		SUMMON_REANIMATED_MAN,
		CORPSE_BURST,
		CURSE_DEATH_LINK,
		CURSE_DISEASE,
		SUMMON_KAI_THE_CAT,
		SERVITOR_PHYSICAL_SHIELD,
		SUMMON_STORM_CUBIC,
		SUMMON_BINDING_CUBIC,
		SERVITOR_MAGIC_SHIELD,
		SERVITOR_HASTE,
		GREATER_GROUP_HEAL
	};
	
	private static final int[] CHEST_REWARD_LIST_2 =
	{
		GREATER_BATTLE_HEAL,
		GREATER_HEAL,
		MIGHT_OF_HEAVEN,
		MASS_RESSURECTION,
		REQUIEM,
		REPOSE,
		PURIFY,
		RESTORE_LIFE,
		VITALIZE,
		HOLD_UNDEAD,
		BENEDICTION,
		GUIDANCE,
		DEATH_WHISPER,
		RETURN,
		INVIGOR,
		BLESS_SHIELD,
		WORD_OF_FEAR,
		MAGIC_BARRIER,
		BLESSED_BODY,
		BLESSED_SOUL,
		HASTE,
		HYDRO_BLAST,
		FROST_BOLT,
		SURRENDER_TO_WATER,
		SOLAR_FLARE,
		FREEZING_SKIN,
		MANA_REGENERATION,
		ICE_DAGGER,
		FREEZING_SHACKLE,
		FROST_WALL,
		SEED_OF_WATER,
		BLIZZARD,
		SUMMON_MERROW_THE_UNICORN,
		SUMMON_LIFE_CUBIC
	};
	
	private static final int[] CHEST_REWARD_LIST_3 =
	{
		SUMMON_AQUA_CUBIC,
		RESIST_SHOCK,
		SERENADE_OF_EVA,
		PARTY_RETURN,
		HURRICANE,
		SHADOW_FLARE,
		TEMPEST,
		SEED_OF_WIND,
		DEMON_WIND,
		SUMMON_SOULLESS,
		SUMMON_PHANTOM_CUBIC,
		SUMMON_SPARK_CUBIC,
		STEAL_ESSENCE,
		SEAL_OF_WINTER,
		SEAL_OF_SCOURGE,
		PAAGRIO_GLORY,
		PAAGRIO_PROTECTION,
		PAAGRIO_WISDOM,
		PAAGRIO_TACK,
		SEAL_OF_MIRAGE,
		SOUL_GUARD,
		PAAGRIO_VISION,
		SEAL_OF_GLOOM,
		PAAGRIO_RAGE,
		SEAL_OF_SILENCE,
		SEAL_OF_SUSPENSION,
		SEAL_OF_FLAME,
		PAAGRIO_HASTE,
		CHANT_OF_EVASION,
		FREEZING_FLAME,
		CHANT_OF_RAGE,
		CHANT_OF_FURY,
		CHANT_OF_REVENGE
	};
	
	private static final int[] DEMON_SET_PARTS_REWARD_LIST =
	{
		DEMON_TUNIC_FABRIC,
		DEMON_HOSE_PATTERN,
		DEMON_BOOTS_FABRIC,
		DEMON_GLOVES_FABRIC
	};
	
	public Q334_TheWishingPotion()
	{
		super(334, "The Wishing Potion");
		
		setItemsIds(ALCHEMY_TEXT, SECRET_BOOK, FIRST_POTION_RECIPE, SECOND_POTION_RECIPE, MATILD_ORB, FORBIDDEN_LOVE_SCROLL, AMBER_SCALE, WIND_SOULSTONE, ANT_SOLDIER_APHID, GLASS_EYE, HORROR_ECTOPLASM, SILENOS_HORN, BUGBEAR_BLOOD, TYRANTS_CHITIN);
		
		addQuestStart(ALCHEMIST_MATILD);
		addTalkId(TORAI, ALCHEMIST_MATILD, FAIRY_RUPINA, CHEST_OF_WISDOM);
		
		addCreated(GRIMA, SUCCUBUS_OF_SEDUCTION, GREAT_DEMON_KING, ALEXANDRO_SANCHES, BONAPARTERIUS, RAMSEBALIUS, FAIRY_RUPINA, CHEST_OF_WISDOM);
		addDecayed(GREAT_DEMON_KING, ALEXANDRO_SANCHES, BONAPARTERIUS, RAMSEBALIUS);
		addMyDying(WHISPERING_WIND, ANT_SOLDIER, ANT_WARRIOR_CAPTAIN, SILENOS, TYRANT, TYRANT_KINGPIN, AMBER_BASILISK, HORROR_MIST_RIPPER, TURAK_BUGBEAR, TURAK_BUGBEAR_WARRIOR, GLASS_JAGUAR, GRIMA, SUCCUBUS_OF_SEDUCTION, GREAT_DEMON_KING, SECRET_KEEPER_TREE, ALEXANDRO_SANCHES, BONAPARTERIUS, RAMSEBALIUS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30738-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ALCHEMY_TEXT, 1);
		}
		else if (event.equalsIgnoreCase("30738-07.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ALCHEMY_TEXT, -1);
			takeItems(player, SECRET_BOOK, -1);
			giveItems(player, FIRST_POTION_RECIPE, 1);
			giveItems(player, SECOND_POTION_RECIPE, 1);
		}
		else if (event.equalsIgnoreCase("30738-11.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_ITEMGET);
			takeItems(player, FIRST_POTION_RECIPE, -1);
			takeItems(player, SECOND_POTION_RECIPE, -1);
			takeItems(player, AMBER_SCALE, -1);
			takeItems(player, WIND_SOULSTONE, -1);
			takeItems(player, GLASS_EYE, -1);
			takeItems(player, HORROR_ECTOPLASM, -1);
			takeItems(player, SILENOS_HORN, -1);
			takeItems(player, ANT_SOLDIER_APHID, -1);
			takeItems(player, TYRANTS_CHITIN, -1);
			takeItems(player, BUGBEAR_BLOOD, -1);
			giveItems(player, WISH_POTION, 1);
			
			if (!player.getInventory().hasItem(MATILD_ORB))
				giveItems(player, MATILD_ORB, 1);
		}
		else if (event.equalsIgnoreCase("30738-14.htm"))
		{
			if (player.getInventory().hasItem(WISH_POTION))
			{
				htmltext = "30738-13.htm";
				if (st.getInteger("wish") != 1)
					st.set("wish", 0);
			}
		}
		else if (event.equalsIgnoreCase("30738-15a.htm"))
		{
			if (!player.getInventory().hasItem(WISH_POTION))
			{
				htmltext = "30738-15.htm";
				giveItems(player, FIRST_POTION_RECIPE, 1);
				giveItems(player, SECOND_POTION_RECIPE, 1);
			}
		}
		else if (event.equalsIgnoreCase("30738-16.htm"))
		{
			exchangeWishPotion(st, event, npc, player, 1);
		}
		else if (event.equalsIgnoreCase("30738-17.htm"))
		{
			exchangeWishPotion(st, event, npc, player, 2);
		}
		else if (event.equalsIgnoreCase("30738-18.htm"))
		{
			exchangeWishPotion(st, event, npc, player, 3);
		}
		else if (event.equalsIgnoreCase("30738-19.htm"))
		{
			exchangeWishPotion(st, event, npc, player, 4);
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
				htmltext = (player.getStatus().getLevel() < 30) ? "30738-01.htm" : "30738-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case ALCHEMIST_MATILD:
						if (player.getInventory().hasItem(ALCHEMY_TEXT))
							htmltext = player.getInventory().hasItem(SECRET_BOOK) ? "30738-06.htm" : "30738-05.htm";
						else if (player.getInventory().hasItems(FIRST_POTION_RECIPE, SECOND_POTION_RECIPE))
							htmltext = player.getInventory().hasItems(AMBER_SCALE, WIND_SOULSTONE, GLASS_EYE, HORROR_ECTOPLASM, SILENOS_HORN, ANT_SOLDIER_APHID, TYRANTS_CHITIN, BUGBEAR_BLOOD) ? "30738-09.htm" : "30738-08.htm";
						else if (player.getInventory().hasItem(MATILD_ORB))
							htmltext = "30738-12.htm";
						break;
					
					case FAIRY_RUPINA:
						if (st.getInteger("wish") == 1 && st.getInteger("choice") == 0)
						{
							if (Rnd.get(100) < 4)
							{
								htmltext = "30742-01.htm";
								giveItems(player, NECKLACE_OF_GRACE, 1);
							}
							else
							{
								htmltext = "30742-02.htm";
								giveItems(player, Rnd.get(DEMON_SET_PARTS_REWARD_LIST), 1);
							}
							
							st.set("wish", 0);
							playSound(player, SOUND_ITEMGET);
							npc.deleteMe();
						}
						break;
					
					case TORAI:
						if (player.getInventory().hasItem(FORBIDDEN_LOVE_SCROLL))
						{
							htmltext = "30557-01.htm";
							playSound(player, SOUND_ITEMGET);
							takeItems(player, FORBIDDEN_LOVE_SCROLL, 1);
							giveItems(player, ADENA, 500000);
						}
						break;
					
					case CHEST_OF_WISDOM:
						if (st.getInteger("wish") == 4)
						{
							final int rand = Rnd.get(10);
							if (rand < 2)
								htmltext = "30743-01.htm";
							else if (rand < 4)
								htmltext = "30743-02.htm";
							else if (rand < 6)
								htmltext = "30743-03.htm";
							else if (rand == 6)
								htmltext = "30743-04.htm";
							else if (rand < 9)
								htmltext = "30743-05.htm";
							else
								htmltext = "30743-06.htm";
							
							playSound(player, SOUND_ITEMGET);
							
							giveItems(player, Rnd.get(CHEST_REWARD_LIST_1), 1);
							giveItems(player, Rnd.get(CHEST_REWARD_LIST_2), 1);
							giveItems(player, Rnd.get(CHEST_REWARD_LIST_3), 1);
							
							if (Rnd.get(3) == 0)
								giveItems(player, HEART_OF_PAAGRIO, 1);
							
							giveItems(player, MUSICAL_SCORE_THEME_OF_LOVE, 1);
							giveItems(player, MUSICAL_SCORE_THEME_OF_BATTLE, 1);
							
							st.set("wish", 0);
							npc.deleteMe();
						}
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equals("timer_1"))
		{
			npc.broadcastNpcSay(NpcStringId.ID_33415);
			startQuestTimer("timer_2", npc, player, 4000);
		}
		else if (name.equals("timer_2"))
		{
			npc.broadcastNpcSay(NpcStringId.ID_33416);
			startQuestTimer("timer_3", npc, player, 4000);
		}
		else if (name.equalsIgnoreCase("timer_3"))
		{
			npc.broadcastNpcSay(NpcStringId.ID_33417);
			
			QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
			if (st == null)
				return null;
			
			final int wish = st.getInteger("wish");
			final int choice = (wish == 1) ? Rnd.get(2) : Rnd.get(3);
			
			if (choice == 0)
			{
				switch (wish)
				{
					case 1:
						st.set("choice", 0);
						addSpawn(FAIRY_RUPINA, player, true, 120000, true);
						break;
					
					case 2:
						st.set("choice", 0);
						for (int i = 0; i < 3; i++)
							addSpawn(GRIMA, player, true, 200000, true);
						break;
					
					case 3:
						st.set("wish", 0);
						st.set("choice", 0);
						playSound(player, SOUND_ITEMGET);
						giveItems(player, CERTIFICATE_OF_ROYALTY, 1);
						break;
					
					case 4:
						st.set("wish", 0);
						st.set("choice", 0);
						playSound(player, SOUND_ITEMGET);
						giveItems(player, Rnd.get(MATILD_REWARD_LIST_1), 1);
						giveItems(player, Rnd.get(MATILD_REWARD_LIST_2), 1);
						giveItems(player, Rnd.get(MATILD_REWARD_LIST_3), 1);
						
						if (Rnd.get(3) == 0)
							giveItems(player, HEART_OF_PAAGRIO, 1);
						break;
				}
			}
			else if (choice == 1)
			{
				switch (wish)
				{
					case 1:
						st.set("choice", 0);
						for (int i = 0; i < 3; i++)
							addSpawn(SUCCUBUS_OF_SEDUCTION, player, true, 200000, true);
						break;
					
					case 2:
						st.set("wish", 0);
						st.set("choice", 0);
						giveItems(player, ADENA, 10000);
						break;
					
					case 3:
						st.set("choice", 0);
						addSpawn(ALEXANDRO_SANCHES, player, true, 200000, true);
						break;
					
					case 4:
						st.set("choice", 1);
						addSpawn(CHEST_OF_WISDOM, player, true, 120000, true);
						break;
				}
			}
			else if (choice == 2)
			{
				switch (wish)
				{
					case 2:
						st.set("wish", 0);
						st.set("choice", 0);
						giveItems(player, ADENA, 10000);
						break;
					
					case 3:
						st.set("wish", 0);
						st.set("choice", 0);
						playSound(player, SOUND_ITEMGET);
						giveItems(player, ANCIENT_CROWN, 1);
						break;
					
					case 4:
						st.set("choice", 2);
						addSpawn(CHEST_OF_WISDOM, player, true, 120000, true);
						break;
				}
			}
			st.set("exchange", 0);
		}
		return null;
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		switch (npc.getNpcId())
		{
			case FAIRY_RUPINA:
				npc.broadcastNpcSay(NpcStringId.ID_33420);
				break;
			
			case GRIMA:
				npc.broadcastNpcSay(NpcStringId.ID_33422);
				break;
			
			case SUCCUBUS_OF_SEDUCTION:
				npc.broadcastNpcSay(NpcStringId.ID_33423);
				break;
			
			case ALEXANDRO_SANCHES:
				npc.broadcastNpcSay(NpcStringId.ID_33424);
				break;
			
			case BONAPARTERIUS:
				npc.broadcastNpcSay(NpcStringId.ID_33425);
				break;
			
			case RAMSEBALIUS:
				npc.broadcastNpcSay(NpcStringId.ID_33426);
				break;
			
			case GREAT_DEMON_KING:
				npc.broadcastNpcSay(NpcStringId.ID_33418);
				break;
			
			case CHEST_OF_WISDOM:
				npc.broadcastNpcSay(NpcStringId.ID_33421);
				break;
		}
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		if (!npc.isDead())
		{
			switch (npc.getNpcId())
			{
				case ALEXANDRO_SANCHES, BONAPARTERIUS, RAMSEBALIUS:
					npc.broadcastNpcSay(NpcStringId.ID_33411);
					break;
				
				case GREAT_DEMON_KING:
					npc.broadcastNpcSay(NpcStringId.ID_33409);
					npc.broadcastNpcSay(NpcStringId.ID_33410);
					break;
			}
		}
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
			case SECRET_KEEPER_TREE:
				if (st.getCond() == 1)
				{
					st.setCond(2);
					dropItemsAlways(player, SECRET_BOOK, 1, 1);
				}
				break;
			
			case AMBER_BASILISK:
				checkDropAndReward(player, st, st.getCond(), AMBER_SCALE);
				break;
			
			case ANT_SOLDIER, ANT_WARRIOR_CAPTAIN:
				checkDropAndReward(player, st, st.getCond(), ANT_SOLDIER_APHID);
				break;
			
			case GLASS_JAGUAR:
				checkDropAndReward(player, st, st.getCond(), GLASS_EYE);
				break;
			
			case HORROR_MIST_RIPPER:
				checkDropAndReward(player, st, st.getCond(), HORROR_ECTOPLASM);
				break;
			
			case SILENOS:
				checkDropAndReward(player, st, st.getCond(), SILENOS_HORN);
				break;
			
			case TURAK_BUGBEAR, TURAK_BUGBEAR_WARRIOR:
				checkDropAndReward(player, st, st.getCond(), BUGBEAR_BLOOD);
				break;
			
			case TYRANT, TYRANT_KINGPIN:
				checkDropAndReward(player, st, st.getCond(), TYRANTS_CHITIN);
				break;
			
			case WHISPERING_WIND:
				checkDropAndReward(player, st, st.getCond(), WIND_SOULSTONE);
				break;
			
			case SUCCUBUS_OF_SEDUCTION:
				if (st.getCond() == 5 && !player.getInventory().hasItem(FORBIDDEN_LOVE_SCROLL) && st.getInteger("wish") == 1)
				{
					if (dropItems(player, FORBIDDEN_LOVE_SCROLL, 1, 0, 28000))
						st.set("wish", 0);
				}
				break;
			
			case GRIMA:
				if (st.getCond() == 5 && st.getInteger("wish") == 2 && Rnd.get(1000) < 33)
				{
					st.set("wish", 0);
					playSound(player, SOUND_ITEMGET);
					
					if (Rnd.get(1000) == 0)
						giveItems(player, ADENA, 100000000);
					else
						giveItems(player, ADENA, 900000);
				}
				break;
			
			case ALEXANDRO_SANCHES:
				checkRewardOrSpawn(player, npc, BONAPARTERIUS, NpcStringId.ID_33414);
				break;
			
			case BONAPARTERIUS:
				checkRewardOrSpawn(player, npc, RAMSEBALIUS, NpcStringId.ID_33413);
				break;
			
			case RAMSEBALIUS:
				checkRewardOrSpawn(player, npc, GREAT_DEMON_KING, NpcStringId.ID_33412);
				break;
			
			case GREAT_DEMON_KING:
				st.set("wish", 0);
				playSound(player, SOUND_ITEMGET);
				giveItems(player, ADENA, 1406956);
				break;
		}
	}
	
	private static void checkDropAndReward(Player player, QuestState st, int cond, int itemId)
	{
		if ((cond == 3 || (cond == 5 && player.getInventory().hasItem(MATILD_ORB))) && player.getInventory().hasItems(FIRST_POTION_RECIPE, SECOND_POTION_RECIPE) && !player.getInventory().hasItem(itemId))
		{
			if (isLastIngredient(player, itemId))
			{
				if (dropItems(player, itemId, 1, 1, 100000))
					st.setCond(4);
			}
			else
				dropItems(player, itemId, 1, 0, 100000);
		}
	}
	
	private void checkRewardOrSpawn(Player player, Npc npc, int npcId, NpcStringId message)
	{
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		if (st.getCond() == 5 && st.getInteger("wish") == 3)
		{
			npc.broadcastNpcSay(message);
			
			if (Rnd.nextBoolean())
				addSpawn(npcId, player, true, 200000, true);
			else
			{
				st.set("wish", 0);
				playSound(player, SOUND_ITEMGET);
				giveItems(player, Rnd.get(DEMON_SET_PARTS_REWARD_LIST), 1);
			}
		}
	}
	
	private static boolean isLastIngredient(Player player, int current)
	{
		if (current != AMBER_SCALE && !player.getInventory().hasItem(AMBER_SCALE))
			return false;
		
		if (current != WIND_SOULSTONE && !player.getInventory().hasItem(WIND_SOULSTONE))
			return false;
		
		if (current != GLASS_EYE && !player.getInventory().hasItem(GLASS_EYE))
			return false;
		
		if (current != HORROR_ECTOPLASM && !player.getInventory().hasItem(HORROR_ECTOPLASM))
			return false;
		
		if (current != SILENOS_HORN && !player.getInventory().hasItem(SILENOS_HORN))
			return false;
		
		if (current != ANT_SOLDIER_APHID && !player.getInventory().hasItem(ANT_SOLDIER_APHID))
			return false;
		
		if (current != TYRANTS_CHITIN && !player.getInventory().hasItem(TYRANTS_CHITIN))
			return false;
		
		if (current != BUGBEAR_BLOOD && !player.getInventory().hasItem(BUGBEAR_BLOOD))
			return false;
		
		return true;
	}
	
	private String exchangeWishPotion(QuestState st, String event, Npc npc, Player player, int wish)
	{
		if (!player.getInventory().hasItem(WISH_POTION))
			return "30738-14.htm";
		
		if (st.getInteger("exchange") != 0)
			return "30738-20.htm";
		
		takeItems(player, WISH_POTION, 1);
		
		st.set("wish", wish);
		st.set("exchange", wish);
		
		startQuestTimer("timer_1", npc, player, 3000);
		return event;
	}
}