package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Q337_AudienceWithTheLandDragon extends Quest
{
	private static final String QUEST_NAME = "Q337_AudienceWithTheLandDragon";
	
	// Variables
	private boolean _jewel1 = false;
	private boolean _jewel2 = false;
	private boolean _jewel3 = false;
	
	// NPCs
	private static final int GABRIELLE = 30753;
	private static final int ORVEN = 30857; // 1
	private static final int KENDRA = 30851; // 2
	private static final int CHAKIRIS = 30705; // 3
	private static final int KAIENA = 30720; // 4
	private static final int MOKE = 30498; // 1st abyssal
	private static final int HELTON = 30678; // 2nd abyssal
	private static final int GILMORE = 30754; // 3rd abyssal
	private static final int THEODRIC = 30755;
	
	// Mobs
	private static final int BLOOD_QUEEN = 18001; // 1
	private static final int SACRIFICE_OF_THE_SACRIFICED = 27171; // 1
	private static final int HARIT_LIZARDMAN_SHAMAN = 20644; // 2
	private static final int HARIT_LIZARDMAN_MATRIARCH = 20645; // 2
	private static final int HARIT_LIZARDMAN_ZEALOT = 27172; // 2
	private static final int KRANROT = 20650; // 3
	private static final int HAMRUT = 20649; // 3
	private static final int MARSH_DRAKE = 20680; // 4
	private static final int MARSH_STALKER = 20679; // 4
	private static final int ABYSSAL_JEWEL_1 = 27165; // 1st abyssal
	private static final int JEWEL_GUARDIAN_MARA = 27168;
	private static final int ABYSSAL_JEWEL_2 = 27166; // 2nd abyssal
	private static final int JEWEL_GUARDIAN_MUSFEL = 27169;
	private static final int CAVE_MAIDEN_1 = 20134; // 3rd abyssal
	private static final int CAVE_MAIDEN_2 = 20287;
	private static final int CAVE_KEEPER_1 = 20246;
	private static final int CAVE_KEEPER_2 = 20277;
	private static final int ABYSSAL_JEWEL_3 = 27167;
	private static final int JEWEL_GUARDIAN_PYTON = 27170;
	
	// Items
	private static final int FEATHER_OF_GABRIELLE = 3852;
	private static final int MARK_OF_WATCHMAN = 3864;
	private static final int REMAINS_OF_SACRIFIED = 3857; // 1
	private static final int TOTEM_OF_LAND_DRAGON = 3858; // 2
	private static final int KRANROT_SKIN = 3855; // 3
	private static final int HAMRUT_LEG = 3856; // 3
	private static final int MARSH_DRAKE_TALONS = 3854; // 4
	private static final int MARSH_STALKER_HORN = 3853; // 4
	private static final int FIRST_FRAGMENT_OF_ABYSS_JEWEL = 3859; // 1st abyssal
	private static final int MARA_FANG = 3862;
	private static final int SECOND_FRAGMENT_OF_ABYSS_JEWEL = 3860; // 2nd abyssal
	private static final int MUSFEL_FANG = 3863;
	private static final int HERALD_OF_SLAYER = 3890;
	private static final int THIRD_FRAGMENT_OF_ABYSS_JEWEL = 3861; // 3rd abyssal
	private static final int PORTAL_STONE = 3865;
	
	/**
	 * 0..npcId, 1..cond, 2..cond2, 3..chance, 4..itemId
	 */
	private static final int[][] DROPS_ON_KILL =
	{
		{
			SACRIFICE_OF_THE_SACRIFICED,
			1,
			1,
			REMAINS_OF_SACRIFIED
		},
		{
			HARIT_LIZARDMAN_ZEALOT,
			1,
			2,
			TOTEM_OF_LAND_DRAGON
		},
		{
			KRANROT,
			1,
			3,
			KRANROT_SKIN
		},
		{
			HAMRUT,
			1,
			3,
			HAMRUT_LEG
		},
		{
			MARSH_DRAKE,
			1,
			4,
			MARSH_DRAKE_TALONS
		},
		{
			MARSH_STALKER,
			1,
			4,
			MARSH_STALKER_HORN
		},
		{
			JEWEL_GUARDIAN_MARA,
			2,
			5,
			MARA_FANG
		},
		{
			JEWEL_GUARDIAN_MUSFEL,
			2,
			6,
			MUSFEL_FANG
		}
	};
	
	/**
	 * 0..npcId, 1..cond, 2..cond2, 3..itemId, 4..amount of mobs, 5..mob
	 */
	private static final int[][] DROP_ON_ATTACK =
	{
		{
			ABYSSAL_JEWEL_1,
			2,
			5,
			FIRST_FRAGMENT_OF_ABYSS_JEWEL,
			20,
			JEWEL_GUARDIAN_MARA
		},
		{
			ABYSSAL_JEWEL_2,
			2,
			6,
			SECOND_FRAGMENT_OF_ABYSS_JEWEL,
			20,
			JEWEL_GUARDIAN_MUSFEL
		},
		{
			ABYSSAL_JEWEL_3,
			4,
			7,
			THIRD_FRAGMENT_OF_ABYSS_JEWEL,
			3,
			JEWEL_GUARDIAN_PYTON
		},
	};
	
	public Q337_AudienceWithTheLandDragon()
	{
		super(337, "Audience with the Land Dragon");
		
		setItemsIds(FEATHER_OF_GABRIELLE, MARK_OF_WATCHMAN, REMAINS_OF_SACRIFIED, TOTEM_OF_LAND_DRAGON, KRANROT_SKIN, HAMRUT_LEG, MARSH_DRAKE_TALONS, MARSH_STALKER_HORN, FIRST_FRAGMENT_OF_ABYSS_JEWEL, MARA_FANG, SECOND_FRAGMENT_OF_ABYSS_JEWEL, MUSFEL_FANG, HERALD_OF_SLAYER, THIRD_FRAGMENT_OF_ABYSS_JEWEL);
		
		addQuestStart(GABRIELLE);
		addTalkId(GABRIELLE, ORVEN, KENDRA, CHAKIRIS, KAIENA, MOKE, HELTON, GILMORE, THEODRIC);
		
		addAttacked(ABYSSAL_JEWEL_1, ABYSSAL_JEWEL_2, ABYSSAL_JEWEL_3);
		addMyDying(BLOOD_QUEEN, SACRIFICE_OF_THE_SACRIFICED, HARIT_LIZARDMAN_SHAMAN, HARIT_LIZARDMAN_MATRIARCH, HARIT_LIZARDMAN_ZEALOT, KRANROT, HAMRUT, MARSH_DRAKE, MARSH_STALKER, JEWEL_GUARDIAN_MARA, JEWEL_GUARDIAN_MUSFEL, CAVE_MAIDEN_1, CAVE_MAIDEN_2, CAVE_KEEPER_1, CAVE_KEEPER_2, JEWEL_GUARDIAN_PYTON);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Gabrielle
		if (event.equalsIgnoreCase("30753-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("drop1", 1);
			st.set("drop2", 1);
			st.set("drop3", 1);
			st.set("drop4", 1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, FEATHER_OF_GABRIELLE, 1);
		}
		else if (event.equalsIgnoreCase("30753-09.htm"))
		{
			if (player.getInventory().getItemCount(MARK_OF_WATCHMAN) >= 4)
			{
				st.setCond(2);
				st.set("drop5", 2);
				st.set("drop6", 2);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, MARK_OF_WATCHMAN, 4);
			}
			else
				htmltext = null;
		}
		// Theodric
		else if (event.equalsIgnoreCase("30755-05.htm"))
		{
			if (player.getInventory().hasItem(THIRD_FRAGMENT_OF_ABYSS_JEWEL))
			{
				takeItems(player, THIRD_FRAGMENT_OF_ABYSS_JEWEL, 1);
				takeItems(player, HERALD_OF_SLAYER, 1);
				giveItems(player, PORTAL_STONE, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				htmltext = null;
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
				htmltext = (player.getStatus().getLevel() < 50) ? "30753-02.htm" : "30753-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case GABRIELLE:
						if (cond == 1)
							htmltext = (player.getInventory().getItemCount(MARK_OF_WATCHMAN) < 4) ? "30753-06.htm" : "30753-08.htm";
						else if (cond == 2)
						{
							if (player.getInventory().getItemCount(MARK_OF_WATCHMAN) < 2)
								htmltext = "30753-10.htm";
							else
							{
								htmltext = "30753-11.htm";
								st.setCond(3);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, FEATHER_OF_GABRIELLE, 1);
								takeItems(player, MARK_OF_WATCHMAN, 1);
								giveItems(player, HERALD_OF_SLAYER, 1);
							}
						}
						else if (cond == 3)
							htmltext = "30753-12.htm";
						else if (cond == 4)
							htmltext = "30753-13.htm";
						break;
					
					case ORVEN:
						if (cond == 1)
						{
							if (st.getInteger("drop1") == 1)
							{
								if (player.getInventory().hasItem(REMAINS_OF_SACRIFIED))
								{
									htmltext = "30857-02.htm";
									st.unset("drop1");
									playSound(player, SOUND_MIDDLE);
									takeItems(player, REMAINS_OF_SACRIFIED, 1);
									giveItems(player, MARK_OF_WATCHMAN, 1);
								}
								else
									htmltext = "30857-01.htm";
							}
							else if (player.getInventory().getItemCount(MARK_OF_WATCHMAN) < 4)
								htmltext = "30857-03.htm";
							else
								htmltext = "30857-04.htm";
						}
						break;
					
					case KENDRA:
						if (cond == 1)
						{
							if (st.getInteger("drop2") == 1)
							{
								if (player.getInventory().hasItem(TOTEM_OF_LAND_DRAGON))
								{
									htmltext = "30851-02.htm";
									st.unset("drop2");
									playSound(player, SOUND_MIDDLE);
									takeItems(player, TOTEM_OF_LAND_DRAGON, 1);
									giveItems(player, MARK_OF_WATCHMAN, 1);
								}
								else
									htmltext = "30851-01.htm";
							}
							else if (player.getInventory().getItemCount(MARK_OF_WATCHMAN) < 4)
								htmltext = "30851-03.htm";
							else
								htmltext = "30851-04.htm";
						}
						break;
					
					case CHAKIRIS:
						if (cond == 1)
						{
							if (st.getInteger("drop3") == 1)
							{
								if (player.getInventory().hasItems(KRANROT_SKIN, HAMRUT_LEG))
								{
									htmltext = "30705-02.htm";
									st.unset("drop3");
									playSound(player, SOUND_MIDDLE);
									takeItems(player, KRANROT_SKIN, 1);
									takeItems(player, HAMRUT_LEG, 1);
									giveItems(player, MARK_OF_WATCHMAN, 1);
								}
								else
									htmltext = "30705-01.htm";
							}
							else if (player.getInventory().getItemCount(MARK_OF_WATCHMAN) < 4)
								htmltext = "30705-03.htm";
							else
								htmltext = "30705-04.htm";
						}
						break;
					
					case KAIENA:
						if (cond == 1)
						{
							if (st.getInteger("drop4") == 1)
							{
								if (player.getInventory().hasItems(MARSH_DRAKE_TALONS, MARSH_STALKER_HORN))
								{
									htmltext = "30720-02.htm";
									st.unset("drop4");
									playSound(player, SOUND_MIDDLE);
									takeItems(player, MARSH_DRAKE_TALONS, 1);
									takeItems(player, MARSH_STALKER_HORN, 1);
									giveItems(player, MARK_OF_WATCHMAN, 1);
								}
								else
									htmltext = "30720-01.htm";
							}
							else if (player.getInventory().getItemCount(MARK_OF_WATCHMAN) < 4)
								htmltext = "30720-03.htm";
							else
								htmltext = "30720-04.htm";
						}
						break;
					
					case MOKE:
						if (cond == 2)
						{
							switch (st.getInteger("drop5"))
							{
								case 2:
									htmltext = "30498-01.htm";
									st.set("drop5", 1);
									break;
								
								case 1:
									if (player.getInventory().hasItems(FIRST_FRAGMENT_OF_ABYSS_JEWEL, MARA_FANG))
									{
										htmltext = "30498-03.htm";
										st.unset("drop5");
										playSound(player, SOUND_MIDDLE);
										takeItems(player, FIRST_FRAGMENT_OF_ABYSS_JEWEL, 1);
										takeItems(player, MARA_FANG, 1);
										giveItems(player, MARK_OF_WATCHMAN, 1);
									}
									else
										htmltext = "30498-02.htm";
									break;
								
								case 0:
									if (player.getInventory().getItemCount(MARK_OF_WATCHMAN) < 2)
										htmltext = "30498-04.htm";
									else
										htmltext = "30498-05.htm";
									break;
							}
						}
						break;
					
					case HELTON:
						if (cond == 2)
						{
							switch (st.getInteger("drop6"))
							{
								case 2:
									htmltext = "30678-01.htm";
									st.set("drop6", 1);
									break;
								
								case 1:
									if (player.getInventory().hasItems(SECOND_FRAGMENT_OF_ABYSS_JEWEL, MUSFEL_FANG))
									{
										htmltext = "30678-03.htm";
										st.unset("drop6");
										playSound(player, SOUND_MIDDLE);
										takeItems(player, SECOND_FRAGMENT_OF_ABYSS_JEWEL, 1);
										takeItems(player, MUSFEL_FANG, 1);
										giveItems(player, MARK_OF_WATCHMAN, 1);
									}
									else
										htmltext = "30678-02.htm";
									break;
								
								case 0:
									if (player.getInventory().getItemCount(MARK_OF_WATCHMAN) < 2)
										htmltext = "30678-04.htm";
									else
										htmltext = "30678-05.htm";
									break;
							}
						}
						break;
					
					case GILMORE:
						if (cond == 1 || cond == 2)
							htmltext = "30754-01.htm";
						else if (cond == 3)
						{
							htmltext = "30754-02.htm";
							st.setCond(4);
							st.set("drop7", 1);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 4)
							htmltext = (!player.getInventory().hasItem(THIRD_FRAGMENT_OF_ABYSS_JEWEL)) ? "30754-04.htm" : "30754-05.htm";
						break;
					
					case THEODRIC:
						if (cond == 1 || cond == 2)
							htmltext = "30755-01.htm";
						else if (cond == 3)
							htmltext = "30755-02.htm";
						else if (cond == 4)
							htmltext = (!player.getInventory().hasItem(THIRD_FRAGMENT_OF_ABYSS_JEWEL)) ? "30755-03.htm" : "30755-04.htm";
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
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int npcId = npc.getNpcId();
		
		for (int[] npcInfo : DROP_ON_ATTACK)
		{
			if (npcInfo[0] != npcId)
				continue;
			
			if (npcInfo[1] != st.getCond())
				break;
			
			final double hpRatio = (npc.getStatus().getHp() + damage) / npc.getStatus().getMaxHp();
			
			// reward jewel fragment
			if (hpRatio < 0.33)
			{
				if (Rnd.get(100) < 33 && st.getInteger("drop" + npcInfo[2]) == 1)
				{
					int itemId = npcInfo[3];
					if (!player.getInventory().hasItem(itemId))
					{
						giveItems(player, itemId, 1);
						playSound(player, SOUND_ITEMGET);
					}
				}
			}
			// spawn monsters and register spawned
			else if (hpRatio < 0.66)
			{
				if (Rnd.get(100) < 33 && st.getInteger("drop" + npcInfo[2]) == 1)
				{
					boolean spawn;
					if (npcId == ABYSSAL_JEWEL_3)
						spawn = _jewel3;
					else if (npcId == ABYSSAL_JEWEL_2)
						spawn = _jewel2;
					else
						spawn = _jewel1;
					
					if (spawn)
					{
						for (int i = 0; i < npcInfo[4]; i++)
						{
							final Npc mob = addSpawn(npcInfo[5], npc.getX() + Rnd.get(-150, 150), npc.getY() + Rnd.get(-150, 150), npc.getZ(), npc.getHeading(), true, 60000, false);
							mob.getAI().addAttackDesire(attacker, 200);
						}
						
						if (npcId == ABYSSAL_JEWEL_3)
							_jewel3 = false;
						else if (npcId == ABYSSAL_JEWEL_2)
							_jewel2 = false;
						else
							_jewel1 = false;
					}
				}
				
			}
			// reset spawned if npc regenerated to 90% HP and more
			else if (hpRatio > 0.9)
			{
				if (npcId == ABYSSAL_JEWEL_3)
					_jewel3 = true;
				else if (npcId == ABYSSAL_JEWEL_2)
					_jewel2 = true;
				else
					_jewel1 = true;
			}
			break;
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		final int cond = st.getCond();
		final int npcId = npc.getNpcId();
		
		switch (npcId)
		{
			case SACRIFICE_OF_THE_SACRIFICED, HARIT_LIZARDMAN_ZEALOT, KRANROT, HAMRUT, MARSH_DRAKE, MARSH_STALKER, JEWEL_GUARDIAN_MARA, JEWEL_GUARDIAN_MUSFEL:
				for (int[] npcInfo : DROPS_ON_KILL)
				{
					if (npcInfo[0] != npcId)
						continue;
					
					if (npcInfo[1] == cond && st.getInteger("drop" + npcInfo[2]) == 1)
					{
						int itemId = npcInfo[3];
						if (!player.getInventory().hasItem(itemId))
						{
							giveItems(player, itemId, 1);
							playSound(player, SOUND_ITEMGET);
						}
					}
					break;
				}
				break;
			
			case BLOOD_QUEEN:// Orven's request
				if (cond == 1 && st.getInteger("drop1") == 1 && !player.getInventory().hasItem(REMAINS_OF_SACRIFIED))
				{
					for (int i = 0; i < 8; i++)
						addSpawn(SACRIFICE_OF_THE_SACRIFICED, npc.getX() + Rnd.get(-100, 100), npc.getY() + Rnd.get(-100, 100), npc.getZ(), npc.getHeading(), true, 60000, false);
				}
				break;
			
			case HARIT_LIZARDMAN_SHAMAN, HARIT_LIZARDMAN_MATRIARCH:// Kendra's request
				if (cond == 1 && Rnd.get(5) == 0 && st.getInteger("drop2") == 1 && !player.getInventory().hasItem(TOTEM_OF_LAND_DRAGON))
				{
					for (int i = 0; i < 3; i++)
						addSpawn(HARIT_LIZARDMAN_ZEALOT, npc.getX() + Rnd.get(-50, 50), npc.getY() + Rnd.get(-50, 50), npc.getZ(), npc.getHeading(), true, 60000, false);
				}
				break;
			
			case CAVE_MAIDEN_1, CAVE_MAIDEN_2, CAVE_KEEPER_1, CAVE_KEEPER_2:// Gilmore's request
				if (cond == 4 && Rnd.get(5) == 0 && !player.getInventory().hasItem(THIRD_FRAGMENT_OF_ABYSS_JEWEL))
					addSpawn(ABYSSAL_JEWEL_3, npc.getX() + Rnd.get(-50, 50), npc.getY() + Rnd.get(-50, 50), npc.getZ(), npc.getHeading(), true, 60000, false);
				break;
		}
	}
}