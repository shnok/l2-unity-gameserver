package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q619_RelicsOfTheOldEmpire extends Quest
{
	private static final String QUEST_NAME = "Q619_RelicsOfTheOldEmpire";
	
	// NPC
	private static final int GHOST_OF_ADVENTURER = 31538;
	
	// Items
	private static final int BROKEN_RELIC_PART = 7254;
	private static final int ENTRANCE_PASS_TO_THE_SEPULCHER = 7075;
	
	// Rewards ; all S grade weapons recipe (60%)
	private static final int[] REWARDS = new int[]
	{
		6881,
		6883,
		6885,
		6887,
		6891,
		6893,
		6895,
		6897,
		6899,
		7580
	};
	
	// Drop chances, Note: when higher then 100%, more items may drop (e.g. 379% -> 79% to drop 4 items, 21% to drop 3)
	private static final Map<Integer, Integer> FOUR_SEPULCHERS_DROPLIST = HashMap.newHashMap(79);
	private static final Map<Integer, Integer> IMPERIAL_TOMB_DROPLIST = HashMap.newHashMap(45);
	
	public Q619_RelicsOfTheOldEmpire()
	{
		super(619, "Relics of the Old Empire");
		
		FOUR_SEPULCHERS_DROPLIST.put(18120, 1280000); // r11_roomboss_strong
		FOUR_SEPULCHERS_DROPLIST.put(18121, 1210000); // r11_roomboss_weak
		FOUR_SEPULCHERS_DROPLIST.put(18122, 930000); // r11_roomboss_teleport
		FOUR_SEPULCHERS_DROPLIST.put(18123, 1280000); // r12_roomboss_strong
		FOUR_SEPULCHERS_DROPLIST.put(18124, 1210000); // r12_roomboss_weak
		FOUR_SEPULCHERS_DROPLIST.put(18125, 930000); // r12_roomboss_teleport
		FOUR_SEPULCHERS_DROPLIST.put(18126, 1280000); // r13_roomboss_strong
		FOUR_SEPULCHERS_DROPLIST.put(18127, 1210000); // r13_roomboss_weak
		FOUR_SEPULCHERS_DROPLIST.put(18128, 930000); // r13_roomboss_teleport
		FOUR_SEPULCHERS_DROPLIST.put(18129, 1280000); // r14_roomboss_strong
		FOUR_SEPULCHERS_DROPLIST.put(18130, 1210000); // r14_roomboss_weak
		FOUR_SEPULCHERS_DROPLIST.put(18131, 930000); // r14_roomboss_teleport
		FOUR_SEPULCHERS_DROPLIST.put(18132, 1300000); // r1_beatle_healer
		FOUR_SEPULCHERS_DROPLIST.put(18133, 1200000); // r1_scorpion_warrior
		FOUR_SEPULCHERS_DROPLIST.put(18134, 900000); // r1_warrior_longatk1_h
		FOUR_SEPULCHERS_DROPLIST.put(18135, 1200000); // r1_warrior_longatk2
		FOUR_SEPULCHERS_DROPLIST.put(18136, 1200000); // r1_warrior_selfbuff
		FOUR_SEPULCHERS_DROPLIST.put(18137, 890000); // r1_wizard_h
		FOUR_SEPULCHERS_DROPLIST.put(18138, 1190000); // r1_wizard_clanbuff
		FOUR_SEPULCHERS_DROPLIST.put(18139, 1170000); // r1_wizard_debuff
		FOUR_SEPULCHERS_DROPLIST.put(18140, 1190000); // r1_wizard_selfbuff
		FOUR_SEPULCHERS_DROPLIST.put(18141, 760000); // r21_scarab_roombosss
		FOUR_SEPULCHERS_DROPLIST.put(18142, 760000); // r22_scarab_roombosss
		FOUR_SEPULCHERS_DROPLIST.put(18143, 760000); // r23_scarab_roombosss
		FOUR_SEPULCHERS_DROPLIST.put(18144, 760000); // r24_scarab_roombosss
		FOUR_SEPULCHERS_DROPLIST.put(18145, 650000); // r2_wizard_clanbuff
		FOUR_SEPULCHERS_DROPLIST.put(18146, 660000); // r2_warrior_longatk2
		FOUR_SEPULCHERS_DROPLIST.put(18147, 620000); // r2_wizard
		FOUR_SEPULCHERS_DROPLIST.put(18148, 720000); // r2_warrior
		FOUR_SEPULCHERS_DROPLIST.put(18149, 630000); // r2_bomb
		FOUR_SEPULCHERS_DROPLIST.put(18166, 920000); // r3_warrior
		FOUR_SEPULCHERS_DROPLIST.put(18167, 900000); // r3_warrior_longatk1_h
		FOUR_SEPULCHERS_DROPLIST.put(18168, 930000); // r3_warrior_longatk2
		FOUR_SEPULCHERS_DROPLIST.put(18169, 900000); // r3_warrior_selfbuff
		FOUR_SEPULCHERS_DROPLIST.put(18170, 900000); // r3_wizard_h
		FOUR_SEPULCHERS_DROPLIST.put(18171, 940000); // r3_wizard_clanbuff
		FOUR_SEPULCHERS_DROPLIST.put(18172, 890000); // r3_wizard_selfbuff
		FOUR_SEPULCHERS_DROPLIST.put(18173, 990000); // r41_roomboss_strong
		FOUR_SEPULCHERS_DROPLIST.put(18174, 1220000); // r41_roomboss_weak
		FOUR_SEPULCHERS_DROPLIST.put(18175, 930000); // r41_roomboss_teleport
		FOUR_SEPULCHERS_DROPLIST.put(18176, 990000); // r42_roomboss_strong
		FOUR_SEPULCHERS_DROPLIST.put(18177, 1220000); // r42_roomboss_weak
		FOUR_SEPULCHERS_DROPLIST.put(18178, 930000); // r42_roomboss_teleport
		FOUR_SEPULCHERS_DROPLIST.put(18179, 990000); // r43_roomboss_strong
		FOUR_SEPULCHERS_DROPLIST.put(18180, 1220000); // r43_roomboss_weak
		FOUR_SEPULCHERS_DROPLIST.put(18181, 930000); // r43_roomboss_teleport
		FOUR_SEPULCHERS_DROPLIST.put(18182, 990000); // r44_roomboss_strong
		FOUR_SEPULCHERS_DROPLIST.put(18183, 1220000); // r44_roomboss_weak
		FOUR_SEPULCHERS_DROPLIST.put(18184, 930000); // r44_roomboss_teleport
		FOUR_SEPULCHERS_DROPLIST.put(18185, 1230000); // r4_healer_srddmagic
		FOUR_SEPULCHERS_DROPLIST.put(18186, 1240000); // r4_hearler_srdebuff
		FOUR_SEPULCHERS_DROPLIST.put(18187, 1200000); // r4_warrior
		FOUR_SEPULCHERS_DROPLIST.put(18188, 900000); // r4_warrior_longatk1_h
		FOUR_SEPULCHERS_DROPLIST.put(18189, 1200000); // r4_warrior_longatk2
		FOUR_SEPULCHERS_DROPLIST.put(18190, 1200000); // r4_warrior_selfbuff
		FOUR_SEPULCHERS_DROPLIST.put(18191, 890000); // r4_wizard_h
		FOUR_SEPULCHERS_DROPLIST.put(18192, 1190000); // r4_wizard_clanbuff
		FOUR_SEPULCHERS_DROPLIST.put(18193, 1170000); // r4_wizard_debuff
		FOUR_SEPULCHERS_DROPLIST.put(18194, 1200000); // r4_wizard_selfbuff
		FOUR_SEPULCHERS_DROPLIST.put(18195, 910000); // r4_bomb
		FOUR_SEPULCHERS_DROPLIST.put(18212, 3790000); // r51_roomboss_clanbuff1
		FOUR_SEPULCHERS_DROPLIST.put(18213, 3790000); // r51_roomboss_clanbuff2
		FOUR_SEPULCHERS_DROPLIST.put(18214, 3790000); // r52_roomboss_clanbuff1
		FOUR_SEPULCHERS_DROPLIST.put(18215, 3790000); // r52_roomboss_clanbuff2
		FOUR_SEPULCHERS_DROPLIST.put(18216, 3790000); // r53_roomboss_clanbuff1
		FOUR_SEPULCHERS_DROPLIST.put(18217, 3790000); // r53_roomboss_clanbuff2
		FOUR_SEPULCHERS_DROPLIST.put(18218, 3790000); // r54_roomboss_clanbuff1
		FOUR_SEPULCHERS_DROPLIST.put(18219, 3790000); // r54_roomboss_clanbuff2
		FOUR_SEPULCHERS_DROPLIST.put(18220, 1240000); // r5_healer1
		FOUR_SEPULCHERS_DROPLIST.put(18221, 1270000); // r5_healer2
		FOUR_SEPULCHERS_DROPLIST.put(18222, 1210000); // r5_warrior
		FOUR_SEPULCHERS_DROPLIST.put(18223, 900000); // r5_warrior_longatk1_h
		FOUR_SEPULCHERS_DROPLIST.put(18224, 1220000); // r5_warrior_longatk2
		FOUR_SEPULCHERS_DROPLIST.put(18225, 1210000); // r5_warrior_sbuff
		FOUR_SEPULCHERS_DROPLIST.put(18226, 890000); // r5_wizard_h
		FOUR_SEPULCHERS_DROPLIST.put(18227, 1530000); // r5_wizard_clanbuff
		FOUR_SEPULCHERS_DROPLIST.put(18228, 1150000); // r5_wizard_debuff
		FOUR_SEPULCHERS_DROPLIST.put(18229, 1190000); // r5_wizard_slefbuff
		FOUR_SEPULCHERS_DROPLIST.put(18230, 1490000); // r5_bomb
		
		IMPERIAL_TOMB_DROPLIST.put(21396, 1510000); // carrion_scarab
		IMPERIAL_TOMB_DROPLIST.put(21397, 500000); // carrion_scarab_a
		IMPERIAL_TOMB_DROPLIST.put(21398, 950000); // soldier_scarab
		IMPERIAL_TOMB_DROPLIST.put(21399, 840000); // soldier_scarab_a
		IMPERIAL_TOMB_DROPLIST.put(21400, 760000); // hexa_beetle
		IMPERIAL_TOMB_DROPLIST.put(21401, 670000); // hexa_beetle_a
		IMPERIAL_TOMB_DROPLIST.put(21402, 690000); // katraxith
		IMPERIAL_TOMB_DROPLIST.put(21403, 800000); // katraxith_a
		IMPERIAL_TOMB_DROPLIST.put(21404, 900000); // tera_beetle
		IMPERIAL_TOMB_DROPLIST.put(21405, 640000); // tera_beetle_a
		IMPERIAL_TOMB_DROPLIST.put(21406, 870000); // imperial_knight
		IMPERIAL_TOMB_DROPLIST.put(21407, 560000); // imperial_knight_a
		IMPERIAL_TOMB_DROPLIST.put(21408, 820000); // imperial_guard
		IMPERIAL_TOMB_DROPLIST.put(21409, 920000); // imperial_guard_a
		IMPERIAL_TOMB_DROPLIST.put(21410, 810000); // guardian_scarab
		IMPERIAL_TOMB_DROPLIST.put(21411, 660000); // guardian_scarab_a
		IMPERIAL_TOMB_DROPLIST.put(21412, 1060000); // ustralith
		IMPERIAL_TOMB_DROPLIST.put(21413, 810000); // ustralith_a
		IMPERIAL_TOMB_DROPLIST.put(21414, 1790000); // imperial_assassin
		IMPERIAL_TOMB_DROPLIST.put(21415, 800000); // imperial_assassin_a
		IMPERIAL_TOMB_DROPLIST.put(21416, 820000); // imperial_warlord
		IMPERIAL_TOMB_DROPLIST.put(21417, 1270000); // imperial_warlord_a
		IMPERIAL_TOMB_DROPLIST.put(21418, 660000); // imperial_highguard
		IMPERIAL_TOMB_DROPLIST.put(21419, 670000); // imperial_highguard_a
		IMPERIAL_TOMB_DROPLIST.put(21420, 820000); // ashuras
		IMPERIAL_TOMB_DROPLIST.put(21421, 770000); // ashuras_a
		IMPERIAL_TOMB_DROPLIST.put(21422, 880000); // imperial_dancer
		IMPERIAL_TOMB_DROPLIST.put(21423, 940000); // imperial_dancer_a
		IMPERIAL_TOMB_DROPLIST.put(21424, 1190000); // ashikenas
		IMPERIAL_TOMB_DROPLIST.put(21425, 1210000); // ashikenas_a
		IMPERIAL_TOMB_DROPLIST.put(21426, 1080000); // abraxian
		IMPERIAL_TOMB_DROPLIST.put(21427, 740000); // abraxian_a
		IMPERIAL_TOMB_DROPLIST.put(21428, 760000); // hasturan
		IMPERIAL_TOMB_DROPLIST.put(21429, 800000); // hasturan_a
		IMPERIAL_TOMB_DROPLIST.put(21430, 1100000); // ahrimanes
		IMPERIAL_TOMB_DROPLIST.put(21431, 940000); // ahrimanes_a
		IMPERIAL_TOMB_DROPLIST.put(21432, 1340000); // chakram_beetle
		IMPERIAL_TOMB_DROPLIST.put(21433, 1340000); // jamadhr_beetle
		IMPERIAL_TOMB_DROPLIST.put(21434, 1900000); // priest_of_blood
		IMPERIAL_TOMB_DROPLIST.put(21435, 1600000); // sacrifice_guide
		IMPERIAL_TOMB_DROPLIST.put(21436, 1660000); // sacrifice_bearer
		IMPERIAL_TOMB_DROPLIST.put(21437, 690000); // sacrifice_scarab
		IMPERIAL_TOMB_DROPLIST.put(21798, 330000); // guard_skeleton_2d
		IMPERIAL_TOMB_DROPLIST.put(21799, 610000); // guard_skeleton_3d
		IMPERIAL_TOMB_DROPLIST.put(21800, 310000); // guard_undead
		
		setItemsIds(BROKEN_RELIC_PART);
		
		addQuestStart(GHOST_OF_ADVENTURER);
		addTalkId(GHOST_OF_ADVENTURER);
		
		addEventIds(IMPERIAL_TOMB_DROPLIST.keySet(), EventHandler.MY_DYING);
		addEventIds(FOUR_SEPULCHERS_DROPLIST.keySet(), EventHandler.MY_DYING);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31538-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31538-09.htm"))
		{
			if (player.getInventory().getItemCount(BROKEN_RELIC_PART) >= 1000)
			{
				htmltext = "31538-09.htm";
				takeItems(player, BROKEN_RELIC_PART, 1000);
				giveItems(player, Rnd.get(REWARDS), 1);
			}
			else
				htmltext = "31538-06.htm";
		}
		else if (event.equalsIgnoreCase("31538-10.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				htmltext = (player.getStatus().getLevel() < 74) ? "31538-02.htm" : "31538-01.htm";
				break;
			
			case STARTED:
				if (player.getInventory().getItemCount(BROKEN_RELIC_PART) >= 1000)
					htmltext = "31538-04.htm";
				else if (player.getInventory().hasItem(ENTRANCE_PASS_TO_THE_SEPULCHER))
					htmltext = "31538-06.htm";
				else
					htmltext = "31538-07.htm";
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
		
		if (IMPERIAL_TOMB_DROPLIST.containsKey(npcId))
		{
			dropItems(st.getPlayer(), BROKEN_RELIC_PART, 1, 0, IMPERIAL_TOMB_DROPLIST.get(npcId));
			
			// only Imperial Tomb monsters drop Entrance Pass to the Sepulcher, always 1:30 (3,33%)
			dropItems(st.getPlayer(), ENTRANCE_PASS_TO_THE_SEPULCHER, 1, 0, 33333);
		}
		else
			dropItems(st.getPlayer(), BROKEN_RELIC_PART, 1, 0, FOUR_SEPULCHERS_DROPLIST.get(npcId));
	}
}