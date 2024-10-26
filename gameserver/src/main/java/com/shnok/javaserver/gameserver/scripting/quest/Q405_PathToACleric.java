package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q405_PathToACleric extends Quest
{
	private static final String QUEST_NAME = "Q405_PathToACleric";
	
	// Items
	private static final int LETTER_OF_ORDER_1 = 1191;
	private static final int LETTER_OF_ORDER_2 = 1192;
	private static final int LIONEL_BOOK = 1193;
	private static final int BOOK_OF_VIVYAN = 1194;
	private static final int BOOK_OF_SIMPLON = 1195;
	private static final int BOOK_OF_PRAGA = 1196;
	private static final int CERTIFICATE_OF_GALLINT = 1197;
	private static final int PENDANT_OF_MOTHER = 1198;
	private static final int NECKLACE_OF_MOTHER = 1199;
	private static final int LIONEL_COVENANT = 1200;
	
	// NPCs
	private static final int GALLINT = 30017;
	private static final int ZIGAUNT = 30022;
	private static final int VIVYAN = 30030;
	private static final int PRAGA = 30333;
	private static final int SIMPLON = 30253;
	private static final int LIONEL = 30408;
	
	// Reward
	private static final int MARK_OF_FATE = 1201;
	
	public Q405_PathToACleric()
	{
		super(405, "Path to a Cleric");
		
		setItemsIds(LETTER_OF_ORDER_1, BOOK_OF_SIMPLON, BOOK_OF_PRAGA, BOOK_OF_VIVYAN, NECKLACE_OF_MOTHER, PENDANT_OF_MOTHER, LETTER_OF_ORDER_2, LIONEL_BOOK, CERTIFICATE_OF_GALLINT, LIONEL_COVENANT);
		
		addQuestStart(ZIGAUNT);
		addTalkId(ZIGAUNT, SIMPLON, PRAGA, VIVYAN, LIONEL, GALLINT);
		
		addMyDying(20029, 20026);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30022-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, LETTER_OF_ORDER_1, 1);
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
				if (player.getClassId() != ClassId.HUMAN_MYSTIC)
					htmltext = (player.getClassId() == ClassId.CLERIC) ? "30022-02a.htm" : "30022-02.htm";
				else if (player.getStatus().getLevel() < 19)
					htmltext = "30022-03.htm";
				else if (player.getInventory().hasItem(MARK_OF_FATE))
					htmltext = "30022-04.htm";
				else
					htmltext = "30022-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ZIGAUNT:
						if (cond == 1)
							htmltext = "30022-06.htm";
						else if (cond == 2)
						{
							htmltext = "30022-08.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BOOK_OF_PRAGA, 1);
							takeItems(player, BOOK_OF_VIVYAN, 1);
							takeItems(player, BOOK_OF_SIMPLON, 3);
							takeItems(player, LETTER_OF_ORDER_1, 1);
							giveItems(player, LETTER_OF_ORDER_2, 1);
						}
						else if (cond > 2 && cond < 6)
							htmltext = "30022-07.htm";
						else if (cond == 6)
						{
							htmltext = "30022-09.htm";
							takeItems(player, LETTER_OF_ORDER_2, 1);
							takeItems(player, LIONEL_COVENANT, 1);
							giveItems(player, MARK_OF_FATE, 1);
							rewardExpAndSp(player, 3200, 5610);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case SIMPLON:
						if (cond == 1 && !player.getInventory().hasItem(BOOK_OF_SIMPLON))
						{
							htmltext = "30253-01.htm";
							playSound(player, SOUND_ITEMGET);
							giveItems(player, BOOK_OF_SIMPLON, 3);
						}
						else if (cond > 1 || player.getInventory().hasItem(BOOK_OF_SIMPLON))
							htmltext = "30253-02.htm";
						break;
					
					case PRAGA:
						if (cond == 1)
						{
							if (!player.getInventory().hasItem(BOOK_OF_PRAGA) && !player.getInventory().hasItem(NECKLACE_OF_MOTHER) && player.getInventory().hasItem(BOOK_OF_SIMPLON))
							{
								htmltext = "30333-01.htm";
								playSound(player, SOUND_ITEMGET);
								giveItems(player, NECKLACE_OF_MOTHER, 1);
							}
							else if (!player.getInventory().hasItem(PENDANT_OF_MOTHER))
								htmltext = "30333-02.htm";
							else if (player.getInventory().hasItem(PENDANT_OF_MOTHER))
							{
								htmltext = "30333-03.htm";
								takeItems(player, NECKLACE_OF_MOTHER, 1);
								takeItems(player, PENDANT_OF_MOTHER, 1);
								giveItems(player, BOOK_OF_PRAGA, 1);
								
								if (player.getInventory().hasItem(BOOK_OF_VIVYAN))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
						}
						else if (cond > 1 || (player.getInventory().hasItem(BOOK_OF_PRAGA)))
							htmltext = "30333-04.htm";
						break;
					
					case VIVYAN:
						if (cond == 1 && !player.getInventory().hasItem(BOOK_OF_VIVYAN) && player.getInventory().hasItem(BOOK_OF_SIMPLON))
						{
							htmltext = "30030-01.htm";
							giveItems(player, BOOK_OF_VIVYAN, 1);
							
							if (player.getInventory().hasItem(BOOK_OF_PRAGA))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (cond > 1 || player.getInventory().hasItem(BOOK_OF_VIVYAN))
							htmltext = "30030-02.htm";
						break;
					
					case LIONEL:
						if (cond < 3)
							htmltext = "30408-02.htm";
						else if (cond == 3)
						{
							htmltext = "30408-01.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, LIONEL_BOOK, 1);
						}
						else if (cond == 4)
							htmltext = "30408-03.htm";
						else if (cond == 5)
						{
							htmltext = "30408-04.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CERTIFICATE_OF_GALLINT, 1);
							giveItems(player, LIONEL_COVENANT, 1);
						}
						else if (cond == 6)
							htmltext = "30408-05.htm";
						break;
					
					case GALLINT:
						if (cond == 4)
						{
							htmltext = "30017-01.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LIONEL_BOOK, 1);
							giveItems(player, CERTIFICATE_OF_GALLINT, 1);
						}
						else if (cond > 4)
							htmltext = "30017-02.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		if (player.getInventory().hasItem(NECKLACE_OF_MOTHER) && !player.getInventory().hasItem(PENDANT_OF_MOTHER))
		{
			playSound(player, SOUND_MIDDLE);
			giveItems(player, PENDANT_OF_MOTHER, 1);
		}
	}
}