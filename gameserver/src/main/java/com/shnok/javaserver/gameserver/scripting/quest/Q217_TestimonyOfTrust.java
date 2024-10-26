package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q217_TestimonyOfTrust extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q217_TestimonyOfTrust";
	
	// Items
	private static final int LETTER_TO_ELF = 2735;
	private static final int LETTER_TO_DARK_ELF = 2736;
	private static final int LETTER_TO_DWARF = 2737;
	private static final int LETTER_TO_ORC = 2738;
	private static final int LETTER_TO_SERESIN = 2739;
	private static final int SCROLL_OF_DARK_ELF_TRUST = 2740;
	private static final int SCROLL_OF_ELF_TRUST = 2741;
	private static final int SCROLL_OF_DWARF_TRUST = 2742;
	private static final int SCROLL_OF_ORC_TRUST = 2743;
	private static final int RECOMMENDATION_OF_HOLLINT = 2744;
	private static final int ORDER_OF_ASTERIOS = 2745;
	private static final int BREATH_OF_WINDS = 2746;
	private static final int SEED_OF_VERDURE = 2747;
	private static final int LETTER_FROM_THIFIELL = 2748;
	private static final int BLOOD_GUARDIAN_BASILIK = 2749;
	private static final int GIANT_APHID = 2750;
	private static final int STAKATO_FLUIDS = 2751;
	private static final int BASILIK_PLASMA = 2752;
	private static final int HONEY_DEW = 2753;
	private static final int STAKATO_ICHOR = 2754;
	private static final int ORDER_OF_CLAYTON = 2755;
	private static final int PARASITE_OF_LOTA = 2756;
	private static final int LETTER_TO_MANAKIA = 2757;
	private static final int LETTER_OF_MANAKIA = 2758;
	private static final int LETTER_TO_NIKOLA = 2759;
	private static final int ORDER_OF_NIKOLA = 2760;
	private static final int HEARTSTONE_OF_PORTA = 2761;
	
	// Rewards
	private static final int MARK_OF_TRUST = 2734;
	
	// NPCs
	private static final int HOLLINT = 30191;
	private static final int ASTERIOS = 30154;
	private static final int THIFIELL = 30358;
	private static final int CLAYTON = 30464;
	private static final int SERESIN = 30657;
	private static final int KAKAI = 30565;
	private static final int MANAKIA = 30515;
	private static final int LOCKIRIN = 30531;
	private static final int NIKOLA = 30621;
	private static final int BIOTIN = 30031;
	
	// Monsters
	private static final int DRYAD = 20013;
	private static final int DRYAD_ELDER = 20019;
	private static final int LIREIN = 20036;
	private static final int LIREIN_ELDER = 20044;
	private static final int ACTEA_OF_VERDANT_WILDS = 27121;
	private static final int LUELL_OF_ZEPHYR_WINDS = 27120;
	private static final int GUARDIAN_BASILIK = 20550;
	private static final int ANT_RECRUIT = 20082;
	private static final int ANT_PATROL = 20084;
	private static final int ANT_GUARD = 20086;
	private static final int ANT_SOLDIER = 20087;
	private static final int ANT_WARRIOR_CAPTAIN = 20088;
	private static final int MARSH_STAKATO = 20157;
	private static final int MARSH_STAKATO_WORKER = 20230;
	private static final int MARSH_STAKATO_SOLDIER = 20232;
	private static final int MARSH_STAKATO_DRONE = 20234;
	private static final int WINDSUS = 20553;
	private static final int PORTA = 20213;
	
	public Q217_TestimonyOfTrust()
	{
		super(217, "Testimony of Trust");
		
		setItemsIds(LETTER_TO_ELF, LETTER_TO_DARK_ELF, LETTER_TO_DWARF, LETTER_TO_ORC, LETTER_TO_SERESIN, SCROLL_OF_DARK_ELF_TRUST, SCROLL_OF_ELF_TRUST, SCROLL_OF_DWARF_TRUST, SCROLL_OF_ORC_TRUST, RECOMMENDATION_OF_HOLLINT, ORDER_OF_ASTERIOS, BREATH_OF_WINDS, SEED_OF_VERDURE, LETTER_FROM_THIFIELL, BLOOD_GUARDIAN_BASILIK, GIANT_APHID, STAKATO_FLUIDS, BASILIK_PLASMA, HONEY_DEW, STAKATO_ICHOR, ORDER_OF_CLAYTON, PARASITE_OF_LOTA, LETTER_TO_MANAKIA, LETTER_OF_MANAKIA, LETTER_TO_NIKOLA, ORDER_OF_NIKOLA, HEARTSTONE_OF_PORTA);
		
		addQuestStart(HOLLINT);
		addTalkId(HOLLINT, ASTERIOS, THIFIELL, CLAYTON, SERESIN, KAKAI, MANAKIA, LOCKIRIN, NIKOLA, BIOTIN);
		
		addMyDying(DRYAD, DRYAD_ELDER, LIREIN, LIREIN_ELDER, ACTEA_OF_VERDANT_WILDS, LUELL_OF_ZEPHYR_WINDS, GUARDIAN_BASILIK, ANT_RECRUIT, ANT_PATROL, ANT_GUARD, ANT_SOLDIER, ANT_WARRIOR_CAPTAIN, MARSH_STAKATO, MARSH_STAKATO_WORKER, MARSH_STAKATO_SOLDIER, MARSH_STAKATO_DRONE, WINDSUS, PORTA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30191-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, LETTER_TO_ELF, 1);
			giveItems(player, LETTER_TO_DARK_ELF, 1);
			
			if (giveDimensionalDiamonds37(player))
				htmltext = "30191-04a.htm";
		}
		else if (event.equalsIgnoreCase("30154-03.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER_TO_ELF, 1);
			giveItems(player, ORDER_OF_ASTERIOS, 1);
		}
		else if (event.equalsIgnoreCase("30358-02.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER_TO_DARK_ELF, 1);
			giveItems(player, LETTER_FROM_THIFIELL, 1);
		}
		else if (event.equalsIgnoreCase("30515-02.htm"))
		{
			st.setCond(14);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER_TO_MANAKIA, 1);
		}
		else if (event.equalsIgnoreCase("30531-02.htm"))
		{
			st.setCond(18);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER_TO_DWARF, 1);
			giveItems(player, LETTER_TO_NIKOLA, 1);
		}
		else if (event.equalsIgnoreCase("30565-02.htm"))
		{
			st.setCond(13);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER_TO_ORC, 1);
			giveItems(player, LETTER_TO_MANAKIA, 1);
		}
		else if (event.equalsIgnoreCase("30621-02.htm"))
		{
			st.setCond(19);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER_TO_NIKOLA, 1);
			giveItems(player, ORDER_OF_NIKOLA, 1);
		}
		else if (event.equalsIgnoreCase("30657-03.htm"))
		{
			if (player.getStatus().getLevel() < 38)
			{
				htmltext = "30657-02.htm";
				if (st.getCond() == 10)
				{
					st.setCond(11);
					playSound(player, SOUND_MIDDLE);
				}
			}
			else
			{
				st.setCond(12);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, LETTER_TO_SERESIN, 1);
				giveItems(player, LETTER_TO_DWARF, 1);
				giveItems(player, LETTER_TO_ORC, 1);
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
				if (player.getClassId().getLevel() != 1)
					htmltext = "30191-01a.htm";
				else if (player.getRace() != ClassRace.HUMAN)
					htmltext = "30191-02.htm";
				else if (player.getStatus().getLevel() < 37)
					htmltext = "30191-01.htm";
				else
					htmltext = "30191-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case HOLLINT:
						if (cond < 9)
							htmltext = "30191-08.htm";
						else if (cond == 9)
						{
							htmltext = "30191-05.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SCROLL_OF_DARK_ELF_TRUST, 1);
							takeItems(player, SCROLL_OF_ELF_TRUST, 1);
							giveItems(player, LETTER_TO_SERESIN, 1);
						}
						else if (cond > 9 && cond < 22)
							htmltext = "30191-09.htm";
						else if (cond == 22)
						{
							htmltext = "30191-06.htm";
							st.setCond(23);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SCROLL_OF_DWARF_TRUST, 1);
							takeItems(player, SCROLL_OF_ORC_TRUST, 1);
							giveItems(player, RECOMMENDATION_OF_HOLLINT, 1);
						}
						else if (cond == 23)
							htmltext = "30191-07.htm";
						break;
					
					case ASTERIOS:
						if (cond == 1)
							htmltext = "30154-01.htm";
						else if (cond == 2)
							htmltext = "30154-04.htm";
						else if (cond == 3)
						{
							htmltext = "30154-05.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BREATH_OF_WINDS, 1);
							takeItems(player, SEED_OF_VERDURE, 1);
							takeItems(player, ORDER_OF_ASTERIOS, 1);
							giveItems(player, SCROLL_OF_ELF_TRUST, 1);
						}
						else if (cond > 3)
							htmltext = "30154-06.htm";
						break;
					
					case THIFIELL:
						if (cond == 4)
							htmltext = "30358-01.htm";
						else if (cond > 4 && cond < 8)
							htmltext = "30358-05.htm";
						else if (cond == 8)
						{
							htmltext = "30358-03.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BASILIK_PLASMA, 1);
							takeItems(player, HONEY_DEW, 1);
							takeItems(player, STAKATO_ICHOR, 1);
							giveItems(player, SCROLL_OF_DARK_ELF_TRUST, 1);
						}
						else if (cond > 8)
							htmltext = "30358-04.htm";
						break;
					
					case CLAYTON:
						if (cond == 5)
						{
							htmltext = "30464-01.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LETTER_FROM_THIFIELL, 1);
							giveItems(player, ORDER_OF_CLAYTON, 1);
						}
						else if (cond == 6)
							htmltext = "30464-02.htm";
						else if (cond > 6)
						{
							htmltext = "30464-03.htm";
							if (cond == 7)
							{
								st.setCond(8);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, ORDER_OF_CLAYTON, 1);
							}
						}
						break;
					
					case SERESIN:
						if (cond == 10 || cond == 11)
							htmltext = "30657-01.htm";
						else if (cond > 11 && cond < 22)
							htmltext = "30657-04.htm";
						else if (cond == 22)
							htmltext = "30657-05.htm";
						break;
					
					case KAKAI:
						if (cond == 12)
							htmltext = "30565-01.htm";
						else if (cond > 12 && cond < 16)
							htmltext = "30565-03.htm";
						else if (cond == 16)
						{
							htmltext = "30565-04.htm";
							st.setCond(17);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LETTER_OF_MANAKIA, 1);
							giveItems(player, SCROLL_OF_ORC_TRUST, 1);
						}
						else if (cond > 16)
							htmltext = "30565-05.htm";
						break;
					
					case MANAKIA:
						if (cond == 13)
							htmltext = "30515-01.htm";
						else if (cond == 14)
							htmltext = "30515-03.htm";
						else if (cond == 15)
						{
							htmltext = "30515-04.htm";
							st.setCond(16);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, PARASITE_OF_LOTA, -1);
							giveItems(player, LETTER_OF_MANAKIA, 1);
						}
						else if (cond > 15)
							htmltext = "30515-05.htm";
						break;
					
					case LOCKIRIN:
						if (cond == 17)
							htmltext = "30531-01.htm";
						else if (cond > 17 && cond < 21)
							htmltext = "30531-03.htm";
						else if (cond == 21)
						{
							htmltext = "30531-04.htm";
							st.setCond(22);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, SCROLL_OF_DWARF_TRUST, 1);
						}
						else if (cond == 22)
							htmltext = "30531-05.htm";
						break;
					
					case NIKOLA:
						if (cond == 18)
							htmltext = "30621-01.htm";
						else if (cond == 19)
							htmltext = "30621-03.htm";
						else if (cond == 20)
						{
							htmltext = "30621-04.htm";
							st.setCond(21);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, HEARTSTONE_OF_PORTA, -1);
							takeItems(player, ORDER_OF_NIKOLA, 1);
						}
						else if (cond > 20)
							htmltext = "30621-05.htm";
						break;
					
					case BIOTIN:
						if (cond == 23)
						{
							htmltext = "30031-01.htm";
							takeItems(player, RECOMMENDATION_OF_HOLLINT, 1);
							giveItems(player, MARK_OF_TRUST, 1);
							rewardExpAndSp(player, 39571, 2500);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
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
			case DRYAD, DRYAD_ELDER:
				if (st.getCond() == 2 && !player.getInventory().hasItem(SEED_OF_VERDURE) && Rnd.get(100) < 33)
				{
					addSpawn(ACTEA_OF_VERDANT_WILDS, npc, true, 200000, true);
					playSound(player, SOUND_BEFORE_BATTLE);
				}
				break;
			
			case LIREIN, LIREIN_ELDER:
				if (st.getCond() == 2 && !player.getInventory().hasItem(BREATH_OF_WINDS) && Rnd.get(100) < 33)
				{
					addSpawn(LUELL_OF_ZEPHYR_WINDS, npc, true, 200000, true);
					playSound(player, SOUND_BEFORE_BATTLE);
				}
				break;
			
			case ACTEA_OF_VERDANT_WILDS:
				if (st.getCond() == 2 && !player.getInventory().hasItem(SEED_OF_VERDURE))
				{
					giveItems(player, SEED_OF_VERDURE, 1);
					if (player.getInventory().hasItem(BREATH_OF_WINDS))
					{
						st.setCond(3);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case LUELL_OF_ZEPHYR_WINDS:
				if (st.getCond() == 2 && !player.getInventory().hasItem(BREATH_OF_WINDS))
				{
					giveItems(player, BREATH_OF_WINDS, 1);
					if (player.getInventory().hasItem(SEED_OF_VERDURE))
					{
						st.setCond(3);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case MARSH_STAKATO, MARSH_STAKATO_WORKER, MARSH_STAKATO_SOLDIER, MARSH_STAKATO_DRONE:
				if (st.getCond() == 6 && !player.getInventory().hasItem(STAKATO_ICHOR) && dropItemsAlways(player, STAKATO_FLUIDS, 1, 10))
				{
					takeItems(player, STAKATO_FLUIDS, -1);
					giveItems(player, STAKATO_ICHOR, 1);
					
					if (player.getInventory().hasItems(BASILIK_PLASMA, HONEY_DEW))
						st.setCond(7);
				}
				break;
			
			case ANT_RECRUIT, ANT_PATROL, ANT_GUARD, ANT_SOLDIER, ANT_WARRIOR_CAPTAIN:
				if (st.getCond() == 6 && !player.getInventory().hasItem(HONEY_DEW) && dropItemsAlways(player, GIANT_APHID, 1, 10))
				{
					takeItems(player, GIANT_APHID, -1);
					giveItems(player, HONEY_DEW, 1);
					
					if (player.getInventory().hasItems(BASILIK_PLASMA, STAKATO_ICHOR))
						st.setCond(7);
				}
				break;
			
			case GUARDIAN_BASILIK:
				if (st.getCond() == 6 && !player.getInventory().hasItem(BASILIK_PLASMA) && dropItemsAlways(player, BLOOD_GUARDIAN_BASILIK, 1, 10))
				{
					takeItems(player, BLOOD_GUARDIAN_BASILIK, -1);
					giveItems(player, BASILIK_PLASMA, 1);
					
					if (player.getInventory().hasItems(HONEY_DEW, STAKATO_ICHOR))
						st.setCond(7);
				}
				break;
			
			case WINDSUS:
				if (st.getCond() == 14 && dropItems(player, PARASITE_OF_LOTA, 1, 10, 500000))
					st.setCond(15);
				break;
			
			case PORTA:
				if (st.getCond() == 19 && dropItemsAlways(player, HEARTSTONE_OF_PORTA, 1, 10))
					st.setCond(20);
				break;
		}
	}
}