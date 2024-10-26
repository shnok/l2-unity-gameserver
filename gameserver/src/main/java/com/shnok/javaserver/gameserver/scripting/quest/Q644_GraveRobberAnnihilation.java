package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q644_GraveRobberAnnihilation extends Quest
{
	private static final String QUEST_NAME = "Q644_GraveRobberAnnihilation";
	
	// Item
	private static final int ORC_GRAVE_GOODS = 8088;
	
	// Rewards
	private static final int[][] REWARDS =
	{
		{
			1865,
			30
		},
		{
			1867,
			40
		},
		{
			1872,
			40
		},
		{
			1871,
			30
		},
		{
			1870,
			30
		},
		{
			1869,
			30
		}
	};
	
	// NPC
	private static final int KARUDA = 32017;
	
	public Q644_GraveRobberAnnihilation()
	{
		super(644, "Grave Robber Annihilation");
		
		setItemsIds(ORC_GRAVE_GOODS);
		
		addQuestStart(KARUDA);
		addTalkId(KARUDA);
		
		addMyDying(22003, 22004, 22005, 22006, 22008);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32017-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (StringUtil.isDigit(event))
		{
			htmltext = "32017-04.htm";
			takeItems(player, ORC_GRAVE_GOODS, -1);
			
			final int[] reward = REWARDS[Integer.parseInt(event)];
			rewardItems(player, reward[0], reward[1]);
			
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				htmltext = (player.getStatus().getLevel() < 20) ? "32017-06.htm" : "32017-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "32017-05.htm";
				else if (cond == 2)
					htmltext = "32017-07.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMember(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(st.getPlayer(), ORC_GRAVE_GOODS, 1, 120, 500000))
			st.setCond(2);
	}
}