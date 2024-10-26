package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * The script uses internal iCond variable, used because cond isn't developed on that quest (only 3 states):
 * <ul>
 * <li>1-3: Initial Mimyu behavior.</li>
 * <li>4-63: Leave acquisition (tree flags: 4, 8, 16, 32 = 60 overall).</li>
 * <li>100: Mimyu acknowledge conditions are fulfilled.</li>
 * </ul>
 */
public class Q421_LittleWingsBigAdventure extends Quest
{
	private static final String QUEST_NAME = "Q421_LittleWingsBigAdventure";
	
	// Item
	private static final int DRAGONFLUTE_OF_WIND = 3500;
	private static final int DRAGONFLUTE_OF_STAR = 3501;
	private static final int DRAGONFLUTE_OF_TWILIGHT = 3502;
	private static final int FAIRY_LEAF = 4325;
	
	// NPCs
	private static final int CRONOS = 30610;
	private static final int MIMYU = 30747;
	
	// Monsters
	private static final int FAIRY_TREE_OF_WIND = 27185;
	private static final int FAIRY_TREE_OF_STAR = 27186;
	private static final int FAIRY_TREE_OF_TWILIGHT = 27187;
	private static final int FAIRY_TREE_OF_ABYSS = 27188;
	private static final int SOUL_OF_TREE_GUARDIAN = 27189;
	
	// Other
	private static final NpcStringId[] GUARDIAN_MESSAGES = new NpcStringId[]
	{
		NpcStringId.ID_42118,
		NpcStringId.ID_42119,
		NpcStringId.ID_42120,
	};
	
	private static final Map<Integer, TreeData> TREES_DATA = HashMap.newHashMap(4);
	
	public Q421_LittleWingsBigAdventure()
	{
		super(421, "Little Wing's Big Adventure");
		
		TREES_DATA.put(FAIRY_TREE_OF_WIND, new TreeData(4, 270, 3, NpcStringId.ID_42112, NpcStringId.ID_42114, NpcStringId.ID_42113)); // tree_q0421_1
		TREES_DATA.put(FAIRY_TREE_OF_STAR, new TreeData(8, 400, 2, NpcStringId.ID_42112, NpcStringId.ID_42114, NpcStringId.ID_42115)); // tree_q0421_2
		TREES_DATA.put(FAIRY_TREE_OF_TWILIGHT, new TreeData(16, 150, 2, NpcStringId.ID_42112, NpcStringId.ID_42114, NpcStringId.ID_42116)); // tree_q0421_3
		TREES_DATA.put(FAIRY_TREE_OF_ABYSS, new TreeData(32, 270, 2, NpcStringId.ID_42112, NpcStringId.ID_42114, NpcStringId.ID_42117)); // tree_q0421_4
		
		setItemsIds(FAIRY_LEAF);
		
		addQuestStart(CRONOS);
		addTalkId(CRONOS, MIMYU);
		
		addAttacked(FAIRY_TREE_OF_WIND, FAIRY_TREE_OF_STAR, FAIRY_TREE_OF_TWILIGHT, FAIRY_TREE_OF_ABYSS);
		addCreated(FAIRY_TREE_OF_WIND, FAIRY_TREE_OF_STAR, FAIRY_TREE_OF_TWILIGHT, FAIRY_TREE_OF_ABYSS, SOUL_OF_TREE_GUARDIAN);
		addMyDying(FAIRY_TREE_OF_WIND, FAIRY_TREE_OF_STAR, FAIRY_TREE_OF_TWILIGHT, FAIRY_TREE_OF_ABYSS);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Cronos
		if (event.equalsIgnoreCase("30610-06.htm"))
		{
			if (getDragonfluteCount(player) == 1)
			{
				// Find the level of the flute.
				for (int i = DRAGONFLUTE_OF_WIND; i <= DRAGONFLUTE_OF_TWILIGHT; i++)
				{
					final ItemInstance item = player.getInventory().getItemByItemId(i);
					if (item != null && item.getEnchantLevel() >= 55)
					{
						st.setState(QuestStatus.STARTED);
						st.setCond(1);
						st.set("iCond", 1);
						st.set("summonOid", item.getObjectId());
						playSound(player, SOUND_ACCEPT);
						return "30610-05.htm";
					}
				}
			}
		}
		// Mimyu
		else if (event.equalsIgnoreCase("30747-02.htm"))
		{
			final Summon summon = player.getSummon();
			if (summon != null)
				htmltext = (summon.getControlItemId() == st.getInteger("summonOid")) ? "30747-04.htm" : "30747-03.htm";
		}
		else if (event.equalsIgnoreCase("30747-05.htm"))
		{
			final Summon summon = player.getSummon();
			if (summon == null || summon.getControlItemId() != st.getInteger("summonOid"))
				htmltext = "30747-06.htm";
			else
			{
				st.setCond(2);
				st.set("iCond", 3);
				playSound(player, SOUND_MIDDLE);
				giveItems(player, FAIRY_LEAF, 4);
			}
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
				// Wrong level.
				if (player.getStatus().getLevel() < 45)
					htmltext = "30610-01.htm";
				// Got more than one flute, or none.
				else if (getDragonfluteCount(player) != 1)
					htmltext = "30610-02.htm";
				else
				{
					// Find the level of the hatchling.
					for (int i = DRAGONFLUTE_OF_WIND; i <= DRAGONFLUTE_OF_TWILIGHT; i++)
					{
						final ItemInstance item = player.getInventory().getItemByItemId(i);
						if (item != null && item.getEnchantLevel() >= 55)
							return "30610-04.htm";
					}
					
					// Invalid level.
					htmltext = "30610-03.htm";
				}
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case CRONOS:
						htmltext = "30610-07.htm";
						break;
					
					case MIMYU:
						final int id = st.getInteger("iCond");
						if (id == 1)
						{
							htmltext = "30747-01.htm";
							st.set("iCond", 2);
						}
						else if (id == 2)
						{
							final Summon summon = player.getSummon();
							if (summon == null)
								htmltext = "30747-02.htm";
							else if (summon.getControlItemId() != st.getInteger("summonOid"))
								htmltext = "30747-03.htm";
							else
								htmltext = "30747-04.htm";
						}
						// Explanation is done, leaves are already given.
						else if (id == 3)
							htmltext = "30747-07.htm";
						// Did at least one tree, but didn't manage to make them all.
						else if (id > 3 && id < 63)
							htmltext = "30747-11.htm";
						// Did all trees, no more leaves.
						else if (id == 63)
						{
							final Summon summon = player.getSummon();
							if (summon == null)
								return "30747-12.htm";
							
							if (summon.getControlItemId() != st.getInteger("summonOid"))
								return "30747-14.htm";
							
							htmltext = "30747-13.htm";
							st.set("iCond", 100);
						}
						// Spoke with the Fairy.
						else if (id == 100)
						{
							final Summon summon = player.getSummon();
							if (summon != null && summon.getControlItemId() == st.getInteger("summonOid"))
								return "30747-15.htm";
							
							if (getDragonfluteCount(player) > 1)
								return "30747-17.htm";
							
							for (int i = DRAGONFLUTE_OF_WIND; i <= DRAGONFLUTE_OF_TWILIGHT; i++)
							{
								final ItemInstance item = player.getInventory().getItemByItemId(i);
								if (item != null && item.getObjectId() == st.getInteger("summonOid"))
								{
									takeItems(player, i, 1);
									// TODO rebuild entirely pet system in order enchant is given a fuck. Supposed to give an item lvl XX for a flute level XX.
									giveItems(player, i + 922, 1, item.getEnchantLevel());
									playSound(player, SOUND_FINISH);
									st.exitQuest(true);
									return "30747-16.htm";
								}
							}
							
							// Curse if the registered objectId is the wrong one (switch flutes).
							htmltext = "30747-18.htm";
							npc.getAI().addCastDesire(player, 4167, 1, 1000000);
						}
						break;
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Player player = attacker.getActingPlayer();
		if (player == null)
			return;
		
		final QuestState st = checkPlayerCondition(player, npc, 2);
		if (st == null)
		{
			// Attacking player does not quest, check tree HP and try to curse attacker.
			if (npc.getStatus().getHpRatio() < 0.67 && Rnd.get(100) < 30)
				npc.getAI().addCastDesire(attacker, 4243, 1, 1000000);
		}
		else if (attacker instanceof Pet attackerPet)
		{
			// Attacker is pet of the player with quest, proceed.
			final int npcId = npc.getNpcId();
			final TreeData td = TREES_DATA.get(npcId);
			final int condition = st.getInteger("iCond");
			final int mask = td._mask;
			
			if ((mask & condition) == 0)
			{
				// Leaf not consumed by this tree yet, check summoned pet.
				if (attackerPet.getControlItemId() == st.getInteger("summonOid"))
				{
					// Check attacks completed.
					int attack = st.getInteger("attack") + 1;
					if (attack > td._attacks)
					{
						// Check leaf present and chance.
						if (Rnd.get(100) < td._chance && player.getInventory().hasItem(FAIRY_LEAF))
						{
							st.set("iCond", condition | mask);
							st.set("attack", 0);
							
							npc.broadcastNpcSay(NpcStringId.ID_42111);
							takeItems(player, FAIRY_LEAF, 1);
							
							// Four leafs have been used, update quest state.
							if (st.getInteger("iCond") == 63)
							{
								st.setCond(3);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
					}
					else
					{
						st.set("attack", attack);
						
						// Cast Dryad Root on attacker, when abyss tree.
						if (npcId == FAIRY_TREE_OF_ABYSS && Rnd.get(100) < 2)
							npc.getAI().addCastDesire(attackerPet, 1201, 33, 1000000);
					}
				}
			}
			else
			{
				// Leaf consumed by this tree, say random message.
				npc.broadcastNpcSay(Rnd.get(td._messages));
			}
		}
		else
		{
			// Attacker is player with quest, try to curse him.
			if (Rnd.get(100) < 30)
				npc.getAI().addCastDesire(attacker, 4243, 1, 1000000);
		}
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		// Regular tree minions are speaking upon spawn.
		if (npc.getNpcId() == SOUL_OF_TREE_GUARDIAN)
			npc.broadcastNpcSay(Rnd.get(GUARDIAN_MESSAGES));
		// Disable core AI for trees.
		else
		{
			npc.disableCoreAi(true);
			npc.setShowSummonAnimation(true);
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		// Spawn 20 ghosts, attacking the killer.
		for (int i = 0; i < 20; i++)
		{
			// Spawn minion and mark as additional wave.
			final Npc ghost = createOnePrivate(npc, SOUL_OF_TREE_GUARDIAN, 300000, true);
			if (ghost != null)
			{
				// First ghost casts a curse on a killer.
				if (i == 0)
					ghost.getAI().addCastDesire(killer, 4243, 1, 1000000);
				
				ghost.getAI().addAttackDesire(killer, 2000);
			}
		}
	}
	
	private static int getDragonfluteCount(Player player)
	{
		final Inventory i = player.getInventory();
		return i.getItemCount(DRAGONFLUTE_OF_WIND) + i.getItemCount(DRAGONFLUTE_OF_STAR) + i.getItemCount(DRAGONFLUTE_OF_TWILIGHT);
	}
	
	/**
	 * Supporting class containing data of a tree.
	 */
	private class TreeData
	{
		private final int _mask;
		private final int _attacks;
		private final int _chance;
		private final NpcStringId[] _messages;
		
		private TreeData(int mask, int attacks, int chance, NpcStringId... messages)
		{
			_mask = mask;
			_attacks = attacks;
			_chance = chance;
			_messages = messages;
		}
	}
}