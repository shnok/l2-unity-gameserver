package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q627_HeartInSearchOfPower extends Quest
{
	private static final String QUEST_NAME = "Q627_HeartInSearchOfPower";
	
	// NPCs
	private static final int NECROMANCER = 31518;
	private static final int ENFEUX = 31519;
	
	// Items
	private static final int SEAL_OF_LIGHT = 7170;
	private static final int BEAD_OF_OBEDIENCE = 7171;
	private static final int GEM_OF_SAINTS = 7172;
	
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(14);
	
	// Rewards
	private static final Map<String, int[]> REWARDS = HashMap.newHashMap(5);
	
	public Q627_HeartInSearchOfPower()
	{
		super(627, "Heart in Search of Power");
		
		CHANCES.put(21520, 550000);
		CHANCES.put(21523, 584000);
		CHANCES.put(21524, 621000);
		CHANCES.put(21525, 621000);
		CHANCES.put(21526, 606000);
		CHANCES.put(21529, 625000);
		CHANCES.put(21530, 578000);
		CHANCES.put(21531, 690000);
		CHANCES.put(21532, 671000);
		CHANCES.put(21535, 693000);
		CHANCES.put(21536, 615000);
		CHANCES.put(21539, 762000);
		CHANCES.put(21540, 762000);
		CHANCES.put(21658, 690000);
		
		REWARDS.put("adena", new int[]
		{
			0,
			0,
			100000
		});
		REWARDS.put("asofe", new int[]
		{
			4043,
			13,
			6400
		});
		REWARDS.put("thon", new int[]
		{
			4044,
			13,
			6400
		});
		REWARDS.put("enria", new int[]
		{
			4042,
			6,
			13600
		});
		REWARDS.put("mold", new int[]
		{
			4041,
			3,
			17200
		});
		
		setItemsIds(BEAD_OF_OBEDIENCE);
		
		addQuestStart(NECROMANCER);
		addTalkId(NECROMANCER, ENFEUX);
		
		for (int npcId : CHANCES.keySet())
			addMyDying(npcId);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31518-01.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31518-03.htm"))
		{
			if (player.getInventory().getItemCount(BEAD_OF_OBEDIENCE) == 300)
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, BEAD_OF_OBEDIENCE, -1);
				giveItems(player, SEAL_OF_LIGHT, 1);
			}
			else
			{
				htmltext = "31518-03a.htm";
				st.setCond(1);
				takeItems(player, BEAD_OF_OBEDIENCE, -1);
			}
		}
		else if (event.equalsIgnoreCase("31519-01.htm"))
		{
			if (player.getInventory().getItemCount(SEAL_OF_LIGHT) == 1)
			{
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, SEAL_OF_LIGHT, 1);
				giveItems(player, GEM_OF_SAINTS, 1);
			}
		}
		else if (REWARDS.containsKey(event))
		{
			if (player.getInventory().getItemCount(GEM_OF_SAINTS) == 1)
			{
				htmltext = "31518-07.htm";
				takeItems(player, GEM_OF_SAINTS, 1);
				
				final int[] reward = REWARDS.get(event);
				if (reward[0] > 0)
					giveItems(player, reward[0], reward[1]);
				
				rewardItems(player, 57, reward[2]);
				
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "31518-7.htm";
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
				htmltext = (player.getStatus().getLevel() < 60) ? "31518-00a.htm" : "31518-00.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case NECROMANCER:
						if (cond == 1)
							htmltext = "31518-01a.htm";
						else if (cond == 2)
							htmltext = "31518-02.htm";
						else if (cond == 3)
							htmltext = "31518-04.htm";
						else if (cond == 4)
							htmltext = "31518-05.htm";
						break;
					
					case ENFEUX:
						if (cond == 3)
							htmltext = "31519-00.htm";
						else if (cond == 4)
							htmltext = "31519-02.htm";
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
		
		final QuestState st = getRandomPartyMember(player, npc, 1);
		if (st == null)
			return;
		
		if (dropItems(st.getPlayer(), BEAD_OF_OBEDIENCE, 1, 300, CHANCES.get(npc.getNpcId())))
			st.setCond(2);
	}
}