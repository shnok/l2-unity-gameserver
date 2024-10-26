package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Q229_TestOfWitchcraft extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q229_TestOfWitchcraft";
	
	// Items
	private static final int ORIM_DIAGRAM = 3308;
	private static final int ALEXANDRIA_BOOK = 3309;
	private static final int IKER_LIST = 3310;
	private static final int DIRE_WYRM_FANG = 3311;
	private static final int LETO_LIZARDMAN_CHARM = 3312;
	private static final int EN_GOLEM_HEARTSTONE = 3313;
	private static final int LARA_MEMO = 3314;
	private static final int NESTLE_MEMO = 3315;
	private static final int LEOPOLD_JOURNAL = 3316;
	private static final int AKLANTOTH_GEM_1 = 3317;
	private static final int AKLANTOTH_GEM_2 = 3318;
	private static final int AKLANTOTH_GEM_3 = 3319;
	private static final int AKLANTOTH_GEM_4 = 3320;
	private static final int AKLANTOTH_GEM_5 = 3321;
	private static final int AKLANTOTH_GEM_6 = 3322;
	private static final int BRIMSTONE_1 = 3323;
	private static final int ORIM_INSTRUCTIONS = 3324;
	private static final int ORIM_LETTER_1 = 3325;
	private static final int ORIM_LETTER_2 = 3326;
	private static final int SIR_VASPER_LETTER = 3327;
	private static final int VADIN_CRUCIFIX = 3328;
	private static final int TAMLIN_ORC_AMULET = 3329;
	private static final int VADIN_SANCTIONS = 3330;
	private static final int IKER_AMULET = 3331;
	private static final int SOULTRAP_CRYSTAL = 3332;
	private static final int PURGATORY_KEY = 3333;
	private static final int ZERUEL_BIND_CRYSTAL = 3334;
	private static final int BRIMSTONE_2 = 3335;
	private static final int SWORD_OF_BINDING = 3029;
	
	// Rewards
	private static final int MARK_OF_WITCHCRAFT = 3307;
	
	// NPCs
	private static final int LARA = 30063;
	private static final int ALEXANDRIA = 30098;
	private static final int IKER = 30110;
	private static final int VADIN = 30188;
	private static final int NESTLE = 30314;
	private static final int SIR_KLAUS_VASPER = 30417;
	private static final int LEOPOLD = 30435;
	private static final int KAIRA = 30476;
	private static final int ORIM = 30630;
	private static final int RODERIK = 30631;
	private static final int ENDRIGO = 30632;
	private static final int EVERT = 30633;
	
	// Monsters
	private static final int DIRE_WYRM = 20557;
	private static final int ENCHANTED_STONE_GOLEM = 20565;
	private static final int LETO_LIZARDMAN = 20577;
	private static final int LETO_LIZARDMAN_ARCHER = 20578;
	private static final int LETO_LIZARDMAN_SOLDIER = 20579;
	private static final int LETO_LIZARDMAN_WARRIOR = 20580;
	private static final int LETO_LIZARDMAN_SHAMAN = 20581;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int TAMLIN_ORC = 20601;
	private static final int TAMLIN_ORC_ARCHER = 20602;
	private static final int NAMELESS_REVENANT = 27099;
	private static final int SKELETAL_MERCENARY = 27100;
	private static final int DREVANUL_PRINCE_ZERUEL = 27101;
	
	// Checks
	private Npc _drevanulPrinceZeruel_Orim;
	private Npc _drevanulPrinceZeruel_Evert;
	
	public Q229_TestOfWitchcraft()
	{
		super(229, "Test Of Witchcraft");
		
		setItemsIds(ORIM_DIAGRAM, ALEXANDRIA_BOOK, IKER_LIST, DIRE_WYRM_FANG, LETO_LIZARDMAN_CHARM, EN_GOLEM_HEARTSTONE, LARA_MEMO, NESTLE_MEMO, LEOPOLD_JOURNAL, AKLANTOTH_GEM_1, AKLANTOTH_GEM_2, AKLANTOTH_GEM_3, AKLANTOTH_GEM_4, AKLANTOTH_GEM_5, AKLANTOTH_GEM_6, BRIMSTONE_1, ORIM_INSTRUCTIONS, ORIM_LETTER_1, ORIM_LETTER_2, SIR_VASPER_LETTER, VADIN_CRUCIFIX, TAMLIN_ORC_AMULET, VADIN_SANCTIONS, IKER_AMULET, SOULTRAP_CRYSTAL, PURGATORY_KEY, ZERUEL_BIND_CRYSTAL, BRIMSTONE_2, SWORD_OF_BINDING);
		
		addQuestStart(ORIM);
		addTalkId(LARA, ALEXANDRIA, IKER, VADIN, NESTLE, SIR_KLAUS_VASPER, LEOPOLD, KAIRA, ORIM, RODERIK, ENDRIGO, EVERT);
		
		addAttacked(NAMELESS_REVENANT, SKELETAL_MERCENARY, DREVANUL_PRINCE_ZERUEL);
		addDecayed(DREVANUL_PRINCE_ZERUEL);
		addMyDying(DIRE_WYRM, ENCHANTED_STONE_GOLEM, LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER, LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR, LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD, TAMLIN_ORC, TAMLIN_ORC_ARCHER, NAMELESS_REVENANT, SKELETAL_MERCENARY, DREVANUL_PRINCE_ZERUEL);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// ORIM
		if (event.equalsIgnoreCase("30630-08.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ORIM_DIAGRAM, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30630-08a.htm";
		}
		else if (event.equalsIgnoreCase("30630-14.htm"))
		{
			st.setCond(4);
			st.unset("gem456");
			playSound(player, SOUND_MIDDLE);
			takeItems(player, AKLANTOTH_GEM_1, 1);
			takeItems(player, AKLANTOTH_GEM_2, 1);
			takeItems(player, AKLANTOTH_GEM_3, 1);
			takeItems(player, AKLANTOTH_GEM_4, 1);
			takeItems(player, AKLANTOTH_GEM_5, 1);
			takeItems(player, AKLANTOTH_GEM_6, 1);
			takeItems(player, ALEXANDRIA_BOOK, 1);
			giveItems(player, BRIMSTONE_1, 1);
			
			if (_drevanulPrinceZeruel_Orim == null)
				_drevanulPrinceZeruel_Orim = addSpawn(DREVANUL_PRINCE_ZERUEL, 70381, 109638, -3726, 0, false, 120000, true);
		}
		else if (event.equalsIgnoreCase("30630-16.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BRIMSTONE_1, 1);
			giveItems(player, ORIM_INSTRUCTIONS, 1);
			giveItems(player, ORIM_LETTER_1, 1);
			giveItems(player, ORIM_LETTER_2, 1);
		}
		else if (event.equalsIgnoreCase("30630-22.htm"))
		{
			takeItems(player, IKER_AMULET, 1);
			takeItems(player, ORIM_INSTRUCTIONS, 1);
			takeItems(player, PURGATORY_KEY, 1);
			takeItems(player, SWORD_OF_BINDING, 1);
			takeItems(player, ZERUEL_BIND_CRYSTAL, 1);
			giveItems(player, MARK_OF_WITCHCRAFT, 1);
			rewardExpAndSp(player, 139796, 40000);
			player.broadcastPacket(new SocialAction(player, 3));
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		// ALEXANDRIA
		else if (event.equalsIgnoreCase("30098-03.htm"))
		{
			st.setCond(2);
			st.set("gem456", 1);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ORIM_DIAGRAM, 1);
			giveItems(player, ALEXANDRIA_BOOK, 1);
		}
		// IKER
		else if (event.equalsIgnoreCase("30110-03.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, IKER_LIST, 1);
		}
		else if (event.equalsIgnoreCase("30110-08.htm"))
		{
			takeItems(player, ORIM_LETTER_2, 1);
			giveItems(player, IKER_AMULET, 1);
			giveItems(player, SOULTRAP_CRYSTAL, 1);
			
			if (player.getInventory().hasItem(SWORD_OF_BINDING))
			{
				st.setCond(7);
				playSound(player, SOUND_MIDDLE);
			}
			else
				playSound(player, SOUND_ITEMGET);
		}
		// KAIRA
		else if (event.equalsIgnoreCase("30476-02.htm"))
		{
			giveItems(player, AKLANTOTH_GEM_2, 1);
			
			if (player.getInventory().hasItems(AKLANTOTH_GEM_1, AKLANTOTH_GEM_3) && st.getInteger("gem456") == 6)
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
			}
			else
				playSound(player, SOUND_ITEMGET);
		}
		// LARA
		else if (event.equalsIgnoreCase("30063-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, LARA_MEMO, 1);
		}
		// NESTLE
		else if (event.equalsIgnoreCase("30314-02.htm"))
		{
			st.set("gem456", 2);
			playSound(player, SOUND_ITEMGET);
			giveItems(player, NESTLE_MEMO, 1);
		}
		// LEOPOLD
		else if (event.equalsIgnoreCase("30435-02.htm"))
		{
			st.set("gem456", 3);
			playSound(player, SOUND_ITEMGET);
			takeItems(player, NESTLE_MEMO, 1);
			giveItems(player, LEOPOLD_JOURNAL, 1);
		}
		// SIR KLAUS VASPER
		else if (event.equalsIgnoreCase("30417-03.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, ORIM_LETTER_1, 1);
			giveItems(player, SIR_VASPER_LETTER, 1);
		}
		// EVERT
		else if (event.equalsIgnoreCase("30633-02.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, BRIMSTONE_2, 1);
			
			if (_drevanulPrinceZeruel_Evert == null)
				_drevanulPrinceZeruel_Evert = addSpawn(DREVANUL_PRINCE_ZERUEL, 13395, 169807, -3708, 0, false, 300000, true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		// Despawns Drevanul Prince Zeruel
		if (name.equalsIgnoreCase("zeruel_despawn"))
		{
			if (npc == _drevanulPrinceZeruel_Orim)
			{
				npc.getAttack().stop();
				npc.deleteMe();
			}
		}
		
		return null;
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
				if (player.getClassId() != ClassId.KNIGHT && player.getClassId() != ClassId.HUMAN_WIZARD && player.getClassId() != ClassId.PALUS_KNIGHT)
					htmltext = "30630-01.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30630-02.htm";
				else
					htmltext = (player.getClassId() == ClassId.HUMAN_WIZARD) ? "30630-03.htm" : "30630-05.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				final int gem456 = st.getInteger("gem456");
				
				switch (npc.getNpcId())
				{
					case ORIM:
						if (cond == 1)
							htmltext = "30630-09.htm";
						else if (cond == 2)
							htmltext = "30630-10.htm";
						else if (cond == 3)
							htmltext = "30630-11.htm";
						else if (cond == 4)
						{
							htmltext = "30630-14.htm";
							
							if (_drevanulPrinceZeruel_Orim == null)
								_drevanulPrinceZeruel_Orim = addSpawn(DREVANUL_PRINCE_ZERUEL, 70381, 109638, -3726, 0, false, 120000, true);
						}
						else if (cond == 5)
							htmltext = "30630-15.htm";
						else if (cond == 6)
							htmltext = "30630-17.htm";
						else if (cond == 7)
						{
							htmltext = "30630-18.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 8 || cond == 9)
							htmltext = "30630-18.htm";
						else if (cond == 10)
							htmltext = "30630-19.htm";
						break;
					
					case ALEXANDRIA:
						if (cond == 1)
							htmltext = "30098-01.htm";
						else if (cond == 2)
							htmltext = "30098-04.htm";
						else
							htmltext = "30098-05.htm";
						break;
					
					case KAIRA:
						if (player.getInventory().hasItem(AKLANTOTH_GEM_2))
							htmltext = "30476-03.htm";
						else if (cond == 2)
							htmltext = "30476-01.htm";
						else if (cond > 3)
							htmltext = "30476-04.htm";
						break;
					
					case IKER:
						if (player.getInventory().hasItem(AKLANTOTH_GEM_1))
							htmltext = "30110-06.htm";
						else if (player.getInventory().hasItem(IKER_LIST))
						{
							if (player.getInventory().getItemCount(DIRE_WYRM_FANG) + player.getInventory().getItemCount(LETO_LIZARDMAN_CHARM) + player.getInventory().getItemCount(EN_GOLEM_HEARTSTONE) < 60)
								htmltext = "30110-04.htm";
							else
							{
								htmltext = "30110-05.htm";
								takeItems(player, IKER_LIST, 1);
								takeItems(player, DIRE_WYRM_FANG, -1);
								takeItems(player, EN_GOLEM_HEARTSTONE, -1);
								takeItems(player, LETO_LIZARDMAN_CHARM, -1);
								giveItems(player, AKLANTOTH_GEM_1, 1);
								
								if (player.getInventory().hasItems(AKLANTOTH_GEM_2, AKLANTOTH_GEM_3) && gem456 == 6)
								{
									st.setCond(3);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
						}
						else if (cond == 2)
							htmltext = "30110-01.htm";
						else if (cond == 6 && !player.getInventory().hasItem(SOULTRAP_CRYSTAL))
							htmltext = "30110-07.htm";
						else if (cond >= 6 && cond < 10)
							htmltext = "30110-09.htm";
						else if (cond == 10)
							htmltext = "30110-10.htm";
						break;
					
					case LARA:
						if (player.getInventory().hasItem(AKLANTOTH_GEM_3))
							htmltext = "30063-04.htm";
						else if (player.getInventory().hasItem(LARA_MEMO))
							htmltext = "30063-03.htm";
						else if (cond == 2)
							htmltext = "30063-01.htm";
						else if (cond > 2)
							htmltext = "30063-05.htm";
						break;
					
					case RODERIK, ENDRIGO:
						if (player.getInventory().hasAtLeastOneItem(LARA_MEMO, AKLANTOTH_GEM_3))
							htmltext = npc.getNpcId() + "-01.htm";
						break;
					
					case NESTLE:
						if (gem456 == 1)
							htmltext = "30314-01.htm";
						else if (gem456 == 2)
							htmltext = "30314-03.htm";
						else if (gem456 > 2)
							htmltext = "30314-04.htm";
						break;
					
					case LEOPOLD:
						if (gem456 == 2)
							htmltext = "30435-01.htm";
						else if (gem456 > 2 && gem456 < 6)
							htmltext = "30435-03.htm";
						else if (gem456 == 6)
							htmltext = "30435-04.htm";
						else if (cond > 3)
							htmltext = "30435-05.htm";
						break;
					
					case SIR_KLAUS_VASPER:
						if (player.getInventory().hasAtLeastOneItem(SIR_VASPER_LETTER, VADIN_CRUCIFIX))
							htmltext = "30417-04.htm";
						else if (player.getInventory().hasItem(VADIN_SANCTIONS))
						{
							htmltext = "30417-05.htm";
							takeItems(player, VADIN_SANCTIONS, 1);
							giveItems(player, SWORD_OF_BINDING, 1);
							
							if (player.getInventory().hasItem(SOULTRAP_CRYSTAL))
							{
								st.setCond(7);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (cond == 6)
							htmltext = "30417-01.htm";
						else if (cond > 6)
							htmltext = "30417-06.htm";
						break;
					
					case VADIN:
						if (player.getInventory().hasItem(SIR_VASPER_LETTER))
						{
							htmltext = "30188-01.htm";
							playSound(player, SOUND_ITEMGET);
							takeItems(player, SIR_VASPER_LETTER, 1);
							giveItems(player, VADIN_CRUCIFIX, 1);
						}
						else if (player.getInventory().hasItem(VADIN_CRUCIFIX))
						{
							if (player.getInventory().getItemCount(TAMLIN_ORC_AMULET) < 20)
								htmltext = "30188-02.htm";
							else
							{
								htmltext = "30188-03.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, TAMLIN_ORC_AMULET, -1);
								takeItems(player, VADIN_CRUCIFIX, -1);
								giveItems(player, VADIN_SANCTIONS, 1);
							}
						}
						else if (player.getInventory().hasItem(VADIN_SANCTIONS))
							htmltext = "30188-04.htm";
						else if (cond > 6)
							htmltext = "30188-05.htm";
						break;
					
					case EVERT:
						if (cond == 7 || cond == 8)
							htmltext = "30633-01.htm";
						else if (cond == 9)
						{
							htmltext = "30633-02.htm";
							
							if (_drevanulPrinceZeruel_Evert == null)
								_drevanulPrinceZeruel_Evert = addSpawn(DREVANUL_PRINCE_ZERUEL, 13395, 169807, -3708, 0, false, 300000, true);
						}
						else if (cond == 10)
							htmltext = "30633-03.htm";
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
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Player player = attacker.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case NAMELESS_REVENANT:
				if (player.getInventory().hasItem(LARA_MEMO) && !npc.isScriptValue(1))
				{
					npc.setScriptValue(1);
					npc.broadcastNpcSay(NpcStringId.ID_22933);
				}
				break;
			
			case SKELETAL_MERCENARY:
				if (st.getInteger("gem456") > 2 && st.getInteger("gem456") < 6 && !npc.isScriptValue(1))
				{
					npc.setScriptValue(1);
					npc.broadcastNpcSay(NpcStringId.ID_22933);
				}
				break;
			
			case DREVANUL_PRINCE_ZERUEL:
				if (npc == _drevanulPrinceZeruel_Orim && st.getCond() == 4 && npc.isScriptValue(0))
				{
					st.setCond(5);
					playSound(player, SOUND_MIDDLE);
					
					npc.setScriptValue(1);
					npc.broadcastNpcSay(NpcStringId.ID_22934);
					
					startQuestTimer("zeruel_despawn", npc, null, 1000);
				}
				else if (npc == _drevanulPrinceZeruel_Evert && st.getCond() == 9 && npc.isScriptValue(0))
				{
					if (player.getInventory().getItemIdFrom(Paperdoll.RHAND) == SWORD_OF_BINDING)
					{
						npc.setScriptValue(player.getObjectId());
						npc.broadcastNpcSay(NpcStringId.ID_22935);
					}
				}
				break;
		}
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		if (npc == _drevanulPrinceZeruel_Orim)
		{
			_drevanulPrinceZeruel_Orim = null;
		}
		else if (npc == _drevanulPrinceZeruel_Evert)
		{
			_drevanulPrinceZeruel_Evert = null;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case DIRE_WYRM:
				if (player.getInventory().hasItem(IKER_LIST))
					dropItemsAlways(player, DIRE_WYRM_FANG, 1, 20);
				break;
			
			case ENCHANTED_STONE_GOLEM:
				if (player.getInventory().hasItem(IKER_LIST))
					dropItemsAlways(player, EN_GOLEM_HEARTSTONE, 1, 20);
				break;
			
			case LETO_LIZARDMAN, LETO_LIZARDMAN_ARCHER:
				if (player.getInventory().hasItem(IKER_LIST))
					dropItems(player, LETO_LIZARDMAN_CHARM, 1, 20, 500000);
				break;
			
			case LETO_LIZARDMAN_SOLDIER, LETO_LIZARDMAN_WARRIOR:
				if (player.getInventory().hasItem(IKER_LIST))
					dropItems(player, LETO_LIZARDMAN_CHARM, 1, 20, 600000);
				break;
			
			case LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD:
				if (player.getInventory().hasItem(IKER_LIST))
					dropItems(player, LETO_LIZARDMAN_CHARM, 1, 20, 700000);
				break;
			
			case NAMELESS_REVENANT:
				if (player.getInventory().hasItem(LARA_MEMO))
				{
					takeItems(player, LARA_MEMO, 1);
					giveItems(player, AKLANTOTH_GEM_3, 1);
					
					if (player.getInventory().hasItems(AKLANTOTH_GEM_1, AKLANTOTH_GEM_2) && st.getInteger("gem456") == 6)
					{
						st.setCond(3);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case SKELETAL_MERCENARY:
				final int gem456 = st.getInteger("gem456");
				if (gem456 == 3)
				{
					st.set("gem456", 4);
					playSound(player, SOUND_ITEMGET);
					giveItems(player, AKLANTOTH_GEM_4, 1);
				}
				else if (gem456 == 4)
				{
					st.set("gem456", 5);
					playSound(player, SOUND_ITEMGET);
					giveItems(player, AKLANTOTH_GEM_5, 1);
				}
				else if (gem456 == 5)
				{
					st.set("gem456", 6);
					takeItems(player, LEOPOLD_JOURNAL, 1);
					giveItems(player, AKLANTOTH_GEM_6, 1);
					
					if (player.getInventory().hasItems(AKLANTOTH_GEM_1, AKLANTOTH_GEM_2, AKLANTOTH_GEM_3))
					{
						st.setCond(3);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case TAMLIN_ORC, TAMLIN_ORC_ARCHER:
				if (player.getInventory().hasItem(VADIN_CRUCIFIX))
					dropItems(player, TAMLIN_ORC_AMULET, 1, 20, 500000);
				break;
			
			case DREVANUL_PRINCE_ZERUEL:
				if (npc == _drevanulPrinceZeruel_Evert && st.getCond() == 9 && npc.getScriptValue() == player.getObjectId())
				{
					st.setCond(10);
					playSound(player, SOUND_ITEMGET);
					takeItems(player, BRIMSTONE_2, 1);
					takeItems(player, SOULTRAP_CRYSTAL, 1);
					giveItems(player, PURGATORY_KEY, 1);
					giveItems(player, ZERUEL_BIND_CRYSTAL, 1);
					npc.broadcastNpcSay(NpcStringId.ID_22936);
				}
				break;
		}
	}
}