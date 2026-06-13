package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Q223_TestOfTheChampion extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q223_TestOfTheChampion";
	
	// Items
	private static final int ASCALON_LETTER_1 = 3277;
	private static final int MASON_LETTER = 3278;
	private static final int IRON_ROSE_RING = 3279;
	private static final int ASCALON_LETTER_2 = 3280;
	private static final int WHITE_ROSE_INSIGNIA = 3281;
	private static final int GROOT_LETTER = 3282;
	private static final int ASCALON_LETTER_3 = 3283;
	private static final int MOUEN_ORDER_1 = 3284;
	private static final int MOUEN_ORDER_2 = 3285;
	private static final int MOUEN_LETTER = 3286;
	private static final int HARPY_EGG = 3287;
	private static final int MEDUSA_VENOM = 3288;
	private static final int WINDSUS_BILE = 3289;
	private static final int BLOODY_AXE_HEAD = 3290;
	private static final int ROAD_RATMAN_HEAD = 3291;
	private static final int LETO_LIZARDMAN_FANG = 3292;
	
	// Rewards
	private static final int MARK_OF_CHAMPION = 3276;
	
	// NPCs
	private static final int ASCALON = 30624;
	private static final int GROOT = 30093;
	private static final int MOUEN = 30196;
	private static final int MASON = 30625;
	
	// Monsters
	private static final int HARPY = 20145;
	private static final int HARPY_MATRIARCH = 27088;
	private static final int MEDUSA = 20158;
	private static final int WINDSUS = 20553;
	private static final int ROAD_COLLECTOR = 27089;
	private static final int ROAD_SCAVENGER = 20551;
	private static final int LETO_LIZARDMAN = 20577;
	private static final int LETO_LIZARDMAN_ARCHER = 20578;
	private static final int LETO_LIZARDMAN_SOLDIER = 20579;
	private static final int LETO_LIZARDMAN_WARRIOR = 20580;
	private static final int LETO_LIZARDMAN_SHAMAN = 20581;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int BLOODY_AXE_ELITE = 20780;
	
	public Q223_TestOfTheChampion()
	{
		super(223, "Test of the Champion");
		
		setItemsIds(MASON_LETTER, MEDUSA_VENOM, WINDSUS_BILE, WHITE_ROSE_INSIGNIA, HARPY_EGG, GROOT_LETTER, MOUEN_LETTER, ASCALON_LETTER_1, IRON_ROSE_RING, BLOODY_AXE_HEAD, ASCALON_LETTER_2, ASCALON_LETTER_3, MOUEN_ORDER_1, ROAD_RATMAN_HEAD, MOUEN_ORDER_2, LETO_LIZARDMAN_FANG);
		
		addQuestStart(ASCALON);
		addTalkId(ASCALON, GROOT, MOUEN, MASON);
		
		addAttacked(HARPY, ROAD_SCAVENGER);
		addMyDying(HARPY, MEDUSA, HARPY_MATRIARCH, ROAD_COLLECTOR, ROAD_SCAVENGER, WINDSUS, LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER, LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR, LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD, BLOODY_AXE_ELITE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equals("30624-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ASCALON_LETTER_1, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30624-06a.htm";
		}
		else if (event.equals("30624-10.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MASON_LETTER, 1);
			giveItems(player, ASCALON_LETTER_2, 1);
		}
		else if (event.equals("30624-14.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, GROOT_LETTER, 1);
			giveItems(player, ASCALON_LETTER_3, 1);
		}
		else if (event.equals("30625-03.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ASCALON_LETTER_1, 1);
			giveItems(player, IRON_ROSE_RING, 1);
		}
		else if (event.equals("30093-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ASCALON_LETTER_2, 1);
			giveItems(player, WHITE_ROSE_INSIGNIA, 1);
		}
		else if (event.equals("30196-03.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ASCALON_LETTER_3, 1);
			giveItems(player, MOUEN_ORDER_1, 1);
		}
		else if (event.equals("30196-06.htm"))
		{
			st.setCond(12);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MOUEN_ORDER_1, 1);
			takeItems(player, ROAD_RATMAN_HEAD, 1);
			giveItems(player, MOUEN_ORDER_2, 1);
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
				final ClassId classId = player.getClassId();
				if (classId != ClassId.WARRIOR && classId != ClassId.ORC_RAIDER)
					htmltext = "30624-01.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30624-02.htm";
				else
					htmltext = (classId == ClassId.WARRIOR) ? "30624-03.htm" : "30624-04.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ASCALON:
						if (cond == 1)
							htmltext = "30624-07.htm";
						else if (cond < 4)
							htmltext = "30624-08.htm";
						else if (cond == 4)
							htmltext = "30624-09.htm";
						else if (cond == 5)
							htmltext = "30624-11.htm";
						else if (cond > 5 && cond < 8)
							htmltext = "30624-12.htm";
						else if (cond == 8)
							htmltext = "30624-13.htm";
						else if (cond == 9)
							htmltext = "30624-15.htm";
						else if (cond > 9 && cond < 14)
							htmltext = "30624-16.htm";
						else if (cond == 14)
						{
							htmltext = "30624-17.htm";
							takeItems(player, MOUEN_LETTER, 1);
							giveItems(player, MARK_OF_CHAMPION, 1);
							rewardExpAndSp(player, 117454, 25000);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case MASON:
						if (cond == 1)
							htmltext = "30625-01.htm";
						else if (cond == 2)
							htmltext = "30625-04.htm";
						else if (cond == 3)
						{
							htmltext = "30625-05.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BLOODY_AXE_HEAD, -1);
							takeItems(player, IRON_ROSE_RING, 1);
							giveItems(player, MASON_LETTER, 1);
						}
						else if (cond == 4)
							htmltext = "30625-06.htm";
						else if (cond > 4)
							htmltext = "30625-07.htm";
						break;
					
					case GROOT:
						if (cond == 5)
							htmltext = "30093-01.htm";
						else if (cond == 6)
							htmltext = "30093-03.htm";
						else if (cond == 7)
						{
							htmltext = "30093-04.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, WHITE_ROSE_INSIGNIA, 1);
							takeItems(player, HARPY_EGG, -1);
							takeItems(player, MEDUSA_VENOM, -1);
							takeItems(player, WINDSUS_BILE, -1);
							giveItems(player, GROOT_LETTER, 1);
						}
						else if (cond == 8)
							htmltext = "30093-05.htm";
						else if (cond > 8)
							htmltext = "30093-06.htm";
						break;
					
					case MOUEN:
						if (cond == 9)
							htmltext = "30196-01.htm";
						else if (cond == 10)
							htmltext = "30196-04.htm";
						else if (cond == 11)
							htmltext = "30196-05.htm";
						else if (cond == 12)
							htmltext = "30196-07.htm";
						else if (cond == 13)
						{
							htmltext = "30196-08.htm";
							st.setCond(14);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LETO_LIZARDMAN_FANG, -1);
							takeItems(player, MOUEN_ORDER_2, 1);
							giveItems(player, MOUEN_LETTER, 1);
						}
						else if (cond > 13)
							htmltext = "30196-09.htm";
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
			case HARPY: // Possibility to spawn an HARPY _MATRIARCH.
				if (st.getCond() == 6 && Rnd.nextBoolean() && !npc.isScriptValue(1))
				{
					// Spawn one or two matriarchs.
					for (int i = 1; i < ((Rnd.get(10) < 7) ? 2 : 3); i++)
					{
						final Npc matriarch = addSpawn(HARPY_MATRIARCH, npc, true, 0, false);
						matriarch.getAI().addAttackDesire(attacker, 200);
					}
					npc.setScriptValue(1);
				}
				break;
			
			case ROAD_SCAVENGER: // Possibility to spawn a Road Collector.
				if (st.getCond() == 10 && Rnd.nextBoolean() && !npc.isScriptValue(1))
				{
					// Spawn one or two collectors.
					for (int i = 1; i < ((Rnd.get(10) < 7) ? 2 : 3); i++)
					{
						final Npc collector = addSpawn(ROAD_COLLECTOR, npc, true, 0, false);
						collector.getAI().addAttackDesire(attacker, 200);
					}
					npc.setScriptValue(1);
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
		
		final int npcId = npc.getNpcId();
		
		switch (npcId)
		{
			case BLOODY_AXE_ELITE:
				if (st.getCond() == 2 && dropItemsAlways(player, BLOODY_AXE_HEAD, 1, 100))
					st.setCond(3);
				break;
			
			case HARPY, HARPY_MATRIARCH:
				if (st.getCond() == 6 && dropItems(player, HARPY_EGG, 1, 30, 500000))
					if (player.getInventory().getItemCount(MEDUSA_VENOM) == 30 && player.getInventory().getItemCount(WINDSUS_BILE) == 30)
						st.setCond(7);
				break;
			
			case MEDUSA:
				if (st.getCond() == 6 && dropItems(player, MEDUSA_VENOM, 1, 30, 500000))
					if (player.getInventory().getItemCount(HARPY_EGG) == 30 && player.getInventory().getItemCount(WINDSUS_BILE) == 30)
						st.setCond(7);
				break;
			
			case WINDSUS:
				if (st.getCond() == 6 && dropItems(player, WINDSUS_BILE, 1, 30, 500000))
					if (player.getInventory().getItemCount(HARPY_EGG) == 30 && player.getInventory().getItemCount(MEDUSA_VENOM) == 30)
						st.setCond(7);
				break;
			
			case ROAD_COLLECTOR, ROAD_SCAVENGER:
				if (st.getCond() == 10 && dropItemsAlways(player, ROAD_RATMAN_HEAD, 1, 100))
					st.setCond(11);
				break;
			
			case LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER, LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR, LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD:
				if (st.getCond() == 12 && dropItems(player, LETO_LIZARDMAN_FANG, 1, 100, 500000 + (100000 * (npcId - 20577))))
					st.setCond(13);
				break;
		}
	}
}