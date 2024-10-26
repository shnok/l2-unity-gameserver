package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q116_BeyondTheHillsOfWinter extends Quest
{
	private static final String QUEST_NAME = "Q116_BeyondTheHillsOfWinter";
	
	// NPCs
	private static final int FILAUR = 30535;
	private static final int OBI = 32052;
	
	// Items
	private static final int BANDAGE = 1833;
	private static final int ENERGY_STONE = 5589;
	private static final int THIEF_KEY = 1661;
	private static final int GOODS = 8098;
	
	// Reward
	private static final int SSD = 1463;
	
	public Q116_BeyondTheHillsOfWinter()
	{
		super(116, "Beyond the Hills of Winter");
		
		setItemsIds(GOODS);
		
		addQuestStart(FILAUR);
		addTalkId(FILAUR, OBI);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30535-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30535-05.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, GOODS, 1);
		}
		else if (event.equalsIgnoreCase("materials"))
		{
			htmltext = "32052-02.htm";
			takeItems(player, GOODS, -1);
			rewardItems(player, SSD, 1650);
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("adena"))
		{
			htmltext = "32052-02.htm";
			takeItems(player, GOODS, -1);
			giveItems(player, 57, 16500);
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
				htmltext = (player.getStatus().getLevel() < 30 || player.getRace() != ClassRace.DWARF) ? "30535-00.htm" : "30535-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case FILAUR:
						if (cond == 1)
						{
							if (player.getInventory().getItemCount(BANDAGE) >= 20 && player.getInventory().getItemCount(ENERGY_STONE) >= 5 && player.getInventory().getItemCount(THIEF_KEY) >= 10)
							{
								htmltext = "30535-03.htm";
								takeItems(player, BANDAGE, 20);
								takeItems(player, ENERGY_STONE, 5);
								takeItems(player, THIEF_KEY, 10);
							}
							else
								htmltext = "30535-04.htm";
						}
						else if (cond == 2)
							htmltext = "30535-05.htm";
						break;
					
					case OBI:
						if (cond == 2)
							htmltext = "32052-00.htm";
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