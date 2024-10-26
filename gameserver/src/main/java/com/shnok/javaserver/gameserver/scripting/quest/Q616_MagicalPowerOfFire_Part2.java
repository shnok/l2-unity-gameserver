package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q616_MagicalPowerOfFire_Part2 extends Quest
{
	private static final String QUEST_NAME = "Q616_MagicalPowerOfFire_Part2";
	
	// Monster
	private static final int SOUL_OF_FIRE_NASTRON = 25306;
	
	// NPCs
	private static final int UDAN_MARDUI = 31379;
	private static final int KETRAS_HOLY_ALTAR = 31558;
	
	// Items
	private static final int RED_TOTEM = 7243;
	private static final int FIRE_HEART_OF_NASTRON = 7244;
	
	// Instances
	private Npc _npc;
	private Npc _raid;
	
	public Q616_MagicalPowerOfFire_Part2()
	{
		super(616, "Magical Power of Fire - Part 2");
		
		setItemsIds(FIRE_HEART_OF_NASTRON);
		
		addQuestStart(UDAN_MARDUI);
		addTalkId(UDAN_MARDUI, KETRAS_HOLY_ALTAR);
		
		addDecayed(SOUL_OF_FIRE_NASTRON);
		addMyDying(SOUL_OF_FIRE_NASTRON);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Udan Mardui
		if (event.equalsIgnoreCase("31379-04.htm"))
		{
			if (player.getInventory().hasItem(RED_TOTEM))
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
			}
			else
				htmltext = "31379-02.htm";
		}
		else if (event.equalsIgnoreCase("31379-08.htm"))
		{
			if (player.getInventory().hasItem(FIRE_HEART_OF_NASTRON))
			{
				takeItems(player, FIRE_HEART_OF_NASTRON, 1);
				rewardExpAndSp(player, 10000, 0);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "31379-09.htm";
		}
		// Ketra's Holy Altar
		else if (event.equalsIgnoreCase("31558-02.htm"))
		{
			if (player.getInventory().hasItem(RED_TOTEM))
			{
				if (_raid == null)
				{
					// Spawn raid.
					_raid = addSpawn(SOUL_OF_FIRE_NASTRON, 142528, -82528, -6496, Rnd.get(65536), false, 1200000, false);
					_raid.broadcastNpcSay(NpcStringId.ID_61650);
					
					// Despawn npc.
					_npc = npc;
					_npc.deleteMe();
					
					st.setCond(2);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, RED_TOTEM, 1);
				}
				else
					htmltext = "31558-04.htm";
			}
			else
				htmltext = "31558-03.htm";
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
				if (!player.getInventory().hasItem(RED_TOTEM))
					htmltext = "31379-02.htm";
				else if (player.getStatus().getLevel() < 75 && player.getAllianceWithVarkaKetra() > -2)
					htmltext = "31379-03.htm";
				else
					htmltext = "31379-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case UDAN_MARDUI:
						if (cond == 1)
							htmltext = "31379-05.htm";
						else if (cond == 2)
							htmltext = "31379-06.htm";
						else
							htmltext = "31379-07.htm";
						break;
					
					case KETRAS_HOLY_ALTAR:
						if (cond == 1)
							htmltext = "31558-01.htm";
						else if (cond == 2)
							htmltext = "31558-05.htm";
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
				
				_raid.broadcastNpcSay(NpcStringId.ID_61651);
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
		giveItems(st.getPlayer(), FIRE_HEART_OF_NASTRON, 1);
	}
}