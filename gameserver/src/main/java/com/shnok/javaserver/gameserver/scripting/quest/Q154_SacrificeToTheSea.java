package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q154_SacrificeToTheSea extends Quest
{
	private static final String QUEST_NAME = "Q154_SacrificeToTheSea";
	
	// NPCs
	private static final int ROCKSWELL = 30312;
	private static final int CRISTEL = 30051;
	private static final int ROLFE = 30055;
	
	// Items
	private static final int FOX_FUR = 1032;
	private static final int FOX_FUR_YARN = 1033;
	private static final int MAIDEN_DOLL = 1034;
	
	// Reward
	private static final int EARING = 113;
	
	public Q154_SacrificeToTheSea()
	{
		super(154, "Sacrifice to the Sea");
		
		setItemsIds(FOX_FUR, FOX_FUR_YARN, MAIDEN_DOLL);
		
		addQuestStart(ROCKSWELL);
		addTalkId(ROCKSWELL, CRISTEL, ROLFE);
		
		addMyDying(20481, 20544, 20545); // Following Keltirs can be found near Talking Island.
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30312-04.htm"))
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
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = (player.getStatus().getLevel() < 2) ? "30312-02.htm" : "30312-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ROCKSWELL:
						if (cond == 1)
							htmltext = "30312-05.htm";
						else if (cond == 2)
							htmltext = "30312-08.htm";
						else if (cond == 3)
							htmltext = "30312-06.htm";
						else if (cond == 4)
						{
							htmltext = "30312-07.htm";
							takeItems(player, MAIDEN_DOLL, -1);
							giveItems(player, EARING, 1);
							rewardExpAndSp(player, 100, 0);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case CRISTEL:
						if (cond == 1)
							htmltext = (player.getInventory().hasItem(FOX_FUR)) ? "30051-01.htm" : "30051-01a.htm";
						else if (cond == 2)
						{
							htmltext = "30051-02.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, FOX_FUR, -1);
							giveItems(player, FOX_FUR_YARN, 1);
						}
						else if (cond == 3)
							htmltext = "30051-03.htm";
						else if (cond == 4)
							htmltext = "30051-04.htm";
						break;
					
					case ROLFE:
						if (cond < 3)
							htmltext = "30055-03.htm";
						else if (cond == 3)
						{
							htmltext = "30055-01.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, FOX_FUR_YARN, 1);
							giveItems(player, MAIDEN_DOLL, 1);
						}
						else if (cond == 4)
							htmltext = "30055-02.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(player, FOX_FUR, 1, 10, 400000))
			st.setCond(2);
	}
}