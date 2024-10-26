package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q123_TheLeaderAndTheFollower extends Quest
{
	private static final String QUEST_NAME = "Q123_TheLeaderAndTheFollower";
	private static final String qn2 = "Q118_ToLeadAndBeLed";
	
	// NPC
	private static final int NEWYEAR = 31961;
	
	// Mobs
	private static final int BRUIN_LIZARDMAN = 27321;
	private static final int PICOT_ARENEID = 27322;
	
	// Items
	private static final int BRUIN_LIZARDMAN_BLOOD = 8549;
	private static final int PICOT_ARANEID_LEG = 8550;
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
	
	public Q123_TheLeaderAndTheFollower()
	{
		super(123, "The Leader and the Follower");
		
		setItemsIds(BRUIN_LIZARDMAN_BLOOD, PICOT_ARANEID_LEG);
		
		addQuestStart(NEWYEAR);
		addTalkId(NEWYEAR);
		
		addMyDying(BRUIN_LIZARDMAN, PICOT_ARENEID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31961-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("state", 1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31961-05d.htm"))
		{
			if (player.getInventory().getItemCount(BRUIN_LIZARDMAN_BLOOD) > 9)
			{
				st.setCond(3);
				st.set("state", 2);
				st.set("stateEx", 1);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BRUIN_LIZARDMAN_BLOOD, -1);
			}
		}
		else if (event.equalsIgnoreCase("31961-05e.htm"))
		{
			if (player.getInventory().getItemCount(BRUIN_LIZARDMAN_BLOOD) > 9)
			{
				st.setCond(4);
				st.set("state", 2);
				st.set("stateEx", 2);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BRUIN_LIZARDMAN_BLOOD, -1);
			}
		}
		else if (event.equalsIgnoreCase("31961-05f.htm"))
		{
			if (player.getInventory().getItemCount(BRUIN_LIZARDMAN_BLOOD) > 9)
			{
				st.setCond(5);
				st.set("state", 2);
				st.set("stateEx", 3);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BRUIN_LIZARDMAN_BLOOD, -1);
			}
		}
		else if (event.equalsIgnoreCase("31961-10.htm"))
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
							htmltext = "31961-11.htm";
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
							htmltext = "31961-11a.htm";
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
						htmltext = (st2.isCompleted()) ? "31961-02a.htm" : "31961-02b.htm";
					else
						htmltext = (player.getStatus().getLevel() > 18) ? "31961-01.htm" : "31961-02.htm";
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
								htmltext = "31961-08.htm";
							else if (state == 3)
								htmltext = "31961-12.htm";
							else
								htmltext = "31961-14.htm";
						}
					}
					else
						htmltext = "31961-09.htm";
				}
				break;
			
			case STARTED:
				final int state = st.getInteger("state");
				if (state == 1)
					htmltext = (player.getInventory().getItemCount(BRUIN_LIZARDMAN_BLOOD) < 10) ? "31961-04.htm" : "31961-05.htm";
				else if (state == 2)
				{
					final int stateEx = st.getInteger("stateEx");
					if (player.getSponsor() == 0)
					{
						if (stateEx == 1)
							htmltext = "31961-06a.htm";
						else if (stateEx == 2)
							htmltext = "31961-06b.htm";
						else if (stateEx == 3)
							htmltext = "31961-06c.htm";
					}
					else
					{
						if (getSponsor(player))
						{
							if (stateEx == 1)
								htmltext = "31961-06.htm";
							else if (stateEx == 2)
								htmltext = "31961-06d.htm";
							else if (stateEx == 3)
								htmltext = "31961-06e.htm";
						}
						else
							htmltext = "31961-07.htm";
					}
				}
				else if (state == 3)
				{
					st.setCond(7);
					st.set("state", 4);
					playSound(player, SOUND_MIDDLE);
					htmltext = "31961-15.htm";
				}
				else if (state == 4)
				{
					if (player.getInventory().getItemCount(PICOT_ARANEID_LEG) > 7)
					{
						htmltext = "31961-17.htm";
						
						takeItems(player, PICOT_ARANEID_LEG, -1);
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
						htmltext = "31961-16.htm";
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
			case BRUIN_LIZARDMAN:
				if (st.getCond() == 1 && dropItems(player, BRUIN_LIZARDMAN_BLOOD, 1, 10, 700000))
					st.setCond(2);
				break;
			
			case PICOT_ARENEID:
				if (st.getCond() == 7 && getSponsor(player) && dropItems(player, PICOT_ARANEID_LEG, 1, 8, 700000))
					st.setCond(8);
				break;
		}
	}
}