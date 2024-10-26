package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q417_PathToBecomeAScavenger extends Quest
{
	private static final String QUEST_NAME = "Q417_PathToBecomeAScavenger";
	
	// Items
	private static final int RING_OF_RAVEN = 1642;
	private static final int PIPPI_LETTER = 1643;
	private static final int RAUT_TELEPORT_SCROLL = 1644;
	private static final int SUCCUBUS_UNDIES = 1645;
	private static final int MION_LETTER = 1646;
	private static final int BRONK_INGOT = 1647;
	private static final int SHARI_AXE = 1648;
	private static final int ZIMENF_POTION = 1649;
	private static final int BRONK_PAY = 1650;
	private static final int SHARI_PAY = 1651;
	private static final int ZIMENF_PAY = 1652;
	private static final int BEAR_PICTURE = 1653;
	private static final int TARANTULA_PICTURE = 1654;
	private static final int HONEY_JAR = 1655;
	private static final int BEAD = 1656;
	private static final int BEAD_PARCEL_1 = 1657;
	private static final int BEAD_PARCEL_2 = 8543;
	
	// NPCs
	private static final int RAUT = 30316;
	private static final int SHARI = 30517;
	private static final int MION = 30519;
	private static final int PIPPI = 30524;
	private static final int BRONK = 30525;
	private static final int ZIMENF = 30538;
	private static final int TOMA = 30556;
	private static final int TORAI = 30557;
	private static final int YASHENI = 31958;
	
	// Monsters
	private static final int HUNTER_TARANTULA = 20403;
	private static final int PLUNDER_TARANTULA = 20508;
	private static final int HUNTER_BEAR = 20777;
	private static final int HONEY_BEAR = 27058;
	
	public Q417_PathToBecomeAScavenger()
	{
		super(417, "Path To Become A Scavenger");
		
		setItemsIds(PIPPI_LETTER, RAUT_TELEPORT_SCROLL, SUCCUBUS_UNDIES, MION_LETTER, BRONK_INGOT, SHARI_AXE, ZIMENF_POTION, BRONK_PAY, SHARI_PAY, ZIMENF_PAY, BEAR_PICTURE, TARANTULA_PICTURE, HONEY_JAR, BEAD, BEAD_PARCEL_1, BEAD_PARCEL_2);
		
		addQuestStart(PIPPI);
		addTalkId(RAUT, SHARI, MION, PIPPI, BRONK, ZIMENF, TOMA, TORAI, YASHENI);
		
		addMyDying(HUNTER_TARANTULA, PLUNDER_TARANTULA, HUNTER_BEAR, HONEY_BEAR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// PIPPI
		if (event.equalsIgnoreCase("30524-05.htm"))
		{
			if (player.getClassId() != ClassId.DWARVEN_FIGHTER)
				htmltext = (player.getClassId() == ClassId.SCAVENGER) ? "30524-02a.htm" : "30524-08.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30524-02.htm";
			else if (player.getInventory().hasItem(RING_OF_RAVEN))
				htmltext = "30524-04.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
				giveItems(player, PIPPI_LETTER, 1);
			}
		}
		// MION
		else if (event.equalsIgnoreCase("30519_1"))
		{
			final int random = Rnd.get(3);
			
			htmltext = "30519-0" + (random + 2) + ".htm";
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, PIPPI_LETTER, -1);
			giveItems(player, ZIMENF_POTION - random, 1);
		}
		else if (event.equalsIgnoreCase("30519_2"))
		{
			final int random = Rnd.get(3);
			
			htmltext = "30519-0" + (random + 2) + ".htm";
			takeItems(player, BRONK_PAY, -1);
			takeItems(player, SHARI_PAY, -1);
			takeItems(player, ZIMENF_PAY, -1);
			giveItems(player, ZIMENF_POTION - random, 1);
		}
		else if (event.equalsIgnoreCase("30519-07.htm"))
			st.set("id", st.getInteger("id") + 1);
		else if (event.equalsIgnoreCase("30519-09.htm"))
		{
			int id = st.getInteger("id");
			if (id / 10 < 2)
			{
				htmltext = "30519-07.htm";
				st.set("id", id + 1);
			}
			else if (id / 10 == 2)
				st.set("id", id + 1);
			else if (id / 10 >= 3)
			{
				htmltext = "30519-10.htm";
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, SHARI_AXE, -1);
				takeItems(player, ZIMENF_POTION, -1);
				takeItems(player, BRONK_INGOT, -1);
				giveItems(player, MION_LETTER, 1);
			}
		}
		else if (event.equalsIgnoreCase("30519-11.htm") && Rnd.nextBoolean())
			htmltext = "30519-06.htm";
		// TOMA
		else if (event.equalsIgnoreCase("30556-05b.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BEAD, -1);
			takeItems(player, TARANTULA_PICTURE, 1);
			giveItems(player, BEAD_PARCEL_1, 1);
		}
		else if (event.equalsIgnoreCase("30556-06b.htm"))
		{
			st.setCond(12);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BEAD, -1);
			takeItems(player, TARANTULA_PICTURE, 1);
			giveItems(player, BEAD_PARCEL_2, 1);
		}
		// RAUT
		else if (event.equalsIgnoreCase("30316-02.htm") || event.equalsIgnoreCase("30316-03.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BEAD_PARCEL_1, 1);
			giveItems(player, RAUT_TELEPORT_SCROLL, 1);
		}
		// TORAI
		else if (event.equalsIgnoreCase("30557-03.htm"))
		{
			st.setCond(11);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, RAUT_TELEPORT_SCROLL, 1);
			giveItems(player, SUCCUBUS_UNDIES, 1);
		}
		// YASHENI
		else if (event.equalsIgnoreCase("31958-02.htm"))
		{
			takeItems(player, BEAD_PARCEL_2, 1);
			giveItems(player, RING_OF_RAVEN, 1);
			rewardExpAndSp(player, 3200, 7080);
			player.broadcastPacket(new SocialAction(player, 3));
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
				htmltext = "30524-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case PIPPI:
						if (cond == 1)
							htmltext = "30524-06.htm";
						else if (cond > 1)
							htmltext = "30524-07.htm";
						break;
					
					case MION:
						if (player.getInventory().hasItem(PIPPI_LETTER))
							htmltext = "30519-01.htm";
						else if (player.getInventory().hasAtLeastOneItem(BRONK_INGOT, SHARI_AXE, ZIMENF_POTION))
						{
							int id = st.getInteger("id");
							if (id / 10 == 0)
								htmltext = "30519-05.htm";
							else
								htmltext = "30519-08.htm";
						}
						else if (player.getInventory().hasAtLeastOneItem(BRONK_PAY, SHARI_PAY, ZIMENF_PAY))
						{
							int id = st.getInteger("id");
							if (id < 50)
								htmltext = "30519-12.htm";
							else
							{
								htmltext = "30519-15.htm";
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, BRONK_PAY, -1);
								takeItems(player, SHARI_PAY, -1);
								takeItems(player, ZIMENF_PAY, -1);
								giveItems(player, MION_LETTER, 1);
							}
						}
						else if (cond == 4)
							htmltext = "30519-13.htm";
						else if (cond > 4)
							htmltext = "30519-14.htm";
						break;
					
					case SHARI:
						if (player.getInventory().hasItem(SHARI_AXE))
						{
							int id = st.getInteger("id");
							if (id < 20)
								htmltext = "30517-01.htm";
							else
							{
								htmltext = "30517-02.htm";
								st.setCond(3);
								playSound(player, SOUND_MIDDLE);
							}
							st.set("id", id + 10);
							takeItems(player, SHARI_AXE, 1);
							giveItems(player, SHARI_PAY, 1);
						}
						else if (player.getInventory().hasItem(SHARI_PAY))
							htmltext = "30517-03.htm";
						break;
					
					case BRONK:
						if (player.getInventory().hasItem(BRONK_INGOT))
						{
							int id = st.getInteger("id");
							if (id < 20)
								htmltext = "30525-01.htm";
							else
							{
								htmltext = "30525-02.htm";
								st.setCond(3);
								playSound(player, SOUND_MIDDLE);
							}
							st.set("id", id + 10);
							takeItems(player, BRONK_INGOT, 1);
							giveItems(player, BRONK_PAY, 1);
						}
						else if (player.getInventory().hasItem(BRONK_PAY))
							htmltext = "30525-03.htm";
						break;
					
					case ZIMENF:
						if (player.getInventory().hasItem(ZIMENF_POTION))
						{
							int id = st.getInteger("id");
							if (id < 20)
								htmltext = "30538-01.htm";
							else
							{
								htmltext = "30538-02.htm";
								st.setCond(3);
								playSound(player, SOUND_MIDDLE);
							}
							st.set("id", id + 10);
							takeItems(player, ZIMENF_POTION, 1);
							giveItems(player, ZIMENF_PAY, 1);
						}
						else if (player.getInventory().hasItem(ZIMENF_PAY))
							htmltext = "30538-03.htm";
						break;
					
					case TOMA:
						if (cond == 4)
						{
							htmltext = "30556-01.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, MION_LETTER, 1);
							giveItems(player, BEAR_PICTURE, 1);
						}
						else if (cond == 5)
							htmltext = "30556-02.htm";
						else if (cond == 6)
						{
							htmltext = "30556-03.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, HONEY_JAR, -1);
							takeItems(player, BEAR_PICTURE, 1);
							giveItems(player, TARANTULA_PICTURE, 1);
						}
						else if (cond == 7)
							htmltext = "30556-04.htm";
						else if (cond == 8)
							htmltext = "30556-05a.htm";
						else if (cond == 9)
							htmltext = "30556-06a.htm";
						else if (cond == 10 || cond == 11)
							htmltext = "30556-07.htm";
						else if (cond == 12)
							htmltext = "30556-06c.htm";
						break;
					
					case RAUT:
						if (cond == 9)
							htmltext = "30316-01.htm";
						else if (cond == 10)
							htmltext = "30316-04.htm";
						else if (cond == 11)
						{
							htmltext = "30316-05.htm";
							takeItems(player, SUCCUBUS_UNDIES, 1);
							giveItems(player, RING_OF_RAVEN, 1);
							rewardExpAndSp(player, 3200, 7080);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case TORAI:
						if (cond == 10)
							htmltext = "30557-01.htm";
						break;
					
					case YASHENI:
						if (cond == 12)
							htmltext = "31958-01.htm";
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
			case HUNTER_BEAR:
				if (st.getCond() == 5)
				{
					int step = st.getInteger("step");
					if (step > 20)
					{
						if (((step - 20) * 10) >= Rnd.get(100))
						{
							addSpawn(HONEY_BEAR, npc, false, 300000, true);
							st.unset("step");
						}
						else
							st.set("step", step + 1);
					}
					else
						st.set("step", step + 1);
				}
				break;
			
			case HONEY_BEAR:
				if (st.getCond() == 5 && ((Monster) npc).getSpoilState().isActualSpoiler(player) && dropItemsAlways(player, HONEY_JAR, 1, 5))
					st.setCond(6);
				break;
			
			case HUNTER_TARANTULA, PLUNDER_TARANTULA:
				if (st.getCond() == 7 && ((Monster) npc).getSpoilState().isActualSpoiler(player) && dropItems(player, BEAD, 1, 20, (npc.getNpcId() == HUNTER_TARANTULA) ? 333333 : 600000))
					st.setCond(8);
				break;
		}
	}
}