package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q602_ShadowOfLight extends Quest
{
	private static final String QUEST_NAME = "Q602_ShadowOfLight";
	
	private static final int EYE_OF_DARKNESS = 7189;
	
	private static final int[][] REWARDS =
	{
		{
			6699,
			40000,
			120000,
			20000,
			20
		},
		{
			6698,
			60000,
			110000,
			15000,
			40
		},
		{
			6700,
			40000,
			150000,
			10000,
			50
		},
		{
			0,
			100000,
			140000,
			11250,
			100
		}
	};
	
	public Q602_ShadowOfLight()
	{
		super(602, "Shadow of Light");
		
		setItemsIds(EYE_OF_DARKNESS);
		
		addQuestStart(31683); // Eye of Argos
		addTalkId(31683);
		
		addMyDying(21299, 21304);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31683-02.htm"))
		{
			if (player.getStatus().getLevel() < 68)
				htmltext = "31683-02a.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
		}
		else if (event.equalsIgnoreCase("31683-05.htm"))
		{
			takeItems(player, EYE_OF_DARKNESS, -1);
			
			final int random = Rnd.get(100);
			for (int[] element : REWARDS)
			{
				if (random < element[4])
				{
					rewardItems(player, 57, element[1]);
					
					if (element[0] != 0)
						giveItems(player, element[0], 3);
					
					rewardExpAndSp(player, element[2], element[3]);
					break;
				}
			}
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
				htmltext = "31683-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				if (cond == 1)
					htmltext = "31683-03.htm";
				else if (cond == 2)
					htmltext = "31683-04.htm";
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
		
		if (dropItems(st.getPlayer(), EYE_OF_DARKNESS, 1, 100, (npc.getNpcId() == 21299) ? 450000 : 500000))
			st.setCond(2);
	}
}