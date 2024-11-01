package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Q348_AnArrogantSearch extends Quest
{
	private static final String QUEST_NAME = "Q348_AnArrogantSearch";
	
	// Items
	private static final int TITAN_POWERSTONE = 4287;
	private static final int HANELLIN_FIRST_LETTER = 4288;
	private static final int HANELLIN_SECOND_LETTER = 4289;
	private static final int HANELLIN_THIRD_LETTER = 4290;
	private static final int FIRST_KEY_OF_ARK = 4291;
	private static final int SECOND_KEY_OF_ARK = 4292;
	private static final int THIRD_KEY_OF_ARK = 4293;
	private static final int WHITE_FABRIC = 4294;
	private static final int BLOODED_FABRIC = 4295;
	private static final int BOOK_OF_SAINT = 4397;
	private static final int BLOOD_OF_SAINT = 4398;
	private static final int BOUGH_OF_SAINT = 4399;
	
	private static final int ANTIDOTE = 1831;
	private static final int HEALING_POTION = 1061;
	
	// NPCs
	private static final int HANELLIN = 30864;
	private static final int CLAUDIA_ATHEBALDT = 31001;
	private static final int MARTIEN = 30645;
	private static final int HARNE = 30144;
	private static final int ARK_GUARDIAN_CORPSE = 30980;
	private static final int HOLY_ARK_OF_SECRECY_1 = 30977;
	private static final int HOLY_ARK_OF_SECRECY_2 = 30978;
	private static final int HOLY_ARK_OF_SECRECY_3 = 30979;
	private static final int GUSTAV_ATHEBALDT = 30760;
	private static final int HARDIN = 30832;
	private static final int IASON_HEINE = 30969;
	
	// Monsters
	private static final int LESSER_GIANT_MAGE = 20657;
	private static final int LESSER_GIANT_ELDER = 20658;
	private static final int PLATINUM_TRIBE_SHAMAN = 20828;
	private static final int PLATINUM_TRIBE_OVERLORD = 20829;
	private static final int GUARDIAN_ANGEL = 20859;
	private static final int SEAL_ANGEL = 20860;
	
	// Quest Monsters
	private static final int ANGEL_KILLER = 27184;
	private static final int ARK_GUARDIAN_ELBEROTH = 27182;
	private static final int ARK_GUARDIAN_SHADOW_FANG = 27183;
	
	// Locations
	private static final Location HOLY_ARK_1_LOC = new Location(-418, 44174, -3568);
	private static final Location HOLY_ARK_2_LOC = new Location(181472, 7158, -2725);
	private static final Location HOLY_ARK_3_LOC = new Location(50693, 158674, 376);
	private static final Location GUARDIAN_CORPSE_LOC = new Location(-2908, 44128, -2712);
	
	// NPCs instances, in order to avoid infinite instances creation speaking to chests.
	private Npc _elberoth;
	private Npc _shadowFang;
	private Npc _angelKiller;
	
	public Q348_AnArrogantSearch()
	{
		super(348, "An Arrogant Search");
		
		setItemsIds(TITAN_POWERSTONE, HANELLIN_FIRST_LETTER, HANELLIN_SECOND_LETTER, HANELLIN_THIRD_LETTER, FIRST_KEY_OF_ARK, SECOND_KEY_OF_ARK, THIRD_KEY_OF_ARK, BOOK_OF_SAINT, BLOOD_OF_SAINT, BOUGH_OF_SAINT, WHITE_FABRIC);
		
		addQuestStart(HANELLIN);
		addTalkId(HANELLIN, CLAUDIA_ATHEBALDT, MARTIEN, HARNE, HOLY_ARK_OF_SECRECY_1, HOLY_ARK_OF_SECRECY_2, HOLY_ARK_OF_SECRECY_3, ARK_GUARDIAN_CORPSE, GUSTAV_ATHEBALDT, HARDIN, IASON_HEINE);
		
		addAttacked(ARK_GUARDIAN_ELBEROTH, ARK_GUARDIAN_SHADOW_FANG, ANGEL_KILLER, PLATINUM_TRIBE_SHAMAN, PLATINUM_TRIBE_OVERLORD);
		addCreated(ARK_GUARDIAN_ELBEROTH, ARK_GUARDIAN_SHADOW_FANG, ANGEL_KILLER);
		addDecayed(ARK_GUARDIAN_ELBEROTH, ARK_GUARDIAN_SHADOW_FANG, ANGEL_KILLER);
		addMyDying(LESSER_GIANT_MAGE, LESSER_GIANT_ELDER, ARK_GUARDIAN_ELBEROTH, ARK_GUARDIAN_SHADOW_FANG, PLATINUM_TRIBE_SHAMAN, PLATINUM_TRIBE_OVERLORD, GUARDIAN_ANGEL, SEAL_ANGEL);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30864-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.setCond(2);
			st.set("points", 0);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30864-09.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, TITAN_POWERSTONE, 1);
		}
		else if (event.equalsIgnoreCase("30864-17.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, HANELLIN_FIRST_LETTER, 1);
			giveItems(player, HANELLIN_SECOND_LETTER, 1);
			giveItems(player, HANELLIN_THIRD_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30864-36.htm"))
		{
			st.setCond(24);
			playSound(player, SOUND_MIDDLE);
			rewardItems(player, 57, Rnd.get(1, 2) * 12000);
		}
		else if (event.equalsIgnoreCase("30864-37.htm"))
		{
			st.setCond(25);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30864-51.htm"))
		{
			st.setCond(26);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, WHITE_FABRIC, (player.getInventory().hasItem(BLOODED_FABRIC)) ? 9 : 10);
		}
		else if (event.equalsIgnoreCase("30864-58.htm"))
		{
			st.setCond(27);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30864-57.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30864-56.htm"))
		{
			st.setCond(29);
			st.unset("gustav");
			st.unset("hardin");
			st.unset("iason");
			playSound(player, SOUND_MIDDLE);
			giveItems(player, WHITE_FABRIC, 10);
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
				if (player.getInventory().hasItem(BLOODED_FABRIC))
					htmltext = "30864-00.htm";
				else if (player.getStatus().getLevel() < 60)
					htmltext = "30864-01.htm";
				else
					htmltext = "30864-02.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case HANELLIN:
						if (cond == 1)
							htmltext = "30864-02.htm";
						else if (cond == 2)
							htmltext = (!player.getInventory().hasItem(TITAN_POWERSTONE)) ? "30864-06.htm" : "30864-07.htm";
						else if (cond == 4)
							htmltext = "30864-09.htm";
						else if (cond > 4 && cond < 21)
							htmltext = (player.getInventory().hasAtLeastOneItem(BOOK_OF_SAINT, BLOOD_OF_SAINT, BOUGH_OF_SAINT)) ? "30864-28.htm" : "30864-24.htm";
						else if (cond == 21)
						{
							htmltext = "30864-29.htm";
							st.setCond(22);
							takeItems(player, BOOK_OF_SAINT, 1);
							takeItems(player, BLOOD_OF_SAINT, 1);
							takeItems(player, BOUGH_OF_SAINT, 1);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 22)
						{
							if (player.getInventory().hasItem(WHITE_FABRIC))
								htmltext = "30864-31.htm";
							else if (player.getInventory().getItemCount(ANTIDOTE) < 5 || !player.getInventory().hasItem(HEALING_POTION))
								htmltext = "30864-30.htm";
							else
							{
								htmltext = "30864-31.htm";
								takeItems(player, ANTIDOTE, 5);
								takeItems(player, HEALING_POTION, 1);
								giveItems(player, WHITE_FABRIC, 1);
								playSound(player, SOUND_ITEMGET);
							}
						}
						else if (cond == 24)
							htmltext = "30864-38.htm";
						else if (cond == 25)
						{
							if (player.getInventory().hasItem(WHITE_FABRIC))
								htmltext = "30864-39.htm";
							else if (player.getInventory().hasItem(BLOODED_FABRIC))
								htmltext = "30864-49.htm";
							// Use the only fabric on Baium, drop the quest.
							else
							{
								playSound(player, SOUND_FINISH);
								st.exitQuest(true);
							}
						}
						else if (cond == 26)
						{
							final int count = player.getInventory().getItemCount(BLOODED_FABRIC);
							
							if (count + player.getInventory().getItemCount(WHITE_FABRIC) < 10)
							{
								htmltext = "30864-54.htm";
								takeItems(player, BLOODED_FABRIC, -1);
								rewardItems(player, 57, (1000 * count) + 4000);
								st.exitQuest(true);
							}
							else if (count < 10)
								htmltext = "30864-52.htm";
							else if (count >= 10)
								htmltext = "30864-53.htm";
						}
						else if (cond == 27)
						{
							if (st.getInteger("gustav") + st.getInteger("hardin") + st.getInteger("iason") == 3)
							{
								htmltext = "30864-60.htm";
								st.setCond(28);
								rewardItems(player, 57, 49000);
								playSound(player, SOUND_MIDDLE);
							}
							else if (player.getInventory().hasItem(BLOODED_FABRIC) && st.getInteger("usedonbaium") != 1)
								htmltext = "30864-59.htm";
							else
							{
								htmltext = "30864-61.htm";
								playSound(player, SOUND_FINISH);
								st.exitQuest(true);
							}
						}
						else if (cond == 28)
							htmltext = "30864-55.htm";
						else if (cond == 29)
						{
							final int count = player.getInventory().getItemCount(BLOODED_FABRIC);
							
							if (count + player.getInventory().getItemCount(WHITE_FABRIC) < 10)
							{
								htmltext = "30864-54.htm";
								takeItems(player, BLOODED_FABRIC, -1);
								rewardItems(player, 57, 5000 * count);
								playSound(player, SOUND_FINISH);
								st.exitQuest(true);
							}
							else if (count < 10)
								htmltext = "30864-52.htm";
							else if (count >= 10)
								htmltext = "30864-53.htm";
						}
						break;
					
					case GUSTAV_ATHEBALDT:
						if (cond == 27)
						{
							if (player.getInventory().getItemCount(BLOODED_FABRIC) >= 3 && st.getInteger("gustav") == 0)
							{
								st.set("gustav", 1);
								htmltext = "30760-01.htm";
								takeItems(player, BLOODED_FABRIC, 3);
							}
							else if (st.getInteger("gustav") == 1)
								htmltext = "30760-02.htm";
							else
							{
								htmltext = "30760-03.htm";
								st.set("usedonbaium", 1);
							}
						}
						break;
					
					case HARDIN:
						if (cond == 27)
						{
							if (player.getInventory().hasItem(BLOODED_FABRIC) && st.getInteger("hardin") == 0)
							{
								st.set("hardin", 1);
								htmltext = "30832-01.htm";
								takeItems(player, BLOODED_FABRIC, 1);
							}
							else if (st.getInteger("hardin") == 1)
								htmltext = "30832-02.htm";
							else
							{
								htmltext = "30832-03.htm";
								st.set("usedonbaium", 1);
							}
						}
						break;
					
					case IASON_HEINE:
						if (cond == 27)
						{
							if (player.getInventory().getItemCount(BLOODED_FABRIC) >= 6 && st.getInteger("iason") == 0)
							{
								st.set("iason", 1);
								htmltext = "30969-01.htm";
								takeItems(player, BLOODED_FABRIC, 6);
							}
							else if (st.getInteger("iason") == 1)
								htmltext = "30969-02.htm";
							else
							{
								htmltext = "30969-03.htm";
								st.set("usedonbaium", 1);
							}
						}
						break;
					
					case HARNE:
						if (cond >= 5 && cond <= 22)
						{
							if (!player.getInventory().hasItem(BLOOD_OF_SAINT))
							{
								if (player.getInventory().hasItem(HANELLIN_FIRST_LETTER))
								{
									htmltext = "30144-01.htm";
									st.setCond(17);
									playSound(player, SOUND_MIDDLE);
									takeItems(player, HANELLIN_FIRST_LETTER, 1);
									player.getRadarList().addMarker(GUARDIAN_CORPSE_LOC);
								}
								else if (!player.getInventory().hasItem(FIRST_KEY_OF_ARK))
								{
									htmltext = "30144-03.htm";
									player.getRadarList().addMarker(GUARDIAN_CORPSE_LOC);
								}
								else
									htmltext = "30144-04.htm";
							}
							else
								htmltext = "30144-05.htm";
						}
						break;
					
					case CLAUDIA_ATHEBALDT:
						if (cond >= 5 && cond <= 22)
						{
							if (!player.getInventory().hasItem(BOOK_OF_SAINT))
							{
								if (player.getInventory().hasItem(HANELLIN_SECOND_LETTER))
								{
									htmltext = "31001-01.htm";
									st.setCond(9);
									playSound(player, SOUND_MIDDLE);
									takeItems(player, HANELLIN_SECOND_LETTER, 1);
									player.getRadarList().addMarker(HOLY_ARK_2_LOC);
								}
								else if (!player.getInventory().hasItem(SECOND_KEY_OF_ARK))
								{
									htmltext = "31001-03.htm";
									player.getRadarList().addMarker(HOLY_ARK_2_LOC);
								}
								else
									htmltext = "31001-04.htm";
							}
							else
								htmltext = "31001-05.htm";
						}
						break;
					
					case MARTIEN:
						if (cond >= 5 && cond <= 22)
						{
							if (!player.getInventory().hasItem(BOUGH_OF_SAINT))
							{
								if (player.getInventory().hasItem(HANELLIN_THIRD_LETTER))
								{
									htmltext = "30645-01.htm";
									st.setCond(13);
									playSound(player, SOUND_MIDDLE);
									takeItems(player, HANELLIN_THIRD_LETTER, 1);
									player.getRadarList().addMarker(HOLY_ARK_3_LOC);
								}
								else if (!player.getInventory().hasItem(THIRD_KEY_OF_ARK))
								{
									htmltext = "30645-03.htm";
									player.getRadarList().addMarker(HOLY_ARK_3_LOC);
								}
								else
									htmltext = "30645-04.htm";
							}
							else
								htmltext = "30645-05.htm";
						}
						break;
					
					case ARK_GUARDIAN_CORPSE:
						if (!player.getInventory().hasItem(HANELLIN_FIRST_LETTER) && cond >= 5 && cond <= 22)
						{
							if (!player.getInventory().hasItem(FIRST_KEY_OF_ARK) && !player.getInventory().hasItem(BLOOD_OF_SAINT))
							{
								if (st.getInteger("angelkiller") == 0)
								{
									htmltext = "30980-01.htm";
									if (_angelKiller == null)
										_angelKiller = addSpawn(ANGEL_KILLER, npc, true, 600000, true);
									
									if (st.getCond() != 18)
									{
										st.setCond(18);
										playSound(player, SOUND_MIDDLE);
									}
								}
								else
								{
									htmltext = "30980-02.htm";
									giveItems(player, FIRST_KEY_OF_ARK, 1);
									playSound(player, SOUND_ITEMGET);
									
									player.getRadarList().addMarker(HOLY_ARK_1_LOC);
									
									st.unset("angelkiller");
								}
							}
							else
								htmltext = "30980-03.htm";
						}
						break;
					
					case HOLY_ARK_OF_SECRECY_1:
						if (!player.getInventory().hasItem(HANELLIN_FIRST_LETTER) && cond >= 5 && cond <= 22)
						{
							if (!player.getInventory().hasItem(BLOOD_OF_SAINT))
							{
								if (player.getInventory().hasItem(FIRST_KEY_OF_ARK))
								{
									htmltext = "30977-02.htm";
									st.setCond(20);
									playSound(player, SOUND_MIDDLE);
									
									takeItems(player, FIRST_KEY_OF_ARK, 1);
									giveItems(player, BLOOD_OF_SAINT, 1);
									
									if (player.getInventory().hasItems(BOOK_OF_SAINT, BOUGH_OF_SAINT))
										st.setCond(21);
								}
								else
									htmltext = "30977-04.htm";
							}
							else
								htmltext = "30977-03.htm";
						}
						break;
					
					case HOLY_ARK_OF_SECRECY_2:
						if (!player.getInventory().hasItem(HANELLIN_SECOND_LETTER) && cond >= 5 && cond <= 22)
						{
							if (!player.getInventory().hasItem(BOOK_OF_SAINT))
							{
								if (!player.getInventory().hasItem(SECOND_KEY_OF_ARK))
								{
									htmltext = "30978-01.htm";
									if (_elberoth == null)
										_elberoth = addSpawn(ARK_GUARDIAN_ELBEROTH, npc, true, 600000, true);
								}
								else
								{
									htmltext = "30978-02.htm";
									st.setCond(12);
									playSound(player, SOUND_MIDDLE);
									
									takeItems(player, SECOND_KEY_OF_ARK, 1);
									giveItems(player, BOOK_OF_SAINT, 1);
									
									if (player.getInventory().hasItems(BLOOD_OF_SAINT, BOUGH_OF_SAINT))
										st.setCond(21);
								}
							}
							else
								htmltext = "30978-03.htm";
						}
						break;
					
					case HOLY_ARK_OF_SECRECY_3:
						if (!player.getInventory().hasItem(HANELLIN_THIRD_LETTER) && cond >= 5 && cond <= 22)
						{
							if (!player.getInventory().hasItem(BOUGH_OF_SAINT))
							{
								if (!player.getInventory().hasItem(THIRD_KEY_OF_ARK))
								{
									htmltext = "30979-01.htm";
									if (_shadowFang == null)
										_shadowFang = addSpawn(ARK_GUARDIAN_SHADOW_FANG, npc, true, 600000, true);
								}
								else
								{
									htmltext = "30979-02.htm";
									st.setCond(16);
									playSound(player, SOUND_MIDDLE);
									
									takeItems(player, THIRD_KEY_OF_ARK, 1);
									giveItems(player, BOUGH_OF_SAINT, 1);
									
									if (player.getInventory().hasItems(BLOOD_OF_SAINT, BOOK_OF_SAINT))
										st.setCond(21);
								}
							}
							else
								htmltext = "30979-03.htm";
						}
						break;
				}
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
			case ARK_GUARDIAN_ELBEROTH:
				if (npc.getScriptValue() == 0)
				{
					npc.broadcastNpcSay(NpcStringId.ID_34833);
					npc.setScriptValue(1);
				}
				break;
			
			case ARK_GUARDIAN_SHADOW_FANG:
				if (npc.getScriptValue() == 0)
				{
					npc.broadcastNpcSay(NpcStringId.ID_34836);
					npc.setScriptValue(1);
				}
				break;
			
			case ANGEL_KILLER:
				if (st.getInteger("angelkiller") == 1 || player.getInventory().hasAtLeastOneItem(FIRST_KEY_OF_ARK, BLOOD_OF_SAINT))
				{
					npc.getAttack().stop();
					npc.broadcastNpcSay(NpcStringId.ID_34839);
					npc.deleteMe();
				}
				else if (npc.getStatus().getHpRatio() < 0.3)
				{
					npc.getAttack().stop();
					npc.broadcastNpcSay(NpcStringId.ID_34830);
					npc.deleteMe();
					
					st.setCond(19);
					st.set("angelkiller", 1);
					playSound(player, SOUND_MIDDLE);
					
					player.getRadarList().addMarker(GUARDIAN_CORPSE_LOC);
				}
				break;
			
			case PLATINUM_TRIBE_OVERLORD, PLATINUM_TRIBE_SHAMAN:
				final int cond = st.getCond();
				if ((cond == 24 || cond == 25) && player.getInventory().hasItem(WHITE_FABRIC))
				{
					final int points = st.getInteger("points") + ((npc.getNpcId() == PLATINUM_TRIBE_SHAMAN) ? 60 : 70);
					if (points > ((cond == 24) ? 80000 : 100000))
					{
						st.set("points", 0);
						
						takeItems(player, WHITE_FABRIC, 1);
						giveItems(player, BLOODED_FABRIC, 1);
						
						if (cond != 24)
							playSound(player, SOUND_ITEMGET);
						else
						{
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
					}
					else
						st.set("points", points);
				}
				break;
		}
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		switch (npc.getNpcId())
		{
			case ARK_GUARDIAN_ELBEROTH:
				npc.broadcastNpcSay(NpcStringId.ID_34837);
				break;
			
			case ARK_GUARDIAN_SHADOW_FANG:
				npc.broadcastNpcSay(NpcStringId.ID_34838);
				break;
			
			case ANGEL_KILLER:
				npc.broadcastNpcSay(NpcStringId.ID_34831);
				break;
		}
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		if (npc == _elberoth)
		{
			_elberoth = null;
		}
		else if (npc == _shadowFang)
		{
			_shadowFang = null;
		}
		else if (npc == _angelKiller)
		{
			_angelKiller = null;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int cond = st.getCond();
		
		switch (npc.getNpcId())
		{
			case LESSER_GIANT_ELDER, LESSER_GIANT_MAGE:
				if (cond == 2)
					dropItems(player, TITAN_POWERSTONE, 1, 1, 100000);
				break;
			
			case ARK_GUARDIAN_ELBEROTH:
				if (cond >= 5 && cond <= 22 && !player.getInventory().hasItem(SECOND_KEY_OF_ARK))
				{
					st.setCond(11);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, SECOND_KEY_OF_ARK, 1);
					npc.broadcastNpcSay(NpcStringId.ID_34832);
				}
				break;
			
			case ARK_GUARDIAN_SHADOW_FANG:
				if (cond >= 5 && cond <= 22 && !player.getInventory().hasItem(THIRD_KEY_OF_ARK))
				{
					st.setCond(15);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, THIRD_KEY_OF_ARK, 1);
					npc.broadcastNpcSay(NpcStringId.ID_34835);
				}
				break;
			
			case PLATINUM_TRIBE_OVERLORD, PLATINUM_TRIBE_SHAMAN:
				if ((cond == 24 || cond == 25) && player.getInventory().hasItem(WHITE_FABRIC))
				{
					final int points = st.getInteger("points") + ((npc.getNpcId() == PLATINUM_TRIBE_SHAMAN) ? 600 : 700);
					if (points > ((cond == 24) ? 80000 : 100000))
					{
						st.set("points", 0);
						
						takeItems(player, WHITE_FABRIC, 1);
						giveItems(player, BLOODED_FABRIC, 1);
						
						if (cond != 24)
							playSound(player, SOUND_ITEMGET);
						else
						{
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
					}
					else
						st.set("points", points);
				}
				break;
			
			case SEAL_ANGEL, GUARDIAN_ANGEL:
				if ((cond == 26 || cond == 29) && Rnd.get(4) < 1 && player.getInventory().hasItem(WHITE_FABRIC))
				{
					playSound(player, SOUND_ITEMGET);
					takeItems(player, WHITE_FABRIC, 1);
					giveItems(player, BLOODED_FABRIC, 1);
				}
				break;
		}
	}
}