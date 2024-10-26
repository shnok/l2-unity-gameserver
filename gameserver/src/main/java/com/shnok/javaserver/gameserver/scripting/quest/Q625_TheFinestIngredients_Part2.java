package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q625_TheFinestIngredients_Part2 extends Quest
{
	private static final String QUEST_NAME = "Q625_TheFinestIngredients_Part2";
	
	// Monster
	private static final int ICICLE_EMPEROR_BUMBALUMP = 25296;
	
	// NPCs
	private static final int JEREMY = 31521;
	private static final int YETI_TABLE = 31542;
	
	// Items
	private static final int SOY_SAUCE_JAR = 7205;
	private static final int FOOD_FOR_BUMBALUMP = 7209;
	private static final int SPECIAL_YETI_MEAT = 7210;
	private static final int[] REWARDS =
	{
		4589,
		4590,
		4591,
		4592,
		4593,
		4594
	};
	
	// Instances
	private Npc _npc;
	private Npc _raid;
	
	public Q625_TheFinestIngredients_Part2()
	{
		super(625, "The Finest Ingredients - Part 2");
		
		setItemsIds(FOOD_FOR_BUMBALUMP, SPECIAL_YETI_MEAT);
		
		addQuestStart(JEREMY);
		addTalkId(JEREMY, YETI_TABLE);
		
		addDecayed(ICICLE_EMPEROR_BUMBALUMP);
		addMyDying(ICICLE_EMPEROR_BUMBALUMP);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Jeremy
		if (event.equalsIgnoreCase("31521-03.htm"))
		{
			if (player.getInventory().hasItem(SOY_SAUCE_JAR))
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
				takeItems(player, SOY_SAUCE_JAR, 1);
				giveItems(player, FOOD_FOR_BUMBALUMP, 1);
			}
			else
				htmltext = "31521-04.htm";
		}
		else if (event.equalsIgnoreCase("31521-08.htm"))
		{
			if (player.getInventory().hasItem(SPECIAL_YETI_MEAT))
			{
				takeItems(player, SPECIAL_YETI_MEAT, 1);
				rewardItems(player, Rnd.get(REWARDS), 5);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = "31521-09.htm";
		}
		// Yeti's Table
		else if (event.equalsIgnoreCase("31542-02.htm"))
		{
			if (player.getInventory().hasItem(FOOD_FOR_BUMBALUMP))
			{
				if (_raid == null)
				{
					// Spawn raid.
					_raid = addSpawn(ICICLE_EMPEROR_BUMBALUMP, 158240, -121536, -2222, Rnd.get(65536), false, 1200000, false);
					_raid.broadcastNpcSay(NpcStringId.ID_62503);
					
					// Despawn npc.
					_npc = npc;
					_npc.deleteMe();
					
					st.setCond(2);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, FOOD_FOR_BUMBALUMP, 1);
				}
				else
					htmltext = "31542-04.htm";
			}
			else
				htmltext = "31542-03.htm";
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
				htmltext = (player.getStatus().getLevel() < 73) ? "31521-02.htm" : "31521-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case JEREMY:
						if (cond == 1)
							htmltext = "31521-05.htm";
						else if (cond == 2)
							htmltext = "31521-06.htm";
						else
							htmltext = "31521-07.htm";
						break;
					
					case YETI_TABLE:
						if (cond == 1)
							htmltext = "31542-01.htm";
						else if (cond == 2)
							htmltext = "31542-05.htm";
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
				
				_raid.broadcastNpcSay(NpcStringId.ID_62504);
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
		giveItems(st.getPlayer(), SPECIAL_YETI_MEAT, 1);
	}
}