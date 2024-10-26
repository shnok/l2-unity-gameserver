package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

/**
 * This quest supports both Q611 && Q612 onKill sections.
 */
public class Q611_AllianceWithVarkaSilenos extends Quest
{
	private static final String QUEST_NAME = "Q611_AllianceWithVarkaSilenos";
	private static final String qn2 = "Q612_WarWithKetraOrcs";
	
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(21);
	
	private static final Map<Integer, Integer> CHANCES_MOLAR = HashMap.newHashMap(15);
	
	// Quest Items
	private static final int KETRA_BADGE_SOLDIER = 7226;
	private static final int KETRA_BADGE_OFFICER = 7227;
	private static final int KETRA_BADGE_CAPTAIN = 7228;
	
	private static final int VARKA_ALLIANCE_1 = 7221;
	private static final int VARKA_ALLIANCE_2 = 7222;
	private static final int VARKA_ALLIANCE_3 = 7223;
	private static final int VARKA_ALLIANCE_4 = 7224;
	private static final int VARKA_ALLIANCE_5 = 7225;
	
	private static final int VALOR_FEATHER = 7229;
	private static final int WISDOM_FEATHER = 7230;
	
	private static final int MOLAR_OF_KETRA_ORC = 7234;
	
	public Q611_AllianceWithVarkaSilenos()
	{
		super(611, "Alliance with Varka Silenos");
		
		CHANCES.put(21324, 500000);
		CHANCES.put(21325, 500000);
		CHANCES.put(21327, 509000);
		CHANCES.put(21328, 521000);
		CHANCES.put(21329, 519000);
		CHANCES.put(21331, 500000);
		CHANCES.put(21332, 500000);
		CHANCES.put(21334, 509000);
		CHANCES.put(21335, 518000);
		CHANCES.put(21336, 518000);
		CHANCES.put(21338, 527000);
		CHANCES.put(21339, 500000);
		CHANCES.put(21340, 500000);
		CHANCES.put(21342, 508000);
		CHANCES.put(21343, 628000);
		CHANCES.put(21344, 604000);
		CHANCES.put(21345, 627000);
		CHANCES.put(21346, 604000);
		CHANCES.put(21347, 649000);
		CHANCES.put(21348, 626000);
		CHANCES.put(21349, 626000);
		
		CHANCES_MOLAR.put(21324, 500000);
		CHANCES_MOLAR.put(21327, 510000);
		CHANCES_MOLAR.put(21328, 522000);
		CHANCES_MOLAR.put(21329, 519000);
		CHANCES_MOLAR.put(21331, 529000);
		CHANCES_MOLAR.put(21332, 529000);
		CHANCES_MOLAR.put(21334, 539000);
		CHANCES_MOLAR.put(21336, 548000);
		CHANCES_MOLAR.put(21338, 558000);
		CHANCES_MOLAR.put(21339, 568000);
		CHANCES_MOLAR.put(21340, 568000);
		CHANCES_MOLAR.put(21342, 578000);
		CHANCES_MOLAR.put(21343, 664000);
		CHANCES_MOLAR.put(21345, 713000);
		CHANCES_MOLAR.put(21347, 738000);
		
		setItemsIds(KETRA_BADGE_SOLDIER, KETRA_BADGE_OFFICER, KETRA_BADGE_CAPTAIN);
		
		addQuestStart(31378); // Naran Ashanuk
		addTalkId(31378);
		
		for (int mobs : CHANCES.keySet())
			addMyDying(mobs);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31378-03a.htm"))
		{
			if (player.isAlliedWithKetra())
				htmltext = "31378-02a.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				playSound(player, SOUND_ACCEPT);
				for (int i = VARKA_ALLIANCE_1; i <= VARKA_ALLIANCE_5; i++)
				{
					if (player.getInventory().hasItem(i))
					{
						st.setCond(i - 7219);
						player.setAllianceWithVarkaKetra(7220 - i);
						return "31378-0" + (i - 7217) + ".htm";
					}
				}
				st.setCond(1);
			}
		}
		// Stage 1
		else if (event.equalsIgnoreCase("31378-10-1.htm"))
		{
			if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) >= 100)
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, KETRA_BADGE_SOLDIER, -1);
				giveItems(player, VARKA_ALLIANCE_1, 1);
				player.setAllianceWithVarkaKetra(-1);
			}
			else
				htmltext = "31378-03b.htm";
		}
		// Stage 2
		else if (event.equalsIgnoreCase("31378-10-2.htm"))
		{
			if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) >= 200 && player.getInventory().getItemCount(KETRA_BADGE_OFFICER) >= 100)
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, KETRA_BADGE_SOLDIER, -1);
				takeItems(player, KETRA_BADGE_OFFICER, -1);
				takeItems(player, VARKA_ALLIANCE_1, -1);
				giveItems(player, VARKA_ALLIANCE_2, 1);
				player.setAllianceWithVarkaKetra(-2);
			}
			else
				htmltext = "31378-12.htm";
		}
		// Stage 3
		else if (event.equalsIgnoreCase("31378-10-3.htm"))
		{
			if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) >= 300 && player.getInventory().getItemCount(KETRA_BADGE_OFFICER) >= 200 && player.getInventory().getItemCount(KETRA_BADGE_CAPTAIN) >= 100)
			{
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, KETRA_BADGE_SOLDIER, -1);
				takeItems(player, KETRA_BADGE_OFFICER, -1);
				takeItems(player, KETRA_BADGE_CAPTAIN, -1);
				takeItems(player, VARKA_ALLIANCE_2, -1);
				giveItems(player, VARKA_ALLIANCE_3, 1);
				player.setAllianceWithVarkaKetra(-3);
			}
			else
				htmltext = "31378-15.htm";
		}
		// Stage 4
		else if (event.equalsIgnoreCase("31378-10-4.htm"))
		{
			if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) >= 300 && player.getInventory().getItemCount(KETRA_BADGE_OFFICER) >= 300 && player.getInventory().getItemCount(KETRA_BADGE_CAPTAIN) >= 200 && player.getInventory().getItemCount(VALOR_FEATHER) >= 1)
			{
				st.setCond(5);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, KETRA_BADGE_SOLDIER, -1);
				takeItems(player, KETRA_BADGE_OFFICER, -1);
				takeItems(player, KETRA_BADGE_CAPTAIN, -1);
				takeItems(player, VALOR_FEATHER, -1);
				takeItems(player, VARKA_ALLIANCE_3, -1);
				giveItems(player, VARKA_ALLIANCE_4, 1);
				player.setAllianceWithVarkaKetra(-4);
			}
			else
				htmltext = "31378-21.htm";
		}
		// Leave quest
		else if (event.equalsIgnoreCase("31378-20.htm"))
		{
			takeItems(player, VARKA_ALLIANCE_1, -1);
			takeItems(player, VARKA_ALLIANCE_2, -1);
			takeItems(player, VARKA_ALLIANCE_3, -1);
			takeItems(player, VARKA_ALLIANCE_4, -1);
			takeItems(player, VARKA_ALLIANCE_5, -1);
			takeItems(player, VALOR_FEATHER, -1);
			takeItems(player, WISDOM_FEATHER, -1);
			player.setAllianceWithVarkaKetra(0);
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
				if (player.getStatus().getLevel() >= 74)
					htmltext = "31378-01.htm";
				else
				{
					htmltext = "31378-02b.htm";
					st.exitQuest(true);
					player.setAllianceWithVarkaKetra(0);
				}
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
				{
					if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) < 100)
						htmltext = "31378-03b.htm";
					else
						htmltext = "31378-09.htm";
				}
				else if (cond == 2)
				{
					if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) < 200 || player.getInventory().getItemCount(KETRA_BADGE_OFFICER) < 100)
						htmltext = "31378-12.htm";
					else
						htmltext = "31378-13.htm";
				}
				else if (cond == 3)
				{
					if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) < 300 || player.getInventory().getItemCount(KETRA_BADGE_OFFICER) < 200 || player.getInventory().getItemCount(KETRA_BADGE_CAPTAIN) < 100)
						htmltext = "31378-15.htm";
					else
						htmltext = "31378-16.htm";
				}
				else if (cond == 4)
				{
					if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) < 300 || player.getInventory().getItemCount(KETRA_BADGE_OFFICER) < 300 || player.getInventory().getItemCount(KETRA_BADGE_CAPTAIN) < 200 || !player.getInventory().hasItem(VALOR_FEATHER))
						htmltext = "31378-21.htm";
					else
						htmltext = "31378-22.htm";
				}
				else if (cond == 5)
				{
					if (player.getInventory().getItemCount(KETRA_BADGE_SOLDIER) < 400 || player.getInventory().getItemCount(KETRA_BADGE_OFFICER) < 400 || player.getInventory().getItemCount(KETRA_BADGE_CAPTAIN) < 200 || !player.getInventory().hasItem(WISDOM_FEATHER))
						htmltext = "31378-17.htm";
					else
					{
						htmltext = "31378-10-5.htm";
						st.setCond(6);
						playSound(player, SOUND_MIDDLE);
						takeItems(player, KETRA_BADGE_SOLDIER, 400);
						takeItems(player, KETRA_BADGE_OFFICER, 400);
						takeItems(player, KETRA_BADGE_CAPTAIN, 200);
						takeItems(player, WISDOM_FEATHER, -1);
						takeItems(player, VARKA_ALLIANCE_4, -1);
						giveItems(player, VARKA_ALLIANCE_5, 1);
						player.setAllianceWithVarkaKetra(-5);
					}
				}
				else if (cond == 6)
					htmltext = "31378-08.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		player = st.getPlayer();
		final int npcId = npc.getNpcId();
		
		// Support for Q612.
		final QuestState st2 = player.getQuestList().getQuestState(qn2);
		if (st2 != null && Rnd.nextBoolean() && CHANCES_MOLAR.containsKey(npcId))
		{
			dropItems(player, MOLAR_OF_KETRA_ORC, 1, 0, CHANCES_MOLAR.get(npcId));
			return;
		}
		
		final int cond = st.getCond();
		if (cond == 6)
			return;
		
		switch (npcId)
		{
			case 21324, 21325, 21327, 21328, 21329:
				if (cond == 1)
					dropItems(player, KETRA_BADGE_SOLDIER, 1, 100, CHANCES.get(npcId));
				else if (cond == 2)
					dropItems(player, KETRA_BADGE_SOLDIER, 1, 200, CHANCES.get(npcId));
				else if (cond == 3 || cond == 4)
					dropItems(player, KETRA_BADGE_SOLDIER, 1, 300, CHANCES.get(npcId));
				else if (cond == 5)
					dropItems(player, KETRA_BADGE_SOLDIER, 1, 400, CHANCES.get(npcId));
				break;
			
			case 21331, 21332, 21334, 21335, 21336, 21338, 21343, 21344:
				if (cond == 2)
					dropItems(player, KETRA_BADGE_OFFICER, 1, 100, CHANCES.get(npcId));
				else if (cond == 3)
					dropItems(player, KETRA_BADGE_OFFICER, 1, 200, CHANCES.get(npcId));
				else if (cond == 4)
					dropItems(player, KETRA_BADGE_OFFICER, 1, 300, CHANCES.get(npcId));
				else if (cond == 5)
					dropItems(player, KETRA_BADGE_OFFICER, 1, 400, CHANCES.get(npcId));
				break;
			
			case 21339, 21340, 21342, 21345, 21346, 21347, 21348, 21349:
				if (cond == 3)
					dropItems(player, KETRA_BADGE_CAPTAIN, 1, 100, CHANCES.get(npcId));
				else if (cond == 4 || cond == 5)
					dropItems(player, KETRA_BADGE_CAPTAIN, 1, 200, CHANCES.get(npcId));
				break;
		}
	}
}