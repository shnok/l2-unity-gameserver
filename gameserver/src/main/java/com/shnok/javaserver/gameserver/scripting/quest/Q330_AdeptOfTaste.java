package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q330_AdeptOfTaste extends Quest
{
	private static final String QUEST_NAME = "Q330_AdeptOfTaste";
	
	// NPCs
	private static final int SONIA = 30062;
	private static final int GLYVKA = 30067;
	private static final int ROLLANT = 30069;
	private static final int JACOB = 30073;
	private static final int PANO = 30078;
	private static final int MIRIEN = 30461;
	private static final int JONAS = 30469;
	
	// Items
	private static final int INGREDIENT_LIST = 1420;
	private static final int SONIA_BOTANY_BOOK = 1421;
	private static final int RED_MANDRAGORA_ROOT = 1422;
	private static final int WHITE_MANDRAGORA_ROOT = 1423;
	private static final int RED_MANDRAGORA_SAP = 1424;
	private static final int WHITE_MANDRAGORA_SAP = 1425;
	private static final int JACOB_INSECT_BOOK = 1426;
	private static final int NECTAR = 1427;
	private static final int ROYAL_JELLY = 1428;
	private static final int HONEY = 1429;
	private static final int GOLDEN_HONEY = 1430;
	private static final int PANO_CONTRACT = 1431;
	private static final int HOBGOBLIN_AMULET = 1432;
	private static final int DIONIAN_POTATO = 1433;
	private static final int GLYVKA_BOTANY_BOOK = 1434;
	private static final int GREEN_MARSH_MOSS = 1435;
	private static final int BROWN_MARSH_MOSS = 1436;
	private static final int GREEN_MOSS_BUNDLE = 1437;
	private static final int BROWN_MOSS_BUNDLE = 1438;
	private static final int ROLANT_CREATURE_BOOK = 1439;
	private static final int MONSTER_EYE_BODY = 1440;
	private static final int MONSTER_EYE_MEAT = 1441;
	private static final int JONAS_STEAK_DISH_1 = 1442;
	private static final int JONAS_STEAK_DISH_2 = 1443;
	private static final int JONAS_STEAK_DISH_3 = 1444;
	private static final int JONAS_STEAK_DISH_4 = 1445;
	private static final int JONAS_STEAK_DISH_5 = 1446;
	private static final int MIRIEN_REVIEW_1 = 1447;
	private static final int MIRIEN_REVIEW_2 = 1448;
	private static final int MIRIEN_REVIEW_3 = 1449;
	private static final int MIRIEN_REVIEW_4 = 1450;
	private static final int MIRIEN_REVIEW_5 = 1451;
	
	// Rewards
	private static final int JONAS_SALAD_RECIPE = 1455;
	private static final int JONAS_SAUCE_RECIPE = 1456;
	private static final int JONAS_STEAK_RECIPE = 1457;
	
	// Drop chances
	private static final Map<Integer, int[]> CHANCES = HashMap.newHashMap(6);
	
	public Q330_AdeptOfTaste()
	{
		super(330, "Adept of Taste");
		
		CHANCES.put(20204, new int[]
		{
			92,
			100
		});
		CHANCES.put(20229, new int[]
		{
			80,
			95
		});
		CHANCES.put(20223, new int[]
		{
			70,
			77
		});
		CHANCES.put(20154, new int[]
		{
			70,
			77
		});
		CHANCES.put(20155, new int[]
		{
			87,
			96
		});
		CHANCES.put(20156, new int[]
		{
			77,
			85
		});
		
		setItemsIds(INGREDIENT_LIST, RED_MANDRAGORA_SAP, WHITE_MANDRAGORA_SAP, HONEY, GOLDEN_HONEY, DIONIAN_POTATO, GREEN_MOSS_BUNDLE, BROWN_MOSS_BUNDLE, MONSTER_EYE_MEAT, MIRIEN_REVIEW_1, MIRIEN_REVIEW_2, MIRIEN_REVIEW_3, MIRIEN_REVIEW_4, MIRIEN_REVIEW_5, JONAS_STEAK_DISH_1, JONAS_STEAK_DISH_2, JONAS_STEAK_DISH_3, JONAS_STEAK_DISH_4, JONAS_STEAK_DISH_5, SONIA_BOTANY_BOOK, RED_MANDRAGORA_ROOT, WHITE_MANDRAGORA_ROOT, JACOB_INSECT_BOOK, NECTAR, ROYAL_JELLY, PANO_CONTRACT, HOBGOBLIN_AMULET, GLYVKA_BOTANY_BOOK, GREEN_MARSH_MOSS, BROWN_MARSH_MOSS, ROLANT_CREATURE_BOOK, MONSTER_EYE_BODY);
		
		addQuestStart(JONAS); // Jonas
		addTalkId(JONAS, SONIA, GLYVKA, ROLLANT, JACOB, PANO, MIRIEN);
		
		addMyDying(20147, 20154, 20155, 20156, 20204, 20223, 20226, 20228, 20229, 20265, 20266);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30469-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, INGREDIENT_LIST, 1);
		}
		else if (event.equalsIgnoreCase("30062-05.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, SONIA_BOTANY_BOOK, 1);
			takeItems(player, RED_MANDRAGORA_ROOT, -1);
			takeItems(player, WHITE_MANDRAGORA_ROOT, -1);
			giveItems(player, RED_MANDRAGORA_SAP, 1);
			
		}
		else if (event.equalsIgnoreCase("30073-05.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, JACOB_INSECT_BOOK, 1);
			takeItems(player, NECTAR, -1);
			takeItems(player, ROYAL_JELLY, -1);
			giveItems(player, HONEY, 1);
		}
		else if (event.equalsIgnoreCase("30067-05.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, GLYVKA_BOTANY_BOOK, 1);
			takeItems(player, GREEN_MARSH_MOSS, -1);
			takeItems(player, BROWN_MARSH_MOSS, -1);
			giveItems(player, GREEN_MOSS_BUNDLE, 1);
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
				htmltext = (player.getStatus().getLevel() < 24) ? "30469-01.htm" : "30469-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case JONAS:
						if (player.getInventory().hasItem(INGREDIENT_LIST))
						{
							if (!hasAllIngredients(player))
								htmltext = "30469-04.htm";
							else
							{
								int dish;
								
								final int specialIngredientsNumber = player.getInventory().getItemCount(WHITE_MANDRAGORA_SAP) + player.getInventory().getItemCount(GOLDEN_HONEY) + player.getInventory().getItemCount(BROWN_MOSS_BUNDLE);
								
								if (Rnd.nextBoolean())
								{
									htmltext = "30469-05t" + Integer.toString(specialIngredientsNumber + 2) + ".htm";
									dish = 1443 + specialIngredientsNumber;
								}
								else
								{
									htmltext = "30469-05t" + Integer.toString(specialIngredientsNumber + 1) + ".htm";
									dish = 1442 + specialIngredientsNumber;
								}
								
								// Sound according dish.
								playSound(player, (dish == JONAS_STEAK_DISH_5) ? SOUND_JACKPOT : SOUND_ITEMGET);
								
								takeItems(player, INGREDIENT_LIST, 1);
								takeItems(player, RED_MANDRAGORA_SAP, 1);
								takeItems(player, WHITE_MANDRAGORA_SAP, 1);
								takeItems(player, HONEY, 1);
								takeItems(player, GOLDEN_HONEY, 1);
								takeItems(player, DIONIAN_POTATO, 1);
								takeItems(player, GREEN_MOSS_BUNDLE, 1);
								takeItems(player, BROWN_MOSS_BUNDLE, 1);
								takeItems(player, MONSTER_EYE_MEAT, 1);
								giveItems(player, dish, 1);
							}
						}
						else if (player.getInventory().hasAtLeastOneItem(JONAS_STEAK_DISH_1, JONAS_STEAK_DISH_2, JONAS_STEAK_DISH_3, JONAS_STEAK_DISH_4, JONAS_STEAK_DISH_5))
							htmltext = "30469-06.htm";
						else if (player.getInventory().hasAtLeastOneItem(MIRIEN_REVIEW_1, MIRIEN_REVIEW_2, MIRIEN_REVIEW_3, MIRIEN_REVIEW_4, MIRIEN_REVIEW_5))
						{
							if (player.getInventory().hasItem(MIRIEN_REVIEW_1))
							{
								htmltext = "30469-06t1.htm";
								takeItems(player, MIRIEN_REVIEW_1, 1);
								rewardItems(player, 57, 7500);
								rewardExpAndSp(player, 6000, 0);
							}
							else if (player.getInventory().hasItem(MIRIEN_REVIEW_2))
							{
								htmltext = "30469-06t2.htm";
								takeItems(player, MIRIEN_REVIEW_2, 1);
								rewardItems(player, 57, 9000);
								rewardExpAndSp(player, 7000, 0);
							}
							else if (player.getInventory().hasItem(MIRIEN_REVIEW_3))
							{
								htmltext = "30469-06t3.htm";
								takeItems(player, MIRIEN_REVIEW_3, 1);
								rewardItems(player, 57, 5800);
								giveItems(player, JONAS_SALAD_RECIPE, 1);
								rewardExpAndSp(player, 9000, 0);
							}
							else if (player.getInventory().hasItem(MIRIEN_REVIEW_4))
							{
								htmltext = "30469-06t4.htm";
								takeItems(player, MIRIEN_REVIEW_4, 1);
								rewardItems(player, 57, 6800);
								giveItems(player, JONAS_SAUCE_RECIPE, 1);
								rewardExpAndSp(player, 10500, 0);
							}
							else if (player.getInventory().hasItem(MIRIEN_REVIEW_5))
							{
								htmltext = "30469-06t5.htm";
								takeItems(player, MIRIEN_REVIEW_5, 1);
								rewardItems(player, 57, 7800);
								giveItems(player, JONAS_STEAK_RECIPE, 1);
								rewardExpAndSp(player, 12000, 0);
							}
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case MIRIEN:
						if (player.getInventory().hasItem(INGREDIENT_LIST))
							htmltext = "30461-01.htm";
						else if (player.getInventory().hasAtLeastOneItem(JONAS_STEAK_DISH_1, JONAS_STEAK_DISH_2, JONAS_STEAK_DISH_3, JONAS_STEAK_DISH_4, JONAS_STEAK_DISH_5))
						{
							playSound(player, SOUND_ITEMGET);
							if (player.getInventory().hasItem(JONAS_STEAK_DISH_1))
							{
								htmltext = "30461-02t1.htm";
								takeItems(player, JONAS_STEAK_DISH_1, 1);
								giveItems(player, MIRIEN_REVIEW_1, 1);
							}
							else if (player.getInventory().hasItem(JONAS_STEAK_DISH_2))
							{
								htmltext = "30461-02t2.htm";
								takeItems(player, JONAS_STEAK_DISH_2, 1);
								giveItems(player, MIRIEN_REVIEW_2, 1);
							}
							else if (player.getInventory().hasItem(JONAS_STEAK_DISH_3))
							{
								htmltext = "30461-02t3.htm";
								takeItems(player, JONAS_STEAK_DISH_3, 1);
								giveItems(player, MIRIEN_REVIEW_3, 1);
							}
							else if (player.getInventory().hasItem(JONAS_STEAK_DISH_4))
							{
								htmltext = "30461-02t4.htm";
								takeItems(player, JONAS_STEAK_DISH_4, 1);
								giveItems(player, MIRIEN_REVIEW_4, 1);
							}
							else if (player.getInventory().hasItem(JONAS_STEAK_DISH_5))
							{
								htmltext = "30461-02t5.htm";
								takeItems(player, JONAS_STEAK_DISH_5, 1);
								giveItems(player, MIRIEN_REVIEW_5, 1);
							}
						}
						else if (player.getInventory().hasAtLeastOneItem(MIRIEN_REVIEW_1, MIRIEN_REVIEW_2, MIRIEN_REVIEW_3, MIRIEN_REVIEW_4, MIRIEN_REVIEW_5))
							htmltext = "30461-04.htm";
						break;
					
					case SONIA:
						if (!player.getInventory().hasItem(RED_MANDRAGORA_SAP) && !player.getInventory().hasItem(WHITE_MANDRAGORA_SAP))
						{
							if (!player.getInventory().hasItem(SONIA_BOTANY_BOOK))
							{
								htmltext = "30062-01.htm";
								giveItems(player, SONIA_BOTANY_BOOK, 1);
								playSound(player, SOUND_ITEMGET);
							}
							else
							{
								if (player.getInventory().getItemCount(RED_MANDRAGORA_ROOT) < 40 || player.getInventory().getItemCount(WHITE_MANDRAGORA_ROOT) < 40)
									htmltext = "30062-02.htm";
								else if (player.getInventory().getItemCount(WHITE_MANDRAGORA_ROOT) >= 40)
								{
									htmltext = "30062-06.htm";
									takeItems(player, SONIA_BOTANY_BOOK, 1);
									takeItems(player, RED_MANDRAGORA_ROOT, -1);
									takeItems(player, WHITE_MANDRAGORA_ROOT, -1);
									giveItems(player, WHITE_MANDRAGORA_SAP, 1);
									playSound(player, SOUND_ITEMGET);
								}
								else
									htmltext = "30062-03.htm";
							}
						}
						else
							htmltext = "30062-07.htm";
						break;
					
					case JACOB:
						if (!player.getInventory().hasItem(HONEY) && !player.getInventory().hasItem(GOLDEN_HONEY))
						{
							if (!player.getInventory().hasItem(JACOB_INSECT_BOOK))
							{
								htmltext = "30073-01.htm";
								giveItems(player, JACOB_INSECT_BOOK, 1);
								playSound(player, SOUND_ITEMGET);
							}
							else
							{
								if (player.getInventory().getItemCount(NECTAR) < 20)
									htmltext = "30073-02.htm";
								else
								{
									if (player.getInventory().getItemCount(ROYAL_JELLY) < 10)
										htmltext = "30073-03.htm";
									else
									{
										htmltext = "30073-06.htm";
										takeItems(player, JACOB_INSECT_BOOK, 1);
										takeItems(player, NECTAR, -1);
										takeItems(player, ROYAL_JELLY, -1);
										giveItems(player, GOLDEN_HONEY, 1);
										playSound(player, SOUND_ITEMGET);
									}
								}
							}
						}
						else
							htmltext = "30073-07.htm";
						break;
					
					case PANO:
						if (!player.getInventory().hasItem(DIONIAN_POTATO))
						{
							if (!player.getInventory().hasItem(PANO_CONTRACT))
							{
								htmltext = "30078-01.htm";
								giveItems(player, PANO_CONTRACT, 1);
								playSound(player, SOUND_ITEMGET);
							}
							else
							{
								if (player.getInventory().getItemCount(HOBGOBLIN_AMULET) < 30)
									htmltext = "30078-02.htm";
								else
								{
									htmltext = "30078-03.htm";
									takeItems(player, PANO_CONTRACT, 1);
									takeItems(player, HOBGOBLIN_AMULET, -1);
									giveItems(player, DIONIAN_POTATO, 1);
									playSound(player, SOUND_ITEMGET);
								}
							}
						}
						else
							htmltext = "30078-04.htm";
						break;
					
					case GLYVKA:
						if (!player.getInventory().hasItem(GREEN_MOSS_BUNDLE) && !player.getInventory().hasItem(BROWN_MOSS_BUNDLE))
						{
							if (!player.getInventory().hasItem(GLYVKA_BOTANY_BOOK))
							{
								giveItems(player, GLYVKA_BOTANY_BOOK, 1);
								htmltext = "30067-01.htm";
								playSound(player, SOUND_ITEMGET);
							}
							else
							{
								if (player.getInventory().getItemCount(GREEN_MARSH_MOSS) < 20 || player.getInventory().getItemCount(BROWN_MARSH_MOSS) < 20)
									htmltext = "30067-02.htm";
								else if (player.getInventory().getItemCount(BROWN_MARSH_MOSS) >= 20)
								{
									htmltext = "30067-06.htm";
									takeItems(player, GLYVKA_BOTANY_BOOK, 1);
									takeItems(player, GREEN_MARSH_MOSS, -1);
									takeItems(player, BROWN_MARSH_MOSS, -1);
									giveItems(player, BROWN_MOSS_BUNDLE, 1);
									playSound(player, SOUND_ITEMGET);
								}
								else
									htmltext = "30067-03.htm";
							}
						}
						else
							htmltext = "30067-07.htm";
						break;
					
					case ROLLANT:
						if (!player.getInventory().hasItem(MONSTER_EYE_MEAT))
						{
							if (!player.getInventory().hasItem(ROLANT_CREATURE_BOOK))
							{
								htmltext = "30069-01.htm";
								giveItems(player, ROLANT_CREATURE_BOOK, 1);
								playSound(player, SOUND_ITEMGET);
							}
							else
							{
								if (player.getInventory().getItemCount(MONSTER_EYE_BODY) < 30)
									htmltext = "30069-02.htm";
								else
								{
									htmltext = "30069-03.htm";
									takeItems(player, ROLANT_CREATURE_BOOK, 1);
									takeItems(player, MONSTER_EYE_BODY, -1);
									giveItems(player, MONSTER_EYE_MEAT, 1);
									playSound(player, SOUND_ITEMGET);
								}
							}
						}
						else
							htmltext = "30069-04.htm";
						break;
				}
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
		
		final int npcId = npc.getNpcId();
		
		switch (npcId)
		{
			case 20265:
				if (player.getInventory().hasItem(ROLANT_CREATURE_BOOK))
					dropItems(player, MONSTER_EYE_BODY, (Rnd.get(97) < 77) ? 2 : 3, 30, 970000);
				break;
			
			case 20266:
				if (player.getInventory().hasItem(ROLANT_CREATURE_BOOK))
					dropItemsAlways(player, MONSTER_EYE_BODY, (Rnd.get(10) < 7) ? 1 : 2, 30);
				break;
			
			case 20226:
				if (player.getInventory().hasItem(GLYVKA_BOTANY_BOOK))
					dropItems(player, ((Rnd.get(96) < 87) ? GREEN_MARSH_MOSS : BROWN_MARSH_MOSS), 1, 20, 960000);
				break;
			
			case 20228:
				if (player.getInventory().hasItem(GLYVKA_BOTANY_BOOK))
					dropItemsAlways(player, ((Rnd.get(10) < 9) ? GREEN_MARSH_MOSS : BROWN_MARSH_MOSS), 1, 20);
				break;
			
			case 20147:
				if (player.getInventory().hasItem(PANO_CONTRACT))
					dropItemsAlways(player, HOBGOBLIN_AMULET, 1, 30);
				break;
			
			case 20204, 20229:
				if (player.getInventory().hasItem(JACOB_INSECT_BOOK))
				{
					final int random = Rnd.get(100);
					final int[] chances = CHANCES.get(npcId);
					if (random < chances[0])
						dropItemsAlways(player, NECTAR, 1, 20);
					else if (random < chances[1])
						dropItemsAlways(player, ROYAL_JELLY, 1, 10);
				}
				break;
			
			case 20154, 20155, 20156, 20223:
				if (player.getInventory().hasItem(SONIA_BOTANY_BOOK))
				{
					final int random = Rnd.get(100);
					final int[] chances = CHANCES.get(npcId);
					if (random < chances[1])
						dropItemsAlways(player, (random < chances[0]) ? RED_MANDRAGORA_ROOT : WHITE_MANDRAGORA_ROOT, 1, 40);
				}
				break;
		}
	}
	
	private static boolean hasAllIngredients(Player player)
	{
		return player.getInventory().hasItems(DIONIAN_POTATO, MONSTER_EYE_MEAT) && player.getInventory().hasAtLeastOneItem(WHITE_MANDRAGORA_SAP, RED_MANDRAGORA_SAP) && player.getInventory().hasAtLeastOneItem(GOLDEN_HONEY, HONEY) && player.getInventory().hasAtLeastOneItem(BROWN_MOSS_BUNDLE, GREEN_MOSS_BUNDLE);
	}
}