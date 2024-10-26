package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q221_TestimonyOfProsperity extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q221_TestimonyOfProsperity";
	
	// Items
	private static final int ADENA = 57;
	private static final int ANIMAL_SKIN = 1867;
	private static final int RECIPE_TITAN_KEY = 3023;
	private static final int KEY_OF_TITAN = 3030;
	
	private static final int RING_OF_TESTIMONY_1 = 3239;
	private static final int RING_OF_TESTIMONY_2 = 3240;
	private static final int OLD_ACCOUNT_BOOK = 3241;
	private static final int BLESSED_SEED = 3242;
	private static final int EMILY_RECIPE = 3243;
	private static final int LILITH_ELVEN_WAFER = 3244;
	private static final int MAPHR_TABLET_FRAGMENT = 3245;
	private static final int COLLECTION_LICENSE = 3246;
	private static final int LOCKIRIN_NOTICE_1 = 3247;
	private static final int LOCKIRIN_NOTICE_2 = 3248;
	private static final int LOCKIRIN_NOTICE_3 = 3249;
	private static final int LOCKIRIN_NOTICE_4 = 3250;
	private static final int LOCKIRIN_NOTICE_5 = 3251;
	private static final int CONTRIBUTION_OF_SHARI = 3252;
	private static final int CONTRIBUTION_OF_MION = 3253;
	private static final int CONTRIBUTION_OF_MARYSE = 3254;
	private static final int MARYSE_REQUEST = 3255;
	private static final int CONTRIBUTION_OF_TOMA = 3256;
	private static final int RECEIPT_OF_BOLTER = 3257;
	private static final int RECEIPT_OF_CONTRIBUTION_1 = 3258;
	private static final int RECEIPT_OF_CONTRIBUTION_2 = 3259;
	private static final int RECEIPT_OF_CONTRIBUTION_3 = 3260;
	private static final int RECEIPT_OF_CONTRIBUTION_4 = 3261;
	private static final int RECEIPT_OF_CONTRIBUTION_5 = 3262;
	private static final int PROCURATION_OF_TOROCCO = 3263;
	private static final int BRIGHT_LIST = 3264;
	private static final int MANDRAGORA_PETAL = 3265;
	private static final int CRIMSON_MOSS = 3266;
	private static final int MANDRAGORA_BOUQUET = 3267;
	private static final int PARMAN_INSTRUCTIONS = 3268;
	private static final int PARMAN_LETTER = 3269;
	private static final int CLAY_DOUGH = 3270;
	private static final int PATTERN_OF_KEYHOLE = 3271;
	private static final int NIKOLAS_LIST = 3272;
	private static final int STAKATO_SHELL = 3273;
	private static final int TOAD_LORD_SAC = 3274;
	private static final int SPIDER_THORN = 3275;
	private static final int CRYSTAL_BROOCH = 3428;
	
	// Rewards
	private static final int MARK_OF_PROSPERITY = 3238;
	
	// NPCs
	private static final int WILFORD = 30005;
	private static final int PARMAN = 30104;
	private static final int LILITH = 30368;
	private static final int BRIGHT = 30466;
	private static final int SHARI = 30517;
	private static final int MION = 30519;
	private static final int LOCKIRIN = 30531;
	private static final int SPIRON = 30532;
	private static final int BALANKI = 30533;
	private static final int KEEF = 30534;
	private static final int FILAUR = 30535;
	private static final int ARIN = 30536;
	private static final int MARYSE_REDBONNET = 30553;
	private static final int BOLTER = 30554;
	private static final int TOROCCO = 30555;
	private static final int TOMA = 30556;
	private static final int PIOTUR = 30597;
	private static final int EMILY = 30620;
	private static final int NIKOLA = 30621;
	private static final int BOX_OF_TITAN = 30622;
	
	// Monsters
	private static final int MANDRAGORA_SPROUT_1 = 20223;
	private static final int MANDRAGORA_SPROUT_2 = 20154;
	private static final int MANDRAGORA_SAPLING = 20155;
	private static final int MANDRAGORA_BLOSSOM = 20156;
	private static final int MARSH_STAKATO = 20157;
	private static final int GIANT_CRIMSON_ANT = 20228;
	private static final int MARSH_STAKATO_WORKER = 20230;
	private static final int TOAD_LORD = 20231;
	private static final int MARSH_STAKATO_SOLDIER = 20232;
	private static final int MARSH_SPIDER = 20233;
	private static final int MARSH_STAKATO_DRONE = 20234;
	
	public Q221_TestimonyOfProsperity()
	{
		super(221, "Testimony Of Prosperity");
		
		setItemsIds(RING_OF_TESTIMONY_1, RING_OF_TESTIMONY_2, OLD_ACCOUNT_BOOK, BLESSED_SEED, EMILY_RECIPE, LILITH_ELVEN_WAFER, MAPHR_TABLET_FRAGMENT, COLLECTION_LICENSE, LOCKIRIN_NOTICE_1, LOCKIRIN_NOTICE_2, LOCKIRIN_NOTICE_3, LOCKIRIN_NOTICE_4, LOCKIRIN_NOTICE_5, CONTRIBUTION_OF_SHARI, CONTRIBUTION_OF_MION, CONTRIBUTION_OF_MARYSE, MARYSE_REQUEST, CONTRIBUTION_OF_TOMA, RECEIPT_OF_BOLTER, RECEIPT_OF_CONTRIBUTION_1, RECEIPT_OF_CONTRIBUTION_2, RECEIPT_OF_CONTRIBUTION_3, RECEIPT_OF_CONTRIBUTION_4, RECEIPT_OF_CONTRIBUTION_5, PROCURATION_OF_TOROCCO, BRIGHT_LIST, MANDRAGORA_PETAL, CRIMSON_MOSS, MANDRAGORA_BOUQUET, PARMAN_INSTRUCTIONS, PARMAN_LETTER, CLAY_DOUGH, PATTERN_OF_KEYHOLE, NIKOLAS_LIST, STAKATO_SHELL, TOAD_LORD_SAC, SPIDER_THORN, CRYSTAL_BROOCH);
		
		addQuestStart(PARMAN);
		addTalkId(WILFORD, PARMAN, LILITH, BRIGHT, SHARI, MION, LOCKIRIN, SPIRON, BALANKI, KEEF, FILAUR, ARIN, MARYSE_REDBONNET, BOLTER, TOROCCO, TOMA, PIOTUR, EMILY, NIKOLA, BOX_OF_TITAN);
		
		addMyDying(MANDRAGORA_SPROUT_1, MANDRAGORA_SAPLING, MANDRAGORA_BLOSSOM, MARSH_STAKATO, MANDRAGORA_SPROUT_2, GIANT_CRIMSON_ANT, MARSH_STAKATO_WORKER, TOAD_LORD, MARSH_STAKATO_SOLDIER, MARSH_SPIDER, MARSH_STAKATO_DRONE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// PARMAN
		if (event.equalsIgnoreCase("30104-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, RING_OF_TESTIMONY_1, 1);
			
			if (giveDimensionalDiamonds37(player))
				htmltext = "30104-04e.htm";
		}
		else if (event.equalsIgnoreCase("30104-07.htm"))
		{
			takeItems(player, BLESSED_SEED, 1);
			takeItems(player, EMILY_RECIPE, 1);
			takeItems(player, LILITH_ELVEN_WAFER, 1);
			takeItems(player, OLD_ACCOUNT_BOOK, 1);
			takeItems(player, RING_OF_TESTIMONY_1, 1);
			playSound(player, SOUND_MIDDLE);
			
			if (player.getStatus().getLevel() < 38)
			{
				st.setCond(3);
				giveItems(player, PARMAN_INSTRUCTIONS, 1);
			}
			else
			{
				htmltext = "30104-08.htm";
				st.setCond(4);
				giveItems(player, PARMAN_LETTER, 1);
				giveItems(player, RING_OF_TESTIMONY_2, 1);
			}
		}
		// LOCKIRIN
		else if (event.equalsIgnoreCase("30531-02.htm") && player.getInventory().hasItem(COLLECTION_LICENSE))
			htmltext = "30531-04.htm";
		else if (event.equalsIgnoreCase("30531-03.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, COLLECTION_LICENSE, 1);
			giveItems(player, LOCKIRIN_NOTICE_1, 1);
			giveItems(player, LOCKIRIN_NOTICE_2, 1);
			giveItems(player, LOCKIRIN_NOTICE_3, 1);
			giveItems(player, LOCKIRIN_NOTICE_4, 1);
			giveItems(player, LOCKIRIN_NOTICE_5, 1);
		}
		// KEEF
		else if (event.equalsIgnoreCase("30534-03a.htm") && player.getInventory().getItemCount(ADENA) >= 5000)
		{
			htmltext = "30534-03b.htm";
			playSound(player, SOUND_ITEMGET);
			takeItems(player, ADENA, 5000);
			takeItems(player, PROCURATION_OF_TOROCCO, 1);
			giveItems(player, RECEIPT_OF_CONTRIBUTION_3, 1);
		}
		// WILFORD
		else if (event.equalsIgnoreCase("30005-04.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, CRYSTAL_BROOCH, 1);
		}
		// BRIGHT
		else if (event.equalsIgnoreCase("30466-03.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, BRIGHT_LIST, 1);
		}
		// TOROCCO
		else if (event.equalsIgnoreCase("30555-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, PROCURATION_OF_TOROCCO, 1);
		}
		// LILITH
		else if (event.equalsIgnoreCase("30368-03.htm"))
		{
			takeItems(player, CRYSTAL_BROOCH, 1);
			giveItems(player, LILITH_ELVEN_WAFER, 1);
			
			if (player.getInventory().hasItems(BLESSED_SEED, OLD_ACCOUNT_BOOK, EMILY_RECIPE))
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
			}
			else
				playSound(player, SOUND_ITEMGET);
		}
		// PIOTUR
		else if (event.equalsIgnoreCase("30597-02.htm"))
		{
			giveItems(player, BLESSED_SEED, 1);
			
			if (player.getInventory().hasItems(OLD_ACCOUNT_BOOK, EMILY_RECIPE, LILITH_ELVEN_WAFER))
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
			}
			else
				playSound(player, SOUND_ITEMGET);
		}
		// EMILY
		else if (event.equalsIgnoreCase("30620-03.htm"))
		{
			takeItems(player, MANDRAGORA_BOUQUET, 1);
			giveItems(player, EMILY_RECIPE, 1);
			
			if (player.getInventory().hasItems(BLESSED_SEED, OLD_ACCOUNT_BOOK, LILITH_ELVEN_WAFER))
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
			}
			else
				playSound(player, SOUND_ITEMGET);
		}
		// NIKOLA
		else if (event.equalsIgnoreCase("30621-04.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, CLAY_DOUGH, 1);
		}
		// BOX OF TITAN
		else if (event.equalsIgnoreCase("30622-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CLAY_DOUGH, 1);
			giveItems(player, PATTERN_OF_KEYHOLE, 1);
		}
		else if (event.equalsIgnoreCase("30622-04.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, KEY_OF_TITAN, 1);
			takeItems(player, NIKOLAS_LIST, 1);
			takeItems(player, RECIPE_TITAN_KEY, 1);
			takeItems(player, STAKATO_SHELL, 20);
			takeItems(player, SPIDER_THORN, 10);
			takeItems(player, TOAD_LORD_SAC, 10);
			giveItems(player, MAPHR_TABLET_FRAGMENT, 1);
		}
		
		return htmltext;
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
				if (player.getRace() != ClassRace.DWARF)
					htmltext = "30104-01.htm";
				else if (player.getStatus().getLevel() < 37)
					htmltext = "30104-02.htm";
				else if (player.getClassId().getLevel() != 1)
					htmltext = "30104-01a.htm";
				else
					htmltext = "30104-03.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case PARMAN:
						if (cond == 1)
							htmltext = "30104-05.htm";
						else if (cond == 2)
							htmltext = "30104-06.htm";
						else if (cond == 3)
						{
							if (player.getStatus().getLevel() < 38)
								htmltext = "30104-09.htm";
							else
							{
								htmltext = "30104-10.htm";
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, PARMAN_INSTRUCTIONS, 1);
								giveItems(player, PARMAN_LETTER, 1);
								giveItems(player, RING_OF_TESTIMONY_2, 1);
							}
						}
						else if (cond > 3 && cond < 7)
							htmltext = "30104-11.htm";
						else if (cond == 7 || cond == 8)
							htmltext = "30104-12.htm";
						else if (cond == 9)
						{
							htmltext = "30104-13.htm";
							takeItems(player, MAPHR_TABLET_FRAGMENT, 1);
							takeItems(player, RING_OF_TESTIMONY_2, 1);
							giveItems(player, MARK_OF_PROSPERITY, 1);
							rewardExpAndSp(player, 12969, 1000);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case LOCKIRIN:
						if (cond == 1 || cond == 2)
						{
							if (player.getInventory().hasItem(COLLECTION_LICENSE))
							{
								if (player.getInventory().hasItems(RECEIPT_OF_CONTRIBUTION_1, RECEIPT_OF_CONTRIBUTION_2, RECEIPT_OF_CONTRIBUTION_3, RECEIPT_OF_CONTRIBUTION_4, RECEIPT_OF_CONTRIBUTION_5))
								{
									htmltext = "30531-05.htm";
									takeItems(player, COLLECTION_LICENSE, 1);
									takeItems(player, RECEIPT_OF_CONTRIBUTION_1, 1);
									takeItems(player, RECEIPT_OF_CONTRIBUTION_2, 1);
									takeItems(player, RECEIPT_OF_CONTRIBUTION_3, 1);
									takeItems(player, RECEIPT_OF_CONTRIBUTION_4, 1);
									takeItems(player, RECEIPT_OF_CONTRIBUTION_5, 1);
									giveItems(player, OLD_ACCOUNT_BOOK, 1);
									
									if (player.getInventory().hasItems(BLESSED_SEED, EMILY_RECIPE, LILITH_ELVEN_WAFER))
									{
										st.setCond(2);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
								else
									htmltext = "30531-04.htm";
							}
							else
								htmltext = (player.getInventory().hasItem(OLD_ACCOUNT_BOOK)) ? "30531-06.htm" : "30531-01.htm";
						}
						else if (cond >= 4)
							htmltext = "30531-07.htm";
						break;
					
					case SPIRON:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(LOCKIRIN_NOTICE_1))
							{
								htmltext = "30532-01.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, LOCKIRIN_NOTICE_1, 1);
							}
							else if (player.getInventory().hasItem(CONTRIBUTION_OF_SHARI))
							{
								htmltext = "30532-03.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, CONTRIBUTION_OF_SHARI, 1);
								giveItems(player, RECEIPT_OF_CONTRIBUTION_1, 1);
							}
							else
								htmltext = (player.getInventory().hasItem(RECEIPT_OF_CONTRIBUTION_1)) ? "30532-04.htm" : "30532-02.htm";
						}
						break;
					
					case BALANKI:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(LOCKIRIN_NOTICE_2))
							{
								htmltext = "30533-01.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, LOCKIRIN_NOTICE_2, 1);
							}
							else if (player.getInventory().hasItems(CONTRIBUTION_OF_MARYSE, CONTRIBUTION_OF_MION))
							{
								htmltext = "30533-03.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, CONTRIBUTION_OF_MARYSE, 1);
								takeItems(player, CONTRIBUTION_OF_MION, 1);
								giveItems(player, RECEIPT_OF_CONTRIBUTION_2, 1);
							}
							else
								htmltext = (player.getInventory().hasItem(RECEIPT_OF_CONTRIBUTION_2)) ? "30533-04.htm" : "30533-02.htm";
						}
						break;
					
					case KEEF:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(LOCKIRIN_NOTICE_3))
							{
								htmltext = "30534-01.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, LOCKIRIN_NOTICE_3, 1);
							}
							else if (player.getInventory().hasItem(PROCURATION_OF_TOROCCO))
								htmltext = "30534-03.htm";
							else
								htmltext = (player.getInventory().hasItem(RECEIPT_OF_CONTRIBUTION_3)) ? "30534-04.htm" : "30534-02.htm";
						}
						break;
					
					case FILAUR:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(LOCKIRIN_NOTICE_4))
							{
								htmltext = "30535-01.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, LOCKIRIN_NOTICE_4, 1);
							}
							else if (player.getInventory().hasItem(RECEIPT_OF_BOLTER))
							{
								htmltext = "30535-03.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, RECEIPT_OF_BOLTER, 1);
								giveItems(player, RECEIPT_OF_CONTRIBUTION_4, 1);
							}
							else
								htmltext = (player.getInventory().hasItem(RECEIPT_OF_CONTRIBUTION_4)) ? "30535-04.htm" : "30535-02.htm";
						}
						break;
					
					case ARIN:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(LOCKIRIN_NOTICE_5))
							{
								htmltext = "30536-01.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, LOCKIRIN_NOTICE_5, 1);
							}
							else if (player.getInventory().hasItem(CONTRIBUTION_OF_TOMA))
							{
								htmltext = "30536-03.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, CONTRIBUTION_OF_TOMA, 1);
								giveItems(player, RECEIPT_OF_CONTRIBUTION_5, 1);
							}
							else
								htmltext = (player.getInventory().hasItem(RECEIPT_OF_CONTRIBUTION_5)) ? "30536-04.htm" : "30536-02.htm";
						}
						break;
					
					case SHARI:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(CONTRIBUTION_OF_SHARI))
								htmltext = "30517-02.htm";
							else if (!player.getInventory().hasAtLeastOneItem(LOCKIRIN_NOTICE_1, RECEIPT_OF_CONTRIBUTION_1))
							{
								htmltext = "30517-01.htm";
								playSound(player, SOUND_ITEMGET);
								giveItems(player, CONTRIBUTION_OF_SHARI, 1);
							}
						}
						break;
					
					case MION:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(CONTRIBUTION_OF_MION))
								htmltext = "30519-02.htm";
							else if (!player.getInventory().hasAtLeastOneItem(LOCKIRIN_NOTICE_2, RECEIPT_OF_CONTRIBUTION_2))
							{
								htmltext = "30519-01.htm";
								playSound(player, SOUND_ITEMGET);
								giveItems(player, CONTRIBUTION_OF_MION, 1);
							}
						}
						break;
					
					case MARYSE_REDBONNET:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(MARYSE_REQUEST))
							{
								if (player.getInventory().getItemCount(ANIMAL_SKIN) < 100)
									htmltext = "30553-02.htm";
								else
								{
									htmltext = "30553-03.htm";
									playSound(player, SOUND_ITEMGET);
									takeItems(player, ANIMAL_SKIN, 100);
									takeItems(player, MARYSE_REQUEST, 1);
									giveItems(player, CONTRIBUTION_OF_MARYSE, 1);
								}
							}
							else if (player.getInventory().hasItem(CONTRIBUTION_OF_MARYSE))
								htmltext = "30553-04.htm";
							else if (!player.getInventory().hasAtLeastOneItem(LOCKIRIN_NOTICE_2, RECEIPT_OF_CONTRIBUTION_2))
							{
								htmltext = "30553-01.htm";
								playSound(player, SOUND_ITEMGET);
								giveItems(player, MARYSE_REQUEST, 1);
							}
						}
						break;
					
					case TOROCCO:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(PROCURATION_OF_TOROCCO))
								htmltext = "30555-03.htm";
							else if (!player.getInventory().hasAtLeastOneItem(LOCKIRIN_NOTICE_3, RECEIPT_OF_CONTRIBUTION_3))
								htmltext = "30555-01.htm";
						}
						break;
					
					case BOLTER:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(RECEIPT_OF_BOLTER))
								htmltext = "30554-02.htm";
							else if (!player.getInventory().hasAtLeastOneItem(LOCKIRIN_NOTICE_4, RECEIPT_OF_CONTRIBUTION_4))
							{
								htmltext = "30554-01.htm";
								playSound(player, SOUND_ITEMGET);
								giveItems(player, RECEIPT_OF_BOLTER, 1);
							}
						}
						break;
					
					case TOMA:
						if (cond == 1 && player.getInventory().hasItem(COLLECTION_LICENSE))
						{
							if (player.getInventory().hasItem(CONTRIBUTION_OF_TOMA))
								htmltext = "30556-02.htm";
							else if (!player.getInventory().hasAtLeastOneItem(LOCKIRIN_NOTICE_5, RECEIPT_OF_CONTRIBUTION_5))
							{
								htmltext = "30556-01.htm";
								playSound(player, SOUND_ITEMGET);
								giveItems(player, CONTRIBUTION_OF_TOMA, 1);
							}
						}
						break;
					
					case PIOTUR:
						if (cond == 1 || cond == 2)
							htmltext = (player.getInventory().hasItem(BLESSED_SEED)) ? "30597-03.htm" : "30597-01.htm";
						else if (cond >= 4)
							htmltext = "30597-04.htm";
						break;
					
					case WILFORD:
						if (cond == 1 || cond == 2)
						{
							if (player.getInventory().hasItem(LILITH_ELVEN_WAFER))
								htmltext = "30005-06.htm";
							else
								htmltext = (player.getInventory().hasItem(CRYSTAL_BROOCH)) ? "30005-05.htm" : "30005-01.htm";
						}
						else if (cond >= 4)
							htmltext = "30005-07.htm";
						break;
					
					case LILITH:
						if (cond == 1 || cond == 2)
						{
							if (player.getInventory().hasItem(CRYSTAL_BROOCH))
								htmltext = "30368-01.htm";
							else if (player.getInventory().hasItem(LILITH_ELVEN_WAFER))
								htmltext = "30368-04.htm";
						}
						else if (cond >= 4)
							htmltext = "30368-05.htm";
						break;
					
					case BRIGHT:
						if (cond == 1 || cond == 2)
						{
							if (player.getInventory().hasItem(EMILY_RECIPE))
								htmltext = "30466-07.htm";
							else if (player.getInventory().hasItem(MANDRAGORA_BOUQUET))
								htmltext = "30466-06.htm";
							else if (player.getInventory().hasItem(BRIGHT_LIST))
							{
								if (player.getInventory().getItemCount(CRIMSON_MOSS) + player.getInventory().getItemCount(MANDRAGORA_PETAL) < 30)
									htmltext = "30466-04.htm";
								else
								{
									htmltext = "30466-05.htm";
									playSound(player, SOUND_ITEMGET);
									takeItems(player, BRIGHT_LIST, 1);
									takeItems(player, CRIMSON_MOSS, 10);
									takeItems(player, MANDRAGORA_PETAL, 20);
									giveItems(player, MANDRAGORA_BOUQUET, 1);
								}
							}
							else
								htmltext = "30466-01.htm";
						}
						else if (cond >= 4)
							htmltext = "30466-08.htm";
						break;
					
					case EMILY:
						if (cond == 1 || cond == 2)
						{
							if (player.getInventory().hasItem(EMILY_RECIPE))
								htmltext = "30620-04.htm";
							else if (player.getInventory().hasItem(MANDRAGORA_BOUQUET))
								htmltext = "30620-01.htm";
						}
						else if (cond >= 4)
							htmltext = "30620-05.htm";
						break;
					
					case NIKOLA:
						if (cond == 4)
						{
							htmltext = "30621-01.htm";
							playSound(player, SOUND_ITEMGET);
							takeItems(player, PARMAN_LETTER, 1);
						}
						else if (cond == 5)
							htmltext = "30621-05.htm";
						else if (cond == 6)
						{
							htmltext = "30621-06.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, PATTERN_OF_KEYHOLE, 1);
							giveItems(player, NIKOLAS_LIST, 1);
							giveItems(player, RECIPE_TITAN_KEY, 1);
						}
						else if (cond == 7 || cond == 8)
							htmltext = (player.getInventory().hasItem(KEY_OF_TITAN)) ? "30621-08.htm" : "30621-07.htm";
						else if (cond == 9)
							htmltext = "30621-09.htm";
						break;
					
					case BOX_OF_TITAN:
						if (cond == 5)
							htmltext = "30622-01.htm";
						else if (cond == 8 && player.getInventory().hasItem(KEY_OF_TITAN))
							htmltext = "30622-03.htm";
						else
							htmltext = "30622-05.htm";
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
			case MANDRAGORA_SPROUT_1:
				if (player.getInventory().hasItem(BRIGHT_LIST))
					dropItems(player, MANDRAGORA_PETAL, 1, 20, 300000);
				break;
			
			case MANDRAGORA_SPROUT_2:
				if (player.getInventory().hasItem(BRIGHT_LIST))
					dropItems(player, MANDRAGORA_PETAL, 1, 20, 600000);
				break;
			
			case MANDRAGORA_SAPLING:
				if (player.getInventory().hasItem(BRIGHT_LIST))
					dropItems(player, MANDRAGORA_PETAL, 1, 20, 800000);
				break;
			
			case MANDRAGORA_BLOSSOM:
				if (player.getInventory().hasItem(BRIGHT_LIST))
					dropItemsAlways(player, MANDRAGORA_PETAL, 1, 20);
				break;
			
			case GIANT_CRIMSON_ANT:
				if (player.getInventory().hasItem(BRIGHT_LIST))
					dropItemsAlways(player, CRIMSON_MOSS, 1, 10);
				break;
			
			case MARSH_STAKATO:
				if (st.getCond() == 7 && dropItems(player, STAKATO_SHELL, 1, 20, 200000) && player.getInventory().getItemCount(TOAD_LORD_SAC) + player.getInventory().getItemCount(SPIDER_THORN) == 20)
					st.setCond(8);
				break;
			
			case MARSH_STAKATO_WORKER:
				if (st.getCond() == 7 && dropItems(player, STAKATO_SHELL, 1, 20, 300000) && player.getInventory().getItemCount(TOAD_LORD_SAC) + player.getInventory().getItemCount(SPIDER_THORN) == 20)
					st.setCond(8);
				break;
			
			case MARSH_STAKATO_SOLDIER:
				if (st.getCond() == 7 && dropItems(player, STAKATO_SHELL, 1, 20, 500000) && player.getInventory().getItemCount(TOAD_LORD_SAC) + player.getInventory().getItemCount(SPIDER_THORN) == 20)
					st.setCond(8);
				break;
			
			case MARSH_STAKATO_DRONE:
				if (st.getCond() == 7 && dropItems(player, STAKATO_SHELL, 1, 20, 600000) && player.getInventory().getItemCount(TOAD_LORD_SAC) + player.getInventory().getItemCount(SPIDER_THORN) == 20)
					st.setCond(8);
				break;
			
			case TOAD_LORD:
				if (st.getCond() == 7 && dropItems(player, TOAD_LORD_SAC, 1, 10, 200000) && player.getInventory().getItemCount(STAKATO_SHELL) + player.getInventory().getItemCount(SPIDER_THORN) == 30)
					st.setCond(8);
				break;
			
			case MARSH_SPIDER:
				if (st.getCond() == 7 && dropItems(player, SPIDER_THORN, 1, 10, 200000) && player.getInventory().getItemCount(STAKATO_SHELL) + player.getInventory().getItemCount(TOAD_LORD_SAC) == 30)
					st.setCond(8);
				break;
		}
	}
}