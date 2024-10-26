package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q265_ChainsOfSlavery extends Quest
{
	private static final String QUEST_NAME = "Q265_ChainsOfSlavery";
	
	// Item
	private static final int SHACKLE = 1368;
	
	public Q265_ChainsOfSlavery()
	{
		super(265, "Chains of Slavery");
		
		setItemsIds(SHACKLE);
		
		addQuestStart(30357); // Kristin
		addTalkId(30357);
		
		addMyDying(20004, 20005);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30357-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30357-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				if (player.getRace() != ClassRace.DARK_ELF)
					htmltext = "30357-00.htm";
				else if (player.getStatus().getLevel() < 6)
					htmltext = "30357-01.htm";
				else
					htmltext = "30357-02.htm";
				break;
			
			case STARTED:
				final int shackles = player.getInventory().getItemCount(SHACKLE);
				if (shackles == 0)
					htmltext = "30357-04.htm";
				else
				{
					htmltext = "30357-05.htm";
					takeItems(player, SHACKLE, -1);
					
					int reward = 12 * shackles;
					if (shackles >= 10)
						reward += 500;
					
					rewardItems(player, 57, reward);
					rewardNewbieShots(player, 6000, 3000);
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
		
		dropItems(player, SHACKLE, 1, 0, (npc.getNpcId() == 20004) ? 500000 : 600000);
	}
}