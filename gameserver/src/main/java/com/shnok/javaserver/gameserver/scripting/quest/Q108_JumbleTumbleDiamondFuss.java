package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q108_JumbleTumbleDiamondFuss extends Quest
{
	private static final String QUEST_NAME = "Q108_JumbleTumbleDiamondFuss";
	
	// NPCs
	private static final int GOUPH = 30523;
	private static final int REEP = 30516;
	private static final int MURDOC = 30521;
	private static final int AIRY = 30522;
	private static final int BRUNON = 30526;
	private static final int MARON = 30529;
	private static final int TOROCCO = 30555;
	
	// Items
	private static final int GOUPH_CONTRACT = 1559;
	private static final int REEP_CONTRACT = 1560;
	private static final int ELVEN_WINE = 1561;
	private static final int BRUNON_DICE = 1562;
	private static final int BRUNON_CONTRACT = 1563;
	private static final int AQUAMARINE = 1564;
	private static final int CHRYSOBERYL = 1565;
	private static final int GEM_BOX = 1566;
	private static final int COAL_PIECE = 1567;
	private static final int BRUNON_LETTER = 1568;
	private static final int BERRY_TART = 1569;
	private static final int BAT_DIAGRAM = 1570;
	private static final int STAR_DIAMOND = 1571;
	
	// Monsters
	private static final int GOBLIN_BRIGAND_LEADER = 20323;
	private static final int GOBLIN_BRIGAND_LIEUTENANT = 20324;
	private static final int BLADE_BAT = 20480;
	
	// Rewards
	private static final int LESSER_HEALING_POTION = 1060;
	private static final int SILVERSMITH_HAMMER = 1511;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	
	private static final int[][] LEADER_DROPLIST =
	{
		{
			AQUAMARINE,
			1,
			10,
			800000
		},
		{
			CHRYSOBERYL,
			1,
			10,
			800000
		}
	};
	
	private static final int[][] LIEUTENANT_DROPLIST =
	{
		{
			AQUAMARINE,
			1,
			10,
			600000
		},
		{
			CHRYSOBERYL,
			1,
			10,
			600000
		}
	};
	
	public Q108_JumbleTumbleDiamondFuss()
	{
		super(108, "Jumble, Tumble, Diamond Fuss");
		
		setItemsIds(GOUPH_CONTRACT, REEP_CONTRACT, ELVEN_WINE, BRUNON_DICE, BRUNON_CONTRACT, AQUAMARINE, CHRYSOBERYL, GEM_BOX, COAL_PIECE, BRUNON_LETTER, BERRY_TART, BAT_DIAGRAM, STAR_DIAMOND);
		
		addQuestStart(GOUPH);
		addTalkId(GOUPH, REEP, MURDOC, AIRY, BRUNON, MARON, TOROCCO);
		
		addMyDying(GOBLIN_BRIGAND_LEADER, GOBLIN_BRIGAND_LIEUTENANT, BLADE_BAT);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = event;
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30523-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, GOUPH_CONTRACT, 1);
		}
		else if (event.equalsIgnoreCase("30555-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, REEP_CONTRACT, 1);
			giveItems(player, ELVEN_WINE, 1);
		}
		else if (event.equalsIgnoreCase("30526-02.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BRUNON_DICE, 1);
			giveItems(player, BRUNON_CONTRACT, 1);
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
					htmltext = "30523-00.htm";
				else if (player.getStatus().getLevel() < 10)
					htmltext = "30523-01.htm";
				else
					htmltext = "30523-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case GOUPH:
						if (cond == 1)
							htmltext = "30523-04.htm";
						else if (cond > 1 && cond < 7)
							htmltext = "30523-05.htm";
						else if (cond == 7)
						{
							htmltext = "30523-06.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GEM_BOX, 1);
							giveItems(player, COAL_PIECE, 1);
						}
						else if (cond > 7 && cond < 12)
							htmltext = "30523-07.htm";
						else if (cond == 12)
						{
							htmltext = "30523-08.htm";
							takeItems(player, STAR_DIAMOND, -1);
							giveItems(player, SILVERSMITH_HAMMER, 1);
							
							rewardNewbieShots(player, 7000, 3000);
							rewardItems(player, LESSER_HEALING_POTION, 100);
							rewardItems(player, ECHO_BATTLE, 10);
							rewardItems(player, ECHO_LOVE, 10);
							rewardItems(player, ECHO_SOLITUDE, 10);
							rewardItems(player, ECHO_FEAST, 10);
							rewardItems(player, ECHO_CELEBRATION, 10);
							
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case REEP:
						if (cond == 1)
						{
							htmltext = "30516-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GOUPH_CONTRACT, 1);
							giveItems(player, REEP_CONTRACT, 1);
						}
						else if (cond > 1)
							htmltext = "30516-02.htm";
						break;
					
					case TOROCCO:
						if (cond == 2)
							htmltext = "30555-01.htm";
						else if (cond == 3)
							htmltext = "30555-03.htm";
						else if (cond == 7)
							htmltext = "30555-04.htm";
						else if (cond > 7)
							htmltext = "30555-05.htm";
						break;
					
					case MARON:
						if (cond == 3)
						{
							htmltext = "30529-01.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ELVEN_WINE, 1);
							giveItems(player, BRUNON_DICE, 1);
						}
						else if (cond == 4)
							htmltext = "30529-02.htm";
						else if (cond > 4)
							htmltext = "30529-03.htm";
						break;
					
					case BRUNON:
						if (cond == 4)
							htmltext = "30526-01.htm";
						else if (cond == 5)
							htmltext = "30526-03.htm";
						else if (cond == 6)
						{
							htmltext = "30526-04.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BRUNON_CONTRACT, 1);
							takeItems(player, AQUAMARINE, -1);
							takeItems(player, CHRYSOBERYL, -1);
							giveItems(player, GEM_BOX, 1);
						}
						else if (cond == 7)
							htmltext = "30526-05.htm";
						else if (cond == 8)
						{
							htmltext = "30526-06.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, COAL_PIECE, 1);
							giveItems(player, BRUNON_LETTER, 1);
						}
						else if (cond == 9)
							htmltext = "30526-07.htm";
						else if (cond > 9)
							htmltext = "30526-08.htm";
						break;
					
					case MURDOC:
						if (cond == 9)
						{
							htmltext = "30521-01.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BRUNON_LETTER, 1);
							giveItems(player, BERRY_TART, 1);
						}
						else if (cond == 10)
							htmltext = "30521-02.htm";
						else if (cond > 10)
							htmltext = "30521-03.htm";
						break;
					
					case AIRY:
						if (cond == 10)
						{
							htmltext = "30522-01.htm";
							st.setCond(11);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BERRY_TART, 1);
							giveItems(player, BAT_DIAGRAM, 1);
						}
						else if (cond == 11)
							htmltext = (Rnd.nextBoolean()) ? "30522-02.htm" : "30522-04.htm";
						else if (cond == 12)
							htmltext = "30522-03.htm";
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
			case GOBLIN_BRIGAND_LEADER:
				if (st.getCond() == 5 && dropMultipleItems(player, LEADER_DROPLIST))
					st.setCond(6);
				break;
			
			case GOBLIN_BRIGAND_LIEUTENANT:
				if (st.getCond() == 5 && dropMultipleItems(player, LIEUTENANT_DROPLIST))
					st.setCond(6);
				break;
			
			case BLADE_BAT:
				if (st.getCond() == 11 && dropItems(player, STAR_DIAMOND, 1, 1, 200000))
				{
					takeItems(player, BAT_DIAGRAM, 1);
					st.setCond(12);
				}
				break;
		}
	}
}