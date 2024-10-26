package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q113_StatusOfTheBeaconTower extends Quest
{
	private static final String QUEST_NAME = "Q113_StatusOfTheBeaconTower";
	
	// NPCs
	private static final int MOIRA = 31979;
	private static final int TORRANT = 32016;
	
	// Item
	private static final int BOX = 8086;
	
	public Q113_StatusOfTheBeaconTower()
	{
		super(113, "Status of the Beacon Tower");
		
		setItemsIds(BOX);
		
		addQuestStart(MOIRA);
		addTalkId(MOIRA, TORRANT);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31979-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, BOX, 1);
		}
		else if (event.equalsIgnoreCase("32016-02.htm"))
		{
			takeItems(player, BOX, 1);
			rewardItems(player, 57, 21578);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
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
				htmltext = (player.getStatus().getLevel() < 40) ? "31979-00.htm" : "31979-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case MOIRA:
						htmltext = "31979-03.htm";
						break;
					
					case TORRANT:
						htmltext = "32016-01.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}