package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q004_LongliveThePaagrioLord extends Quest
{
	private static final String QUEST_NAME = "Q004_LongliveThePaagrioLord";
	
	private static final Map<Integer, Integer> NPC_GIFTS = HashMap.newHashMap(6);
	
	public Q004_LongliveThePaagrioLord()
	{
		super(4, "Long live the Pa'agrio Lord!");
		
		NPC_GIFTS.put(30585, 1542);
		NPC_GIFTS.put(30566, 1541);
		NPC_GIFTS.put(30562, 1543);
		NPC_GIFTS.put(30560, 1544);
		NPC_GIFTS.put(30559, 1545);
		NPC_GIFTS.put(30587, 1546);
		
		setItemsIds(1541, 1542, 1543, 1544, 1545, 1546);
		
		addQuestStart(30578); // Nakusin
		addTalkId(30578, 30585, 30566, 30562, 30560, 30559, 30587);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30578-03.htm"))
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
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getRace() != ClassRace.ORC)
					htmltext = "30578-00.htm";
				else if (player.getStatus().getLevel() < 2)
					htmltext = "30578-01.htm";
				else
					htmltext = "30578-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				int npcId = npc.getNpcId();
				
				if (npcId == 30578)
				{
					if (cond == 1)
						htmltext = "30578-04.htm";
					else if (cond == 2)
					{
						htmltext = "30578-06.htm";
						giveItems(player, 4, 1);
						for (int item : NPC_GIFTS.values())
							takeItems(player, item, -1);
						
						playSound(player, SOUND_FINISH);
						st.exitQuest(false);
					}
				}
				else
				{
					int i = NPC_GIFTS.get(npcId);
					if (player.getInventory().hasItem(i))
						htmltext = npcId + "-02.htm";
					else
					{
						giveItems(player, i, 1);
						htmltext = npcId + "-01.htm";
						
						int count = 0;
						for (int item : NPC_GIFTS.values())
							count += player.getInventory().getItemCount(item);
						
						if (count == 6)
						{
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
						}
						else
							playSound(player, SOUND_ITEMGET);
					}
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
}