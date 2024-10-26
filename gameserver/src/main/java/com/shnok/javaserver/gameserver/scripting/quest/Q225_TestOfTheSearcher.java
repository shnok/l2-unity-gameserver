package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Q225_TestOfTheSearcher extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q225_TestOfTheSearcher";
	
	// Items
	private static final int LUTHER_LETTER = 2784;
	private static final int ALEX_WARRANT = 2785;
	private static final int LEIRYNN_ORDER_1 = 2786;
	private static final int DELU_TOTEM = 2787;
	private static final int LEIRYNN_ORDER_2 = 2788;
	private static final int CHIEF_KALKI_FANG = 2789;
	private static final int LEIRYNN_REPORT = 2790;
	private static final int STRANGE_MAP = 2791;
	private static final int LAMBERT_MAP = 2792;
	private static final int ALEX_LETTER = 2793;
	private static final int ALEX_ORDER = 2794;
	private static final int WINE_CATALOG = 2795;
	private static final int TYRA_CONTRACT = 2796;
	private static final int RED_SPORE_DUST = 2797;
	private static final int MALRUKIAN_WINE = 2798;
	private static final int OLD_ORDER = 2799;
	private static final int JAX_DIARY = 2800;
	private static final int TORN_MAP_PIECE_1 = 2801;
	private static final int TORN_MAP_PIECE_2 = 2802;
	private static final int SOLT_MAP = 2803;
	private static final int MAKEL_MAP = 2804;
	private static final int COMBINED_MAP = 2805;
	private static final int RUSTED_KEY = 2806;
	private static final int GOLD_BAR = 2807;
	private static final int ALEX_RECOMMEND = 2808;
	
	// Rewards
	private static final int MARK_OF_SEARCHER = 2809;
	
	// NPCs
	private static final int ALEX = 30291;
	private static final int TYRA = 30420;
	private static final int TREE = 30627;
	private static final int STRONG_WOODEN_CHEST = 30628;
	private static final int LUTHER = 30690;
	private static final int LEIRYNN = 30728;
	private static final int BORYS = 30729;
	private static final int JAX = 30730;
	
	// Monsters
	private static final int HANGMAN_TREE = 20144;
	private static final int ROAD_SCAVENGER = 20551;
	private static final int GIANT_FUNGUS = 20555;
	private static final int DELU_LIZARDMAN_SHAMAN = 20781;
	private static final int DELU_CHIEF_KALKIS = 27093;
	private static final int NEER_BODYGUARD = 27092;
	
	private Npc _strongWoodenChest; // Used to avoid to spawn multiple instances.
	
	public Q225_TestOfTheSearcher()
	{
		super(225, "Test of the Searcher");
		
		setItemsIds(LUTHER_LETTER, ALEX_WARRANT, LEIRYNN_ORDER_1, DELU_TOTEM, LEIRYNN_ORDER_2, CHIEF_KALKI_FANG, LEIRYNN_REPORT, STRANGE_MAP, LAMBERT_MAP, ALEX_LETTER, ALEX_ORDER, WINE_CATALOG, TYRA_CONTRACT, RED_SPORE_DUST, MALRUKIAN_WINE, OLD_ORDER, JAX_DIARY, TORN_MAP_PIECE_1, TORN_MAP_PIECE_2, SOLT_MAP, MAKEL_MAP, COMBINED_MAP, RUSTED_KEY, GOLD_BAR, ALEX_RECOMMEND);
		
		addQuestStart(LUTHER);
		addTalkId(ALEX, TYRA, TREE, STRONG_WOODEN_CHEST, LUTHER, LEIRYNN, BORYS, JAX);
		
		addAttacked(DELU_LIZARDMAN_SHAMAN);
		addDecayed(STRONG_WOODEN_CHEST);
		addMyDying(HANGMAN_TREE, ROAD_SCAVENGER, GIANT_FUNGUS, DELU_LIZARDMAN_SHAMAN, DELU_CHIEF_KALKIS, NEER_BODYGUARD);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// LUTHER
		if (event.equalsIgnoreCase("30690-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, LUTHER_LETTER, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30690-05a.htm";
		}
		// ALEX
		else if (event.equalsIgnoreCase("30291-07.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LEIRYNN_REPORT, 1);
			takeItems(player, STRANGE_MAP, 1);
			giveItems(player, ALEX_LETTER, 1);
			giveItems(player, ALEX_ORDER, 1);
			giveItems(player, LAMBERT_MAP, 1);
		}
		// TYRA
		else if (event.equalsIgnoreCase("30420-01a.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, WINE_CATALOG, 1);
			giveItems(player, TYRA_CONTRACT, 1);
		}
		// JAX
		else if (event.equalsIgnoreCase("30730-01d.htm"))
		{
			st.setCond(14);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, OLD_ORDER, 1);
			giveItems(player, JAX_DIARY, 1);
		}
		// TREE
		else if (event.equalsIgnoreCase("30627-01a.htm"))
		{
			if (_strongWoodenChest == null)
			{
				if (st.getCond() == 16)
				{
					st.setCond(17);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, RUSTED_KEY, 1);
				}
				
				_strongWoodenChest = addSpawn(STRONG_WOODEN_CHEST, 10098, 157287, -2406, 300000, false, 0, true);
			}
		}
		// STRONG WOODEN CHEST
		else if (event.equalsIgnoreCase("30628-01a.htm"))
		{
			if (!player.getInventory().hasItem(RUSTED_KEY))
				htmltext = "30628-02.htm";
			else
			{
				st.setCond(18);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, RUSTED_KEY, -1);
				giveItems(player, GOLD_BAR, 20);
				
				_strongWoodenChest.deleteMe();
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
				if (player.getClassId() != ClassId.ROGUE && player.getClassId() != ClassId.ELVEN_SCOUT && player.getClassId() != ClassId.ASSASSIN && player.getClassId() != ClassId.SCAVENGER)
					htmltext = "30690-01.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30690-02.htm";
				else
					htmltext = (player.getClassId() == ClassId.SCAVENGER) ? "30690-04.htm" : "30690-03.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case LUTHER:
						if (cond == 1)
							htmltext = "30690-06.htm";
						else if (cond > 1 && cond < 19)
							htmltext = "30690-07.htm";
						else if (cond == 19)
						{
							htmltext = "30690-08.htm";
							takeItems(player, ALEX_RECOMMEND, 1);
							giveItems(player, MARK_OF_SEARCHER, 1);
							rewardExpAndSp(player, 37831, 18750);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ALEX:
						if (cond == 1)
						{
							htmltext = "30291-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LUTHER_LETTER, 1);
							giveItems(player, ALEX_WARRANT, 1);
						}
						else if (cond == 2)
							htmltext = "30291-02.htm";
						else if (cond > 2 && cond < 7)
							htmltext = "30291-03.htm";
						else if (cond == 7)
							htmltext = "30291-04.htm";
						else if (cond > 7 && cond < 13)
							htmltext = "30291-08.htm";
						else if (cond > 12 && cond < 16)
							htmltext = "30291-09.htm";
						else if (cond > 15 && cond < 18)
							htmltext = "30291-10.htm";
						else if (cond == 18)
						{
							htmltext = "30291-11.htm";
							st.setCond(19);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ALEX_ORDER, 1);
							takeItems(player, COMBINED_MAP, 1);
							takeItems(player, GOLD_BAR, -1);
							giveItems(player, ALEX_RECOMMEND, 1);
						}
						else if (cond == 19)
							htmltext = "30291-12.htm";
						break;
					
					case LEIRYNN:
						if (cond == 2)
						{
							htmltext = "30728-01.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ALEX_WARRANT, 1);
							giveItems(player, LEIRYNN_ORDER_1, 1);
						}
						else if (cond == 3)
							htmltext = "30728-02.htm";
						else if (cond == 4)
						{
							htmltext = "30728-03.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, DELU_TOTEM, -1);
							takeItems(player, LEIRYNN_ORDER_1, 1);
							giveItems(player, LEIRYNN_ORDER_2, 1);
						}
						else if (cond == 5)
							htmltext = "30728-04.htm";
						else if (cond == 6)
						{
							htmltext = "30728-05.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CHIEF_KALKI_FANG, 1);
							takeItems(player, LEIRYNN_ORDER_2, 1);
							giveItems(player, LEIRYNN_REPORT, 1);
						}
						else if (cond == 7)
							htmltext = "30728-06.htm";
						else if (cond > 7)
							htmltext = "30728-07.htm";
						break;
					
					case BORYS:
						if (cond == 8)
						{
							htmltext = "30729-01.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ALEX_LETTER, 1);
							giveItems(player, WINE_CATALOG, 1);
						}
						else if (cond > 8 && cond < 12)
							htmltext = "30729-02.htm";
						else if (cond == 12)
						{
							htmltext = "30729-03.htm";
							st.setCond(13);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, MALRUKIAN_WINE, 1);
							takeItems(player, WINE_CATALOG, 1);
							giveItems(player, OLD_ORDER, 1);
						}
						else if (cond == 13)
							htmltext = "30729-04.htm";
						else if (cond > 13)
							htmltext = "30729-05.htm";
						break;
					
					case TYRA:
						if (cond == 9)
							htmltext = "30420-01.htm";
						else if (cond == 10)
							htmltext = "30420-02.htm";
						else if (cond == 11)
						{
							htmltext = "30420-03.htm";
							st.setCond(12);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, RED_SPORE_DUST, -1);
							takeItems(player, TYRA_CONTRACT, 1);
							giveItems(player, MALRUKIAN_WINE, 1);
						}
						else if (cond > 11)
							htmltext = "30420-04.htm";
						break;
					
					case JAX:
						if (cond == 13)
							htmltext = "30730-01.htm";
						else if (cond == 14)
							htmltext = "30730-02.htm";
						else if (cond == 15)
						{
							htmltext = "30730-03.htm";
							st.setCond(16);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, LAMBERT_MAP, 1);
							takeItems(player, MAKEL_MAP, 1);
							takeItems(player, JAX_DIARY, 1);
							takeItems(player, SOLT_MAP, 1);
							giveItems(player, COMBINED_MAP, 1);
						}
						else if (cond > 15)
							htmltext = "30730-04.htm";
						break;
					
					case TREE:
						if (cond == 16 || cond == 17)
							htmltext = "30627-01.htm";
						break;
					
					case STRONG_WOODEN_CHEST:
						if (cond == 17)
							htmltext = "30628-01.htm";
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
		
		if (player.getInventory().hasItem(LEIRYNN_ORDER_1) && !npc.isScriptValue(1))
		{
			npc.setScriptValue(1);
			addSpawn(NEER_BODYGUARD, npc, false, 200000, true);
		}
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		if (npc == _strongWoodenChest)
		{
			_strongWoodenChest = null;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		switch (npc.getNpcId())
		{
			case DELU_LIZARDMAN_SHAMAN:
				QuestState st = checkPlayerCondition(player, npc, 3);
				if (st == null)
					return;
				
				if (dropItemsAlways(player, DELU_TOTEM, 1, 10))
					st.setCond(4);
				break;
			
			case DELU_CHIEF_KALKIS:
				st = checkPlayerCondition(player, npc, 5);
				if (st == null)
					return;
				
				st.setCond(6);
				playSound(player, SOUND_MIDDLE);
				giveItems(player, CHIEF_KALKI_FANG, 1);
				giveItems(player, STRANGE_MAP, 1);
				break;
			
			case GIANT_FUNGUS:
				st = checkPlayerCondition(player, npc, 10);
				if (st == null)
					return;
				
				if (dropItemsAlways(player, RED_SPORE_DUST, 1, 10))
					st.setCond(11);
				break;
			
			case ROAD_SCAVENGER:
				st = checkPlayerCondition(player, npc, 14);
				if (st == null)
					return;
				
				if (!player.getInventory().hasItem(SOLT_MAP) && dropItems(player, TORN_MAP_PIECE_1, 1, 4, 500000))
				{
					takeItems(player, TORN_MAP_PIECE_1, -1);
					giveItems(player, SOLT_MAP, 1);
					
					if (player.getInventory().hasItem(MAKEL_MAP))
						st.setCond(15);
				}
				break;
			
			case HANGMAN_TREE:
				st = checkPlayerCondition(player, npc, 14);
				if (st == null)
					return;
				
				if (!player.getInventory().hasItem(MAKEL_MAP) && dropItems(player, TORN_MAP_PIECE_2, 1, 4, 500000))
				{
					takeItems(player, TORN_MAP_PIECE_2, -1);
					giveItems(player, MAKEL_MAP, 1);
					
					if (player.getInventory().hasItem(SOLT_MAP))
						st.setCond(15);
				}
				break;
		}
	}
}