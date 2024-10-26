package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q219_TestimonyOfFate extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q219_TestimonyOfFate";
	
	// NPCs
	private static final int KAIRA = 30476;
	private static final int METHEUS = 30614;
	private static final int IXIA = 30463;
	private static final int ALDER_SPIRIT = 30613;
	private static final int ROA = 30114;
	private static final int NORMAN = 30210;
	private static final int THIFIELL = 30358;
	private static final int ARKENIA = 30419;
	private static final int BLOODY_PIXY = 31845;
	private static final int BLIGHT_TREANT = 31850;
	
	// Items
	private static final int KAIRA_LETTER = 3173;
	private static final int METHEUS_FUNERAL_JAR = 3174;
	private static final int KASANDRA_REMAINS = 3175;
	private static final int HERBALISM_TEXTBOOK = 3176;
	private static final int IXIA_LIST = 3177;
	private static final int MEDUSA_ICHOR = 3178;
	private static final int MARSH_SPIDER_FLUIDS = 3179;
	private static final int DEAD_SEEKER_DUNG = 3180;
	private static final int TYRANT_BLOOD = 3181;
	private static final int NIGHTSHADE_ROOT = 3182;
	private static final int BELLADONNA = 3183;
	private static final int ALDER_SKULL_1 = 3184;
	private static final int ALDER_SKULL_2 = 3185;
	private static final int ALDER_RECEIPT = 3186;
	private static final int REVELATIONS_MANUSCRIPT = 3187;
	private static final int KAIRA_RECOMMENDATION = 3189;
	private static final int KAIRA_INSTRUCTIONS = 3188;
	private static final int PALUS_CHARM = 3190;
	private static final int THIFIELL_LETTER = 3191;
	private static final int ARKENIA_NOTE = 3192;
	private static final int PIXY_GARNET = 3193;
	private static final int GRANDIS_SKULL = 3194;
	private static final int KARUL_BUGBEAR_SKULL = 3195;
	private static final int BREKA_OVERLORD_SKULL = 3196;
	private static final int LETO_OVERLORD_SKULL = 3197;
	private static final int RED_FAIRY_DUST = 3198;
	private static final int BLIGHT_TREANT_SEED = 3199;
	private static final int BLACK_WILLOW_LEAF = 3200;
	private static final int BLIGHT_TREANT_SAP = 3201;
	private static final int ARKENIA_LETTER = 3202;
	
	// Rewards
	private static final int MARK_OF_FATE = 3172;
	
	// Monsters
	private static final int HANGMAN_TREE = 20144;
	private static final int MARSH_STAKATO = 20157;
	private static final int MEDUSA = 20158;
	private static final int TYRANT = 20192;
	private static final int TYRANT_KINGPIN = 20193;
	private static final int DEAD_SEEKER = 20202;
	private static final int MARSH_STAKATO_WORKER = 20230;
	private static final int MARSH_STAKATO_SOLDIER = 20232;
	private static final int MARSH_SPIDER = 20233;
	private static final int MARSH_STAKATO_DRONE = 20234;
	private static final int BREKA_ORC_OVERLORD = 20270;
	private static final int GRANDIS = 20554;
	private static final int LETO_LIZARDMAN_OVERLORD = 20582;
	private static final int KARUL_BUGBEAR = 20600;
	private static final int BLACK_WILLOW_LURKER = 27079;
	
	// Cond 6 drop chances
	private static final Map<Integer, Integer> CHANCES = HashMap.newHashMap(9);
	
	public Q219_TestimonyOfFate()
	{
		super(219, "Testimony of Fate");
		
		CHANCES.put(DEAD_SEEKER, 500000);
		CHANCES.put(TYRANT, 500000);
		CHANCES.put(TYRANT_KINGPIN, 600000);
		CHANCES.put(MEDUSA, 500000);
		CHANCES.put(MARSH_STAKATO, 400000);
		CHANCES.put(MARSH_STAKATO_WORKER, 300000);
		CHANCES.put(MARSH_STAKATO_SOLDIER, 500000);
		CHANCES.put(MARSH_STAKATO_DRONE, 600000);
		CHANCES.put(MARSH_SPIDER, 500000);
		
		setItemsIds(KAIRA_LETTER, METHEUS_FUNERAL_JAR, KASANDRA_REMAINS, HERBALISM_TEXTBOOK, IXIA_LIST, MEDUSA_ICHOR, MARSH_SPIDER_FLUIDS, DEAD_SEEKER_DUNG, TYRANT_BLOOD, NIGHTSHADE_ROOT, BELLADONNA, ALDER_SKULL_1, ALDER_SKULL_2, ALDER_RECEIPT, REVELATIONS_MANUSCRIPT, KAIRA_RECOMMENDATION, KAIRA_INSTRUCTIONS, PALUS_CHARM, THIFIELL_LETTER, ARKENIA_NOTE, PIXY_GARNET, GRANDIS_SKULL, KARUL_BUGBEAR_SKULL, BREKA_OVERLORD_SKULL, LETO_OVERLORD_SKULL, RED_FAIRY_DUST, BLIGHT_TREANT_SEED, BLACK_WILLOW_LEAF, BLIGHT_TREANT_SAP, ARKENIA_LETTER);
		
		addQuestStart(KAIRA);
		addTalkId(KAIRA, METHEUS, IXIA, ALDER_SPIRIT, ROA, NORMAN, THIFIELL, ARKENIA, BLOODY_PIXY, BLIGHT_TREANT);
		
		addMyDying(HANGMAN_TREE, MARSH_STAKATO, MEDUSA, TYRANT, TYRANT_KINGPIN, DEAD_SEEKER, MARSH_STAKATO_WORKER, MARSH_STAKATO_SOLDIER, MARSH_SPIDER, MARSH_STAKATO_DRONE, BREKA_ORC_OVERLORD, GRANDIS, LETO_LIZARDMAN_OVERLORD, KARUL_BUGBEAR, BLACK_WILLOW_LURKER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30476-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, KAIRA_LETTER, 1);
			
			if (giveDimensionalDiamonds37(player))
				htmltext = "30476-05a.htm";
		}
		else if (event.equalsIgnoreCase("30114-04.htm"))
		{
			st.setCond(12);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ALDER_SKULL_2, 1);
			giveItems(player, ALDER_RECEIPT, 1);
		}
		else if (event.equalsIgnoreCase("30476-12.htm"))
		{
			playSound(player, SOUND_MIDDLE);
			
			if (player.getStatus().getLevel() < 38)
			{
				htmltext = "30476-13.htm";
				st.setCond(14);
				giveItems(player, KAIRA_INSTRUCTIONS, 1);
			}
			else
			{
				st.setCond(15);
				takeItems(player, REVELATIONS_MANUSCRIPT, 1);
				giveItems(player, KAIRA_RECOMMENDATION, 1);
			}
		}
		else if (event.equalsIgnoreCase("30419-02.htm"))
		{
			st.setCond(17);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, THIFIELL_LETTER, 1);
			giveItems(player, ARKENIA_NOTE, 1);
		}
		else if (event.equalsIgnoreCase("31845-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, PIXY_GARNET, 1);
		}
		else if (event.equalsIgnoreCase("31850-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, BLIGHT_TREANT_SEED, 1);
		}
		else if (event.equalsIgnoreCase("30419-05.htm"))
		{
			st.setCond(18);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ARKENIA_NOTE, 1);
			takeItems(player, BLIGHT_TREANT_SAP, 1);
			takeItems(player, RED_FAIRY_DUST, 1);
			giveItems(player, ARKENIA_LETTER, 1);
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
				if (player.getRace() != ClassRace.DARK_ELF)
					htmltext = "30476-02.htm";
				else if (player.getStatus().getLevel() < 37 || player.getClassId().getLevel() != 1)
					htmltext = "30476-01.htm";
				else
					htmltext = "30476-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case KAIRA:
						if (cond == 1)
							htmltext = "30476-06.htm";
						else if (cond == 2 || cond == 3)
							htmltext = "30476-07.htm";
						else if (cond > 3 && cond < 9)
							htmltext = "30476-08.htm";
						else if (cond == 9)
						{
							htmltext = "30476-09.htm";
							st.setCond(10);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ALDER_SKULL_1, 1);
							addSpawn(ALDER_SPIRIT, player, false, 0, false);
						}
						else if (cond > 9 && cond < 13)
							htmltext = "30476-10.htm";
						else if (cond == 13)
							htmltext = "30476-11.htm";
						else if (cond == 14)
						{
							if (player.getStatus().getLevel() < 38)
								htmltext = "30476-14.htm";
							else
							{
								htmltext = "30476-12.htm";
								st.setCond(15);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, KAIRA_INSTRUCTIONS, 1);
								takeItems(player, REVELATIONS_MANUSCRIPT, 1);
								giveItems(player, KAIRA_RECOMMENDATION, 1);
							}
						}
						else if (cond == 15)
							htmltext = "30476-16.htm";
						else if (cond > 15)
							htmltext = "30476-17.htm";
						break;
					
					case METHEUS:
						if (cond == 1)
						{
							htmltext = "30614-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, KAIRA_LETTER, 1);
							giveItems(player, METHEUS_FUNERAL_JAR, 1);
						}
						else if (cond == 2)
							htmltext = "30614-02.htm";
						else if (cond == 3)
						{
							htmltext = "30614-03.htm";
							st.setCond(4);
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, KASANDRA_REMAINS, 1);
							giveItems(player, HERBALISM_TEXTBOOK, 1);
						}
						else if (cond > 3 && cond < 8)
							htmltext = "30614-04.htm";
						else if (cond == 8)
						{
							htmltext = "30614-05.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BELLADONNA, 1);
							giveItems(player, ALDER_SKULL_1, 1);
						}
						else if (cond > 8)
							htmltext = "30614-06.htm";
						break;
					
					case IXIA:
						if (cond == 5)
						{
							htmltext = "30463-01.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, HERBALISM_TEXTBOOK, 1);
							giveItems(player, IXIA_LIST, 1);
						}
						else if (cond == 6)
							htmltext = "30463-02.htm";
						else if (cond == 7)
						{
							htmltext = "30463-03.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, IXIA_LIST, 1);
							takeItems(player, DEAD_SEEKER_DUNG, -1);
							takeItems(player, MARSH_SPIDER_FLUIDS, -1);
							takeItems(player, MEDUSA_ICHOR, -1);
							takeItems(player, NIGHTSHADE_ROOT, -1);
							takeItems(player, TYRANT_BLOOD, -1);
							giveItems(player, BELLADONNA, 1);
						}
						else if (cond == 8)
							htmltext = "30463-04.htm";
						else if (cond > 8)
							htmltext = "30463-05.htm";
						break;
					
					case ALDER_SPIRIT:
						if (cond == 10)
						{
							htmltext = "30613-01.htm";
							st.setCond(11);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, ALDER_SKULL_2, 1);
							npc.deleteMe();
						}
						break;
					
					case ROA:
						if (cond == 11)
							htmltext = "30114-01.htm";
						else if (cond == 12)
							htmltext = "30114-05.htm";
						else if (cond > 12)
							htmltext = "30114-06.htm";
						break;
					
					case NORMAN:
						if (cond == 12)
						{
							htmltext = "30210-01.htm";
							st.setCond(13);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ALDER_RECEIPT, 1);
							giveItems(player, REVELATIONS_MANUSCRIPT, 1);
						}
						else if (cond > 12)
							htmltext = "30210-02.htm";
						break;
					
					case THIFIELL:
						if (cond == 15)
						{
							htmltext = "30358-01.htm";
							st.setCond(16);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, KAIRA_RECOMMENDATION, 1);
							giveItems(player, PALUS_CHARM, 1);
							giveItems(player, THIFIELL_LETTER, 1);
						}
						else if (cond == 16)
							htmltext = "30358-02.htm";
						else if (cond == 17)
							htmltext = "30358-03.htm";
						else if (cond == 18)
						{
							htmltext = "30358-04.htm";
							takeItems(player, PALUS_CHARM, 1);
							takeItems(player, ARKENIA_LETTER, 1);
							giveItems(player, MARK_OF_FATE, 1);
							rewardExpAndSp(player, 68183, 1750);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case ARKENIA:
						if (cond == 16)
							htmltext = "30419-01.htm";
						else if (cond == 17)
							htmltext = (player.getInventory().hasItem(BLIGHT_TREANT_SAP) && player.getInventory().hasItem(RED_FAIRY_DUST)) ? "30419-04.htm" : "30419-03.htm";
						else if (cond == 18)
							htmltext = "30419-06.htm";
						break;
					
					case BLOODY_PIXY:
						if (cond == 17)
						{
							if (player.getInventory().hasItem(PIXY_GARNET))
							{
								if (player.getInventory().getItemCount(GRANDIS_SKULL) >= 10 && player.getInventory().getItemCount(KARUL_BUGBEAR_SKULL) >= 10 && player.getInventory().getItemCount(BREKA_OVERLORD_SKULL) >= 10 && player.getInventory().getItemCount(LETO_OVERLORD_SKULL) >= 10)
								{
									htmltext = "31845-04.htm";
									playSound(player, SOUND_ITEMGET);
									takeItems(player, BREKA_OVERLORD_SKULL, -1);
									takeItems(player, GRANDIS_SKULL, -1);
									takeItems(player, KARUL_BUGBEAR_SKULL, -1);
									takeItems(player, LETO_OVERLORD_SKULL, -1);
									takeItems(player, PIXY_GARNET, 1);
									giveItems(player, RED_FAIRY_DUST, 1);
								}
								else
									htmltext = "31845-03.htm";
							}
							else if (player.getInventory().hasItem(RED_FAIRY_DUST))
								htmltext = "31845-05.htm";
							else
								htmltext = "31845-01.htm";
						}
						else if (cond == 18)
							htmltext = "31845-05.htm";
						break;
					
					case BLIGHT_TREANT:
						if (cond == 17)
						{
							if (player.getInventory().hasItem(BLIGHT_TREANT_SEED))
							{
								if (player.getInventory().hasItem(BLACK_WILLOW_LEAF))
								{
									htmltext = "31850-04.htm";
									playSound(player, SOUND_ITEMGET);
									takeItems(player, BLACK_WILLOW_LEAF, 1);
									takeItems(player, BLIGHT_TREANT_SEED, 1);
									giveItems(player, BLIGHT_TREANT_SAP, 1);
								}
								else
									htmltext = "31850-03.htm";
							}
							else if (player.getInventory().hasItem(BLIGHT_TREANT_SAP))
								htmltext = "31850-05.htm";
							else
								htmltext = "31850-01.htm";
						}
						else if (cond == 18)
							htmltext = "31850-05.htm";
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
		
		final int npcId = npc.getNpcId();
		
		switch (npcId)
		{
			case HANGMAN_TREE:
				if (st.getCond() == 2)
				{
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, METHEUS_FUNERAL_JAR, 1);
					giveItems(player, KASANDRA_REMAINS, 1);
				}
				break;
			
			case DEAD_SEEKER:
				if (st.getCond() == 6 && dropItems(player, DEAD_SEEKER_DUNG, 1, 10, CHANCES.get(npcId)))
					if (player.getInventory().getItemCount(TYRANT_BLOOD) >= 10 && player.getInventory().getItemCount(MEDUSA_ICHOR) >= 10 && player.getInventory().getItemCount(NIGHTSHADE_ROOT) >= 10 && player.getInventory().getItemCount(MARSH_SPIDER_FLUIDS) >= 10)
						st.setCond(7);
				break;
			
			case TYRANT, TYRANT_KINGPIN:
				if (st.getCond() == 6 && dropItems(player, TYRANT_BLOOD, 1, 10, CHANCES.get(npcId)))
					if (player.getInventory().getItemCount(DEAD_SEEKER_DUNG) >= 10 && player.getInventory().getItemCount(MEDUSA_ICHOR) >= 10 && player.getInventory().getItemCount(NIGHTSHADE_ROOT) >= 10 && player.getInventory().getItemCount(MARSH_SPIDER_FLUIDS) >= 10)
						st.setCond(7);
				break;
			
			case MEDUSA:
				if (st.getCond() == 6 && dropItems(player, MEDUSA_ICHOR, 1, 10, CHANCES.get(npcId)))
					if (player.getInventory().getItemCount(DEAD_SEEKER_DUNG) >= 10 && player.getInventory().getItemCount(TYRANT_BLOOD) >= 10 && player.getInventory().getItemCount(NIGHTSHADE_ROOT) >= 10 && player.getInventory().getItemCount(MARSH_SPIDER_FLUIDS) >= 10)
						st.setCond(7);
				break;
			
			case MARSH_STAKATO, MARSH_STAKATO_WORKER, MARSH_STAKATO_SOLDIER, MARSH_STAKATO_DRONE:
				if (st.getCond() == 6 && dropItems(player, NIGHTSHADE_ROOT, 1, 10, CHANCES.get(npcId)))
					if (player.getInventory().getItemCount(DEAD_SEEKER_DUNG) >= 10 && player.getInventory().getItemCount(TYRANT_BLOOD) >= 10 && player.getInventory().getItemCount(MEDUSA_ICHOR) >= 10 && player.getInventory().getItemCount(MARSH_SPIDER_FLUIDS) >= 10)
						st.setCond(7);
				break;
			
			case MARSH_SPIDER:
				if (st.getCond() == 6 && dropItems(player, MARSH_SPIDER_FLUIDS, 1, 10, CHANCES.get(npcId)))
					if (player.getInventory().getItemCount(DEAD_SEEKER_DUNG) >= 10 && player.getInventory().getItemCount(TYRANT_BLOOD) >= 10 && player.getInventory().getItemCount(MEDUSA_ICHOR) >= 10 && player.getInventory().getItemCount(NIGHTSHADE_ROOT) >= 10)
						st.setCond(7);
				break;
			
			case GRANDIS:
				if (player.getInventory().hasItem(PIXY_GARNET))
					dropItemsAlways(player, GRANDIS_SKULL, 1, 10);
				break;
			
			case LETO_LIZARDMAN_OVERLORD:
				if (player.getInventory().hasItem(PIXY_GARNET))
					dropItemsAlways(player, LETO_OVERLORD_SKULL, 1, 10);
				break;
			
			case BREKA_ORC_OVERLORD:
				if (player.getInventory().hasItem(PIXY_GARNET))
					dropItemsAlways(player, BREKA_OVERLORD_SKULL, 1, 10);
				break;
			
			case KARUL_BUGBEAR:
				if (player.getInventory().hasItem(PIXY_GARNET))
					dropItemsAlways(player, KARUL_BUGBEAR_SKULL, 1, 10);
				break;
			
			case BLACK_WILLOW_LURKER:
				if (player.getInventory().hasItem(BLIGHT_TREANT_SEED))
					dropItemsAlways(player, BLACK_WILLOW_LEAF, 1, 1);
				break;
		}
	}
}