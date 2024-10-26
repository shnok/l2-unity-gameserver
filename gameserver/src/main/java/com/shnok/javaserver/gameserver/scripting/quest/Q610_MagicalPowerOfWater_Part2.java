package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q610_MagicalPowerOfWater_Part2 extends Quest
{
	private static final String QUEST_NAME = "Q610_MagicalPowerOfWater_Part2";
	
	// Monster
	private static final int SOUL_OF_WATER_ASHUTAR = 25316;
	
	// NPCs
	private static final int ASEFA = 31372;
	private static final int VARKAS_HOLY_ALTAR = 31560;
	
	// Items
	private static final int GREEN_TOTEM = 7238;
	private static final int ICE_HEART_OF_ASHUTAR = 7239;
	
	// Instances
	private Npc _npc;
	private Npc _raid;
	
	public Q610_MagicalPowerOfWater_Part2()
	{
		super(610, "Magical Power of Water - Part 2");
		
		setItemsIds(ICE_HEART_OF_ASHUTAR);
		
		addQuestStart(ASEFA);
		addTalkId(ASEFA, VARKAS_HOLY_ALTAR);
		
		addDecayed(SOUL_OF_WATER_ASHUTAR);
		addMyDying(SOUL_OF_WATER_ASHUTAR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Asefa
		if (event.equalsIgnoreCase("31372-04.htm"))
		{
			if (player.getInventory().hasItem(GREEN_TOTEM))
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
				htmltext = "31372-02.htm";
		}
		else if (event.equalsIgnoreCase("31372-07.htm"))
		{
			if (player.getInventory().hasItem(ICE_HEART_OF_ASHUTAR))
			{
				takeItems(player, ICE_HEART_OF_ASHUTAR, 1);
				rewardExpAndSp(player, 10000, 0);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "31372-08.htm";
		}
		// Varka's Holy Altar
		else if (event.equalsIgnoreCase("31560-02.htm"))
		{
			if (player.getInventory().hasItem(GREEN_TOTEM))
			{
				if (_raid == null)
				{
					// Spawn raid.
					_raid = addSpawn(SOUL_OF_WATER_ASHUTAR, 104825, -36926, -1136, Rnd.get(65536), false, 1200000, false);
					_raid.broadcastNpcSay(NpcStringId.ID_61050);
					
					// Despawn npc.
					_npc = npc;
					_npc.deleteMe();
					
					st.setCond(2);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, GREEN_TOTEM, 1);
				}
				else
					htmltext = "31560-04.htm";
			}
			else
				htmltext = "31560-03.htm";
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
				if (!player.getInventory().hasItem(GREEN_TOTEM))
					htmltext = "31372-02.htm";
				else if (player.getStatus().getLevel() < 75 && player.getAllianceWithVarkaKetra() < 2)
					htmltext = "31372-03.htm";
				else
					htmltext = "31372-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ASEFA:
						htmltext = (cond < 3) ? "31372-05.htm" : "31372-06.htm";
						break;
					
					case VARKAS_HOLY_ALTAR:
						if (cond == 1)
							htmltext = "31560-01.htm";
						else if (cond == 2)
							htmltext = "31560-05.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		if (npc == _raid)
		{
			// Raid is not dead, decay it.
			if (!_raid.isDead())
			{
				// Respawn npc (it cancels respawn task).
				_npc.getSpawn().doRespawn(_npc);
				
				_raid.broadcastNpcSay(NpcStringId.ID_61051);
			}
			
			_npc = null;
			_raid = null;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMember(player, npc, 2);
		if (st == null)
			return;
		
		st.setCond(3);
		playSound(st.getPlayer(), SOUND_MIDDLE);
		giveItems(st.getPlayer(), ICE_HEART_OF_ASHUTAR, 1);
	}
}