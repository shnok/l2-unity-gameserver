package com.shnok.javaserver.gameserver.scripting.script.feature;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class FirstClassChange extends Quest
{
	// Quest Items Dark Elf
	private static final int GAZE_OF_ABYSS = 1244;
	private static final int IRON_HEART = 1252;
	private static final int JEWEL_OF_DARKNESS = 1261;
	private static final int ORB_OF_ABYSS = 1270;
	
	// Quest Items Orcs
	private static final int MARK_OF_RAIDER = 1592;
	private static final int KHAVATARI_TOTEM = 1615;
	private static final int MASK_OF_MEDIUM = 1631;
	
	// Quest Items Dwarf
	private static final int ARTI_MARKS = 1635;
	private static final int SCAV_MARKS = 1642;
	
	// Quest Items Light Elf
	private static final int ELVEN_KNIGHT_BROOCH = 1204;
	private static final int REORIA_RECOMMENDATION = 1217;
	private static final int ETERNITY_DIAMOND = 1230;
	private static final int LEAF_OF_ORACLE = 1235;
	
	// Quest Items Human
	private static final int MEDALLION_OF_WARRIOR = 1145;
	private static final int SWORD_OF_RITUAL = 1161;
	private static final int BEZIQUES_RECOMMENDATION = 1190;
	private static final int BEAD_OF_SEASON = 1292;
	private static final int MARK_OF_FAITH = 1201;
	
	// Reward Item
	private static final int D_GRADE_COUPON = 8869;
	
	// Classes
	private static final Map<String, int[]> CLASSES = new HashMap<>();
	{
		// Dark Elf
		CLASSES.put("PK", new int[]
		{
			32,
			31,
			2,
			15,
			16,
			17,
			18,
			GAZE_OF_ABYSS,
			33
		});
		CLASSES.put("AS", new int[]
		{
			35,
			31,
			2,
			19,
			20,
			21,
			22,
			IRON_HEART,
			33
		});
		CLASSES.put("DW", new int[]
		{
			39,
			38,
			2,
			23,
			24,
			25,
			26,
			JEWEL_OF_DARKNESS,
			33
		});
		CLASSES.put("SO", new int[]
		{
			42,
			38,
			2,
			27,
			28,
			29,
			30,
			ORB_OF_ABYSS,
			33
		});
		
		// Orc
		CLASSES.put("OR", new int[]
		{
			45,
			44,
			3,
			9,
			10,
			11,
			12,
			MARK_OF_RAIDER,
			23
		});
		CLASSES.put("OM", new int[]
		{
			47,
			44,
			3,
			13,
			14,
			15,
			16,
			KHAVATARI_TOTEM,
			23
		});
		CLASSES.put("OS", new int[]
		{
			50,
			49,
			3,
			17,
			18,
			19,
			20,
			MASK_OF_MEDIUM,
			23
		});
		
		// Dwarf
		CLASSES.put("SC", new int[]
		{
			54,
			53,
			4,
			5,
			6,
			7,
			8,
			SCAV_MARKS,
			11
		});
		CLASSES.put("AR", new int[]
		{
			56,
			53,
			4,
			5,
			6,
			7,
			8,
			ARTI_MARKS,
			11
		});
		
		// Light Elf
		CLASSES.put("EK", new int[]
		{
			19,
			18,
			1,
			18,
			19,
			20,
			21,
			ELVEN_KNIGHT_BROOCH,
			40
		});
		CLASSES.put("ES", new int[]
		{
			22,
			18,
			1,
			22,
			23,
			24,
			25,
			REORIA_RECOMMENDATION,
			40
		});
		CLASSES.put("EW", new int[]
		{
			26,
			25,
			1,
			15,
			16,
			17,
			18,
			ETERNITY_DIAMOND,
			33
		});
		CLASSES.put("EO", new int[]
		{
			29,
			25,
			1,
			19,
			20,
			21,
			22,
			LEAF_OF_ORACLE,
			33
		});
		
		// Human
		CLASSES.put("HW", new int[]
		{
			1,
			0,
			0,
			26,
			27,
			28,
			29,
			MEDALLION_OF_WARRIOR,
			40
		});
		CLASSES.put("HK", new int[]
		{
			4,
			0,
			0,
			30,
			31,
			32,
			33,
			SWORD_OF_RITUAL,
			40
		});
		CLASSES.put("HR", new int[]
		{
			7,
			0,
			0,
			34,
			35,
			36,
			37,
			BEZIQUES_RECOMMENDATION,
			40
		});
		CLASSES.put("HWI", new int[]
		{
			11,
			10,
			0,
			23,
			24,
			25,
			26,
			BEAD_OF_SEASON,
			33
		});
		CLASSES.put("HC", new int[]
		{
			15,
			10,
			0,
			27,
			28,
			29,
			30,
			MARK_OF_FAITH,
			33
		});
	}
	
	// Also used by ShadowWeapon script.
	protected static final int[] FIRST_CLASS_NPCS =
	{
		30026,
		30031,
		30037,
		30066,
		30070,
		30154,
		30288,
		30289,
		30290,
		30297,
		30358,
		30373,
		30462,
		30498,
		30499,
		30500,
		30503,
		30504,
		30505,
		30508,
		30520,
		30525,
		30565,
		30594,
		30595,
		32092,
		32093,
		32097,
		32098
	};
	
	public FirstClassChange()
	{
		super(-1, "feature");
		
		addTalkId(FIRST_CLASS_NPCS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		
		String suffix = "";
		
		if (CLASSES.containsKey(event))
		{
			// 0 = newClass, 1 = reqClass, 2 = reqRace, 3 = no/no, 4 = no/ok, 5 = ok/no, 6 = ok/ok, 7 = reqItem, 8 = deniedClass
			final int[] array = CLASSES.get(event);
			
			if (player.getClassId().getId() == array[1] && player.getRace().ordinal() == array[2])
			{
				final boolean gotItem = player.getInventory().hasItem(array[7]);
				
				if (player.getStatus().getLevel() < 20)
					suffix = "-" + ((gotItem) ? array[4] : array[3]);
				else
				{
					if (gotItem)
					{
						suffix = "-" + array[6];
						
						takeItems(player, array[7], 1);
						giveItems(player, D_GRADE_COUPON, 15);
						playSound(player, SOUND_FANFARE);
						
						player.setClassId(array[0]);
						player.setBaseClass(array[0]);
						player.refreshHennaList();
						player.broadcastUserInfo();
					}
					else
						suffix = "-" + array[5];
				}
				
				htmltext = npc.getNpcId() + suffix + ".htm";
			}
			else
				htmltext = npc.getNpcId() + "-" + array[8] + ".htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		
		if (player.isSubClassActive())
			return htmltext;
		
		final int npcId = npc.getNpcId();
		switch (npcId)
		{
			case 30290, 30297, 30462: // Dark Elf
				if (player.getRace() == ClassRace.DARK_ELF)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 31)
							htmltext = npcId + "-01.htm";
						else if (player.getClassId().getId() == 38)
							htmltext = npcId + "-08.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-32.htm";
					else
						htmltext = npcId + "-31.htm";
				}
				else
					htmltext = npcId + "-33.htm";
				break;
			
			case 30358: // Thifiell (dark elf)
				if (player.getRace() == ClassRace.DARK_ELF)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 31)
							htmltext = npcId + "-01.htm";
						else if (player.getClassId().getId() == 38)
							htmltext = npcId + "-02.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-12.htm";
					else
						htmltext = npcId + "-13.htm";
				}
				else
					htmltext = npcId + "-11.htm";
				break;
			
			case 30500, 30505, 30508, 32097: // Orcs
				if (player.getRace() == ClassRace.ORC)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 44)
							htmltext = npcId + "-01.htm";
						else if (player.getClassId().getId() == 49)
							htmltext = npcId + "-06.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-21.htm";
					else
						htmltext = npcId + "-22.htm";
				}
				else
					htmltext = npcId + "-23.htm";
				break;
			
			case 30565: // Kakai (Orcs)
				if (player.getRace() == ClassRace.ORC)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 44)
							htmltext = npcId + "-01.htm";
						else if (player.getClassId().getId() == 49)
							htmltext = npcId + "-06.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-09.htm";
					else
						htmltext = npcId + "-10.htm";
				}
				else
					htmltext = npcId + "-11.htm";
				break;
			
			case 30498, 30499, 30503, 30504, 30594, 30595, 32092, 32093: // Dwarf
				if (player.getRace() == ClassRace.DWARF)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 53)
							htmltext = npcId + "-01.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-09.htm";
					else
						htmltext = npcId + "-10.htm";
				}
				else
					htmltext = npcId + "-11.htm";
				break;
			
			case 30520, 30525: // Bronk and Reed(dwarf)
				if (player.getRace() == ClassRace.DWARF)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 53)
							htmltext = npcId + "-01.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-05.htm";
					else
						htmltext = npcId + "-06.htm";
				}
				else
					htmltext = npcId + "-07.htm";
				break;
			
			case 30037, 30070, 30289, 32098: // Elfs and humans mages
				if (player.getRace() == ClassRace.ELF)
				{
					if (player.isMageClass())
					{
						if (player.getClassId().getLevel() == 0)
						{
							if (player.getClassId().getId() == 25)
								htmltext = npcId + "-01.htm";
						}
						else if (player.getClassId().getLevel() == 1)
							htmltext = npcId + "-31.htm";
						else
							htmltext = npcId + "-32.htm";
					}
					else
						htmltext = npcId + "-33.htm";
				}
				else if (player.getRace() == ClassRace.HUMAN)
				{
					if (player.isMageClass())
					{
						if (player.getClassId().getLevel() == 0)
						{
							if (player.getClassId().getId() == 10)
								htmltext = npcId + "-08.htm";
						}
						else if (player.getClassId().getLevel() == 1)
							htmltext = npcId + "-31.htm";
						else
							htmltext = npcId + "-32.htm";
					}
					else
						htmltext = npcId + "-33.htm";
				}
				else
					htmltext = npcId + "-33.htm";
				break;
			
			case 30154: // Asterios (Elf fighters and mages)
				if (player.getRace() == ClassRace.ELF)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 18)
							htmltext = npcId + "-01.htm";
						else if (player.getClassId().getId() == 25)
							htmltext = npcId + "-02.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-12.htm";
					else
						htmltext = npcId + "-13.htm";
				}
				else
					htmltext = npcId + "-11.htm";
				break;
			
			case 30031: // Biotin (Human mages)
				if (player.getRace() == ClassRace.HUMAN)
				{
					if (player.isMageClass())
					{
						if (player.getClassId().getLevel() == 0)
						{
							if (player.getClassId().getId() == 10)
								htmltext = npcId + "-01.htm";
						}
						else if (player.getClassId().getLevel() == 1)
							htmltext = npcId + "-06.htm";
						else
							htmltext = npcId + "-07.htm";
					}
					else
						htmltext = npcId + "-08.htm";
				}
				else
					htmltext = npcId + "-08.htm";
				break;
			
			case 30066, 30288, 30373: // Human and Elfs fighters
				if (player.getRace() == ClassRace.HUMAN)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 0)
							htmltext = npcId + "-08.htm";
						else
							htmltext = npcId + "-40.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-38.htm";
					else
						htmltext = npcId + "-39.htm";
				}
				else if (player.getRace() == ClassRace.ELF)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 18)
							htmltext = npcId + "-01.htm";
						else
							htmltext = npcId + "-40.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-38.htm";
					else
						htmltext = npcId + "-39.htm";
				}
				else
					htmltext = npcId + "-40.htm";
				break;
			
			case 30026: // Bitz (Human fighters)
				if (player.getRace() == ClassRace.HUMAN)
				{
					if (player.getClassId().getLevel() == 0)
					{
						if (player.getClassId().getId() == 0)
							htmltext = npcId + "-01.htm";
						else
							htmltext = npcId + "-10.htm";
					}
					else if (player.getClassId().getLevel() == 1)
						htmltext = npcId + "-08.htm";
					else
						htmltext = npcId + "-09.htm";
				}
				else
					htmltext = npcId + "-10.htm";
				break;
		}
		
		return htmltext;
	}
}