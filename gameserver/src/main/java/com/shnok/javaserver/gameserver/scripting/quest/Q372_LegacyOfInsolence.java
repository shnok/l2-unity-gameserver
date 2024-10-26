package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q372_LegacyOfInsolence extends Quest
{
	private static final String QUEST_NAME = "Q372_LegacyOfInsolence";
	
	// NPCs
	private static final int WALDERAL = 30844;
	private static final int PATRIN = 30929;
	private static final int HOLLY = 30839;
	private static final int CLAUDIA = 31001;
	private static final int DESMOND = 30855;
	
	// Monsters
	private static final int[][] MONSTERS_DROPS =
	{
		// npcId
		{
			20817,
			20821,
			20825,
			20829,
			21069,
			21063
		},
		// parchment (red, blue, black, white)
		{
			5966,
			5966,
			5966,
			5967,
			5968,
			5969
		},
		// rate
		{
			302000,
			410000,
			447000,
			451000,
			280000,
			290000
		}
	};
	
	// Items
	private static final int[][] SCROLLS =
	{
		// Walderal => 13 blueprints => parts, recipes.
		{
			5989,
			6001
		},
		// Holly -> 5x Imperial Genealogy -> Dark Crystal parts/Adena
		{
			5984,
			5988
		},
		// Patrin -> 5x Ancient Epic -> Tallum parts/Adena
		{
			5979,
			5983
		},
		// Claudia -> 7x Revelation of the Seals -> Nightmare parts/Adena
		{
			5972,
			5978
		},
		// Desmond -> 7x Revelation of the Seals -> Majestic parts/Adena
		{
			5972,
			5978
		}
	};
	
	public Q372_LegacyOfInsolence()
	{
		super(372, "Legacy of Insolence");
		
		addQuestStart(WALDERAL);
		addTalkId(WALDERAL, PATRIN, HOLLY, CLAUDIA, DESMOND);
		
		addMyDying(MONSTERS_DROPS[0]);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30844-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30844-05b.htm"))
		{
			if (st.getCond() == 1)
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("30844-07.htm"))
		{
			for (int blueprint = 5989; blueprint <= 6001; blueprint++)
			{
				if (!player.getInventory().hasItem(blueprint))
				{
					htmltext = "30844-06.htm";
					break;
				}
			}
		}
		else if (event.startsWith("30844-07-"))
		{
			if (!checkAndTakeItems(player, 0))
				return npc.getNpcId() + "-07a.htm";
			
			final int chance = Rnd.get(100);
			
			switch (Integer.parseInt(event.substring(9, 10)))
			{
				case 0:
					if (chance < 10)
						rewardItems(player, 5496, 1);
					else if (chance < 20)
						rewardItems(player, 5508, 1);
					else if (chance < 30)
						rewardItems(player, 5525, 1);
					else if (chance < 40)
					{
						rewardItems(player, 5496, 1);
						rewardItems(player, 5508, 1);
						rewardItems(player, 5525, 1);
					}
					else if (chance < 51)
						rewardItems(player, 5368, 1);
					else if (chance < 62)
						rewardItems(player, 5392, 1);
					else if (chance < 79)
						rewardItems(player, 5426, 1);
					else
					{
						rewardItems(player, 5368, 1);
						rewardItems(player, 5392, 1);
						rewardItems(player, 5426, 1);
					}
					break;
				
				case 1:
					if (chance < 10)
						rewardItems(player, 5497, 1);
					else if (chance < 20)
						rewardItems(player, 5509, 1);
					else if (chance < 30)
						rewardItems(player, 5526, 1);
					else if (chance < 40)
					{
						rewardItems(player, 5497, 1);
						rewardItems(player, 5509, 1);
						rewardItems(player, 5526, 1);
					}
					else if (chance < 51)
						rewardItems(player, 5370, 1);
					else if (chance < 62)
						rewardItems(player, 5394, 1);
					else if (chance < 79)
						rewardItems(player, 5428, 1);
					else
					{
						rewardItems(player, 5370, 1);
						rewardItems(player, 5394, 1);
						rewardItems(player, 5428, 1);
					}
					break;
				
				case 2:
					if (chance < 17)
						rewardItems(player, 5502, 1);
					else if (chance < 34)
						rewardItems(player, 5514, 1);
					else if (chance < 49)
						rewardItems(player, 5527, 1);
					else if (chance < 58)
					{
						rewardItems(player, 5502, 1);
						rewardItems(player, 5514, 1);
						rewardItems(player, 5527, 1);
					}
					else if (chance < 70)
						rewardItems(player, 5380, 1);
					else if (chance < 82)
						rewardItems(player, 5404, 1);
					else if (chance < 92)
						rewardItems(player, 5430, 1);
					else
					{
						rewardItems(player, 5380, 1);
						rewardItems(player, 5404, 1);
						rewardItems(player, 5430, 1);
					}
					break;
				
				case 3:
					if (chance < 17)
						rewardItems(player, 5503, 1);
					else if (chance < 34)
						rewardItems(player, 5515, 1);
					else if (chance < 49)
						rewardItems(player, 5528, 1);
					else if (chance < 58)
					{
						rewardItems(player, 5503, 1);
						rewardItems(player, 5515, 1);
						rewardItems(player, 5528, 1);
					}
					else if (chance < 70)
						rewardItems(player, 5382, 1);
					else if (chance < 82)
						rewardItems(player, 5406, 1);
					else if (chance < 92)
						rewardItems(player, 5432, 1);
					else
					{
						rewardItems(player, 5382, 1);
						rewardItems(player, 5406, 1);
						rewardItems(player, 5432, 1);
					}
					break;
			}
		}
		else if (event.equalsIgnoreCase("30844-09.htm"))
		{
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
				htmltext = (player.getStatus().getLevel() < 59) ? "30844-01.htm" : "30844-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case WALDERAL:
						htmltext = "30844-05.htm";
						break;
					
					case HOLLY:
						if (!checkAndTakeItems(player, 1))
							htmltext = "30839-01.htm";
						else
						{
							final int chance = Rnd.get(100);
							if (chance < 30)
								rewardItems(player, 5496, 1);
							else if (chance < 60)
								rewardItems(player, 5508, 1);
							else if (chance < 80)
								rewardItems(player, 5525, 1);
							else if (chance < 90)
							{
								rewardItems(player, 5496, 1);
								rewardItems(player, 5508, 1);
								rewardItems(player, 5525, 1);
							}
							else if (chance < 100)
								rewardItems(player, 57, 4000);
							
							htmltext = "30839-02.htm";
						}
						break;
					
					case PATRIN:
						if (!checkAndTakeItems(player, 2))
							htmltext = "30929-01.htm";
						else
						{
							final int chance = Rnd.get(100);
							if (chance < 30)
								rewardItems(player, 5497, 1);
							else if (chance < 60)
								rewardItems(player, 5509, 1);
							else if (chance < 80)
								rewardItems(player, 5526, 1);
							else if (chance < 90)
							{
								rewardItems(player, 5497, 1);
								rewardItems(player, 5509, 1);
								rewardItems(player, 5526, 1);
							}
							else if (chance < 100)
								rewardItems(player, 57, 4000);
							
							htmltext = "30929-02.htm";
						}
						break;
					
					case CLAUDIA:
						if (!checkAndTakeItems(player, 3))
							htmltext = "31001-01.htm";
						else
						{
							final int chance = Rnd.get(100);
							if (chance < 31)
								rewardItems(player, 5502, 1);
							else if (chance < 62)
								rewardItems(player, 5514, 1);
							else if (chance < 75)
								rewardItems(player, 5527, 1);
							else if (chance < 83)
							{
								rewardItems(player, 5502, 1);
								rewardItems(player, 5514, 1);
								rewardItems(player, 5527, 1);
							}
							else if (chance < 100)
								rewardItems(player, 57, 4000);
							
							htmltext = "31001-02.htm";
						}
						break;
					
					case DESMOND:
						if (!checkAndTakeItems(player, 4))
							htmltext = "30855-01.htm";
						else
						{
							final int chance = Rnd.get(100);
							if (chance < 31)
								rewardItems(player, 5503, 1);
							else if (chance < 62)
								rewardItems(player, 5515, 1);
							else if (chance < 75)
								rewardItems(player, 5528, 1);
							else if (chance < 83)
							{
								rewardItems(player, 5503, 1);
								rewardItems(player, 5515, 1);
								rewardItems(player, 5528, 1);
							}
							else if (chance < 100)
								rewardItems(player, 57, 4000);
							
							htmltext = "30855-02.htm";
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
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int npcId = npc.getNpcId();
		
		for (int i = 0; i < MONSTERS_DROPS[0].length; i++)
		{
			if (MONSTERS_DROPS[0][i] == npcId)
			{
				dropItems(st.getPlayer(), MONSTERS_DROPS[1][i], 1, 0, MONSTERS_DROPS[2][i]);
				break;
			}
		}
	}
	
	private static boolean checkAndTakeItems(Player player, int itemType)
	{
		// Retrieve array with items to check.
		final int[] itemsToCheck = SCROLLS[itemType];
		
		// Check set of items.
		for (int item = itemsToCheck[0]; item <= itemsToCheck[1]; item++)
			if (!player.getInventory().hasItem(item))
				return false;
			
		// Remove set of items.
		for (int item = itemsToCheck[0]; item <= itemsToCheck[1]; item++)
			takeItems(player, item, 1);
		
		return true;
	}
}