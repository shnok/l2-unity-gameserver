package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q039_RedEyedInvaders extends Quest
{
	private static final String QUEST_NAME = "Q039_RedEyedInvaders";
	
	// NPCs
	private static final int BABENCO = 30334;
	private static final int BATHIS = 30332;
	
	// Mobs
	private static final int MAILLE_LIZARDMAN = 20919;
	private static final int MAILLE_LIZARDMAN_SCOUT = 20920;
	private static final int MAILLE_LIZARDMAN_GUARD = 20921;
	private static final int ARANEID = 20925;
	
	// Items
	private static final int BLACK_BONE_NECKLACE = 7178;
	private static final int RED_BONE_NECKLACE = 7179;
	private static final int INCENSE_POUCH = 7180;
	private static final int GEM_OF_MAILLE = 7181;
	
	// First droplist
	private static final Map<Integer, int[]> FIRST_DP = new HashMap<>();
	{
		FIRST_DP.put(MAILLE_LIZARDMAN_GUARD, new int[]
		{
			RED_BONE_NECKLACE,
			BLACK_BONE_NECKLACE
		});
		FIRST_DP.put(MAILLE_LIZARDMAN, new int[]
		{
			BLACK_BONE_NECKLACE,
			RED_BONE_NECKLACE
		});
		FIRST_DP.put(MAILLE_LIZARDMAN_SCOUT, new int[]
		{
			BLACK_BONE_NECKLACE,
			RED_BONE_NECKLACE
		});
	}
	
	// Second droplist
	private static final Map<Integer, int[]> SECOND_DP = new HashMap<>();
	{
		SECOND_DP.put(ARANEID, new int[]
		{
			GEM_OF_MAILLE,
			INCENSE_POUCH,
			500000
		});
		SECOND_DP.put(MAILLE_LIZARDMAN_GUARD, new int[]
		{
			INCENSE_POUCH,
			GEM_OF_MAILLE,
			300000
		});
		SECOND_DP.put(MAILLE_LIZARDMAN_SCOUT, new int[]
		{
			INCENSE_POUCH,
			GEM_OF_MAILLE,
			250000
		});
	}
	
	// Rewards
	private static final int GREEN_COLORED_LURE_HG = 6521;
	private static final int BABY_DUCK_RODE = 6529;
	private static final int FISHING_SHOT_NG = 6535;
	
	public Q039_RedEyedInvaders()
	{
		super(39, "Red-Eyed Invaders");
		
		setItemsIds(BLACK_BONE_NECKLACE, RED_BONE_NECKLACE, INCENSE_POUCH, GEM_OF_MAILLE);
		
		addQuestStart(BABENCO);
		addTalkId(BABENCO, BATHIS);
		
		addMyDying(MAILLE_LIZARDMAN, MAILLE_LIZARDMAN_SCOUT, MAILLE_LIZARDMAN_GUARD, ARANEID);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30334-1.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30332-1.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30332-3.htm"))
		{
			st.setCond(4);
			takeItems(player, BLACK_BONE_NECKLACE, -1);
			takeItems(player, RED_BONE_NECKLACE, -1);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30332-5.htm"))
		{
			takeItems(player, INCENSE_POUCH, -1);
			takeItems(player, GEM_OF_MAILLE, -1);
			giveItems(player, GREEN_COLORED_LURE_HG, 60);
			giveItems(player, BABY_DUCK_RODE, 1);
			giveItems(player, FISHING_SHOT_NG, 500);
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
				htmltext = (player.getStatus().getLevel() < 20) ? "30334-2.htm" : "30334-0.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case BABENCO:
						htmltext = "30334-3.htm";
						break;
					
					case BATHIS:
						if (cond == 1)
							htmltext = "30332-0.htm";
						else if (cond == 2)
							htmltext = "30332-2a.htm";
						else if (cond == 3)
							htmltext = "30332-2.htm";
						else if (cond == 4)
							htmltext = "30332-3a.htm";
						else if (cond == 5)
							htmltext = "30332-4.htm";
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
		Player player = killer.getActingPlayer();
		final int npcId = npc.getNpcId();
		
		QuestState st = getRandomPartyMember(player, npc, 2);
		if (st != null && npcId != ARANEID)
		{
			final int[] list = FIRST_DP.get(npcId);
			
			player = st.getPlayer();
			if (dropItems(player, list[0], 1, 100, 500000) && player.getInventory().getItemCount(list[1]) == 100)
				st.setCond(3);
		}
		else
		{
			st = getRandomPartyMember(player, npc, 4);
			if (st != null && npcId != MAILLE_LIZARDMAN)
			{
				final int[] list = SECOND_DP.get(npcId);
				
				player = st.getPlayer();
				if (dropItems(player, list[0], 1, 30, list[2]) && player.getInventory().getItemCount(list[1]) == 30)
					st.setCond(5);
			}
		}
	}
}