package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q607_ProveYourCourage extends Quest
{
	private static final String QUEST_NAME = "Q607_ProveYourCourage";
	
	// Items
	private static final int HEAD_OF_SHADITH = 7235;
	private static final int TOTEM_OF_VALOR = 7219;
	private static final int KETRA_ALLIANCE_3 = 7213;
	
	public Q607_ProveYourCourage()
	{
		super(607, "Prove your courage!");
		
		setItemsIds(HEAD_OF_SHADITH);
		
		addQuestStart(31370); // Kadun Zu Ketra
		addTalkId(31370);
		
		addMyDying(25309); // Shadith
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31370-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31370-07.htm"))
		{
			if (player.getInventory().hasItem(HEAD_OF_SHADITH))
			{
				takeItems(player, HEAD_OF_SHADITH, -1);
				giveItems(player, TOTEM_OF_VALOR, 1);
				rewardExpAndSp(player, 10000, 0);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
			{
				htmltext = "31370-06.htm";
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
					htmltext = "31370-03.htm";
				else if (player.getAllianceWithVarkaKetra() >= 3 && player.getInventory().hasItem(KETRA_ALLIANCE_3) && !player.getInventory().hasItem(TOTEM_OF_VALOR))
					htmltext = "31370-01.htm";
				else
					htmltext = "31370-02.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasItem(HEAD_OF_SHADITH)) ? "31370-05.htm" : "31370-06.htm";
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
			if (pm.getAllianceWithVarkaKetra() >= 3 && pm.getInventory().hasItem(KETRA_ALLIANCE_3))
			{
				st.setCond(2);
				playSound(pm, SOUND_MIDDLE);
				giveItems(pm, HEAD_OF_SHADITH, 1);
			}
		}
	}
}