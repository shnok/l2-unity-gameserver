package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q216_TrialOfTheGuildsman extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q216_TrialOfTheGuildsman";
	
	// Items
	private static final int RECIPE_JOURNEYMAN_RING = 3024;
	private static final int RECIPE_AMBER_BEAD = 3025;
	private static final int VALKON_RECOMMENDATION = 3120;
	private static final int MANDRAGORA_BERRY = 3121;
	private static final int ALTRAN_INSTRUCTIONS = 3122;
	private static final int ALTRAN_RECOMMENDATION_1 = 3123;
	private static final int ALTRAN_RECOMMENDATION_2 = 3124;
	private static final int NORMAN_INSTRUCTIONS = 3125;
	private static final int NORMAN_RECEIPT = 3126;
	private static final int DUNING_INSTRUCTIONS = 3127;
	private static final int DUNING_KEY = 3128;
	private static final int NORMAN_LIST = 3129;
	private static final int GRAY_BONE_POWDER = 3130;
	private static final int GRANITE_WHETSTONE = 3131;
	private static final int RED_PIGMENT = 3132;
	private static final int BRAIDED_YARN = 3133;
	private static final int JOURNEYMAN_GEM = 3134;
	private static final int PINTER_INSTRUCTIONS = 3135;
	private static final int AMBER_BEAD = 3136;
	private static final int AMBER_LUMP = 3137;
	private static final int JOURNEYMAN_DECO_BEADS = 3138;
	private static final int JOURNEYMAN_RING = 3139;
	
	// Rewards
	private static final int MARK_OF_GUILDSMAN = 3119;
	
	// NPCs
	private static final int VALKON = 30103;
	private static final int NORMAN = 30210;
	private static final int ALTRAN = 30283;
	private static final int PINTER = 30298;
	private static final int DUNING = 30688;
	
	// Monsters
	private static final int ANT = 20079;
	private static final int ANT_CAPTAIN = 20080;
	private static final int GRANITE_GOLEM = 20083;
	private static final int MANDRAGORA_SPROUT = 20154;
	private static final int MANDRAGORA_SAPLING = 20155;
	private static final int MANDRAGORA_BLOSSOM = 20156;
	private static final int SILENOS = 20168;
	private static final int STRAIN = 20200;
	private static final int GHOUL = 20201;
	private static final int DEAD_SEEKER = 20202;
	private static final int BREKA_ORC_SHAMAN = 20269;
	private static final int BREKA_ORC_OVERLORD = 20270;
	private static final int BREKA_ORC_WARRIOR = 20271;
	
	public Q216_TrialOfTheGuildsman()
	{
		super(216, "Trial of the Guildsman");
		
		setItemsIds(RECIPE_JOURNEYMAN_RING, RECIPE_AMBER_BEAD, VALKON_RECOMMENDATION, MANDRAGORA_BERRY, ALTRAN_INSTRUCTIONS, ALTRAN_RECOMMENDATION_1, ALTRAN_RECOMMENDATION_2, NORMAN_INSTRUCTIONS, NORMAN_RECEIPT, DUNING_INSTRUCTIONS, DUNING_KEY, NORMAN_LIST, GRAY_BONE_POWDER, GRANITE_WHETSTONE, RED_PIGMENT, BRAIDED_YARN, JOURNEYMAN_GEM, PINTER_INSTRUCTIONS, AMBER_BEAD, AMBER_LUMP, JOURNEYMAN_DECO_BEADS, JOURNEYMAN_RING);
		
		addQuestStart(VALKON);
		addTalkId(VALKON, NORMAN, ALTRAN, PINTER, DUNING);
		
		addMyDying(ANT, ANT_CAPTAIN, GRANITE_GOLEM, MANDRAGORA_SPROUT, MANDRAGORA_SAPLING, MANDRAGORA_BLOSSOM, SILENOS, STRAIN, GHOUL, DEAD_SEEKER, BREKA_ORC_SHAMAN, BREKA_ORC_OVERLORD, BREKA_ORC_WARRIOR);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30103-06.htm"))
		{
			if (player.getAdena() >= 2000)
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
				takeItems(player, 57, 2000);
				giveItems(player, VALKON_RECOMMENDATION, 1);
				
				if (giveDimensionalDiamonds35(player))
					htmltext = "30103-06d.htm";
			}
			else
				htmltext = "30103-05a.htm";
		}
		else if (event.equalsIgnoreCase("30103-06c.htm") || event.equalsIgnoreCase("30103-07c.htm"))
		{
			if (st.getCond() < 3)
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("30103-09a.htm") || event.equalsIgnoreCase("30103-09b.htm"))
		{
			takeItems(player, ALTRAN_INSTRUCTIONS, 1);
			takeItems(player, JOURNEYMAN_RING, -1);
			giveItems(player, MARK_OF_GUILDSMAN, 1);
			rewardExpAndSp(player, 80993, 12250);
			player.broadcastPacket(new SocialAction(player, 3));
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("30210-04.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, ALTRAN_RECOMMENDATION_1, 1);
			giveItems(player, NORMAN_INSTRUCTIONS, 1);
			giveItems(player, NORMAN_RECEIPT, 1);
		}
		else if (event.equalsIgnoreCase("30210-10.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, NORMAN_LIST, 1);
		}
		else if (event.equalsIgnoreCase("30283-03.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MANDRAGORA_BERRY, 1);
			takeItems(player, VALKON_RECOMMENDATION, 1);
			giveItems(player, ALTRAN_INSTRUCTIONS, 1);
			giveItems(player, ALTRAN_RECOMMENDATION_1, 1);
			giveItems(player, ALTRAN_RECOMMENDATION_2, 1);
			giveItems(player, RECIPE_JOURNEYMAN_RING, 1);
		}
		else if (event.equalsIgnoreCase("30298-04.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, ALTRAN_RECOMMENDATION_2, 1);
			giveItems(player, PINTER_INSTRUCTIONS, 1);
			
			// Artisan receives a recipe to craft Amber Beads, while spoiler case is handled in onKill section.
			if (player.getClassId() == ClassId.ARTISAN)
			{
				htmltext = "30298-05.htm";
				giveItems(player, RECIPE_AMBER_BEAD, 1);
			}
		}
		else if (event.equalsIgnoreCase("30688-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, NORMAN_RECEIPT, 1);
			giveItems(player, DUNING_INSTRUCTIONS, 1);
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
				if (player.getClassId() != ClassId.SCAVENGER && player.getClassId() != ClassId.ARTISAN)
					htmltext = "30103-01.htm";
				else if (player.getStatus().getLevel() < 35)
					htmltext = "30103-02.htm";
				else
					htmltext = "30103-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case VALKON:
						if (cond == 1)
							htmltext = "30103-06c.htm";
						else if (cond < 5)
							htmltext = "30103-07.htm";
						else if (cond == 5)
							htmltext = "30103-08.htm";
						else if (cond == 6)
							htmltext = (player.getInventory().getItemCount(JOURNEYMAN_RING) == 7) ? "30103-09.htm" : "30103-08.htm";
						break;
					
					case ALTRAN:
						if (cond < 4)
						{
							htmltext = "30283-01.htm";
							if (cond == 1)
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
						}
						else if (cond == 4)
							htmltext = "30283-02.htm";
						else if (cond > 4)
							htmltext = "30283-04.htm";
						break;
					
					case NORMAN:
						if (cond == 5)
						{
							if (player.getInventory().hasItem(ALTRAN_RECOMMENDATION_1))
								htmltext = "30210-01.htm";
							else if (player.getInventory().hasItem(NORMAN_RECEIPT))
								htmltext = "30210-05.htm";
							else if (player.getInventory().hasItem(DUNING_INSTRUCTIONS))
								htmltext = "30210-06.htm";
							else if (player.getInventory().getItemCount(DUNING_KEY) == 30)
							{
								htmltext = "30210-07.htm";
								playSound(player, SOUND_ITEMGET);
								takeItems(player, DUNING_KEY, -1);
							}
							else if (player.getInventory().hasItem(NORMAN_LIST))
							{
								if (player.getInventory().getItemCount(GRAY_BONE_POWDER) == 70 && player.getInventory().getItemCount(GRANITE_WHETSTONE) == 70 && player.getInventory().getItemCount(RED_PIGMENT) == 70 && player.getInventory().getItemCount(BRAIDED_YARN) == 70)
								{
									htmltext = "30210-12.htm";
									takeItems(player, NORMAN_INSTRUCTIONS, 1);
									takeItems(player, NORMAN_LIST, 1);
									takeItems(player, BRAIDED_YARN, -1);
									takeItems(player, GRANITE_WHETSTONE, -1);
									takeItems(player, GRAY_BONE_POWDER, -1);
									takeItems(player, RED_PIGMENT, -1);
									giveItems(player, JOURNEYMAN_GEM, 7);
									
									if (player.getInventory().getItemCount(JOURNEYMAN_DECO_BEADS) == 7)
									{
										st.setCond(6);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
								else
									htmltext = "30210-11.htm";
							}
						}
						break;
					
					case DUNING:
						if (cond == 5)
						{
							if (player.getInventory().hasItem(NORMAN_RECEIPT))
								htmltext = "30688-01.htm";
							else if (player.getInventory().hasItem(DUNING_INSTRUCTIONS))
							{
								if (player.getInventory().getItemCount(DUNING_KEY) < 30)
									htmltext = "30688-03.htm";
								else
								{
									htmltext = "30688-04.htm";
									playSound(player, SOUND_ITEMGET);
									takeItems(player, DUNING_INSTRUCTIONS, 1);
								}
							}
							else
								htmltext = "30688-05.htm";
						}
						break;
					
					case PINTER:
						if (cond == 5)
						{
							if (player.getInventory().hasItem(ALTRAN_RECOMMENDATION_2))
								htmltext = (player.getStatus().getLevel() < 36) ? "30298-01.htm" : "30298-02.htm";
							else if (player.getInventory().hasItem(PINTER_INSTRUCTIONS))
							{
								if (player.getInventory().getItemCount(AMBER_BEAD) < 70)
									htmltext = "30298-06.htm";
								else
								{
									htmltext = "30298-07.htm";
									takeItems(player, AMBER_BEAD, -1);
									takeItems(player, PINTER_INSTRUCTIONS, 1);
									giveItems(player, JOURNEYMAN_DECO_BEADS, 7);
									
									if (player.getInventory().getItemCount(JOURNEYMAN_GEM) == 7)
									{
										st.setCond(6);
										playSound(player, SOUND_MIDDLE);
									}
									else
										playSound(player, SOUND_ITEMGET);
								}
							}
						}
						else if (player.getInventory().hasItem(JOURNEYMAN_DECO_BEADS))
							htmltext = "30298-08.htm";
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
			case MANDRAGORA_SPROUT, MANDRAGORA_SAPLING, MANDRAGORA_BLOSSOM:
				if (st.getCond() == 3 && dropItemsAlways(player, MANDRAGORA_BERRY, 1, 1))
					st.setCond(4);
				break;
			
			case BREKA_ORC_WARRIOR, BREKA_ORC_OVERLORD, BREKA_ORC_SHAMAN:
				if (player.getInventory().hasItem(DUNING_INSTRUCTIONS))
					dropItemsAlways(player, DUNING_KEY, 1, 30);
				break;
			
			case GHOUL, STRAIN:
				if (player.getInventory().hasItem(NORMAN_LIST))
					dropItemsAlways(player, GRAY_BONE_POWDER, 5, 70);
				break;
			
			case GRANITE_GOLEM:
				if (player.getInventory().hasItem(NORMAN_LIST))
					dropItemsAlways(player, GRANITE_WHETSTONE, 7, 70);
				break;
			
			case DEAD_SEEKER:
				if (player.getInventory().hasItem(NORMAN_LIST))
					dropItemsAlways(player, RED_PIGMENT, 7, 70);
				break;
			
			case SILENOS:
				if (player.getInventory().hasItem(NORMAN_LIST))
					dropItemsAlways(player, BRAIDED_YARN, 10, 70);
				break;
			
			case ANT, ANT_CAPTAIN:
				if (player.getInventory().hasItem(PINTER_INSTRUCTIONS))
				{
					// Different cases if player is a wannabe BH or WS.
					if (dropItemsAlways(player, AMBER_BEAD, (player.getClassId() == ClassId.SCAVENGER && ((Monster) npc).getSpoilState().isActualSpoiler(player)) ? 10 : 5, 70))
						if (player.getClassId() == ClassId.ARTISAN && Rnd.nextBoolean())
							giveItems(player, AMBER_LUMP, 1);
				}
				break;
		}
	}
}