package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q233_TestOfTheWarSpirit extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q233_TestOfTheWarSpirit";
	
	// Items
	private static final int VENDETTA_TOTEM = 2880;
	private static final int TAMLIN_ORC_HEAD = 2881;
	private static final int WARSPIRIT_TOTEM = 2882;
	private static final int ORIM_CONTRACT = 2883;
	private static final int PORTA_EYE = 2884;
	private static final int EXCURO_SCALE = 2885;
	private static final int MORDEO_TALON = 2886;
	private static final int BRAKI_REMAINS_1 = 2887;
	private static final int PEKIRON_TOTEM = 2888;
	private static final int TONAR_SKULL = 2889;
	private static final int TONAR_RIBBONE = 2890;
	private static final int TONAR_SPINE = 2891;
	private static final int TONAR_ARMBONE = 2892;
	private static final int TONAR_THIGHBONE = 2893;
	private static final int TONAR_REMAINS_1 = 2894;
	private static final int MANAKIA_TOTEM = 2895;
	private static final int HERMODT_SKULL = 2896;
	private static final int HERMODT_RIBBONE = 2897;
	private static final int HERMODT_SPINE = 2898;
	private static final int HERMODT_ARMBONE = 2899;
	private static final int HERMODT_THIGHBONE = 2900;
	private static final int HERMODT_REMAINS_1 = 2901;
	private static final int RACOY_TOTEM = 2902;
	private static final int VIVYAN_LETTER = 2903;
	private static final int INSECT_DIAGRAM_BOOK = 2904;
	private static final int KIRUNA_SKULL = 2905;
	private static final int KIRUNA_RIBBONE = 2906;
	private static final int KIRUNA_SPINE = 2907;
	private static final int KIRUNA_ARMBONE = 2908;
	private static final int KIRUNA_THIGHBONE = 2909;
	private static final int KIRUNA_REMAINS_1 = 2910;
	private static final int BRAKI_REMAINS_2 = 2911;
	private static final int TONAR_REMAINS_2 = 2912;
	private static final int HERMODT_REMAINS_2 = 2913;
	private static final int KIRUNA_REMAINS_2 = 2914;
	
	// Rewards
	private static final int MARK_OF_WARSPIRIT = 2879;
	
	// NPCs
	private static final int VIVYAN = 30030;
	private static final int SARIEN = 30436;
	private static final int RACOY = 30507;
	private static final int SOMAK = 30510;
	private static final int MANAKIA = 30515;
	private static final int ORIM = 30630;
	private static final int ANCESTOR_MARTANKUS = 30649;
	private static final int PEKIRON = 30682;
	
	// Monsters
	private static final int NOBLE_ANT = 20089;
	private static final int NOBLE_ANT_LEADER = 20090;
	private static final int MEDUSA = 20158;
	private static final int PORTA = 20213;
	private static final int EXCURO = 20214;
	private static final int MORDEO = 20215;
	private static final int LETO_LIZARDMAN_SHAMAN = 20581;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int TAMLIN_ORC = 20601;
	private static final int TAMLIN_ORC_ARCHER = 20602;
	private static final int STENOA_GORGON_QUEEN = 27108;
	
	public Q233_TestOfTheWarSpirit()
	{
		super(233, "Test of the War Spirit");
		
		setItemsIds(VENDETTA_TOTEM, TAMLIN_ORC_HEAD, WARSPIRIT_TOTEM, ORIM_CONTRACT, PORTA_EYE, EXCURO_SCALE, MORDEO_TALON, BRAKI_REMAINS_1, PEKIRON_TOTEM, TONAR_SKULL, TONAR_RIBBONE, TONAR_SPINE, TONAR_ARMBONE, TONAR_THIGHBONE, TONAR_REMAINS_1, MANAKIA_TOTEM, HERMODT_SKULL, HERMODT_RIBBONE, HERMODT_SPINE, HERMODT_ARMBONE, HERMODT_THIGHBONE, HERMODT_REMAINS_1, RACOY_TOTEM, VIVYAN_LETTER, INSECT_DIAGRAM_BOOK, KIRUNA_SKULL, KIRUNA_RIBBONE, KIRUNA_SPINE, KIRUNA_ARMBONE, KIRUNA_THIGHBONE, KIRUNA_REMAINS_1, BRAKI_REMAINS_2, TONAR_REMAINS_2, HERMODT_REMAINS_2, KIRUNA_REMAINS_2);
		
		addQuestStart(SOMAK);
		addTalkId(SOMAK, VIVYAN, SARIEN, RACOY, MANAKIA, ORIM, ANCESTOR_MARTANKUS, PEKIRON);
		addMyDying(NOBLE_ANT, NOBLE_ANT_LEADER, MEDUSA, PORTA, EXCURO, MORDEO, LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD, TAMLIN_ORC, TAMLIN_ORC_ARCHER, STENOA_GORGON_QUEEN);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// SOMAK
		if (event.equalsIgnoreCase("30510-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30510-05e.htm";
		}
		// ORIM
		else if (event.equalsIgnoreCase("30630-04.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, ORIM_CONTRACT, 1);
		}
		// RACOY
		else if (event.equalsIgnoreCase("30507-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, RACOY_TOTEM, 1);
		}
		// VIVYAN
		else if (event.equalsIgnoreCase("30030-04.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, VIVYAN_LETTER, 1);
		}
		// PEKIRON
		else if (event.equalsIgnoreCase("30682-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, PEKIRON_TOTEM, 1);
		}
		// MANAKIA
		else if (event.equalsIgnoreCase("30515-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, MANAKIA_TOTEM, 1);
		}
		// ANCESTOR MARTANKUS
		else if (event.equalsIgnoreCase("30649-03.htm"))
		{
			takeItems(player, TAMLIN_ORC_HEAD, -1);
			takeItems(player, WARSPIRIT_TOTEM, -1);
			takeItems(player, BRAKI_REMAINS_2, -1);
			takeItems(player, HERMODT_REMAINS_2, -1);
			takeItems(player, KIRUNA_REMAINS_2, -1);
			takeItems(player, TONAR_REMAINS_2, -1);
			giveItems(player, MARK_OF_WARSPIRIT, 1);
			rewardExpAndSp(player, 63483, 17500);
			player.broadcastPacket(new SocialAction(player, 3));
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getClassId() == ClassId.ORC_SHAMAN)
					htmltext = (player.getStatus().getLevel() < 39) ? "30510-03.htm" : "30510-04.htm";
				else
					htmltext = (player.getRace() == ClassRace.ORC) ? "30510-02.htm" : "30510-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case SOMAK:
						if (cond == 1)
							htmltext = "30510-06.htm";
						else if (cond == 2)
						{
							htmltext = "30510-07.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BRAKI_REMAINS_1, 1);
							takeItems(player, HERMODT_REMAINS_1, 1);
							takeItems(player, KIRUNA_REMAINS_1, 1);
							takeItems(player, TONAR_REMAINS_1, 1);
							giveItems(player, VENDETTA_TOTEM, 1);
						}
						else if (cond == 3)
							htmltext = "30510-08.htm";
						else if (cond == 4)
						{
							htmltext = "30510-09.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, VENDETTA_TOTEM, 1);
							giveItems(player, BRAKI_REMAINS_2, 1);
							giveItems(player, HERMODT_REMAINS_2, 1);
							giveItems(player, KIRUNA_REMAINS_2, 1);
							giveItems(player, TONAR_REMAINS_2, 1);
							giveItems(player, WARSPIRIT_TOTEM, 1);
						}
						else if (cond == 5)
							htmltext = "30510-10.htm";
						break;
					
					case ORIM:
						if (cond == 1 && !player.getInventory().hasItem(BRAKI_REMAINS_1))
						{
							if (!player.getInventory().hasItem(ORIM_CONTRACT))
								htmltext = "30630-01.htm";
							else if (player.getInventory().getItemCount(PORTA_EYE) + player.getInventory().getItemCount(EXCURO_SCALE) + player.getInventory().getItemCount(MORDEO_TALON) == 30)
							{
								htmltext = "30630-06.htm";
								takeItems(player, EXCURO_SCALE, 10);
								takeItems(player, MORDEO_TALON, 10);
								takeItems(player, PORTA_EYE, 10);
								takeItems(player, ORIM_CONTRACT, 1);
								giveItems(player, BRAKI_REMAINS_1, 1);
								
								if (player.getInventory().hasItems(HERMODT_REMAINS_1, KIRUNA_REMAINS_1, TONAR_REMAINS_1))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30630-05.htm";
						}
						else
							htmltext = "30630-07.htm";
						break;
					
					case RACOY:
						if (cond == 1 && !player.getInventory().hasItem(KIRUNA_REMAINS_1))
						{
							if (!player.getInventory().hasItem(RACOY_TOTEM))
								htmltext = "30507-01.htm";
							else if (player.getInventory().hasItem(VIVYAN_LETTER))
								htmltext = "30507-04.htm";
							else if (player.getInventory().hasItem(INSECT_DIAGRAM_BOOK))
							{
								if (player.getInventory().hasItems(KIRUNA_ARMBONE, KIRUNA_RIBBONE, KIRUNA_SKULL, KIRUNA_SPINE, KIRUNA_THIGHBONE))
								{
									htmltext = "30507-06.htm";
									takeItems(player, INSECT_DIAGRAM_BOOK, 1);
									takeItems(player, RACOY_TOTEM, 1);
									takeItems(player, KIRUNA_ARMBONE, 1);
									takeItems(player, KIRUNA_RIBBONE, 1);
									takeItems(player, KIRUNA_SKULL, 1);
									takeItems(player, KIRUNA_SPINE, 1);
									takeItems(player, KIRUNA_THIGHBONE, 1);
									giveItems(player, KIRUNA_REMAINS_1, 1);
									
									if (player.getInventory().hasItems(BRAKI_REMAINS_1, HERMODT_REMAINS_1, TONAR_REMAINS_1))
									{
										st.setCond(2);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
								else
									htmltext = "30507-05.htm";
							}
							else
								htmltext = "30507-03.htm";
						}
						else
							htmltext = "30507-07.htm";
						break;
					
					case VIVYAN:
						if (cond == 1 && player.getInventory().hasItem(RACOY_TOTEM))
						{
							if (player.getInventory().hasItem(VIVYAN_LETTER))
								htmltext = "30030-05.htm";
							else if (player.getInventory().hasItem(INSECT_DIAGRAM_BOOK))
								htmltext = "30030-06.htm";
							else
								htmltext = "30030-01.htm";
						}
						else
							htmltext = "30030-07.htm";
						break;
					
					case SARIEN:
						if (cond == 1 && player.getInventory().hasItem(RACOY_TOTEM))
						{
							if (player.getInventory().hasItem(VIVYAN_LETTER))
							{
								htmltext = "30436-01.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, VIVYAN_LETTER, 1);
								giveItems(player, INSECT_DIAGRAM_BOOK, 1);
							}
							else if (player.getInventory().hasItem(INSECT_DIAGRAM_BOOK))
								htmltext = "30436-02.htm";
						}
						else
							htmltext = "30436-03.htm";
						break;
					
					case PEKIRON:
						if (cond == 1 && !player.getInventory().hasItem(TONAR_REMAINS_1))
						{
							if (!player.getInventory().hasItem(PEKIRON_TOTEM))
								htmltext = "30682-01.htm";
							else if (player.getInventory().hasItems(TONAR_ARMBONE, TONAR_RIBBONE, TONAR_SKULL, TONAR_SPINE, TONAR_THIGHBONE))
							{
								htmltext = "30682-04.htm";
								takeItems(player, PEKIRON_TOTEM, 1);
								takeItems(player, TONAR_ARMBONE, 1);
								takeItems(player, TONAR_RIBBONE, 1);
								takeItems(player, TONAR_SKULL, 1);
								takeItems(player, TONAR_SPINE, 1);
								takeItems(player, TONAR_THIGHBONE, 1);
								giveItems(player, TONAR_REMAINS_1, 1);
								
								if (player.getInventory().hasItems(BRAKI_REMAINS_1, HERMODT_REMAINS_1, KIRUNA_REMAINS_1))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30682-03.htm";
						}
						else
							htmltext = "30682-05.htm";
						break;
					
					case MANAKIA:
						if (cond == 1 && !player.getInventory().hasItem(HERMODT_REMAINS_1))
						{
							if (!player.getInventory().hasItem(MANAKIA_TOTEM))
								htmltext = "30515-01.htm";
							else if (player.getInventory().hasItems(HERMODT_ARMBONE, HERMODT_RIBBONE, HERMODT_SKULL, HERMODT_SPINE, HERMODT_THIGHBONE))
							{
								htmltext = "30515-04.htm";
								takeItems(player, MANAKIA_TOTEM, 1);
								takeItems(player, HERMODT_ARMBONE, 1);
								takeItems(player, HERMODT_RIBBONE, 1);
								takeItems(player, HERMODT_SKULL, 1);
								takeItems(player, HERMODT_SPINE, 1);
								takeItems(player, HERMODT_THIGHBONE, 1);
								giveItems(player, HERMODT_REMAINS_1, 1);
								
								if (player.getInventory().hasItems(BRAKI_REMAINS_1, KIRUNA_REMAINS_1, TONAR_REMAINS_1))
								{
									st.setCond(2);
									playSound(player, SOUND_MIDDLE);
								}
								else
									playSound(player, SOUND_ITEMGET);
							}
							else
								htmltext = "30515-03.htm";
						}
						else
							htmltext = "30515-05.htm";
						break;
					
					case ANCESTOR_MARTANKUS:
						if (cond == 5)
							htmltext = "30649-01.htm";
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
		
		switch (npc.getNpcId())
		{
			case PORTA:
				if (player.getInventory().hasItem(ORIM_CONTRACT))
					dropItemsAlways(player, PORTA_EYE, 1, 10);
				break;
			
			case EXCURO:
				if (player.getInventory().hasItem(ORIM_CONTRACT))
					dropItemsAlways(player, EXCURO_SCALE, 1, 10);
				break;
			
			case MORDEO:
				if (player.getInventory().hasItem(ORIM_CONTRACT))
					dropItemsAlways(player, MORDEO_TALON, 1, 10);
				break;
			
			case NOBLE_ANT, NOBLE_ANT_LEADER:
				if (player.getInventory().hasItem(INSECT_DIAGRAM_BOOK))
				{
					int rndAnt = Rnd.get(100);
					if (rndAnt > 70)
					{
						if (player.getInventory().hasItem(KIRUNA_THIGHBONE))
							dropItemsAlways(player, KIRUNA_ARMBONE, 1, 1);
						else
							dropItemsAlways(player, KIRUNA_THIGHBONE, 1, 1);
					}
					else if (rndAnt > 40)
					{
						if (player.getInventory().hasItem(KIRUNA_SPINE))
							dropItemsAlways(player, KIRUNA_RIBBONE, 1, 1);
						else
							dropItemsAlways(player, KIRUNA_SPINE, 1, 1);
					}
					else if (rndAnt > 10)
						dropItemsAlways(player, KIRUNA_SKULL, 1, 1);
				}
				break;
			
			case LETO_LIZARDMAN_SHAMAN, LETO_LIZARDMAN_OVERLORD:
				if (player.getInventory().hasItem(PEKIRON_TOTEM) && Rnd.nextBoolean())
				{
					if (!player.getInventory().hasItem(TONAR_SKULL))
						dropItemsAlways(player, TONAR_SKULL, 1, 1);
					else if (!player.getInventory().hasItem(TONAR_RIBBONE))
						dropItemsAlways(player, TONAR_RIBBONE, 1, 1);
					else if (!player.getInventory().hasItem(TONAR_SPINE))
						dropItemsAlways(player, TONAR_SPINE, 1, 1);
					else if (!player.getInventory().hasItem(TONAR_ARMBONE))
						dropItemsAlways(player, TONAR_ARMBONE, 1, 1);
					else
						dropItemsAlways(player, TONAR_THIGHBONE, 1, 1);
				}
				break;
			
			case MEDUSA:
				if (player.getInventory().hasItem(MANAKIA_TOTEM) && Rnd.nextBoolean())
				{
					if (!player.getInventory().hasItem(HERMODT_RIBBONE))
						dropItemsAlways(player, HERMODT_RIBBONE, 1, 1);
					else if (!player.getInventory().hasItem(HERMODT_SPINE))
						dropItemsAlways(player, HERMODT_SPINE, 1, 1);
					else if (!player.getInventory().hasItem(HERMODT_ARMBONE))
						dropItemsAlways(player, HERMODT_ARMBONE, 1, 1);
					else
						dropItemsAlways(player, HERMODT_THIGHBONE, 1, 1);
				}
				break;
			
			case STENOA_GORGON_QUEEN:
				if (player.getInventory().hasItem(MANAKIA_TOTEM))
					dropItemsAlways(player, HERMODT_SKULL, 1, 1);
				break;
			
			case TAMLIN_ORC, TAMLIN_ORC_ARCHER:
				if (player.getInventory().hasItem(VENDETTA_TOTEM) && dropItems(player, TAMLIN_ORC_HEAD, 1, 13, 500000))
					st.setCond(4);
				break;
		}
	}
}