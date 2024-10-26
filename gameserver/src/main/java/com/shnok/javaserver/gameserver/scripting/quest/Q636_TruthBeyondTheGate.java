package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q636_TruthBeyondTheGate extends Quest
{
	private static final String QUEST_NAME = "Q636_TruthBeyondTheGate";
	
	// NPCs
	private static final int ELIYAH = 31329;
	private static final int FLAURON = 32010;
	
	// Reward
	private static final int VISITOR_MARK = 8064;
	private static final int FADED_VISITOR_MARK = 8065;
	
	public Q636_TruthBeyondTheGate()
	{
		super(636, "The Truth Beyond the Gate");
		
		addQuestStart(ELIYAH);
		addTalkId(ELIYAH, FLAURON);
		
		addZoneEnter(100000);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31329-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32010-02.htm"))
		{
			giveItems(player, VISITOR_MARK, 1);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
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
				htmltext = (player.getStatus().getLevel() < 73) ? "31329-01.htm" : "31329-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case ELIYAH:
						htmltext = "31329-05.htm";
						break;
					
					case FLAURON:
						htmltext = (player.getInventory().hasItem(VISITOR_MARK)) ? "32010-03.htm" : "32010-01.htm";
						break;
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public final void onZoneEnter(Creature creature, ZoneType zone)
	{
		// QuestState already null on enter because quest is finished
		if (creature instanceof Player player)
		{
			if (player.destroyItemByItemId(VISITOR_MARK, 1, false))
				player.addItem(FADED_VISITOR_MARK, 1, true);
		}
		super.onZoneEnter(creature, zone);
	}
}