package com.shnok.javaserver.gameserver.scripting.script.teleport;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class CastleDefendTeleporter extends Quest
{
	private static final int GLUDIO_DEFEND_TELEPORTER_1 = 35092;
	private static final int GLUDIO_DEFEND_TELEPORTER_2 = 35093;
	private static final int GLUDIO_DEFEND_TELEPORTER_3 = 35094;
	
	private static final int DION_DEFEND_TELEPORTER_1 = 35134;
	private static final int DION_DEFEND_TELEPORTER_2 = 35135;
	private static final int DION_DEFEND_TELEPORTER_3 = 35136;
	
	private static final int GIRAN_DEFEND_TELEPORTER_1 = 35176;
	private static final int GIRAN_DEFEND_TELEPORTER_2 = 35177;
	private static final int GIRAN_DEFEND_TELEPORTER_3 = 35178;
	
	private static final int OREN_DEFEND_TELEPORTER_1 = 35218;
	private static final int OREN_DEFEND_TELEPORTER_2 = 35219;
	private static final int OREN_DEFEND_TELEPORTER_3 = 35220;
	
	private static final int ADEN_DEFEND_TELEPORTER_1 = 35261;
	private static final int ADEN_DEFEND_TELEPORTER_2 = 35262;
	private static final int ADEN_DEFEND_TELEPORTER_3 = 35263;
	private static final int ADEN_DEFEND_TELEPORTER_4 = 35264;
	private static final int ADEN_DEFEND_TELEPORTER_5 = 35265;
	
	private static final int INNADRIL_DEFEND_TELEPORTER_1 = 35308;
	private static final int INNADRIL_DEFEND_TELEPORTER_2 = 35309;
	private static final int INNADRIL_DEFEND_TELEPORTER_3 = 35310;
	
	private static final int GODDARD_DEFEND_TELEPORTER_1 = 35352;
	private static final int GODDARD_DEFEND_TELEPORTER_2 = 35353;
	private static final int GODDARD_DEFEND_TELEPORTER_3 = 35354;
	
	private static final int RUNE_DEFEND_TELEPORTER_1 = 35497;
	private static final int RUNE_DEFEND_TELEPORTER_2 = 35498;
	private static final int RUNE_DEFEND_TELEPORTER_3 = 35499;
	private static final int RUNE_DEFEND_TELEPORTER_4 = 35500;
	private static final int RUNE_DEFEND_TELEPORTER_5 = 35501;
	
	private static final int SCHUTTGART_DEFEND_TELEPORTER_1 = 35544;
	private static final int SCHUTTGART_DEFEND_TELEPORTER_2 = 35545;
	private static final int SCHUTTGART_DEFEND_TELEPORTER_3 = 35546;
	
	private static final Map<Integer, String[]> DEFEND_DIALOGUES = new HashMap<>();
	{
		DEFEND_DIALOGUES.put(GLUDIO_DEFEND_TELEPORTER_1, new String[]
		{
			"first_group_1.htm",
			"first_group_1_no.htm"
		});
		DEFEND_DIALOGUES.put(GLUDIO_DEFEND_TELEPORTER_2, new String[]
		{
			"first_group_2.htm",
			"first_group_2_no.htm"
		});
		DEFEND_DIALOGUES.put(GLUDIO_DEFEND_TELEPORTER_3, new String[]
		{
			"first_group_3.htm",
			"first_group_3_no.htm"
		});
		
		DEFEND_DIALOGUES.put(DION_DEFEND_TELEPORTER_1, new String[]
		{
			"first_group_1.htm",
			"first_group_1_no.htm"
		});
		DEFEND_DIALOGUES.put(DION_DEFEND_TELEPORTER_2, new String[]
		{
			"first_group_2.htm",
			"first_group_2_no.htm"
		});
		DEFEND_DIALOGUES.put(DION_DEFEND_TELEPORTER_3, new String[]
		{
			"first_group_3.htm",
			"first_group_3_no.htm"
		});
		
		DEFEND_DIALOGUES.put(GIRAN_DEFEND_TELEPORTER_1, new String[]
		{
			"first_group_1.htm",
			"first_group_1_no.htm"
		});
		DEFEND_DIALOGUES.put(GIRAN_DEFEND_TELEPORTER_2, new String[]
		{
			"first_group_2.htm",
			"first_group_2_no.htm"
		});
		DEFEND_DIALOGUES.put(GIRAN_DEFEND_TELEPORTER_3, new String[]
		{
			"first_group_3.htm",
			"first_group_3_no.htm"
		});
		
		DEFEND_DIALOGUES.put(OREN_DEFEND_TELEPORTER_1, new String[]
		{
			"first_group_1.htm",
			"first_group_1_no.htm"
		});
		DEFEND_DIALOGUES.put(OREN_DEFEND_TELEPORTER_2, new String[]
		{
			"first_group_2.htm",
			"first_group_2_no.htm"
		});
		DEFEND_DIALOGUES.put(OREN_DEFEND_TELEPORTER_3, new String[]
		{
			"first_group_3.htm",
			"first_group_3_no.htm"
		});
		
		DEFEND_DIALOGUES.put(ADEN_DEFEND_TELEPORTER_1, new String[]
		{
			"second_group_1.htm",
			"second_group_no.htm"
		});
		DEFEND_DIALOGUES.put(ADEN_DEFEND_TELEPORTER_2, new String[]
		{
			"second_group_2.htm",
			"second_group_no.htm"
		});
		DEFEND_DIALOGUES.put(ADEN_DEFEND_TELEPORTER_3, new String[]
		{
			"second_group_3.htm",
			"second_group_no.htm"
		});
		DEFEND_DIALOGUES.put(ADEN_DEFEND_TELEPORTER_4, new String[]
		{
			"second_group_4.htm",
			"second_group_no.htm"
		});
		DEFEND_DIALOGUES.put(ADEN_DEFEND_TELEPORTER_5, new String[]
		{
			"second_group_5.htm",
			"second_group_no.htm"
		});
		
		DEFEND_DIALOGUES.put(INNADRIL_DEFEND_TELEPORTER_1, new String[]
		{
			"first_group_1.htm",
			"first_group_1_no.htm"
		});
		DEFEND_DIALOGUES.put(INNADRIL_DEFEND_TELEPORTER_2, new String[]
		{
			"first_group_2.htm",
			"first_group_2_no.htm"
		});
		DEFEND_DIALOGUES.put(INNADRIL_DEFEND_TELEPORTER_3, new String[]
		{
			"first_group_3.htm",
			"first_group_3_no.htm"
		});
		
		DEFEND_DIALOGUES.put(GODDARD_DEFEND_TELEPORTER_1, new String[]
		{
			"first_group_1.htm",
			"first_group_1_no.htm"
		});
		DEFEND_DIALOGUES.put(GODDARD_DEFEND_TELEPORTER_2, new String[]
		{
			"first_group_2.htm",
			"first_group_2_no.htm"
		});
		DEFEND_DIALOGUES.put(GODDARD_DEFEND_TELEPORTER_3, new String[]
		{
			"first_group_3.htm",
			"first_group_3_no.htm"
		});
		
		DEFEND_DIALOGUES.put(RUNE_DEFEND_TELEPORTER_1, new String[]
		{
			"third_group_1.htm",
			"third_group_no.htm"
		});
		DEFEND_DIALOGUES.put(RUNE_DEFEND_TELEPORTER_2, new String[]
		{
			"third_group_2.htm",
			"third_group_no.htm"
		});
		DEFEND_DIALOGUES.put(RUNE_DEFEND_TELEPORTER_3, new String[]
		{
			"third_group_3.htm",
			"third_group_no.htm"
		});
		DEFEND_DIALOGUES.put(RUNE_DEFEND_TELEPORTER_4, new String[]
		{
			"third_group_4.htm",
			"third_group_no.htm"
		});
		DEFEND_DIALOGUES.put(RUNE_DEFEND_TELEPORTER_5, new String[]
		{
			"third_group_5.htm",
			"third_group_no.htm"
		});
		
		DEFEND_DIALOGUES.put(SCHUTTGART_DEFEND_TELEPORTER_1, new String[]
		{
			"first_group_1.htm",
			"first_group_1_no.htm"
		});
		DEFEND_DIALOGUES.put(SCHUTTGART_DEFEND_TELEPORTER_2, new String[]
		{
			"first_group_2.htm",
			"first_group_2_no.htm"
		});
		DEFEND_DIALOGUES.put(SCHUTTGART_DEFEND_TELEPORTER_3, new String[]
		{
			"first_group_3.htm",
			"first_group_3_no.htm"
		});
	}
	
	private static final Map<Integer, Location[]> TELEPORT_POINTS_0 = new HashMap<>();
	{
		TELEPORT_POINTS_0.put(GLUDIO_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(-18123, 109927, -2492),
			new Location(-18103, 109947, -2492),
			new Location(-18123, 109947, -2492)
		});
		TELEPORT_POINTS_0.put(GLUDIO_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(-18123, 109927, -2492),
			new Location(-18103, 109947, -2492),
			new Location(-18123, 109947, -2492)
		});
		TELEPORT_POINTS_0.put(DION_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(22094, 159894, -2686),
			new Location(22074, 159874, -2686),
			new Location(22094, 159874, -2686)
		});
		TELEPORT_POINTS_0.put(DION_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(22094, 159894, -2686),
			new Location(22074, 159874, -2686),
			new Location(22094, 159874, -2686)
		});
		TELEPORT_POINTS_0.put(GIRAN_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(115999, 145031, -2561),
			new Location(115979, 145051, -2561),
			new Location(115979, 145031, -2561)
		});
		TELEPORT_POINTS_0.put(GIRAN_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(115999, 145031, -2561),
			new Location(115979, 145051, -2561),
			new Location(115979, 145031, -2561)
		});
		TELEPORT_POINTS_0.put(OREN_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(82077, 37179, -2282),
			new Location(82057, 37199, -2282),
			new Location(82057, 37179, -2282)
		});
		TELEPORT_POINTS_0.put(OREN_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(82077, 37179, -2282),
			new Location(82057, 37199, -2282),
			new Location(82057, 37179, -2282)
		});
		TELEPORT_POINTS_0.put(ADEN_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(147848, 7951, -470),
			new Location(147731, 7886, -470),
			new Location(147874, 7852, -470)
		});
		TELEPORT_POINTS_0.put(ADEN_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(147848, 7951, -470),
			new Location(147731, 7886, -470),
			new Location(147874, 7852, -470)
		});
		TELEPORT_POINTS_0.put(ADEN_DEFEND_TELEPORTER_4, new Location[]
		{
			new Location(147848, 7951, -470),
			new Location(147731, 7886, -470),
			new Location(147874, 7852, -470)
		});
		TELEPORT_POINTS_0.put(ADEN_DEFEND_TELEPORTER_5, new Location[]
		{
			new Location(147848, 7951, -470),
			new Location(147731, 7886, -470),
			new Location(147874, 7852, -470)
		});
		TELEPORT_POINTS_0.put(INNADRIL_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(116034, 248602, -782),
			new Location(116014, 248582, -782),
			new Location(116034, 248582, -782)
		});
		TELEPORT_POINTS_0.put(INNADRIL_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(116034, 248602, -782),
			new Location(116014, 248582, -782),
			new Location(116034, 248582, -782)
		});
		TELEPORT_POINTS_0.put(GODDARD_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(147461, -46782, -1360),
			new Location(147520, -46784, -1360),
			new Location(147419, -46791, -1360)
		});
		TELEPORT_POINTS_0.put(GODDARD_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(147461, -46782, -1360),
			new Location(147520, -46784, -1360),
			new Location(147419, -46791, -1360)
		});
		TELEPORT_POINTS_0.put(RUNE_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(16094, -48945, -1060),
			new Location(16087, -49009, -1060),
			new Location(16143, -48958, -1060)
		});
		TELEPORT_POINTS_0.put(RUNE_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(16094, -48945, -1060),
			new Location(16087, -49009, -1060),
			new Location(16143, -48958, -1060)
		});
		TELEPORT_POINTS_0.put(RUNE_DEFEND_TELEPORTER_4, new Location[]
		{
			new Location(16094, -48945, -1060),
			new Location(16087, -49009, -1060),
			new Location(16143, -48958, -1060)
		});
		TELEPORT_POINTS_0.put(RUNE_DEFEND_TELEPORTER_5, new Location[]
		{
			new Location(16094, -48945, -1060),
			new Location(16087, -49009, -1060),
			new Location(16143, -48958, -1060)
		});
		TELEPORT_POINTS_0.put(SCHUTTGART_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(77546, -150836, 376),
			new Location(77605, -150838, 376),
			new Location(77504, -150845, 376)
		});
		TELEPORT_POINTS_0.put(SCHUTTGART_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(77546, -150836, 376),
			new Location(77605, -150838, 376),
			new Location(77504, -150845, 376)
		});
	}
	
	private static final Map<Integer, Location[]> TELEPORT_POINTS_1 = new HashMap<>();
	{
		TELEPORT_POINTS_1.put(GLUDIO_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(-18635, 108298, -2556),
			new Location(-18615, 108298, -2556),
			new Location(-18635, 108298, -2556)
		});
		TELEPORT_POINTS_1.put(GLUDIO_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(-18635, 108298, -2556),
			new Location(-18615, 108298, -2556),
			new Location(-18635, 108298, -2556)
		});
		TELEPORT_POINTS_1.put(DION_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(22606, 161523, -2750),
			new Location(22586, 161523, -2750),
			new Location(22606, 161523, -2750)
		});
		TELEPORT_POINTS_1.put(DION_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(22606, 161523, -2750),
			new Location(22586, 161523, -2750),
			new Location(22606, 161523, -2750)
		});
		TELEPORT_POINTS_1.put(GIRAN_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(117628, 144519, -2625),
			new Location(117628, 144539, -2625),
			new Location(117628, 144519, -2625)
		});
		TELEPORT_POINTS_1.put(GIRAN_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(117628, 144519, -2625),
			new Location(117628, 144539, -2625),
			new Location(117628, 144519, -2625)
		});
		TELEPORT_POINTS_1.put(OREN_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(83706, 36667, -2346),
			new Location(83706, 36687, -2346),
			new Location(83706, 36667, -2346)
		});
		TELEPORT_POINTS_1.put(OREN_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(83706, 36667, -2346),
			new Location(83706, 36687, -2346),
			new Location(83706, 36667, -2346)
		});
		TELEPORT_POINTS_1.put(ADEN_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(146445, 4739, -408),
			new Location(146411, 4613, -408),
			new Location(146460, 4491, -408)
		});
		TELEPORT_POINTS_1.put(ADEN_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(146445, 4739, -408),
			new Location(146411, 4613, -408),
			new Location(146460, 4491, -408)
		});
		TELEPORT_POINTS_1.put(ADEN_DEFEND_TELEPORTER_4, new Location[]
		{
			new Location(146445, 4739, -408),
			new Location(146411, 4613, -408),
			new Location(146460, 4491, -408)
		});
		TELEPORT_POINTS_1.put(ADEN_DEFEND_TELEPORTER_5, new Location[]
		{
			new Location(146445, 4739, -408),
			new Location(146411, 4613, -408),
			new Location(146460, 4491, -408)
		});
		TELEPORT_POINTS_1.put(INNADRIL_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(116546, 250231, -846),
			new Location(116526, 250231, -846),
			new Location(116546, 250231, -846)
		});
		TELEPORT_POINTS_1.put(INNADRIL_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(116546, 250231, -846),
			new Location(116526, 250231, -846),
			new Location(116546, 250231, -846)
		});
		TELEPORT_POINTS_1.put(GODDARD_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(147449, -50965, -1344),
			new Location(147514, -50970, -1344),
			new Location(147388, -50960, -1344)
		});
		TELEPORT_POINTS_1.put(GODDARD_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(147449, -50965, -1344),
			new Location(147514, -50970, -1344),
			new Location(147388, -50960, -1344)
		});
		TELEPORT_POINTS_1.put(RUNE_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(13076, -51241, -1089),
			new Location(12990, -51241, -1089),
			new Location(13035, -51331, -1089)
		});
		TELEPORT_POINTS_1.put(RUNE_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(13076, -51241, -1089),
			new Location(12990, -51241, -1089),
			new Location(13035, -51331, -1089)
		});
		TELEPORT_POINTS_1.put(RUNE_DEFEND_TELEPORTER_4, new Location[]
		{
			new Location(13076, -51241, -1089),
			new Location(12990, -51241, -1089),
			new Location(13035, -51331, -1089)
		});
		TELEPORT_POINTS_1.put(RUNE_DEFEND_TELEPORTER_5, new Location[]
		{
			new Location(13076, -51241, -1089),
			new Location(12990, -51241, -1089),
			new Location(13035, -51331, -1089)
		});
		TELEPORT_POINTS_1.put(SCHUTTGART_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(77534, -155019, 392),
			new Location(77599, -155024, 392),
			new Location(77473, -155014, 392)
		});
		TELEPORT_POINTS_1.put(SCHUTTGART_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(77534, -155019, 392),
			new Location(77599, -155024, 392),
			new Location(77473, -155014, 392)
		});
	}
	
	private static final Map<Integer, Location[]> TELEPORT_POINTS_2 = new HashMap<>();
	{
		TELEPORT_POINTS_2.put(GLUDIO_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(-18988, 112081, -2724),
			new Location(-18968, 112101, -2724),
			new Location(-18988, 112101, -2724)
		});
		TELEPORT_POINTS_2.put(GLUDIO_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(-18988, 112081, -2724),
			new Location(-18968, 112101, -2724),
			new Location(-18988, 112101, -2724)
		});
		TELEPORT_POINTS_2.put(DION_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(22959, 157740, -2918),
			new Location(22939, 157720, -2918),
			new Location(22959, 157720, -2918)
		});
		TELEPORT_POINTS_2.put(DION_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(22959, 157740, -2918),
			new Location(22939, 157720, -2918),
			new Location(22959, 157720, -2918)
		});
		TELEPORT_POINTS_2.put(GIRAN_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(113845, 144166, -2793),
			new Location(113825, 144186, -2793),
			new Location(113825, 144166, -2793)
		});
		TELEPORT_POINTS_2.put(GIRAN_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(113845, 144166, -2793),
			new Location(113825, 144186, -2793),
			new Location(113825, 144166, -2793)
		});
		TELEPORT_POINTS_2.put(OREN_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(79923, 36314, -2514),
			new Location(79903, 36334, -2514),
			new Location(79903, 36314, -2514)
		});
		TELEPORT_POINTS_2.put(OREN_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(79923, 36314, -2514),
			new Location(79903, 36334, -2514),
			new Location(79903, 36314, -2514)
		});
		TELEPORT_POINTS_2.put(ADEN_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(148551, 4607, -408),
			new Location(148482, 4527, -408),
			new Location(148498, 4707, -408)
		});
		TELEPORT_POINTS_2.put(ADEN_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(148551, 4607, -408),
			new Location(148482, 4527, -408),
			new Location(148498, 4707, -408)
		});
		TELEPORT_POINTS_2.put(ADEN_DEFEND_TELEPORTER_4, new Location[]
		{
			new Location(148551, 4607, -408),
			new Location(148482, 4527, -408),
			new Location(148498, 4707, -408)
		});
		TELEPORT_POINTS_2.put(ADEN_DEFEND_TELEPORTER_5, new Location[]
		{
			new Location(148551, 4607, -408),
			new Location(148482, 4527, -408),
			new Location(148498, 4707, -408)
		});
		TELEPORT_POINTS_2.put(INNADRIL_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(116899, 246448, -1014),
			new Location(116879, 246428, -1014),
			new Location(116899, 246428, -1014)
		});
		TELEPORT_POINTS_2.put(INNADRIL_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(116899, 246448, -1014),
			new Location(116879, 246428, -1014),
			new Location(116899, 246428, -1014)
		});
		TELEPORT_POINTS_2.put(GODDARD_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(147475, -45860, -2080),
			new Location(147399, -45851, -2080),
			new Location(147540, -45841, -2080)
		});
		TELEPORT_POINTS_2.put(GODDARD_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(147475, -45860, -2080),
			new Location(147399, -45851, -2080),
			new Location(147540, -45841, -2080)
		});
		TELEPORT_POINTS_2.put(RUNE_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(14071, -49547, 987),
			new Location(14006, -49523, 987),
			new Location(13911, -49493, 987)
		});
		TELEPORT_POINTS_2.put(RUNE_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(14071, -49547, 987),
			new Location(14006, -49523, 987),
			new Location(13911, -49493, 987)
		});
		TELEPORT_POINTS_2.put(RUNE_DEFEND_TELEPORTER_4, new Location[]
		{
			new Location(14071, -49547, 987),
			new Location(14006, -49523, 987),
			new Location(13911, -49493, 987)
		});
		TELEPORT_POINTS_2.put(RUNE_DEFEND_TELEPORTER_5, new Location[]
		{
			new Location(14071, -49547, 987),
			new Location(14006, -49523, 987),
			new Location(13911, -49493, 987)
		});
		TELEPORT_POINTS_2.put(SCHUTTGART_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(77560, -149914, -344),
			new Location(77484, -149905, -344),
			new Location(77625, -149895, -344)
		});
		TELEPORT_POINTS_2.put(SCHUTTGART_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(77560, -149914, -344),
			new Location(77484, -149905, -344),
			new Location(77625, -149895, -344)
		});
	}
	
	private static final Map<Integer, Location[]> TELEPORT_POINTS_3 = new HashMap<>();
	{
		TELEPORT_POINTS_3.put(ADEN_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(147453, 5354, -345),
			new Location(147501, 5298, -345),
			new Location(147403, 5302, -345)
		});
		TELEPORT_POINTS_3.put(ADEN_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(147453, 5354, -345),
			new Location(147501, 5298, -345),
			new Location(147403, 5302, -345)
		});
		TELEPORT_POINTS_3.put(ADEN_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(147453, 5354, -345),
			new Location(147501, 5298, -345),
			new Location(147403, 5302, -345)
		});
		TELEPORT_POINTS_3.put(ADEN_DEFEND_TELEPORTER_5, new Location[]
		{
			new Location(147453, 5354, -345),
			new Location(147501, 5298, -345),
			new Location(147403, 5302, -345)
		});
		TELEPORT_POINTS_3.put(RUNE_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(13158, -49149, -536),
			new Location(13288, -49148, -536),
			new Location(13417, -49149, -536)
		});
		TELEPORT_POINTS_3.put(RUNE_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(13158, -49149, -536),
			new Location(13288, -49148, -536),
			new Location(13417, -49149, -536)
		});
		TELEPORT_POINTS_3.put(RUNE_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(13158, -49149, -536),
			new Location(13288, -49148, -536),
			new Location(13417, -49149, -536)
		});
		TELEPORT_POINTS_3.put(RUNE_DEFEND_TELEPORTER_5, new Location[]
		{
			new Location(13158, -49149, -536),
			new Location(13288, -49148, -536),
			new Location(13417, -49149, -536)
		});
	}
	
	private static final Map<Integer, Location[]> TELEPORT_POINTS_4 = new HashMap<>();
	{
		TELEPORT_POINTS_4.put(ADEN_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(147050, 1498, -473),
			new Location(146977, 1584, -473),
			new Location(147111, 1586, -473)
		});
		TELEPORT_POINTS_4.put(ADEN_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(147050, 1498, -473),
			new Location(146977, 1584, -473),
			new Location(147111, 1586, -473)
		});
		TELEPORT_POINTS_4.put(ADEN_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(147050, 1498, -473),
			new Location(146977, 1584, -473),
			new Location(147111, 1586, -473)
		});
		TELEPORT_POINTS_4.put(ADEN_DEFEND_TELEPORTER_4, new Location[]
		{
			new Location(147050, 1498, -473),
			new Location(146977, 1584, -473),
			new Location(147111, 1586, -473)
		});
		TELEPORT_POINTS_4.put(RUNE_DEFEND_TELEPORTER_1, new Location[]
		{
			new Location(9762, -49184, 997),
			new Location(9897, -49184, 997),
			new Location(10013, -49184, 997)
		});
		TELEPORT_POINTS_4.put(RUNE_DEFEND_TELEPORTER_2, new Location[]
		{
			new Location(9762, -49184, 997),
			new Location(9897, -49184, 997),
			new Location(10013, -49184, 997)
		});
		TELEPORT_POINTS_4.put(RUNE_DEFEND_TELEPORTER_3, new Location[]
		{
			new Location(9762, -49184, 997),
			new Location(9897, -49184, 997),
			new Location(10013, -49184, 997)
		});
		TELEPORT_POINTS_4.put(RUNE_DEFEND_TELEPORTER_4, new Location[]
		{
			new Location(9762, -49184, 997),
			new Location(9897, -49184, 997),
			new Location(10013, -49184, 997)
		});
	}
	
	public CastleDefendTeleporter()
	{
		super(-1, "teleport");
		
		for (Integer npc : DEFEND_DIALOGUES.keySet())
		{
			addTalkId(npc);
			addFirstTalkId(npc);
		}
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		final Location[] locs = getPoints(npc.getNpcId(), Integer.parseInt(event));
		if (locs != null)
			player.teleportTo(Rnd.get(locs), 0);
		
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final Castle castle = npc.getCastle();
		if (castle != null && castle.getSiege().checkSides(player.getClan(), SiegeSide.OWNER, SiegeSide.DEFENDER))
			return DEFEND_DIALOGUES.get(npc.getNpcId())[0];
		
		return DEFEND_DIALOGUES.get(npc.getNpcId())[1];
	}
	
	private static Location[] getPoints(int npcId, int index)
	{
		switch (index)
		{
			case 0:
				return TELEPORT_POINTS_0.get(npcId);
			
			case 1:
				return TELEPORT_POINTS_1.get(npcId);
			
			case 2:
				return TELEPORT_POINTS_2.get(npcId);
			
			case 3:
				return TELEPORT_POINTS_3.get(npcId);
			
			case 4:
				return TELEPORT_POINTS_4.get(npcId);
		}
		
		return null;
	}
}