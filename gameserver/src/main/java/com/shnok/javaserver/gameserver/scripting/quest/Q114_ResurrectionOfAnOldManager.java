package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q114_ResurrectionOfAnOldManager extends Quest
{
	private static final String QUEST_NAME = "Q114_ResurrectionOfAnOldManager";
	
	// NPCs
	private static final int NEWYEAR = 31961;
	private static final int YUMI = 32041;
	private static final int STONE = 32046;
	private static final int WENDY = 32047;
	private static final int BOX = 32050;
	
	// Items
	private static final int LETTER = 8288;
	private static final int DETECTOR = 8090;
	private static final int DETECTOR_2 = 8091;
	private static final int STARSTONE = 8287;
	private static final int STARSTONE_2 = 8289;
	
	// Mobs
	private static final int GOLEM = 27318;
	
	private Npc _golem;
	
	public Q114_ResurrectionOfAnOldManager()
	{
		super(114, "Resurrection of an Old Manager");
		
		setItemsIds(LETTER, DETECTOR, DETECTOR_2, STARSTONE, STARSTONE_2);
		
		addQuestStart(YUMI);
		addTalkId(YUMI, WENDY, BOX, STONE, NEWYEAR);
		
		addDecayed(GOLEM);
		addMyDying(GOLEM);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32041-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("talk", 0);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32041-06.htm"))
			st.set("talk", 1);
		else if (event.equalsIgnoreCase("32041-07.htm"))
		{
			st.setCond(2);
			st.set("talk", 0);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32041-10.htm"))
		{
			final int choice = st.getInteger("choice");
			
			if (choice == 1)
				htmltext = "32041-10.htm";
			else if (choice == 2)
				htmltext = "32041-10a.htm";
			else if (choice == 3)
				htmltext = "32041-10b.htm";
		}
		else if (event.equalsIgnoreCase("32041-11.htm"))
			st.set("talk", 1);
		else if (event.equalsIgnoreCase("32041-18.htm"))
			st.set("talk", 2);
		else if (event.equalsIgnoreCase("32041-20.htm"))
		{
			st.setCond(6);
			st.set("talk", 0);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32041-25.htm"))
		{
			st.setCond(17);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, DETECTOR, 1);
		}
		else if (event.equalsIgnoreCase("32041-28.htm"))
		{
			st.set("talk", 1);
			takeItems(player, DETECTOR_2, 1);
		}
		else if (event.equalsIgnoreCase("32041-31.htm"))
		{
			if (st.getInteger("choice") > 1)
				htmltext = "32041-37.htm";
		}
		else if (event.equalsIgnoreCase("32041-32.htm"))
		{
			st.setCond(21);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, LETTER, 1);
		}
		else if (event.equalsIgnoreCase("32041-36.htm"))
		{
			st.setCond(20);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32046-02.htm"))
		{
			st.setCond(19);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32046-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("32047-01.htm"))
		{
			final int talk = st.getInteger("talk");
			final int talk1 = st.getInteger("talk1");
			
			if (talk == 1 && talk1 == 1)
				htmltext = "32047-04.htm";
			else if (talk == 2 && talk1 == 2 && st.getInteger("talk2") == 2)
				htmltext = "32047-08.htm";
		}
		else if (event.equalsIgnoreCase("32047-02.htm"))
		{
			if (st.getInteger("talk") == 0)
				st.set("talk", 1);
		}
		else if (event.equalsIgnoreCase("32047-03.htm"))
		{
			if (st.getInteger("talk1") == 0)
				st.set("talk1", 1);
		}
		else if (event.equalsIgnoreCase("32047-05.htm"))
		{
			st.setCond(3);
			st.set("talk", 0);
			st.set("choice", 1);
			st.unset("talk1");
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-06.htm"))
		{
			st.setCond(4);
			st.set("talk", 0);
			st.set("choice", 2);
			st.unset("talk1");
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-07.htm"))
		{
			st.setCond(5);
			st.set("talk", 0);
			st.set("choice", 3);
			st.unset("talk1");
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-13.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-13a.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-15.htm"))
		{
			if (st.getInteger("talk") == 0)
				st.set("talk", 1);
		}
		else if (event.equalsIgnoreCase("32047-15a.htm"))
		{
			if (_golem == null)
			{
				_golem = addSpawn(GOLEM, 96977, -110625, -3322, 0, true, 300000, true);
				_golem.broadcastNpcSay(NpcStringId.ID_11450, player.getName());
				_golem.getAI().addAttackDesire(player, 2000);
			}
			else
				htmltext = "32047-19a.htm";
		}
		else if (event.equalsIgnoreCase("32047-17a.htm"))
		{
			st.setCond(12);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-20.htm"))
			st.set("talk", 2);
		else if (event.equalsIgnoreCase("32047-23.htm"))
		{
			st.setCond(13);
			st.set("talk", 0);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-25.htm"))
		{
			st.setCond(15);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, STARSTONE, 1);
		}
		else if (event.equalsIgnoreCase("32047-30.htm"))
			st.set("talk", 2);
		else if (event.equalsIgnoreCase("32047-33.htm"))
		{
			final int cond = st.getCond();
			
			if (cond == 7)
			{
				st.setCond(8);
				st.set("talk", 0);
				playSound(player, SOUND_MIDDLE);
			}
			else if (cond == 8)
			{
				st.setCond(9);
				htmltext = "32047-34.htm";
				playSound(player, SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("32047-34.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32047-38.htm"))
		{
			if (player.getAdena() >= 3000)
			{
				st.setCond(26);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, 57, 3000);
				giveItems(player, STARSTONE_2, 1);
			}
			else
				htmltext = "32047-39.htm";
		}
		else if (event.equalsIgnoreCase("32050-02.htm"))
		{
			st.set("talk", 1);
			playSound(player, "ItemSound.armor_wood_3");
		}
		else if (event.equalsIgnoreCase("32050-04.htm"))
		{
			st.setCond(14);
			st.set("talk", 0);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, STARSTONE, 1);
		}
		else if (event.equalsIgnoreCase("31961-02.htm"))
		{
			st.setCond(22);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER, 1);
			giveItems(player, STARSTONE_2, 1);
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
				QuestState pavelReq = player.getQuestList().getQuestState("Q121_PavelTheGiant");
				htmltext = (pavelReq == null || !pavelReq.isCompleted() || player.getStatus().getLevel() < 49) ? "32041-00.htm" : "32041-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				final int talk = st.getInteger("talk");
				
				switch (npc.getNpcId())
				{
					case YUMI:
						if (cond == 1)
						{
							if (talk == 0)
								htmltext = "32041-02.htm";
							else
								htmltext = "32041-06.htm";
						}
						else if (cond == 2)
							htmltext = "32041-08.htm";
						else if (cond > 2 && cond < 6)
						{
							if (talk == 0)
								htmltext = "32041-09.htm";
							else if (talk == 1)
								htmltext = "32041-11.htm";
							else
								htmltext = "32041-18.htm";
						}
						else if (cond == 6)
							htmltext = "32041-21.htm";
						else if (cond == 9 || cond == 12 || cond == 16)
							htmltext = "32041-22.htm";
						else if (cond == 17)
							htmltext = "32041-26.htm";
						else if (cond == 19)
						{
							if (talk == 0)
								htmltext = "32041-27.htm";
							else
								htmltext = "32041-28.htm";
						}
						else if (cond == 20)
							htmltext = "32041-36.htm";
						else if (cond == 21)
							htmltext = "32041-33.htm";
						else if (cond == 22 || cond == 26)
						{
							htmltext = "32041-34.htm";
							st.setCond(27);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 27)
							htmltext = "32041-35.htm";
						break;
					
					case WENDY:
						if (cond == 2)
						{
							if (talk == 0 && st.getInteger("talk1") == 0)
								htmltext = "32047-01.htm";
							else if (talk == 1 && st.getInteger("talk1") == 1)
								htmltext = "32047-04.htm";
						}
						else if (cond == 3)
							htmltext = "32047-09.htm";
						else if (cond == 4 || cond == 5)
							htmltext = "32047-09a.htm";
						else if (cond == 6)
						{
							final int choice = st.getInteger("choice");
							
							if (choice == 1)
							{
								if (talk == 0)
									htmltext = "32047-10.htm";
								else if (talk == 1)
									htmltext = "32047-20.htm";
							}
							else if (choice == 2)
								htmltext = "32047-10a.htm";
							else if (choice == 3)
							{
								if (talk == 0)
									htmltext = "32047-14.htm";
								else if (talk == 1)
									htmltext = "32047-15.htm";
								else
									htmltext = "32047-20.htm";
							}
						}
						else if (cond == 7)
						{
							if (talk == 0)
								htmltext = "32047-14.htm";
							else if (talk == 1)
								htmltext = "32047-15.htm";
							else
								htmltext = "32047-20.htm";
						}
						else if (cond == 8)
							htmltext = "32047-30.htm";
						else if (cond == 9)
							htmltext = "32047-27.htm";
						else if (cond == 10)
							htmltext = "32047-14a.htm";
						else if (cond == 11)
							htmltext = "32047-16a.htm";
						else if (cond == 12)
							htmltext = "32047-18a.htm";
						else if (cond == 13)
							htmltext = "32047-23.htm";
						else if (cond == 14)
							htmltext = "32047-24.htm";
						else if (cond == 15)
						{
							htmltext = "32047-26.htm";
							st.setCond(16);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 16)
							htmltext = "32047-27.htm";
						else if (cond == 20)
							htmltext = "32047-35.htm";
						else if (cond == 26)
							htmltext = "32047-40.htm";
						break;
					
					case BOX:
						if (cond == 13)
						{
							if (talk == 0)
								htmltext = "32050-01.htm";
							else
								htmltext = "32050-03.htm";
						}
						else if (cond == 14)
							htmltext = "32050-05.htm";
						break;
					
					case STONE:
						if (st.getCond() == 17)
						{
							st.setCond(18);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, DETECTOR, 1);
							giveItems(player, DETECTOR_2, 1);
							player.sendPacket(new ExShowScreenMessage(NpcStringId.ID_11453, 4500));
							return null;
						}
						else if (cond == 18)
							htmltext = "32046-01.htm";
						else if (cond == 19)
							htmltext = "32046-02.htm";
						else if (cond == 27)
							htmltext = "32046-03.htm";
						break;
					
					case NEWYEAR:
						if (cond == 21)
							htmltext = "31961-01.htm";
						else if (cond == 22)
							htmltext = "31961-03.htm";
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
	public void onDecayed(Npc npc)
	{
		if (npc == _golem)
		{
			if (!npc.isDead())
				npc.broadcastNpcSay(NpcStringId.ID_11451, "Wendy");
			
			_golem = null;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 10);
		if (st == null)
			return;
		
		st.setCond(11);
		playSound(player, SOUND_MIDDLE);
		npc.broadcastNpcSay(NpcStringId.ID_11452);
	}
}