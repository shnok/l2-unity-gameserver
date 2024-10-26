package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Q227_TestOfTheReformer extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q227_TestOfTheReformer";
	
	// Items
	private static final int BOOK_OF_REFORM = 2822;
	private static final int LETTER_OF_INTRODUCTION = 2823;
	private static final int SLA_LETTER = 2824;
	private static final int GREETINGS = 2825;
	private static final int OL_MAHUM_MONEY = 2826;
	private static final int KATARI_LETTER = 2827;
	private static final int NYAKURI_LETTER = 2828;
	private static final int UNDEAD_LIST = 2829;
	private static final int RAMUS_LETTER = 2830;
	private static final int RIPPED_DIARY = 2831;
	private static final int HUGE_NAIL = 2832;
	private static final int LETTER_OF_BETRAYER = 2833;
	private static final int BONE_FRAGMENT_4 = 2834;
	private static final int BONE_FRAGMENT_5 = 2835;
	private static final int BONE_FRAGMENT_6 = 2836;
	private static final int BONE_FRAGMENT_7 = 2837;
	private static final int BONE_FRAGMENT_8 = 2838;
	private static final int BONE_FRAGMENT_9 = 2839;
	private static final int KAKAN_LETTER = 3037;
	
	// Rewards
	private static final int MARK_OF_REFORMER = 2821;
	
	// NPCs
	private static final int PUPINA = 30118;
	private static final int SLA = 30666;
	private static final int RAMUS = 30667;
	private static final int KATARI = 30668;
	private static final int KAKAN = 30669;
	private static final int NYAKURI = 30670;
	private static final int OL_MAHUM_PILGRIM = 30732;
	
	// Monsters
	private static final int MISERY_SKELETON = 20022;
	private static final int SKELETON_ARCHER = 20100;
	private static final int SKELETON_MARKSMAN = 20102;
	private static final int SKELETON_LORD = 20104;
	private static final int SILENT_HORROR = 20404;
	private static final int NAMELESS_REVENANT = 27099;
	private static final int ARURAUNE = 27128;
	private static final int OL_MAHUM_INSPECTOR = 27129;
	private static final int OL_MAHUM_BETRAYER = 27130;
	private static final int CRIMSON_WEREWOLF = 27131;
	private static final int KRUDEL_LIZARDMAN = 27132;
	
	// Instances
	private Npc _olMahumPilgrim_Katari;
	private Npc _olMahumInspector;
	private Npc _olMahumBetrayer;
	private Npc _olMahumPilgrim_Kakan;
	private Npc _crimsonWerewolf;
	private Npc _olMahumPilgrim_Nyakuri;
	private Npc _krudelLizardman;
	
	// Allowed skills when attacking Crimson Werewolf
	private static final int[] ALLOWED_SKILLS =
	{
		1031,
		1069,
		1164,
		1168,
		1147,
		1177,
		1184,
		1201,
		1206
	};
	
	public Q227_TestOfTheReformer()
	{
		super(227, "Test Of The Reformer");
		
		setItemsIds(BOOK_OF_REFORM, LETTER_OF_INTRODUCTION, SLA_LETTER, GREETINGS, OL_MAHUM_MONEY, KATARI_LETTER, NYAKURI_LETTER, UNDEAD_LIST, RAMUS_LETTER, RIPPED_DIARY, HUGE_NAIL, LETTER_OF_BETRAYER, BONE_FRAGMENT_4, BONE_FRAGMENT_5, BONE_FRAGMENT_6, BONE_FRAGMENT_7, BONE_FRAGMENT_8, BONE_FRAGMENT_9, KAKAN_LETTER);
		
		addQuestStart(PUPINA);
		addTalkId(PUPINA, SLA, RAMUS, KATARI, KAKAN, NYAKURI, OL_MAHUM_PILGRIM);
		
		addAttacked(NAMELESS_REVENANT, CRIMSON_WEREWOLF, OL_MAHUM_PILGRIM);
		addDecayed(OL_MAHUM_PILGRIM, OL_MAHUM_INSPECTOR, OL_MAHUM_BETRAYER, CRIMSON_WEREWOLF, KRUDEL_LIZARDMAN);
		addMyDying(MISERY_SKELETON, SKELETON_ARCHER, SKELETON_MARKSMAN, SKELETON_LORD, SILENT_HORROR, NAMELESS_REVENANT, ARURAUNE, OL_MAHUM_INSPECTOR, OL_MAHUM_BETRAYER, CRIMSON_WEREWOLF, KRUDEL_LIZARDMAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// PUPINA
		if (event.equalsIgnoreCase("30118-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, BOOK_OF_REFORM, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30118-04b.htm";
		}
		else if (event.equalsIgnoreCase("30118-06.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BOOK_OF_REFORM, 1);
			takeItems(player, HUGE_NAIL, 1);
			giveItems(player, LETTER_OF_INTRODUCTION, 1);
		}
		// SLA
		else if (event.equalsIgnoreCase("30666-04.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LETTER_OF_INTRODUCTION, 1);
			giveItems(player, SLA_LETTER, 1);
		}
		// KAKAN
		else if (event.equalsIgnoreCase("30669-03.htm"))
		{
			if (st.getCond() != 12)
			{
				st.setCond(12);
				playSound(player, SOUND_MIDDLE);
			}
			
			if (_olMahumPilgrim_Kakan == null)
				_olMahumPilgrim_Kakan = addSpawn(OL_MAHUM_PILGRIM, -9282, -89975, -2331, 0, false, 300000, true);
			
			if (_crimsonWerewolf == null)
				_crimsonWerewolf = addSpawn(CRIMSON_WEREWOLF, -9382, -89852, -2333, 0, false, 300000, true);
			
			_crimsonWerewolf.getAI().addAttackDesire(_olMahumPilgrim_Kakan, 2000);
		}
		// NYAKURI
		else if (event.equalsIgnoreCase("30670-03.htm"))
		{
			st.setCond(15);
			playSound(player, SOUND_MIDDLE);
			
			if (_olMahumPilgrim_Nyakuri == null)
				_olMahumPilgrim_Nyakuri = addSpawn(OL_MAHUM_PILGRIM, 125947, -180049, -1778, 0, false, 300000, true);
			
			if (_krudelLizardman == null)
				_krudelLizardman = addSpawn(KRUDEL_LIZARDMAN, 126019, -179983, -1781, 0, false, 300000, true);
			
			_krudelLizardman.getAI().addAttackDesire(_olMahumPilgrim_Nyakuri, 2000);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		// Despawns Crimson Werewolf
		if (name.equalsIgnoreCase("werewolf_despawn"))
		{
			if (npc == _crimsonWerewolf)
			{
				npc.broadcastNpcSay(NpcStringId.ID_22720);
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
				if (player.getClassId() == ClassId.CLERIC || player.getClassId() == ClassId.SHILLIEN_ORACLE)
					htmltext = (player.getStatus().getLevel() < 39) ? "30118-01.htm" : "30118-03.htm";
				else
					htmltext = "30118-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case PUPINA:
						if (cond < 3)
							htmltext = "30118-04a.htm";
						else if (cond == 3)
							htmltext = "30118-05.htm";
						else if (cond > 3)
							htmltext = "30118-07.htm";
						break;
					
					case SLA:
						if (cond == 4)
							htmltext = "30666-01.htm";
						else if (cond > 4 && cond < 10)
							htmltext = "30666-05.htm";
						else if (cond == 10)
						{
							htmltext = "30666-06.htm";
							st.setCond(11);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, OL_MAHUM_MONEY, 1);
							giveItems(player, GREETINGS, 3);
						}
						else if (cond > 10 && cond < 20)
							htmltext = "30666-06.htm";
						else if (cond == 20)
						{
							htmltext = "30666-07.htm";
							takeItems(player, KATARI_LETTER, 1);
							takeItems(player, KAKAN_LETTER, 1);
							takeItems(player, NYAKURI_LETTER, 1);
							takeItems(player, RAMUS_LETTER, 1);
							giveItems(player, MARK_OF_REFORMER, 1);
							rewardExpAndSp(player, 164032, 17500);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case KATARI:
						if (cond == 5 || cond == 6 || cond == 7)
						{
							htmltext = "30668-01.htm";
							
							if (cond == 5)
							{
								st.setCond(6);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, SLA_LETTER, 1);
							}
							
							if (_olMahumPilgrim_Katari == null)
								_olMahumPilgrim_Katari = addSpawn(OL_MAHUM_PILGRIM, -4015, 40141, -3664, 0, false, 300000, true);
							
							if (_olMahumInspector == null)
								_olMahumInspector = addSpawn(OL_MAHUM_INSPECTOR, -4034, 40201, -3665, 0, false, 300000, true);
							
							_olMahumInspector.getAI().addAttackDesire(_olMahumPilgrim_Katari, 2000);
						}
						else if (cond == 8)
						{
							htmltext = "30668-02.htm";
							
							if (_olMahumBetrayer == null)
							{
								_olMahumBetrayer = addSpawn(OL_MAHUM_BETRAYER, -4106, 40174, -3660, 0, false, 60000, true);
								_olMahumBetrayer.forceRunStance();
								_olMahumBetrayer.getAI().addMoveToDesire(new Location(-7732, 36787, -3709), 10000);
							}
						}
						else if (cond == 9)
						{
							htmltext = "30668-03.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LETTER_OF_BETRAYER, 1);
							giveItems(player, KATARI_LETTER, 1);
						}
						else if (cond > 9)
							htmltext = "30668-04.htm";
						break;
					
					case OL_MAHUM_PILGRIM:
						if (cond == 7)
						{
							htmltext = "30732-01.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, OL_MAHUM_MONEY, 1);
						}
						break;
					
					case KAKAN:
						if (cond == 11 || cond == 12)
							htmltext = "30669-01.htm";
						else if (cond == 13)
						{
							htmltext = "30669-04.htm";
							st.setCond(14);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GREETINGS, 1);
							giveItems(player, KAKAN_LETTER, 1);
						}
						else if (cond > 13)
							htmltext = "30669-04.htm";
						break;
					
					case NYAKURI:
						if (cond == 14 || cond == 15)
							htmltext = "30670-01.htm";
						else if (cond == 16)
						{
							htmltext = "30670-04.htm";
							st.setCond(17);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GREETINGS, 1);
							giveItems(player, NYAKURI_LETTER, 1);
						}
						else if (cond > 16)
							htmltext = "30670-04.htm";
						break;
					
					case RAMUS:
						if (cond == 17)
						{
							htmltext = "30667-01.htm";
							st.setCond(18);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GREETINGS, 1);
							giveItems(player, UNDEAD_LIST, 1);
						}
						else if (cond == 18)
							htmltext = "30667-02.htm";
						else if (cond == 19)
						{
							htmltext = "30667-03.htm";
							st.setCond(20);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BONE_FRAGMENT_4, 1);
							takeItems(player, BONE_FRAGMENT_5, 1);
							takeItems(player, BONE_FRAGMENT_6, 1);
							takeItems(player, BONE_FRAGMENT_7, 1);
							takeItems(player, BONE_FRAGMENT_8, 1);
							takeItems(player, UNDEAD_LIST, 1);
							giveItems(player, RAMUS_LETTER, 1);
						}
						else if (cond > 19)
							htmltext = "30667-03.htm";
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
		switch (npc.getNpcId())
		{
			case CRIMSON_WEREWOLF, KRUDEL_LIZARDMAN, OL_MAHUM_PILGRIM:
				npc.getAI().addAttackDesire(attacker, 2000);
				break;
		}
		
		final Player player = attacker.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int cond = st.getCond();
		switch (npc.getNpcId())
		{
			case NAMELESS_REVENANT:
				if ((cond == 1 || cond == 2) && skill != null && skill.getId() == 1031)
					npc.setScriptValue(1);
				break;
			
			case CRIMSON_WEREWOLF:
				if (cond == 12 && npc.isScriptValue(0) && (skill == null || !ArraysUtil.contains(ALLOWED_SKILLS, skill.getId())))
				{
					npc.setScriptValue(1);
					startQuestTimer("werewolf_despawn", npc, null, 1000);
				}
				break;
		}
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		if (npc == _olMahumPilgrim_Katari)
		{
			_olMahumPilgrim_Katari = null;
		}
		else if (npc == _olMahumInspector)
		{
			_olMahumInspector = null;
		}
		else if (npc == _olMahumBetrayer)
		{
			_olMahumBetrayer = null;
		}
		else if (npc == _olMahumPilgrim_Kakan)
		{
			_olMahumPilgrim_Kakan = null;
		}
		else if (npc == _crimsonWerewolf)
		{
			_crimsonWerewolf = null;
		}
		else if (npc == _olMahumPilgrim_Nyakuri)
		{
			_olMahumPilgrim_Nyakuri = null;
		}
		else if (npc == _krudelLizardman)
		{
			_krudelLizardman = null;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		int cond = st.getCond();
		switch (npc.getNpcId())
		{
			case NAMELESS_REVENANT:
				if ((cond == 1 || cond == 2) && npc.isScriptValue(1) && dropItemsAlways(player, RIPPED_DIARY, 1, 7))
				{
					st.setCond(2);
					takeItems(player, RIPPED_DIARY, -1);
					addSpawn(ARURAUNE, npc, false, 300000, true);
				}
				break;
			
			case ARURAUNE:
				if (cond == 2)
				{
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, HUGE_NAIL, 1);
					npc.broadcastNpcSay(NpcStringId.ID_22719);
				}
				break;
			
			case OL_MAHUM_INSPECTOR:
				if (cond == 6)
				{
					st.setCond(7);
					playSound(player, SOUND_MIDDLE);
				}
				break;
			
			case OL_MAHUM_BETRAYER:
				if (cond == 8)
				{
					st.setCond(9);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, LETTER_OF_BETRAYER, 1);
				}
				break;
			
			case CRIMSON_WEREWOLF:
				if (cond == 12 && npc.isScriptValue(0))
				{
					st.setCond(13);
					playSound(player, SOUND_MIDDLE);
				}
				break;
			
			case KRUDEL_LIZARDMAN:
				if (cond == 15)
				{
					st.setCond(16);
					playSound(player, SOUND_MIDDLE);
				}
				break;
			
			case SILENT_HORROR:
				if (cond == 18 && !player.getInventory().hasItem(BONE_FRAGMENT_4))
				{
					giveItems(player, BONE_FRAGMENT_4, 1);
					if (player.getInventory().hasItems(BONE_FRAGMENT_5, BONE_FRAGMENT_6, BONE_FRAGMENT_7, BONE_FRAGMENT_8))
					{
						st.setCond(19);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case SKELETON_LORD:
				if (cond == 18 && !player.getInventory().hasItem(BONE_FRAGMENT_5))
				{
					giveItems(player, BONE_FRAGMENT_5, 1);
					if (player.getInventory().hasItems(BONE_FRAGMENT_4, BONE_FRAGMENT_6, BONE_FRAGMENT_7, BONE_FRAGMENT_8))
					{
						st.setCond(19);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case SKELETON_MARKSMAN:
				if (cond == 18 && !player.getInventory().hasItem(BONE_FRAGMENT_6))
				{
					giveItems(player, BONE_FRAGMENT_6, 1);
					if (player.getInventory().hasItems(BONE_FRAGMENT_4, BONE_FRAGMENT_5, BONE_FRAGMENT_7, BONE_FRAGMENT_8))
					{
						st.setCond(19);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case MISERY_SKELETON:
				if (cond == 18 && !player.getInventory().hasItem(BONE_FRAGMENT_7))
				{
					giveItems(player, BONE_FRAGMENT_7, 1);
					if (player.getInventory().hasItems(BONE_FRAGMENT_4, BONE_FRAGMENT_5, BONE_FRAGMENT_6, BONE_FRAGMENT_8))
					{
						st.setCond(19);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case SKELETON_ARCHER:
				if (cond == 18 && !player.getInventory().hasItem(BONE_FRAGMENT_8))
				{
					giveItems(player, BONE_FRAGMENT_8, 1);
					if (player.getInventory().hasItems(BONE_FRAGMENT_4, BONE_FRAGMENT_5, BONE_FRAGMENT_6, BONE_FRAGMENT_7))
					{
						st.setCond(19);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
		}
	}
}