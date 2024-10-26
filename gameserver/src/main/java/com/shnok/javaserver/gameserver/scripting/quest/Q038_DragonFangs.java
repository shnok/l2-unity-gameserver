package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q038_DragonFangs extends Quest
{
	private static final String QUEST_NAME = "Q038_DragonFangs";
	
	// Items
	private static final int FEATHER_ORNAMENT = 7173;
	private static final int TOOTH_OF_TOTEM = 7174;
	private static final int TOOTH_OF_DRAGON = 7175;
	private static final int LETTER_OF_IRIS = 7176;
	private static final int LETTER_OF_ROHMER = 7177;
	
	// NPCs
	private static final int LUIS = 30386;
	private static final int IRIS = 30034;
	private static final int ROHMER = 30344;
	
	// Reward { item, adena }
	private static final int[][] REWARDS =
	{
		{
			45,
			5200
		},
		{
			627,
			1500
		},
		{
			1123,
			3200
		},
		{
			605,
			3200
		}
	};
	
	// Droplist
	private static final Map<Integer, int[]> DROPLIST = new HashMap<>();
	static
	{
		DROPLIST.put(21100, new int[]
		{
			1,
			FEATHER_ORNAMENT,
			100,
			1000000
		});
		DROPLIST.put(20357, new int[]
		{
			1,
			FEATHER_ORNAMENT,
			100,
			1000000
		});
		DROPLIST.put(21101, new int[]
		{
			6,
			TOOTH_OF_DRAGON,
			50,
			500000
		});
		DROPLIST.put(20356, new int[]
		{
			6,
			TOOTH_OF_DRAGON,
			50,
			500000
		});
	}
	
	public Q038_DragonFangs()
	{
		super(38, "Dragon Fangs");
		
		setItemsIds(FEATHER_ORNAMENT, TOOTH_OF_TOTEM, TOOTH_OF_DRAGON, LETTER_OF_IRIS, LETTER_OF_ROHMER);
		
		addQuestStart(LUIS);
		addTalkId(LUIS, IRIS, ROHMER);
		
		for (int mob : DROPLIST.keySet())
			addMyDying(mob);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30386-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30386-04.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, FEATHER_ORNAMENT, 100);
			giveItems(player, TOOTH_OF_TOTEM, 1);
		}
		else if (event.equalsIgnoreCase("30034-02a.htm"))
		{
			if (player.getInventory().hasItem(TOOTH_OF_TOTEM))
			{
				htmltext = "30034-02.htm";
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, TOOTH_OF_TOTEM, 1);
				giveItems(player, LETTER_OF_IRIS, 1);
			}
		}
		else if (event.equalsIgnoreCase("30344-02a.htm"))
		{
			if (player.getInventory().hasItem(LETTER_OF_IRIS))
			{
				htmltext = "30344-02.htm";
				st.setCond(5);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, LETTER_OF_IRIS, 1);
				giveItems(player, LETTER_OF_ROHMER, 1);
			}
		}
		else if (event.equalsIgnoreCase("30034-04a.htm"))
		{
			if (player.getInventory().hasItem(LETTER_OF_ROHMER))
			{
				htmltext = "30034-04.htm";
				st.setCond(6);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, LETTER_OF_ROHMER, 1);
			}
		}
		else if (event.equalsIgnoreCase("30034-06a.htm"))
		{
			if (player.getInventory().getItemCount(TOOTH_OF_DRAGON) >= 50)
			{
				int position = Rnd.get(REWARDS.length);
				
				htmltext = "30034-06.htm";
				takeItems(player, TOOTH_OF_DRAGON, 50);
				giveItems(player, REWARDS[position][0], 1);
				rewardItems(player, 57, REWARDS[position][1]);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
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
				htmltext = (player.getStatus().getLevel() < 19) ? "30386-01a.htm" : "30386-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case LUIS:
						if (cond == 1)
							htmltext = "30386-02a.htm";
						else if (cond == 2)
							htmltext = "30386-03.htm";
						else if (cond > 2)
							htmltext = "30386-03a.htm";
						break;
					
					case IRIS:
						if (cond == 3)
							htmltext = "30034-01.htm";
						else if (cond == 4)
							htmltext = "30034-02b.htm";
						else if (cond == 5)
							htmltext = "30034-03.htm";
						else if (cond == 6)
							htmltext = "30034-05a.htm";
						else if (cond == 7)
							htmltext = "30034-05.htm";
						break;
					
					case ROHMER:
						if (cond == 4)
							htmltext = "30344-01.htm";
						else if (cond > 4)
							htmltext = "30344-03.htm";
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
		
		final int[] droplist = DROPLIST.get(npc.getNpcId());
		
		if (st.getCond() == droplist[0] && dropItems(player, droplist[1], 1, droplist[2], droplist[3]))
			st.setCond(droplist[0] + 1);
	}
}