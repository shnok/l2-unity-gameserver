package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q232_TestOfTheLord extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q232_TestOfTheLord";
	
	// NPCs
	private static final int SOMAK = 30510;
	private static final int MANAKIA = 30515;
	private static final int JAKAL = 30558;
	private static final int SUMARI = 30564;
	private static final int KAKAI = 30565;
	private static final int VARKEES = 30566;
	private static final int TANTUS = 30567;
	private static final int HATOS = 30568;
	private static final int TAKUNA = 30641;
	private static final int CHIANTA = 30642;
	private static final int FIRST_ORC = 30643;
	private static final int ANCESTOR_MARTANKUS = 30649;
	
	// Items
	private static final int ORDEAL_NECKLACE = 3391;
	private static final int VARKEES_CHARM = 3392;
	private static final int TANTUS_CHARM = 3393;
	private static final int HATOS_CHARM = 3394;
	private static final int TAKUNA_CHARM = 3395;
	private static final int CHIANTA_CHARM = 3396;
	private static final int MANAKIAS_ORDERS = 3397;
	private static final int BREKA_ORC_FANG = 3398;
	private static final int MANAKIAS_AMULET = 3399;
	private static final int HUGE_ORC_FANG = 3400;
	private static final int SUMARIS_LETTER = 3401;
	private static final int URUTU_BLADE = 3402;
	private static final int TIMAK_ORC_SKULL = 3403;
	private static final int SWORD_INTO_SKULL = 3404;
	private static final int NERUGA_AXE_BLADE = 3405;
	private static final int AXE_OF_CEREMONY = 3406;
	private static final int MARSH_SPIDER_FEELER = 3407;
	private static final int MARSH_SPIDER_FEET = 3408;
	private static final int HANDIWORK_SPIDER_BROOCH = 3409;
	private static final int MONSTEREYE_CORNEA = 3410;
	private static final int MONSTEREYE_WOODCARVING = 3411;
	private static final int BEAR_FANG_NECKLACE = 3412;
	private static final int MARTANKUS_CHARM = 3413;
	private static final int RAGNA_ORC_HEAD = 3414;
	private static final int RAGNA_CHIEF_NOTICE = 3415;
	private static final int BONE_ARROW = 1341;
	private static final int IMMORTAL_FLAME = 3416;
	
	// Rewards
	private static final int MARK_LORD = 3390;
	
	private Npc _firstOrc; // Used to avoid to spawn multiple instances.
	
	public Q232_TestOfTheLord()
	{
		super(232, "Test of the Lord");
		
		setItemsIds(VARKEES_CHARM, TANTUS_CHARM, HATOS_CHARM, TAKUNA_CHARM, CHIANTA_CHARM, MANAKIAS_ORDERS, BREKA_ORC_FANG, MANAKIAS_AMULET, HUGE_ORC_FANG, SUMARIS_LETTER, URUTU_BLADE, TIMAK_ORC_SKULL, SWORD_INTO_SKULL, NERUGA_AXE_BLADE, AXE_OF_CEREMONY, MARSH_SPIDER_FEELER, MARSH_SPIDER_FEET, HANDIWORK_SPIDER_BROOCH, MONSTEREYE_CORNEA, MONSTEREYE_WOODCARVING, BEAR_FANG_NECKLACE, MARTANKUS_CHARM, RAGNA_ORC_HEAD, RAGNA_CHIEF_NOTICE, IMMORTAL_FLAME);
		
		addQuestStart(KAKAI);
		addTalkId(KAKAI, CHIANTA, HATOS, SOMAK, SUMARI, TAKUNA, TANTUS, JAKAL, VARKEES, MANAKIA, ANCESTOR_MARTANKUS, FIRST_ORC);
		
		addMyDying(20233, 20269, 20270, 20564, 20583, 20584, 20585, 20586, 20587, 20588, 20778, 20779);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30565-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ORDEAL_NECKLACE, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30565-05b.htm";
		}
		else if (event.equalsIgnoreCase("30565-08.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SWORD_INTO_SKULL, 1);
			takeItems(player, AXE_OF_CEREMONY, 1);
			takeItems(player, MONSTEREYE_WOODCARVING, 1);
			takeItems(player, HANDIWORK_SPIDER_BROOCH, 1);
			takeItems(player, ORDEAL_NECKLACE, 1);
			takeItems(player, HUGE_ORC_FANG, 1);
			giveItems(player, BEAR_FANG_NECKLACE, 1);
		}
		else if (event.equalsIgnoreCase("30566-02.htm"))
		{
			giveItems(player, VARKEES_CHARM, 1);
			playSound(player, SOUND_ITEMGET);
		}
		else if (event.equalsIgnoreCase("30567-02.htm"))
		{
			giveItems(player, TANTUS_CHARM, 1);
			playSound(player, SOUND_ITEMGET);
		}
		else if (event.equalsIgnoreCase("30558-02.htm"))
		{
			takeItems(player, 57, 1000);
			giveItems(player, NERUGA_AXE_BLADE, 1);
			playSound(player, SOUND_ITEMGET);
		}
		else if (event.equalsIgnoreCase("30568-02.htm"))
		{
			giveItems(player, HATOS_CHARM, 1);
			playSound(player, SOUND_ITEMGET);
		}
		else if (event.equalsIgnoreCase("30641-02.htm"))
		{
			giveItems(player, TAKUNA_CHARM, 1);
			playSound(player, SOUND_ITEMGET);
		}
		else if (event.equalsIgnoreCase("30642-02.htm"))
		{
			giveItems(player, CHIANTA_CHARM, 1);
			playSound(player, SOUND_ITEMGET);
		}
		else if (event.equalsIgnoreCase("30643-02.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			startQuestTimer("f_orc_despawn", null, null, 10000);
		}
		else if (event.equalsIgnoreCase("30649-04.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BEAR_FANG_NECKLACE, 1);
			giveItems(player, MARTANKUS_CHARM, 1);
		}
		else if (event.equalsIgnoreCase("30649-07.htm"))
		{
			if (_firstOrc == null)
				_firstOrc = addSpawn(FIRST_ORC, 21036, -107690, -3038, 200000, false, 0, true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("f_orc_despawn"))
		{
			if (_firstOrc != null)
			{
				_firstOrc.deleteMe();
				_firstOrc = null;
			}
		}
		
		return null;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getRace() != ClassRace.ORC)
					htmltext = "30565-01.htm";
				else if (player.getClassId() != ClassId.ORC_SHAMAN)
					htmltext = "30565-02.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30565-03.htm";
				else
					htmltext = "30565-04.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case VARKEES:
						if (player.getInventory().hasItem(HUGE_ORC_FANG))
							htmltext = "30566-05.htm";
						else if (player.getInventory().hasItem(VARKEES_CHARM))
						{
							if (player.getInventory().hasItem(MANAKIAS_AMULET))
							{
								htmltext = "30566-04.htm";
								takeItems(player, VARKEES_CHARM, -1);
								takeItems(player, MANAKIAS_AMULET, -1);
								giveItems(player, HUGE_ORC_FANG, 1);
								
								if (player.getInventory().hasItems(SWORD_INTO_SKULL, AXE_OF_CEREMONY, MONSTEREYE_WOODCARVING, HANDIWORK_SPIDER_BROOCH, ORDEAL_NECKLACE))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30566-03.htm";
						}
						else
							htmltext = "30566-01.htm";
						break;
					
					case MANAKIA:
						if (player.getInventory().hasItem(HUGE_ORC_FANG))
							htmltext = "30515-05.htm";
						else if (player.getInventory().hasItem(MANAKIAS_AMULET))
							htmltext = "30515-04.htm";
						else if (player.getInventory().hasItem(MANAKIAS_ORDERS))
						{
							if (player.getInventory().getItemCount(BREKA_ORC_FANG) >= 20)
							{
								htmltext = "30515-03.htm";
								takeItems(player, MANAKIAS_ORDERS, -1);
								takeItems(player, BREKA_ORC_FANG, -1);
								giveItems(player, MANAKIAS_AMULET, 1);
								playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30515-02.htm";
						}
						else
						{
							htmltext = "30515-01.htm";
							giveItems(player, MANAKIAS_ORDERS, 1);
							playSound(player, SOUND_ITEMGET);
						}
						break;
					
					case TANTUS:
						if (player.getInventory().hasItem(AXE_OF_CEREMONY))
							htmltext = "30567-05.htm";
						else if (player.getInventory().hasItem(TANTUS_CHARM))
						{
							if (player.getInventory().getItemCount(BONE_ARROW) >= 1000)
							{
								htmltext = "30567-04.htm";
								takeItems(player, BONE_ARROW, 1000);
								takeItems(player, NERUGA_AXE_BLADE, 1);
								takeItems(player, TANTUS_CHARM, 1);
								giveItems(player, AXE_OF_CEREMONY, 1);
								
								if (player.getInventory().hasItems(SWORD_INTO_SKULL, MONSTEREYE_WOODCARVING, HANDIWORK_SPIDER_BROOCH, ORDEAL_NECKLACE, HUGE_ORC_FANG))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30567-03.htm";
						}
						else
							htmltext = "30567-01.htm";
						break;
					
					case JAKAL:
						if (player.getInventory().hasItem(AXE_OF_CEREMONY))
							htmltext = "30558-05.htm";
						else if (player.getInventory().hasItem(NERUGA_AXE_BLADE))
							htmltext = "30558-04.htm";
						else if (player.getInventory().hasItem(TANTUS_CHARM))
						{
							if (player.getAdena() >= 1000)
								htmltext = "30558-01.htm";
							else
								htmltext = "30558-03.htm";
						}
						break;
					
					case HATOS:
						if (player.getInventory().hasItem(SWORD_INTO_SKULL))
							htmltext = "30568-05.htm";
						else if (player.getInventory().hasItem(HATOS_CHARM))
						{
							if (player.getInventory().hasItem(URUTU_BLADE) && player.getInventory().getItemCount(TIMAK_ORC_SKULL) >= 10)
							{
								htmltext = "30568-04.htm";
								takeItems(player, HATOS_CHARM, 1);
								takeItems(player, URUTU_BLADE, 1);
								takeItems(player, TIMAK_ORC_SKULL, -1);
								giveItems(player, SWORD_INTO_SKULL, 1);
								
								if (player.getInventory().hasItems(AXE_OF_CEREMONY, MONSTEREYE_WOODCARVING, HANDIWORK_SPIDER_BROOCH, ORDEAL_NECKLACE, HUGE_ORC_FANG))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30568-03.htm";
						}
						else
							htmltext = "30568-01.htm";
						break;
					
					case SUMARI:
						if (player.getInventory().hasItem(URUTU_BLADE))
							htmltext = "30564-03.htm";
						else if (player.getInventory().hasItem(SUMARIS_LETTER))
							htmltext = "30564-02.htm";
						else if (player.getInventory().hasItem(HATOS_CHARM))
						{
							htmltext = "30564-01.htm";
							giveItems(player, SUMARIS_LETTER, 1);
							playSound(player, SOUND_ITEMGET);
						}
						break;
					
					case SOMAK:
						if (player.getInventory().hasItem(SWORD_INTO_SKULL))
							htmltext = "30510-03.htm";
						else if (player.getInventory().hasItem(URUTU_BLADE))
							htmltext = "30510-02.htm";
						else if (player.getInventory().hasItem(SUMARIS_LETTER))
						{
							htmltext = "30510-01.htm";
							takeItems(player, SUMARIS_LETTER, 1);
							giveItems(player, URUTU_BLADE, 1);
							playSound(player, SOUND_ITEMGET);
						}
						break;
					
					case TAKUNA:
						if (player.getInventory().hasItem(HANDIWORK_SPIDER_BROOCH))
							htmltext = "30641-05.htm";
						else if (player.getInventory().hasItem(TAKUNA_CHARM))
						{
							if (player.getInventory().getItemCount(MARSH_SPIDER_FEELER) >= 10 && player.getInventory().getItemCount(MARSH_SPIDER_FEET) >= 10)
							{
								htmltext = "30641-04.htm";
								takeItems(player, MARSH_SPIDER_FEELER, -1);
								takeItems(player, MARSH_SPIDER_FEET, -1);
								takeItems(player, TAKUNA_CHARM, 1);
								giveItems(player, HANDIWORK_SPIDER_BROOCH, 1);
								
								if (player.getInventory().hasItems(SWORD_INTO_SKULL, AXE_OF_CEREMONY, MONSTEREYE_WOODCARVING, ORDEAL_NECKLACE, HUGE_ORC_FANG))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30641-03.htm";
						}
						else
							htmltext = "30641-01.htm";
						break;
					
					case CHIANTA:
						if (player.getInventory().hasItem(MONSTEREYE_WOODCARVING))
							htmltext = "30642-05.htm";
						else if (player.getInventory().hasItem(CHIANTA_CHARM))
						{
							if (player.getInventory().getItemCount(MONSTEREYE_CORNEA) >= 20)
							{
								htmltext = "30642-04.htm";
								takeItems(player, MONSTEREYE_CORNEA, -1);
								takeItems(player, CHIANTA_CHARM, 1);
								giveItems(player, MONSTEREYE_WOODCARVING, 1);
								
								if (player.getInventory().hasItems(SWORD_INTO_SKULL, AXE_OF_CEREMONY, HANDIWORK_SPIDER_BROOCH, ORDEAL_NECKLACE, HUGE_ORC_FANG))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30642-03.htm";
						}
						else
							htmltext = "30642-01.htm";
						break;
					
					case KAKAI:
						if (cond == 1)
							htmltext = "30565-06.htm";
						else if (cond == 2)
							htmltext = "30565-07.htm";
						else if (cond == 3)
							htmltext = "30565-09.htm";
						else if (cond > 3 && cond < 7)
							htmltext = "30565-10.htm";
						else if (cond == 7)
						{
							htmltext = "30565-11.htm";
							
							takeItems(player, IMMORTAL_FLAME, 1);
							giveItems(player, MARK_LORD, 1);
							rewardExpAndSp(player, 92955, 16250);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ANCESTOR_MARTANKUS:
						if (cond == 3)
							htmltext = "30649-01.htm";
						else if (cond == 4)
							htmltext = "30649-05.htm";
						else if (cond == 5)
						{
							htmltext = "30649-06.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							
							takeItems(player, MARTANKUS_CHARM, 1);
							takeItems(player, RAGNA_ORC_HEAD, 1);
							takeItems(player, RAGNA_CHIEF_NOTICE, 1);
							giveItems(player, IMMORTAL_FLAME, 1);
						}
						else if (cond == 6)
							htmltext = "30649-07.htm";
						else if (cond == 7)
							htmltext = "30649-08.htm";
						break;
					
					case FIRST_ORC:
						if (cond == 6)
							htmltext = "30643-01.htm";
						else if (cond == 7)
							htmltext = "30643-03.htm";
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
			case 20564:
				if (player.getInventory().hasItem(CHIANTA_CHARM))
					dropItemsAlways(player, MONSTEREYE_CORNEA, 1, 20);
				break;
			
			case 20583, 20584, 20585:
				if (player.getInventory().hasItem(HATOS_CHARM))
					dropItems(player, TIMAK_ORC_SKULL, 1, 10, 710000);
				break;
			
			case 20586:
				if (player.getInventory().hasItem(HATOS_CHARM))
					dropItems(player, TIMAK_ORC_SKULL, 1, 10, 810000);
				break;
			
			case 20587, 20588:
				if (player.getInventory().hasItem(HATOS_CHARM))
					dropItemsAlways(player, TIMAK_ORC_SKULL, 1, 10);
				break;
			
			case 20233:
				if (player.getInventory().hasItem(TAKUNA_CHARM))
					dropItemsAlways(player, (player.getInventory().getItemCount(MARSH_SPIDER_FEELER) >= 10) ? MARSH_SPIDER_FEET : MARSH_SPIDER_FEELER, 1, 10);
				break;
			
			case 20269:
				if (player.getInventory().hasItem(MANAKIAS_ORDERS))
					dropItems(player, BREKA_ORC_FANG, 1, 20, 410000);
				break;
			
			case 20270:
				if (player.getInventory().hasItem(MANAKIAS_ORDERS))
					dropItems(player, BREKA_ORC_FANG, 1, 20, 510000);
				break;
			
			case 20778, 20779:
				if (player.getInventory().hasItem(MARTANKUS_CHARM))
				{
					if (!player.getInventory().hasItem(RAGNA_CHIEF_NOTICE))
					{
						playSound(player, SOUND_MIDDLE);
						giveItems(player, RAGNA_CHIEF_NOTICE, 1);
					}
					else if (!player.getInventory().hasItem(RAGNA_ORC_HEAD))
					{
						st.setCond(5);
						playSound(player, SOUND_MIDDLE);
						giveItems(player, RAGNA_ORC_HEAD, 1);
					}
				}
				break;
		}
	}
}