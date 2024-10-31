package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.network.serverpackets.actor.UserInfo;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q422_RepentYourSins extends Quest
{
	private static final String QUEST_NAME = "Q422_RepentYourSins";
	
	// Items
	private static final int RATMAN_SCAVENGER_SKULL = 4326;
	private static final int TUREK_WAR_HOUND_TAIL = 4327;
	private static final int TYRANT_KINGPIN_HEART = 4328;
	private static final int TRISALIM_TARANTULA_VENOM_SAC = 4329;
	
	private static final int QITEM_PENITENT_MANACLES = 4330;
	private static final int MANUAL_OF_MANACLES = 4331;
	private static final int PENITENT_MANACLES = 4425;
	private static final int LEFT_PENITENT_MANACLES = 4426;
	
	private static final int SILVER_NUGGET = 1873;
	private static final int ADAMANTINE_NUGGET = 1877;
	private static final int BLACKSMITH_FRAME = 1892;
	private static final int COKES = 1879;
	private static final int STEEL = 1880;
	
	// NPCs
	private static final int BLACK_JUDGE = 30981;
	private static final int KATARI = 30668;
	private static final int PIOTUR = 30597;
	private static final int CASIAN = 30612;
	private static final int JOAN = 30718;
	private static final int PUSHKIN = 30300;
	
	public Q422_RepentYourSins()
	{
		super(422, "Repent Your Sins");
		
		setItemsIds(RATMAN_SCAVENGER_SKULL, TUREK_WAR_HOUND_TAIL, TYRANT_KINGPIN_HEART, TRISALIM_TARANTULA_VENOM_SAC, MANUAL_OF_MANACLES, PENITENT_MANACLES, QITEM_PENITENT_MANACLES);
		
		addQuestStart(BLACK_JUDGE);
		addTalkId(BLACK_JUDGE, KATARI, PIOTUR, CASIAN, JOAN, PUSHKIN);
		
		addMyDying(20039, 20494, 20193, 20561);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("Start"))
		{
			st.setCond(1);
			if (player.getStatus().getLevel() <= 20)
			{
				htmltext = "30981-03.htm";
				st.setCond(2);
			}
			else if (player.getStatus().getLevel() >= 20 && player.getStatus().getLevel() <= 30)
			{
				htmltext = "30981-04.htm";
				st.setCond(3);
			}
			else if (player.getStatus().getLevel() >= 30 && player.getStatus().getLevel() <= 40)
			{
				htmltext = "30981-05.htm";
				st.setCond(4);
			}
			else
			{
				htmltext = "30981-06.htm";
				st.setCond(5);
			}
			st.setState(QuestStatus.STARTED);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30981-11.htm"))
		{
			if (!player.getInventory().hasItem(PENITENT_MANACLES))
			{
				int cond = st.getCond();
				
				// Case you return back the qitem to Black Judge. She rewards you with the pet item.
				if (cond == 15)
				{
					st.setCond(16);
					st.set("level", player.getStatus().getLevel());
					playSound(player, SOUND_ITEMGET);
					takeItems(player, QITEM_PENITENT_MANACLES, -1);
					giveItems(player, PENITENT_MANACLES, 1);
				}
				// Case you return back to Black Judge with leftover of previous quest.
				else if (cond == 16)
				{
					st.set("level", player.getStatus().getLevel());
					playSound(player, SOUND_ITEMGET);
					takeItems(player, LEFT_PENITENT_MANACLES, -1);
					giveItems(player, PENITENT_MANACLES, 1);
				}
			}
		}
		else if (event.equalsIgnoreCase("30981-19.htm"))
		{
			if (player.getInventory().hasItem(LEFT_PENITENT_MANACLES))
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(16);
				playSound(player, SOUND_ACCEPT);
			}
		}
		else if (event.equalsIgnoreCase("Pk"))
		{
			final Summon summon = player.getSummon();
			
			// If Sin Eater is currently summoned, show a warning.
			if (summon != null && summon.getNpcId() == 12564)
				htmltext = "30981-16.htm";
			// If Sin Eater level is bigger than registered level, decrease PK counter by 1-10.
			else if (findSinEaterLvl(player) > st.getInteger("level"))
			{
				takeItems(player, PENITENT_MANACLES, 1);
				giveItems(player, LEFT_PENITENT_MANACLES, 1);
				
				int removePkAmount = Rnd.get(10) + 1;
				
				// Player's PKs are lower than random amount ; finish the quest.
				if (player.getPkKills() <= removePkAmount)
				{
					htmltext = "30981-15.htm";
					playSound(player, SOUND_FINISH);
					st.exitQuest(true);
					
					player.setPkKills(0);
					player.sendPacket(new UserInfo(player));
				}
				// Player's PK are bigger than random amount ; continue the quest.
				else
				{
					htmltext = "30981-14.htm";
					st.set("level", player.getStatus().getLevel());
					playSound(player, SOUND_MIDDLE);
					
					player.setPkKills(player.getPkKills() - removePkAmount);
					player.sendPacket(new UserInfo(player));
				}
			}
		}
		else if (event.equalsIgnoreCase("Quit"))
		{
			htmltext = "30981-20.htm";
			
			takeItems(player, RATMAN_SCAVENGER_SKULL, -1);
			takeItems(player, TUREK_WAR_HOUND_TAIL, -1);
			takeItems(player, TYRANT_KINGPIN_HEART, -1);
			takeItems(player, TRISALIM_TARANTULA_VENOM_SAC, -1);
			
			takeItems(player, MANUAL_OF_MANACLES, -1);
			takeItems(player, PENITENT_MANACLES, -1);
			takeItems(player, QITEM_PENITENT_MANACLES, -1);
			
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getAlreadyCompletedMsg();
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getPkKills() >= 1)
					htmltext = (player.getInventory().hasItem(LEFT_PENITENT_MANACLES)) ? "30981-18.htm" : "30981-02.htm";
				else
					htmltext = "30981-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case BLACK_JUDGE:
						if (cond <= 9)
							htmltext = "30981-07.htm";
						else if (cond > 9 && cond < 14)
						{
							htmltext = "30981-08.htm";
							st.setCond(14);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, MANUAL_OF_MANACLES, 1);
						}
						else if (cond == 14)
							htmltext = "30981-09.htm";
						else if (cond == 15)
							htmltext = "30981-10.htm";
						else if (cond == 16)
						{
							if (player.getInventory().hasItem(PENITENT_MANACLES))
								htmltext = (findSinEaterLvl(player) > st.getInteger("level")) ? "30981-13.htm" : "30981-12.htm";
							else
								htmltext = "30981-18.htm";
						}
						break;
					
					case KATARI:
						if (cond == 2)
						{
							htmltext = "30668-01.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 6)
						{
							if (player.getInventory().getItemCount(RATMAN_SCAVENGER_SKULL) < 10)
								htmltext = "30668-02.htm";
							else
							{
								htmltext = "30668-03.htm";
								st.setCond(10);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, RATMAN_SCAVENGER_SKULL, -1);
							}
						}
						else if (cond == 10)
							htmltext = "30668-04.htm";
						break;
					
					case PIOTUR:
						if (cond == 3)
						{
							htmltext = "30597-01.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 7)
						{
							if (player.getInventory().getItemCount(TUREK_WAR_HOUND_TAIL) < 10)
								htmltext = "30597-02.htm";
							else
							{
								htmltext = "30597-03.htm";
								st.setCond(11);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, TUREK_WAR_HOUND_TAIL, -1);
							}
						}
						else if (cond == 11)
							htmltext = "30597-04.htm";
						break;
					
					case CASIAN:
						if (cond == 4)
						{
							htmltext = "30612-01.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 8)
						{
							if (!player.getInventory().hasItem(TYRANT_KINGPIN_HEART))
								htmltext = "30612-02.htm";
							else
							{
								htmltext = "30612-03.htm";
								st.setCond(12);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, TYRANT_KINGPIN_HEART, -1);
							}
						}
						else if (cond == 12)
							htmltext = "30612-04.htm";
						break;
					
					case JOAN:
						if (cond == 5)
						{
							htmltext = "30718-01.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 9)
						{
							if (player.getInventory().getItemCount(TRISALIM_TARANTULA_VENOM_SAC) < 3)
								htmltext = "30718-02.htm";
							else
							{
								htmltext = "30718-03.htm";
								st.setCond(13);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, TRISALIM_TARANTULA_VENOM_SAC, -1);
							}
						}
						else if (cond == 13)
							htmltext = "30718-04.htm";
						break;
					
					case PUSHKIN:
						if (cond == 14 && player.getInventory().getItemCount(MANUAL_OF_MANACLES) == 1)
						{
							if (player.getInventory().getItemCount(SILVER_NUGGET) < 10 || player.getInventory().getItemCount(STEEL) < 5 || player.getInventory().getItemCount(ADAMANTINE_NUGGET) < 2 || player.getInventory().getItemCount(COKES) < 10 || player.getInventory().getItemCount(BLACKSMITH_FRAME) < 1)
								htmltext = "30300-02.htm";
							else
							{
								htmltext = "30300-01.htm";
								st.setCond(15);
								playSound(player, SOUND_MIDDLE);
								
								takeItems(player, MANUAL_OF_MANACLES, 1);
								takeItems(player, SILVER_NUGGET, 10);
								takeItems(player, ADAMANTINE_NUGGET, 2);
								takeItems(player, COKES, 10);
								takeItems(player, STEEL, 5);
								takeItems(player, BLACKSMITH_FRAME, 1);
								
								giveItems(player, QITEM_PENITENT_MANACLES, 1);
							}
						}
						else if (player.getInventory().hasAtLeastOneItem(QITEM_PENITENT_MANACLES, PENITENT_MANACLES, LEFT_PENITENT_MANACLES))
							htmltext = "30300-03.htm";
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
			case 20039:
				if (st.getCond() == 6)
					dropItemsAlways(player, RATMAN_SCAVENGER_SKULL, 1, 10);
				break;
			
			case 20494:
				if (st.getCond() == 7)
					dropItemsAlways(player, TUREK_WAR_HOUND_TAIL, 1, 10);
				break;
			
			case 20193:
				if (st.getCond() == 8)
					dropItemsAlways(player, TYRANT_KINGPIN_HEART, 1, 1);
				break;
			
			case 20561:
				if (st.getCond() == 9)
					dropItemsAlways(player, TRISALIM_TARANTULA_VENOM_SAC, 1, 3);
				break;
		}
	}
	
	private static int findSinEaterLvl(Player player)
	{
		return player.getInventory().getItemByItemId(PENITENT_MANACLES).getEnchantLevel();
	}
}