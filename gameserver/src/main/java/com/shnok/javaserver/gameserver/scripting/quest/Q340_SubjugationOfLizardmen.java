package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q340_SubjugationOfLizardmen extends Quest
{
	private static final String QUEST_NAME = "Q340_SubjugationOfLizardmen";
	
	// NPCs
	private static final int WEISZ = 30385;
	private static final int ADONIUS = 30375;
	private static final int LEVIAN = 30037;
	private static final int CHEST = 30989;
	
	// Items
	private static final int CARGO = 4255;
	private static final int HOLY = 4256;
	private static final int ROSARY = 4257;
	private static final int TOTEM = 4258;
	
	public Q340_SubjugationOfLizardmen()
	{
		super(340, "Subjugation of Lizardmen");
		
		setItemsIds(CARGO, HOLY, ROSARY, TOTEM);
		
		addQuestStart(WEISZ);
		addTalkId(WEISZ, ADONIUS, LEVIAN, CHEST);
		
		addMyDying(20008, 20010, 20014, 20024, 20027, 20030, 25146);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30385-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30385-07.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CARGO, -1);
		}
		else if (event.equalsIgnoreCase("30385-09.htm"))
		{
			takeItems(player, CARGO, -1);
			rewardItems(player, 57, 4090);
		}
		else if (event.equalsIgnoreCase("30385-10.htm"))
		{
			takeItems(player, CARGO, -1);
			rewardItems(player, 57, 4090);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30375-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30037-02.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30989-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, TOTEM, 1);
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
				htmltext = (player.getStatus().getLevel() < 17) ? "30385-01.htm" : "30385-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case WEISZ:
						if (cond == 1)
							htmltext = (player.getInventory().getItemCount(CARGO) < 30) ? "30385-05.htm" : "30385-06.htm";
						else if (cond == 2)
							htmltext = "30385-11.htm";
						else if (cond == 7)
						{
							htmltext = "30385-13.htm";
							rewardItems(player, 57, 14700);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ADONIUS:
						if (cond == 2)
							htmltext = "30375-01.htm";
						else if (cond == 3)
						{
							if (player.getInventory().hasItems(ROSARY, HOLY))
							{
								htmltext = "30375-04.htm";
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, HOLY, -1);
								takeItems(player, ROSARY, -1);
							}
							else
								htmltext = "30375-03.htm";
						}
						else if (cond == 4)
							htmltext = "30375-05.htm";
						break;
					
					case LEVIAN:
						if (cond == 4)
							htmltext = "30037-01.htm";
						else if (cond == 5)
							htmltext = "30037-03.htm";
						else if (cond == 6)
						{
							htmltext = "30037-04.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, TOTEM, -1);
						}
						else if (cond == 7)
							htmltext = "30037-05.htm";
						break;
					
					case CHEST:
						if (cond == 5)
							htmltext = "30989-01.htm";
						else
							htmltext = "30989-03.htm";
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
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case 20008:
				if (st.getCond() == 1)
					dropItems(player, CARGO, 1, 30, 500000);
				break;
			
			case 20010:
				if (st.getCond() == 1)
					dropItems(player, CARGO, 1, 30, 520000);
				break;
			
			case 20014:
				if (st.getCond() == 1)
					dropItems(player, CARGO, 1, 30, 550000);
				break;
			
			case 20024, 20027, 20030:
				if (st.getCond() == 3 && dropItems(player, HOLY, 1, 1, 100000))
					dropItems(player, ROSARY, 1, 1, 100000);
				break;
			
			case 25146:
				addSpawn(CHEST, npc, false, 30000, false);
				break;
		}
	}
}