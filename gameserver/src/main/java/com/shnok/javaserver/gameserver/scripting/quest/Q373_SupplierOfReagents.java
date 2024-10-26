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

public class Q373_SupplierOfReagents extends Quest
{
	private static final String QUEST_NAME = "Q373_SupplierOfReagents";
	
	// Variables
	private static final String INGREDIENT = "ingredient";
	private static final String CATALYST = "catalyst";
	
	// NPCs
	private static final int WESLEY = 30166;
	private static final int URN = 31149;
	
	// Monsters
	private static final int CRENDION = 20813;
	private static final int HALLATE_MAID = 20822;
	private static final int HALLATE_GUARDIAN = 21061;
	private static final int PLATINUM_TRIBE_SHAMAN = 20828;
	private static final int PLATINUM_GUARDIAN_SHAMAN = 21066;
	private static final int LAVA_WYRM = 21111;
	private static final int HAMES_ORC_SHAMAN = 21115;
	
	// Quest items
	private static final int MIXING_STONE = 5904;
	private static final int MIXING_MANUAL = 6317;
	
	// Items - pouches
	private static final int REAGENT_POUCH_1 = 6007;
	private static final int REAGENT_POUCH_2 = 6008;
	private static final int REAGENT_POUCH_3 = 6009;
	private static final int REAGENT_BOX = 6010;
	// Items - ingredients
	private static final int WYRMS_BLOOD = 6011;
	private static final int LAVA_STONE = 6012;
	private static final int MOONSTONE_SHARD = 6013;
	private static final int ROTTEN_BONE = 6014;
	private static final int DEMONS_BLOOD = 6015;
	private static final int INFERNIUM_ORE = 6016;
	// Items - catalysts
	private static final int BLOOD_ROOT = 6017;
	private static final int VOLCANIC_ASH = 6018;
	private static final int QUICKSILVER = 6019;
	private static final int SULFUR = 6020;
	private static final int DEMONIC_ESSENCE = 6031;
	private static final int MIDNIGHT_OIL = 6030;
	// Items - products
	private static final int DRACOPLASM = 6021;
	private static final int MAGMA_DUST = 6022;
	private static final int MOON_DUST = 6023;
	private static final int NECROPLASM = 6024;
	private static final int DEMONPLASM = 6025;
	private static final int INFERNO_DUST = 6026;
	private static final int FIRE_ESSENCE = 6028;
	private static final int LUNARGENT = 6029;
	// Items - products final
	private static final int DRACONIC_ESSENCE = 6027;
	private static final int ABYSS_OIL = 6032;
	private static final int HELLFIRE_OIL = 6033;
	private static final int NIGHTMARE_OIL = 6034;
	private static final int PURE_SILVER = 6320;
	
	/**
	 * This droplist defines the npcId, the item dropped and the luck.
	 * <ul>
	 * <li>HAMES_ORC_SHAMAN : 47% chance to drop - reagent pouch (47%)</li>
	 * <li>HALLATES_MAID : 84,4% chance to drop - reageant pouch (66,4%) and volcanic ash (18%)</li>
	 * <li>HALLATES_GUARDIAN : 83,3% chance to drop - demon's blood (72,9%) and moonstone shard (10,4%)</li>
	 * <li>PLATINUM_GUARDIAN_SHAMAN : 44,2% chance to drop - reagent box (44,2%)</li>
	 * <li>PLATINUM_TRIBE_SHAMAN : 100% chance to drop - reagent pouch (68%) and quichsilver (32%)</li>
	 * <li>CRENDION : 100% chance to drop - rotten bone piece (61,8%) and quicksilver (38,2%)</li>
	 * <li>LAVA_WYRM : 75% chance to drop - wyrm's blood (50,5%) and lava stone (24,5%)</li>
	 * </ul>
	 */
	private static final Map<Integer, int[]> DROPLIST = new HashMap<>();
	static
	{
		DROPLIST.put(PLATINUM_GUARDIAN_SHAMAN, new int[]
		{
			REAGENT_BOX,
			442000,
			0
		});
		DROPLIST.put(HAMES_ORC_SHAMAN, new int[]
		{
			REAGENT_POUCH_3,
			470000,
			0
		});
		DROPLIST.put(PLATINUM_TRIBE_SHAMAN, new int[]
		{
			REAGENT_POUCH_2,
			QUICKSILVER,
			680,
			1000
		});
		DROPLIST.put(HALLATE_MAID, new int[]
		{
			REAGENT_POUCH_1,
			VOLCANIC_ASH,
			664,
			844
		});
		DROPLIST.put(HALLATE_GUARDIAN, new int[]
		{
			DEMONS_BLOOD,
			MOONSTONE_SHARD,
			729,
			833
		});
		DROPLIST.put(CRENDION, new int[]
		{
			ROTTEN_BONE,
			QUICKSILVER,
			618,
			1000
		});
		DROPLIST.put(LAVA_WYRM, new int[]
		{
			WYRMS_BLOOD,
			LAVA_STONE,
			505,
			750
		});
	}
	
	private static final int[][] FORMULAS =
	{
		{
			10,
			WYRMS_BLOOD,
			BLOOD_ROOT,
			DRACOPLASM
		},
		{
			10,
			LAVA_STONE,
			VOLCANIC_ASH,
			MAGMA_DUST
		},
		{
			10,
			MOONSTONE_SHARD,
			VOLCANIC_ASH,
			MOON_DUST
		},
		{
			10,
			ROTTEN_BONE,
			BLOOD_ROOT,
			NECROPLASM
		},
		{
			10,
			DEMONS_BLOOD,
			BLOOD_ROOT,
			DEMONPLASM
		},
		{
			10,
			INFERNIUM_ORE,
			VOLCANIC_ASH,
			INFERNO_DUST
		},
		{
			10,
			DRACOPLASM,
			QUICKSILVER,
			DRACONIC_ESSENCE
		},
		{
			10,
			MAGMA_DUST,
			SULFUR,
			FIRE_ESSENCE
		},
		{
			10,
			MOON_DUST,
			QUICKSILVER,
			LUNARGENT
		},
		{
			10,
			NECROPLASM,
			QUICKSILVER,
			MIDNIGHT_OIL
		},
		{
			10,
			DEMONPLASM,
			SULFUR,
			DEMONIC_ESSENCE
		},
		{
			10,
			INFERNO_DUST,
			SULFUR,
			ABYSS_OIL
		},
		{
			1,
			FIRE_ESSENCE,
			DEMONIC_ESSENCE,
			HELLFIRE_OIL
		},
		{
			1,
			LUNARGENT,
			MIDNIGHT_OIL,
			NIGHTMARE_OIL
		},
		{
			1,
			LUNARGENT,
			QUICKSILVER,
			PURE_SILVER
		}
	};
	
	private static final int[][] TEMPERATURES =
	{
		{
			1,
			100,
			1
		},
		{
			2,
			45,
			3
		},
		{
			3,
			15,
			5
		}
	};
	
	public Q373_SupplierOfReagents()
	{
		super(373, "Supplier of Reagents");
		
		setItemsIds(MIXING_STONE, MIXING_MANUAL);
		
		addQuestStart(WESLEY);
		addTalkId(WESLEY, URN);
		
		addMyDying(CRENDION, HALLATE_MAID, HALLATE_GUARDIAN, PLATINUM_TRIBE_SHAMAN, PLATINUM_GUARDIAN_SHAMAN, LAVA_WYRM, HAMES_ORC_SHAMAN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Wesley
		if (event.equalsIgnoreCase("30166-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			
			giveItems(player, MIXING_STONE, 1);
			giveItems(player, MIXING_MANUAL, 1);
		}
		else if (event.equalsIgnoreCase("30166-09.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		// Urn
		else if (event.equalsIgnoreCase("31149-02.htm"))
		{
			if (!player.getInventory().hasItem(MIXING_STONE))
				htmltext = "31149-04.htm";
		}
		else if (event.startsWith("31149-03-"))
		{
			int regentId = Integer.parseInt(event.substring(9, 13));
			for (int[] formula : FORMULAS)
			{
				if (formula[1] != regentId)
					continue;
				
				// Not enough items, cancel the operation.
				if (player.getInventory().getItemCount(regentId) < formula[0])
					break;
				
				st.set(INGREDIENT, regentId);
				return htmltext;
			}
			htmltext = "31149-04.htm";
		}
		else if (event.startsWith("31149-06-"))
		{
			int catalyst = Integer.parseInt(event.substring(9, 13));
			
			// Not enough items, cancel the operation.
			if (!player.getInventory().hasItem(catalyst))
				return "31149-04.htm";
			
			st.set(CATALYST, catalyst);
		}
		else if (event.startsWith("31149-12-"))
		{
			int regent = st.getInteger(INGREDIENT);
			int catalyst = st.getInteger(CATALYST);
			
			for (int[] formula : FORMULAS)
			{
				if (formula[1] != regent || formula[2] != catalyst)
					continue;
				
				// Not enough regents.
				if (player.getInventory().getItemCount(regent) < formula[0])
					break;
				
				// Not enough catalysts.
				if (!player.getInventory().hasItem(catalyst))
					break;
				
				takeItems(player, regent, formula[0]);
				takeItems(player, catalyst, 1);
				
				int tempIndex = Integer.parseInt(event.substring(9, 10));
				for (int[] temperature : TEMPERATURES)
				{
					if (temperature[0] != tempIndex)
						continue;
					
					if (Rnd.get(100) < temperature[1])
					{
						giveItems(player, formula[3], temperature[2]);
						return "31149-12-" + formula[3] + ".htm";
					}
					return "31149-11.htm";
				}
			}
			htmltext = "31149-13.htm";
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
				htmltext = (player.getStatus().getLevel() < 57) ? "30166-01.htm" : "30166-02.htm";
				break;
			
			case STARTED:
				if (npc.getNpcId() == WESLEY)
					htmltext = "30166-05.htm";
				else
					htmltext = "31149-01.htm";
				break;
		}
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int[] drop = DROPLIST.get(npc.getNpcId());
		
		if (drop[2] == 0)
			dropItems(st.getPlayer(), drop[0], 1, 0, drop[1]);
		else
		{
			final int random = Rnd.get(1000);
			if (random < drop[3])
				dropItemsAlways(st.getPlayer(), (random < drop[2]) ? drop[0] : drop[1], 1, 0);
		}
	}
}