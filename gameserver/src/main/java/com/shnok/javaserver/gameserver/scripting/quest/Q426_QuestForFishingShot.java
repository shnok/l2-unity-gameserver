package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q426_QuestForFishingShot extends Quest
{
	private static final String QUEST_NAME = "Q426_QuestForFishingShot";
	
	private static final int SWEET_FLUID = 7586;
	
	private static final Map<Integer, Integer> MOBS1 = new HashMap<>();
	{
		MOBS1.put(20005, 45);
		MOBS1.put(20013, 100);
		MOBS1.put(20016, 100);
		MOBS1.put(20017, 115);
		MOBS1.put(20030, 105);
		MOBS1.put(20132, 70);
		MOBS1.put(20038, 135);
		MOBS1.put(20044, 125);
		MOBS1.put(20046, 100);
		MOBS1.put(20047, 100);
		MOBS1.put(20050, 140);
		MOBS1.put(20058, 140);
		MOBS1.put(20063, 160);
		MOBS1.put(20066, 170);
		MOBS1.put(20070, 180);
		MOBS1.put(20074, 195);
		MOBS1.put(20077, 205);
		MOBS1.put(20078, 205);
		MOBS1.put(20079, 205);
		MOBS1.put(20080, 220);
		MOBS1.put(20081, 370);
		MOBS1.put(20083, 245);
		MOBS1.put(20084, 255);
		MOBS1.put(20085, 265);
		MOBS1.put(20087, 565);
		MOBS1.put(20088, 605);
		MOBS1.put(20089, 250);
		MOBS1.put(20100, 85);
		MOBS1.put(20103, 110);
		MOBS1.put(20105, 110);
		MOBS1.put(20115, 190);
		MOBS1.put(20120, 20);
		MOBS1.put(20131, 45);
		MOBS1.put(20135, 360);
		MOBS1.put(20157, 235);
		MOBS1.put(20162, 195);
		MOBS1.put(20176, 280);
		MOBS1.put(20211, 170);
		MOBS1.put(20225, 160);
		MOBS1.put(20227, 180);
		MOBS1.put(20230, 260);
		MOBS1.put(20232, 245);
		MOBS1.put(20234, 290);
		MOBS1.put(20241, 700);
		MOBS1.put(20267, 215);
		MOBS1.put(20268, 295);
		MOBS1.put(20269, 255);
		MOBS1.put(20270, 365);
		MOBS1.put(20271, 295);
		MOBS1.put(20286, 700);
		MOBS1.put(20308, 110);
		MOBS1.put(20312, 45);
		MOBS1.put(20317, 20);
		MOBS1.put(20324, 85);
		MOBS1.put(20333, 100);
		MOBS1.put(20341, 100);
		MOBS1.put(20346, 85);
		MOBS1.put(20349, 850);
		MOBS1.put(20356, 165);
		MOBS1.put(20357, 140);
		MOBS1.put(20363, 70);
		MOBS1.put(20368, 85);
		MOBS1.put(20371, 100);
		MOBS1.put(20386, 85);
		MOBS1.put(20389, 90);
		MOBS1.put(20403, 110);
		MOBS1.put(20404, 95);
		MOBS1.put(20433, 100);
		MOBS1.put(20436, 140);
		MOBS1.put(20448, 45);
		MOBS1.put(20456, 20);
		MOBS1.put(20463, 85);
		MOBS1.put(20470, 45);
		MOBS1.put(20471, 85);
		MOBS1.put(20475, 20);
		MOBS1.put(20478, 110);
		MOBS1.put(20487, 90);
		MOBS1.put(20511, 100);
		MOBS1.put(20525, 20);
		MOBS1.put(20528, 100);
		MOBS1.put(20536, 15);
		MOBS1.put(20537, 15);
		MOBS1.put(20538, 15);
		MOBS1.put(20539, 15);
		MOBS1.put(20544, 15);
		MOBS1.put(20550, 300);
		MOBS1.put(20551, 300);
		MOBS1.put(20552, 650);
		MOBS1.put(20553, 335);
		MOBS1.put(20554, 390);
		MOBS1.put(20555, 350);
		MOBS1.put(20557, 390);
		MOBS1.put(20559, 420);
		MOBS1.put(20560, 440);
		MOBS1.put(20562, 485);
		MOBS1.put(20573, 545);
		MOBS1.put(20575, 645);
		MOBS1.put(20630, 350);
		MOBS1.put(20632, 475);
		MOBS1.put(20634, 960);
		MOBS1.put(20636, 495);
		MOBS1.put(20638, 540);
		MOBS1.put(20641, 680);
		MOBS1.put(20643, 660);
		MOBS1.put(20644, 645);
		MOBS1.put(20659, 440);
		MOBS1.put(20661, 575);
		MOBS1.put(20663, 525);
		MOBS1.put(20665, 680);
		MOBS1.put(20667, 730);
		MOBS1.put(20766, 210);
		MOBS1.put(20781, 270);
		MOBS1.put(20783, 140);
		MOBS1.put(20784, 155);
		MOBS1.put(20786, 170);
		MOBS1.put(20788, 325);
		MOBS1.put(20790, 390);
		MOBS1.put(20792, 620);
		MOBS1.put(20794, 635);
		MOBS1.put(20796, 640);
		MOBS1.put(20798, 850);
		MOBS1.put(20800, 740);
		MOBS1.put(20802, 900);
		MOBS1.put(20804, 775);
		MOBS1.put(20806, 805);
		MOBS1.put(20833, 455);
		MOBS1.put(20834, 680);
		MOBS1.put(20836, 785);
		MOBS1.put(20837, 835);
		MOBS1.put(20839, 430);
		MOBS1.put(20841, 460);
		MOBS1.put(20845, 605);
		MOBS1.put(20847, 570);
		MOBS1.put(20849, 585);
		MOBS1.put(20936, 290);
		MOBS1.put(20937, 315);
		MOBS1.put(20939, 385);
		MOBS1.put(20940, 500);
		MOBS1.put(20941, 460);
		MOBS1.put(20943, 345);
		MOBS1.put(20944, 335);
		MOBS1.put(21100, 125);
		MOBS1.put(21101, 155);
		MOBS1.put(21103, 215);
		MOBS1.put(21105, 310);
		MOBS1.put(21107, 600);
		MOBS1.put(21117, 120);
		MOBS1.put(21023, 170);
		MOBS1.put(21024, 175);
		MOBS1.put(21025, 185);
		MOBS1.put(21026, 200);
		MOBS1.put(21034, 195);
		MOBS1.put(21125, 12);
		MOBS1.put(21263, 650);
		MOBS1.put(21520, 880);
		MOBS1.put(21526, 970);
		MOBS1.put(21536, 985);
		MOBS1.put(21602, 555);
		MOBS1.put(21603, 750);
		MOBS1.put(21605, 620);
		MOBS1.put(21606, 875);
		MOBS1.put(21611, 590);
		MOBS1.put(21612, 835);
		MOBS1.put(21617, 615);
		MOBS1.put(21618, 875);
		MOBS1.put(21635, 775);
		MOBS1.put(21638, 165);
		MOBS1.put(21639, 185);
		MOBS1.put(21641, 195);
		MOBS1.put(21644, 170);
	}
	
	private static final Map<Integer, Integer> MOBS2 = new HashMap<>();
	{
		MOBS2.put(20579, 420);
		MOBS2.put(20639, 280);
		MOBS2.put(20646, 145);
		MOBS2.put(20648, 120);
		MOBS2.put(20650, 460);
		MOBS2.put(20651, 260);
		MOBS2.put(20652, 335);
		MOBS2.put(20657, 630);
		MOBS2.put(20658, 570);
		MOBS2.put(20808, 50);
		MOBS2.put(20809, 865);
		MOBS2.put(20832, 700);
		MOBS2.put(20979, 980);
		MOBS2.put(20991, 665);
		MOBS2.put(20994, 590);
		MOBS2.put(21261, 170);
		MOBS2.put(21263, 795);
		MOBS2.put(21508, 100);
		MOBS2.put(21510, 280);
		MOBS2.put(21511, 995);
		MOBS2.put(21512, 995);
		MOBS2.put(21514, 185);
		MOBS2.put(21516, 495);
		MOBS2.put(21517, 495);
		MOBS2.put(21518, 255);
		MOBS2.put(21636, 950);
	}
	
	private static final Map<Integer, Integer> MOBS3 = new HashMap<>();
	{
		MOBS3.put(20655, 110);
		MOBS3.put(20656, 150);
		MOBS3.put(20772, 105);
		MOBS3.put(20810, 50);
		MOBS3.put(20812, 490);
		MOBS3.put(20814, 775);
		MOBS3.put(20816, 875);
		MOBS3.put(20819, 280);
		MOBS3.put(20955, 670);
		MOBS3.put(20978, 555);
		MOBS3.put(21058, 355);
		MOBS3.put(21060, 45);
		MOBS3.put(21075, 110);
		MOBS3.put(21078, 610);
		MOBS3.put(21081, 955);
		MOBS3.put(21264, 920);
	}
	
	private static final Map<Integer, Integer> MOBS4 = new HashMap<>();
	{
		MOBS4.put(20815, 205);
		MOBS4.put(20822, 100);
		MOBS4.put(20824, 665);
		MOBS4.put(20825, 620);
		MOBS4.put(20983, 205);
		MOBS4.put(21314, 145);
		MOBS4.put(21316, 235);
		MOBS4.put(21318, 280);
		MOBS4.put(21320, 355);
		MOBS4.put(21322, 430);
		MOBS4.put(21376, 280);
		MOBS4.put(21378, 375);
		MOBS4.put(21380, 375);
		MOBS4.put(21387, 640);
		MOBS4.put(21393, 935);
		MOBS4.put(21395, 855);
		MOBS4.put(21652, 375);
		MOBS4.put(21655, 640);
		MOBS4.put(21657, 935);
	}
	
	private static final Map<Integer, Integer> MOBS5 = new HashMap<>();
	{
		MOBS5.put(20828, 935);
		MOBS5.put(21061, 530);
		MOBS5.put(21069, 825);
		MOBS5.put(21382, 125);
		MOBS5.put(21384, 400);
		MOBS5.put(21390, 750);
		MOBS5.put(21654, 400);
		MOBS5.put(21656, 750);
	}
	
	private static final Map<Integer, int[]> MOBSspecial = new HashMap<>();
	{
		MOBSspecial.put(20829, new int[]
		{
			115,
			6
		});
		MOBSspecial.put(20859, new int[]
		{
			890,
			8
		});
		MOBSspecial.put(21066, new int[]
		{
			5,
			5
		});
		MOBSspecial.put(21068, new int[]
		{
			565,
			11
		});
		MOBSspecial.put(21071, new int[]
		{
			400,
			12
		});
	}
	
	public Q426_QuestForFishingShot()
	{
		super(426, "Quest for Fishing Shot");
		
		setItemsIds(SWEET_FLUID);
		
		addQuestStart(31562, 31563, 31564, 31565, 31566, 31567, 31568, 31569, 31570, 31571, 31572, 31573, 31574, 31575, 31576, 31577, 31578, 31579, 31696, 31697, 31989, 32007);
		addTalkId(31562, 31563, 31564, 31565, 31566, 31567, 31568, 31569, 31570, 31571, 31572, 31573, 31574, 31575, 31576, 31577, 31578, 31579, 31696, 31697, 31989, 32007);
		
		for (int mob : MOBS1.keySet())
			addMyDying(mob);
		for (int mob : MOBS2.keySet())
			addMyDying(mob);
		for (int mob : MOBS3.keySet())
			addMyDying(mob);
		for (int mob : MOBS4.keySet())
			addMyDying(mob);
		for (int mob : MOBS5.keySet())
			addMyDying(mob);
		for (int mob : MOBSspecial.keySet())
			addMyDying(mob);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("08.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			st = newQuestState(player);
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = "01.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasItem(SWEET_FLUID)) ? "05.htm" : "04.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int npcId = npc.getNpcId();
		int drop = 0;
		int chance = 0;
		
		if (MOBS1.containsKey(npcId))
			chance = MOBS1.get(npcId);
		else if (MOBS2.containsKey(npcId))
		{
			chance = MOBS2.get(npcId);
			drop = 1;
		}
		else if (MOBS3.containsKey(npcId))
		{
			chance = MOBS3.get(npcId);
			drop = 2;
		}
		else if (MOBS4.containsKey(npcId))
		{
			chance = MOBS4.get(npcId);
			drop = 3;
		}
		else if (MOBS5.containsKey(npcId))
		{
			chance = MOBS5.get(npcId);
			drop = 4;
		}
		else if (MOBSspecial.containsKey(npcId))
		{
			chance = MOBSspecial.get(npcId)[0];
			drop = MOBSspecial.get(npcId)[1];
		}
		
		if (Rnd.get(1000) <= chance)
			drop++;
		
		if (drop != 0)
		{
			playSound(st.getPlayer(), SOUND_ITEMGET);
			rewardItems(st.getPlayer(), SWEET_FLUID, drop);
		}
	}
}