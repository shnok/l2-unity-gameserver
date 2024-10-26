package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q338_AlligatorHunter extends Quest
{
	private static final String QUEST_NAME = "Q338_AlligatorHunter";
	
	// Item
	private static final int ALLIGATOR_PELT = 4337;
	
	public Q338_AlligatorHunter()
	{
		super(338, "Alligator Hunter");
		
		setItemsIds(ALLIGATOR_PELT);
		
		addQuestStart(30892); // Enverun
		addTalkId(30892);
		
		addMyDying(20135); // Alligator
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30892-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30892-05.htm"))
		{
			final int pelts = player.getInventory().getItemCount(ALLIGATOR_PELT);
			
			int reward = pelts * 60;
			if (pelts > 10)
				reward += 3430;
			
			takeItems(player, ALLIGATOR_PELT, -1);
			rewardItems(player, 57, reward);
		}
		else if (event.equalsIgnoreCase("30892-08.htm"))
		{
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
				htmltext = (player.getStatus().getLevel() < 40) ? "30892-00.htm" : "30892-01.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasItem(ALLIGATOR_PELT)) ? "30892-03.htm" : "30892-04.htm";
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
		
		dropItemsAlways(player, ALLIGATOR_PELT, 1, 0);
	}
}