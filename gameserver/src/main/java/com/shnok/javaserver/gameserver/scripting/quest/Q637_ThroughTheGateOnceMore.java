package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q637_ThroughTheGateOnceMore extends Quest
{
	private static final String QUEST_NAME = "Q637_ThroughTheGateOnceMore";
	
	// NPC
	private static final int FLAURON = 32010;
	
	// Items
	private static final int FADED_VISITOR_MARK = 8065;
	private static final int NECROMANCER_HEART = 8066;
	
	// Reward
	private static final int PAGAN_MARK = 8067;
	
	public Q637_ThroughTheGateOnceMore()
	{
		super(637, "Through the Gate Once More");
		
		setItemsIds(NECROMANCER_HEART);
		
		addQuestStart(FLAURON);
		addTalkId(FLAURON);
		
		addMyDying(21565, 21566, 21567);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32010-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32010-10.htm"))
			st.exitQuest(true);
		
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
				if (player.getStatus().getLevel() < 73 || !player.getInventory().hasItem(FADED_VISITOR_MARK))
					htmltext = "32010-01a.htm";
				else if (player.getInventory().hasItem(PAGAN_MARK))
					htmltext = "32010-00.htm";
				else
					htmltext = "32010-01.htm";
				break;
			
			case STARTED:
				if (st.getCond() == 2)
				{
					if (player.getInventory().getItemCount(NECROMANCER_HEART) == 10)
					{
						htmltext = "32010-06.htm";
						takeItems(player, FADED_VISITOR_MARK, 1);
						takeItems(player, NECROMANCER_HEART, -1);
						giveItems(player, PAGAN_MARK, 1);
						giveItems(player, 8273, 10);
						playSound(player, SOUND_FINISH);
						st.exitQuest(true);
					}
					else
						st.setCond(1);
				}
				else
					htmltext = "32010-05.htm";
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
		
		if (dropItems(st.getPlayer(), NECROMANCER_HEART, 1, 10, 400000))
			st.setCond(2);
	}
}