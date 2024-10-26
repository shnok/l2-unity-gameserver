package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q362_BardsMandolin extends Quest
{
	private static final String QUEST_NAME = "Q362_BardsMandolin";
	
	// Items
	private static final int SWAN_FLUTE = 4316;
	private static final int SWAN_LETTER = 4317;
	
	// NPCs
	private static final int SWAN = 30957;
	private static final int NANARIN = 30956;
	private static final int GALION = 30958;
	private static final int WOODROW = 30837;
	
	public Q362_BardsMandolin()
	{
		super(362, "Bard's Mandolin");
		
		setItemsIds(SWAN_FLUTE, SWAN_LETTER);
		
		addQuestStart(SWAN);
		addTalkId(SWAN, NANARIN, GALION, WOODROW);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30957-3.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30957-7.htm") || event.equalsIgnoreCase("30957-8.htm"))
		{
			rewardItems(player, 57, 10000);
			giveItems(player, 4410, 1);
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30957-2.htm" : "30957-1.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case SWAN:
						if (cond == 1 || cond == 2)
							htmltext = "30957-4.htm";
						else if (cond == 3)
						{
							htmltext = "30957-5.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, SWAN_LETTER, 1);
						}
						else if (cond == 4)
							htmltext = "30957-5a.htm";
						else if (cond == 5)
							htmltext = "30957-6.htm";
						break;
					
					case WOODROW:
						if (cond == 1)
						{
							htmltext = "30837-1.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 2)
							htmltext = "30837-2.htm";
						else if (cond > 2)
							htmltext = "30837-3.htm";
						break;
					
					case GALION:
						if (cond == 2)
						{
							htmltext = "30958-1.htm";
							st.setCond(3);
							playSound(player, SOUND_ITEMGET);
							giveItems(player, SWAN_FLUTE, 1);
						}
						else if (cond > 2)
							htmltext = "30958-2.htm";
						break;
					
					case NANARIN:
						if (cond == 4)
						{
							htmltext = "30956-1.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SWAN_FLUTE, 1);
							takeItems(player, SWAN_LETTER, 1);
						}
						else if (cond == 5)
							htmltext = "30956-2.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
}