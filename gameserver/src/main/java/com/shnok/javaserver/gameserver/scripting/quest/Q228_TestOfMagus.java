package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q228_TestOfMagus extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q228_TestOfMagus";
	
	// Items
	private static final int RUKAL_LETTER = 2841;
	private static final int PARINA_LETTER = 2842;
	private static final int LILAC_CHARM = 2843;
	private static final int GOLDEN_SEED_1 = 2844;
	private static final int GOLDEN_SEED_2 = 2845;
	private static final int GOLDEN_SEED_3 = 2846;
	private static final int SCORE_OF_ELEMENTS = 2847;
	private static final int DAZZLING_DROP = 2848;
	private static final int FLAME_CRYSTAL = 2849;
	private static final int HARPY_FEATHER = 2850;
	private static final int WYRM_WINGBONE = 2851;
	private static final int WINDSUS_MANE = 2852;
	private static final int EN_MONSTEREYE_SHELL = 2853;
	private static final int EN_STONEGOLEM_POWDER = 2854;
	private static final int EN_IRONGOLEM_SCRAP = 2855;
	private static final int TONE_OF_WATER = 2856;
	private static final int TONE_OF_FIRE = 2857;
	private static final int TONE_OF_WIND = 2858;
	private static final int TONE_OF_EARTH = 2859;
	private static final int SALAMANDER_CHARM = 2860;
	private static final int SYLPH_CHARM = 2861;
	private static final int UNDINE_CHARM = 2862;
	private static final int SERPENT_CHARM = 2863;
	
	// Rewards
	private static final int MARK_OF_MAGUS = 2840;
	
	// NPCs
	private static final int PARINA = 30391;
	private static final int EARTH_SNAKE = 30409;
	private static final int FLAME_SALAMANDER = 30411;
	private static final int WIND_SYLPH = 30412;
	private static final int WATER_UNDINE = 30413;
	private static final int CASIAN = 30612;
	private static final int RUKAL = 30629;
	
	// Monsters
	private static final int HARPY = 20145;
	private static final int MARSH_STAKATO = 20157;
	private static final int WYRM = 20176;
	private static final int MARSH_STAKATO_WORKER = 20230;
	private static final int TOAD_LORD = 20231;
	private static final int MARSH_STAKATO_SOLDIER = 20232;
	private static final int MARSH_STAKATO_DRONE = 20234;
	private static final int WINDSUS = 20553;
	private static final int ENCHANTED_MONSTEREYE = 20564;
	private static final int ENCHANTED_STONE_GOLEM = 20565;
	private static final int ENCHANTED_IRON_GOLEM = 20566;
	private static final int SINGING_FLOWER_PHANTASM = 27095;
	private static final int SINGING_FLOWER_NIGHTMARE = 27096;
	private static final int SINGING_FLOWER_DARKLING = 27097;
	private static final int GHOST_FIRE = 27098;
	
	public Q228_TestOfMagus()
	{
		super(228, "Test Of Magus");
		
		setItemsIds(RUKAL_LETTER, PARINA_LETTER, LILAC_CHARM, GOLDEN_SEED_1, GOLDEN_SEED_2, GOLDEN_SEED_3, SCORE_OF_ELEMENTS, DAZZLING_DROP, FLAME_CRYSTAL, HARPY_FEATHER, WYRM_WINGBONE, WINDSUS_MANE, EN_MONSTEREYE_SHELL, EN_STONEGOLEM_POWDER, EN_IRONGOLEM_SCRAP, TONE_OF_WATER, TONE_OF_FIRE, TONE_OF_WIND, TONE_OF_EARTH, SALAMANDER_CHARM, SYLPH_CHARM, UNDINE_CHARM, SERPENT_CHARM);
		
		addQuestStart(RUKAL);
		addTalkId(PARINA, EARTH_SNAKE, FLAME_SALAMANDER, WIND_SYLPH, WATER_UNDINE, CASIAN, RUKAL);
		
		addMyDying(HARPY, MARSH_STAKATO, WYRM, MARSH_STAKATO_WORKER, TOAD_LORD, MARSH_STAKATO_SOLDIER, MARSH_STAKATO_DRONE, WINDSUS, ENCHANTED_MONSTEREYE, ENCHANTED_STONE_GOLEM, ENCHANTED_IRON_GOLEM, SINGING_FLOWER_PHANTASM, SINGING_FLOWER_NIGHTMARE, SINGING_FLOWER_DARKLING, GHOST_FIRE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// RUKAL
		if (event.equalsIgnoreCase("30629-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, RUKAL_LETTER, 1);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30629-04a.htm";
		}
		else if (event.equalsIgnoreCase("30629-10.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, GOLDEN_SEED_1, 1);
			takeItems(player, GOLDEN_SEED_2, 1);
			takeItems(player, GOLDEN_SEED_3, 1);
			takeItems(player, LILAC_CHARM, 1);
			giveItems(player, SCORE_OF_ELEMENTS, 1);
		}
		// PARINA
		else if (event.equalsIgnoreCase("30391-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, RUKAL_LETTER, 1);
			giveItems(player, PARINA_LETTER, 1);
		}
		// CASIAN
		else if (event.equalsIgnoreCase("30612-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, PARINA_LETTER, 1);
			giveItems(player, LILAC_CHARM, 1);
		}
		// WIND SYLPH
		else if (event.equalsIgnoreCase("30412-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, SYLPH_CHARM, 1);
		}
		// EARTH SNAKE
		else if (event.equalsIgnoreCase("30409-03.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, SERPENT_CHARM, 1);
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
				if (player.getClassId() != ClassId.HUMAN_WIZARD && player.getClassId() != ClassId.ELVEN_WIZARD && player.getClassId() != ClassId.DARK_WIZARD)
					htmltext = "30629-01.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30629-02.htm";
				else
					htmltext = "30629-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case RUKAL:
						if (cond == 1)
							htmltext = "30629-05.htm";
						else if (cond == 2)
							htmltext = "30629-06.htm";
						else if (cond == 3)
							htmltext = "30629-07.htm";
						else if (cond == 4)
							htmltext = "30629-08.htm";
						else if (cond == 5)
							htmltext = "30629-11.htm";
						else if (cond == 6)
						{
							htmltext = "30629-12.htm";
							takeItems(player, SCORE_OF_ELEMENTS, 1);
							takeItems(player, TONE_OF_EARTH, 1);
							takeItems(player, TONE_OF_FIRE, 1);
							takeItems(player, TONE_OF_WATER, 1);
							takeItems(player, TONE_OF_WIND, 1);
							giveItems(player, MARK_OF_MAGUS, 1);
							rewardExpAndSp(player, 139039, 40000);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case PARINA:
						if (cond == 1)
							htmltext = "30391-01.htm";
						else if (cond == 2)
							htmltext = "30391-03.htm";
						else if (cond == 3 || cond == 4)
							htmltext = "30391-04.htm";
						else if (cond > 4)
							htmltext = "30391-05.htm";
						break;
					
					case CASIAN:
						if (cond == 2)
							htmltext = "30612-01.htm";
						else if (cond == 3)
							htmltext = "30612-03.htm";
						else if (cond == 4)
							htmltext = "30612-04.htm";
						else if (cond > 4)
							htmltext = "30612-05.htm";
						break;
					
					case WATER_UNDINE:
						if (cond == 5)
						{
							if (player.getInventory().hasItem(UNDINE_CHARM))
							{
								if (player.getInventory().getItemCount(DAZZLING_DROP) < 20)
									htmltext = "30413-02.htm";
								else
								{
									htmltext = "30413-03.htm";
									takeItems(player, DAZZLING_DROP, 20);
									takeItems(player, UNDINE_CHARM, 1);
									giveItems(player, TONE_OF_WATER, 1);
									
									if (player.getInventory().hasItems(TONE_OF_FIRE, TONE_OF_WIND, TONE_OF_EARTH))
									{
										st.setCond(6);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
							}
							else if (!player.getInventory().hasItem(TONE_OF_WATER))
							{
								htmltext = "30413-01.htm";
								playSound(player, SOUND_ITEMGET);
								giveItems(player, UNDINE_CHARM, 1);
							}
							else
								htmltext = "30413-04.htm";
						}
						else if (cond == 6)
							htmltext = "30413-04.htm";
						break;
					
					case FLAME_SALAMANDER:
						if (cond == 5)
						{
							if (player.getInventory().hasItem(SALAMANDER_CHARM))
							{
								if (player.getInventory().getItemCount(FLAME_CRYSTAL) < 5)
									htmltext = "30411-02.htm";
								else
								{
									htmltext = "30411-03.htm";
									takeItems(player, FLAME_CRYSTAL, 5);
									takeItems(player, SALAMANDER_CHARM, 1);
									giveItems(player, TONE_OF_FIRE, 1);
									
									if (player.getInventory().hasItems(TONE_OF_WATER, TONE_OF_WIND, TONE_OF_EARTH))
									{
										st.setCond(6);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
							}
							else if (!player.getInventory().hasItem(TONE_OF_FIRE))
							{
								htmltext = "30411-01.htm";
								giveItems(player, SALAMANDER_CHARM, 1);
							}
							else
								htmltext = "30411-04.htm";
						}
						else if (cond == 6)
							htmltext = "30411-04.htm";
						break;
					
					case WIND_SYLPH:
						if (cond == 5)
						{
							if (player.getInventory().hasItem(SYLPH_CHARM))
							{
								if (player.getInventory().getItemCount(HARPY_FEATHER) + player.getInventory().getItemCount(WYRM_WINGBONE) + player.getInventory().getItemCount(WINDSUS_MANE) < 40)
									htmltext = "30412-03.htm";
								else
								{
									htmltext = "30412-04.htm";
									takeItems(player, HARPY_FEATHER, 20);
									takeItems(player, SYLPH_CHARM, 1);
									takeItems(player, WINDSUS_MANE, 10);
									takeItems(player, WYRM_WINGBONE, 10);
									giveItems(player, TONE_OF_WIND, 1);
									
									if (player.getInventory().hasItems(TONE_OF_WATER, TONE_OF_FIRE, TONE_OF_EARTH))
									{
										st.setCond(6);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
							}
							else if (!player.getInventory().hasItem(TONE_OF_WIND))
								htmltext = "30412-01.htm";
							else
								htmltext = "30412-05.htm";
						}
						else if (cond == 6)
							htmltext = "30412-05.htm";
						break;
					
					case EARTH_SNAKE:
						if (cond == 5)
						{
							if (player.getInventory().hasItem(SERPENT_CHARM))
							{
								if (player.getInventory().getItemCount(EN_MONSTEREYE_SHELL) + player.getInventory().getItemCount(EN_STONEGOLEM_POWDER) + player.getInventory().getItemCount(EN_IRONGOLEM_SCRAP) < 30)
									htmltext = "30409-04.htm";
								else
								{
									htmltext = "30409-05.htm";
									takeItems(player, EN_IRONGOLEM_SCRAP, 10);
									takeItems(player, EN_MONSTEREYE_SHELL, 10);
									takeItems(player, EN_STONEGOLEM_POWDER, 10);
									takeItems(player, SERPENT_CHARM, 1);
									giveItems(player, TONE_OF_EARTH, 1);
									
									if (player.getInventory().hasItems(TONE_OF_WATER, TONE_OF_FIRE, TONE_OF_WIND))
									{
										st.setCond(6);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
							}
							else if (!player.getInventory().hasItem(TONE_OF_EARTH))
								htmltext = "30409-01.htm";
							else
								htmltext = "30409-06.htm";
						}
						else if (cond == 6)
							htmltext = "30409-06.htm";
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
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int cond = st.getCond();
		
		if (cond == 3)
		{
			switch (npc.getNpcId())
			{
				case SINGING_FLOWER_PHANTASM:
					if (!player.getInventory().hasItem(GOLDEN_SEED_1))
					{
						npc.broadcastNpcSay(NpcStringId.ID_22819);
						dropItemsAlways(player, GOLDEN_SEED_1, 1, 1);
						if (player.getInventory().hasItems(GOLDEN_SEED_2, GOLDEN_SEED_3))
							st.setCond(4);
					}
					break;
				
				case SINGING_FLOWER_NIGHTMARE:
					if (!player.getInventory().hasItem(GOLDEN_SEED_2))
					{
						npc.broadcastNpcSay(NpcStringId.ID_22820);
						dropItemsAlways(player, GOLDEN_SEED_2, 1, 1);
						if (player.getInventory().hasItems(GOLDEN_SEED_1, GOLDEN_SEED_3))
							st.setCond(4);
					}
					break;
				
				case SINGING_FLOWER_DARKLING:
					if (!player.getInventory().hasItem(GOLDEN_SEED_3))
					{
						npc.broadcastNpcSay(NpcStringId.ID_22821);
						dropItemsAlways(player, GOLDEN_SEED_3, 1, 1);
						if (player.getInventory().hasItems(GOLDEN_SEED_1, GOLDEN_SEED_2))
							st.setCond(4);
					}
					break;
			}
		}
		else if (cond == 5)
		{
			switch (npc.getNpcId())
			{
				case GHOST_FIRE:
					if (player.getInventory().hasItem(SALAMANDER_CHARM))
						dropItems(player, FLAME_CRYSTAL, 1, 5, 500000);
					break;
				
				case TOAD_LORD, MARSH_STAKATO, MARSH_STAKATO_WORKER:
					if (player.getInventory().hasItem(UNDINE_CHARM))
						dropItems(player, DAZZLING_DROP, 1, 20, 300000);
					break;
				
				case MARSH_STAKATO_SOLDIER:
					if (player.getInventory().hasItem(UNDINE_CHARM))
						dropItems(player, DAZZLING_DROP, 1, 20, 400000);
					break;
				
				case MARSH_STAKATO_DRONE:
					if (player.getInventory().hasItem(UNDINE_CHARM))
						dropItems(player, DAZZLING_DROP, 1, 20, 500000);
					break;
				
				case HARPY:
					if (player.getInventory().hasItem(SYLPH_CHARM))
						dropItemsAlways(player, HARPY_FEATHER, 1, 20);
					break;
				
				case WYRM:
					if (player.getInventory().hasItem(SYLPH_CHARM))
						dropItems(player, WYRM_WINGBONE, 1, 10, 500000);
					break;
				
				case WINDSUS:
					if (player.getInventory().hasItem(SYLPH_CHARM))
						dropItems(player, WINDSUS_MANE, 1, 10, 500000);
					break;
				
				case ENCHANTED_MONSTEREYE:
					if (player.getInventory().hasItem(SERPENT_CHARM))
						dropItemsAlways(player, EN_MONSTEREYE_SHELL, 1, 10);
					break;
				
				case ENCHANTED_STONE_GOLEM:
					if (player.getInventory().hasItem(SERPENT_CHARM))
						dropItemsAlways(player, EN_STONEGOLEM_POWDER, 1, 10);
					break;
				
				case ENCHANTED_IRON_GOLEM:
					if (player.getInventory().hasItem(SERPENT_CHARM))
						dropItemsAlways(player, EN_IRONGOLEM_SCRAP, 1, 10);
					break;
			}
		}
	}
}