package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q431_WeddingMarch extends Quest
{
	private static final String QUEST_NAME = "Q431_WeddingMarch";
	
	// NPC
	private static final int KANTABILON = 31042;
	
	// Item
	private static final int SILVER_CRYSTAL = 7540;
	
	// Reward
	private static final int WEDDING_ECHO_CRYSTAL = 7062;
	
	public Q431_WeddingMarch()
	{
		super(431, "Wedding March");
		
		setItemsIds(SILVER_CRYSTAL);
		
		addQuestStart(KANTABILON);
		addTalkId(KANTABILON);
		
		addMyDying(20786, 20787);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31042-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31042-05.htm"))
		{
			if (player.getInventory().getItemCount(SILVER_CRYSTAL) < 50)
				htmltext = "31042-03.htm";
			else
			{
				takeItems(player, SILVER_CRYSTAL, -1);
				giveItems(player, WEDDING_ECHO_CRYSTAL, 25);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
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
				htmltext = (player.getStatus().getLevel() < 38) ? "31042-00.htm" : "31042-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
					htmltext = "31042-02.htm";
				else if (cond == 2)
					htmltext = (player.getInventory().getItemCount(SILVER_CRYSTAL) < 50) ? "31042-03.htm" : "31042-04.htm";
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
		
		if (dropItems(st.getPlayer(), SILVER_CRYSTAL, 1, 50, 500000))
			st.setCond(2);
	}
}