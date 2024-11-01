package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q226_TestOfTheHealer extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q226_TestOfTheHealer";
	
	// Items
	private static final int REPORT_OF_PERRIN = 2810;
	private static final int KRISTINA_LETTER = 2811;
	private static final int PICTURE_OF_WINDY = 2812;
	private static final int GOLDEN_STATUE = 2813;
	private static final int WINDY_PEBBLES = 2814;
	private static final int ORDER_OF_SORIUS = 2815;
	private static final int SECRET_LETTER_1 = 2816;
	private static final int SECRET_LETTER_2 = 2817;
	private static final int SECRET_LETTER_3 = 2818;
	private static final int SECRET_LETTER_4 = 2819;
	
	// Rewards
	private static final int MARK_OF_HEALER = 2820;
	
	// NPCs
	private static final int BANDELLOS = 30473;
	private static final int SORIUS = 30327;
	private static final int ALLANA = 30424;
	private static final int PERRIN = 30428;
	private static final int GUPU = 30658;
	private static final int ORPHAN_GIRL = 30659;
	private static final int WINDY_SHAORING = 30660;
	private static final int MYSTERIOUS_DARKELF = 30661;
	private static final int PIPER_LONGBOW = 30662;
	private static final int SLEIN_SHINING_BLADE = 30663;
	private static final int KAIN_FLYING_KNIFE = 30664;
	private static final int KRISTINA = 30665;
	private static final int DAURIN_HAMMERCRUSH = 30674;
	
	// Monsters
	private static final int LETO_LIZARDMAN_LEADER = 27123;
	private static final int LETO_LIZARDMAN_ASSASSIN = 27124;
	private static final int LETO_LIZARDMAN_SNIPER = 27125;
	private static final int LETO_LIZARDMAN_WIZARD = 27126;
	private static final int LETO_LIZARDMAN_LORD = 27127;
	private static final int TATOMA = 27134;
	
	private Npc _tatoma;
	private Npc _letoLeader;
	
	public Q226_TestOfTheHealer()
	{
		super(226, "Test of the Healer");
		
		setItemsIds(REPORT_OF_PERRIN, KRISTINA_LETTER, PICTURE_OF_WINDY, GOLDEN_STATUE, WINDY_PEBBLES, ORDER_OF_SORIUS, SECRET_LETTER_1, SECRET_LETTER_2, SECRET_LETTER_3, SECRET_LETTER_4);
		
		addQuestStart(BANDELLOS);
		addTalkId(BANDELLOS, SORIUS, ALLANA, PERRIN, GUPU, ORPHAN_GIRL, WINDY_SHAORING, MYSTERIOUS_DARKELF, PIPER_LONGBOW, SLEIN_SHINING_BLADE, KAIN_FLYING_KNIFE, KRISTINA, DAURIN_HAMMERCRUSH);
		
		addDecayed(LETO_LIZARDMAN_LEADER, TATOMA);
		addMyDying(LETO_LIZARDMAN_LEADER, LETO_LIZARDMAN_ASSASSIN, LETO_LIZARDMAN_SNIPER, LETO_LIZARDMAN_WIZARD, LETO_LIZARDMAN_LORD, TATOMA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// BANDELLOS
		if (event.equalsIgnoreCase("30473-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, REPORT_OF_PERRIN, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30473-04a.htm";
		}
		else if (event.equalsIgnoreCase("30473-09.htm"))
		{
			takeItems(player, GOLDEN_STATUE, 1);
			giveItems(player, MARK_OF_HEALER, 1);
			rewardExpAndSp(player, 134839, 50000);
			player.broadcastPacket(new SocialAction(player, 3));
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		// PERRIN
		else if (event.equalsIgnoreCase("30428-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			
			if (_tatoma == null)
				_tatoma = addSpawn(TATOMA, -93254, 147559, -2679, 0, false, 200000, false);
		}
		// GUPU
		else if (event.equalsIgnoreCase("30658-02.htm"))
		{
			if (player.getAdena() >= 100000)
			{
				st.setCond(7);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, 57, 100000);
				giveItems(player, PICTURE_OF_WINDY, 1);
			}
			else
				htmltext = "30658-05.htm";
		}
		else if (event.equalsIgnoreCase("30658-03.htm"))
		{
			st.set("gupu", 1);
		}
		else if (event.equalsIgnoreCase("30658-07.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
		}
		// WINDY SHAORING
		else if (event.equalsIgnoreCase("30660-03.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, PICTURE_OF_WINDY, 1);
			giveItems(player, WINDY_PEBBLES, 1);
		}
		// DAURIN HAMMERCRUSH
		else if (event.equalsIgnoreCase("30674-02.htm"))
		{
			st.setCond(11);
			playSound(player, SOUND_BEFORE_BATTLE);
			takeItems(player, ORDER_OF_SORIUS, 1);
			
			if (_letoLeader == null)
				_letoLeader = addSpawn(LETO_LIZARDMAN_LEADER, -97441, 106585, -3405, 0, false, 200000, false);
		}
		// KRISTINA
		else if (event.equalsIgnoreCase("30665-02.htm"))
		{
			st.setCond(22);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SECRET_LETTER_1, 1);
			takeItems(player, SECRET_LETTER_2, 1);
			takeItems(player, SECRET_LETTER_3, 1);
			takeItems(player, SECRET_LETTER_4, 1);
			giveItems(player, KRISTINA_LETTER, 1);
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
				if (player.getClassId() != ClassId.KNIGHT && player.getClassId() != ClassId.ELVEN_KNIGHT && player.getClassId() != ClassId.CLERIC && player.getClassId() != ClassId.ELVEN_ORACLE)
					htmltext = "30473-01.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30473-02.htm";
				else
					htmltext = "30473-03.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case BANDELLOS:
						if (cond < 23)
							htmltext = "30473-05.htm";
						else
						{
							if (!player.getInventory().hasItem(GOLDEN_STATUE))
							{
								htmltext = "30473-06.htm";
								giveItems(player, MARK_OF_HEALER, 1);
								rewardExpAndSp(player, 118304, 26250);
								player.broadcastPacket(new SocialAction(player, 3));
								playSound(player, SOUND_FINISH);
								st.exitQuest(false);
							}
							else
								htmltext = "30473-07.htm";
						}
						break;
					
					case PERRIN:
						if (cond < 3)
							htmltext = "30428-01.htm";
						else if (cond == 3)
						{
							htmltext = "30428-03.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, REPORT_OF_PERRIN, 1);
						}
						else
							htmltext = "30428-04.htm";
						break;
					
					case ORPHAN_GIRL:
						htmltext = "30659-0" + Rnd.get(1, 5) + ".htm";
						break;
					
					case ALLANA:
						if (cond == 4)
						{
							htmltext = "30424-01.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond > 4)
							htmltext = "30424-02.htm";
						break;
					
					case GUPU:
						if (st.getInteger("gupu") == 1 && cond != 9)
						{
							htmltext = "30658-07.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 5)
						{
							htmltext = "30658-01.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 6)
							htmltext = "30658-01.htm";
						else if (cond == 7)
							htmltext = "30658-04.htm";
						else if (cond == 8)
						{
							htmltext = "30658-06.htm";
							playSound(player, SOUND_ITEMGET);
							takeItems(player, WINDY_PEBBLES, 1);
							giveItems(player, GOLDEN_STATUE, 1);
						}
						else if (cond > 8)
							htmltext = "30658-07.htm";
						break;
					
					case WINDY_SHAORING:
						if (cond == 7)
							htmltext = "30660-01.htm";
						else if (player.getInventory().hasItem(WINDY_PEBBLES))
							htmltext = "30660-04.htm";
						break;
					
					case SORIUS:
						if (cond == 9)
						{
							htmltext = "30327-01.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, ORDER_OF_SORIUS, 1);
						}
						else if (cond > 9 && cond < 22)
							htmltext = "30327-02.htm";
						else if (cond == 22)
						{
							htmltext = "30327-03.htm";
							st.setCond(23);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, KRISTINA_LETTER, 1);
						}
						else if (cond == 23)
							htmltext = "30327-04.htm";
						break;
					
					case DAURIN_HAMMERCRUSH:
						if (cond == 10)
							htmltext = "30674-01.htm";
						else if (cond == 11)
						{
							htmltext = "30674-02a.htm";
							if (_letoLeader == null)
								_letoLeader = addSpawn(LETO_LIZARDMAN_LEADER, -97441, 106585, -3405, 200000, false, 0, false);
						}
						else if (cond == 12)
						{
							htmltext = "30674-03.htm";
							st.setCond(13);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond > 12)
							htmltext = "30674-04.htm";
						break;
					
					case PIPER_LONGBOW, SLEIN_SHINING_BLADE, KAIN_FLYING_KNIFE:
						if (cond == 13 || cond == 14)
							htmltext = npc.getNpcId() + "-01.htm";
						else if (cond > 14 && cond < 19)
							htmltext = npc.getNpcId() + "-02.htm";
						else if (cond > 18 && cond < 22)
						{
							htmltext = npc.getNpcId() + "-03.htm";
							st.setCond(21);
							playSound(player, SOUND_MIDDLE);
						}
						break;
					
					case MYSTERIOUS_DARKELF:
						if (cond == 13)
						{
							htmltext = "30661-01.htm";
							st.setCond(14);
							playSound(player, SOUND_BEFORE_BATTLE);
							addSpawn(LETO_LIZARDMAN_ASSASSIN, player, true, 0, false);
							addSpawn(LETO_LIZARDMAN_ASSASSIN, player, true, 0, false);
							addSpawn(LETO_LIZARDMAN_ASSASSIN, player, true, 0, false);
						}
						else if (cond == 14)
							htmltext = "30661-01.htm";
						else if (cond == 15)
						{
							htmltext = "30661-02.htm";
							st.setCond(16);
							playSound(player, SOUND_BEFORE_BATTLE);
							addSpawn(LETO_LIZARDMAN_SNIPER, player, true, 0, false);
							addSpawn(LETO_LIZARDMAN_SNIPER, player, true, 0, false);
							addSpawn(LETO_LIZARDMAN_SNIPER, player, true, 0, false);
						}
						else if (cond == 16)
							htmltext = "30661-02.htm";
						else if (cond == 17)
						{
							htmltext = "30661-03.htm";
							st.setCond(18);
							playSound(player, SOUND_BEFORE_BATTLE);
							addSpawn(LETO_LIZARDMAN_WIZARD, player, true, 0, false);
							addSpawn(LETO_LIZARDMAN_WIZARD, player, true, 0, false);
							addSpawn(LETO_LIZARDMAN_LORD, player, true, 0, false);
						}
						else if (cond == 18)
							htmltext = "30661-03.htm";
						else if (cond == 19)
						{
							htmltext = "30661-04.htm";
							st.setCond(20);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 20 || cond == 21)
							htmltext = "30661-04.htm";
						break;
					
					case KRISTINA:
						if (cond > 18 && cond < 22)
							htmltext = "30665-01.htm";
						else if (cond > 21)
							htmltext = "30665-04.htm";
						else
							htmltext = "30665-03.htm";
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
		if (npc == _tatoma)
		{
			_tatoma = null;
		}
		else if (npc == _letoLeader)
		{
			_letoLeader = null;
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
			case TATOMA:
				if (cond == 1 || cond == 2)
				{
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
				}
				break;
			
			case LETO_LIZARDMAN_LEADER:
				if (cond == 10 || cond == 11)
				{
					st.setCond(12);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, SECRET_LETTER_1, 1);
				}
				break;
			
			case LETO_LIZARDMAN_ASSASSIN:
				if (cond == 13 || cond == 14)
				{
					st.setCond(15);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, SECRET_LETTER_2, 1);
				}
				break;
			
			case LETO_LIZARDMAN_SNIPER:
				if (cond == 15 || cond == 16)
				{
					st.setCond(17);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, SECRET_LETTER_3, 1);
				}
				break;
			
			case LETO_LIZARDMAN_LORD:
				if (cond == 17 || cond == 18)
				{
					st.setCond(19);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, SECRET_LETTER_4, 1);
				}
				break;
		}
	}
}