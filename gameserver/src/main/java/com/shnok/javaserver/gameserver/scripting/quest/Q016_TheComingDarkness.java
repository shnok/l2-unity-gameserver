package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q016_TheComingDarkness extends Quest
{
	private static final String QUEST_NAME = "Q016_TheComingDarkness";
	
	// NPCs
	private static final int HIERARCH = 31517;
	private static final int EVIL_ALTAR_1 = 31512;
	private static final int EVIL_ALTAR_2 = 31513;
	private static final int EVIL_ALTAR_3 = 31514;
	private static final int EVIL_ALTAR_4 = 31515;
	private static final int EVIL_ALTAR_5 = 31516;
	
	// Item
	private static final int CRYSTAL_OF_SEAL = 7167;
	
	public Q016_TheComingDarkness()
	{
		super(16, "The Coming Darkness");
		
		setItemsIds(CRYSTAL_OF_SEAL);
		
		addQuestStart(HIERARCH);
		addTalkId(HIERARCH, EVIL_ALTAR_1, EVIL_ALTAR_2, EVIL_ALTAR_3, EVIL_ALTAR_4, EVIL_ALTAR_5);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31517-2.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, CRYSTAL_OF_SEAL, 5);
		}
		else if (event.equalsIgnoreCase("31512-1.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRYSTAL_OF_SEAL, 1);
		}
		else if (event.equalsIgnoreCase("31513-1.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRYSTAL_OF_SEAL, 1);
		}
		else if (event.equalsIgnoreCase("31514-1.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRYSTAL_OF_SEAL, 1);
		}
		else if (event.equalsIgnoreCase("31515-1.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRYSTAL_OF_SEAL, 1);
		}
		else if (event.equalsIgnoreCase("31516-1.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRYSTAL_OF_SEAL, 1);
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
				htmltext = (player.getStatus().getLevel() < 62) ? "31517-0a.htm" : "31517-0.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				int npcId = npc.getNpcId();
				
				switch (npcId)
				{
					case HIERARCH:
						if (cond == 6)
						{
							htmltext = "31517-4.htm";
							rewardExpAndSp(player, 221958, 0);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						else
						{
							if (player.getInventory().hasItem(CRYSTAL_OF_SEAL))
								htmltext = "31517-3.htm";
							else
							{
								htmltext = "31517-3a.htm";
								st.exitQuest(true);
							}
						}
						break;
					
					case EVIL_ALTAR_1, EVIL_ALTAR_2, EVIL_ALTAR_3, EVIL_ALTAR_4, EVIL_ALTAR_5:
						final int condAltar = npcId - 31511;
						
						if (cond == condAltar)
						{
							if (player.getInventory().hasItem(CRYSTAL_OF_SEAL))
								htmltext = npcId + "-0.htm";
							else
								htmltext = "altar_nocrystal.htm";
						}
						else if (cond > condAltar)
							htmltext = npcId + "-2.htm";
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