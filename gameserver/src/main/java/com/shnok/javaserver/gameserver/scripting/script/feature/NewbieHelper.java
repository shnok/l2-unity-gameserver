package com.shnok.javaserver.gameserver.scripting.script.feature;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.data.SkillTable.FrequentSkill;
import com.shnok.javaserver.gameserver.data.xml.NewbieBuffData;
import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.TeleportType;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.records.NewbieBuff;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class NewbieHelper extends Quest
{
	private static final String QUEST_NAME = "NewbieHelper";
	private static final String QUEST_NAME_TUTORIAL = "Tutorial";
	
	// Quest Items
	private static final int RECOMMENDATION_01 = 1067;
	private static final int RECOMMENDATION_02 = 1068;
	private static final int LEAF_OF_MOTHERTREE = 1069;
	private static final int BLOOD_OF_JUNDIN = 1070;
	private static final int LICENSE_OF_MINER = 1498;
	private static final int VOUCHER_OF_FLAME = 1496;
	
	// Items Reward
	private static final int SOULSHOT_NO_GRADE_FOR_BEGINNERS = 5789;
	private static final int SPIRITSHOT_NO_GRADE_FOR_BEGINNERS = 5790;
	private static final int BLUE_GEMSTONE = 6353;
	private static final int NEWBIE_TRAVEL_TOKEN = 8542;
	
	private static final Map<String, Location> TELEPORT_LOCS = new HashMap<>();
	
	static
	{
		TELEPORT_LOCS.put("30598", new Location(-84053, 243343, -3729));
		TELEPORT_LOCS.put("30599", new Location(45470, 48328, -3059));
		TELEPORT_LOCS.put("30600", new Location(12160, 16554, -4583));
		TELEPORT_LOCS.put("30601", new Location(115594, -177993, -912));
		TELEPORT_LOCS.put("30602", new Location(-45067, -113563, -199));
	}
	
	private static final Map<Integer, Location> NEWBIE_GUIDE_LOCS = new HashMap<>();
	
	static
	{
		NEWBIE_GUIDE_LOCS.put(30008, new Location(-84058, 243239, -3730));
		NEWBIE_GUIDE_LOCS.put(30017, new Location(-84058, 243239, -3730));
		NEWBIE_GUIDE_LOCS.put(30129, new Location(12116, 16666, -4610));
		NEWBIE_GUIDE_LOCS.put(30370, new Location(45491, 48359, -3086));
		NEWBIE_GUIDE_LOCS.put(30528, new Location(115632, -177996, -912));
		NEWBIE_GUIDE_LOCS.put(30573, new Location(-45067, -113549, -235));
	}
	
	private static final int[] RADARS =
	{
		// Talking Island
		30006, // Gatekeeper Roxxy
		30039, // Captain Gilbert
		30040, // Guard Leon
		30041, // Guard Arnold
		30042, // Guard Abellos
		30043, // Guard Johnstone
		30044, // Guard Chiperan
		30045, // Guard Kenyos
		30046, // Guard Hanks
		30283, // Blacksmith Altran
		30003, // Trader Silvia
		30004, // Trader Katerina
		30001, // Trader Lector
		30002, // Trader Jackson
		30031, // High Priest Biotin
		30033, // Magister Baulro
		30035, // Magister Harrys
		30032, // Priest Yohanes
		30036, // Priest Petron
		30026, // Grand Master Bitz
		30027, // Master Gwinter
		30029, // Master Minia
		30028, // Master Pintage
		30054, // Warehouse Keeper Rant
		30055, // Warehouse Keeper Rolfe
		30005, // Warehouse Keeper Wilford
		30048, // Darin
		30312, // Lighthouse Keeper Rockswell
		30368, // Lilith
		30049, // Bonnie
		30047, // Wharf Manager Firon
		30497, // Edmond
		30050, // Elias
		30311, // Sir Collin Windawood
		30051, // Cristel
		
		// Dark Elf Village
		30134, // Gatekeeper Jasmine
		30224, // Sentry Knight Rayla
		30348, // Sentry Nelsya
		30355, // Sentry Roselyn
		30347, // Sentry Marion
		30432, // Sentry Irene
		30356, // Sentry Altima
		30349, // Sentry Jenna
		30346, // Sentry Kayleen
		30433, // Sentry Kathaway
		30357, // Sentry Kristin
		30431, // Sentry Eriel
		30430, // Sentry Trionell
		30307, // Blacksmith Karrod
		30138, // Trader Minaless
		30137, // Trader Vollodos
		30135, // Trader Iria
		30136, // Trader Payne
		30143, // Master Trudy
		30360, // Master Harant
		30145, // Master Vlasty
		30135, // Magister Harne
		30144, // Tetrarch Vellior
		30358, // Tetrarch Thifiell
		30359, // Tetrarch Kaitar
		30141, // Tetrarch Talloth
		30139, // Warehouse Keeper Dorankus
		30140, // Warehouse Keeper Erviante
		30350, // Warehouse Freightman Carlon
		30421, // Varika
		30419, // Arkenia
		30130, // Abyssal Celebrant Undrias
		30351, // Astaron
		30353, // Jughead
		30354, // Jewel
		
		// Elven Village
		30146, // Gatekeeper Mirabel
		30285, // Sentinel Gartrandell
		30284, // Sentinel Knight Alberius
		30221, // Sentinel Rayen
		30217, // Sentinel Berros
		30219, // Sentinel Veltress
		30220, // Sentinel Starden
		30218, // Sentinel Kendell
		30216, // Sentinel Wheeler
		30363, // Blacksmith Aios
		30149, // Trader Creamees
		30150, // Trader Herbiel
		30148, // Trader Ariel
		30147, // Trader Unoren
		30155, // Master Ellenia
		30156, // Master Cobendell
		30157, // Magister Greenis
		30158, // Magister Esrandell
		30154, // Hierarch Asterios
		30153, // Warehouse Keeper Markius
		30152, // Warehouse Keeper Julia
		30151, // Warehouse Freightman Chad
		30423, // Northwind
		30414, // Rosella
		30361, // Rizraell
		31853, // Treant Bremec
		30223, // Arujien
		30362, // Andellia
		30222, // Alshupes
		30371, // Thalia
		31852, // Pixy Murika
		
		// Dwarven Village
		30540, // Gatekeeper Wirphy
		30541, // Protector Paion
		30542, // Defender Runant
		30543, // Defender Ethan
		30544, // Defender Cromwell
		30545, // Defender Proton
		30546, // Defender Dinkey
		30547, // Defender Tardyon
		30548, // Defender Nathan
		30531, // Iron Gate's Lockirin
		30532, // Golden Wheel's Spiron
		30533, // Silver Scale's Balanki
		30534, // Bronze Key's Keef
		30535, // Filaur of the Gray Pillar
		30536, // Black Anvil's Arin
		30525, // Head Blacksmith Bronk
		30526, // Blacksmith Brunon
		30527, // Blacksmith Silvera
		30518, // Trader Garita
		30519, // Trader Mion
		30516, // Trader Reep
		30517, // Trader Shari
		30520, // Warehouse Chief Reed
		30521, // Warehouse Freightman Murdoc
		30522, // Warehouse Keeper Airy
		30523, // Collector Gouph
		30524, // Collector Pippi
		30537, // Daichir, Priest of the Eart
		30650, // Priest of the Earth Gerald
		30538, // Priest of the Earth Zimenf
		30539, // Priestess of the Earth Chichirin
		30671, // Captain Croto
		30651, // Wanderer Dorf
		30550, // Gauri Twinklerock
		30554, // Miner Bolter
		30553, // Maryse Redbonnet
		
		// Orc Village
		30576, // Gatekeeper Tamil
		30577, // Praetorian Rukain
		30578, // Centurion Nakusin
		30579, // Centurion Tamai
		30580, // Centurion Parugon
		30581, // Centurion Orinak
		30582, // Centurion Tiku
		30583, // Centurion Petukai
		30584, // Centurion Vapook
		30569, // Prefect Brukurse
		30570, // Prefect Karukia
		30571, // Seer Tanapi
		30572, // Seer Livina
		30564, // Blacksmith Sumari
		30560, // Trader Uska
		30561, // Trader Papuma
		30558, // Trader Jakal
		30559, // Trader Kunai
		30562, // Warehouse Keeper Grookin
		30563, // Warehouse Keeper Imantu
		30565, // Flame Lord Kakai
		30566, // Atuba Chief Varkees
		30567, // Neruga Chief Tantus
		30568, // Urutu Chief Hatos
		30585, // Tataru Zu Hestui
		30587, // Gantaki Zu Urutu
	};
	
	public NewbieHelper()
	{
		super(-1, "feature");
		
		addTalkId(30009, 30019, 30131, 30400, 30530, 30575, 30008, 30017, 30129, 30370, 30528, 30573, 30598, 30599, 30600, 30601, 30602, 31076, 31077);
		addFirstTalkId(30009, 30019, 30131, 30400, 30530, 30575, 30008, 30017, 30129, 30370, 30528, 30573, 30598, 30599, 30600, 30601, 30602, 31076, 31077);
		
		addMyDying(18342);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		QuestState qs = player.getQuestList().getQuestState(QUEST_NAME_TUTORIAL);
		if (st == null || qs == null)
			return null;
		
		String htmltext = name;
		
		if (name.equalsIgnoreCase("TimerEx_NewbieHelper"))
		{
			final int ex = qs.getInteger("Ex");
			if (ex == 0)
			{
				switch (player.getClassId())
				{
					case HUMAN_FIGHTER, ELVEN_FIGHTER, DARK_FIGHTER, ORC_FIGHTER, DWARVEN_FIGHTER:
						playTutorialVoice(player, "tutorial_voice_009a");
						break;
					
					case HUMAN_MYSTIC, ELVEN_MYSTIC, DARK_MYSTIC:
						playTutorialVoice(player, "tutorial_voice_009b");
						break;
					
					case ORC_MYSTIC:
						playTutorialVoice(player, "tutorial_voice_009c");
						break;
				}
				qs.set("Ex", 1);
			}
			else if (ex == 3)
			{
				switch (player.getClassId())
				{
					case HUMAN_FIGHTER:
						playTutorialVoice(player, "tutorial_voice_010a");
						break;
					
					case HUMAN_MYSTIC:
						playTutorialVoice(player, "tutorial_voice_010b");
						break;
					
					case ELVEN_FIGHTER, ELVEN_MYSTIC:
						playTutorialVoice(player, "tutorial_voice_010c");
						break;
					
					case DARK_FIGHTER, DARK_MYSTIC:
						playTutorialVoice(player, "tutorial_voice_010d");
						break;
					
					case ORC_FIGHTER, ORC_MYSTIC:
						playTutorialVoice(player, "tutorial_voice_010e");
						break;
					
					case DWARVEN_FIGHTER:
						playTutorialVoice(player, "tutorial_voice_010f");
						break;
				}
				qs.set("Ex", 4);
			}
			return null;
		}
		else if (name.equalsIgnoreCase("TimerEx_GrandMaster"))
		{
			if (qs.getInteger("Ex") >= 4)
			{
				showQuestionMark(player, 7);
				playSound(player, SOUND_TUTORIAL);
				playTutorialVoice(player, "tutorial_voice_025");
			}
			return null;
		}
		
		return (htmltext.isEmpty()) ? null : htmltext;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		QuestState qs = player.getQuestList().getQuestState(QUEST_NAME_TUTORIAL);
		if (st == null || qs == null)
			return null;
		
		String htmltext = event;
		
		if (event.equalsIgnoreCase("30008-03.htm") || event.equalsIgnoreCase("30017-03.htm") || event.equalsIgnoreCase("30129-03.htm") || event.equalsIgnoreCase("30370-03.htm") || event.equalsIgnoreCase("30528-03.htm") || event.equalsIgnoreCase("30573-03.htm"))
		{
			player.getRadarList().addMarker(NEWBIE_GUIDE_LOCS.get(npc.getNpcId()));
			
			final int itemId = getItemId(npc.getNpcId());
			if (player.getInventory().hasItem(itemId) && st.getInteger("onlyone") == 0)
			{
				takeItems(player, itemId, 1);
				rewardExpAndSp(player, 0, 50);
				
				startQuestTimer("TimerEx_GrandMaster", null, player, 60000);
				
				if (qs.getInteger("Ex") <= 3)
					qs.set("Ex", 4);
				
				if (player.isMageClass() && player.getClassId() != ClassId.ORC_MYSTIC)
				{
					playTutorialVoice(player, "tutorial_voice_027");
					giveItems(player, SPIRITSHOT_NO_GRADE_FOR_BEGINNERS, 100);
				}
				else
				{
					playTutorialVoice(player, "tutorial_voice_026");
					giveItems(player, SOULSHOT_NO_GRADE_FOR_BEGINNERS, 200);
				}
				
				st.unset("step");
				st.set("onlyone", 1);
			}
		}
		else if (event.startsWith("AskAdvice"))
		{
			switch (npc.getTemplate().getRace())
			{
				case HUMAN:
					if (player.getRace() != ClassRace.HUMAN)
						return "human/guide_human_cnacelot003.htm";
					
					htmltext = "human/guide_human_cnacelot";
					break;
				
				case ELVE:
					if (player.getRace() != ClassRace.ELF)
						return "elf/guide_elf_roios003.htm";
					
					htmltext = "elf/guide_elf_roios";
					break;
				
				case DARKELVE:
					if (player.getRace() != ClassRace.DARK_ELF)
						return "darkelf/guide_delf_frankia003.htm";
					
					htmltext = "darkelf/guide_delf_frankia";
					break;
				
				case ORC:
					if (player.getRace() != ClassRace.ORC)
						return "orc/guide_orc_tanai003.htm";
					
					htmltext = "orc/guide_orc_tanai";
					break;
				
				case DWARVE:
					if (player.getRace() != ClassRace.DWARF)
						return "dwarf/guide_dwarf_gullin003.htm";
					
					htmltext = "dwarf/guide_dwarf_gullin";
					break;
			}
			
			final int level = player.getStatus().getLevel();
			
			// Already too high or different class level.
			if (level >= 20 || player.getClassId().getLevel() != 0)
				htmltext += "002.htm";
			// Fighter related HTMs.
			else if (!player.isMageClass())
			{
				if (level <= 5)
					htmltext += "_f05.htm";
				else if (level <= 10)
					htmltext += "_f10.htm";
				else if (level <= 15)
					htmltext += "_f15.htm";
				else
					htmltext += "_f20.htm";
			}
			// Mage related HTMs.
			else if (level <= 7)
				htmltext += "_m07.htm";
			else if (level <= 14)
				htmltext += "_m14.htm";
			else
				htmltext += "_m20.htm";
		}
		else if (event.startsWith("SupportMagic"))
		{
			// Prevent a cursed weapon wielder of being buffed.
			if (player.isCursedWeaponEquipped())
				return null;
			
			// Orc Mage and Orc Shaman should receive fighter buffs since IL, although they are mage classes.
			final boolean isMage = player.isMageClass() && player.getClassId() != ClassId.ORC_MYSTIC && player.getClassId() != ClassId.ORC_SHAMAN;
			
			final int playerLevel = player.getStatus().getLevel();
			
			// If the player is too low level, display a message and return.
			if (playerLevel < NewbieBuffData.getInstance().getLowestBuffLevel(isMage))
				htmltext = "guide_for_newbie002.htm";
			// If the player is too high level, display a message and return.
			else if (!player.isNewbie(false))
				htmltext = "guide_for_newbie003.htm";
			else
			{
				// Go through the NewbieBuff List and cast skills.
				int i = 0;
				for (NewbieBuff buff : NewbieBuffData.getInstance().getValidBuffs(isMage, playerLevel))
					ThreadPool.schedule(() -> callSkill(player, player, buff.getSkill()), 1000L * i++);
				
				return null;
			}
		}
		else if (event.equals("NewbieToken"))
		{
			if (!player.isNewbie(false))
				htmltext = getInvalidHtm(npc);
			else
			{
				npc.showTeleportWindow(player, TeleportType.NEWBIE_TOKEN);
				return null;
			}
		}
		else if (event.startsWith("NewbieToken"))
		{
			if (!player.getInventory().hasItem(NEWBIE_TRAVEL_TOKEN))
				htmltext = "newbie_guide_no_token.htm";
			else
			{
				final Location loc = TELEPORT_LOCS.get(event);
				if (loc != null)
				{
					takeItems(player, NEWBIE_TRAVEL_TOKEN, 1);
					player.teleportTo(loc, 0);
				}
				return null;
			}
		}
		else if (event.startsWith("GiveBlessing"))
		{
			if (player.getStatus().getLevel() > 39 || player.getClassId().getLevel() >= 2)
				htmltext = getInvalidHtm(npc);
			else
			{
				ThreadPool.schedule(() -> callSkill(player, player, FrequentSkill.BLESSING_OF_PROTECTION.getSkill()), 1000);
				return null;
			}
		}
		else if (event.startsWith("NpcLocationInfo"))
		{
			final int npcId = Integer.parseInt(event.substring(16));
			
			if (!ArraysUtil.contains(RADARS, npcId))
				return null;
			
			npc = World.getInstance().getNpc(npcId);
			if (npc != null)
				player.getRadarList().addMarker(npc.getSpawnLocation());
			
			htmltext = "newbie_guide_move_to_loc.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final QuestState qs = player.getQuestList().getQuestState(QUEST_NAME_TUTORIAL);
		if (qs == null)
			return null;
		
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			st = newQuestState(player);
		
		final int npcId = npc.getNpcId();
		
		if (npcId == 31076 || npcId == 31077)
			return npcId + ".htm";
		
		if (npcId >= 30598 && npcId <= 30602)
		{
			if (!st.isCompleted())
			{
				if (player.isMageClass())
				{
					playTutorialVoice(player, "tutorial_voice_027");
					giveItems(player, SPIRITSHOT_NO_GRADE_FOR_BEGINNERS, 100);
				}
				else
				{
					playTutorialVoice(player, "tutorial_voice_026");
					giveItems(player, SOULSHOT_NO_GRADE_FOR_BEGINNERS, 200);
				}
				giveItems(player, NEWBIE_TRAVEL_TOKEN, 12);
				
				st.setState(QuestStatus.COMPLETED);
			}
			return npcId + ".htm";
		}
		
		if (npcId == 30008 || npcId == 30017 || npcId == 30129 || npcId == 30370 || npcId == 30528 || npcId == 30573)
		{
			if (st.isCompleted())
				return npcId + "-04.htm";
			
			final int step = st.getInteger("step");
			if (step == 0)
			{
				npc.showChatWindow(player);
				return null;
			}
			
			if (step == 1)
				return npcId + "-01.htm";
			
			if (step == 2)
				return npcId + "-02.htm";
			
			if (step == 3 || qs.getInteger("ucMemo") >= 3)
				return npcId + "-04.htm";
		}
		
		if (npcId == 30009 || npcId == 30019 || npcId == 30131 || npcId == 30400 || npcId == 30530 || npcId == 30575)
		{
			final int level = player.getStatus().getLevel();
			if (level >= 10 || st.getInteger("onlyone") == 1)
				return "newbiehelper_03.htm";
			
			String htmltext = "newbiehelper_fig_01.htm";
			
			final int step = st.getInteger("step");
			if (step == 0)
			{
				qs.set("Ex", 0);
				
				st.set("step", 1);
				st.setState(QuestStatus.STARTED);
				
				startQuestTimer("TimerEx_NewbieHelper", null, player, 30000);
				
				if (player.isMageClass())
					htmltext = (player.getClassId() == ClassId.ORC_MYSTIC) ? "newbiehelper_mage_02.htm" : "newbiehelper_mage_01.htm";
			}
			else if (step == 1 && qs.getInteger("Ex") <= 2)
			{
				if (player.getInventory().hasAtLeastOneItem(BLUE_GEMSTONE))
				{
					qs.set("Ex", 3);
					qs.set("ucMemo", 3);
					
					st.set("step", 2);
					takeItems(player, BLUE_GEMSTONE, -1);
					giveItems(player, getItemId(npcId), 1);
					
					startQuestTimer("TimerEx_NewbieHelper", null, player, 30000);
					
					if (player.isMageClass() && player.getClassId() != ClassId.ORC_MYSTIC)
					{
						htmltext = npcId + ((npcId == 30009 || npcId == 30530) ? "-03.htm" : "-03a.htm");
						
						giveItems(player, SPIRITSHOT_NO_GRADE_FOR_BEGINNERS, 100);
						playTutorialVoice(player, "tutorial_voice_027");
					}
					else
					{
						htmltext = npcId + "-03.htm";
						giveItems(player, SOULSHOT_NO_GRADE_FOR_BEGINNERS, 200);
						playTutorialVoice(player, "tutorial_voice_026");
					}
				}
				else if (player.isMageClass())
					htmltext = (player.getClassId() == ClassId.ORC_MYSTIC) ? "newbiehelper_mage_02a.htm" : "newbiehelper_mage_01a.htm";
				else
					htmltext = "newbiehelper_fig_01a.htm";
			}
			else if (step == 2)
				htmltext = npcId + "-04.htm";
			
			return htmltext;
		}
		return null;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final QuestState qs = player.getQuestList().getQuestState(QUEST_NAME_TUTORIAL);
		if (qs == null)
			return;
		
		final int ex = qs.getInteger("Ex");
		if (ex <= 1)
		{
			qs.set("Ex", 2);
			showQuestionMark(player, 3);
			playTutorialVoice(player, "tutorial_voice_011");
		}
		
		if (ex <= 2 && qs.getInteger("Gemstone") == 0 && Rnd.get(100) < 25)
		{
			npc.dropItem(player, BLUE_GEMSTONE, 1);
			playSound(player, SOUND_TUTORIAL);
		}
	}
	
	private static String getInvalidHtm(Npc npc)
	{
		switch (npc.getTemplate().getRace())
		{
			case HUMAN:
				return "human/guide_human_cnacelot002.htm";
			
			case ELVE:
				return "elf/guide_elf_roios002.htm";
			
			case DARKELVE:
				return "darkelf/guide_delf_frankia002.htm";
			
			case ORC:
				return "orc/guide_orc_tanai002.htm";
			
			case DWARVE:
				return "dwarf/guide_dwarf_gullin002.htm";
		}
		return null;
	}
	
	private static int getItemId(int npcId)
	{
		if (npcId == 30008 || npcId == 30009)
			return RECOMMENDATION_01;
		
		if (npcId == 30017 || npcId == 30019)
			return RECOMMENDATION_02;
		
		if (npcId == 30129 || npcId == 30131)
			return BLOOD_OF_JUNDIN;
		
		if (npcId == 30370 || npcId == 30400)
			return LEAF_OF_MOTHERTREE;
		
		if (npcId == 30528 || npcId == 30530)
			return LICENSE_OF_MINER;
		
		if (npcId == 30573 || npcId == 30575)
			return VOUCHER_OF_FLAME;
		
		return 0;
	}
}