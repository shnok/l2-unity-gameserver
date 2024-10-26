package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q120_PavelsResearch extends Quest
{
	private static final String QUEST_NAME = "Q120_PavelsResearch";
	
	// NPCs
	private static final int YUMI = 32041;
	private static final int WEATHERMASTER_1 = 32042; // north
	private static final int WEATHERMASTER_2 = 32043; // east
	private static final int WEATHERMASTER_3 = 32044; // west
	private static final int DOCTOR_CHAOS_SECRET_BOOKSHELF = 32045;
	private static final int SUSPICIOUS_PILE_OF_STONES = 32046;
	private static final int WENDY = 32047;
	
	// Items
	private static final int LOCKUP_RESEARCH_REPORT = 8058;
	private static final int RESEARCH_REPORT = 8059;
	private static final int KEY_OF_ENIGMA = 8060;
	private static final int FLOWER_OF_PAVEL = 8290;
	private static final int HEART_OF_ATLANTA = 8291;
	private static final int WENDY_NECKLACE = 8292;
	
	// Reward
	private static final int EARRING_OF_BINDING = 854;
	
	public Q120_PavelsResearch()
	{
		super(120, "Pavel's Research");
		
		setItemsIds(LOCKUP_RESEARCH_REPORT, RESEARCH_REPORT, KEY_OF_ENIGMA, FLOWER_OF_PAVEL, HEART_OF_ATLANTA, WENDY_NECKLACE);
		
		addQuestStart(SUSPICIOUS_PILE_OF_STONES);
		addTalkId(YUMI, WEATHERMASTER_1, WEATHERMASTER_2, WEATHERMASTER_3, DOCTOR_CHAOS_SECRET_BOOKSHELF, SUSPICIOUS_PILE_OF_STONES, WENDY);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32041-03.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32041-04.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32041-12.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32041-16.htm"))
		{
			st.setCond(16);
			giveItems(player, KEY_OF_ENIGMA, 1);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32041-22.htm"))
		{
			st.setCond(17);
			takeItems(player, KEY_OF_ENIGMA, 1);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32041-32.htm"))
		{
			takeItems(player, WENDY_NECKLACE, 1);
			giveItems(player, EARRING_OF_BINDING, 1);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("32042-06.htm"))
		{
			if (st.getCond() == 10)
			{
				if (st.getInteger("talk") + st.getInteger("talk1") == 2)
				{
					st.setCond(11);
					st.set("talk", 0);
					st.set("talk1", 0);
					playSound(player, SOUND_MIDDLE);
				}
				else
					htmltext = "32042-03.htm";
			}
		}
		else if (event.equalsIgnoreCase("32042-08.htm"))
			playSound(player, "AmbSound.dt_percussion_01");
		else if (event.equalsIgnoreCase("32042-10.htm"))
		{
			if (st.getInteger("talk") + st.getInteger("talk1") + st.getInteger("talk2") == 3)
				htmltext = "32042-14.htm";
		}
		else if (event.equalsIgnoreCase("32042-11.htm"))
		{
			if (st.getInteger("talk") == 0)
				st.set("talk", 1);
		}
		else if (event.equalsIgnoreCase("32042-12.htm"))
		{
			if (st.getInteger("talk1") == 0)
				st.set("talk1", 1);
		}
		else if (event.equalsIgnoreCase("32042-13.htm"))
		{
			if (st.getInteger("talk2") == 0)
				st.set("talk2", 1);
		}
		else if (event.equalsIgnoreCase("32042-15.htm"))
		{
			st.setCond(12);
			st.set("talk", 0);
			st.set("talk1", 0);
			st.set("talk2", 0);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32043-06.htm"))
		{
			if (st.getCond() == 17)
			{
				if (st.getInteger("talk") + st.getInteger("talk1") == 2)
				{
					st.setCond(18);
					st.set("talk", 0);
					st.set("talk1", 0);
					playSound(player, SOUND_MIDDLE);
				}
				else
					htmltext = "32043-03.htm";
			}
		}
		else if (event.equalsIgnoreCase("32043-15.htm"))
		{
			if (st.getInteger("talk") + st.getInteger("talk1") == 2)
				htmltext = "32043-29.htm";
		}
		else if (event.equalsIgnoreCase("32043-18.htm"))
		{
			if (st.getInteger("talk") == 1)
				htmltext = "32043-21.htm";
		}
		else if (event.equalsIgnoreCase("32043-20.htm"))
		{
			st.set("talk", 1);
			playSound(player, "AmbSound.ed_drone_02");
		}
		else if (event.equalsIgnoreCase("32043-28.htm"))
		{
			st.set("talk1", 1);
		}
		else if (event.equalsIgnoreCase("32043-30.htm"))
		{
			st.setCond(19);
			st.set("talk", 0);
			st.set("talk1", 0);
		}
		else if (event.equalsIgnoreCase("32044-06.htm"))
		{
			if (st.getCond() == 20)
			{
				if (st.getInteger("talk") + st.getInteger("talk1") == 2)
				{
					st.setCond(21);
					st.set("talk", 0);
					st.set("talk1", 0);
					playSound(player, SOUND_MIDDLE);
					playSound(player, "AmbSound.ac_percussion_02");
				}
				else
					htmltext = "32044-03.htm";
			}
		}
		else if (event.equalsIgnoreCase("32044-08.htm"))
		{
			if (st.getInteger("talk") + st.getInteger("talk1") == 2)
				htmltext = "32044-11.htm";
		}
		else if (event.equalsIgnoreCase("32044-09.htm"))
		{
			if (st.getInteger("talk") == 0)
				st.set("talk", 1);
		}
		else if (event.equalsIgnoreCase("32044-10.htm"))
		{
			if (st.getInteger("talk1") == 0)
				st.set("talk1", 1);
		}
		else if (event.equalsIgnoreCase("32044-17.htm"))
		{
			st.setCond(22);
			st.set("talk", 0);
			st.set("talk1", 0);
			playSound(player, SOUND_MIDDLE);
			
			playSound(player, "AmbSound.ed_drone_02");
			npc.getAI().addCastDesire(player, 5073, 5, 1000000);
		}
		else if (event.equalsIgnoreCase("32045-02.htm"))
		{
			st.setCond(15);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, LOCKUP_RESEARCH_REPORT, 1);
			
			npc.getAI().addCastDesire(player, 5073, 5, 1000000);
		}
		else if (event.equalsIgnoreCase("32046-04.htm") || event.equalsIgnoreCase("32046-05.htm"))
		{
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("32046-06.htm"))
		{
			if (player.getStatus().getLevel() >= 50)
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
			{
				htmltext = "32046-00.htm";
				st.exitQuest(true);
			}
		}
		else if (event.equalsIgnoreCase("32046-08.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32046-12.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, FLOWER_OF_PAVEL, 1);
		}
		else if (event.equalsIgnoreCase("32046-22.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32046-29.htm"))
		{
			st.setCond(13);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32046-35.htm"))
		{
			st.setCond(20);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32046-38.htm"))
		{
			st.setCond(23);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, HEART_OF_ATLANTA, 1);
		}
		else if (event.equalsIgnoreCase("32047-06.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-10.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, FLOWER_OF_PAVEL, 1);
		}
		else if (event.equalsIgnoreCase("32047-15.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-18.htm"))
		{
			st.setCond(14);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-26.htm"))
		{
			st.setCond(24);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HEART_OF_ATLANTA, 1);
		}
		else if (event.equalsIgnoreCase("32047-32.htm"))
		{
			st.setCond(25);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, WENDY_NECKLACE, 1);
		}
		else if (event.equalsIgnoreCase("w1_1"))
		{
			st.set("talk", 1);
			htmltext = "32042-04.htm";
		}
		else if (event.equalsIgnoreCase("w1_2"))
		{
			st.set("talk1", 1);
			htmltext = "32042-05.htm";
		}
		else if (event.equalsIgnoreCase("w2_1"))
		{
			st.set("talk", 1);
			htmltext = "32043-04.htm";
		}
		else if (event.equalsIgnoreCase("w2_2"))
		{
			st.set("talk1", 1);
			htmltext = "32043-05.htm";
		}
		else if (event.equalsIgnoreCase("w3_1"))
		{
			st.set("talk", 1);
			htmltext = "32044-04.htm";
		}
		else if (event.equalsIgnoreCase("w3_2"))
		{
			st.set("talk1", 1);
			htmltext = "32044-05.htm";
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
				QuestState st2 = player.getQuestList().getQuestState("Q114_ResurrectionOfAnOldManager");
				if (st2 != null && st2.isCompleted())
					htmltext = (player.getStatus().getLevel() < 50) ? "32046-00.htm" : "32046-01.htm";
				else
					htmltext = "32046-00.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case SUSPICIOUS_PILE_OF_STONES:
						if (cond == 1)
							htmltext = "32046-06.htm";
						else if (cond > 1 && cond < 5)
							htmltext = "32046-09.htm";
						else if (cond == 5)
							htmltext = "32046-10.htm";
						else if (cond > 5 && cond < 9)
							htmltext = "32046-13.htm";
						else if (cond == 9)
							htmltext = "32046-14.htm";
						else if (cond == 10 || cond == 11)
							htmltext = "32046-23.htm";
						else if (cond == 12)
							htmltext = "32046-26.htm";
						else if (cond > 12 && cond < 19)
							htmltext = "32046-30.htm";
						else if (cond == 19)
							htmltext = "32046-31.htm";
						else if (cond == 20 || cond == 21)
							htmltext = "32046-36.htm";
						else if (cond == 22)
							htmltext = "32046-37.htm";
						else if (cond > 22)
							htmltext = "32046-39.htm";
						break;
					
					case WENDY:
						if (cond == 2 || cond == 3 || cond == 4)
							htmltext = "32047-01.htm";
						else if (cond == 5)
							htmltext = "32047-07.htm";
						else if (cond == 6)
							htmltext = "32047-08.htm";
						else if (cond == 7)
							htmltext = "32047-11.htm";
						else if (cond == 8)
							htmltext = "32047-12.htm";
						else if (cond > 8 && cond < 13)
							htmltext = "32047-15.htm";
						else if (cond == 13)
							htmltext = "32047-16.htm";
						else if (cond == 14)
							htmltext = "32047-19.htm";
						else if (cond > 14 && cond < 23)
							htmltext = "32047-20.htm";
						else if (cond == 23)
							htmltext = "32047-21.htm";
						else if (cond == 24)
							htmltext = "32047-26.htm";
						else if (cond == 25)
							htmltext = "32047-33.htm";
						break;
					
					case YUMI:
						if (cond == 2)
							htmltext = "32041-01.htm";
						else if (cond == 3)
							htmltext = "32041-05.htm";
						else if (cond > 3 && cond < 7)
							htmltext = "32041-06.htm";
						else if (cond == 7)
							htmltext = "32041-07.htm";
						else if (cond > 7 && cond < 15)
							htmltext = "32041-13.htm";
						else if (cond == 15)
							htmltext = "32041-14.htm";
						else if (cond == 16)
							htmltext = (!player.getInventory().hasItem(RESEARCH_REPORT)) ? "32041-17.htm" : "32041-18.htm";
						else if (cond > 16 && cond < 25)
							htmltext = "32041-22.htm";
						else if (cond == 25)
							htmltext = "32041-26.htm";
						break;
					
					case WEATHERMASTER_1:
						if (cond == 10)
						{
							htmltext = "32042-01.htm";
							playSound(player, "AmbSound.cd_crystal_loop");
						}
						else if (cond == 11)
						{
							if (st.getInteger("talk") + st.getInteger("talk1") + st.getInteger("talk2") == 3)
								htmltext = "32042-14.htm";
							else
								htmltext = "32042-06.htm";
						}
						else if (cond > 11)
							htmltext = "32042-15.htm";
						break;
					
					case WEATHERMASTER_2:
						if (cond == 17)
							htmltext = "32043-01.htm";
						else if (cond == 18)
						{
							if (st.getInteger("talk") + st.getInteger("talk1") == 2)
								htmltext = "32043-29.htm";
							else
								htmltext = "32043-06.htm";
						}
						else if (cond > 18)
							htmltext = "32043-30.htm";
						break;
					
					case WEATHERMASTER_3:
						if (cond == 20)
							htmltext = "32044-01.htm";
						else if (cond == 21)
							htmltext = "32044-06.htm";
						else if (cond > 21)
							htmltext = "32044-18.htm";
						break;
					
					case DOCTOR_CHAOS_SECRET_BOOKSHELF:
						if (cond == 14)
							htmltext = "32045-01.htm";
						else if (cond > 14)
							htmltext = "32045-03.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		return htmltext;
	}
}