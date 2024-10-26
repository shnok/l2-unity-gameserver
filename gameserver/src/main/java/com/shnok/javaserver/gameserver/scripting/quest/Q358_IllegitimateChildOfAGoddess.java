package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q358_IllegitimateChildOfAGoddess extends Quest
{
	private static final String QUEST_NAME = "Q358_IllegitimateChildOfAGoddess";
	
	// Item
	private static final int SCALE = 5868;
	
	// Reward
	private static final int[] REWARDS =
	{
		6329,
		6331,
		6333,
		6335,
		6337,
		6339,
		5364,
		5366
	};
	
	public Q358_IllegitimateChildOfAGoddess()
	{
		super(358, "Illegitimate Child of a Goddess");
		
		setItemsIds(SCALE);
		
		addQuestStart(30862); // Oltlin
		addTalkId(30862);
		
		addMyDying(20672, 20673); // Trives, Falibati
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30862-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
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
				htmltext = (player.getStatus().getLevel() < 63) ? "30862-01.htm" : "30862-02.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 1)
					htmltext = "30862-06.htm";
				else
				{
					htmltext = "30862-07.htm";
					takeItems(player, SCALE, -1);
					giveItems(player, Rnd.get(REWARDS), 1);
					playSound(player, SOUND_FINISH);
					st.exitQuest(true);
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
		
		if (dropItems(player, SCALE, 1, 108, (npc.getNpcId() == 20672) ? 680000 : 660000))
			st.setCond(2);
	}
}