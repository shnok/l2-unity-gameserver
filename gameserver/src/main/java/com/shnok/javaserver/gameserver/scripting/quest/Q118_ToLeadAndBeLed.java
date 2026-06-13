package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q118_ToLeadAndBeLed extends Quest
{
	private static final String QUEST_NAME = "Q118_ToLeadAndBeLed";
	private static final String qn2 = "Q123_TheLeaderAndTheFollower";
	
	// Npc
	private static final int PINTER = 30298;
	
	// Mobs
	private static final int MAILLE_LIZARDMAN = 20919;
	private static final int MAILLE_LIZARDMAN_SCOUT = 20920;
	private static final int MAILLE_LIZARDMAN_GUARD = 20921;
	private static final int KING_OF_THE_ARANEID = 20927;
	
	// Items
	private static final int BLOOD_OF_MAILLE_LIZARDMAN = 8062;
	private static final int LEG_OF_KING_ARANEID = 8063;
	private static final int CRYSTAL_D = 1458;
	
	// Rewards
	private static final int CLAN_OATH_HELM = 7850;
	private static final int CLAN_OATH_ARMOR = 7851;
	private static final int CLAN_OATH_GAUNTLETS = 7852;
	private static final int CLAN_OATH_SABATON = 7853;
	private static final int CLAN_OATH_BRIGANDINE = 7854;
	private static final int CLAN_OATH_LEATHER_GLOVES = 7855;
	private static final int CLAN_OATH_BOOTS = 7856;
	private static final int CLAN_OATH_AKETON = 7857;
	private static final int CLAN_OATH_PADDED_GLOVES = 7858;
	private static final int CLAN_OATH_SANDALS = 7859;
	
	public Q118_ToLeadAndBeLed()
	{
		super(118, "To Lead and Be Led");
		
		setItemsIds(BLOOD_OF_MAILLE_LIZARDMAN, LEG_OF_KING_ARANEID);
		
		addQuestStart(PINTER);
		addTalkId(PINTER);
		
		addMyDying(MAILLE_LIZARDMAN, MAILLE_LIZARDMAN_SCOUT, MAILLE_LIZARDMAN_GUARD, KING_OF_THE_ARANEID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30298-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("state", 1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30298-05d.htm"))
		{
			if (player.getInventory().getItemCount(BLOOD_OF_MAILLE_LIZARDMAN) > 9)
			{
				st.setCond(3);
				st.set("state", 2);
				st.set("stateEx", 1);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BLOOD_OF_MAILLE_LIZARDMAN, -1);
			}
		}
		else if (event.equalsIgnoreCase("30298-05e.htm"))
		{
			if (player.getInventory().getItemCount(BLOOD_OF_MAILLE_LIZARDMAN) > 9)
			{
				st.setCond(4);
				st.set("state", 2);
				st.set("stateEx", 2);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BLOOD_OF_MAILLE_LIZARDMAN, -1);
			}
		}
		else if (event.equalsIgnoreCase("30298-05f.htm"))
		{
			if (player.getInventory().getItemCount(BLOOD_OF_MAILLE_LIZARDMAN) > 9)
			{
				st.setCond(5);
				st.set("state", 2);
				st.set("stateEx", 3);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BLOOD_OF_MAILLE_LIZARDMAN, -1);
			}
		}
		else if (event.equalsIgnoreCase("30298-10.htm"))
		{
			final Player academic = getApprentice(player);
			if (academic != null)
			{
				final QuestState st2 = academic.getQuestList().getQuestState(QUEST_NAME);
				if (st2 != null && st2.getInteger("state") == 2)
				{
					final int stateEx = st2.getInteger("stateEx");
					if (stateEx == 1)
					{
						if (player.getInventory().getItemCount(CRYSTAL_D) > 921)
						{
							takeItems(player, CRYSTAL_D, 922);
							st2.setCond(6);
							st2.set("state", 3);
							playSound(academic, SOUND_MIDDLE);
						}
						else
							htmltext = "30298-11.htm";
					}
					else
					{
						if (player.getInventory().getItemCount(CRYSTAL_D) > 770)
						{
							takeItems(player, CRYSTAL_D, 771);
							st2.setCond(6);
							st2.set("state", 3);
							playSound(academic, SOUND_MIDDLE);
						}
						else
							htmltext = "30298-11a.htm";
					}
				}
			}
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
				if (player.getSponsor() > 0)
				{
					QuestState st2 = player.getQuestList().getQuestState(qn2);
					if (st2 != null)
						htmltext = (st2.isCompleted()) ? "30298-02a.htm" : "30298-02b.htm";
					else
						htmltext = (player.getStatus().getLevel() > 18) ? "30298-01.htm" : "30298-02.htm";
				}
				else if (player.getApprentice() > 0)
				{
					final Player academic = getApprentice(player);
					if (academic != null)
					{
						final QuestState st3 = academic.getQuestList().getQuestState(QUEST_NAME);
						if (st3 != null)
						{
							final int state = st3.getInteger("state");
							if (state == 2)
								htmltext = "30298-08.htm";
							else if (state == 3)
								htmltext = "30298-12.htm";
							else
								htmltext = "30298-14.htm";
						}
					}
					else
						htmltext = "30298-09.htm";
				}
				break;
			
			case STARTED:
				final int state = st.getInteger("state");
				if (state == 1)
					htmltext = (player.getInventory().getItemCount(BLOOD_OF_MAILLE_LIZARDMAN) < 10) ? "30298-04.htm" : "30298-05.htm";
				else if (state == 2)
				{
					final int stateEx = st.getInteger("stateEx");
					if (player.getSponsor() == 0)
					{
						if (stateEx == 1)
							htmltext = "30298-06a.htm";
						else if (stateEx == 2)
							htmltext = "30298-06b.htm";
						else if (stateEx == 3)
							htmltext = "30298-06c.htm";
					}
					else
					{
						if (getSponsor(player))
						{
							if (stateEx == 1)
								htmltext = "30298-06.htm";
							else if (stateEx == 2)
								htmltext = "30298-06d.htm";
							else if (stateEx == 3)
								htmltext = "30298-06e.htm";
						}
						else
							htmltext = "30298-07.htm";
					}
				}
				else if (state == 3)
				{
					st.setCond(7);
					st.set("state", 4);
					playSound(player, SOUND_MIDDLE);
					htmltext = "30298-15.htm";
				}
				else if (state == 4)
				{
					if (player.getInventory().getItemCount(LEG_OF_KING_ARANEID) > 7)
					{
						htmltext = "30298-17.htm";
						
						takeItems(player, LEG_OF_KING_ARANEID, -1);
						giveItems(player, CLAN_OATH_HELM, 1);
						
						switch (st.getInteger("stateEx"))
						{
							case 1:
								giveItems(player, CLAN_OATH_ARMOR, 1);
								giveItems(player, CLAN_OATH_GAUNTLETS, 1);
								giveItems(player, CLAN_OATH_SABATON, 1);
								break;
							
							case 2:
								giveItems(player, CLAN_OATH_BRIGANDINE, 1);
								giveItems(player, CLAN_OATH_LEATHER_GLOVES, 1);
								giveItems(player, CLAN_OATH_BOOTS, 1);
								break;
							
							case 3:
								giveItems(player, CLAN_OATH_AKETON, 1);
								giveItems(player, CLAN_OATH_PADDED_GLOVES, 1);
								giveItems(player, CLAN_OATH_SANDALS, 1);
								break;
						}
						
						playSound(player, SOUND_FINISH);
						st.exitQuest(false);
					}
					else
						htmltext = "30298-16.htm";
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
		
		if (player.getSponsor() == 0)
		{
			st.exitQuest(true);
			return;
		}
		
		switch (npc.getNpcId())
		{
			case MAILLE_LIZARDMAN, MAILLE_LIZARDMAN_SCOUT, MAILLE_LIZARDMAN_GUARD:
				if (st.getCond() == 1 && dropItems(player, BLOOD_OF_MAILLE_LIZARDMAN, 1, 10, 700000))
					st.setCond(2);
				break;
			
			case KING_OF_THE_ARANEID:
				if (st.getCond() == 7 && getSponsor(player) && dropItems(player, LEG_OF_KING_ARANEID, 1, 8, 700000))
					st.setCond(8);
				break;
		}
	}
}