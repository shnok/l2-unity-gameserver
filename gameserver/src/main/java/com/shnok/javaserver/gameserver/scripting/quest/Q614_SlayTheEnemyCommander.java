package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q614_SlayTheEnemyCommander extends Quest
{
	private static final String QUEST_NAME = "Q614_SlayTheEnemyCommander";
	
	// Quest Items
	private static final int HEAD_OF_TAYR = 7241;
	private static final int FEATHER_OF_WISDOM = 7230;
	private static final int VARKA_ALLIANCE_4 = 7224;
	
	public Q614_SlayTheEnemyCommander()
	{
		super(614, "Slay the enemy commander!");
		
		setItemsIds(HEAD_OF_TAYR);
		
		addQuestStart(31377); // Ashas Varka Durai
		addTalkId(31377);
		
		addMyDying(25302); // Tayr
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
			if (player.getInventory().hasItem(HEAD_OF_TAYR))
			{
				takeItems(player, HEAD_OF_TAYR, -1);
				giveItems(player, FEATHER_OF_WISDOM, 1);
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
				if (player.getStatus().getLevel() >= 75)
				{
					if (player.getAllianceWithVarkaKetra() <= -4 && player.getInventory().hasItem(VARKA_ALLIANCE_4) && !player.getInventory().hasItem(FEATHER_OF_WISDOM))
						htmltext = "31377-01.htm";
					else
						htmltext = "31377-02.htm";
				}
				else
					htmltext = "31377-03.htm";
				break;
			
			case STARTED:
				htmltext = (player.getInventory().hasItem(HEAD_OF_TAYR)) ? "31377-05.htm" : "31377-06.htm";
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
			if (pm.getAllianceWithVarkaKetra() <= -4 && pm.getInventory().hasItem(VARKA_ALLIANCE_4))
			{
				st.setCond(2);
				playSound(pm, SOUND_MIDDLE);
				giveItems(pm, HEAD_OF_TAYR, 1);
			}
		}
	}
}