package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q414_PathToAnOrcRaider extends Quest
{
	private static final String QUEST_NAME = "Q414_PathToAnOrcRaider";
	
	// Items
	private static final int GREEN_BLOOD = 1578;
	private static final int GOBLIN_DWELLING_MAP = 1579;
	private static final int KURUKA_RATMAN_TOOTH = 1580;
	private static final int BETRAYER_REPORT_1 = 1589;
	private static final int BETRAYER_REPORT_2 = 1590;
	private static final int HEAD_OF_BETRAYER = 1591;
	private static final int MARK_OF_RAIDER = 1592;
	private static final int TIMORA_ORC_HEAD = 8544;
	
	// NPCs
	private static final int KARUKIA = 30570;
	private static final int KASMAN = 30501;
	private static final int TAZEER = 31978;
	
	// Monsters
	private static final int GOBLIN_TOMB_RAIDER_LEADER = 20320;
	private static final int KURUKA_RATMAN_LEADER = 27045;
	private static final int UMBAR_ORC = 27054;
	private static final int TIMORA_ORC = 27320;
	
	public Q414_PathToAnOrcRaider()
	{
		super(414, "Path To An Orc Raider");
		
		setItemsIds(GREEN_BLOOD, GOBLIN_DWELLING_MAP, KURUKA_RATMAN_TOOTH, BETRAYER_REPORT_1, BETRAYER_REPORT_2, HEAD_OF_BETRAYER, TIMORA_ORC_HEAD);
		
		addQuestStart(KARUKIA);
		addTalkId(KARUKIA, KASMAN, TAZEER);
		
		addMyDying(GOBLIN_TOMB_RAIDER_LEADER, KURUKA_RATMAN_LEADER, UMBAR_ORC, TIMORA_ORC);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// KARUKIA
		if (event.equalsIgnoreCase("30570-05.htm"))
		{
			if (player.getClassId() != ClassId.ORC_FIGHTER)
				htmltext = (player.getClassId() == ClassId.ORC_RAIDER) ? "30570-02a.htm" : "30570-03.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30570-02.htm";
			else if (player.getInventory().hasItem(MARK_OF_RAIDER))
				htmltext = "30570-04.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
				giveItems(player, GOBLIN_DWELLING_MAP, 1);
			}
		}
		else if (event.equalsIgnoreCase("30570-07a.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, GOBLIN_DWELLING_MAP, 1);
			takeItems(player, KURUKA_RATMAN_TOOTH, -1);
			giveItems(player, BETRAYER_REPORT_1, 1);
		}
		else if (event.equalsIgnoreCase("30570-07b.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, GOBLIN_DWELLING_MAP, 1);
			takeItems(player, KURUKA_RATMAN_TOOTH, -1);
			giveItems(player, BETRAYER_REPORT_2, 1);
		}
		// TAZEER
		else if (event.equalsIgnoreCase("31978-03.htm"))
		{
			st.setCond(6);
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
				htmltext = "30570-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case KARUKIA:
						if (cond == 1)
							htmltext = "30570-06.htm";
						else if (cond == 2)
							htmltext = "30570-07.htm";
						else if (cond > 2)
							htmltext = "30570-08.htm";
						break;
					
					case KASMAN:
						if (cond == 3)
							htmltext = player.getInventory().hasItem(HEAD_OF_BETRAYER) ? "30501-02.htm" : "30501-01.htm";
						else if (cond == 4)
						{
							htmltext = "30501-03.htm";
							takeItems(player, BETRAYER_REPORT_1, 1);
							takeItems(player, HEAD_OF_BETRAYER, -1);
							giveItems(player, MARK_OF_RAIDER, 1);
							rewardExpAndSp(player, 3200, 2360);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case TAZEER:
						if (cond == 5)
							htmltext = "31978-01.htm";
						else if (cond == 6)
							htmltext = "31978-04.htm";
						else if (cond == 7)
						{
							htmltext = "31978-05.htm";
							takeItems(player, BETRAYER_REPORT_2, 1);
							takeItems(player, TIMORA_ORC_HEAD, 1);
							giveItems(player, MARK_OF_RAIDER, 1);
							rewardExpAndSp(player, 3200, 2360);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
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
		
		final int cond = st.getCond();
		
		switch (npc.getNpcId())
		{
			case GOBLIN_TOMB_RAIDER_LEADER:
				if (cond == 1)
				{
					if (player.getInventory().getItemCount(GREEN_BLOOD) <= Rnd.get(20))
					{
						playSound(player, SOUND_ITEMGET);
						giveItems(player, GREEN_BLOOD, 1);
					}
					else
					{
						takeItems(player, GREEN_BLOOD, -1);
						addSpawn(KURUKA_RATMAN_LEADER, npc, false, 300000, true);
					}
				}
				break;
			
			case KURUKA_RATMAN_LEADER:
				if (cond == 1 && dropItemsAlways(player, KURUKA_RATMAN_TOOTH, 1, 10))
					st.setCond(2);
				break;
			
			case UMBAR_ORC:
				if (cond == 3 && dropItems(player, HEAD_OF_BETRAYER, 1, 2, 200000))
					st.setCond(4);
				break;
			
			case TIMORA_ORC:
				if (cond == 6 && dropItems(player, TIMORA_ORC_HEAD, 1, 1, 600000))
					st.setCond(7);
				break;
		}
	}
}