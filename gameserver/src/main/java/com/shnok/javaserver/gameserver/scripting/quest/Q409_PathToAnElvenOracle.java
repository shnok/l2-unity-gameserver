package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Q409_PathToAnElvenOracle extends Quest
{
	private static final String QUEST_NAME = "Q409_PathToAnElvenOracle";
	
	// Items
	private static final int CRYSTAL_MEDALLION = 1231;
	private static final int SWINDLER_MONEY = 1232;
	private static final int ALLANA_DIARY = 1233;
	private static final int LIZARD_CAPTAIN_ORDER = 1234;
	private static final int LEAF_OF_ORACLE = 1235;
	private static final int HALF_OF_DIARY = 1236;
	private static final int TAMIL_NECKLACE = 1275;
	
	// NPCs
	private static final int MANUEL = 30293;
	private static final int ALLANA = 30424;
	private static final int PERRIN = 30428;
	
	// Quest Monsters
	private static final int LIZARDMAN_WARRIOR = 27032;
	private static final int LIZARDMAN_SCOUT = 27033;
	private static final int LIZARDMAN = 27034;
	private static final int TAMIL = 27035;
	
	private final Map<Npc, Integer> _spawns = new ConcurrentHashMap<>();
	
	public Q409_PathToAnElvenOracle()
	{
		super(409, "Path to an Elven Oracle");
		
		setItemsIds(CRYSTAL_MEDALLION, SWINDLER_MONEY, ALLANA_DIARY, LIZARD_CAPTAIN_ORDER, HALF_OF_DIARY, TAMIL_NECKLACE);
		
		addQuestStart(MANUEL);
		addTalkId(MANUEL, ALLANA, PERRIN);
		
		addAttacked(LIZARDMAN_WARRIOR, LIZARDMAN_SCOUT, LIZARDMAN, TAMIL);
		addDecayed(LIZARDMAN_WARRIOR, LIZARDMAN_SCOUT, LIZARDMAN, TAMIL);
		addMyDying(LIZARDMAN_WARRIOR, LIZARDMAN_SCOUT, LIZARDMAN, TAMIL);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Manuel
		if (event.equalsIgnoreCase("30293-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, CRYSTAL_MEDALLION, 1);
		}
		// Allana
		else if (event.equalsIgnoreCase("spawn_lizards"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			
			final int oid = player.getObjectId();
			_spawns.put(addSpawn(LIZARDMAN_WARRIOR, npc, true, 0, false), oid);
			_spawns.put(addSpawn(LIZARDMAN_SCOUT, npc, true, 0, false), oid);
			_spawns.put(addSpawn(LIZARDMAN, npc, true, 0, false), oid);
			return null;
		}
		// Perrin
		else if (event.equalsIgnoreCase("30428-06.htm"))
		{
			_spawns.put(addSpawn(TAMIL, npc, true, 0, true), player.getObjectId());
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
				if (player.getClassId() != ClassId.ELVEN_MYSTIC)
					htmltext = (player.getClassId() == ClassId.ELVEN_ORACLE) ? "30293-02a.htm" : "30293-02.htm";
				else if (player.getStatus().getLevel() < 19)
					htmltext = "30293-03.htm";
				else if (player.getInventory().hasItem(LEAF_OF_ORACLE))
					htmltext = "30293-04.htm";
				else
					htmltext = "30293-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case MANUEL:
						if (cond == 1)
							htmltext = "30293-06.htm";
						else if (cond == 2 || cond == 3)
							htmltext = "30293-09.htm";
						else if (cond > 3 && cond < 7)
							htmltext = "30293-07.htm";
						else if (cond == 7)
						{
							htmltext = "30293-08.htm";
							takeItems(player, ALLANA_DIARY, 1);
							takeItems(player, CRYSTAL_MEDALLION, 1);
							takeItems(player, LIZARD_CAPTAIN_ORDER, 1);
							takeItems(player, SWINDLER_MONEY, 1);
							giveItems(player, LEAF_OF_ORACLE, 1);
							rewardExpAndSp(player, 3200, 1130);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case ALLANA:
						if (cond == 1 || cond == 2)
						{
							if (_spawns.containsValue(player.getObjectId()))
								return null;
							
							htmltext = "30424-01.htm";
						}
						else if (cond == 3)
						{
							htmltext = "30424-02.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, HALF_OF_DIARY, 1);
						}
						else if (cond == 4)
							htmltext = "30424-03.htm";
						else if (cond == 5)
							htmltext = "30424-06.htm";
						else if (cond == 6)
						{
							htmltext = "30424-04.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, HALF_OF_DIARY, -1);
							giveItems(player, ALLANA_DIARY, 1);
						}
						else if (cond == 7)
							htmltext = "30424-05.htm";
						break;
					
					case PERRIN:
						if (cond == 4)
						{
							if (_spawns.containsValue(player.getObjectId()))
								htmltext = "30428-06.htm";
							else
								htmltext = "30428-01.htm";
						}
						else if (cond == 5)
						{
							htmltext = "30428-04.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, TAMIL_NECKLACE, -1);
							giveItems(player, SWINDLER_MONEY, 1);
						}
						else if (cond > 5)
							htmltext = "30428-05.htm";
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final int condition = npc.getScriptValue();
		if (condition < 0)
			return;
		
		// Attacker must be player with started quest.
		final Player player = attacker.getActingPlayer();
		if (player == null || checkPlayerState(player, npc, QuestStatus.STARTED) == null)
			return;
		
		if (condition == 0)
		{
			// First valid attack. Save player object ID.
			switch (npc.getNpcId())
			{
				case LIZARDMAN:
					npc.broadcastNpcSay(NpcStringId.ID_40912);
					break;
				case LIZARDMAN_SCOUT:
					npc.broadcastNpcSay(NpcStringId.ID_40911);
					break;
				case LIZARDMAN_WARRIOR:
					npc.broadcastNpcSay(NpcStringId.ID_40909);
					break;
				case TAMIL:
					npc.broadcastNpcSay(NpcStringId.ID_40913);
					break;
			}
			
			npc.setScriptValue(player.getObjectId());
		}
		else
		{
			// Repeated valid attack. Mark invalid attack condition, if another player also attacks the monster.
			if (condition != player.getObjectId())
				npc.setScriptValue(-1);
		}
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		_spawns.remove(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null || npc.getScriptValue() < 0)
			return;
		
		switch (npc.getNpcId())
		{
			case LIZARDMAN_WARRIOR, LIZARDMAN_SCOUT, LIZARDMAN:
				if (st.getCond() == 2)
				{
					npc.broadcastNpcSay(NpcStringId.ID_40910);
					
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, LIZARD_CAPTAIN_ORDER, 1);
				}
				break;
			
			case TAMIL:
				if (st.getCond() == 4)
				{
					st.setCond(5);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, TAMIL_NECKLACE, 1);
				}
				break;
		}
	}
}