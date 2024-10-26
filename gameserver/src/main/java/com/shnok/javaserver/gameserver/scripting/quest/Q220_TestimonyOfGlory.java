package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Q220_TestimonyOfGlory extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q220_TestimonyOfGlory";
	
	private static final Location VUKU_CHIEF_DRIKO_LOC = new Location(-2150, 124443, -3724);
	private static final Location TUREK_CHIEF_BURAI_LOC = new Location(-94294, 110818, -3696);
	private static final Location LEUNT_CHIEF_HARAK_LOC = new Location(-55217, 200628, -3724);
	private static final Location BREKA_CHIEF_VOLTAR_LOC = new Location(80100, 119991, -2264);
	private static final Location ENKU_CHIEF_KEPRA_LOC = new Location(12805, 189249, -3601);
	
	// Items
	private static final int VOKIAN_ORDER_1 = 3204;
	private static final int MANASHEN_SHARD = 3205;
	private static final int TYRANT_TALON = 3206;
	private static final int GUARDIAN_BASILISK_FANG = 3207;
	private static final int VOKIAN_ORDER_2 = 3208;
	private static final int NECKLACE_OF_AUTHORITY = 3209;
	private static final int CHIANTA_ORDER_1 = 3210;
	private static final int SCEPTER_OF_BREKA = 3211;
	private static final int SCEPTER_OF_ENKU = 3212;
	private static final int SCEPTER_OF_VUKU = 3213;
	private static final int SCEPTER_OF_TUREK = 3214;
	private static final int SCEPTER_OF_TUNATH = 3215;
	private static final int CHIANTA_ORDER_2 = 3216;
	private static final int CHIANTA_ORDER_3 = 3217;
	private static final int TAMLIN_ORC_SKULL = 3218;
	private static final int TIMAK_ORC_HEAD = 3219;
	private static final int SCEPTER_BOX = 3220;
	private static final int PASHIKA_HEAD = 3221;
	private static final int VULTUS_HEAD = 3222;
	private static final int GLOVE_OF_VOLTAR = 3223;
	private static final int ENKU_OVERLORD_HEAD = 3224;
	private static final int GLOVE_OF_KEPRA = 3225;
	private static final int MAKUM_BUGBEAR_HEAD = 3226;
	private static final int GLOVE_OF_BURAI = 3227;
	private static final int MANAKIA_LETTER_1 = 3228;
	private static final int MANAKIA_LETTER_2 = 3229;
	private static final int KASMAN_LETTER_1 = 3230;
	private static final int KASMAN_LETTER_2 = 3231;
	private static final int KASMAN_LETTER_3 = 3232;
	private static final int DRIKO_CONTRACT = 3233;
	private static final int STAKATO_DRONE_HUSK = 3234;
	private static final int TANAPI_ORDER = 3235;
	private static final int SCEPTER_OF_TANTOS = 3236;
	private static final int RITUAL_BOX = 3237;
	
	// Rewards
	private static final int MARK_OF_GLORY = 3203;
	
	// NPCs
	private static final int KASMAN = 30501;
	private static final int VOKIAN = 30514;
	private static final int MANAKIA = 30515;
	private static final int KAKAI = 30565;
	private static final int TANAPI = 30571;
	private static final int VOLTAR = 30615;
	private static final int KEPRA = 30616;
	private static final int BURAI = 30617;
	private static final int HARAK = 30618;
	private static final int DRIKO = 30619;
	private static final int CHIANTA = 30642;
	
	// Monsters
	private static final int TYRANT = 20192;
	private static final int MARSH_STAKATO_DRONE = 20234;
	private static final int GUARDIAN_BASILISK = 20550;
	private static final int MANASHEN_GARGOYLE = 20563;
	private static final int TIMAK_ORC = 20583;
	private static final int TIMAK_ORC_ARCHER = 20584;
	private static final int TIMAK_ORC_SOLDIER = 20585;
	private static final int TIMAK_ORC_WARRIOR = 20586;
	private static final int TIMAK_ORC_SHAMAN = 20587;
	private static final int TIMAK_ORC_OVERLORD = 20588;
	private static final int TAMLIN_ORC = 20601;
	private static final int TAMLIN_ORC_ARCHER = 20602;
	private static final int RAGNA_ORC_OVERLORD = 20778;
	private static final int RAGNA_ORC_SEER = 20779;
	private static final int PASHIKA_SON_OF_VOLTAR = 27080;
	private static final int VULTUS_SON_OF_VOLTAR = 27081;
	private static final int ENKU_ORC_OVERLORD = 27082;
	private static final int MAKUM_BUGBEAR_THUG = 27083;
	private static final int REVENANT_OF_TANTOS_CHIEF = 27086;
	
	public Q220_TestimonyOfGlory()
	{
		super(220, "Testimony Of Glory");
		
		setItemsIds(VOKIAN_ORDER_1, MANASHEN_SHARD, TYRANT_TALON, GUARDIAN_BASILISK_FANG, VOKIAN_ORDER_2, NECKLACE_OF_AUTHORITY, CHIANTA_ORDER_1, SCEPTER_OF_BREKA, SCEPTER_OF_ENKU, SCEPTER_OF_VUKU, SCEPTER_OF_TUREK, SCEPTER_OF_TUNATH, CHIANTA_ORDER_2, CHIANTA_ORDER_3, TAMLIN_ORC_SKULL, TIMAK_ORC_HEAD, SCEPTER_BOX, PASHIKA_HEAD, VULTUS_HEAD, GLOVE_OF_VOLTAR, ENKU_OVERLORD_HEAD, GLOVE_OF_KEPRA, MAKUM_BUGBEAR_HEAD, GLOVE_OF_BURAI, MANAKIA_LETTER_1, MANAKIA_LETTER_2, KASMAN_LETTER_1, KASMAN_LETTER_2, KASMAN_LETTER_3, DRIKO_CONTRACT, STAKATO_DRONE_HUSK, TANAPI_ORDER, SCEPTER_OF_TANTOS, RITUAL_BOX);
		
		addQuestStart(VOKIAN);
		addTalkId(KASMAN, VOKIAN, MANAKIA, KAKAI, TANAPI, VOLTAR, KEPRA, BURAI, HARAK, DRIKO, CHIANTA);
		
		addAttacked(RAGNA_ORC_OVERLORD, RAGNA_ORC_SEER, REVENANT_OF_TANTOS_CHIEF);
		addMyDying(TYRANT, MARSH_STAKATO_DRONE, GUARDIAN_BASILISK, MANASHEN_GARGOYLE, TIMAK_ORC, TIMAK_ORC_ARCHER, TIMAK_ORC_SOLDIER, TIMAK_ORC_WARRIOR, TIMAK_ORC_SHAMAN, TIMAK_ORC_OVERLORD, TAMLIN_ORC, TAMLIN_ORC_ARCHER, RAGNA_ORC_OVERLORD, RAGNA_ORC_SEER, PASHIKA_SON_OF_VOLTAR, VULTUS_SON_OF_VOLTAR, ENKU_ORC_OVERLORD, MAKUM_BUGBEAR_THUG, REVENANT_OF_TANTOS_CHIEF);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// VOKIAN
		if (event.equalsIgnoreCase("30514-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, VOKIAN_ORDER_1, 1);
			
			if (giveDimensionalDiamonds37(player))
				htmltext = "30514-05a.htm";
		}
		// CHIANTA
		else if (event.equalsIgnoreCase("30642-03.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, VOKIAN_ORDER_2, 1);
			giveItems(player, CHIANTA_ORDER_1, 1);
		}
		else if (event.equalsIgnoreCase("30642-07.htm"))
		{
			takeItems(player, CHIANTA_ORDER_1, 1);
			takeItems(player, KASMAN_LETTER_1, 1);
			takeItems(player, MANAKIA_LETTER_1, 1);
			takeItems(player, MANAKIA_LETTER_2, 1);
			takeItems(player, SCEPTER_OF_BREKA, 1);
			takeItems(player, SCEPTER_OF_ENKU, 1);
			takeItems(player, SCEPTER_OF_TUNATH, 1);
			takeItems(player, SCEPTER_OF_TUREK, 1);
			takeItems(player, SCEPTER_OF_VUKU, 1);
			
			if (player.getStatus().getLevel() >= 37)
			{
				st.setCond(6);
				playSound(player, SOUND_MIDDLE);
				giveItems(player, CHIANTA_ORDER_3, 1);
			}
			else
			{
				htmltext = "30642-06.htm";
				playSound(player, SOUND_ITEMGET);
				giveItems(player, CHIANTA_ORDER_2, 1);
			}
		}
		// KASMAN
		else if (event.equalsIgnoreCase("30501-02.htm") && !player.getInventory().hasItem(SCEPTER_OF_VUKU))
		{
			if (player.getInventory().hasItem(KASMAN_LETTER_1))
				htmltext = "30501-04.htm";
			else
			{
				htmltext = "30501-03.htm";
				playSound(player, SOUND_ITEMGET);
				giveItems(player, KASMAN_LETTER_1, 1);
			}
			player.getRadarList().addMarker(VUKU_CHIEF_DRIKO_LOC);
		}
		else if (event.equalsIgnoreCase("30501-05.htm") && !player.getInventory().hasItem(SCEPTER_OF_TUREK))
		{
			if (player.getInventory().hasItem(KASMAN_LETTER_2))
				htmltext = "30501-07.htm";
			else
			{
				htmltext = "30501-06.htm";
				playSound(player, SOUND_ITEMGET);
				giveItems(player, KASMAN_LETTER_2, 1);
			}
			player.getRadarList().addMarker(TUREK_CHIEF_BURAI_LOC);
		}
		else if (event.equalsIgnoreCase("30501-08.htm") && !player.getInventory().hasItem(SCEPTER_OF_TUNATH))
		{
			if (player.getInventory().hasItem(KASMAN_LETTER_3))
				htmltext = "30501-10.htm";
			else
			{
				htmltext = "30501-09.htm";
				playSound(player, SOUND_ITEMGET);
				giveItems(player, KASMAN_LETTER_3, 1);
			}
			player.getRadarList().addMarker(LEUNT_CHIEF_HARAK_LOC);
		}
		// MANAKIA
		else if (event.equalsIgnoreCase("30515-02.htm") && !player.getInventory().hasItem(SCEPTER_OF_BREKA))
		{
			if (player.getInventory().hasItem(MANAKIA_LETTER_1))
				htmltext = "30515-04.htm";
			else
			{
				htmltext = "30515-03.htm";
				playSound(player, SOUND_ITEMGET);
				giveItems(player, MANAKIA_LETTER_1, 1);
			}
			player.getRadarList().addMarker(BREKA_CHIEF_VOLTAR_LOC);
		}
		else if (event.equalsIgnoreCase("30515-05.htm") && !player.getInventory().hasItem(SCEPTER_OF_ENKU))
		{
			if (player.getInventory().hasItem(MANAKIA_LETTER_2))
				htmltext = "30515-07.htm";
			else
			{
				htmltext = "30515-06.htm";
				playSound(player, SOUND_ITEMGET);
				giveItems(player, MANAKIA_LETTER_2, 1);
			}
			player.getRadarList().addMarker(ENKU_CHIEF_KEPRA_LOC);
		}
		// VOLTAR
		else if (event.equalsIgnoreCase("30615-04.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, MANAKIA_LETTER_1, 1);
			giveItems(player, GLOVE_OF_VOLTAR, 1);
			
			if (npc.getMinions().isEmpty())
			{
				createOnePrivate(npc, PASHIKA_SON_OF_VOLTAR, 200000, true);
				createOnePrivate(npc, VULTUS_SON_OF_VOLTAR, 200000, true);
			}
		}
		// KEPRA
		else if (event.equalsIgnoreCase("30616-05.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, MANAKIA_LETTER_2, 1);
			giveItems(player, GLOVE_OF_KEPRA, 1);
			
			if (npc.getMinions().isEmpty())
			{
				createOnePrivate(npc, ENKU_ORC_OVERLORD, 200000, true);
				createOnePrivate(npc, ENKU_ORC_OVERLORD, 200000, true);
				createOnePrivate(npc, ENKU_ORC_OVERLORD, 200000, true);
				createOnePrivate(npc, ENKU_ORC_OVERLORD, 200000, true);
			}
		}
		// BURAI
		else if (event.equalsIgnoreCase("30617-04.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, KASMAN_LETTER_2, 1);
			giveItems(player, GLOVE_OF_BURAI, 1);
			
			if (npc.getMinions().isEmpty())
			{
				createOnePrivate(npc, MAKUM_BUGBEAR_THUG, 200000, true);
				createOnePrivate(npc, MAKUM_BUGBEAR_THUG, 200000, true);
			}
		}
		// HARAK
		else if (event.equalsIgnoreCase("30618-03.htm"))
		{
			takeItems(player, KASMAN_LETTER_3, 1);
			giveItems(player, SCEPTER_OF_TUNATH, 1);
			
			if (player.getInventory().hasItems(SCEPTER_OF_BREKA, SCEPTER_OF_ENKU, SCEPTER_OF_VUKU, SCEPTER_OF_TUREK))
			{
				st.setCond(5);
				playSound(player, SOUND_MIDDLE);
			}
			else
				playSound(player, SOUND_ITEMGET);
		}
		// DRIKO
		else if (event.equalsIgnoreCase("30619-03.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, KASMAN_LETTER_1, 1);
			giveItems(player, DRIKO_CONTRACT, 1);
		}
		// TANAPI
		else if (event.equalsIgnoreCase("30571-03.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SCEPTER_BOX, 1);
			giveItems(player, TANAPI_ORDER, 1);
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
				if (player.getRace() != ClassRace.ORC)
					htmltext = "30514-01.htm";
				else if (player.getStatus().getLevel() < 37)
					htmltext = "30514-02.htm";
				else if (player.getClassId().getLevel() != 1)
					htmltext = "30514-01a.htm";
				else
					htmltext = "30514-03.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case VOKIAN:
						if (cond == 1)
							htmltext = "30514-06.htm";
						else if (cond == 2)
						{
							htmltext = "30514-08.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GUARDIAN_BASILISK_FANG, 10);
							takeItems(player, MANASHEN_SHARD, 10);
							takeItems(player, TYRANT_TALON, 10);
							takeItems(player, VOKIAN_ORDER_1, 1);
							giveItems(player, NECKLACE_OF_AUTHORITY, 1);
							giveItems(player, VOKIAN_ORDER_2, 1);
						}
						else if (cond == 3)
							htmltext = "30514-09.htm";
						else if (cond == 8)
							htmltext = "30514-10.htm";
						break;
					
					case CHIANTA:
						if (cond == 3)
							htmltext = "30642-01.htm";
						else if (cond == 4)
							htmltext = "30642-04.htm";
						else if (cond == 5)
						{
							if (player.getInventory().hasItem(CHIANTA_ORDER_2))
							{
								if (player.getStatus().getLevel() >= 37)
								{
									htmltext = "30642-09.htm";
									st.setCond(6);
									playSound(player, SOUND_MIDDLE);
									takeItems(player, CHIANTA_ORDER_2, 1);
									giveItems(player, CHIANTA_ORDER_3, 1);
								}
								else
									htmltext = "30642-08.htm";
							}
							else
								htmltext = "30642-05.htm";
						}
						else if (cond == 6)
							htmltext = "30642-10.htm";
						else if (cond == 7)
						{
							htmltext = "30642-11.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CHIANTA_ORDER_3, 1);
							takeItems(player, NECKLACE_OF_AUTHORITY, 1);
							takeItems(player, TAMLIN_ORC_SKULL, 20);
							takeItems(player, TIMAK_ORC_HEAD, 20);
							giveItems(player, SCEPTER_BOX, 1);
						}
						else if (cond == 8)
							htmltext = "30642-12.htm";
						else if (cond > 8)
							htmltext = "30642-13.htm";
						break;
					
					case KASMAN:
						if (player.getInventory().hasItem(CHIANTA_ORDER_1))
							htmltext = "30501-01.htm";
						else if (cond > 4)
							htmltext = "30501-11.htm";
						break;
					
					case MANAKIA:
						if (player.getInventory().hasItem(CHIANTA_ORDER_1))
							htmltext = "30515-01.htm";
						else if (cond > 4)
							htmltext = "30515-08.htm";
						break;
					
					case VOLTAR:
						if (cond > 3)
						{
							if (player.getInventory().hasItem(MANAKIA_LETTER_1))
							{
								htmltext = "30615-02.htm";
								player.getRadarList().removeMarker(BREKA_CHIEF_VOLTAR_LOC);
							}
							else if (player.getInventory().hasItem(GLOVE_OF_VOLTAR))
							{
								htmltext = "30615-05.htm";
								
								if (npc.getMinions().isEmpty())
								{
									createOnePrivate(npc, PASHIKA_SON_OF_VOLTAR, 200000, true);
									createOnePrivate(npc, VULTUS_SON_OF_VOLTAR, 200000, true);
								}
							}
							else if (player.getInventory().hasItems(PASHIKA_HEAD, VULTUS_HEAD))
							{
								htmltext = "30615-06.htm";
								takeItems(player, PASHIKA_HEAD, 1);
								takeItems(player, VULTUS_HEAD, 1);
								giveItems(player, SCEPTER_OF_BREKA, 1);
								
								if (player.getInventory().hasItems(SCEPTER_OF_ENKU, SCEPTER_OF_VUKU, SCEPTER_OF_TUREK, SCEPTER_OF_TUNATH))
								{
									st.setCond(5);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else if (player.getInventory().hasItem(SCEPTER_OF_BREKA))
								htmltext = "30615-07.htm";
							else if (player.getInventory().hasItem(CHIANTA_ORDER_1))
								htmltext = "30615-01.htm";
							else if (cond < 9)
								htmltext = "30615-08.htm";
						}
						break;
					
					case KEPRA:
						if (cond > 3)
						{
							if (player.getInventory().hasItem(MANAKIA_LETTER_2))
							{
								htmltext = "30616-02.htm";
								player.getRadarList().removeMarker(ENKU_CHIEF_KEPRA_LOC);
							}
							else if (player.getInventory().hasItem(GLOVE_OF_KEPRA))
							{
								htmltext = "30616-05.htm";
								
								if (npc.getMinions().isEmpty())
								{
									createOnePrivate(npc, ENKU_ORC_OVERLORD, 200000, true);
									createOnePrivate(npc, ENKU_ORC_OVERLORD, 200000, true);
									createOnePrivate(npc, ENKU_ORC_OVERLORD, 200000, true);
									createOnePrivate(npc, ENKU_ORC_OVERLORD, 200000, true);
								}
							}
							else if (player.getInventory().getItemCount(ENKU_OVERLORD_HEAD) == 4)
							{
								htmltext = "30616-06.htm";
								takeItems(player, ENKU_OVERLORD_HEAD, 4);
								giveItems(player, SCEPTER_OF_ENKU, 1);
								
								if (player.getInventory().hasItems(SCEPTER_OF_BREKA, SCEPTER_OF_VUKU, SCEPTER_OF_TUREK, SCEPTER_OF_TUNATH))
								{
									st.setCond(5);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else if (player.getInventory().hasItem(SCEPTER_OF_ENKU))
								htmltext = "30616-07.htm";
							else if (player.getInventory().hasItem(CHIANTA_ORDER_1))
								htmltext = "30616-01.htm";
							else if (cond < 9)
								htmltext = "30616-08.htm";
						}
						break;
					
					case BURAI:
						if (cond > 3)
						{
							if (player.getInventory().hasItem(KASMAN_LETTER_2))
							{
								htmltext = "30617-02.htm";
								player.getRadarList().removeMarker(TUREK_CHIEF_BURAI_LOC);
							}
							else if (player.getInventory().hasItem(GLOVE_OF_BURAI))
							{
								htmltext = "30617-04.htm";
								
								if (npc.getMinions().isEmpty())
								{
									createOnePrivate(npc, MAKUM_BUGBEAR_THUG, 200000, true);
									createOnePrivate(npc, MAKUM_BUGBEAR_THUG, 200000, true);
								}
							}
							else if (player.getInventory().getItemCount(MAKUM_BUGBEAR_HEAD) == 2)
							{
								htmltext = "30617-05.htm";
								takeItems(player, MAKUM_BUGBEAR_HEAD, 2);
								giveItems(player, SCEPTER_OF_TUREK, 1);
								
								if (player.getInventory().hasItems(SCEPTER_OF_BREKA, SCEPTER_OF_VUKU, SCEPTER_OF_ENKU, SCEPTER_OF_TUNATH))
								{
									st.setCond(5);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else if (player.getInventory().hasItem(SCEPTER_OF_TUREK))
								htmltext = "30617-06.htm";
							else if (player.getInventory().hasItem(CHIANTA_ORDER_1))
								htmltext = "30617-01.htm";
							else if (cond < 8)
								htmltext = "30617-07.htm";
						}
						break;
					
					case HARAK:
						if (cond > 3)
						{
							if (player.getInventory().hasItem(KASMAN_LETTER_3))
							{
								htmltext = "30618-02.htm";
								player.getRadarList().removeMarker(LEUNT_CHIEF_HARAK_LOC);
							}
							else if (player.getInventory().hasItem(SCEPTER_OF_TUNATH))
								htmltext = "30618-04.htm";
							else if (player.getInventory().hasItem(CHIANTA_ORDER_1))
								htmltext = "30618-01.htm";
							else if (cond < 9)
								htmltext = "30618-05.htm";
						}
						break;
					
					case DRIKO:
						if (cond > 3)
						{
							if (player.getInventory().hasItem(KASMAN_LETTER_1))
							{
								htmltext = "30619-02.htm";
								player.getRadarList().removeMarker(VUKU_CHIEF_DRIKO_LOC);
							}
							else if (player.getInventory().hasItem(DRIKO_CONTRACT))
							{
								if (player.getInventory().getItemCount(STAKATO_DRONE_HUSK) == 30)
								{
									htmltext = "30619-05.htm";
									takeItems(player, DRIKO_CONTRACT, 1);
									takeItems(player, STAKATO_DRONE_HUSK, 30);
									giveItems(player, SCEPTER_OF_VUKU, 1);
									
									if (player.getInventory().hasItems(SCEPTER_OF_BREKA, SCEPTER_OF_TUREK, SCEPTER_OF_ENKU, SCEPTER_OF_TUNATH))
									{
										st.setCond(5);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
								else
									htmltext = "30619-04.htm";
							}
							else if (player.getInventory().hasItem(SCEPTER_OF_VUKU))
								htmltext = "30619-06.htm";
							else if (player.getInventory().hasItem(CHIANTA_ORDER_1))
								htmltext = "30619-01.htm";
							else if (cond < 8)
								htmltext = "30619-07.htm";
						}
						break;
					
					case TANAPI:
						if (cond == 8)
							htmltext = "30571-01.htm";
						else if (cond == 9)
							htmltext = "30571-04.htm";
						else if (cond == 10)
						{
							htmltext = "30571-05.htm";
							st.setCond(11);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SCEPTER_OF_TANTOS, 1);
							takeItems(player, TANAPI_ORDER, 1);
							giveItems(player, RITUAL_BOX, 1);
						}
						else if (cond == 11)
							htmltext = "30571-06.htm";
						break;
					
					case KAKAI:
						if (cond > 7 && cond < 11)
							htmltext = "30565-01.htm";
						else if (cond == 11)
						{
							htmltext = "30565-02.htm";
							takeItems(player, RITUAL_BOX, 1);
							giveItems(player, MARK_OF_GLORY, 1);
							rewardExpAndSp(player, 91457, 2500);
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
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Player player = attacker.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case RAGNA_ORC_OVERLORD, RAGNA_ORC_SEER:
				if (st.getCond() == 9 && npc.isScriptValue(0))
				{
					npc.broadcastNpcSay(NpcStringId.ID_22051);
					npc.setScriptValue(1);
				}
				break;
			
			case REVENANT_OF_TANTOS_CHIEF:
				if (st.getCond() == 9)
				{
					if (npc.isScriptValue(0))
					{
						npc.broadcastNpcSay(NpcStringId.ID_22055);
						npc.setScriptValue(1);
					}
					else if (npc.isScriptValue(1) && npc.getStatus().getHpRatio() < 0.33)
					{
						npc.broadcastNpcSay(NpcStringId.ID_22057);
						npc.setScriptValue(2);
					}
				}
				break;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int cond = st.getCond();
		
		switch (npc.getNpcId())
		{
			case TYRANT:
				if (cond == 1 && dropItems(player, TYRANT_TALON, 1, 10, 500000) && player.getInventory().getItemCount(GUARDIAN_BASILISK_FANG) + player.getInventory().getItemCount(MANASHEN_SHARD) == 20)
					st.setCond(2);
				break;
			
			case GUARDIAN_BASILISK:
				if (cond == 1 && dropItems(player, GUARDIAN_BASILISK_FANG, 1, 10, 500000) && player.getInventory().getItemCount(TYRANT_TALON) + player.getInventory().getItemCount(MANASHEN_SHARD) == 20)
					st.setCond(2);
				break;
			
			case MANASHEN_GARGOYLE:
				if (cond == 1 && dropItems(player, MANASHEN_SHARD, 1, 10, 750000) && player.getInventory().getItemCount(TYRANT_TALON) + player.getInventory().getItemCount(GUARDIAN_BASILISK_FANG) == 20)
					st.setCond(2);
				break;
			
			case MARSH_STAKATO_DRONE:
				if (player.getInventory().hasItem(DRIKO_CONTRACT))
					dropItems(player, STAKATO_DRONE_HUSK, 1, 30, 750000);
				break;
			
			case PASHIKA_SON_OF_VOLTAR:
				if (player.getInventory().hasItem(GLOVE_OF_VOLTAR) && !player.getInventory().hasItem(PASHIKA_HEAD))
				{
					giveItems(player, PASHIKA_HEAD, 1);
					if (player.getInventory().hasItem(VULTUS_HEAD))
					{
						playSound(player, SOUND_MIDDLE);
						takeItems(player, GLOVE_OF_VOLTAR, 1);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case VULTUS_SON_OF_VOLTAR:
				if (player.getInventory().hasItem(GLOVE_OF_VOLTAR) && !player.getInventory().hasItem(VULTUS_HEAD))
				{
					giveItems(player, VULTUS_HEAD, 1);
					if (player.getInventory().hasItem(PASHIKA_HEAD))
					{
						playSound(player, SOUND_MIDDLE);
						takeItems(player, GLOVE_OF_VOLTAR, 1);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case ENKU_ORC_OVERLORD:
				if (player.getInventory().hasItem(GLOVE_OF_KEPRA) && dropItemsAlways(player, ENKU_OVERLORD_HEAD, 1, 4))
					takeItems(player, GLOVE_OF_KEPRA, 1);
				break;
			
			case MAKUM_BUGBEAR_THUG:
				if (player.getInventory().hasItem(GLOVE_OF_BURAI) && dropItemsAlways(player, MAKUM_BUGBEAR_HEAD, 1, 2))
					takeItems(player, GLOVE_OF_BURAI, 1);
				break;
			
			case TIMAK_ORC, TIMAK_ORC_ARCHER, TIMAK_ORC_SOLDIER, TIMAK_ORC_WARRIOR, TIMAK_ORC_SHAMAN, TIMAK_ORC_OVERLORD:
				if (cond == 6 && dropItems(player, TIMAK_ORC_HEAD, 1, 20, 500000 + ((npc.getNpcId() - 20583) * 100000)) && player.getInventory().getItemCount(TAMLIN_ORC_SKULL) == 20)
					st.setCond(7);
				break;
			
			case TAMLIN_ORC:
				if (cond == 6 && dropItems(player, TAMLIN_ORC_SKULL, 1, 20, 500000) && player.getInventory().getItemCount(TIMAK_ORC_HEAD) == 20)
					st.setCond(7);
				break;
			
			case TAMLIN_ORC_ARCHER:
				if (cond == 6 && dropItems(player, TAMLIN_ORC_SKULL, 1, 20, 600000) && player.getInventory().getItemCount(TIMAK_ORC_HEAD) == 20)
					st.setCond(7);
				break;
			
			case RAGNA_ORC_OVERLORD, RAGNA_ORC_SEER:
				if (cond == 9)
				{
					npc.broadcastNpcSay(NpcStringId.ID_22052);
					createOnePrivateEx(npc, REVENANT_OF_TANTOS_CHIEF, npc.getX(), npc.getY(), npc.getZ(), 0, 200000, false);
				}
				break;
			
			case REVENANT_OF_TANTOS_CHIEF:
				if (cond == 9 && dropItemsAlways(player, SCEPTER_OF_TANTOS, 1, 1))
				{
					st.setCond(10);
					npc.broadcastNpcSay(NpcStringId.ID_22056);
				}
				break;
		}
	}
}