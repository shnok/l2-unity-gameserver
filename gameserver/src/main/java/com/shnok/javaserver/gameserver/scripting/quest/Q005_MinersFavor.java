package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q005_MinersFavor extends Quest
{
	private static final String QUEST_NAME = "Q005_MinersFavor";
	
	// NPCs
	private static final int BOLTER = 30554;
	private static final int SHARI = 30517;
	private static final int GARITA = 30518;
	private static final int REED = 30520;
	private static final int BRUNON = 30526;
	
	// Items
	private static final int BOLTERS_LIST = 1547;
	private static final int MINING_BOOTS = 1548;
	private static final int MINERS_PICK = 1549;
	private static final int BOOMBOOM_POWDER = 1550;
	private static final int REDSTONE_BEER = 1551;
	private static final int BOLTERS_SMELLY_SOCKS = 1552;
	
	// Reward
	private static final int NECKLACE = 906;
	
	public Q005_MinersFavor()
	{
		super(5, "Miner's Favor");
		
		setItemsIds(BOLTERS_LIST, MINING_BOOTS, MINERS_PICK, BOOMBOOM_POWDER, REDSTONE_BEER, BOLTERS_SMELLY_SOCKS);
		
		addQuestStart(BOLTER);
		addTalkId(BOLTER, SHARI, GARITA, REED, BRUNON);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30554-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, BOLTERS_LIST, 1);
			giveItems(player, BOLTERS_SMELLY_SOCKS, 1);
		}
		else if (event.equalsIgnoreCase("30526-02.htm"))
		{
			takeItems(player, BOLTERS_SMELLY_SOCKS, 1);
			giveItems(player, MINERS_PICK, 1);
			
			if (player.getInventory().hasItems(MINING_BOOTS, BOOMBOOM_POWDER, REDSTONE_BEER))
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
			}
			else
				playSound(player, SOUND_ITEMGET);
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
				htmltext = (player.getStatus().getLevel() < 2) ? "30554-01.htm" : "30554-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case BOLTER:
						if (cond == 1)
							htmltext = "30554-04.htm";
						else if (cond == 2)
						{
							htmltext = "30554-06.htm";
							takeItems(player, BOLTERS_LIST, 1);
							takeItems(player, BOOMBOOM_POWDER, 1);
							takeItems(player, MINERS_PICK, 1);
							takeItems(player, MINING_BOOTS, 1);
							takeItems(player, REDSTONE_BEER, 1);
							giveItems(player, NECKLACE, 1);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case SHARI:
						if (cond == 1 && !player.getInventory().hasItem(BOOMBOOM_POWDER))
						{
							htmltext = "30517-01.htm";
							giveItems(player, BOOMBOOM_POWDER, 1);
							
							if (player.getInventory().hasItems(MINING_BOOTS, MINERS_PICK, REDSTONE_BEER))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else
							htmltext = "30517-02.htm";
						break;
					
					case GARITA:
						if (cond == 1 && !player.getInventory().hasItem(MINING_BOOTS))
						{
							htmltext = "30518-01.htm";
							giveItems(player, MINING_BOOTS, 1);
							
							if (player.getInventory().hasItems(MINERS_PICK, BOOMBOOM_POWDER, REDSTONE_BEER))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else
							htmltext = "30518-02.htm";
						break;
					
					case REED:
						if (cond == 1 && !player.getInventory().hasItem(REDSTONE_BEER))
						{
							htmltext = "30520-01.htm";
							giveItems(player, REDSTONE_BEER, 1);
							
							if (player.getInventory().hasItems(MINING_BOOTS, MINERS_PICK, BOOMBOOM_POWDER))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else
							htmltext = "30520-02.htm";
						break;
					
					case BRUNON:
						if (cond == 1 && !player.getInventory().hasItem(MINERS_PICK))
							htmltext = "30526-01.htm";
						else
							htmltext = "30526-03.htm";
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