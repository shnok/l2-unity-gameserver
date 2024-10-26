package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q171_ActsOfEvil extends Quest
{
	private static final String QUEST_NAME = "Q171_ActsOfEvil";
	
	// Items
	private static final int BLADE_MOLD = 4239;
	private static final int TYRA_BILL = 4240;
	private static final int RANGER_REPORT_1 = 4241;
	private static final int RANGER_REPORT_2 = 4242;
	private static final int RANGER_REPORT_3 = 4243;
	private static final int RANGER_REPORT_4 = 4244;
	private static final int WEAPON_TRADE_CONTRACT = 4245;
	private static final int ATTACK_DIRECTIVES = 4246;
	private static final int CERTIFICATE = 4247;
	private static final int CARGO_BOX = 4248;
	private static final int OL_MAHUM_HEAD = 4249;
	
	// NPCs
	private static final int ALVAH = 30381;
	private static final int ARODIN = 30207;
	private static final int TYRA = 30420;
	private static final int ROLENTO = 30437;
	private static final int NETI = 30425;
	private static final int BURAI = 30617;
	private static final int OL_MAHUM_SUPPORT_TROOP = 27190;
	
	// Turek Orcs drop chances
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(4);
	
	public Q171_ActsOfEvil()
	{
		super(171, "Acts of Evil");
		
		CHANCES.put(20496, 530000);
		CHANCES.put(20497, 550000);
		CHANCES.put(20498, 510000);
		CHANCES.put(20499, 500000);
		
		setItemsIds(BLADE_MOLD, TYRA_BILL, RANGER_REPORT_1, RANGER_REPORT_2, RANGER_REPORT_3, RANGER_REPORT_4, WEAPON_TRADE_CONTRACT, ATTACK_DIRECTIVES, CERTIFICATE, CARGO_BOX, OL_MAHUM_HEAD);
		
		addQuestStart(ALVAH);
		addTalkId(ALVAH, ARODIN, TYRA, ROLENTO, NETI, BURAI);
		
		addDecayed(OL_MAHUM_SUPPORT_TROOP);
		addMyDying(20496, 20497, 20498, 20499, 20062, 20064, 20066, 20438);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30381-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30207-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30381-04.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30381-07.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, WEAPON_TRADE_CONTRACT, 1);
		}
		else if (event.equalsIgnoreCase("30437-03.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, CARGO_BOX, 1);
			giveItems(player, CERTIFICATE, 1);
		}
		else if (event.equalsIgnoreCase("30617-04.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ATTACK_DIRECTIVES, 1);
			takeItems(player, CARGO_BOX, 1);
			takeItems(player, CERTIFICATE, 1);
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
				htmltext = (player.getStatus().getLevel() < 27) ? "30381-01a.htm" : "30381-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ALVAH:
						if (cond < 4)
							htmltext = "30381-02a.htm";
						else if (cond == 4)
							htmltext = "30381-03.htm";
						else if (cond == 5)
						{
							if (player.getInventory().hasItems(RANGER_REPORT_1, RANGER_REPORT_2, RANGER_REPORT_3, RANGER_REPORT_4))
							{
								htmltext = "30381-05.htm";
								st.setCond(6);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, RANGER_REPORT_1, 1);
								takeItems(player, RANGER_REPORT_2, 1);
								takeItems(player, RANGER_REPORT_3, 1);
								takeItems(player, RANGER_REPORT_4, 1);
							}
							else
								htmltext = "30381-04a.htm";
						}
						else if (cond == 6)
						{
							if (player.getInventory().hasItems(WEAPON_TRADE_CONTRACT, ATTACK_DIRECTIVES))
								htmltext = "30381-06.htm";
							else
								htmltext = "30381-05a.htm";
						}
						else if (cond > 6 && cond < 11)
							htmltext = "30381-07a.htm";
						else if (cond == 11)
						{
							htmltext = "30381-08.htm";
							rewardItems(player, 57, 90000);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ARODIN:
						if (cond == 1)
							htmltext = "30207-01.htm";
						else if (cond == 2)
							htmltext = "30207-01a.htm";
						else if (cond == 3)
						{
							if (player.getInventory().hasItem(TYRA_BILL))
							{
								htmltext = "30207-03.htm";
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, TYRA_BILL, 1);
							}
							else
								htmltext = "30207-01a.htm";
						}
						else if (cond > 3)
							htmltext = "30207-03a.htm";
						break;
					
					case TYRA:
						if (cond == 2)
						{
							if (player.getInventory().getItemCount(BLADE_MOLD) >= 20)
							{
								htmltext = "30420-01.htm";
								st.setCond(3);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, BLADE_MOLD, -1);
								giveItems(player, TYRA_BILL, 1);
							}
							else
								htmltext = "30420-01b.htm";
						}
						else if (cond == 3)
							htmltext = "30420-01a.htm";
						else if (cond > 3)
							htmltext = "30420-02.htm";
						break;
					
					case NETI:
						if (cond == 7)
						{
							htmltext = "30425-01.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond > 7)
							htmltext = "30425-02.htm";
						break;
					
					case ROLENTO:
						if (cond == 8)
							htmltext = "30437-01.htm";
						else if (cond > 8)
							htmltext = "30437-03a.htm";
						break;
					
					case BURAI:
						if (cond == 9 && player.getInventory().hasItems(CERTIFICATE, CARGO_BOX, ATTACK_DIRECTIVES))
							htmltext = "30617-01.htm";
						else if (cond == 10)
						{
							if (player.getInventory().getItemCount(OL_MAHUM_HEAD) >= 30)
							{
								htmltext = "30617-05.htm";
								st.setCond(11);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, OL_MAHUM_HEAD, -1);
								rewardItems(player, 57, 8000);
							}
							else
								htmltext = "30617-04a.htm";
						}
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
	public void onDecayed(Npc npc)
	{
		if (!npc.isDead())
			npc.broadcastNpcSay(NpcStringId.ID_17151);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int npcId = npc.getNpcId();
		
		switch (npcId)
		{
			case 20496, 20497, 20498, 20499:
				if (st.getCond() == 2 && !dropItems(player, BLADE_MOLD, 1, 20, CHANCES.get(npcId)))
				{
					final int count = player.getInventory().getItemCount(BLADE_MOLD);
					if (count == 5 || (count >= 10 && Rnd.get(100) < 25))
					{
						Npc troop = addSpawn(OL_MAHUM_SUPPORT_TROOP, npc, false, 200000, true);
						troop.getAI().addAttackDesire(player, 2000);
					}
				}
				break;
			
			case 20062, 20064:
				if (st.getCond() == 5)
				{
					if (!player.getInventory().hasItem(RANGER_REPORT_1))
					{
						giveItems(player, RANGER_REPORT_1, 1);
						playSound(player, SOUND_ITEMGET);
					}
					else if (Rnd.get(100) < 20)
					{
						if (!player.getInventory().hasItem(RANGER_REPORT_2))
						{
							giveItems(player, RANGER_REPORT_2, 1);
							playSound(player, SOUND_ITEMGET);
						}
						else if (!player.getInventory().hasItem(RANGER_REPORT_3))
						{
							giveItems(player, RANGER_REPORT_3, 1);
							playSound(player, SOUND_ITEMGET);
						}
						else if (!player.getInventory().hasItem(RANGER_REPORT_4))
						{
							giveItems(player, RANGER_REPORT_4, 1);
							playSound(player, SOUND_ITEMGET);
						}
					}
				}
				break;
			
			case 20438:
				if (st.getCond() == 6 && Rnd.get(100) < 10 && !player.getInventory().hasItems(WEAPON_TRADE_CONTRACT, ATTACK_DIRECTIVES))
				{
					playSound(player, SOUND_ITEMGET);
					giveItems(player, WEAPON_TRADE_CONTRACT, 1);
					giveItems(player, ATTACK_DIRECTIVES, 1);
				}
				break;
			
			case 20066:
				if (st.getCond() == 10)
					dropItems(player, OL_MAHUM_HEAD, 1, 30, 500000);
				break;
		}
	}
}