package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillUse;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ThirdClassQuest extends Quest
{
	// Class
	private final int _classId;
	private final ClassId _prevClassId;
	
	// Items
	private static final int ICE_CRYSTAL = 7080; // item 0
	private static final int DIVINE_STONE_OF_WISDOM = 7081; // item 2
	private static final int BOOK_OF_GIANTS = 6622;
	/** Main quest item, identifying the quest. */
	protected int _itemMain = 0; // item 10
	/** Optional item (fish) to be delivered with Ice Crystal. */
	protected int _itemOptional = 0; // item 11
	/** Reward for delivering Ice Crystal (with optional item). */
	protected int _itemReward = 0; // item 1
	/** Resonance Amulet - 1 to resonate with Tablet of Vision. */
	protected int _itemAmulet1st = 0; // item 4
	/** Resonance Amulet - 2 to resonate with Tablet of Vision. */
	protected int _itemAmulet2nd = 0; // item 5
	/** Resonance Amulet - 3 to resonate with Tablet of Vision. */
	protected int _itemAmulet3rd = 0; // item 6
	/** Resonance Amulet - 4 to resonate with Tablet of Vision. */
	protected int _itemAmulet4th = 0; // item 7
	/** Halisha's Mark being dropped by monsters in Shrine of Loyalty. */
	protected int _itemHalishaMark = 0; // item 3
	/** Resonance Amulet - 5 to resonate with Tablet of Vision. */
	protected int _itemAmulet5th = 0; // item 8
	/** Resonance Amulet - 6 to resonate with Tablet of Vision. */
	protected int _itemAmulet6th = 0; // item 9
	
	// NPCs
	/** Main quest NPC. */
	protected int _npcMain = 0; // npc 0
	/** The NPC trading reward item for the Resonance Amulet - 1. */
	protected int _npc1st = 0; // npc 2
	/** The NPC trading Ice Crystal (with optional item) for reward item. */
	protected int _npc2nd = 0; // npc 1
	/** 1st Tablet of Vision. */
	protected int _npcTablet1st = 0; // npc 5
	/** 2nd Tablet of Vision. */
	protected int _npcTablet2nd = 0; // npc 6
	/** 3rd Tablet of Vision. */
	protected int _npcTablet3rd = 0; // npc 7
	/** The NPC trading Divine Stone of Wisdom for the Resonance Amulet - 4. */
	protected int _npc3rd = 0; // npc 3
	/** 4th Tablet of Vision. */
	protected int _npcTablet4th = 0; // npc 8
	/** The NPC guiding to hunt Halisha to obtain the Resonance Amulet - 5. */
	protected int _npc4th = 0; // npc 11
	/** 5th Tablet of Vision. */
	protected int _npcTablet5th = 0; // npc 9
	/** 6th Tablet of Vision. */
	protected int _npcTablet6th = 0; // npc 10
	/** The defending NPC of the combat near 6th Tablet of Vision. */
	protected int _npcDefender = 0; // npc 4
	
	// Monsters
	/** Monsters in Shrine of Loyalty giving Halisha's Mark. */
	private static final int[] SHRINE_OF_LOYALTY =
	{
		21646,
		21647,
		21648,
		21649,
		21650,
		21651
	};
	/** Archons of Halisha spawned in Four Sepulchers giving Resonance Amulet - 5. */
	private static final int[] ARCHON_OF_HALISHA_FOUR_SEPULCHERS =
	{
		18212,
		18213,
		18214,
		18215,
		18216,
		18217,
		18218,
		18219
	};
	/** The Guardian of Forbidden Knowledge monster giving Resonance Amulet - 2. */
	protected int _mobGuardian = 0; // new definition
	/** The corrupted monster giving Resonance Amulet - 3. */
	protected int _mobCorrupted = 0; // mob 0
	/** The Archon of Halisha spawned in Shrine of Loyalty giving Resonance Amulet - 5. */
	protected int _mobHalisha = 0; // mob 1
	/** The attacking monster of the combat near 6th Tablet of Vision. */
	protected int _mobAttacker = 0; // mob 2
	
	// Locations
	/** The location to spawn corrupted monster. */
	protected SpawnLocation _locCorrupted = null; // loc 0
	/** The location to spawn attacking monster of the combat near 6th Tablet of Vision. */
	protected SpawnLocation _locAttacker = null; // loc 1
	/** The location to spawn defending NPC of the combat near 6th Tablet of Vision. */
	protected SpawnLocation _locDefender = null; // loc 2
	
	// Messages
	protected NpcStringId _msgCorruptedSpawn = null; // Text[0]
	protected NpcStringId _msgCorruptedDespawn = null; // Text[1]
	protected NpcStringId _msgCorruptedKill = null; // Text[12]
	protected NpcStringId _msgHalishaSpawn = null; // new definition
	protected NpcStringId _msgHalishaDespawn = null; // Text[6]
	protected NpcStringId _msgHalishaKill = null; // Text[4]
	protected NpcStringId _msgHalishaKillOther = null; // Text[5]
	protected NpcStringId _msgAttackerSpawn = null; //
	protected NpcStringId _msgAttackerDespawn = null; //
	protected NpcStringId _msgAttackerAttack1 = null; // Text[16]
	protected NpcStringId _msgAttackerAttack16 = null; // Text[17]
	protected NpcStringId _msgDefenderSpawn = null; //
	protected NpcStringId _msgDefenderDespawnWon = null; //
	protected NpcStringId _msgDefenderDespawnLost = null; //
	protected NpcStringId _msgDefenderCombat = null; //
	protected NpcStringId _msgDefenderCombatIdle1 = null; //
	protected NpcStringId _msgDefenderCombatIdle2 = null; //
	protected NpcStringId _msgDefenderReward = null; // Text[13]
	
	// Shared quest variables.
	private static final Map<ClassId, ThirdClassQuest> _quests = HashMap.newHashMap(31);
	private static final Map<Npc, Npc> _npcBusy = new ConcurrentHashMap<>();
	private static final Map<Npc, Attackable> _npcSpawns = new ConcurrentHashMap<>();
	
	/**
	 * Implicit constructor for third class quest core. The third class quest core is used for shared functionalities between all third class quests ({@link Q070_SagaOfThePhoenixKnight} up to {@link Q100_SagaOfTheMaestro}).
	 */
	public ThirdClassQuest()
	{
		super(-1, "Third Class Quest");
		
		_classId = 0;
		_prevClassId = null;
		
		addMyDying(SHRINE_OF_LOYALTY);
	}
	
	/**
	 * Superclass constructor, used by particular third class quest ({@link Q070_SagaOfThePhoenixKnight} up to {@link Q100_SagaOfTheMaestro}).
	 * @param id : ID of the third class quest.
	 * @param descr : Name of the third class quest.
	 * @param classId : The target third class of the quest.
	 */
	public ThirdClassQuest(int id, String descr, ClassId classId)
	{
		super(id, descr);
		
		_classId = classId.getId();
		_prevClassId = classId.getParent();
		_quests.put(_prevClassId, this);
		
		// Register quest items, NPCs, monsters and actions.
		setItemsNpcsMobsLocs();
		setItemsIds(_itemMain, _itemReward, _itemAmulet1st, _itemAmulet2nd, _itemAmulet3rd, _itemAmulet4th, _itemHalishaMark, _itemAmulet5th, _itemAmulet6th);
		addQuestStart(_npcMain);
		addFirstTalkId(_npcDefender);
		addTalkId(_npcMain, _npc1st, _npc2nd, _npcTablet1st, _npcTablet2nd, _npcTablet3rd, _npc3rd, _npcTablet4th, _npc4th, _npcTablet5th, _npcDefender, _npcTablet6th);
		
		addAttacked(_mobCorrupted, _mobAttacker, _npcDefender);
		addDecayed(_mobCorrupted, _mobHalisha, _npcDefender, _mobAttacker);
		addMyDying(_mobGuardian, _mobCorrupted, _mobHalisha, _mobAttacker);
		addMyDying(ARCHON_OF_HALISHA_FOUR_SEPULCHERS);
		addSeeSpell(_mobCorrupted);
	}
	
	/**
	 * Set third class quest specific items, NPCs, monsters and spawn locations.
	 */
	protected void setItemsNpcsMobsLocs()
	{
		// Overidden in dedicated classes.
	}
	
	private static void cast(Npc npc, Creature target, int skillId, int level)
	{
		target.broadcastPacket(new MagicSkillUse(target, target, skillId, level, 6000, 1));
		target.broadcastPacket(new MagicSkillUse(npc, npc, skillId, level, 6000, 1));
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(getName());
		if (st == null)
			return null;
		
		// Main quest NPC.
		if (event.equalsIgnoreCase("0-1"))
		{
			if (player.getStatus().getLevel() < 76)
				htmltext = "0-02.htm";
			else
				htmltext = "0-05.htm";
		}
		else if (event.equalsIgnoreCase("accept"))
		{
			htmltext = "0-03.htm";
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, _itemMain, 1);
		}
		else if (event.equalsIgnoreCase("0-2"))
		{
			if (player.getStatus().getLevel() >= 76)
			{
				htmltext = "0-07.htm";
				giveItems(player, 57, 5000000);
				giveItems(player, BOOK_OF_GIANTS, 1);
				rewardExpAndSp(player, 2299404, 0);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
				
				player.setClassId(_classId);
				if (!player.isSubClassActive() && player.getBaseClass() == _prevClassId.getId())
					player.setBaseClass(_classId);
				player.broadcastUserInfo();
				
				cast(npc, player, 4339, 1);
			}
			else
			{
				htmltext = "0-08.htm";
				st.setCond(20);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, _itemMain, -1);
			}
		}
		// 1st NPC - trading reward item for the Resonance Amulet - 1.
		else if (event.equalsIgnoreCase("2-1"))
		{
			htmltext = "2-05.htm";
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("2-2"))
		{
			htmltext = "2-06.htm";
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, _itemReward, 1);
			giveItems(player, _itemAmulet1st, 1);
		}
		// 2nd NPC - trading Ice Crystal (with optional item) for reward item.
		else if (event.equalsIgnoreCase("1-3"))
		{
			htmltext = "1-05.htm";
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("1-4"))
		{
			htmltext = "1-06.htm";
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ICE_CRYSTAL, 1);
			if (_itemOptional != 0)
				takeItems(player, _itemOptional, 1);
			giveItems(player, _itemReward, 1);
		}
		// 1st Tablet of Vision.
		else if (event.equalsIgnoreCase("5-1"))
		{
			htmltext = "5-02.htm";
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, _itemAmulet1st, 1);
			
			cast(npc, player, 4546, 1);
		}
		// 2nd Tablet of Vision.
		else if (event.equalsIgnoreCase("6-1"))
		{
			htmltext = "6-03.htm";
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, _itemAmulet2nd, 1);
			
			cast(npc, player, 4546, 1);
		}
		// 3rd Tablet of Vision.
		else if (event.equalsIgnoreCase("7-1"))
		{
			// Get monster spawned by this NPC and check busy state.
			Npc corrupted = _npcBusy.get(npc);
			if (corrupted == null)
			{
				// Npc is free.
				
				// Create corrupted NPC, link it to the player.
				corrupted = createOnePrivateEx(npc, _mobCorrupted, _locCorrupted.getX(), _locCorrupted.getY(), _locCorrupted.getZ(), _locCorrupted.getHeading(), 0, true, player.getObjectId(), player.getObjectId(), npc.getObjectId());
				corrupted.setScriptValue(player.getObjectId());
				
				// Set NPC to be busy by this corrupted NPC and start timer for message.
				_npcBusy.put(npc, corrupted);
				startQuestTimer("corrupted", corrupted, player, 500);
				htmltext = "7-02.htm";
			}
			else
			{
				// Npc is busy by fight being in progress.
				
				// Check if it is player's fight or not.
				if (corrupted.getScriptValue() == player.getObjectId())
					htmltext = "7-03.htm";
				else
					htmltext = "7-04.htm";
			}
		}
		else if (event.equalsIgnoreCase("7-2"))
		{
			htmltext = "7-06.htm";
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, _itemAmulet3rd, 1);
			
			cast(npc, player, 4546, 1);
		}
		// 3rd NPC - trading Divine Stone of Wisdom for the Resonance Amulet - 4.
		else if (event.equalsIgnoreCase("3-5"))
		{
			htmltext = "3-07.htm";
		}
		else if (event.equalsIgnoreCase("3-6"))
		{
			htmltext = "3-02.htm";
			st.setCond(11);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("3-7"))
		{
			htmltext = "3-03.htm";
			st.setCond(12);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("3-8"))
		{
			htmltext = "3-08.htm";
			st.setCond(13);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, DIVINE_STONE_OF_WISDOM, 1);
			giveItems(player, _itemAmulet4th, 1);
		}
		// 4th Tablet of Vision.
		else if (event.equalsIgnoreCase("8-1"))
		{
			htmltext = "8-02.htm";
			st.setCond(14);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, _itemAmulet4th, 1);
			
			cast(npc, player, 4546, 1);
		}
		// 4th NPC - guiding to hunt Halisha to obtain the Resonance Amulet - 5.
		else if (event.equalsIgnoreCase("11-9"))
		{
			htmltext = "11-03.htm";
			st.setCond(15);
			playSound(player, SOUND_MIDDLE);
		}
		// 5th Tablet of Vision.
		else if (event.equalsIgnoreCase("9-1"))
		{
			htmltext = "9-03.htm";
			st.setCond(17);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, _itemAmulet5th, 1);
			
			cast(npc, player, 4546, 1);
		}
		// 6th Tablet of Vision.
		else if (event.equalsIgnoreCase("10-1"))
		{
			// Get monster spawned by this NPC and check busy state.
			Npc defender = _npcBusy.get(npc);
			if (defender == null)
			{
				// Npc is free.
				
				// Reset values, if previous spawn has not succeeded.
				st.unset("attacks");
				
				// Create defender NPC, link it to the player.
				defender = addSpawn(_npcDefender, _locDefender, false, 60000, true);
				defender.setScriptValue(player.getObjectId());
				
				// Create attacker NPC.
				Attackable attacker = (Attackable) addSpawn(_mobAttacker, _locAttacker, false, 59000, true);
				attacker.setScriptValue(player.getObjectId());
				attacker.getAI().addAttackDesire(defender, 200);
				
				// Set NPC to be busy by this defending NPC and start timer for message.
				_npcBusy.put(npc, defender);
				_npcSpawns.put(defender, attacker);
				
				startQuestTimer("defender", defender, player, 500);
				startQuestTimer("attacker", attacker, player, 500);
				
				htmltext = "10-02.htm";
			}
			else
			{
				// Npc is busy by fight being in progress.
				
				// Check if it is player's fight or not.
				if (defender.getScriptValue() == player.getObjectId())
					htmltext = "10-03.htm";
				else
					htmltext = "10-04.htm";
			}
		}
		else if (event.equalsIgnoreCase("10-2"))
		{
			htmltext = "10-06.htm";
			st.setCond(19);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, _itemAmulet6th, 1);
			
			cast(npc, player, 4546, 1);
		}
		// The defending NPC of the combat near 6th Tablet of Vision.
		else if (event.equalsIgnoreCase("4-1"))
		{
			htmltext = "4-010.htm";
		}
		else if (event.equalsIgnoreCase("4-2"))
		{
			htmltext = "4-011.htm";
			st.unset("attacks");
			st.setCond(18);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, _itemAmulet6th, 1);
		}
		else if (event.equalsIgnoreCase("4-3"))
		{
			htmltext = null;
			st.unset("attacks");
			st.setCond(18);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, _itemAmulet6th, 1);
			
			npc.broadcastNpcSay(_msgDefenderReward);
			cancelQuestTimers("defender2", npc);
			cancelQuestTimers("defender3", npc);
			npc.deleteMe();
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		// Corrupted NPC at 3rd Tablet of Vision - spawn.
		if (name.equalsIgnoreCase("corrupted"))
		{
			// Send spawn message.
			npc.broadcastNpcSay(_msgCorruptedSpawn, player.getName());
			return null;
		}
		
		// Defending NPC at 6th Tablet of Vision - spawn.
		if (name.equalsIgnoreCase("defender"))
		{
			// Send spawn message.
			npc.broadcastNpcSay(_msgDefenderSpawn);
			startQuestTimer("defender2", npc, player, 1500);
			return null;
		}
		// Defending NPC at 6th Tablet of Vision - 1st combat message.
		else if (name.equalsIgnoreCase("defender2"))
		{
			// Send 1st combat message.
			npc.broadcastNpcSay(_msgDefenderCombat, player.getName());
			startQuestTimer("defender3", npc, player, 10000);
			return null;
		}
		// Defending NPC at 6th Tablet of Vision - 2nd combat message.
		else if (name.equalsIgnoreCase("defender3"))
		{
			QuestState st = checkPlayerCondition(player, npc, 17);
			if (st == null)
				return null;
			
			// Send next combat messages (if npc is alive).
			if (_npcSpawns.containsKey(npc))
			{
				npc.broadcastNpcSay(Rnd.nextBoolean() ? _msgDefenderCombatIdle1 : _msgDefenderCombatIdle2, player.getName());
				startQuestTimer("defender3", npc, player, 10000);
			}
			return null;
		}
		
		// Attacker NPC at 6th Tablet of Vision - spawn.
		if (name.equalsIgnoreCase("attacker"))
		{
			// Send spawn message.
			npc.broadcastNpcSay(_msgAttackerSpawn, player.getName());
			return null;
		}
		
		return null;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestList().getQuestState(getName());
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				if (npc.getNpcId() == _npcMain)
				{
					// Check player class.
					if (player.getClassId() == _prevClassId)
						htmltext = "0-01.htm";
					else
						htmltext = "0-02.htm";
				}
				break;
			
			case STARTED:
				// Player class is not second class of this quest, show no quest.
				if (player.getClassId() != _prevClassId)
					return htmltext;
				
				final int npcId = npc.getNpcId();
				switch (st.getCond())
				{
					case 1:
						if (npcId == _npcMain)
							htmltext = "0-04.htm";
						else if (npcId == _npc1st)
							htmltext = "2-01.htm";
						break;
					
					case 2:
						if (npcId == _npc1st)
							htmltext = "2-02.htm";
						else if (npcId == _npc2nd)
							htmltext = "1-01.htm";
						break;
					
					case 3:
						if (npcId == _npc1st)
							htmltext = "2-02.htm";
						else if (npcId == _npc2nd)
						{
							if (player.getInventory().hasItem(ICE_CRYSTAL) && (_itemOptional == 0 || player.getInventory().hasItem(_itemOptional)))
								htmltext = "1-03.htm";
							else
								htmltext = "1-02.htm";
						}
						break;
					
					case 4:
						if (npcId == _npc2nd)
							htmltext = "1-04.htm";
						else if (npcId == _npc1st)
							htmltext = "2-03.htm";
						break;
					
					case 5:
						if (npcId == _npc1st)
							htmltext = "2-04.htm";
						else if (npcId == _npcTablet1st)
							htmltext = "5-01.htm";
						break;
					
					case 6:
						if (npcId == _npcTablet1st)
							htmltext = "5-03.htm";
						else if (npcId == _npcTablet2nd)
							htmltext = "6-01.htm";
						break;
					
					case 7:
						if (npcId == _npcTablet2nd)
							htmltext = "6-02.htm";
						break;
					
					case 8:
						if (npcId == _npcTablet2nd)
							htmltext = "6-04.htm";
						else if (npcId == _npcTablet3rd)
							htmltext = "7-01.htm";
						break;
					
					case 9:
						if (npcId == _npcTablet3rd)
							htmltext = "7-05.htm";
						break;
					
					case 10:
						if (npcId == _npcTablet3rd)
							htmltext = "7-07.htm";
						else if (npcId == _npc3rd)
							htmltext = "3-01.htm";
						break;
					
					case 11, 12:
						if (npcId == _npc3rd)
						{
							if (player.getInventory().hasItem(DIVINE_STONE_OF_WISDOM))
								htmltext = "3-05.htm";
							else
								htmltext = "3-04.htm";
						}
						break;
					
					case 13:
						if (npcId == _npc3rd)
							htmltext = "3-06.htm";
						else if (npcId == _npcTablet4th)
							htmltext = "8-01.htm";
						break;
					
					case 14:
						if (npcId == _npcTablet4th)
							htmltext = "8-03.htm";
						else if (npcId == _npc4th)
							htmltext = "11-01.htm";
						break;
					
					case 15:
						if (npcId == _npc4th)
							htmltext = "11-02.htm";
						else if (npcId == _npcTablet5th)
							htmltext = "9-01.htm";
						break;
					
					case 16:
						if (npcId == _npcTablet5th)
							htmltext = "9-02.htm";
						break;
					
					case 17:
						if (npcId == _npcTablet5th)
							htmltext = "9-04.htm";
						else if (npcId == _npcTablet6th)
							htmltext = "10-01.htm";
						break;
					
					case 18:
						if (npcId == _npcTablet6th)
							htmltext = "10-05.htm";
						break;
					
					case 19:
						if (npcId == _npcTablet6th)
							htmltext = "10-07.htm";
						else if (npcId == _npcMain)
							htmltext = "0-06.htm";
						break;
					
					case 20:
						if (npcId == _npcMain)
						{
							// Player has completed the quest, check his level.
							if (player.getStatus().getLevel() >= 76)
							{
								htmltext = "0-09.htm";
								giveItems(player, 57, 5000000);
								giveItems(player, BOOK_OF_GIANTS, 1);
								rewardExpAndSp(player, 2299404, 0);
								playSound(player, SOUND_FINISH);
								st.exitQuest(false);
								
								player.setClassId(_classId);
								if (!player.isSubClassActive() && player.getBaseClass() == _prevClassId.getId())
									player.setBaseClass(_classId);
								player.broadcastUserInfo();
								
								cast(npc, player, 4339, 1);
							}
							else
								htmltext = "0-010.htm";
						}
						break;
				}
				break;
			
			case COMPLETED:
				// Show completed html, if main npc.
				if (npc.getNpcId() == _npcMain)
					htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		
		QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return htmltext;
		
		int cond = st.getCond();
		if (cond == 17)
		{
			if (_npcSpawns.containsKey(npc))
			{
				// Attacker is alive. Check spawning player.
				if (npc.getScriptValue() == player.getObjectId())
				{
					// Attacker is alive, spawned by this player. Check status.
					if (st.getInteger("attacks") < 16)
						// Fight still in progress.
						htmltext = "4-01.htm";
					else
						// Won previous fight (attacker respawned by this player).
						htmltext = "4-04.htm";
				}
				else
				{
					// Attacker is alive, spanwed by another player. Check status.
					if (st.getInteger("attacks") < 16)
						// Fight still in progress.
						htmltext = "4-02.htm";
					else
						// Won another fight (attacker respawned by another player).
						htmltext = "4-05.htm";
				}
			}
			else
			{
				// Attacker despawned.
				
				if (st.getInteger("attacks") < 16)
					// Attacker despawned, spawned by any player, player not won yet.
					htmltext = "4-03.htm";
				else if (npc.getScriptValue() == player.getObjectId())
					// Attacker despawned, spawned by this player, player won this or another fight.
					htmltext = "4-06.htm";
				else
					// Attacker despawned, spawned by another player, player won another fight (attacker respawned by another player).
					htmltext = "4-07.htm";
			}
		}
		else if (cond == 18)
		{
			if (_npcSpawns.containsKey(npc))
				htmltext = "4-08.htm";
			else
				htmltext = "4-09.htm";
		}
		
		player.getQuestList().setLastQuestNpcObjectId(npc.getObjectId());
		return htmltext;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Player player = attacker.getActingPlayer();
		if (player == null)
			return;
		
		final int npcId = npc.getNpcId();
		
		// Corrupted NPC at 3rd Tablet of Vision.
		if (npcId == _mobCorrupted)
		{
			// Check if player spawned the corrupted NPC, and check Player class.
			if (player.getObjectId() != npc.getScriptValue() || player.getClassId() != _prevClassId)
			{
				// Force despawn the corrupted NPC.
				npc.setScriptValue(0);
				npc.deleteMe();
			}
		}
		else if (npcId == _mobAttacker)
		{
			// Check if player spawned the corrupted NPC.
			if (player.getObjectId() != npc.getScriptValue())
				return;
			
			// Check player's class.
			if (player.getClassId() != _prevClassId)
				return;
			
			QuestState st = checkPlayerCondition(player, npc, 17);
			if (st == null)
				return;
			
			// Get and increase attack count.
			int attacks = st.getInteger("attacks");
			attacks++;
			st.set("attacks", attacks);
			
			// Check attack count.
			if (attacks == 1)
			{
				npc.broadcastNpcSay(_msgAttackerAttack1, player.getName());
			}
			else if (attacks == 16)
			{
				npc.broadcastNpcSay(_msgAttackerAttack16, player.getName());
				
				// Despawn (notify onDecay not to say message via script value).
				npc.setScriptValue(0);
				npc.deleteMe();
			}
		}
		else if (npcId == _npcDefender && !(attacker instanceof Playable))
			npc.getAI().addAttackDesire(attacker, 2000);
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		final int npcId = npc.getNpcId();
		
		if (npcId == _mobCorrupted)
		{
			// Remove NPC registration.
			_npcBusy.values().remove(npc);
			
			// Speak only when despawning naturally (not being killed).
			if (!npc.isDead() && npc.getScriptValue() > 0)
				npc.broadcastNpcSay(_msgCorruptedDespawn);
		}
		else if (npcId == _mobHalisha)
		{
			// Speak only when despawning naturally (not being killed).
			if (!npc.isDead())
				npc.broadcastNpcSay(_msgHalishaDespawn);
		}
		else if (npcId == _mobAttacker)
		{
			// Remove NPC registration.
			_npcSpawns.values().remove(npc);
			
			// Speak only when despawning naturally (not being killed).
			if (!npc.isDead() && npc.getScriptValue() > 0)
				npc.broadcastNpcSay(_msgAttackerDespawn);
		}
		else if (npcId == _npcDefender)
		{
			// Remove NPC registration.
			_npcBusy.values().remove(npc);
			_npcSpawns.remove(npc);
			
			Player p = World.getInstance().getPlayer(npc.getScriptValue());
			if (p == null)
			{
				npc.broadcastNpcSay(_msgDefenderDespawnLost);
				return;
			}
			
			QuestState st = checkPlayerCondition(p, npc, 17);
			if (st != null)
			{
				if (st.getInteger("attacks") > 15)
					npc.broadcastNpcSay(_msgDefenderDespawnWon);
				else
					npc.broadcastNpcSay(_msgDefenderDespawnLost);
			}
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		if (player == null)
			return;
		
		final int npcId = npc.getNpcId();
		
		// Guardian of Forbidden Knowledge at 2nd Tablet of Vision.
		if (npcId == _mobGuardian)
		{
			// Check player's class.
			if (player.getClassId() != _prevClassId)
				return;
			
			// Check player's quest status.
			QuestState st = checkPlayerCondition(player, npc, 6);
			if (st == null)
				return;
			
			// Kill 10 Guardians to obtain Resonance Amulet - 2.
			int kills = st.getInteger("kills") + 1;
			if (kills < 10)
				st.set("kills", kills);
			else
			{
				st.unset("kills");
				st.setCond(7);
				playSound(player, SOUND_MIDDLE);
				giveItems(player, _itemAmulet2nd, 1);
			}
			return;
		}
		
		// Corrupted NPC at 3rd Tablet of Vision.
		if (npcId == _mobCorrupted)
		{
			// Check if player spawned the corrupted NPC.
			if (player.getObjectId() != npc.getScriptValue())
				return;
			
			// Check player's class.
			if (player.getClassId() != _prevClassId)
				return;
			
			// Check player's quest status.
			QuestState st = checkPlayerCondition(player, npc, 8);
			if (st == null)
				return;
			
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, _itemAmulet3rd, 1);
			
			npc.broadcastNpcSay(_msgCorruptedKill);
			return;
		}
		
		// Monsters in Shrine of Loyalty.
		if (ArraysUtil.contains(SHRINE_OF_LOYALTY, npcId))
		{
			ThirdClassQuest tcq;
			QuestState st;
			
			// Check and evaluate party.
			if (player.isInParty())
			{
				List<QuestState> valid = new ArrayList<>();
				for (Player pm : player.getParty().getMembers())
				{
					// Get party member's third class quest.
					tcq = _quests.get(pm.getClassId());
					if (tcq == null)
						continue;
					
					// Check party member's quest status.
					st = tcq.checkPlayerCondition(pm, npc, 15);
					if (st == null)
						continue;
					
					valid.add(st);
				}
				
				// Pick random party member' state and check it.
				st = Rnd.get(valid);
				if (st == null)
					return;
				
				// Get party member's third class quest.
				tcq = (ThirdClassQuest) st.getQuest();
			}
			else
			{
				// Get player's third class quest.
				tcq = _quests.get(player.getClassId());
				if (tcq == null)
					return;
				
				// Check player's quest status.
				st = tcq.checkPlayerCondition(player, npc, 15);
				if (st == null)
					return;
			}
			
			// There is valid QuestState existing.
			Player p = st.getPlayer();
			if (p.getInventory().getItemCount(tcq._itemHalishaMark) < 700)
			{
				// Drop Mark of Halishas.
				dropItemsAlways(p, tcq._itemHalishaMark, 1, 700);
			}
			else
			{
				// Take 20 of Mark of Halisha.
				takeItems(p, tcq._itemHalishaMark, 20);
				
				// Create quest Archon of Halisha monster, link it to the player.
				Attackable archon = (Attackable) addSpawn(tcq._mobHalisha, npc, false, 600000, true);
				archon.setScriptValue(p.getObjectId());
				
				// Quest Archon of Halisha attack the player.
				archon.getAI().addAttackDesire(p, 200);
				
				// Send spawn message.
				archon.broadcastNpcSay(tcq._msgHalishaSpawn, p.getName());
			}
			return;
		}
		
		// The Archon of Halisha spawned in Shrine of Loyalty.
		if (npcId == _mobHalisha)
		{
			// Check if player spawned the Archon of Halisha.
			if (player.getObjectId() != npc.getScriptValue())
			{
				npc.broadcastNpcSay(_msgHalishaKillOther);
				return;
			}
			
			// Check player's class.
			if (player.getClassId() != _prevClassId)
				return;
			
			// Check player's quest status.
			QuestState st = checkPlayerCondition(player, npc, 15);
			if (st == null)
				return;
			
			st.setCond(16);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, _itemHalishaMark, -1);
			giveItems(player, _itemAmulet5th, 1);
			
			npc.broadcastNpcSay(_msgHalishaKill);
			return;
		}
		
		// Archon of Halisha inside Four Sepulchers.
		if (ArraysUtil.contains(ARCHON_OF_HALISHA_FOUR_SEPULCHERS, npcId))
		{
			// Check and evaluate party.
			if (player.isInParty())
			{
				for (Player pm : player.getParty().getMembers())
				{
					// Check party member's class.
					if (pm.getClassId() != _prevClassId)
						continue;
					
					// Check party member's quest status.
					QuestState st = checkPlayerCondition(pm, npc, 15);
					if (st == null)
						continue;
					
					st.setCond(16);
					playSound(pm, SOUND_MIDDLE);
					takeItems(pm, _itemHalishaMark, -1);
					giveItems(pm, _itemAmulet5th, 1);
				}
			}
			else
			{
				// Check player's class.
				if (player.getClassId() != _prevClassId)
					return;
				
				// Check player's quest status.
				QuestState st = checkPlayerCondition(player, npc, 15);
				if (st == null)
					return;
				
				st.setCond(16);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, _itemHalishaMark, -1);
				giveItems(player, _itemAmulet5th, 1);
			}
			return;
		}
		
		// The attacking monster of the combat near 6th Tablet of Vision. Do nothing (notify onDecay not to say message via script value).
		if (npcId == _mobAttacker)
			npc.setScriptValue(0);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player player, L2Skill skill, Creature[] targets, boolean isPet)
	{
		// Check player is existing and target is the monster.
		if (player == null || !ArraysUtil.contains(targets, npc))
			return;
		
		// Corrupted NPC at 3rd Tablet of Vision. Force the despawn if checks aren't correct.
		if (_mobCorrupted == npc.getNpcId() && (player.getObjectId() != npc.getScriptValue() || player.getClassId() != _prevClassId))
		{
			npc.setScriptValue(0);
			npc.deleteMe();
		}
	}
}