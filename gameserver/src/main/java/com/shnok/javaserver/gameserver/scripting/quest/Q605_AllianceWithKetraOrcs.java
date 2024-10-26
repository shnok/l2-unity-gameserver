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
 * This quest supports both Q605 && Q606 onKill sections.
 */
public class Q605_AllianceWithKetraOrcs extends Quest
{
	private static final String QUEST_NAME = "Q605_AllianceWithKetraOrcs";
	private static final String qn2 = "Q606_WarWithVarkaSilenos";
	
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(21);
	
	private static final Map<Integer, Integer> CHANCES_MANE = HashMap.newHashMap(15);
	
	// Quest Items
	private static final int VARKA_BADGE_SOLDIER = 7216;
	private static final int VARKA_BADGE_OFFICER = 7217;
	private static final int VARKA_BADGE_CAPTAIN = 7218;
	
	private static final int KETRA_ALLIANCE_1 = 7211;
	private static final int KETRA_ALLIANCE_2 = 7212;
	private static final int KETRA_ALLIANCE_3 = 7213;
	private static final int KETRA_ALLIANCE_4 = 7214;
	private static final int KETRA_ALLIANCE_5 = 7215;
	
	private static final int TOTEM_OF_VALOR = 7219;
	private static final int TOTEM_OF_WISDOM = 7220;
	
	private static final int VARKA_MANE = 7233;
	
	public Q605_AllianceWithKetraOrcs()
	{
		super(605, "Alliance with Ketra Orcs");
		
		CHANCES.put(21350, 500000);
		CHANCES.put(21351, 500000);
		CHANCES.put(21353, 509000);
		CHANCES.put(21354, 521000);
		CHANCES.put(21355, 519000);
		CHANCES.put(21357, 500000);
		CHANCES.put(21358, 500000);
		CHANCES.put(21360, 509000);
		CHANCES.put(21361, 518000);
		CHANCES.put(21362, 518000);
		CHANCES.put(21364, 527000);
		CHANCES.put(21365, 500000);
		CHANCES.put(21366, 500000);
		CHANCES.put(21368, 508000);
		CHANCES.put(21369, 628000);
		CHANCES.put(21370, 604000);
		CHANCES.put(21371, 627000);
		CHANCES.put(21372, 604000);
		CHANCES.put(21373, 649000);
		CHANCES.put(21374, 626000);
		CHANCES.put(21375, 626000);
		
		CHANCES_MANE.put(21350, 500000);
		CHANCES_MANE.put(21353, 510000);
		CHANCES_MANE.put(21354, 522000);
		CHANCES_MANE.put(21355, 519000);
		CHANCES_MANE.put(21357, 529000);
		CHANCES_MANE.put(21358, 529000);
		CHANCES_MANE.put(21360, 539000);
		CHANCES_MANE.put(21362, 548000);
		CHANCES_MANE.put(21364, 558000);
		CHANCES_MANE.put(21365, 568000);
		CHANCES_MANE.put(21366, 568000);
		CHANCES_MANE.put(21368, 568000);
		CHANCES_MANE.put(21369, 664000);
		CHANCES_MANE.put(21371, 713000);
		CHANCES_MANE.put(21373, 738000);
		
		setItemsIds(VARKA_BADGE_SOLDIER, VARKA_BADGE_OFFICER, VARKA_BADGE_CAPTAIN);
		
		addQuestStart(31371); // Wahkan
		addTalkId(31371);
		
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
		
		if (event.equalsIgnoreCase("31371-03a.htm"))
		{
			if (player.isAlliedWithVarka())
				htmltext = "31371-02a.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				playSound(player, SOUND_ACCEPT);
				for (int i = KETRA_ALLIANCE_1; i <= KETRA_ALLIANCE_5; i++)
				{
					if (player.getInventory().hasItem(i))
					{
						st.setCond(i - 7209);
						player.setAllianceWithVarkaKetra(i - 7210);
						return "31371-0" + (i - 7207) + ".htm";
					}
				}
				st.setCond(1);
			}
		}
		// Stage 1
		else if (event.equalsIgnoreCase("31371-10-1.htm"))
		{
			if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) >= 100)
			{
				st.setCond(2);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, VARKA_BADGE_SOLDIER, -1);
				giveItems(player, KETRA_ALLIANCE_1, 1);
				player.setAllianceWithVarkaKetra(1);
			}
			else
				htmltext = "31371-03b.htm";
		}
		// Stage 2
		else if (event.equalsIgnoreCase("31371-10-2.htm"))
		{
			if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) >= 200 && player.getInventory().getItemCount(VARKA_BADGE_OFFICER) >= 100)
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, VARKA_BADGE_SOLDIER, -1);
				takeItems(player, VARKA_BADGE_OFFICER, -1);
				takeItems(player, KETRA_ALLIANCE_1, -1);
				giveItems(player, KETRA_ALLIANCE_2, 1);
				player.setAllianceWithVarkaKetra(2);
			}
			else
				htmltext = "31371-12.htm";
		}
		// Stage 3
		else if (event.equalsIgnoreCase("31371-10-3.htm"))
		{
			if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) >= 300 && player.getInventory().getItemCount(VARKA_BADGE_OFFICER) >= 200 && player.getInventory().getItemCount(VARKA_BADGE_CAPTAIN) >= 100)
			{
				st.setCond(4);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, VARKA_BADGE_SOLDIER, -1);
				takeItems(player, VARKA_BADGE_OFFICER, -1);
				takeItems(player, VARKA_BADGE_CAPTAIN, -1);
				takeItems(player, KETRA_ALLIANCE_2, -1);
				giveItems(player, KETRA_ALLIANCE_3, 1);
				player.setAllianceWithVarkaKetra(3);
			}
			else
				htmltext = "31371-15.htm";
		}
		// Stage 4
		else if (event.equalsIgnoreCase("31371-10-4.htm"))
		{
			if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) >= 300 && player.getInventory().getItemCount(VARKA_BADGE_OFFICER) >= 300 && player.getInventory().getItemCount(VARKA_BADGE_CAPTAIN) >= 200 && player.getInventory().getItemCount(TOTEM_OF_VALOR) >= 1)
			{
				st.setCond(5);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, VARKA_BADGE_SOLDIER, -1);
				takeItems(player, VARKA_BADGE_OFFICER, -1);
				takeItems(player, VARKA_BADGE_CAPTAIN, -1);
				takeItems(player, TOTEM_OF_VALOR, -1);
				takeItems(player, KETRA_ALLIANCE_3, -1);
				giveItems(player, KETRA_ALLIANCE_4, 1);
				player.setAllianceWithVarkaKetra(4);
			}
			else
				htmltext = "31371-21.htm";
		}
		// Leave quest
		else if (event.equalsIgnoreCase("31371-20.htm"))
		{
			takeItems(player, KETRA_ALLIANCE_1, -1);
			takeItems(player, KETRA_ALLIANCE_2, -1);
			takeItems(player, KETRA_ALLIANCE_3, -1);
			takeItems(player, KETRA_ALLIANCE_4, -1);
			takeItems(player, KETRA_ALLIANCE_5, -1);
			takeItems(player, TOTEM_OF_VALOR, -1);
			takeItems(player, TOTEM_OF_WISDOM, -1);
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
					htmltext = "31371-01.htm";
				else
				{
					htmltext = "31371-02b.htm";
					st.exitQuest(true);
					player.setAllianceWithVarkaKetra(0);
				}
				break;
			
			case STARTED:
				final int cond = st.getCond();
				if (cond == 1)
				{
					if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) < 100)
						htmltext = "31371-03b.htm";
					else
						htmltext = "31371-09.htm";
				}
				else if (cond == 2)
				{
					if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) < 200 || player.getInventory().getItemCount(VARKA_BADGE_OFFICER) < 100)
						htmltext = "31371-12.htm";
					else
						htmltext = "31371-13.htm";
				}
				else if (cond == 3)
				{
					if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) < 300 || player.getInventory().getItemCount(VARKA_BADGE_OFFICER) < 200 || player.getInventory().getItemCount(VARKA_BADGE_CAPTAIN) < 100)
						htmltext = "31371-15.htm";
					else
						htmltext = "31371-16.htm";
				}
				else if (cond == 4)
				{
					if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) < 300 || player.getInventory().getItemCount(VARKA_BADGE_OFFICER) < 300 || player.getInventory().getItemCount(VARKA_BADGE_CAPTAIN) < 200 || !player.getInventory().hasItem(TOTEM_OF_VALOR))
						htmltext = "31371-21.htm";
					else
						htmltext = "31371-22.htm";
				}
				else if (cond == 5)
				{
					if (player.getInventory().getItemCount(VARKA_BADGE_SOLDIER) < 400 || player.getInventory().getItemCount(VARKA_BADGE_OFFICER) < 400 || player.getInventory().getItemCount(VARKA_BADGE_CAPTAIN) < 200 || !player.getInventory().hasItem(TOTEM_OF_WISDOM))
						htmltext = "31371-17.htm";
					else
					{
						htmltext = "31371-10-5.htm";
						st.setCond(6);
						playSound(player, SOUND_MIDDLE);
						takeItems(player, VARKA_BADGE_SOLDIER, 400);
						takeItems(player, VARKA_BADGE_OFFICER, 400);
						takeItems(player, VARKA_BADGE_CAPTAIN, 200);
						takeItems(player, TOTEM_OF_WISDOM, -1);
						takeItems(player, KETRA_ALLIANCE_4, -1);
						giveItems(player, KETRA_ALLIANCE_5, 1);
						player.setAllianceWithVarkaKetra(5);
					}
				}
				else if (cond == 6)
					htmltext = "31371-08.htm";
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
		
		// Support for Q606.
		final QuestState st2 = player.getQuestList().getQuestState(qn2);
		if (st2 != null && Rnd.nextBoolean() && CHANCES_MANE.containsKey(npcId))
		{
			dropItems(player, VARKA_MANE, 1, 0, CHANCES_MANE.get(npcId));
			return;
		}
		
		final int cond = st.getCond();
		if (cond == 6)
			return;
		
		switch (npcId)
		{
			case 21350, 21351, 21353, 21354, 21355:
				if (cond == 1)
					dropItems(player, VARKA_BADGE_SOLDIER, 1, 100, CHANCES.get(npcId));
				else if (cond == 2)
					dropItems(player, VARKA_BADGE_SOLDIER, 1, 200, CHANCES.get(npcId));
				else if (cond == 3 || cond == 4)
					dropItems(player, VARKA_BADGE_SOLDIER, 1, 300, CHANCES.get(npcId));
				else if (cond == 5)
					dropItems(player, VARKA_BADGE_SOLDIER, 1, 400, CHANCES.get(npcId));
				break;
			
			case 21357, 21358, 21360, 21361, 21362, 21364, 21369, 21370:
				if (cond == 2)
					dropItems(player, VARKA_BADGE_OFFICER, 1, 100, CHANCES.get(npcId));
				else if (cond == 3)
					dropItems(player, VARKA_BADGE_OFFICER, 1, 200, CHANCES.get(npcId));
				else if (cond == 4)
					dropItems(player, VARKA_BADGE_OFFICER, 1, 300, CHANCES.get(npcId));
				else if (cond == 5)
					dropItems(player, VARKA_BADGE_OFFICER, 1, 400, CHANCES.get(npcId));
				break;
			
			case 21365, 21366, 21368, 21371, 21372, 21373, 21374, 21375:
				if (cond == 3)
					dropItems(player, VARKA_BADGE_CAPTAIN, 1, 100, CHANCES.get(npcId));
				else if (cond == 4 || cond == 5)
					dropItems(player, VARKA_BADGE_CAPTAIN, 1, 200, CHANCES.get(npcId));
				break;
		}
	}
}