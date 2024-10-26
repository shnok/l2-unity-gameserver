package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q292_BrigandsSweep extends Quest
{
	private static final String QUEST_NAME = "Q292_BrigandsSweep";
	
	// NPCs
	private static final int SPIRON = 30532;
	private static final int BALANKI = 30533;
	
	// Items
	private static final int GOBLIN_NECKLACE = 1483;
	private static final int GOBLIN_PENDANT = 1484;
	private static final int GOBLIN_LORD_PENDANT = 1485;
	private static final int SUSPICIOUS_MEMO = 1486;
	private static final int SUSPICIOUS_CONTRACT = 1487;
	
	// Monsters
	private static final int GOBLIN_BRIGAND = 20322;
	private static final int GOBLIN_BRIGAND_LEADER = 20323;
	private static final int GOBLIN_BRIGAND_LIEUTENANT = 20324;
	private static final int GOBLIN_SNOOPER = 20327;
	private static final int GOBLIN_LORD = 20528;
	
	public Q292_BrigandsSweep()
	{
		super(292, "Brigands Sweep");
		
		setItemsIds(GOBLIN_NECKLACE, GOBLIN_PENDANT, GOBLIN_LORD_PENDANT, SUSPICIOUS_MEMO, SUSPICIOUS_CONTRACT);
		
		addQuestStart(SPIRON);
		addTalkId(SPIRON, BALANKI);
		
		addMyDying(GOBLIN_BRIGAND, GOBLIN_BRIGAND_LEADER, GOBLIN_BRIGAND_LIEUTENANT, GOBLIN_SNOOPER, GOBLIN_LORD);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30532-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30532-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				if (player.getRace() != ClassRace.DWARF)
					htmltext = "30532-00.htm";
				else if (player.getStatus().getLevel() < 5)
					htmltext = "30532-01.htm";
				else
					htmltext = "30532-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case SPIRON:
						final int goblinNecklaces = player.getInventory().getItemCount(GOBLIN_NECKLACE);
						final int goblinPendants = player.getInventory().getItemCount(GOBLIN_PENDANT);
						final int goblinLordPendants = player.getInventory().getItemCount(GOBLIN_LORD_PENDANT);
						final int suspiciousMemos = player.getInventory().getItemCount(SUSPICIOUS_MEMO);
						
						final int countAll = goblinNecklaces + goblinPendants + goblinLordPendants;
						
						final boolean hasContract = player.getInventory().hasItem(SUSPICIOUS_CONTRACT);
						
						if (countAll == 0)
							htmltext = "30532-04.htm";
						else
						{
							if (hasContract)
								htmltext = "30532-10.htm";
							else if (suspiciousMemos > 0)
							{
								if (suspiciousMemos > 1)
									htmltext = "30532-09.htm";
								else
									htmltext = "30532-08.htm";
							}
							else
								htmltext = "30532-05.htm";
							
							takeItems(player, GOBLIN_NECKLACE, -1);
							takeItems(player, GOBLIN_PENDANT, -1);
							takeItems(player, GOBLIN_LORD_PENDANT, -1);
							
							if (hasContract)
							{
								st.setCond(1);
								takeItems(player, SUSPICIOUS_CONTRACT, -1);
							}
							
							rewardItems(player, 57, ((12 * goblinNecklaces) + (36 * goblinPendants) + (33 * goblinLordPendants) + (countAll >= 10 ? 1000 : 0) + ((hasContract) ? 1120 : 0)));
						}
						break;
					
					case BALANKI:
						if (!player.getInventory().hasItem(SUSPICIOUS_CONTRACT))
							htmltext = "30533-01.htm";
						else
						{
							htmltext = "30533-02.htm";
							st.setCond(1);
							takeItems(player, SUSPICIOUS_CONTRACT, -1);
							rewardItems(player, 57, 1500);
						}
						break;
				}
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
		
		final int chance = Rnd.get(10);
		
		if (chance > 5)
		{
			switch (npc.getNpcId())
			{
				case GOBLIN_BRIGAND, GOBLIN_SNOOPER, GOBLIN_BRIGAND_LIEUTENANT:
					dropItemsAlways(player, GOBLIN_NECKLACE, 1, 0);
					break;
				
				case GOBLIN_BRIGAND_LEADER:
					dropItemsAlways(player, GOBLIN_PENDANT, 1, 0);
					break;
				
				case GOBLIN_LORD:
					dropItemsAlways(player, GOBLIN_LORD_PENDANT, 1, 0);
					break;
			}
		}
		else if (chance > 4 && st.getCond() == 1 && dropItemsAlways(player, SUSPICIOUS_MEMO, 1, 3))
		{
			st.setCond(2);
			takeItems(player, SUSPICIOUS_MEMO, -1);
			giveItems(player, SUSPICIOUS_CONTRACT, 1);
		}
	}
}