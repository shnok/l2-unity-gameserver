package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q156_MillenniumLove extends Quest
{
	private static final String QUEST_NAME = "Q156_MillenniumLove";
	
	// Items
	private static final int LILITH_LETTER = 1022;
	private static final int THEON_DIARY = 1023;
	
	// NPCs
	private static final int LILITH = 30368;
	private static final int BAENEDES = 30369;
	
	public Q156_MillenniumLove()
	{
		super(156, "Millennium Love");
		
		setItemsIds(LILITH_LETTER, THEON_DIARY);
		
		addQuestStart(LILITH);
		addTalkId(LILITH, BAENEDES);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30368-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, LILITH_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30369-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LILITH_LETTER, 1);
			giveItems(player, THEON_DIARY, 1);
		}
		else if (event.equalsIgnoreCase("30369-03.htm"))
		{
			takeItems(player, LILITH_LETTER, 1);
			rewardExpAndSp(player, 3000, 0);
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30368-00.htm" : "30368-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case LILITH:
						if (player.getInventory().hasItem(LILITH_LETTER))
							htmltext = "30368-05.htm";
						else if (player.getInventory().hasItem(THEON_DIARY))
						{
							htmltext = "30368-06.htm";
							takeItems(player, THEON_DIARY, 1);
							giveItems(player, 5250, 1);
							rewardExpAndSp(player, 3000, 0);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case BAENEDES:
						if (player.getInventory().hasItem(LILITH_LETTER))
							htmltext = "30369-01.htm";
						else if (player.getInventory().hasItem(THEON_DIARY))
							htmltext = "30369-04.htm";
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