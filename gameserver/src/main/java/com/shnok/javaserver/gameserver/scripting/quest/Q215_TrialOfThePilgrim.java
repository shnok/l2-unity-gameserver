package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q215_TrialOfThePilgrim extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q215_TrialOfThePilgrim";
	
	// Items
	private static final int BOOK_OF_SAGE = 2722;
	private static final int VOUCHER_OF_TRIAL = 2723;
	private static final int SPIRIT_OF_FLAME = 2724;
	private static final int ESSENCE_OF_FLAME = 2725;
	private static final int BOOK_OF_GERALD = 2726;
	private static final int GRAY_BADGE = 2727;
	private static final int PICTURE_OF_NAHIR = 2728;
	private static final int HAIR_OF_NAHIR = 2729;
	private static final int STATUE_OF_EINHASAD = 2730;
	private static final int BOOK_OF_DARKNESS = 2731;
	private static final int DEBRIS_OF_WILLOW = 2732;
	private static final int TAG_OF_RUMOR = 2733;
	
	// Rewards
	private static final int MARK_OF_PILGRIM = 2721;
	
	// NPCs
	private static final int SANTIAGO = 30648;
	private static final int TANAPI = 30571;
	private static final int ANCESTOR_MARTANKUS = 30649;
	private static final int GAURI_TWINKLEROCK = 30550;
	private static final int DORF = 30651;
	private static final int GERALD = 30650;
	private static final int PRIMOS = 30117;
	private static final int PETRON = 30036;
	private static final int ANDELLIA = 30362;
	private static final int URUHA = 30652;
	private static final int CASIAN = 30612;
	
	// Monsters
	private static final int LAVA_SALAMANDER = 27116;
	private static final int NAHIR = 27117;
	private static final int BLACK_WILLOW = 27118;
	
	public Q215_TrialOfThePilgrim()
	{
		super(215, "Trial of the Pilgrim");
		
		setItemsIds(BOOK_OF_SAGE, VOUCHER_OF_TRIAL, SPIRIT_OF_FLAME, ESSENCE_OF_FLAME, BOOK_OF_GERALD, GRAY_BADGE, PICTURE_OF_NAHIR, HAIR_OF_NAHIR, STATUE_OF_EINHASAD, BOOK_OF_DARKNESS, DEBRIS_OF_WILLOW, TAG_OF_RUMOR);
		
		addQuestStart(SANTIAGO);
		addTalkId(SANTIAGO, TANAPI, ANCESTOR_MARTANKUS, GAURI_TWINKLEROCK, DORF, GERALD, PRIMOS, PETRON, ANDELLIA, URUHA, CASIAN);
		
		addMyDying(LAVA_SALAMANDER, NAHIR, BLACK_WILLOW);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30648-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, VOUCHER_OF_TRIAL, 1);
			
			if (giveDimensionalDiamonds35(player))
				htmltext = "30648-04a.htm";
		}
		else if (event.equalsIgnoreCase("30649-04.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ESSENCE_OF_FLAME, 1);
			giveItems(player, SPIRIT_OF_FLAME, 1);
		}
		else if (event.equalsIgnoreCase("30650-02.htm"))
		{
			if (player.getAdena() >= 100000)
			{
				playSound(player, SOUND_ITEMGET);
				takeItems(player, 57, 100000);
				giveItems(player, BOOK_OF_GERALD, 1);
			}
			else
				htmltext = "30650-03.htm";
		}
		else if (event.equalsIgnoreCase("30652-02.htm"))
		{
			st.setCond(15);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, DEBRIS_OF_WILLOW, 1);
			giveItems(player, BOOK_OF_DARKNESS, 1);
		}
		else if (event.equalsIgnoreCase("30362-04.htm"))
		{
			st.setCond(16);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30362-05.htm"))
		{
			st.setCond(16);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BOOK_OF_DARKNESS, 1);
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
				if (player.getClassId() != ClassId.CLERIC && player.getClassId() != ClassId.ELVEN_ORACLE && player.getClassId() != ClassId.SHILLIEN_ORACLE && player.getClassId() != ClassId.ORC_SHAMAN)
					htmltext = "30648-02.htm";
				else if (player.getStatus().getLevel() < 35)
					htmltext = "30648-01.htm";
				else
					htmltext = "30648-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case SANTIAGO:
						if (cond < 17)
							htmltext = "30648-09.htm";
						else if (cond == 17)
						{
							htmltext = "30648-10.htm";
							takeItems(player, BOOK_OF_SAGE, 1);
							giveItems(player, MARK_OF_PILGRIM, 1);
							rewardExpAndSp(player, 77382, 16000);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case TANAPI:
						if (cond == 1)
						{
							htmltext = "30571-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, VOUCHER_OF_TRIAL, 1);
						}
						else if (cond < 5)
							htmltext = "30571-02.htm";
						else if (cond >= 5)
						{
							htmltext = "30571-03.htm";
							
							if (cond == 5)
							{
								st.setCond(6);
								playSound(player, SOUND_MIDDLE);
							}
						}
						break;
					
					case ANCESTOR_MARTANKUS:
						if (cond == 2)
						{
							htmltext = "30649-01.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 3)
							htmltext = "30649-02.htm";
						else if (cond == 4)
							htmltext = "30649-03.htm";
						break;
					
					case GAURI_TWINKLEROCK:
						if (cond == 6)
						{
							htmltext = "30550-01.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, TAG_OF_RUMOR, 1);
						}
						else if (cond > 6)
							htmltext = "30550-02.htm";
						break;
					
					case DORF:
						if (cond == 7)
						{
							htmltext = (!player.getInventory().hasItem(BOOK_OF_GERALD)) ? "30651-01.htm" : "30651-02.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, TAG_OF_RUMOR, 1);
							giveItems(player, GRAY_BADGE, 1);
						}
						else if (cond > 7)
							htmltext = "30651-03.htm";
						break;
					
					case GERALD:
						if (cond == 7 && !player.getInventory().hasItem(BOOK_OF_GERALD))
							htmltext = "30650-01.htm";
						else if (cond == 8 && player.getInventory().hasItem(BOOK_OF_GERALD))
						{
							htmltext = "30650-04.htm";
							playSound(player, SOUND_ITEMGET);
							takeItems(player, BOOK_OF_GERALD, 1);
							giveItems(player, 57, 100000);
						}
						break;
					
					case PRIMOS:
						if (cond == 8)
						{
							htmltext = "30117-01.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond > 8)
							htmltext = "30117-02.htm";
						break;
					
					case PETRON:
						if (cond == 9)
						{
							htmltext = "30036-01.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, PICTURE_OF_NAHIR, 1);
						}
						else if (cond == 10)
							htmltext = "30036-02.htm";
						else if (cond == 11)
						{
							htmltext = "30036-03.htm";
							st.setCond(12);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, HAIR_OF_NAHIR, 1);
							takeItems(player, PICTURE_OF_NAHIR, 1);
							giveItems(player, STATUE_OF_EINHASAD, 1);
						}
						else if (cond > 11)
							htmltext = "30036-04.htm";
						break;
					
					case ANDELLIA:
						if (cond == 12)
						{
							if (player.getStatus().getLevel() < 36)
								htmltext = "30362-01a.htm";
							else
							{
								htmltext = "30362-01.htm";
								st.setCond(13);
								playSound(player, SOUND_MIDDLE);
							}
						}
						else if (cond == 13)
							htmltext = (Rnd.nextBoolean()) ? "30362-02.htm" : "30362-02a.htm";
						else if (cond == 14)
							htmltext = "30362-07.htm";
						else if (cond == 15)
							htmltext = "30362-03.htm";
						else if (cond == 16)
							htmltext = "30362-06.htm";
						break;
					
					case URUHA:
						if (cond == 14)
							htmltext = "30652-01.htm";
						else if (cond == 15)
							htmltext = "30652-03.htm";
						break;
					
					case CASIAN:
						if (cond == 16)
						{
							htmltext = "30612-01.htm";
							st.setCond(17);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BOOK_OF_DARKNESS, 1);
							takeItems(player, GRAY_BADGE, 1);
							takeItems(player, SPIRIT_OF_FLAME, 1);
							takeItems(player, STATUE_OF_EINHASAD, 1);
							giveItems(player, BOOK_OF_SAGE, 1);
						}
						else if (cond == 17)
							htmltext = "30612-02.htm";
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
			case LAVA_SALAMANDER:
				if (st.getCond() == 3 && dropItems(player, ESSENCE_OF_FLAME, 1, 1, 200000))
					st.setCond(4);
				break;
			
			case NAHIR:
				if (st.getCond() == 10 && dropItems(player, HAIR_OF_NAHIR, 1, 1, 200000))
					st.setCond(11);
				break;
			
			case BLACK_WILLOW:
				if (st.getCond() == 13 && dropItems(player, DEBRIS_OF_WILLOW, 1, 1, 200000))
					st.setCond(14);
				break;
		}
	}
}