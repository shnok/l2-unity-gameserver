package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q117_TheOceanOfDistantStars extends Quest
{
	private static final String QUEST_NAME = "Q117_TheOceanOfDistantStars";
	
	// NPCs
	private static final int ABEY = 32053;
	private static final int GHOST = 32054;
	private static final int ANCIENT_GHOST = 32055;
	private static final int OBI = 32052;
	private static final int BOX = 32076;
	
	// Items
	private static final int GREY_STAR = 8495;
	private static final int ENGRAVED_HAMMER = 8488;
	
	// Monsters
	private static final int BANDIT_WARRIOR = 22023;
	private static final int BANDIT_INSPECTOR = 22024;
	
	public Q117_TheOceanOfDistantStars()
	{
		super(117, "The Ocean of Distant Stars");
		
		setItemsIds(GREY_STAR, ENGRAVED_HAMMER);
		
		addQuestStart(ABEY);
		addTalkId(ABEY, ANCIENT_GHOST, GHOST, OBI, BOX);
		addMyDying(BANDIT_WARRIOR, BANDIT_INSPECTOR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32053-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32055-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32052-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32053-04.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32076-02.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, ENGRAVED_HAMMER, 1);
		}
		else if (event.equalsIgnoreCase("32053-06.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32052-04.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32052-06.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, GREY_STAR, 1);
		}
		else if (event.equalsIgnoreCase("32055-04.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ENGRAVED_HAMMER, 1);
		}
		else if (event.equalsIgnoreCase("32054-03.htm"))
		{
			rewardExpAndSp(player, 63591, 0);
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
				htmltext = (player.getStatus().getLevel() < 39) ? "32053-00.htm" : "32053-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ANCIENT_GHOST:
						if (cond == 1)
							htmltext = "32055-01.htm";
						else if (cond > 1 && cond < 9)
							htmltext = "32055-02.htm";
						else if (cond == 9)
							htmltext = "32055-03.htm";
						else if (cond > 9)
							htmltext = "32055-05.htm";
						break;
					
					case OBI:
						if (cond == 2)
							htmltext = "32052-01.htm";
						else if (cond > 2 && cond < 6)
							htmltext = "32052-02.htm";
						else if (cond == 6)
							htmltext = "32052-03.htm";
						else if (cond == 7)
							htmltext = "32052-04.htm";
						else if (cond == 8)
							htmltext = "32052-05.htm";
						else if (cond > 8)
							htmltext = "32052-06.htm";
						break;
					
					case ABEY:
						if (cond == 1 || cond == 2)
							htmltext = "32053-02.htm";
						else if (cond == 3)
							htmltext = "32053-03.htm";
						else if (cond == 4)
							htmltext = "32053-04.htm";
						else if (cond == 5)
							htmltext = "32053-05.htm";
						else if (cond > 5)
							htmltext = "32053-06.htm";
						break;
					
					case BOX:
						if (cond == 4)
							htmltext = "32076-01.htm";
						else if (cond > 4)
							htmltext = "32076-03.htm";
						break;
					
					case GHOST:
						if (cond == 10)
							htmltext = "32054-01.htm";
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
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 7);
		if (st == null)
			return;
		
		if (dropItems(player, GREY_STAR, 1, 1, 200000))
			st.setCond(8);
	}
}