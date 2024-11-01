package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q024_InhabitantsOfTheForestOfTheDead extends Quest
{
	private static final String QUEST_NAME = "Q024_InhabitantsOfTheForestOfTheDead";
	
	// NPCs
	private static final int NIGHT_DORIAN = 25332;
	private static final int DORIAN = 31389;
	private static final int MYSTERIOUS_WIZARD = 31522;
	private static final int TOMBSTONE = 31531;
	private static final int LIDIA_MAID = 31532;
	
	// MOBs
	private static final int BONE_SNATCHER = 21557;
	private static final int BONE_SNATCHER_A = 21558;
	private static final int BONE_SHAPER = 21560;
	private static final int BONE_COLLECTOR = 21563;
	private static final int SKULL_COLLECTOR = 21564;
	private static final int BONE_ANIMATOR = 21565;
	private static final int SKULL_ANIMATOR = 21566;
	private static final int BONE_SLAYER = 21567;
	
	// Items
	private static final int LIDIAS_LETTER = 7065;
	private static final int LIDIAS_HAIRPIN = 7148;
	private static final int SUSPICIOUS_TOTEM_DOLL = 7151;
	private static final int FLOWER_BOUQUET = 7152;
	private static final int SILVER_CROSS_OF_EINHASAD = 7153;
	private static final int BROKEN_SILVER_CROSS_OF_EINHASAD = 7154;
	private static final int SUSPICIOUS_TOTEM_DOLL_2 = 7156;
	
	public Q024_InhabitantsOfTheForestOfTheDead()
	{
		super(24, "Inhabitants of the Forest of the Dead");
		
		setItemsIds(LIDIAS_LETTER, LIDIAS_HAIRPIN, SUSPICIOUS_TOTEM_DOLL, FLOWER_BOUQUET, SILVER_CROSS_OF_EINHASAD, BROKEN_SILVER_CROSS_OF_EINHASAD);
		
		addQuestStart(DORIAN);
		addTalkId(DORIAN, MYSTERIOUS_WIZARD, LIDIA_MAID, TOMBSTONE);
		
		addMyDying(BONE_SNATCHER, BONE_SNATCHER_A, BONE_SHAPER, BONE_COLLECTOR, SKULL_COLLECTOR, BONE_ANIMATOR, SKULL_ANIMATOR, BONE_SLAYER);
		addSeeCreature(NIGHT_DORIAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31389-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("state", 1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, FLOWER_BOUQUET, 1);
		}
		else if (event.equalsIgnoreCase("31389-08.htm"))
			st.set("state", 3);
		else if (event.equalsIgnoreCase("31389-13.htm"))
		{
			st.setCond(3);
			st.set("state", 4);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, SILVER_CROSS_OF_EINHASAD, 1);
		}
		else if (event.equalsIgnoreCase("31389-18.htm"))
			playSound(player, "InterfaceSound.charstat_open_01");
		else if (event.equalsIgnoreCase("31389-19.htm"))
		{
			st.setCond(5);
			st.set("state", 5);
			takeItems(player, BROKEN_SILVER_CROSS_OF_EINHASAD, -1);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31522-03.htm"))
		{
			st.set("state", 12);
			takeItems(player, SUSPICIOUS_TOTEM_DOLL, -1);
		}
		else if (event.equalsIgnoreCase("31522-08.htm"))
		{
			st.setCond(11);
			st.set("state", 13);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31522-17.htm"))
			st.set("state", 14);
		else if (event.equalsIgnoreCase("31522-21.htm"))
		{
			giveItems(player, SUSPICIOUS_TOTEM_DOLL_2, 1);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("31532-04.htm"))
		{
			st.setCond(6);
			st.set("state", 6);
			giveItems(player, LIDIAS_LETTER, 1);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31532-06.htm"))
		{
			if (player.getInventory().hasItem(LIDIAS_HAIRPIN))
			{
				st.set("state", 8);
				takeItems(player, LIDIAS_LETTER, -1);
				takeItems(player, LIDIAS_HAIRPIN, -1);
			}
			else
			{
				st.setCond(7);
				st.set("state", 7);
				htmltext = "31532-07.htm";
			}
		}
		else if (event.equalsIgnoreCase("31532-10.htm"))
			st.set("state", 9);
		else if (event.equalsIgnoreCase("31532-14.htm"))
			st.set("state", 10);
		else if (event.equalsIgnoreCase("31532-19.htm"))
		{
			st.setCond(9);
			st.set("state", 11);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31531-02.htm"))
		{
			st.setCond(2);
			st.set("state", 2);
			takeItems(player, FLOWER_BOUQUET, -1);
			playSound(player, SOUND_MIDDLE);
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
				QuestState st2 = player.getQuestList().getQuestState("Q023_LidiasHeart");
				if (st2 == null || !st2.isCompleted() || player.getStatus().getLevel() < 65)
					htmltext = "31389-02.htm";
				else
					htmltext = "31389-01.htm";
				break;
			
			case STARTED:
				final int state = st.getInteger("state");
				switch (npc.getNpcId())
				{
					case DORIAN:
						if (state == 1)
							htmltext = "31389-04.htm";
						else if (state == 2)
							htmltext = "31389-05.htm";
						else if (state == 3)
							htmltext = "31389-09.htm";
						else if (state == 4)
						{
							if (player.getInventory().hasItem(SILVER_CROSS_OF_EINHASAD))
								htmltext = "31389-14.htm";
							else if (player.getInventory().hasItem(BROKEN_SILVER_CROSS_OF_EINHASAD))
								htmltext = "31389-15.htm";
						}
						else if (state == 5)
							htmltext = "31389-20.htm";
						else if (state == 7 && !player.getInventory().hasItem(LIDIAS_HAIRPIN))
						{
							htmltext = "31389-21.htm";
							st.setCond(8);
							giveItems(player, LIDIAS_HAIRPIN, 1);
							playSound(player, SOUND_MIDDLE);
						}
						else if ((state == 7 && player.getInventory().hasItem(LIDIAS_HAIRPIN)) || state == 6)
							htmltext = "31389-22.htm";
						break;
					
					case MYSTERIOUS_WIZARD:
						if (state == 11 && player.getInventory().hasItem(SUSPICIOUS_TOTEM_DOLL))
							htmltext = "31522-01.htm";
						else if (state == 12)
							htmltext = "31522-04.htm";
						else if (state == 13)
							htmltext = "31522-09.htm";
						else if (state == 14)
							htmltext = "31522-18.htm";
						break;
					
					case LIDIA_MAID:
						if (state == 5)
							htmltext = "31532-01.htm";
						else if (state == 6 && player.getInventory().hasItem(LIDIAS_LETTER))
							htmltext = "31532-05.htm";
						else if (state == 7)
							htmltext = "31532-07a.htm";
						else if (state == 8)
							htmltext = "31532-08.htm";
						else if (state == 9)
							htmltext = "31532-11.htm";
						else if (state == 10)
							htmltext = "31532-15.htm";
						else if (state == 11)
							htmltext = "31532-20.htm";
						break;
					
					case TOMBSTONE:
						if (state == 1 && player.getInventory().hasItem(FLOWER_BOUQUET))
						{
							htmltext = "31531-01.htm";
							playSound(player, "AmdSound.d_wind_loot_02");
						}
						else if (state == 2)
							htmltext = "31531-03.htm";
						break;
				}
				break;
			
			case COMPLETED:
				if (npc.getNpcId() == MYSTERIOUS_WIZARD)
					htmltext = "31522-22.htm";
				else
					htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 9);
		if (st == null)
			return;
		
		if (dropItems(player, SUSPICIOUS_TOTEM_DOLL, 1, 1, 100000))
			st.setCond(10);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player player)
		{
			final QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
			if (st == null)
				return;
			
			if (st.getCond() == 3)
			{
				st.setCond(4);
				takeItems(player, SILVER_CROSS_OF_EINHASAD, -1);
				giveItems(player, BROKEN_SILVER_CROSS_OF_EINHASAD, 1);
				playSound(player, SOUND_MIDDLE);
				npc.broadcastNpcSay(NpcStringId.ID_2450);
			}
		}
	}
}