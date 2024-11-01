package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q613_ProveYourCourage extends Quest
{
	private static final String QUEST_NAME = "Q613_ProveYourCourage";
	
	// Items
	private static final int HEAD_OF_HEKATON = 7240;
	private static final int FEATHER_OF_VALOR = 7229;
	private static final int VARKA_ALLIANCE_3 = 7223;
	
	public Q613_ProveYourCourage()
	{
		super(613, "Prove your courage!");
		
		setItemsIds(HEAD_OF_HEKATON);
		
		addQuestStart(31377); // Ashas Varka Durai
		addTalkId(31377);
		
		addMyDying(25299); // Hekaton
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31377-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31377-07.htm"))
		{
			if (player.getInventory().hasItem(HEAD_OF_HEKATON))
			{
				takeItems(player, HEAD_OF_HEKATON, -1);
				giveItems(player, FEATHER_OF_VALOR, 1);
				rewardExpAndSp(player, 10000, 0);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
			{
				htmltext = "31377-06.htm";
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
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
				if (player.getStatus().getLevel() < 75)
					htmltext = "31377-03.htm";
				else if (player.getAllianceWithVarkaKetra() <= -3 && player.getInventory().hasItem(VARKA_ALLIANCE_3) && !player.getInventory().hasItem(FEATHER_OF_VALOR))
					htmltext = "31377-01.htm";
				else
					htmltext = "31377-02.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasItem(HEAD_OF_HEKATON)) ? "31377-05.htm" : "31377-06.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		for (QuestState st : getPartyMembers(player, npc, 1))
		{
			Player pm = st.getPlayer();
			if (pm.getAllianceWithVarkaKetra() <= -3 && pm.getInventory().hasItem(VARKA_ALLIANCE_3))
			{
				st.setCond(2);
				playSound(pm, SOUND_MIDDLE);
				giveItems(pm, HEAD_OF_HEKATON, 1);
			}
		}
	}
}