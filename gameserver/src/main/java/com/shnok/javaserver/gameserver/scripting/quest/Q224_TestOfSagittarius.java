package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q224_TestOfSagittarius extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q224_TestOfSagittarius";
	
	// Items
	private static final int BERNARD_INTRODUCTION = 3294;
	private static final int HAMIL_LETTER_1 = 3295;
	private static final int HAMIL_LETTER_2 = 3296;
	private static final int HAMIL_LETTER_3 = 3297;
	private static final int HUNTER_RUNE_1 = 3298;
	private static final int HUNTER_RUNE_2 = 3299;
	private static final int TALISMAN_OF_KADESH = 3300;
	private static final int TALISMAN_OF_SNAKE = 3301;
	private static final int MITHRIL_CLIP = 3302;
	private static final int STAKATO_CHITIN = 3303;
	private static final int REINFORCED_BOWSTRING = 3304;
	private static final int MANASHEN_HORN = 3305;
	private static final int BLOOD_OF_LIZARDMAN = 3306;
	
	private static final int CRESCENT_MOON_BOW = 3028;
	private static final int WOODEN_ARROW = 17;
	
	// Rewards
	private static final int MARK_OF_SAGITTARIUS = 3293;
	
	// NPCs
	private static final int BERNARD = 30702;
	private static final int HAMIL = 30626;
	private static final int SIR_ARON_TANFORD = 30653;
	private static final int VOKIAN = 30514;
	private static final int GAUEN = 30717;
	
	// Monsters
	private static final int ANT = 20079;
	private static final int ANT_CAPTAIN = 20080;
	private static final int ANT_OVERSEER = 20081;
	private static final int ANT_RECRUIT = 20082;
	private static final int ANT_PATROL = 20084;
	private static final int ANT_GUARD = 20086;
	private static final int NOBLE_ANT = 20089;
	private static final int NOBLE_ANT_LEADER = 20090;
	private static final int BREKA_ORC_SHAMAN = 20269;
	private static final int BREKA_ORC_OVERLORD = 20270;
	private static final int MARSH_STAKATO_WORKER = 20230;
	private static final int MARSH_STAKATO_SOLDIER = 20232;
	private static final int MARSH_STAKATO_DRONE = 20234;
	private static final int MARSH_SPIDER = 20233;
	private static final int ROAD_SCAVENGER = 20551;
	private static final int MANASHEN_GARGOYLE = 20563;
	private static final int LETO_LIZARDMAN = 20577;
	private static final int LETO_LIZARDMAN_ARCHER = 20578;
	private static final int LETO_LIZARDMAN_SOLDIER = 20579;
	private static final int LETO_LIZARDMAN_WARRIOR = 20580;
	private static final int LETO_LIZARDMAN_SHAMAN = 20581;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int SERPENT_DEMON_KADESH = 27090;
	
	public Q224_TestOfSagittarius()
	{
		super(224, "Test Of Sagittarius");
		
		setItemsIds(BERNARD_INTRODUCTION, HAMIL_LETTER_1, HAMIL_LETTER_2, HAMIL_LETTER_3, HUNTER_RUNE_1, HUNTER_RUNE_2, TALISMAN_OF_KADESH, TALISMAN_OF_SNAKE, MITHRIL_CLIP, STAKATO_CHITIN, REINFORCED_BOWSTRING, MANASHEN_HORN, BLOOD_OF_LIZARDMAN, CRESCENT_MOON_BOW);
		
		addQuestStart(BERNARD);
		addTalkId(BERNARD, HAMIL, SIR_ARON_TANFORD, VOKIAN, GAUEN);
		
		addMyDying(ANT, ANT_CAPTAIN, ANT_OVERSEER, ANT_RECRUIT, ANT_PATROL, ANT_GUARD, NOBLE_ANT, NOBLE_ANT_LEADER, BREKA_ORC_SHAMAN, BREKA_ORC_OVERLORD, MARSH_STAKATO_WORKER, MARSH_STAKATO_SOLDIER, MARSH_STAKATO_DRONE, MARSH_SPIDER, ROAD_SCAVENGER, MANASHEN_GARGOYLE, LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER, LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR, LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD, SERPENT_DEMON_KADESH);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// BERNARD
		if (event.equalsIgnoreCase("30702-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, BERNARD_INTRODUCTION, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30702-04a.htm";
		}
		// HAMIL
		else if (event.equalsIgnoreCase("30626-03.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BERNARD_INTRODUCTION, 1);
			giveItems(player, HAMIL_LETTER_1, 1);
		}
		else if (event.equalsIgnoreCase("30626-07.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HUNTER_RUNE_1, 10);
			giveItems(player, HAMIL_LETTER_2, 1);
		}
		// SIR_ARON_TANFORD
		else if (event.equalsIgnoreCase("30653-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HAMIL_LETTER_1, 1);
		}
		// VOKIAN
		else if (event.equalsIgnoreCase("30514-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HAMIL_LETTER_2, 1);
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
				if (player.getClassId() != ClassId.ROGUE && player.getClassId() != ClassId.ELVEN_SCOUT && player.getClassId() != ClassId.ASSASSIN)
					htmltext = "30702-02.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30702-01.htm";
				else
					htmltext = "30702-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case BERNARD:
						htmltext = "30702-05.htm";
						break;
					
					case HAMIL:
						if (cond == 1)
							htmltext = "30626-01.htm";
						else if (cond == 2 || cond == 3)
							htmltext = "30626-04.htm";
						else if (cond == 4)
							htmltext = "30626-05.htm";
						else if (cond > 4 && cond < 8)
							htmltext = "30626-08.htm";
						else if (cond == 8)
						{
							htmltext = "30626-09.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, HUNTER_RUNE_2, 10);
							giveItems(player, HAMIL_LETTER_3, 1);
						}
						else if (cond > 8 && cond < 12)
							htmltext = "30626-10.htm";
						else if (cond == 12)
						{
							htmltext = "30626-11.htm";
							st.setCond(13);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 13)
							htmltext = "30626-12.htm";
						else if (cond == 14)
						{
							htmltext = "30626-13.htm";
							takeItems(player, BLOOD_OF_LIZARDMAN, -1);
							takeItems(player, CRESCENT_MOON_BOW, 1);
							takeItems(player, TALISMAN_OF_KADESH, 1);
							giveItems(player, MARK_OF_SAGITTARIUS, 1);
							rewardExpAndSp(player, 54726, 20250);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case SIR_ARON_TANFORD:
						if (cond == 2)
							htmltext = "30653-01.htm";
						else if (cond > 2)
							htmltext = "30653-03.htm";
						break;
					
					case VOKIAN:
						if (cond == 5)
							htmltext = "30514-01.htm";
						else if (cond == 6)
							htmltext = "30514-03.htm";
						else if (cond == 7)
						{
							htmltext = "30514-04.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, TALISMAN_OF_SNAKE, 1);
						}
						else if (cond > 7)
							htmltext = "30514-05.htm";
						break;
					
					case GAUEN:
						if (cond == 9)
						{
							htmltext = "30717-01.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, HAMIL_LETTER_3, 1);
						}
						else if (cond == 10)
							htmltext = "30717-03.htm";
						else if (cond == 11)
						{
							htmltext = "30717-02.htm";
							st.setCond(12);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, MANASHEN_HORN, 1);
							takeItems(player, MITHRIL_CLIP, 1);
							takeItems(player, REINFORCED_BOWSTRING, 1);
							takeItems(player, STAKATO_CHITIN, 1);
							giveItems(player, CRESCENT_MOON_BOW, 1);
							giveItems(player, WOODEN_ARROW, 10);
						}
						else if (cond > 11)
							htmltext = "30717-04.htm";
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
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case ANT, ANT_CAPTAIN, ANT_OVERSEER, ANT_RECRUIT, ANT_PATROL, ANT_GUARD, NOBLE_ANT, NOBLE_ANT_LEADER:
				if (st.getCond() == 3 && dropItems(player, HUNTER_RUNE_1, 1, 10, 500000))
					st.setCond(4);
				break;
			
			case BREKA_ORC_SHAMAN, BREKA_ORC_OVERLORD:
				if (st.getCond() == 6 && dropItems(player, HUNTER_RUNE_2, 1, 10, 500000))
				{
					st.setCond(7);
					giveItems(player, TALISMAN_OF_SNAKE, 1);
				}
				break;
			
			case MARSH_STAKATO_WORKER, MARSH_STAKATO_SOLDIER, MARSH_STAKATO_DRONE:
				if (st.getCond() == 10 && dropItems(player, STAKATO_CHITIN, 1, 1, 100000) && player.getInventory().hasItems(MANASHEN_HORN, MITHRIL_CLIP, REINFORCED_BOWSTRING))
					st.setCond(11);
				break;
			
			case MARSH_SPIDER:
				if (st.getCond() == 10 && dropItems(player, REINFORCED_BOWSTRING, 1, 1, 100000) && player.getInventory().hasItems(MANASHEN_HORN, MITHRIL_CLIP, STAKATO_CHITIN))
					st.setCond(11);
				break;
			
			case ROAD_SCAVENGER:
				if (st.getCond() == 10 && dropItems(player, MITHRIL_CLIP, 1, 1, 100000) && player.getInventory().hasItems(MANASHEN_HORN, REINFORCED_BOWSTRING, STAKATO_CHITIN))
					st.setCond(11);
				break;
			
			case MANASHEN_GARGOYLE:
				if (st.getCond() == 10 && dropItems(player, MANASHEN_HORN, 1, 1, 100000) && player.getInventory().hasItems(REINFORCED_BOWSTRING, MITHRIL_CLIP, STAKATO_CHITIN))
					st.setCond(11);
				break;
			
			case LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER, LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR, LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD:
				if (st.getCond() == 13)
				{
					if (((player.getInventory().getItemCount(BLOOD_OF_LIZARDMAN) - 120) * 5) > Rnd.get(100))
					{
						playSound(player, SOUND_BEFORE_BATTLE);
						takeItems(player, BLOOD_OF_LIZARDMAN, -1);
						addSpawn(SERPENT_DEMON_KADESH, player, false, 300000, true);
					}
					else
						dropItemsAlways(player, BLOOD_OF_LIZARDMAN, 1, 0);
				}
				break;
			
			case SERPENT_DEMON_KADESH:
				if (st.getCond() == 13)
				{
					if (player.getInventory().getItemIdFrom(Paperdoll.RHAND) == CRESCENT_MOON_BOW)
					{
						st.setCond(14);
						playSound(player, SOUND_MIDDLE);
						giveItems(player, TALISMAN_OF_KADESH, 1);
					}
					else
						addSpawn(SERPENT_DEMON_KADESH, player, false, 300000, true);
				}
				break;
		}
	}
}