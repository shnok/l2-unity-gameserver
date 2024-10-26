package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q222_TestOfTheDuelist extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q222_TestOfTheDuelist";
	
	private static final int KAIEN = 30623;
	
	// Items
	private static final int ORDER_GLUDIO = 2763;
	private static final int ORDER_DION = 2764;
	private static final int ORDER_GIRAN = 2765;
	private static final int ORDER_OREN = 2766;
	private static final int ORDER_ADEN = 2767;
	private static final int PUNCHER_SHARD = 2768;
	private static final int NOBLE_ANT_FEELER = 2769;
	private static final int DRONE_CHITIN = 2770;
	private static final int DEAD_SEEKER_FANG = 2771;
	private static final int OVERLORD_NECKLACE = 2772;
	private static final int FETTERED_SOUL_CHAIN = 2773;
	private static final int CHIEF_AMULET = 2774;
	private static final int ENCHANTED_EYE_MEAT = 2775;
	private static final int TAMRIN_ORC_RING = 2776;
	private static final int TAMRIN_ORC_ARROW = 2777;
	private static final int FINAL_ORDER = 2778;
	private static final int EXCURO_SKIN = 2779;
	private static final int KRATOR_SHARD = 2780;
	private static final int GRANDIS_SKIN = 2781;
	private static final int TIMAK_ORC_BELT = 2782;
	private static final int LAKIN_MACE = 2783;
	
	// Rewards
	private static final int MARK_OF_DUELIST = 2762;
	
	// Monsters
	private static final int PUNCHER = 20085;
	private static final int NOBLE_ANT_LEADER = 20090;
	private static final int MARSH_STAKATO_DRONE = 20234;
	private static final int DEAD_SEEKER = 20202;
	private static final int BREKA_ORC_OVERLORD = 20270;
	private static final int FETTERED_SOUL = 20552;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int ENCHANTED_MONSTEREYE = 20564;
	private static final int TAMLIN_ORC = 20601;
	private static final int TAMLIN_ORC_ARCHER = 20602;
	private static final int EXCURO = 20214;
	private static final int KRATOR = 20217;
	private static final int GRANDIS = 20554;
	private static final int TIMAK_ORC_OVERLORD = 20588;
	private static final int LAKIN = 20604;
	
	public Q222_TestOfTheDuelist()
	{
		super(222, "Test of the Duelist");
		
		setItemsIds(ORDER_GLUDIO, ORDER_DION, ORDER_GIRAN, ORDER_OREN, ORDER_ADEN, FINAL_ORDER, PUNCHER_SHARD, NOBLE_ANT_FEELER, DRONE_CHITIN, DEAD_SEEKER_FANG, OVERLORD_NECKLACE, FETTERED_SOUL_CHAIN, CHIEF_AMULET, ENCHANTED_EYE_MEAT, TAMRIN_ORC_RING, TAMRIN_ORC_ARROW, EXCURO_SKIN, KRATOR_SHARD, GRANDIS_SKIN, TIMAK_ORC_BELT, LAKIN_MACE);
		
		addQuestStart(KAIEN);
		addTalkId(KAIEN);
		
		addMyDying(PUNCHER, NOBLE_ANT_LEADER, MARSH_STAKATO_DRONE, DEAD_SEEKER, BREKA_ORC_OVERLORD, FETTERED_SOUL, LETO_LIZARDMAN_OVERLORD, ENCHANTED_MONSTEREYE, TAMLIN_ORC, TAMLIN_ORC_ARCHER, EXCURO, KRATOR, GRANDIS, TIMAK_ORC_OVERLORD, LAKIN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30623-04.htm"))
		{
			if (player.getRace() == ClassRace.ORC)
				htmltext = "30623-05.htm";
		}
		else if (event.equalsIgnoreCase("30623-07.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.setCond(2);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ORDER_GLUDIO, 1);
			giveItems(player, ORDER_DION, 1);
			giveItems(player, ORDER_GIRAN, 1);
			giveItems(player, ORDER_OREN, 1);
			giveItems(player, ORDER_ADEN, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30623-07a.htm";
		}
		else if (event.equalsIgnoreCase("30623-16.htm"))
		{
			if (st.getCond() == 3)
			{
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
				
				takeItems(player, ORDER_GLUDIO, 1);
				takeItems(player, ORDER_DION, 1);
				takeItems(player, ORDER_GIRAN, 1);
				takeItems(player, ORDER_OREN, 1);
				takeItems(player, ORDER_ADEN, 1);
				
				takeItems(player, PUNCHER_SHARD, -1);
				takeItems(player, NOBLE_ANT_FEELER, -1);
				takeItems(player, DRONE_CHITIN, -1);
				takeItems(player, DEAD_SEEKER_FANG, -1);
				takeItems(player, OVERLORD_NECKLACE, -1);
				takeItems(player, FETTERED_SOUL_CHAIN, -1);
				takeItems(player, CHIEF_AMULET, -1);
				takeItems(player, ENCHANTED_EYE_MEAT, -1);
				takeItems(player, TAMRIN_ORC_RING, -1);
				takeItems(player, TAMRIN_ORC_ARROW, -1);
				
				giveItems(player, FINAL_ORDER, 1);
			}
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
				final int classId = player.getClassId().getId();
				if (classId != 0x01 && classId != 0x2f && classId != 0x13 && classId != 0x20)
					htmltext = "30623-02.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30623-01.htm";
				else
					htmltext = "30623-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				if (cond == 2)
					htmltext = "30623-07a.htm";
				else if (cond == 3)
					htmltext = "30623-13.htm";
				else if (cond == 4)
					htmltext = "30623-17.htm";
				else if (cond == 5)
				{
					htmltext = "30623-18.htm";
					takeItems(player, FINAL_ORDER, 1);
					takeItems(player, EXCURO_SKIN, -1);
					takeItems(player, KRATOR_SHARD, -1);
					takeItems(player, GRANDIS_SKIN, -1);
					takeItems(player, TIMAK_ORC_BELT, -1);
					takeItems(player, LAKIN_MACE, -1);
					giveItems(player, MARK_OF_DUELIST, 1);
					rewardExpAndSp(player, 47015, 20000);
					player.broadcastPacket(new SocialAction(player, 3));
					playSound(player, SOUND_FINISH);
					st.exitQuest(false);
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
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
		
		if (st.getCond() == 2)
		{
			switch (npc.getNpcId())
			{
				case PUNCHER:
					if (dropItemsAlways(player, PUNCHER_SHARD, 1, 10))
						if (player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case NOBLE_ANT_LEADER:
					if (dropItemsAlways(player, NOBLE_ANT_FEELER, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case MARSH_STAKATO_DRONE:
					if (dropItemsAlways(player, DRONE_CHITIN, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case DEAD_SEEKER:
					if (dropItemsAlways(player, DEAD_SEEKER_FANG, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case BREKA_ORC_OVERLORD:
					if (dropItemsAlways(player, OVERLORD_NECKLACE, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case FETTERED_SOUL:
					if (dropItemsAlways(player, FETTERED_SOUL_CHAIN, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case LETO_LIZARDMAN_OVERLORD:
					if (dropItemsAlways(player, CHIEF_AMULET, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case ENCHANTED_MONSTEREYE:
					if (dropItemsAlways(player, ENCHANTED_EYE_MEAT, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case TAMLIN_ORC:
					if (dropItemsAlways(player, TAMRIN_ORC_RING, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_ARROW) >= 10)
							st.setCond(3);
					break;
				
				case TAMLIN_ORC_ARCHER:
					if (dropItemsAlways(player, TAMRIN_ORC_ARROW, 1, 10))
						if (player.getInventory().getItemCount(PUNCHER_SHARD) >= 10 && player.getInventory().getItemCount(NOBLE_ANT_FEELER) >= 10 && player.getInventory().getItemCount(DRONE_CHITIN) >= 10 && player.getInventory().getItemCount(DEAD_SEEKER_FANG) >= 10 && player.getInventory().getItemCount(OVERLORD_NECKLACE) >= 10 && player.getInventory().getItemCount(FETTERED_SOUL_CHAIN) >= 10 && player.getInventory().getItemCount(CHIEF_AMULET) >= 10 && player.getInventory().getItemCount(ENCHANTED_EYE_MEAT) >= 10 && player.getInventory().getItemCount(TAMRIN_ORC_RING) >= 10)
							st.setCond(3);
					break;
			}
		}
		else if (st.getCond() == 4)
		{
			switch (npc.getNpcId())
			{
				case EXCURO:
					if (dropItemsAlways(player, EXCURO_SKIN, 1, 3))
						if (player.getInventory().getItemCount(KRATOR_SHARD) >= 3 && player.getInventory().getItemCount(LAKIN_MACE) >= 3 && player.getInventory().getItemCount(GRANDIS_SKIN) >= 3 && player.getInventory().getItemCount(TIMAK_ORC_BELT) >= 3)
							st.setCond(5);
					break;
				
				case KRATOR:
					if (dropItemsAlways(player, KRATOR_SHARD, 1, 3))
						if (player.getInventory().getItemCount(EXCURO_SKIN) >= 3 && player.getInventory().getItemCount(LAKIN_MACE) >= 3 && player.getInventory().getItemCount(GRANDIS_SKIN) >= 3 && player.getInventory().getItemCount(TIMAK_ORC_BELT) >= 3)
							st.setCond(5);
					break;
				
				case LAKIN:
					if (dropItemsAlways(player, LAKIN_MACE, 1, 3))
						if (player.getInventory().getItemCount(EXCURO_SKIN) >= 3 && player.getInventory().getItemCount(KRATOR_SHARD) >= 3 && player.getInventory().getItemCount(GRANDIS_SKIN) >= 3 && player.getInventory().getItemCount(TIMAK_ORC_BELT) >= 3)
							st.setCond(5);
					break;
				
				case GRANDIS:
					if (dropItemsAlways(player, GRANDIS_SKIN, 1, 3))
						if (player.getInventory().getItemCount(EXCURO_SKIN) >= 3 && player.getInventory().getItemCount(KRATOR_SHARD) >= 3 && player.getInventory().getItemCount(LAKIN_MACE) >= 3 && player.getInventory().getItemCount(TIMAK_ORC_BELT) >= 3)
							st.setCond(5);
					break;
				
				case TIMAK_ORC_OVERLORD:
					if (dropItemsAlways(player, TIMAK_ORC_BELT, 1, 3))
						if (player.getInventory().getItemCount(EXCURO_SKIN) >= 3 && player.getInventory().getItemCount(KRATOR_SHARD) >= 3 && player.getInventory().getItemCount(LAKIN_MACE) >= 3 && player.getInventory().getItemCount(GRANDIS_SKIN) >= 3)
							st.setCond(5);
					break;
			}
		}
	}
}