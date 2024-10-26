package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q604_DaimonTheWhiteEyed_Part2 extends Quest
{
	private static final String QUEST_NAME = "Q604_DaimonTheWhiteEyed_Part2";
	
	// Monster
	private static final int DAIMON_THE_WHITE_EYED = 25290;
	
	// NPCs
	private static final int EYE_OF_ARGOS = 31683;
	private static final int DAIMON_ALTAR = 31541;
	
	// Items
	private static final int UNFINISHED_SUMMON_CRYSTAL = 7192;
	private static final int SUMMON_CRYSTAL = 7193;
	private static final int ESSENCE_OF_DAIMON = 7194;
	private static final int[] REWARDS =
	{
		4595,
		4596,
		4597,
		4598,
		4599,
		4600
	};
	
	// Instances
	private Npc _npc;
	private Npc _raid;
	
	public Q604_DaimonTheWhiteEyed_Part2()
	{
		super(604, "Daimon The White-Eyed - Part 2");
		
		setItemsIds(SUMMON_CRYSTAL, ESSENCE_OF_DAIMON);
		
		addQuestStart(EYE_OF_ARGOS);
		addTalkId(EYE_OF_ARGOS, DAIMON_ALTAR);
		
		addDecayed(DAIMON_THE_WHITE_EYED);
		addMyDying(DAIMON_THE_WHITE_EYED);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Eye of Argos
		if (event.equalsIgnoreCase("31683-03.htm"))
		{
			if (player.getInventory().hasItem(UNFINISHED_SUMMON_CRYSTAL))
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
				takeItems(player, UNFINISHED_SUMMON_CRYSTAL, 1);
				giveItems(player, SUMMON_CRYSTAL, 1);
			}
			else
				htmltext = "31683-04.htm";
		}
		else if (event.equalsIgnoreCase("31683-08.htm"))
		{
			if (player.getInventory().hasItem(ESSENCE_OF_DAIMON))
			{
				takeItems(player, ESSENCE_OF_DAIMON, 1);
				rewardItems(player, Rnd.get(REWARDS), 5);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "31683-09.htm";
		}
		// Diamon's Altar
		else if (event.equalsIgnoreCase("31541-02.htm"))
		{
			if (player.getInventory().hasItem(SUMMON_CRYSTAL))
			{
				if (_raid == null)
				{
					// Spawn raid.
					_raid = addSpawn(DAIMON_THE_WHITE_EYED, 186320, -43904, -3175, Rnd.get(65536), false, 1200000, false);
					_raid.broadcastNpcSay(NpcStringId.ID_60403);
					
					// Despawn npc.
					_npc = npc;
					_npc.deleteMe();
					
					st.setCond(2);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, SUMMON_CRYSTAL, 1);
				}
				else
					htmltext = "31541-04.htm";
			}
			else
				htmltext = "31541-03.htm";
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
				if (player.getStatus().getLevel() < 73)
				{
					htmltext = "31683-02.htm";
					st.exitQuest(true);
				}
				else
					htmltext = "31683-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case EYE_OF_ARGOS:
						if (cond == 1)
							htmltext = "31683-05.htm";
						else if (cond == 2)
							htmltext = "31683-06.htm";
						else
							htmltext = "31683-07.htm";
						break;
					
					case DAIMON_ALTAR:
						if (cond == 1)
							htmltext = "31541-01.htm";
						else if (cond == 2)
							htmltext = "31541-05.htm";
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
				
				_raid.broadcastNpcSay(NpcStringId.ID_60404);
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
		giveItems(st.getPlayer(), ESSENCE_OF_DAIMON, 1);
	}
}