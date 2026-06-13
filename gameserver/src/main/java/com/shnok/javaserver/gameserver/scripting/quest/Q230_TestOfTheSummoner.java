package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Q230_TestOfTheSummoner extends SecondClassQuest
{
	public static final String QUEST_NAME = "Q230_TestOfTheSummoner";
	
	// Items
	private static final int LETO_LIZARDMAN_AMULET = 3337;
	private static final int SAC_OF_REDSPORES = 3338;
	private static final int KARUL_BUGBEAR_TOTEM = 3339;
	private static final int SHARDS_OF_MANASHEN = 3340;
	private static final int BREKA_ORC_TOTEM = 3341;
	private static final int CRIMSON_BLOODSTONE = 3342;
	private static final int TALONS_OF_TYRANT = 3343;
	private static final int WINGS_OF_DRONEANT = 3344;
	private static final int TUSK_OF_WINDSUS = 3345;
	private static final int FANGS_OF_WYRM = 3346;
	private static final int LARA_LIST_1 = 3347;
	private static final int LARA_LIST_2 = 3348;
	private static final int LARA_LIST_3 = 3349;
	private static final int LARA_LIST_4 = 3350;
	private static final int LARA_LIST_5 = 3351;
	private static final int GALATEA_LETTER = 3352;
	private static final int BEGINNER_ARCANA = 3353;
	private static final int ALMORS_ARCANA = 3354;
	private static final int CAMONIELL_ARCANA = 3355;
	private static final int BELTHUS_ARCANA = 3356;
	private static final int BASILLIA_ARCANA = 3357;
	private static final int CELESTIEL_ARCANA = 3358;
	private static final int BRYNTHEA_ARCANA = 3359;
	private static final int CRYSTAL_OF_PROGRESS_1 = 3360;
	private static final int CRYSTAL_OF_INPROGRESS_1 = 3361;
	private static final int CRYSTAL_OF_FOUL_1 = 3362;
	private static final int CRYSTAL_OF_DEFEAT_1 = 3363;
	private static final int CRYSTAL_OF_VICTORY_1 = 3364;
	private static final int CRYSTAL_OF_PROGRESS_2 = 3365;
	private static final int CRYSTAL_OF_INPROGRESS_2 = 3366;
	private static final int CRYSTAL_OF_FOUL_2 = 3367;
	private static final int CRYSTAL_OF_DEFEAT_2 = 3368;
	private static final int CRYSTAL_OF_VICTORY_2 = 3369;
	private static final int CRYSTAL_OF_PROGRESS_3 = 3370;
	private static final int CRYSTAL_OF_INPROGRESS_3 = 3371;
	private static final int CRYSTAL_OF_FOUL_3 = 3372;
	private static final int CRYSTAL_OF_DEFEAT_3 = 3373;
	private static final int CRYSTAL_OF_VICTORY_3 = 3374;
	private static final int CRYSTAL_OF_PROGRESS_4 = 3375;
	private static final int CRYSTAL_OF_INPROGRESS_4 = 3376;
	private static final int CRYSTAL_OF_FOUL_4 = 3377;
	private static final int CRYSTAL_OF_DEFEAT_4 = 3378;
	private static final int CRYSTAL_OF_VICTORY_4 = 3379;
	private static final int CRYSTAL_OF_PROGRESS_5 = 3380;
	private static final int CRYSTAL_OF_INPROGRESS_5 = 3381;
	private static final int CRYSTAL_OF_FOUL_5 = 3382;
	private static final int CRYSTAL_OF_DEFEAT_5 = 3383;
	private static final int CRYSTAL_OF_VICTORY_5 = 3384;
	private static final int CRYSTAL_OF_PROGRESS_6 = 3385;
	private static final int CRYSTAL_OF_INPROGRESS_6 = 3386;
	private static final int CRYSTAL_OF_FOUL_6 = 3387;
	private static final int CRYSTAL_OF_DEFEAT_6 = 3388;
	private static final int CRYSTAL_OF_VICTORY_6 = 3389;
	
	// Rewards
	private static final int MARK_OF_SUMMONER = 3336;
	
	// Npcs
	private static final int LARA = 30063;
	private static final int GALATEA = 30634;
	private static final int ALMORS = 30635;
	private static final int CAMONIELL = 30636;
	private static final int BELTHUS = 30637;
	private static final int BASILLA = 30638;
	private static final int CELESTIEL = 30639;
	private static final int BRYNTHEA = 30640;
	
	// Monsters
	private static final int NOBLE_ANT = 20089;
	private static final int NOBLE_ANT_LEADER = 20090;
	private static final int WYRM = 20176;
	private static final int TYRANT = 20192;
	private static final int TYRANT_KINGPIN = 20193;
	private static final int BREKA_ORC = 20267;
	private static final int BREKA_ORC_ARCHER = 20268;
	private static final int BREKA_ORC_SHAMAN = 20269;
	private static final int BREKA_ORC_OVERLORD = 20270;
	private static final int BREKA_ORC_WARRIOR = 20271;
	private static final int FETTERED_SOUL = 20552;
	private static final int WINDSUS = 20553;
	private static final int GIANT_FUNGUS = 20555;
	private static final int MANASHEN_GARGOYLE = 20563;
	private static final int LETO_LIZARDMAN = 20577;
	private static final int LETO_LIZARDMAN_ARCHER = 20578;
	private static final int LETO_LIZARDMAN_SOLDIER = 20579;
	private static final int LETO_LIZARDMAN_WARRIOR = 20580;
	private static final int LETO_LIZARDMAN_SHAMAN = 20581;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int KARUL_BUGBEAR = 20600;
	
	// Quest Monsters
	private static final int PAKO_THE_CAT = 27102;
	private static final int UNICORN_RACER = 27103;
	private static final int SHADOW_TUREN = 27104;
	private static final int MIMI_THE_CAT = 27105;
	private static final int UNICORN_PHANTASM = 27106;
	private static final int SILHOUETTE_TILFO = 27107;
	
	private static final int[][] LARA_LISTS = new int[][]
	{
		{
			LARA_LIST_1,
			SAC_OF_REDSPORES,
			LETO_LIZARDMAN_AMULET
		},
		{
			LARA_LIST_2,
			KARUL_BUGBEAR_TOTEM,
			SHARDS_OF_MANASHEN
		},
		{
			LARA_LIST_3,
			CRIMSON_BLOODSTONE,
			BREKA_ORC_TOTEM
		},
		{
			LARA_LIST_4,
			TUSK_OF_WINDSUS,
			TALONS_OF_TYRANT
		},
		{
			LARA_LIST_5,
			WINGS_OF_DRONEANT,
			FANGS_OF_WYRM
		}
	};
	
	private static final Map<Integer, ProgressDuelMob> _duelsInProgress = new ConcurrentHashMap<>();
	
	public Q230_TestOfTheSummoner()
	{
		super(230, "Test of the Summoner");
		
		setItemsIds(LETO_LIZARDMAN_AMULET, SAC_OF_REDSPORES, KARUL_BUGBEAR_TOTEM, SHARDS_OF_MANASHEN, BREKA_ORC_TOTEM, CRIMSON_BLOODSTONE, TALONS_OF_TYRANT, WINGS_OF_DRONEANT, TUSK_OF_WINDSUS, FANGS_OF_WYRM, LARA_LIST_1, LARA_LIST_2, LARA_LIST_3, LARA_LIST_4, LARA_LIST_5, GALATEA_LETTER, BEGINNER_ARCANA, ALMORS_ARCANA, CAMONIELL_ARCANA, BELTHUS_ARCANA, BASILLIA_ARCANA, CELESTIEL_ARCANA, BRYNTHEA_ARCANA, CRYSTAL_OF_PROGRESS_1, CRYSTAL_OF_INPROGRESS_1, CRYSTAL_OF_FOUL_1, CRYSTAL_OF_DEFEAT_1, CRYSTAL_OF_VICTORY_1, CRYSTAL_OF_PROGRESS_2, CRYSTAL_OF_INPROGRESS_2, CRYSTAL_OF_FOUL_2, CRYSTAL_OF_DEFEAT_2, CRYSTAL_OF_VICTORY_2, CRYSTAL_OF_PROGRESS_3, CRYSTAL_OF_INPROGRESS_3, CRYSTAL_OF_FOUL_3, CRYSTAL_OF_DEFEAT_3, CRYSTAL_OF_VICTORY_3, CRYSTAL_OF_PROGRESS_4, CRYSTAL_OF_INPROGRESS_4, CRYSTAL_OF_FOUL_4, CRYSTAL_OF_DEFEAT_4, CRYSTAL_OF_VICTORY_4, CRYSTAL_OF_PROGRESS_5, CRYSTAL_OF_INPROGRESS_5, CRYSTAL_OF_FOUL_5, CRYSTAL_OF_DEFEAT_5, CRYSTAL_OF_VICTORY_5, CRYSTAL_OF_PROGRESS_6, CRYSTAL_OF_INPROGRESS_6, CRYSTAL_OF_FOUL_6, CRYSTAL_OF_DEFEAT_6, CRYSTAL_OF_VICTORY_6);
		
		addQuestStart(GALATEA);
		addTalkId(GALATEA, ALMORS, CAMONIELL, BELTHUS, BASILLA, CELESTIEL, BRYNTHEA, LARA);
		
		addAttacked(PAKO_THE_CAT, UNICORN_RACER, SHADOW_TUREN, MIMI_THE_CAT, UNICORN_PHANTASM, SILHOUETTE_TILFO);
		addMyDying(NOBLE_ANT, NOBLE_ANT_LEADER, WYRM, TYRANT, TYRANT_KINGPIN, BREKA_ORC, BREKA_ORC_ARCHER, BREKA_ORC_SHAMAN, BREKA_ORC_OVERLORD, BREKA_ORC_WARRIOR, FETTERED_SOUL, WINDSUS, GIANT_FUNGUS, MANASHEN_GARGOYLE, LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER, LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR, LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD, KARUL_BUGBEAR, PAKO_THE_CAT, UNICORN_RACER, SHADOW_TUREN, MIMI_THE_CAT, UNICORN_PHANTASM, SILHOUETTE_TILFO);
		
		setTriggeredOnDeath();
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return null;
		
		// GALATEA
		if (event.equals("30634-08.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("Belthus", 1);
			st.set("Brynthea", 1);
			st.set("Celestiel", 1);
			st.set("Camoniell", 1);
			st.set("Basilla", 1);
			st.set("Almors", 1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, GALATEA_LETTER, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30634-08a.htm";
		}
		// LARA
		else if (event.equals("30063-02.htm")) // Lara first time to give a list out
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, GALATEA_LETTER, 1);
			
			final int random = Rnd.get(5);
			
			giveItems(player, LARA_LISTS[random][0], 1);
			st.set("Lara", random + 1); // avoid 0
		}
		else if (event.equals("30063-04.htm")) // Lara later to give a list out
		{
			final int random = Rnd.get(5);
			
			playSound(player, SOUND_ITEMGET);
			giveItems(player, LARA_LISTS[random][0], 1);
			st.set("Lara", random + 1);
		}
		// ALMORS
		else if (event.equals("30635-02.htm"))
		{
			if (player.getInventory().hasItem(BEGINNER_ARCANA))
				htmltext = "30635-03.htm";
		}
		else if (event.equals("30635-04.htm"))
		{
			st.set("Almors", 2); // set state ready to fight
			playSound(player, SOUND_ITEMGET);
			takeItems(player, CRYSTAL_OF_FOUL_1, -1); // just in case he cheated or lost
			takeItems(player, CRYSTAL_OF_DEFEAT_1, -1);
			takeItems(player, BEGINNER_ARCANA, 1);
			giveItems(player, CRYSTAL_OF_PROGRESS_1, 1); // give Starting Crystal
			
			npc.getAI().addCastDesire(player, 4126, 1, 1000000);
		}
		// CAMONIELL
		else if (event.equals("30636-02.htm"))
		{
			if (player.getInventory().hasItem(BEGINNER_ARCANA))
				htmltext = "30636-03.htm";
		}
		else if (event.equals("30636-04.htm"))
		{
			st.set("Camoniell", 2);
			playSound(player, SOUND_ITEMGET);
			takeItems(player, CRYSTAL_OF_FOUL_2, -1);
			takeItems(player, CRYSTAL_OF_DEFEAT_2, -1);
			takeItems(player, BEGINNER_ARCANA, 1);
			giveItems(player, CRYSTAL_OF_PROGRESS_2, 1);
			
			npc.getAI().addCastDesire(player, 4126, 1, 1000000);
		}
		// BELTHUS
		else if (event.equals("30637-02.htm"))
		{
			if (player.getInventory().hasItem(BEGINNER_ARCANA))
				htmltext = "30637-03.htm";
		}
		else if (event.equals("30637-04.htm"))
		{
			st.set("Belthus", 2);
			playSound(player, SOUND_ITEMGET);
			takeItems(player, CRYSTAL_OF_FOUL_3, -1);
			takeItems(player, CRYSTAL_OF_DEFEAT_3, -1);
			takeItems(player, BEGINNER_ARCANA, 1);
			giveItems(player, CRYSTAL_OF_PROGRESS_3, 1);
			
			npc.getAI().addCastDesire(player, 4126, 1, 1000000);
		}
		// BASILLA
		else if (event.equals("30638-02.htm"))
		{
			if (player.getInventory().hasItem(BEGINNER_ARCANA))
				htmltext = "30638-03.htm";
		}
		else if (event.equals("30638-04.htm"))
		{
			st.set("Basilla", 2);
			playSound(player, SOUND_ITEMGET);
			takeItems(player, CRYSTAL_OF_FOUL_4, -1);
			takeItems(player, CRYSTAL_OF_DEFEAT_4, -1);
			takeItems(player, BEGINNER_ARCANA, 1);
			giveItems(player, CRYSTAL_OF_PROGRESS_4, 1);
			
			npc.getAI().addCastDesire(player, 4126, 1, 1000000);
		}
		// CELESTIEL
		else if (event.equals("30639-02.htm"))
		{
			if (player.getInventory().hasItem(BEGINNER_ARCANA))
				htmltext = "30639-03.htm";
		}
		else if (event.equals("30639-04.htm"))
		{
			st.set("Celestiel", 2);
			playSound(player, SOUND_ITEMGET);
			takeItems(player, CRYSTAL_OF_FOUL_5, -1);
			takeItems(player, CRYSTAL_OF_DEFEAT_5, -1);
			takeItems(player, BEGINNER_ARCANA, 1);
			giveItems(player, CRYSTAL_OF_PROGRESS_5, 1);
			
			npc.getAI().addCastDesire(player, 4126, 1, 1000000);
		}
		// BRYNTHEA
		else if (event.equals("30640-02.htm"))
		{
			if (player.getInventory().hasItem(BEGINNER_ARCANA))
				htmltext = "30640-03.htm";
		}
		else if (event.equals("30640-04.htm"))
		{
			st.set("Brynthea", 2);
			playSound(player, SOUND_ITEMGET);
			takeItems(player, CRYSTAL_OF_FOUL_6, -1);
			takeItems(player, CRYSTAL_OF_DEFEAT_6, -1);
			takeItems(player, BEGINNER_ARCANA, 1);
			giveItems(player, CRYSTAL_OF_PROGRESS_6, 1);
			
			npc.getAI().addCastDesire(player, 4126, 1, 1000000);
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
		final int npcId = npc.getNpcId();
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getClassId() != ClassId.HUMAN_WIZARD && player.getClassId() != ClassId.ELVEN_WIZARD && player.getClassId() != ClassId.DARK_WIZARD) // wizard, elven wizard, dark wizard
					htmltext = "30634-01.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30634-02.htm";
				else
					htmltext = "30634-03.htm";
				break;
			
			case STARTED:
				switch (npcId)
				{
					case LARA:
						if (cond == 1)
							htmltext = "30063-01.htm";
						else
						{
							if (st.getInteger("Lara") == 0) // if you havent a part taken, give one
								htmltext = "30063-03.htm";
							else
							{
								final int[] laraPart = LARA_LISTS[st.getInteger("Lara") - 1];
								if (player.getInventory().getItemCount(laraPart[1]) < 30 || player.getInventory().getItemCount(laraPart[2]) < 30)
									htmltext = "30063-05.htm";
								else
								{
									htmltext = "30063-06.htm";
									st.setCond(3);
									st.unset("Lara");
									playSound(player, SOUND_MIDDLE);
									takeItems(player, laraPart[0], 1);
									takeItems(player, laraPart[1], -1);
									takeItems(player, laraPart[2], -1);
									giveItems(player, BEGINNER_ARCANA, 2);
								}
							}
						}
						break;
					
					case GALATEA:
						if (cond == 1)
							htmltext = "30634-09.htm";
						else if (cond == 2 || cond == 3)
							htmltext = (!player.getInventory().hasItem(BEGINNER_ARCANA)) ? "30634-10.htm" : "30634-11.htm";
						else if (cond == 4)
						{
							htmltext = "30634-12.htm";
							takeItems(player, BEGINNER_ARCANA, -1);
							takeItems(player, ALMORS_ARCANA, -1);
							takeItems(player, BASILLIA_ARCANA, -1);
							takeItems(player, BELTHUS_ARCANA, -1);
							takeItems(player, BRYNTHEA_ARCANA, -1);
							takeItems(player, CAMONIELL_ARCANA, -1);
							takeItems(player, CELESTIEL_ARCANA, -1);
							takeItems(player, LARA_LIST_1, -1);
							takeItems(player, LARA_LIST_2, -1);
							takeItems(player, LARA_LIST_3, -1);
							takeItems(player, LARA_LIST_4, -1);
							takeItems(player, LARA_LIST_5, -1);
							giveItems(player, MARK_OF_SUMMONER, 1);
							rewardExpAndSp(player, 148409, 30000);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ALMORS:
						int almorsStat = st.getInteger("Almors");
						if (almorsStat == 1)
							htmltext = "30635-01.htm";
						else if (almorsStat == 2)
							htmltext = "30635-08.htm";
						else if (almorsStat == 3) // in battle...
							htmltext = "30635-09.htm";
						else if (almorsStat == 4) // haha... your summon lose
							htmltext = "30635-05.htm";
						else if (almorsStat == 5)
							htmltext = "30635-06.htm";
						else if (almorsStat == 6)
						{
							htmltext = "30635-07.htm";
							st.set("Almors", 7);
							takeItems(player, CRYSTAL_OF_VICTORY_1, -1);
							giveItems(player, ALMORS_ARCANA, 1);
							
							if (player.getInventory().hasItems(CAMONIELL_ARCANA, BELTHUS_ARCANA, BASILLIA_ARCANA, CELESTIEL_ARCANA, BRYNTHEA_ARCANA))
							{
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (almorsStat == 7)
							htmltext = "30635-10.htm";
						break;
					
					case CAMONIELL:
						int camoniellStat = st.getInteger("Camoniell");
						if (camoniellStat == 1)
							htmltext = "30636-01.htm";
						else if (camoniellStat == 2)
							htmltext = "30636-08.htm";
						else if (camoniellStat == 3) // in battle...
							htmltext = "30636-09.htm";
						else if (camoniellStat == 4) // haha... your summon lose
							htmltext = "30636-05.htm";
						else if (camoniellStat == 5)
							htmltext = "30636-06.htm";
						else if (camoniellStat == 6)
						{
							htmltext = "30636-07.htm";
							st.set("Camoniell", 7);
							takeItems(player, CRYSTAL_OF_VICTORY_2, -1);
							giveItems(player, CAMONIELL_ARCANA, 1);
							
							if (player.getInventory().hasItems(ALMORS_ARCANA, BELTHUS_ARCANA, BASILLIA_ARCANA, CELESTIEL_ARCANA, BRYNTHEA_ARCANA))
							{
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (camoniellStat == 7)
							htmltext = "30636-10.htm";
						break;
					
					case BELTHUS:
						int belthusStat = st.getInteger("Belthus");
						if (belthusStat == 1)
							htmltext = "30637-01.htm";
						else if (belthusStat == 2)
							htmltext = "30637-08.htm";
						else if (belthusStat == 3) // in battle...
							htmltext = "30637-09.htm";
						else if (belthusStat == 4) // haha... your summon lose
							htmltext = "30637-05.htm";
						else if (belthusStat == 5)
							htmltext = "30637-06.htm";
						else if (belthusStat == 6)
						{
							htmltext = "30637-07.htm";
							st.set("Belthus", 7);
							takeItems(player, CRYSTAL_OF_VICTORY_3, -1);
							giveItems(player, BELTHUS_ARCANA, 1);
							
							if (player.getInventory().hasItems(ALMORS_ARCANA, CAMONIELL_ARCANA, BASILLIA_ARCANA, CELESTIEL_ARCANA, BRYNTHEA_ARCANA))
							{
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (belthusStat == 7)
							htmltext = "30637-10.htm";
						break;
					
					case BASILLA:
						int basillaStat = st.getInteger("Basilla");
						if (basillaStat == 1)
							htmltext = "30638-01.htm";
						else if (basillaStat == 2)
							htmltext = "30638-08.htm";
						else if (basillaStat == 3) // in battle...
							htmltext = "30638-09.htm";
						else if (basillaStat == 4) // haha... your summon lose
							htmltext = "30638-05.htm";
						else if (basillaStat == 5)
							htmltext = "30638-06.htm";
						else if (basillaStat == 6)
						{
							htmltext = "30638-07.htm";
							st.set("Basilla", 7);
							takeItems(player, CRYSTAL_OF_VICTORY_4, -1);
							giveItems(player, BASILLIA_ARCANA, 1);
							
							if (player.getInventory().hasItems(ALMORS_ARCANA, CAMONIELL_ARCANA, BELTHUS_ARCANA, CELESTIEL_ARCANA, BRYNTHEA_ARCANA))
							{
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (basillaStat == 7)
							htmltext = "30638-10.htm";
						break;
					
					case CELESTIEL:
						int celestielStat = st.getInteger("Celestiel");
						if (celestielStat == 1)
							htmltext = "30639-01.htm";
						else if (celestielStat == 2)
							htmltext = "30639-08.htm";
						else if (celestielStat == 3) // in battle...
							htmltext = "30639-09.htm";
						else if (celestielStat == 4) // haha... your summon lose
							htmltext = "30639-05.htm";
						else if (celestielStat == 5)
							htmltext = "30639-06.htm";
						else if (celestielStat == 6)
						{
							htmltext = "30639-07.htm";
							st.set("Celestiel", 7);
							takeItems(player, CRYSTAL_OF_VICTORY_5, -1);
							giveItems(player, CELESTIEL_ARCANA, 1);
							
							if (player.getInventory().hasItems(ALMORS_ARCANA, CAMONIELL_ARCANA, BELTHUS_ARCANA, BASILLIA_ARCANA, BRYNTHEA_ARCANA))
							{
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (celestielStat == 7)
							htmltext = "30639-10.htm";
						break;
					
					case BRYNTHEA:
						int bryntheaStat = st.getInteger("Brynthea");
						if (bryntheaStat == 1)
							htmltext = "30640-01.htm";
						else if (bryntheaStat == 2)
							htmltext = "30640-08.htm";
						else if (bryntheaStat == 3) // in battle...
							htmltext = "30640-09.htm";
						else if (bryntheaStat == 4) // haha... your summon lose
							htmltext = "30640-05.htm";
						else if (bryntheaStat == 5)
							htmltext = "30640-06.htm";
						else if (bryntheaStat == 6)
						{
							htmltext = "30640-07.htm";
							st.set("Brynthea", 7);
							takeItems(player, CRYSTAL_OF_VICTORY_6, -1);
							giveItems(player, BRYNTHEA_ARCANA, 1);
							
							if (player.getInventory().hasItems(ALMORS_ARCANA, CAMONIELL_ARCANA, BELTHUS_ARCANA, BASILLIA_ARCANA, CELESTIEL_ARCANA))
							{
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (bryntheaStat == 7)
							htmltext = "30640-10.htm";
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
	public void onDeath(Creature killer, Player player)
	{
		if (!(killer instanceof Attackable attackable))
			return;
		
		QuestState st = checkPlayerState(player, attackable, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (attackable.getNpcId())
		{
			case PAKO_THE_CAT:
				if (st.getInteger("Almors") == 3)
				{
					st.set("Almors", 4);
					playSound(player, SOUND_ITEMGET);
					giveItems(player, CRYSTAL_OF_DEFEAT_1, 1);
				}
				break;
			
			case UNICORN_RACER:
				if (st.getInteger("Camoniell") == 3)
				{
					st.set("Camoniell", 4);
					playSound(player, SOUND_ITEMGET);
					giveItems(player, CRYSTAL_OF_DEFEAT_2, 1);
				}
				break;
			
			case SHADOW_TUREN:
				if (st.getInteger("Belthus") == 3)
				{
					st.set("Belthus", 4);
					playSound(player, SOUND_ITEMGET);
					giveItems(player, CRYSTAL_OF_DEFEAT_3, 1);
				}
				break;
			
			case MIMI_THE_CAT:
				if (st.getInteger("Basilla") == 3)
				{
					st.set("Basilla", 4);
					playSound(player, SOUND_ITEMGET);
					giveItems(player, CRYSTAL_OF_DEFEAT_4, 1);
				}
				break;
			
			case UNICORN_PHANTASM:
				if (st.getInteger("Celestiel") == 3)
				{
					st.set("Celestiel", 4);
					playSound(player, SOUND_ITEMGET);
					giveItems(player, CRYSTAL_OF_DEFEAT_5, 1);
				}
				break;
			
			case SILHOUETTE_TILFO:
				if (st.getInteger("Brynthea") == 3)
				{
					st.set("Brynthea", 4);
					playSound(player, SOUND_ITEMGET);
					giveItems(player, CRYSTAL_OF_DEFEAT_6, 1);
				}
				break;
		}
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Player player = attacker.getActingPlayer();
		
		QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int npcId = npc.getNpcId();
		final boolean isPet = attacker instanceof Summon;
		
		switch (npcId)
		{
			case PAKO_THE_CAT:
				if (st.getInteger("Almors") == 2 && isPet && npc.getStatus().getHpRatio() == 1.0)
				{
					st.set("Almors", 3);
					playSound(player, SOUND_ITEMGET);
					takeItems(player, CRYSTAL_OF_PROGRESS_1, -1);
					giveItems(player, CRYSTAL_OF_INPROGRESS_1, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23063);
					_duelsInProgress.put(npcId, new ProgressDuelMob(player, attacker.getSummon()));
				}
				else if (st.getInteger("Almors") == 3 && _duelsInProgress.containsKey(npcId))
				{
					ProgressDuelMob duel = _duelsInProgress.get(npcId);
					// check if the attacker is the same pet as the one that attacked before.
					if (!isPet || attacker.getSummon() != duel.getSummon()) // if a foul occured find the player who had the duel in progress and give a foul crystal
					{
						Player foulPlayer = duel.getAttacker();
						if (foulPlayer != null)
						{
							st = foulPlayer.getQuestList().getQuestState(QUEST_NAME);
							if (st != null)
							{
								st.set("Almors", 5);
								takeItems(player, CRYSTAL_OF_PROGRESS_1, -1);
								takeItems(player, CRYSTAL_OF_INPROGRESS_1, -1);
								giveItems(player, CRYSTAL_OF_FOUL_1, 1);
								npc.broadcastNpcSay(NpcStringId.ID_23064);
								npc.doDie(npc);
							}
						}
					}
				}
				break;
			
			case UNICORN_RACER:
				if (st.getInteger("Camoniell") == 2 && isPet && npc.getStatus().getHpRatio() == 1.0)
				{
					st.set("Camoniell", 3);
					playSound(player, SOUND_ITEMGET);
					takeItems(player, CRYSTAL_OF_PROGRESS_2, -1);
					giveItems(player, CRYSTAL_OF_INPROGRESS_2, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23060);
					_duelsInProgress.put(npcId, new ProgressDuelMob(player, attacker.getSummon()));
				}
				else if (st.getInteger("Camoniell") == 3 && _duelsInProgress.containsKey(npcId))
				{
					ProgressDuelMob duel = _duelsInProgress.get(npcId);
					if (!isPet || attacker.getSummon() != duel.getSummon())
					{
						Player foulPlayer = duel.getAttacker();
						if (foulPlayer != null)
						{
							st = foulPlayer.getQuestList().getQuestState(QUEST_NAME);
							if (st != null)
							{
								st.set("Camoniell", 5);
								takeItems(player, CRYSTAL_OF_PROGRESS_2, -1);
								takeItems(player, CRYSTAL_OF_INPROGRESS_2, -1);
								giveItems(player, CRYSTAL_OF_FOUL_2, 1);
								npc.broadcastNpcSay(NpcStringId.ID_23061);
								npc.doDie(npc);
							}
						}
					}
				}
				break;
			
			case SHADOW_TUREN:
				if (st.getInteger("Belthus") == 2 && isPet && npc.getStatus().getHpRatio() == 1.0)
				{
					st.set("Belthus", 3);
					playSound(player, SOUND_ITEMGET);
					takeItems(player, CRYSTAL_OF_PROGRESS_3, -1);
					giveItems(player, CRYSTAL_OF_INPROGRESS_3, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23072);
					_duelsInProgress.put(npcId, new ProgressDuelMob(player, attacker.getSummon()));
				}
				else if (st.getInteger("Belthus") == 3 && _duelsInProgress.containsKey(npcId))
				{
					ProgressDuelMob duel = _duelsInProgress.get(npcId);
					if (!isPet || attacker.getSummon() != duel.getSummon())
					{
						Player foulPlayer = duel.getAttacker();
						if (foulPlayer != null)
						{
							st = foulPlayer.getQuestList().getQuestState(QUEST_NAME);
							if (st != null)
							{
								st.set("Belthus", 5);
								takeItems(player, CRYSTAL_OF_PROGRESS_3, -1);
								takeItems(player, CRYSTAL_OF_INPROGRESS_3, -1);
								giveItems(player, CRYSTAL_OF_FOUL_3, 1);
								npc.broadcastNpcSay(NpcStringId.ID_23073);
								npc.doDie(npc);
							}
						}
					}
				}
				break;
			
			case MIMI_THE_CAT:
				if (st.getInteger("Basilla") == 2 && isPet && npc.getStatus().getHpRatio() == 1.0)
				{
					st.set("Basilla", 3);
					playSound(player, SOUND_ITEMGET);
					takeItems(player, CRYSTAL_OF_PROGRESS_4, -1);
					giveItems(player, CRYSTAL_OF_INPROGRESS_4, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23066);
					_duelsInProgress.put(npcId, new ProgressDuelMob(player, attacker.getSummon()));
				}
				else if (st.getInteger("Basilla") == 3 && _duelsInProgress.containsKey(npcId))
				{
					ProgressDuelMob duel = _duelsInProgress.get(npcId);
					if (!isPet || attacker.getSummon() != duel.getSummon())
					{
						Player foulPlayer = duel.getAttacker();
						if (foulPlayer != null)
						{
							st = foulPlayer.getQuestList().getQuestState(QUEST_NAME);
							if (st != null)
							{
								st.set("Basilla", 5);
								takeItems(player, CRYSTAL_OF_PROGRESS_4, -1);
								takeItems(player, CRYSTAL_OF_INPROGRESS_4, -1);
								giveItems(player, CRYSTAL_OF_FOUL_4, 1);
								npc.broadcastNpcSay(NpcStringId.ID_23067);
								npc.doDie(npc);
							}
						}
					}
				}
				break;
			
			case UNICORN_PHANTASM:
				if (st.getInteger("Celestiel") == 2 && isPet && npc.getStatus().getHpRatio() == 1.0)
				{
					st.set("Celestiel", 3);
					playSound(player, SOUND_ITEMGET);
					takeItems(player, CRYSTAL_OF_PROGRESS_5, -1);
					giveItems(player, CRYSTAL_OF_INPROGRESS_5, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23069);
					_duelsInProgress.put(npcId, new ProgressDuelMob(player, attacker.getSummon()));
				}
				else if (st.getInteger("Celestiel") == 3 && _duelsInProgress.containsKey(npcId))
				{
					ProgressDuelMob duel = _duelsInProgress.get(npcId);
					if (!isPet || attacker.getSummon() != duel.getSummon())
					{
						Player foulPlayer = duel.getAttacker();
						if (foulPlayer != null)
						{
							st = foulPlayer.getQuestList().getQuestState(QUEST_NAME);
							if (st != null)
							{
								st.set("Celestiel", 5);
								takeItems(player, CRYSTAL_OF_PROGRESS_5, -1);
								takeItems(player, CRYSTAL_OF_INPROGRESS_5, -1);
								giveItems(player, CRYSTAL_OF_FOUL_5, 1);
								npc.broadcastNpcSay(NpcStringId.ID_23070);
								npc.doDie(npc);
							}
						}
					}
				}
				break;
			
			case SILHOUETTE_TILFO:
				if (st.getInteger("Brynthea") == 2 && isPet && npc.getStatus().getHpRatio() == 1.0)
				{
					st.set("Brynthea", 3);
					playSound(player, SOUND_ITEMGET);
					takeItems(player, CRYSTAL_OF_PROGRESS_6, -1);
					giveItems(player, CRYSTAL_OF_INPROGRESS_6, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23075);
					_duelsInProgress.put(npcId, new ProgressDuelMob(player, attacker.getSummon()));
				}
				else if (st.getInteger("Brynthea") == 3 && _duelsInProgress.containsKey(npcId))
				{
					ProgressDuelMob duel = _duelsInProgress.get(npcId);
					if (!isPet || attacker.getSummon() != duel.getSummon())
					{
						Player foulPlayer = duel.getAttacker();
						if (foulPlayer != null)
						{
							st = foulPlayer.getQuestList().getQuestState(QUEST_NAME);
							if (st != null)
							{
								st.set("Brynthea", 5);
								takeItems(player, CRYSTAL_OF_PROGRESS_6, -1);
								takeItems(player, CRYSTAL_OF_INPROGRESS_6, -1);
								giveItems(player, CRYSTAL_OF_FOUL_6, 1);
								npc.broadcastNpcSay(NpcStringId.ID_23076);
								npc.doDie(npc);
							}
						}
					}
				}
				break;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int npcId = npc.getNpcId();
		
		switch (npcId)
		{
			case GIANT_FUNGUS:
				if (st.getInteger("Lara") == 1)
					dropItems(player, SAC_OF_REDSPORES, 1, 30, 800000);
				break;
			
			case LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER:
				if (st.getInteger("Lara") == 1)
					dropItems(player, LETO_LIZARDMAN_AMULET, 1, 30, 250000);
				break;
			
			case LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR:
				if (st.getInteger("Lara") == 1)
					dropItems(player, LETO_LIZARDMAN_AMULET, 1, 30, 500000);
				break;
			
			case LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD:
				if (st.getInteger("Lara") == 1)
					dropItems(player, LETO_LIZARDMAN_AMULET, 1, 30, 750000);
				break;
			
			case MANASHEN_GARGOYLE:
				if (st.getInteger("Lara") == 2)
					dropItems(player, SHARDS_OF_MANASHEN, 1, 30, 800000);
				break;
			
			case KARUL_BUGBEAR:
				if (st.getInteger("Lara") == 2)
					dropItems(player, KARUL_BUGBEAR_TOTEM, 1, 30, 800000);
				break;
			
			case BREKA_ORC, BREKA_ORC_ARCHER, BREKA_ORC_WARRIOR:
				if (st.getInteger("Lara") == 3)
					dropItems(player, BREKA_ORC_TOTEM, 1, 30, 250000);
				break;
			
			case BREKA_ORC_SHAMAN, BREKA_ORC_OVERLORD:
				if (st.getInteger("Lara") == 3)
					dropItems(player, BREKA_ORC_TOTEM, 1, 30, 500000);
				break;
			
			case FETTERED_SOUL:
				if (st.getInteger("Lara") == 3)
					dropItems(player, CRIMSON_BLOODSTONE, 1, 30, 600000);
				break;
			
			case WINDSUS:
				if (st.getInteger("Lara") == 4)
					dropItems(player, TUSK_OF_WINDSUS, 1, 30, 700000);
				break;
			
			case TYRANT, TYRANT_KINGPIN:
				if (st.getInteger("Lara") == 4)
					dropItems(player, TALONS_OF_TYRANT, 1, 30, 500000);
				break;
			
			case NOBLE_ANT, NOBLE_ANT_LEADER:
				if (st.getInteger("Lara") == 5)
					dropItems(player, WINGS_OF_DRONEANT, 1, 30, 600000);
				break;
			
			case WYRM:
				if (st.getInteger("Lara") == 5)
					dropItems(player, FANGS_OF_WYRM, 1, 30, 500000);
				break;
			
			case PAKO_THE_CAT:
				if (st.getInteger("Almors") == 3 && _duelsInProgress.containsKey(npcId))
				{
					st.set("Almors", 6);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, CRYSTAL_OF_INPROGRESS_1, -1);
					giveItems(player, CRYSTAL_OF_VICTORY_1, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23065);
					_duelsInProgress.remove(npcId);
				}
				break;
			
			case UNICORN_RACER:
				if (st.getInteger("Camoniell") == 3 && _duelsInProgress.containsKey(npcId))
				{
					st.set("Camoniell", 6);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, CRYSTAL_OF_INPROGRESS_2, -1);
					giveItems(player, CRYSTAL_OF_VICTORY_2, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23062);
					_duelsInProgress.remove(npcId);
				}
				break;
			
			case SHADOW_TUREN:
				if (st.getInteger("Belthus") == 3 && _duelsInProgress.containsKey(npcId))
				{
					st.set("Belthus", 6);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, CRYSTAL_OF_INPROGRESS_3, -1);
					giveItems(player, CRYSTAL_OF_VICTORY_3, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23074);
					_duelsInProgress.remove(npcId);
				}
				break;
			
			case MIMI_THE_CAT:
				if (st.getInteger("Basilla") == 3 && _duelsInProgress.containsKey(npcId))
				{
					st.set("Basilla", 6);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, CRYSTAL_OF_INPROGRESS_4, -1);
					giveItems(player, CRYSTAL_OF_VICTORY_4, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23068);
					_duelsInProgress.remove(npcId);
				}
				break;
			
			case UNICORN_PHANTASM:
				if (st.getInteger("Celestiel") == 3 && _duelsInProgress.containsKey(npcId))
				{
					st.set("Celestiel", 6);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, CRYSTAL_OF_INPROGRESS_5, -1);
					giveItems(player, CRYSTAL_OF_VICTORY_5, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23071);
					_duelsInProgress.remove(npcId);
				}
				break;
			
			case SILHOUETTE_TILFO:
				if (st.getInteger("Brynthea") == 3 && _duelsInProgress.containsKey(npcId))
				{
					st.set("Brynthea", 6);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, CRYSTAL_OF_INPROGRESS_6, -1);
					giveItems(player, CRYSTAL_OF_VICTORY_6, 1);
					npc.broadcastNpcSay(NpcStringId.ID_23077);
					_duelsInProgress.remove(npcId);
				}
				break;
		}
	}
	
	private final class ProgressDuelMob
	{
		private final Player _attacker;
		private final Summon _summon;
		
		public ProgressDuelMob(Player attacker, Summon summon)
		{
			_attacker = attacker;
			_summon = summon;
		}
		
		public Player getAttacker()
		{
			return _attacker;
		}
		
		public Summon getSummon()
		{
			return _summon;
		}
	}
}