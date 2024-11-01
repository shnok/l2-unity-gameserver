package com.shnok.javaserver.gameserver.scripting.script.feature;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.records.TutorialEvent;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Tutorial extends Quest
{
	private static final String QUEST_NAME = "Tutorial";
	private static final String QUEST_NAME_101 = "Q101_SwordOfSolidarity";
	private static final String QUEST_NAME_102 = "Q102_SeaOfSporesFever";
	private static final String QUEST_NAME_103 = "Q103_SpiritOfCraftsman";
	private static final String QUEST_NAME_104 = "Q104_SpiritOfMirrors";
	private static final String QUEST_NAME_105 = "Q105_SkirmishWithTheOrcs";
	private static final String QUEST_NAME_106 = "Q106_ForgottenTruth";
	private static final String QUEST_NAME_107 = "Q107_MercilessPunishment";
	private static final String QUEST_NAME_108 = "Q108_JumbleTumbleDiamondFuss";
	
	private static final Map<Integer, TutorialEvent> EVENTS = HashMap.newHashMap(9);
	
	// Table for Tutorial Close Link (26) 2nd class transfer [raceId, html]
	private static final Map<Integer, String> TCLa = HashMap.newHashMap(18);
	
	// Table for Tutorial Close Link (23) 2nd class transfer [raceId, html]
	private static final Map<Integer, String> TCLb = HashMap.newHashMap(11);
	
	// Table for Tutorial Close Link (24) 2nd class transfer [raceId, html]
	private static final Map<Integer, String> TCLc = HashMap.newHashMap(11);
	
	private static final int TUTORIAL_GUIDE = 5588;
	private static final int BLUE_GEMSTONE = 6353;
	
	public Tutorial()
	{
		super(-1, "feature");
		
		EVENTS.put(0, new TutorialEvent("tutorial_voice_001a", "tutorial_human_fighter001.htm", "tutorial_human_fighter007.htm", new Location(-71424, 258336, -3109), "tutorial_fighter017.htm", new Location(-83020, 242553, -3718), "tutorial_newbie003a.htm", "tutorial_21.htm", Location.DUMMY_LOC));
		EVENTS.put(10, new TutorialEvent("tutorial_voice_001b", "tutorial_human_mage001.htm", "tutorial_human_mage007.htm", new Location(-91036, 248044, -3568), "tutorial_mage017.htm", Location.DUMMY_LOC, "tutorial_newbie003a.htm", "tutorial_21a.htm", new Location(-84981, 244764, -3726)));
		EVENTS.put(18, new TutorialEvent("tutorial_voice_001c", "tutorial_elven_fighter001.htm", "tutorial_elf007.htm", new Location(46112, 41200, -3504), "tutorial_fighter017.htm", new Location(45061, 52468, -2796), "tutorial_newbie003b.htm", "tutorial_21b.htm", Location.DUMMY_LOC));
		EVENTS.put(25, new TutorialEvent("tutorial_voice_001d", "tutorial_elven_mage001.htm", "tutorial_elf007.htm", new Location(46112, 41200, -3504), "tutorial_mage017.htm", Location.DUMMY_LOC, "tutorial_newbie003b.htm", "tutorial_21c.htm", new Location(45701, 52459, -2796)));
		EVENTS.put(31, new TutorialEvent("tutorial_voice_001e", "tutorial_delf_fighter001.htm", "tutorial_delf007.htm", new Location(28384, 11056, -4233), "tutorial_fighter017.htm", new Location(10447, 14620, -4242), "tutorial_newbie003c.htm", "tutorial_21g.htm", Location.DUMMY_LOC));
		EVENTS.put(38, new TutorialEvent("tutorial_voice_001f", "tutorial_delf_mage001.htm", "tutorial_delf007.htm", new Location(28384, 11056, -4233), "tutorial_mage017.htm", Location.DUMMY_LOC, "tutorial_newbie003c.htm", "tutorial_21h.htm", new Location(10344, 14445, -4242)));
		EVENTS.put(44, new TutorialEvent("tutorial_voice_001g", "tutorial_orc_fighter001.htm", "tutorial_orc007.htm", new Location(-56736, -113680, -672), "tutorial_orc_fighter017.htm", new Location(-46389, -113905, -21), "tutorial_newbie003d.htm", "tutorial_21d.htm", Location.DUMMY_LOC));
		EVENTS.put(49, new TutorialEvent("tutorial_voice_001h", "tutorial_orc_mage001.htm", "tutorial_orc007.htm", new Location(-56736, -113680, -672), "tutorial_mage017.htm", Location.DUMMY_LOC, "tutorial_newbie003d.htm", "tutorial_21e.htm", new Location(-46225, -113312, -21)));
		EVENTS.put(53, new TutorialEvent("tutorial_voice_001i", "tutorial_dwarven_fighter001.htm", "tutorial_dwarven_fighter007.htm", new Location(108567, -173994, -406), "tutorial_dwarven017.htm", new Location(115271, -182692, -1445), "tutorial_newbie003e.htm", "tutorial_21f.htm", Location.DUMMY_LOC));
		
		TCLa.put(1, "tutorial_22w.htm");
		TCLa.put(4, "tutorial_22.htm");
		TCLa.put(7, "tutorial_22b.htm");
		TCLa.put(11, "tutorial_22c.htm");
		TCLa.put(15, "tutorial_22d.htm");
		TCLa.put(19, "tutorial_22e.htm");
		TCLa.put(22, "tutorial_22f.htm");
		TCLa.put(26, "tutorial_22g.htm");
		TCLa.put(29, "tutorial_22h.htm");
		TCLa.put(32, "tutorial_22n.htm");
		TCLa.put(35, "tutorial_22o.htm");
		TCLa.put(39, "tutorial_22p.htm");
		TCLa.put(42, "tutorial_22q.htm");
		TCLa.put(45, "tutorial_22i.htm");
		TCLa.put(47, "tutorial_22j.htm");
		TCLa.put(50, "tutorial_22k.htm");
		TCLa.put(54, "tutorial_22l.htm");
		TCLa.put(56, "tutorial_22m.htm");
		
		TCLb.put(4, "tutorial_22aa.htm");
		TCLb.put(7, "tutorial_22ba.htm");
		TCLb.put(11, "tutorial_22ca.htm");
		TCLb.put(15, "tutorial_22da.htm");
		TCLb.put(19, "tutorial_22ea.htm");
		TCLb.put(22, "tutorial_22fa.htm");
		TCLb.put(26, "tutorial_22ga.htm");
		TCLb.put(32, "tutorial_22na.htm");
		TCLb.put(35, "tutorial_22oa.htm");
		TCLb.put(39, "tutorial_22pa.htm");
		TCLb.put(50, "tutorial_22ka.htm");
		
		TCLc.put(4, "tutorial_22ab.htm");
		TCLc.put(7, "tutorial_22bb.htm");
		TCLc.put(11, "tutorial_22cb.htm");
		TCLc.put(15, "tutorial_22db.htm");
		TCLc.put(19, "tutorial_22eb.htm");
		TCLc.put(22, "tutorial_22fb.htm");
		TCLc.put(26, "tutorial_22gb.htm");
		TCLc.put(32, "tutorial_22nb.htm");
		TCLc.put(35, "tutorial_22ob.htm");
		TCLc.put(39, "tutorial_22pb.htm");
		TCLc.put(50, "tutorial_22kb.htm");
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.startsWith("QT"))
		{
			final QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
			if (st == null)
				return null;
			
			final int Ex = st.getInteger("Ex");
			if (Ex == -2)
			{
				final TutorialEvent evt = EVENTS.get(player.getClassId().getId());
				if (evt == null)
					return null;
				
				if (!player.getInventory().hasItem(TUTORIAL_GUIDE))
					giveItems(player, TUTORIAL_GUIDE, 1);
				
				st.set("Ex", -3);
				playTutorialVoice(player, evt.initialVoice());
				
				cancelQuestTimers("QT");
				startQuestTimer("QT", null, player, 30000);
				
				showTutorialHTML(player, evt.initialHtm());
			}
			else if (Ex == -3)
			{
				st.set("Ex", 0);
				playTutorialVoice(player, "tutorial_voice_002");
			}
			else if (Ex == -4)
			{
				st.set("Ex", -5);
				playTutorialVoice(player, "tutorial_voice_008");
			}
		}
		return null;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		final QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return null;
		
		String html = "";
		
		final int classId = player.getClassId().getId();
		
		if (event.startsWith("UC"))
		{
			if (player.getStatus().getLevel() < 6 && st.getInteger("onlyone") == 0)
			{
				switch (st.getInteger("ucMemo"))
				{
					case 0:
						st.set("Ex", -2);
						st.set("ucMemo", 1);
						
						startQuestTimer("QT", null, player, 10000);
						break;
					
					case 1:
						showQuestionMark(player, 1);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_006");
						break;
					
					case 2:
						final QuestState qs101 = player.getQuestList().getQuestState(QUEST_NAME_101);
						final QuestState qs102 = player.getQuestList().getQuestState(QUEST_NAME_102);
						final QuestState qs103 = player.getQuestList().getQuestState(QUEST_NAME_103);
						final QuestState qs104 = player.getQuestList().getQuestState(QUEST_NAME_104);
						final QuestState qs105 = player.getQuestList().getQuestState(QUEST_NAME_105);
						final QuestState qs106 = player.getQuestList().getQuestState(QUEST_NAME_106);
						final QuestState qs107 = player.getQuestList().getQuestState(QUEST_NAME_107);
						final QuestState qs108 = player.getQuestList().getQuestState(QUEST_NAME_108);
						
						if (qs101 != null || qs102 != null || qs103 != null || qs104 != null || qs105 != null || qs106 != null || qs107 != null || qs108 != null)
						{
							st.set("ucMemo", 5);
							showQuestionMark(player, 6);
						}
						else
							showQuestionMark(player, 2);
						
						playSound(player, SOUND_TUTORIAL);
						break;
					
					case 3:
						if (st.getInteger("Ex") == 2)
							showQuestionMark(player, 3);
						else if (player.getInventory().hasItem(BLUE_GEMSTONE))
							showQuestionMark(player, 5);
						
						playSound(player, SOUND_TUTORIAL);
						break;
					
					case 4:
						showQuestionMark(player, 12);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_025");
						break;
				}
				onTutorialClientEvent(player, 0);
			}
		}
		// Tutorial close
		else if (event.startsWith("TE"))
		{
			cancelQuestTimers("TE");
			
			if (!event.equalsIgnoreCase("TE"))
			{
				switch (Integer.valueOf(event.substring(2)))
				{
					case 0:
						closeTutorialHtml(player);
						break;
					
					case 1:
						st.set("Ex", -4);
						closeTutorialHtml(player);
						showQuestionMark(player, 1);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_006");
						
						startQuestTimer("QT", null, player, 30000);
						break;
					
					case 2:
						html = "tutorial_02.htm";
						st.set("Ex", -5);
						onTutorialClientEvent(player, 1);
						playTutorialVoice(player, "tutorial_voice_003");
						break;
					
					case 3:
						html = "tutorial_03.htm";
						onTutorialClientEvent(player, 2);
						break;
					
					case 4:
						html = "tutorial_04.htm";
						onTutorialClientEvent(player, 4);
						break;
					
					case 5:
						html = "tutorial_05.htm";
						onTutorialClientEvent(player, 8);
						break;
					
					case 6:
						html = "tutorial_06.htm";
						onTutorialClientEvent(player, 16);
						break;
					
					case 7:
						html = "tutorial_100.htm";
						onTutorialClientEvent(player, 0);
						break;
					
					case 8:
						html = "tutorial_101.htm";
						onTutorialClientEvent(player, 0);
						break;
					
					case 9:
						html = "tutorial_102.htm";
						onTutorialClientEvent(player, 0);
						break;
					
					case 10:
						html = "tutorial_103.htm";
						onTutorialClientEvent(player, 0);
						break;
					
					case 11:
						html = "tutorial_104.htm";
						onTutorialClientEvent(player, 0);
						break;
					
					case 12:
						closeTutorialHtml(player);
						break;
					
					case 23:
						html = TCLb.getOrDefault(classId, html);
						break;
					
					case 24:
						html = TCLc.getOrDefault(classId, html);
						break;
					
					case 25:
						html = "tutorial_22cc.htm";
						break;
					
					case 26:
						html = TCLa.getOrDefault(classId, html);
						break;
					
					case 27:
						html = "tutorial_29.htm";
						break;
					
					case 28:
						html = "tutorial_28.htm";
						break;
					
					case 29, 30:
						html = "tutorial_07a.htm";
						break;
				}
			}
		}
		// Client Event
		else if (event.startsWith("CE"))
		{
			final int level = player.getStatus().getLevel();
			
			switch (Integer.valueOf(event.substring(2)))
			{
				case 1:
					if (level < 6)
					{
						html = "tutorial_03.htm";
						onTutorialClientEvent(player, 2);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_004");
					}
					break;
				
				case 2:
					if (level < 6)
					{
						html = "tutorial_05.htm";
						onTutorialClientEvent(player, 8);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_005");
					}
					break;
				
				case 8:
					if (level < 6)
					{
						final TutorialEvent evt = EVENTS.get(classId);
						if (evt == null)
							return null;
						
						html = evt.ce8Htm();
						st.set("Ex", -5);
						st.set("ucMemo", 1);
						player.getRadarList().addMarker(evt.ce8Loc());
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_007");
					}
					break;
				
				case 30:
					if (level < 10 && st.getInteger("Die") == 0)
					{
						st.set("Die", 1);
						showQuestionMark(player, 8);
						onTutorialClientEvent(player, 0);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_016");
					}
					break;
				
				case 40:
					final int qLvl = st.getInteger("lvl");
					switch (level)
					{
						case 5:
							if (qLvl < 5)
							{
								st.set("lvl", 5);
								showQuestionMark(player, 9);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, (player.isMageClass()) ? "tutorial_voice_015" : "tutorial_voice_014");
							}
							break;
						
						case 6:
							if (qLvl < 6)
							{
								st.set("lvl", 6);
								showQuestionMark(player, 24);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, "tutorial_voice_020");
							}
							break;
						
						case 7:
							if (qLvl < 7 && player.isMageClass())
							{
								st.set("lvl", 7);
								showQuestionMark(player, 11);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, "tutorial_voice_019");
								
								final TutorialEvent evt = EVENTS.get(classId);
								if (evt != null)
									player.getRadarList().addMarker(evt.ce47Loc());
							}
							break;
						
						case 9:
							if (qLvl < 9 && classId == 0)
							{
								st.set("lvl", 9);
								showQuestionMark(player, 25);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, "tutorial_voice_021");
							}
							break;
						
						case 10:
							if (qLvl < 10 && classId != 0 && classId != 44 && classId != 49)
							{
								st.set("lvl", 10);
								showQuestionMark(player, 25);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, "tutorial_voice_021");
							}
							break;
						
						case 12:
							if (qLvl < 12 && classId == 44 || classId == 49)
							{
								st.set("lvl", 12);
								showQuestionMark(player, 25);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, "tutorial_voice_021");
							}
							break;
						
						case 15:
							if (qLvl < 15)
							{
								st.set("lvl", 15);
								showQuestionMark(player, 17);
								playSound(player, SOUND_TUTORIAL);
							}
							break;
						
						case 19:
							if (qLvl < 19)
							{
								st.set("lvl", 19);
								showQuestionMark(player, 13);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, "tutorial_voice_022");
							}
							break;
						
						case 35:
							if (qLvl < 35)
							{
								st.set("lvl", 35);
								showQuestionMark(player, 15);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, "tutorial_voice_023");
							}
							break;
						
						case 75:
							if (qLvl < 75)
							{
								st.set("lvl", 75);
								showQuestionMark(player, 16);
								playSound(player, SOUND_TUTORIAL);
								playTutorialVoice(player, "tutorial_voice_024");
							}
							break;
					}
					break;
				
				case 45:
					if (level < 6 && st.getInteger("HP") == 0)
					{
						st.set("HP", 1);
						st.set("sit", 8388608);
						showQuestionMark(player, 10);
						onTutorialClientEvent(player, 8388608);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_017");
					}
					break;
				
				case 57:
					if (level < 6 && st.getInteger("Adena") == 0)
					{
						st.set("Adena", 1);
						showQuestionMark(player, 23);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_012");
					}
					break;
				
				case 6353:
					if (level < 6 && st.getInteger("Gemstone") == 0)
					{
						st.set("Gemstone", 1);
						showQuestionMark(player, 5);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_013");
					}
					break;
				
				case 1048576:
					if (level < 6)
					{
						showQuestionMark(player, 5);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_013");
					}
					break;
				
				case 8388608:
					if (level < 6 && st.getInteger("sit") == 8388608)
					{
						html = "tutorial_21z.htm";
						st.set("sit", 1);
						onTutorialClientEvent(player, 0);
						playSound(player, SOUND_TUTORIAL);
						playTutorialVoice(player, "tutorial_voice_018");
					}
					break;
			}
		}
		// Question mark clicked
		else if (event.startsWith("QM"))
		{
			switch (Integer.valueOf(event.substring(2)))
			{
				case 1:
					TutorialEvent evt = EVENTS.get(classId);
					if (evt == null)
						return null;
					
					html = evt.ce8Htm();
					st.set("Ex", -5);
					st.set("ucMemo", 2);
					player.getRadarList().addMarker(evt.ce8Loc());
					playTutorialVoice(player, "tutorial_voice_007");
					break;
				
				case 2:
					switch (player.getClassId())
					{
						case HUMAN_FIGHTER:
							html = "tutorial_human_fighter008.htm";
							break;
						
						case HUMAN_MYSTIC:
							html = "tutorial_human_mage008.htm";
							break;
						
						case ELVEN_FIGHTER, ELVEN_MYSTIC:
							html = "tutorial_elf008.htm";
							break;
						
						case DARK_FIGHTER, DARK_MYSTIC:
							html = "tutorial_delf008.htm";
							break;
						
						case ORC_FIGHTER, ORC_MYSTIC:
							html = "tutorial_orc008.htm";
							break;
						
						case DWARVEN_FIGHTER:
							html = "tutorial_dwarven_fighter008.htm";
							break;
					}
					break;
				
				case 3:
					html = "tutorial_09.htm";
					onTutorialClientEvent(player, 1048576);
					break;
				
				case 4:
					html = "tutorial_10.htm";
					break;
				
				case 5:
					evt = EVENTS.get(classId);
					if (evt == null)
						return null;
					
					html = "tutorial_11.htm";
					player.getRadarList().addMarker(evt.ce8Loc());
					break;
				
				case 7:
					html = "tutorial_15.htm";
					st.set("ucMemo", 5);
					break;
				
				case 8:
					html = "tutorial_18.htm";
					break;
				
				case 9:
					evt = EVENTS.get(classId);
					if (evt == null)
						return null;
					
					html = evt.qmc9Htm();
					if (!evt.qmc9Loc().equals(Location.DUMMY_LOC))
						player.getRadarList().addMarker(evt.qmc9Loc());
					break;
				
				case 10:
					html = "tutorial_19.htm";
					break;
				
				case 11:
					switch (player.getRace())
					{
						case HUMAN:
							html = "tutorial_mage020.htm";
							break;
						
						case ELF, DARK_ELF:
							html = "tutorial_mage_elf020.htm";
							break;
						
						case ORC:
							html = "tutorial_mage_orc020.htm";
							break;
					}
					break;
				
				case 12:
					html = "tutorial_15.htm";
					st.set("ucMemo", 4);
					break;
				
				case 13:
					evt = EVENTS.get(classId);
					if (evt == null)
						return null;
					
					html = evt.qmc35Htm();
					break;
				
				case 15:
					html = "tutorial_28.htm";
					break;
				
				case 16:
					html = "tutorial_30.htm";
					break;
				
				case 17:
					html = "tutorial_27.htm";
					break;
				
				case 19:
					html = "tutorial_07.htm";
					break;
				
				case 22:
					html = "tutorial_14.htm";
					break;
				
				case 23:
					html = "tutorial_24.htm";
					break;
				
				case 24:
					evt = EVENTS.get(classId);
					if (evt == null)
						return null;
					
					html = evt.qmc24Htm();
					break;
				
				case 25:
					switch (player.getClassId())
					{
						case HUMAN_FIGHTER:
							html = "tutorial_newbie002a.htm";
							break;
						
						case HUMAN_MYSTIC:
							html = "tutorial_newbie002b.htm";
							break;
						
						case ELVEN_FIGHTER, ELVEN_MYSTIC:
							html = "tutorial_newbie002c.htm";
							break;
						
						case DARK_FIGHTER:
							html = "tutorial_newbie002e.htm";
							break;
						
						case DARK_MYSTIC:
							html = "tutorial_newbie002d.htm";
							break;
						
						case ORC_FIGHTER, ORC_MYSTIC:
							html = "tutorial_newbie002f.htm";
							break;
						
						case DWARVEN_FIGHTER:
							html = "tutorial_newbie002g.htm";
							break;
					}
					break;
				
				case 26:
					if (player.isMageClass() && classId != 49)
						html = "tutorial_newbie004a.htm";
					else
						html = "tutorial_newbie004b.htm";
					break;
			}
		}
		
		if (!html.isEmpty())
			showTutorialHTML(player, html);
		
		return null;
	}
}