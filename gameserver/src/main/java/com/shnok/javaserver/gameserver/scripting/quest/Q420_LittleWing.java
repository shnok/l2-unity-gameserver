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

public class Q420_LittleWing extends Quest
{
	private static final String QUEST_NAME = "Q420_LittleWing";
	
	// Needed items
	private static final int COAL = 1870;
	private static final int CHARCOAL = 1871;
	private static final int SILVER_NUGGET = 1873;
	private static final int STONE_OF_PURITY = 1875;
	private static final int GEMSTONE_D = 2130;
	private static final int GEMSTONE_C = 2131;
	
	// Items
	private static final int FAIRY_DUST = 3499;
	
	private static final int FAIRY_STONE = 3816;
	private static final int DELUXE_FAIRY_STONE = 3817;
	private static final int FAIRY_STONE_LIST = 3818;
	private static final int DELUXE_FAIRY_STONE_LIST = 3819;
	private static final int TOAD_LORD_BACK_SKIN = 3820;
	private static final int JUICE_OF_MONKSHOOD = 3821;
	private static final int SCALE_OF_DRAKE_EXARION = 3822;
	private static final int EGG_OF_DRAKE_EXARION = 3823;
	private static final int SCALE_OF_DRAKE_ZWOV = 3824;
	private static final int EGG_OF_DRAKE_ZWOV = 3825;
	private static final int SCALE_OF_DRAKE_KALIBRAN = 3826;
	private static final int EGG_OF_DRAKE_KALIBRAN = 3827;
	private static final int SCALE_OF_WYVERN_SUZET = 3828;
	private static final int EGG_OF_WYVERN_SUZET = 3829;
	private static final int SCALE_OF_WYVERN_SHAMHAI = 3830;
	private static final int EGG_OF_WYVERN_SHAMHAI = 3831;
	
	// Rewards
	private static final int DRAGONFLUTE_OF_WIND = 3500;
	private static final int DRAGONFLUTE_OF_STAR = 3501;
	private static final int DRAGONFLUTE_OF_TWILIGHT = 3502;
	private static final int HATCHLING_SOFT_LEATHER = 3912;
	private static final int FOOD_FOR_HATCHLING = 4038;
	
	// NPCs
	private static final int MARIA = 30608;
	private static final int CRONOS = 30610;
	private static final int BYRON = 30711;
	private static final int MIMYU = 30747;
	private static final int EXARION = 30748;
	private static final int ZWOV = 30749;
	private static final int KALIBRAN = 30750;
	private static final int SUZET = 30751;
	private static final int SHAMHAI = 30752;
	private static final int COOPER = 30829;
	
	// Spawn Points
	private static final Location[] MIMYU_LOCS =
	{
		new Location(109816, 40854, -4640),
		new Location(108940, 41615, -4643),
		new Location(110395, 41625, -4642)
	};
	
	private int _counter = 0;
	
	public Q420_LittleWing()
	{
		super(420, "Little Wing");
		
		setItemsIds(FAIRY_STONE, DELUXE_FAIRY_STONE, FAIRY_STONE_LIST, DELUXE_FAIRY_STONE_LIST, TOAD_LORD_BACK_SKIN, JUICE_OF_MONKSHOOD, SCALE_OF_DRAKE_EXARION, EGG_OF_DRAKE_EXARION, SCALE_OF_DRAKE_ZWOV, EGG_OF_DRAKE_ZWOV, SCALE_OF_DRAKE_KALIBRAN, EGG_OF_DRAKE_KALIBRAN, SCALE_OF_WYVERN_SUZET, EGG_OF_WYVERN_SUZET, SCALE_OF_WYVERN_SHAMHAI, EGG_OF_WYVERN_SHAMHAI);
		
		addQuestStart(COOPER, MIMYU);
		addTalkId(MARIA, CRONOS, BYRON, MIMYU, EXARION, ZWOV, KALIBRAN, SUZET, SHAMHAI, COOPER);
		
		addMyDying(20202, 20231, 20233, 20270, 20551, 20580, 20589, 20590, 20591, 20592, 20593, 20594, 20595, 20596, 20597, 20598, 20599);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// COOPER
		if (event.equalsIgnoreCase("30829-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		// CRONOS
		else if (event.equalsIgnoreCase("30610-05.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, FAIRY_STONE_LIST, 1);
		}
		else if (event.equalsIgnoreCase("30610-06.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, DELUXE_FAIRY_STONE_LIST, 1);
		}
		else if (event.equalsIgnoreCase("30610-12.htm"))
		{
			st.setCond(2);
			st.set("deluxestone", 1);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, FAIRY_STONE_LIST, 1);
		}
		else if (event.equalsIgnoreCase("30610-13.htm"))
		{
			st.setCond(2);
			st.set("deluxestone", 1);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, DELUXE_FAIRY_STONE_LIST, 1);
		}
		// MARIA
		else if (event.equalsIgnoreCase("30608-03.htm"))
		{
			if (!checkItems(player, false))
				htmltext = "30608-01.htm"; // Avoid to continue while trade or drop mats before clicking bypass
			else
			{
				takeItems(player, COAL, 10);
				takeItems(player, CHARCOAL, 10);
				takeItems(player, GEMSTONE_D, 1);
				takeItems(player, SILVER_NUGGET, 3);
				takeItems(player, TOAD_LORD_BACK_SKIN, -1);
				takeItems(player, FAIRY_STONE_LIST, 1);
				giveItems(player, FAIRY_STONE, 1);
			}
		}
		else if (event.equalsIgnoreCase("30608-05.htm"))
		{
			if (!checkItems(player, true))
				htmltext = "30608-01.htm"; // Avoid to continue while trade or drop mats before clicking bypass
			else
			{
				takeItems(player, COAL, 10);
				takeItems(player, CHARCOAL, 10);
				takeItems(player, GEMSTONE_C, 1);
				takeItems(player, STONE_OF_PURITY, 1);
				takeItems(player, SILVER_NUGGET, 5);
				takeItems(player, TOAD_LORD_BACK_SKIN, -1);
				takeItems(player, DELUXE_FAIRY_STONE_LIST, 1);
				giveItems(player, DELUXE_FAIRY_STONE, 1);
			}
		}
		// BYRON
		else if (event.equalsIgnoreCase("30711-03.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			if (player.getInventory().hasItem(DELUXE_FAIRY_STONE))
				htmltext = "30711-04.htm";
		}
		// MIMYU
		else if (event.equalsIgnoreCase("30747-02.htm"))
		{
			st.set("mimyu", 1);
			takeItems(player, FAIRY_STONE, 1);
		}
		else if (event.equalsIgnoreCase("30747-04.htm"))
		{
			st.set("mimyu", 1);
			takeItems(player, DELUXE_FAIRY_STONE, 1);
			giveItems(player, FAIRY_DUST, 1);
		}
		else if (event.equalsIgnoreCase("30747-07.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, JUICE_OF_MONKSHOOD, 1);
		}
		else if (event.equalsIgnoreCase("30747-12.htm") && !player.getInventory().hasItem(FAIRY_DUST))
		{
			htmltext = "30747-15.htm";
			giveRandomPet(player, false);
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30747-13.htm"))
		{
			giveRandomPet(player, player.getInventory().hasItem(FAIRY_DUST));
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30747-14.htm"))
		{
			if (player.getInventory().hasItem(FAIRY_DUST))
			{
				takeItems(player, FAIRY_DUST, 1);
				giveRandomPet(player, true);
				if (Rnd.get(20) == 1)
					giveItems(player, HATCHLING_SOFT_LEATHER, 1);
				else
				{
					htmltext = "30747-14t.htm";
					giveItems(player, FOOD_FOR_HATCHLING, 20);
				}
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "30747-13.htm";
		}
		// EXARION
		else if (event.equalsIgnoreCase("30748-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, JUICE_OF_MONKSHOOD, 1);
			giveItems(player, SCALE_OF_DRAKE_EXARION, 1);
		}
		// ZWOV
		else if (event.equalsIgnoreCase("30749-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, JUICE_OF_MONKSHOOD, 1);
			giveItems(player, SCALE_OF_DRAKE_ZWOV, 1);
		}
		// KALIBRAN
		else if (event.equalsIgnoreCase("30750-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, JUICE_OF_MONKSHOOD, 1);
			giveItems(player, SCALE_OF_DRAKE_KALIBRAN, 1);
		}
		else if (event.equalsIgnoreCase("30750-05.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, EGG_OF_DRAKE_KALIBRAN, 19);
			takeItems(player, SCALE_OF_DRAKE_KALIBRAN, 1);
		}
		// SUZET
		else if (event.equalsIgnoreCase("30751-03.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, JUICE_OF_MONKSHOOD, 1);
			giveItems(player, SCALE_OF_WYVERN_SUZET, 1);
		}
		// SHAMHAI
		else if (event.equalsIgnoreCase("30752-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, JUICE_OF_MONKSHOOD, 1);
			giveItems(player, SCALE_OF_WYVERN_SHAMHAI, 1);
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
				switch (npc.getNpcId())
				{
					case COOPER:
						htmltext = (player.getStatus().getLevel() >= 35) ? "30829-01.htm" : "30829-03.htm";
						break;
					
					case MIMYU:
						_counter += 1;
						npc.teleportTo(MIMYU_LOCS[_counter % 3], 0);
						return null;
				}
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case COOPER:
						htmltext = "30829-04.htm";
						break;
					
					case CRONOS:
						if (cond == 1)
							htmltext = "30610-01.htm";
						else if (st.getInteger("deluxestone") == 2)
							htmltext = "30610-10.htm";
						else if (cond == 2)
						{
							if (player.getInventory().hasAtLeastOneItem(FAIRY_STONE, DELUXE_FAIRY_STONE))
							{
								if (st.getInteger("deluxestone") == 1)
									htmltext = "30610-14.htm";
								else
								{
									htmltext = "30610-08.htm";
									st.setCond(3);
									playSound(player, SOUND_MIDDLE);
								}
							}
							else
								htmltext = "30610-07.htm";
						}
						else if (cond == 3)
							htmltext = "30610-09.htm";
						else if (cond == 4 && player.getInventory().hasAtLeastOneItem(FAIRY_STONE, DELUXE_FAIRY_STONE))
							htmltext = "30610-11.htm";
						break;
					
					case MARIA:
						if (player.getInventory().hasAtLeastOneItem(FAIRY_STONE, DELUXE_FAIRY_STONE))
							htmltext = "30608-06.htm";
						else if (cond == 2)
						{
							if (player.getInventory().hasItem(FAIRY_STONE_LIST))
								htmltext = (checkItems(player, false)) ? "30608-02.htm" : "30608-01.htm";
							else if (player.getInventory().hasItem(DELUXE_FAIRY_STONE_LIST))
								htmltext = (checkItems(player, true)) ? "30608-04.htm" : "30608-01.htm";
						}
						break;
					
					case BYRON:
						final int deluxestone = st.getInteger("deluxestone");
						if (deluxestone == 1)
						{
							if (player.getInventory().hasItem(FAIRY_STONE))
							{
								htmltext = "30711-05.htm";
								st.setCond(4);
								st.unset("deluxestone");
								playSound(player, SOUND_MIDDLE);
							}
							else if (player.getInventory().hasItem(DELUXE_FAIRY_STONE))
							{
								htmltext = "30711-06.htm";
								st.setCond(4);
								st.unset("deluxestone");
								playSound(player, SOUND_MIDDLE);
							}
							else
								htmltext = "30711-10.htm";
						}
						else if (deluxestone == 2)
							htmltext = "30711-09.htm";
						else if (cond == 3)
							htmltext = "30711-01.htm";
						else if (cond == 4)
						{
							if (player.getInventory().hasItem(FAIRY_STONE))
								htmltext = "30711-07.htm";
							else if (player.getInventory().hasItem(DELUXE_FAIRY_STONE))
								htmltext = "30711-08.htm";
						}
						break;
					
					case MIMYU:
						if (cond == 4)
						{
							if (st.getInteger("mimyu") == 1)
								htmltext = "30747-06.htm";
							else if (player.getInventory().hasItem(FAIRY_STONE))
								htmltext = "30747-01.htm";
							else if (player.getInventory().hasItem(DELUXE_FAIRY_STONE))
								htmltext = "30747-03.htm";
						}
						else if (cond == 5)
							htmltext = "30747-08.htm";
						else if (cond == 6)
						{
							final int eggs = player.getInventory().getItemCount(EGG_OF_DRAKE_EXARION) + player.getInventory().getItemCount(EGG_OF_DRAKE_ZWOV) + player.getInventory().getItemCount(EGG_OF_DRAKE_KALIBRAN) + player.getInventory().getItemCount(EGG_OF_WYVERN_SUZET) + player.getInventory().getItemCount(EGG_OF_WYVERN_SHAMHAI);
							if (eggs < 20)
								htmltext = "30747-09.htm";
							else
								htmltext = "30747-10.htm";
						}
						else if (cond == 7)
							htmltext = "30747-11.htm";
						else
						{
							_counter += 1;
							npc.teleportTo(MIMYU_LOCS[_counter % 3], 0);
							return null;
						}
						break;
					
					case EXARION:
						if (cond == 5)
							htmltext = "30748-01.htm";
						else if (cond == 6)
						{
							if (player.getInventory().getItemCount(EGG_OF_DRAKE_EXARION) < 20)
								htmltext = "30748-03.htm";
							else
							{
								htmltext = "30748-04.htm";
								st.setCond(7);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, EGG_OF_DRAKE_EXARION, 19);
								takeItems(player, SCALE_OF_DRAKE_EXARION, 1);
							}
						}
						else if (cond == 7)
							htmltext = "30748-05.htm";
						break;
					
					case ZWOV:
						if (cond == 5)
							htmltext = "30749-01.htm";
						else if (cond == 6)
						{
							if (player.getInventory().getItemCount(EGG_OF_DRAKE_ZWOV) < 20)
								htmltext = "30749-03.htm";
							else
							{
								htmltext = "30749-04.htm";
								st.setCond(7);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, EGG_OF_DRAKE_ZWOV, 19);
								takeItems(player, SCALE_OF_DRAKE_ZWOV, 1);
							}
						}
						else if (cond == 7)
							htmltext = "30749-05.htm";
						break;
					
					case KALIBRAN:
						if (cond == 5)
							htmltext = "30750-01.htm";
						else if (cond == 6)
							htmltext = (player.getInventory().getItemCount(EGG_OF_DRAKE_KALIBRAN) < 20) ? "30750-03.htm" : "30750-04.htm";
						else if (cond == 7)
							htmltext = "30750-06.htm";
						break;
					
					case SUZET:
						if (cond == 5)
							htmltext = "30751-01.htm";
						else if (cond == 6)
						{
							if (player.getInventory().getItemCount(EGG_OF_WYVERN_SUZET) < 20)
								htmltext = "30751-04.htm";
							else
							{
								htmltext = "30751-05.htm";
								st.setCond(7);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, EGG_OF_WYVERN_SUZET, 19);
								takeItems(player, SCALE_OF_WYVERN_SUZET, 1);
							}
						}
						else if (cond == 7)
							htmltext = "30751-06.htm";
						break;
					
					case SHAMHAI:
						if (cond == 5)
							htmltext = "30752-01.htm";
						else if (cond == 6)
						{
							if (player.getInventory().getItemCount(EGG_OF_WYVERN_SHAMHAI) < 20)
								htmltext = "30752-03.htm";
							else
							{
								htmltext = "30752-04.htm";
								st.setCond(7);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, EGG_OF_WYVERN_SHAMHAI, 19);
								takeItems(player, SCALE_OF_WYVERN_SHAMHAI, 1);
							}
						}
						else if (cond == 7)
							htmltext = "30752-05.htm";
						break;
				}
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
			case 20231:
				if (player.getInventory().hasItem(FAIRY_STONE_LIST))
					dropItems(player, TOAD_LORD_BACK_SKIN, 1, 10, 300000);
				else if (player.getInventory().hasItem(DELUXE_FAIRY_STONE_LIST))
					dropItems(player, TOAD_LORD_BACK_SKIN, 1, 20, 300000);
				break;
			
			case 20580:
				if (player.getInventory().hasItem(SCALE_OF_DRAKE_EXARION) && !dropItems(player, EGG_OF_DRAKE_EXARION, 1, 20, 500000))
					npc.broadcastNpcSay(NpcStringId.ID_42049);
				break;
			
			case 20233:
				if (player.getInventory().hasItem(SCALE_OF_DRAKE_ZWOV))
					dropItems(player, EGG_OF_DRAKE_ZWOV, 1, 20, 500000);
				break;
			
			case 20551:
				if (player.getInventory().hasItem(SCALE_OF_DRAKE_KALIBRAN) && !dropItems(player, EGG_OF_DRAKE_KALIBRAN, 1, 20, 500000))
					npc.broadcastNpcSay(NpcStringId.ID_42046);
				break;
			
			case 20270:
				if (player.getInventory().hasItem(SCALE_OF_WYVERN_SUZET) && !dropItems(player, EGG_OF_WYVERN_SUZET, 1, 20, 500000))
					npc.broadcastNpcSay(NpcStringId.ID_42047);
				break;
			
			case 20202:
				if (player.getInventory().hasItem(SCALE_OF_WYVERN_SHAMHAI))
					dropItems(player, EGG_OF_WYVERN_SHAMHAI, 1, 20, 500000);
				break;
			
			case 20589, 20590, 20591, 20592, 20593, 20594, 20595, 20596, 20597, 20598, 20599:
				if (player.getInventory().hasItem(DELUXE_FAIRY_STONE) && Rnd.get(100) < 30)
				{
					st.set("deluxestone", 2);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, DELUXE_FAIRY_STONE, 1);
					npc.broadcastNpcSay(NpcStringId.ID_42048);
				}
				break;
		}
	}
	
	private static boolean checkItems(Player player, boolean isDeluxe)
	{
		// Conditions required for both cases.
		if (player.getInventory().getItemCount(COAL) < 10 || player.getInventory().getItemCount(CHARCOAL) < 10)
			return false;
		
		if (isDeluxe)
		{
			if (player.getInventory().getItemCount(GEMSTONE_C) >= 1 && player.getInventory().getItemCount(SILVER_NUGGET) >= 5 && player.getInventory().getItemCount(STONE_OF_PURITY) >= 1 && player.getInventory().getItemCount(TOAD_LORD_BACK_SKIN) >= 20)
				return true;
		}
		else
		{
			if (player.getInventory().getItemCount(GEMSTONE_D) >= 1 && player.getInventory().getItemCount(SILVER_NUGGET) >= 3 && player.getInventory().getItemCount(TOAD_LORD_BACK_SKIN) >= 10)
				return true;
		}
		return false;
	}
	
	private static void giveRandomPet(Player player, boolean hasFairyDust)
	{
		int pet = DRAGONFLUTE_OF_TWILIGHT;
		int chance = Rnd.get(100);
		if (player.getInventory().hasItem(EGG_OF_DRAKE_EXARION))
		{
			takeItems(player, EGG_OF_DRAKE_EXARION, 1);
			if (hasFairyDust)
			{
				if (chance < 45)
					pet = DRAGONFLUTE_OF_WIND;
				else if (chance < 75)
					pet = DRAGONFLUTE_OF_STAR;
			}
			else if (chance < 50)
				pet = DRAGONFLUTE_OF_WIND;
			else if (chance < 85)
				pet = DRAGONFLUTE_OF_STAR;
		}
		else if (player.getInventory().hasItem(EGG_OF_WYVERN_SUZET))
		{
			takeItems(player, EGG_OF_WYVERN_SUZET, 1);
			if (hasFairyDust)
			{
				if (chance < 55)
					pet = DRAGONFLUTE_OF_WIND;
				else if (chance < 85)
					pet = DRAGONFLUTE_OF_STAR;
			}
			else if (chance < 65)
				pet = DRAGONFLUTE_OF_WIND;
			else if (chance < 95)
				pet = DRAGONFLUTE_OF_STAR;
		}
		else if (player.getInventory().hasItem(EGG_OF_DRAKE_KALIBRAN))
		{
			takeItems(player, EGG_OF_DRAKE_KALIBRAN, 1);
			if (hasFairyDust)
			{
				if (chance < 60)
					pet = DRAGONFLUTE_OF_WIND;
				else if (chance < 90)
					pet = DRAGONFLUTE_OF_STAR;
			}
			else if (chance < 70)
				pet = DRAGONFLUTE_OF_WIND;
			else
				pet = DRAGONFLUTE_OF_STAR;
		}
		else if (player.getInventory().hasItem(EGG_OF_WYVERN_SHAMHAI))
		{
			takeItems(player, EGG_OF_WYVERN_SHAMHAI, 1);
			if (hasFairyDust)
			{
				if (chance < 70)
					pet = DRAGONFLUTE_OF_WIND;
				else
					pet = DRAGONFLUTE_OF_STAR;
			}
			else if (chance < 85)
				pet = DRAGONFLUTE_OF_WIND;
			else
				pet = DRAGONFLUTE_OF_STAR;
		}
		else if (player.getInventory().hasItem(EGG_OF_DRAKE_ZWOV))
		{
			takeItems(player, EGG_OF_DRAKE_ZWOV, 1);
			if (hasFairyDust)
			{
				if (chance < 90)
					pet = DRAGONFLUTE_OF_WIND;
				else
					pet = DRAGONFLUTE_OF_STAR;
			}
			else
				pet = DRAGONFLUTE_OF_WIND;
		}
		
		giveItems(player, pet, 1);
	}
}