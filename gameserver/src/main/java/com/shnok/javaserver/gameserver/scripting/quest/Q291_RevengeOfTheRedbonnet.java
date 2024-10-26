package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q291_RevengeOfTheRedbonnet extends Quest
{
	private static final String QUEST_NAME = "Q291_RevengeOfTheRedbonnet";
	
	// Quest items
	private static final int BLACK_WOLF_PELT = 1482;
	
	// Rewards
	private static final int SCROLL_OF_ESCAPE = 736;
	private static final int GRANDMA_PEARL = 1502;
	private static final int GRANDMA_MIRROR = 1503;
	private static final int GRANDMA_NECKLACE = 1504;
	private static final int GRANDMA_HAIRPIN = 1505;
	
	public Q291_RevengeOfTheRedbonnet()
	{
		super(291, "Revenge of the Redbonnet");
		
		setItemsIds(BLACK_WOLF_PELT);
		
		addQuestStart(30553); // Maryse Redbonnet
		addTalkId(30553);
		
		addMyDying(20317);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30553-03.htm"))
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
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 4) ? "30553-01.htm" : "30553-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				if (cond == 1)
					htmltext = "30553-04.htm";
				else if (cond == 2)
				{
					htmltext = "30553-05.htm";
					takeItems(player, BLACK_WOLF_PELT, -1);
					
					int random = Rnd.get(100);
					if (random < 3)
						rewardItems(player, GRANDMA_PEARL, 1);
					else if (random < 21)
						rewardItems(player, GRANDMA_MIRROR, 1);
					else if (random < 46)
						rewardItems(player, GRANDMA_NECKLACE, 1);
					else
					{
						rewardItems(player, SCROLL_OF_ESCAPE, 1);
						rewardItems(player, GRANDMA_HAIRPIN, 1);
					}
					
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
		
		if (dropItemsAlways(player, BLACK_WOLF_PELT, 1, 40))
			st.setCond(2);
	}
}