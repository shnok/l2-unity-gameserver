package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q212_TrialOfDuty extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q212_TrialOfDuty";
	
	// Items
	private static final int LETTER_OF_DUSTIN = 2634;
	private static final int KNIGHTS_TEAR = 2635;
	private static final int MIRROR_OF_ORPIC = 2636;
	private static final int TEAR_OF_CONFESSION = 2637;
	private static final int REPORT_PIECE_1 = 2638;
	private static final int REPORT_PIECE_2 = 2639;
	private static final int TEAR_OF_LOYALTY = 2640;
	private static final int MILITAS_ARTICLE = 2641;
	private static final int SAINTS_ASHES_URN = 2642;
	private static final int ATHEBALDT_SKULL = 2643;
	private static final int ATHEBALDT_RIBS = 2644;
	private static final int ATHEBALDT_SHIN = 2645;
	private static final int LETTER_OF_WINDAWOOD = 2646;
	private static final int OLD_KNIGHT_SWORD = 3027;
	
	// Rewards
	private static final int MARK_OF_DUTY = 2633;
	
	// NPCs
	private static final int HANNAVALT = 30109;
	private static final int DUSTIN = 30116;
	private static final int SIR_COLLIN = 30311;
	private static final int SIR_ARON = 30653;
	private static final int SIR_KIEL = 30654;
	private static final int SILVERSHADOW = 30655;
	private static final int SPIRIT_TALIANUS = 30656;
	
	public Q212_TrialOfDuty()
	{
		super(212, "Trial of Duty");
		
		setItemsIds(LETTER_OF_DUSTIN, KNIGHTS_TEAR, MIRROR_OF_ORPIC, TEAR_OF_CONFESSION, REPORT_PIECE_1, REPORT_PIECE_2, TEAR_OF_LOYALTY, MILITAS_ARTICLE, SAINTS_ASHES_URN, ATHEBALDT_SKULL, ATHEBALDT_RIBS, ATHEBALDT_SHIN, LETTER_OF_WINDAWOOD, OLD_KNIGHT_SWORD);
		
		addQuestStart(HANNAVALT);
		addTalkId(HANNAVALT, DUSTIN, SIR_COLLIN, SIR_ARON, SIR_KIEL, SILVERSHADOW, SPIRIT_TALIANUS);
		
		addMyDying(20144, 20190, 20191, 20200, 20201, 20270, 27119, 20577, 20578, 20579, 20580, 20581, 20582);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30109-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			
			if (giveDimensionalDiamonds35(player))
				htmltext = "30109-04a.htm";
		}
		else if (event.equalsIgnoreCase("30116-05.htm"))
		{
			st.setCond(14);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, TEAR_OF_LOYALTY, 1);
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
				if (player.getClassId() != ClassId.KNIGHT && player.getClassId() != ClassId.ELVEN_KNIGHT && player.getClassId() != ClassId.PALUS_KNIGHT)
					htmltext = "30109-02.htm";
				else if (player.getStatus().getLevel() < 35)
					htmltext = "30109-01.htm";
				else
					htmltext = "30109-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case HANNAVALT:
						if (cond == 18)
						{
							htmltext = "30109-05.htm";
							takeItems(player, LETTER_OF_DUSTIN, 1);
							giveItems(player, MARK_OF_DUTY, 1);
							rewardExpAndSp(player, 79832, 3750);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						else
							htmltext = "30109-04a.htm";
						break;
					
					case SIR_ARON:
						if (cond == 1)
						{
							htmltext = "30653-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, OLD_KNIGHT_SWORD, 1);
						}
						else if (cond == 2)
							htmltext = "30653-02.htm";
						else if (cond == 3)
						{
							htmltext = "30653-03.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, KNIGHTS_TEAR, 1);
							takeItems(player, OLD_KNIGHT_SWORD, 1);
						}
						else if (cond > 3)
							htmltext = "30653-04.htm";
						break;
					
					case SIR_KIEL:
						if (cond == 4)
						{
							htmltext = "30654-01.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 5)
							htmltext = "30654-02.htm";
						else if (cond == 6)
						{
							htmltext = "30654-03.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, MIRROR_OF_ORPIC, 1);
						}
						else if (cond == 7)
							htmltext = "30654-04.htm";
						else if (cond == 9)
						{
							htmltext = "30654-05.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, TEAR_OF_CONFESSION, 1);
						}
						else if (cond > 9)
							htmltext = "30654-06.htm";
						break;
					
					case SPIRIT_TALIANUS:
						if (cond == 8)
						{
							htmltext = "30656-01.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, MIRROR_OF_ORPIC, 1);
							takeItems(player, REPORT_PIECE_2, 1);
							giveItems(player, TEAR_OF_CONFESSION, 1);
							
							// Despawn the spirit.
							npc.deleteMe();
						}
						break;
					
					case SILVERSHADOW:
						if (cond == 10)
						{
							if (player.getStatus().getLevel() < 35)
								htmltext = "30655-01.htm";
							else
							{
								htmltext = "30655-02.htm";
								st.setCond(11);
								playSound(player, SOUND_MIDDLE);
							}
						}
						else if (cond == 11)
							htmltext = "30655-03.htm";
						else if (cond == 12)
						{
							htmltext = "30655-04.htm";
							st.setCond(13);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, MILITAS_ARTICLE, -1);
							giveItems(player, TEAR_OF_LOYALTY, 1);
						}
						else if (cond == 13)
							htmltext = "30655-05.htm";
						break;
					
					case DUSTIN:
						if (cond == 13)
							htmltext = "30116-01.htm";
						else if (cond == 14)
							htmltext = "30116-06.htm";
						else if (cond == 15)
						{
							htmltext = "30116-07.htm";
							st.setCond(16);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ATHEBALDT_SKULL, 1);
							takeItems(player, ATHEBALDT_RIBS, 1);
							takeItems(player, ATHEBALDT_SHIN, 1);
							giveItems(player, SAINTS_ASHES_URN, 1);
						}
						else if (cond == 16)
							htmltext = "30116-09.htm";
						else if (cond == 17)
						{
							htmltext = "30116-08.htm";
							st.setCond(18);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LETTER_OF_WINDAWOOD, 1);
							giveItems(player, LETTER_OF_DUSTIN, 1);
						}
						else if (cond == 18)
							htmltext = "30116-10.htm";
						break;
					
					case SIR_COLLIN:
						if (cond == 16)
						{
							htmltext = "30311-01.htm";
							st.setCond(17);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SAINTS_ASHES_URN, 1);
							giveItems(player, LETTER_OF_WINDAWOOD, 1);
						}
						else if (cond > 16)
							htmltext = "30311-02.htm";
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
		
		int cond = st.getCond();
		switch (npc.getNpcId())
		{
			case 20190, 20191:
				if (cond == 2 && Rnd.get(10) < 1)
				{
					playSound(player, SOUND_BEFORE_BATTLE);
					addSpawn(27119, npc, false, 120000, true);
				}
				break;
			
			case 27119:
				if (cond == 2 && player.getInventory().getItemIdFrom(Paperdoll.RHAND) == OLD_KNIGHT_SWORD)
				{
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, KNIGHTS_TEAR, 1);
				}
				break;
			
			case 20200, 20201:
				if (cond == 5 && dropItemsAlways(player, REPORT_PIECE_1, 1, 10))
				{
					st.setCond(6);
					takeItems(player, REPORT_PIECE_1, -1);
					giveItems(player, REPORT_PIECE_2, 1);
				}
				break;
			
			case 20144:
				if ((cond == 7 || cond == 8) && Rnd.get(100) < 33)
				{
					if (cond == 7)
					{
						st.setCond(8);
						playSound(player, SOUND_MIDDLE);
					}
					addSpawn(30656, npc, false, 300000, true);
				}
				break;
			
			case 20577, 20578, 20579, 20580, 20581, 20582:
				if (cond == 11 && dropItemsAlways(player, MILITAS_ARTICLE, 1, 20))
					st.setCond(12);
				break;
			
			case 20270:
				if (cond == 14 && Rnd.nextBoolean())
				{
					if (!player.getInventory().hasItem(ATHEBALDT_SKULL))
					{
						playSound(player, SOUND_ITEMGET);
						giveItems(player, ATHEBALDT_SKULL, 1);
					}
					else if (!player.getInventory().hasItem(ATHEBALDT_RIBS))
					{
						playSound(player, SOUND_ITEMGET);
						giveItems(player, ATHEBALDT_RIBS, 1);
					}
					else if (!player.getInventory().hasItem(ATHEBALDT_SHIN))
					{
						st.setCond(15);
						playSound(player, SOUND_MIDDLE);
						giveItems(player, ATHEBALDT_SHIN, 1);
					}
				}
				break;
		}
	}
}